package com.hulkdx.moneymanagerv2.injections;

import com.hulkdx.moneymanagerv2.injection.component.ApplicationComponent;
import javax.inject.Singleton;
import dagger.Component;

@Singleton
@Component(modules = ApplicationTestModule.class)
public interface TestComponent extends ApplicationComponent {

}
