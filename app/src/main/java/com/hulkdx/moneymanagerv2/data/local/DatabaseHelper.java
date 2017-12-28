/**
 * Created by Mohammad Jafarzadeh Rezvan on 7/10/2017.
 */
package com.hulkdx.moneymanagerv2.data.local;

import com.hulkdx.moneymanagerv2.data.model.Category;
import com.hulkdx.moneymanagerv2.data.model.Transaction;
import com.hulkdx.moneymanagerv2.data.model.TransactionResponse;
import com.hulkdx.moneymanagerv2.util.RxUtil;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;
import timber.log.Timber;

@Singleton
public class DatabaseHelper {

    private final static BackpressureStrategy DEFAULT_STRATEGY = BackpressureStrategy.LATEST;
    public enum Transaction_Fields { DATE, CATEGORY, AMOUNT, ATTACHMENT }
    private final Provider<Realm> mRealmProvider;

    @Inject
    public DatabaseHelper(Provider<Realm> realmProvider) {
        mRealmProvider = realmProvider;
    }

    // Helper Function to remove all data
    public void removeAllTransactions() {
        Realm realm = mRealmProvider.get();
        realm.executeTransaction(realm1 -> realm1.deleteAll());
    }

    /************************* Transactions Section *************************/
    public Flowable<List<Transaction>> getTransactions() {
        Realm realm = mRealmProvider.get();
        RealmResults<Transaction> results = realm.where(Transaction.class).
                findAllSortedAsync(new String[] {"date", "id"} ,
                        new Sort[] {Sort.DESCENDING, Sort.DESCENDING});
        
        return RxUtil.createFlowableFromRealmResult(realm, results)
                .filter(RealmResults::isLoaded)
                .map(transactions -> transactions);
    }
    /*
     * Add a new Transaction into database.
     * @param newTransaction : the new transaction to be added in database.
     * @param categoryId : the id of selected category. -1 means category is not selected.
     */
    public Flowable<Transaction> addTransaction(final Transaction newTransaction,
                                                final long categoryId) {

        return Flowable.create(subscriber -> {
            Realm realm = null;
            try {
                realm = mRealmProvider.get();
                realm.executeTransactionAsync(
                        bgRealm -> {
                            // If the id equals to zero that means the id is not set and
                            // it is not synced ( the id is not from the api).
                            if (newTransaction.getId() == 0) {
                                // Auto Incremental Id
                                Number currentIdNum = bgRealm.where(Transaction.class).max("id");
                                int nextId = currentIdNum == null ? 1 : currentIdNum.intValue() + 1;
                                newTransaction.setId(nextId);
                            }
                            if (categoryId != -1) {
                                Category c = bgRealm.where(Category.class)
                                                    .equalTo("id", categoryId).findFirst();
                                newTransaction.setCategory(c);
                            }
                            bgRealm.copyToRealm(newTransaction);
                        },
                        subscriber::onComplete,
                        subscriber::onError);
            } finally {
                if (realm != null) {
                    realm.close();
                }
            }
        }, DEFAULT_STRATEGY);
    }
    /**
     * Search the db for the specify date.
     * @param day, @param months, @param year is the date to search.
     * @param isDailyOrMonthlyOrYearly: 0 -> daily, 1 -> Monthly, 2 -> yearly.
     */
    public Flowable<List<Transaction>> searchTransactionWithDate(int day, int month, int year,
                                                                 int isDailyOrMonthlyOrYearly) {
        Realm realm = mRealmProvider.get();

        RealmQuery<Transaction> query = realm.where(Transaction.class);

        if (isDailyOrMonthlyOrYearly == 2) {
            query.beginsWith("date", year + "-");
        } else if (isDailyOrMonthlyOrYearly == 1) {
            query.beginsWith("date", String.format(Locale.ENGLISH, "%d-%02d", year, month));
        } else if (isDailyOrMonthlyOrYearly == 0) {
            query.equalTo("date", String.format(Locale.ENGLISH, "%d-%02d-%02d", year, month, day));
        }

        RealmResults<Transaction> results = query.findAllAsync();
        return RxUtil.createFlowableFromRealmResult(realm, results)
                .filter(RealmResults::isLoaded)
                .map(transactions -> transactions);
    }
    /**
     * Add a list of Transactions from the api into database.
     * @param response : @link TransactionResponse from DataManager:syncTransactions
     */
    public Flowable<TransactionResponse> addTransactions(TransactionResponse response) {
        return Flowable.create(subscriber -> {
            Realm realm = null;
            try {
                realm = mRealmProvider.get();
                realm.executeTransactionAsync(
                        bgRealm -> {
                            bgRealm.copyToRealmOrUpdate(response.getResponse());
                        },
                        subscriber::onComplete,
                        subscriber::onError);
            } finally {
                subscriber.onNext(response);
                if (realm != null) {
                    realm.close();
                }
            }
        }, DEFAULT_STRATEGY);
    }

    public Flowable<TransactionResponse> removeTransactions(long[] selectedIds,
                                                            boolean isSync) {
        return Flowable.create(subscriber -> {
            Timber.i("DatabaseHelper:removeTransactions");
            Realm realm = null;
            try {
                realm = mRealmProvider.get();

                realm.executeTransactionAsync(
                        bgRealm -> {
                            RealmQuery<Transaction> query = bgRealm.where(Transaction.class);

                            for (int i = 0; i < selectedIds.length; i++) {
                                if (i > 0) {
                                    query.or();
                                }
                                query.equalTo("id", selectedIds[i]);
                            }
                            boolean isDeletedAll = query.findAll().deleteAllFromRealm();

                            if (isDeletedAll) {

                                // Calculate the amount manually for the not sync.
                                TransactionResponse response = new TransactionResponse();
                                if (!isSync) {
                                    RealmResults<Transaction> queryResponse =
                                            bgRealm.where(Transaction.class).findAll();
                                    float count = 0;
                                    for (Transaction t : queryResponse) {
                                        count += t.getAmount();
                                    }
                                    response.setAmountCount(count);
                                    subscriber.onNext(response);
                                } else {
                                    subscriber.onNext(response);
                                }


                            } else {
                                subscriber.onError(
                                        new Throwable("could not delete all transactions"));
                            }
                        },
                        () -> {},
                        subscriber::onError);
            } finally {
                if (realm != null) {
                    realm.close();
                }
            }
        }, DEFAULT_STRATEGY);
    }

    /**
     *
     * @param transactionId : The id to be updated
     * @param keys Field names
     * @param values values to be updated
     */
    public Flowable<Object> updateTransaction(long transactionId,
                                              Transaction_Fields[] keys,
                                              Object[] values) {
        return Flowable.create(subscriber -> {
            Realm realm = null;
            try {
                realm = mRealmProvider.get();

                realm.executeTransactionAsync(
                        bgRealm -> {
                            Transaction transaction = bgRealm.where(Transaction.class).equalTo("id", transactionId).findFirst();
                            if (transaction == null) {
                                subscriber.onError(new Throwable("cannot update transaction, its not on db"));
                                return;
                            }
                            for (int i=0, len=keys.length; i<len; i++) {
                                switch (keys[i]) {
                                    case DATE:
                                        transaction.setDate((String) values[i]);
                                        break;
                                    case CATEGORY:
                                        transaction.setCategory((Category) values[i]);
                                        break;
                                    case AMOUNT:
                                        transaction.setAmount((float) values[i]);
                                        break;
                                    case ATTACHMENT:
                                        transaction.setAttachment((String) values[i]);
                                        break;
                                    default:
                                        subscriber.onError(new Throwable("unsupported transaction format"));
                                        return;
                                }
                            }
                            bgRealm.copyToRealmOrUpdate(transaction);
                        },
                        subscriber::onComplete,
                        subscriber::onError);
            } finally {

                if (realm != null) {
                    realm.close();
                }
            }
        }, DEFAULT_STRATEGY);
    }

    /************************* Category Section *************************/
    /*
     * Get all categories from db.
     */
    public Flowable<List<Category>> getCategories() {
        Realm realm = mRealmProvider.get();
        RealmResults<Category> results = realm.where(Category.class).findAllAsync();
        return RxUtil.createFlowableFromRealmResult(realm, results)
                .filter(RealmResults::isLoaded)
                .map(category -> category);
    }
    /*
     * Add a new Category into database.
     * @param newCategory : the new category to be added in database.
     */
    public Flowable<Category> addCategory(final Category newCategory) {
        return Flowable.create(subscriber -> {
            Realm realm = null;
            try {
                realm = mRealmProvider.get();
                realm.executeTransactionAsync(
                        bgRealm -> {
                            if (newCategory.getId() == 0) {
                                // Auto Incremental Id
                                Number currentIdNum = bgRealm.where(Category.class).max("id");
                                int nextId = currentIdNum == null ? 1 : currentIdNum.intValue() + 1;
                                newCategory.setId(nextId);
                            }
                            bgRealm.insert(newCategory);
                        },
                        subscriber::onComplete,
                        subscriber::onError);
            } finally {
                if (realm != null) {
                    realm.close();
                }
            }
        }, DEFAULT_STRATEGY);
    }
    /*
     * Add a list of categories from the api into database.
     * @param categories : the list of categories
     */
    public Flowable<List<Category>> addCategories(List<Category> categories) {
        return Flowable.create(subscriber -> {
            Realm realm = null;
            try {
                realm = mRealmProvider.get();
                realm.executeTransactionAsync(
                        bgRealm -> {
                            bgRealm.copyToRealmOrUpdate(categories);
                        },
                        subscriber::onComplete,
                        subscriber::onError);
            } finally {
                if (realm != null) {
                    realm.close();
                }
            }
        }, DEFAULT_STRATEGY);
    }
}
