/*
 *  Copyright 2016 Detlef Gregor Herm
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package de.herm_detlef.java.application.mvc.controller.app_menu;


import java.util.Objects;

import de.herm_detlef.java.application.CommonData;
import de.herm_detlef.java.application.Remote;
import de.herm_detlef.java.application.mvc.controller.about.AboutController;
import de.herm_detlef.java.application.mvc.controller.preferences.PreferencesController;
import de.herm_detlef.java.application.utilities.Utilities;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;

import javax.inject.Inject;
import javax.inject.Singleton;

import static de.herm_detlef.java.application.mvc.view.ViewResourcesPath.APP_MENU_FXML;
import static de.herm_detlef.java.application.mvc.view.ViewResourcesPath.APP_MENU_RESOURCE_BUNDLE;

/* @formatter:off */

/**
 * TODO
 * @author Detlef G. Herm, 2016-09-26
 * @since 1.0
 *
 */
@Singleton
class AppMenuControllerImpl implements AppMenuController {

    private final CommonData commonData;
    private final Remote     remote;

    @Inject
    private AppMenuControllerImpl(CommonData commonData,
                                  Remote remote ) {

        this.commonData = commonData;
        this.remote = remote;

        remote.setAppMenuController( this );
    }

    @Override
    public Menu create(CommonData commonData, Remote remote) {

        Menu root = Utilities.createSceneGraphObjectFromFXMLResource(
                this,
                APP_MENU_FXML.path(),
                APP_MENU_RESOURCE_BUNDLE.path(),
                commonData );

        return Objects.requireNonNull(root);
    }

    @FXML
    private void initialize() {

    }

    @FXML
    private void onShowAbout( ActionEvent event ) {

        AboutController ac = new AboutController();
        ac.showAbout(
            commonData.getPrimaryStage() );
    }

    @FXML
    private void onOpenPreferences( ActionEvent event ) {

        PreferencesController pc = remote.getPreferencesController();

        pc.openDialog();
    }

    @FXML
    private void onQuit( ActionEvent event ) {
        quit();
    }

    @Override
    public void quit() {

        boolean needsSave = remote.getApplicationController().checkExerciseItemListMasterNeedsSave();

        if ( needsSave )
            return;

        Platform.exit();
    }
}
