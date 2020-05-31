/*
 *   Copyright 2016 Detlef Gregor Herm
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package de.herm_detlef.java.application;

import com.google.inject.AbstractModule;
import de.herm_detlef.java.application.mvc.controller.app.ApplicationController;
import de.herm_detlef.java.application.mvc.controller.app.ApplicationControllerImpl;
import de.herm_detlef.java.application.mvc.controller.preferences.PreferencesController;
import de.herm_detlef.java.application.mvc.controller.preferences.PreferencesControllerImpl;

public class BasicModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(ApplicationPreferences.class)
                .to(ApplicationPreferencesImpl.class)
                .asEagerSingleton();

        bind(Remote.class)
                .to(RemoteImpl.class)
                .asEagerSingleton();

        bind(CommonData.class)
                .to(CommonDataImpl.class)
                .asEagerSingleton();

        bind(ApplicationController.class)
                .to(ApplicationControllerImpl.class)
                .asEagerSingleton();

        bind(PreferencesController.class)
                .to(PreferencesControllerImpl.class)
                .asEagerSingleton();
    }

}
