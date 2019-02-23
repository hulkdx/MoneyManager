/**
 * Created by Mohammad Jafarzadeh Rezvan on 7/6/2017.
 */

package com.hulkdx.moneymanagerv2.injection.component;

import com.hulkdx.moneymanagerv2.injection.PerActivity;
import com.hulkdx.moneymanagerv2.injection.module.ActivityModule;
import com.hulkdx.moneymanagerv2.ui.chooser.ChooserActivity;
import com.hulkdx.moneymanagerv2.ui.login.LoginActivity;
import com.hulkdx.moneymanagerv2.ui.loginsync.LoginSyncActivity;
import com.hulkdx.moneymanagerv2.ui.main.MainActivity;
import com.hulkdx.moneymanagerv2.ui.register.RegisterActivity;

import dagger.Subcomponent;

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

    void inject(RegisterActivity registerActivity);
}
