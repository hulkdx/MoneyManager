package com.hulkdx.moneymanager.injections;

import android.content.Context;
import com.hulkdx.moneymanager.HulkApplication;
import com.hulkdx.moneymanager.data.DataManager;
import com.hulkdx.moneymanager.data.local.PreferencesHelper;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;


public class TestComponentRule implements TestRule {

    private final TestComponent mTestComponent;
    private final Context mContext;

    public TestComponentRule(Context context) {
        mContext = context;
        HulkApplication application = HulkApplication.get(context);
        mTestComponent = DaggerTestComponent.builder()
                .applicationTestModule(new ApplicationTestModule(application))
                .build();
    }

    public Context getContext() {
        return mContext;
    }

    public DataManager getMockDataManager() {
        return mTestComponent.dataManager();
    }

    public PreferencesHelper getMockPreferencesHelper() {
        return mTestComponent.preferencesHelper();
    }

    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                HulkApplication application = HulkApplication.get(mContext);
                application.setComponent(mTestComponent);
                base.evaluate();
                application.setComponent(null);
            }
        };
    }
}
