package com.hulkdx.moneymanager.Injections;

import com.hulkdx.moneymanager.injection.component.ApplicationComponent;
import javax.inject.Singleton;
import dagger.Component;

@Singleton
@Component(modules = ApplicationTestModule.class)
public interface TestComponent extends ApplicationComponent {

}
