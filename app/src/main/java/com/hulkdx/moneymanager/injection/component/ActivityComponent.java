/**
 * Created by Mohammad Jafarzadeh Rezvan on 7/6/2017.
 */

package com.hulkdx.moneymanager.injection.component;

import dagger.Subcomponent;
import com.hulkdx.moneymanager.injection.PerActivity;
import com.hulkdx.moneymanager.injection.module.ActivityModule;
import com.hulkdx.moneymanager.ui.chooser.ChooserActivity;
import com.hulkdx.moneymanager.ui.login.LoginActivity;
import com.hulkdx.moneymanager.ui.login_sync.LoginSyncActivity;
import com.hulkdx.moneymanager.ui.main.MainActivity;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(LoginActivity loginActivity);

    void inject(MainActivity mainActivity);

    void inject(LoginSyncActivity loginSyncActivity);

    void inject(ChooserActivity chooserActivity);
}
