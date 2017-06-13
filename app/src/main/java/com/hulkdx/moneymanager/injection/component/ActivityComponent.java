package com.hulkdx.moneymanager.injection.component;

import dagger.Subcomponent;
import com.hulkdx.moneymanager.injection.PerActivity;
import com.hulkdx.moneymanager.injection.module.ActivityModule;
import com.hulkdx.moneymanager.ui.main.MainActivity;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);

}
