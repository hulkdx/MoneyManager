/**
 * Created by Mohammad Jafarzadeh Rezvan on 7/10/2017.
 */
package com.hulkdx.moneymanagerv2.data.local;

import com.hulkdx.moneymanagerv2.data.model.Category;
import com.hulkdx.moneymanagerv2.data.model.Transaction;
import com.hulkdx.moneymanagerv2.data.model.TransactionResponse;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import io.reactivex.Flowable;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

@Singleton
public class DatabaseHelper extends RealmHelper {

    // TODO try to change this enum to String[] or get fields by reflection: {@link: https://stackoverflow.com/questions/2989560/how-to-get-the-fields-in-an-object-via-reflection}
    public enum Transaction_Fields { DATE, CATEGORY, AMOUNT, ATTACHMENT }

    @Inject
    public DatabaseHelper(Provider<Realm> realmProvider) {
        super(realmProvider);
    }

    // Helper Function to remove all data
    public void removeAllTransactions() {
        Realm realm = mRealmProvider.get();
        realm.executeTransaction(realm1 -> realm1.deleteAll());
    }

    /************************* Transactions Section *************************/
    public Flowable<List<Transaction>> getTransactions() {
        return getData(Transaction.class,
                       realmQuery -> realmQuery.sort(new String[] {"date", "id"} ,
                                                     new Sort[] {Sort.DESCENDING, Sort.DESCENDING})
                                                .findAllAsync());
    }
    /**
     * Add a new Transaction into database.
     * @param newTransaction : the new transaction to be added in database.
     * @param categoryId : the id of selected category. -1 means category is not selected.
     */
    public Flowable<Transaction> addTransaction(final Transaction newTransaction,
                                                final long categoryId) {
        return executeTransactionAsync((bgRealm, subscriber) -> {
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
        });
    }
    /**
     * Search the db for the specify date.
     * @param day, @param months, @param year is the date to search.
     * @param isDailyOrMonthlyOrYearly: 0 -> daily, 1 -> Monthly, 2 -> yearly.
     */
    public Flowable<List<Transaction>> searchTransactionWithDate(int day, int month, int year,
                                                                 int isDailyOrMonthlyOrYearly) {

        return getData(Transaction.class, query -> {
            if (isDailyOrMonthlyOrYearly == 2) {
                query.beginsWith("date", year + "-");
            } else if (isDailyOrMonthlyOrYearly == 1) {
                query.beginsWith("date", String.format(Locale.ENGLISH, "%d-%02d", year, month));
            } else if (isDailyOrMonthlyOrYearly == 0) {
                query.equalTo("date", String.format(Locale.ENGLISH, "%d-%02d-%02d", year, month, day));
            }
            return query.findAllAsync();
        });
    }
    /**
     * Add a list of Transactions from the api into database.
     * @param response : @link TransactionResponse from DataManager:syncTransactions
     */
    public Flowable<TransactionResponse> addTransactions(TransactionResponse response) {
        return executeTransactionAsync((bgRealm, subscriber) -> {
            bgRealm.copyToRealmOrUpdate(response.getResponse());
            subscriber.onNext(response);
        });
    }

    public Flowable<TransactionResponse> removeTransactions(long[] selectedIds, boolean isSync) {
        return executeTransactionAsync(false, true, (bgRealm, subscriber) ->
        {
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
                subscriber.onError(new Exception("could not delete all transactions"));
            }
        });
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

        return executeTransactionAsync((bgRealm, subscriber) -> {
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
        });
    }

    /************************* Category Section *************************/
    /*
     * Get all categories from db.
     */
    public Flowable<List<Category>> getCategories() {
        return findAllAsyncData(Category.class);
    }
    /*
     * Add a new Category into database.
     * @param newCategory : the new category to be added in database.
     */
    public Flowable<Category> addCategory(final Category newCategory) {
        return executeTransactionAsync((bgRealm, subscriber) -> {
            if (newCategory.getId() == 0) {
                // Auto Incremental Id
                Number currentIdNum = bgRealm.where(Category.class).max("id");
                int nextId = currentIdNum == null ? 1 : currentIdNum.intValue() + 1;
                newCategory.setId(nextId);
            }
            bgRealm.insert(newCategory);
        });
    }
    /*
     * Add a list of categories from the api into database.
     * @param categories : the list of categories
     */
    public Flowable<List<Category>> addCategories(List<Category> categories) {
        return executeTransactionAsync((bgRealm, subscriber) -> {
            bgRealm.copyToRealmOrUpdate(categories);
        });
    }
}
