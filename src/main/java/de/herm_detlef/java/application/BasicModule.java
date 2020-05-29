package de.herm_detlef.java.application;

import com.google.inject.AbstractModule;
import de.herm_detlef.java.application.mvc.controller.app.ApplicationController;
import de.herm_detlef.java.application.mvc.controller.app.ApplicationControllerImpl;

public class BasicModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(ApplicationPreferences.class).to(ApplicationPreferencesImpl.class).asEagerSingleton();
        bind(Remote.class).to(RemoteImpl.class).asEagerSingleton();
        bind(CommonData.class).to(CommonDataImpl.class).asEagerSingleton();
        bind(ApplicationController.class).to(ApplicationControllerImpl.class).asEagerSingleton();
    }

}
