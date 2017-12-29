package com.hulkdx.moneymanagerv2;


import com.hulkdx.moneymanagerv2.data.model.requests.UpdateTransactionRequest;
import com.hulkdx.moneymanagerv2.data.remote.HulkService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Mohammad Jafarzadeh Rezvan on 29/12/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class ApiTest {

    private static final String token = "JWT eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6ImFkbWluIiwidXNlcl9pZCI6MSwiZW1haWwiOiJhZG1pbkBhZG1pbi5jb20iLCJleHAiOjE1MTk2NjczNTB9.lEhpmLNytqn-Qu8ss_1ia7mlTS1f_Berb5bHjK6wmFg";
    private final Object LOCK = new Object();
    private HulkService mHulkService;
//    private DataManager mDataManager;
//    @Mock private PreferencesHelper mMockPreferencesHelper;
//    @Mock private DatabaseHelper mDatabaseHelper;

    @Before
    public void SetUp() {
        mHulkService = HulkService.Creator.newService();
//        mDataManager = new DataManager(mMockPreferencesHelper, mDatabaseHelper, mHulkService);
//        when(mMockPreferencesHelper.isSync())
//                .thenReturn(true);
    }

    @Test
    public void UpdateTransactionTest() throws InterruptedException {

        List<HashMap<String, Object>> list = new ArrayList<>();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("amount", 666);
        list.add(hashMap);
        UpdateTransactionRequest request = new UpdateTransactionRequest(177, list);

        mHulkService.updateTransaction(
                token,
                request)
                .subscribeOn(Schedulers.io())
                .subscribe(
                        o -> {
                            System.out.println("onNext");
                        },
                        throwable -> {
                            System.out.println("error"+ throwable.getMessage());
                            synchronized (LOCK) {
                                LOCK.notify();
                            }
                        } ,
                        () -> {
                            System.out.println("comp");
                            synchronized (LOCK) {
                                LOCK.notify();
                            }
                        }
                );

        synchronized (LOCK) {
            LOCK.wait();
        }
    }
}
