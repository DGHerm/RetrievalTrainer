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

package de.herm_detlef.java.application.controller.preferences;


import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import de.herm_detlef.java.application.ApplicationConstants;
import de.herm_detlef.java.application.ApplicationPreferences;
import de.herm_detlef.java.application.CommonData;
import de.herm_detlef.java.application.Remote;
import de.herm_detlef.java.application.utilities.Utilities;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/* @formatter:off */

/**
 * TODO
 * @author Detlef G. Herm, 2016-09-26
 * @since 1.0
 *
 */
public class PreferencesController implements Initializable {

    private final ApplicationPreferences applicationPreferences;
    private final Stage                  stage;
    private final CommonData             commonData;
    private final Remote                 remote;

    @FXML
    private Parent                       root;

    @FXML
    private ChoiceBox< Locale >          languageChoice;

    public PreferencesController( CommonData commonData,
                                  Remote remote ) {

        this.commonData = commonData;
        this.remote = remote;
        this.applicationPreferences = commonData.getApplicationPreferences();
        stage = new Stage();
    }

    public void openDialog() {

        if ( commonData.getPrimaryStage() == null ) {
            assert false;
            return;
        }

        stage.setTitle(
            ApplicationConstants.TITLE_OF_DIALOG_PREFERENCES );

        Parent root = Utilities.< PreferencesController, Parent >createSceneGraphObjectFromFXMLResource(
            this,
            "Preferences.fxml",
            "Preferences",
            commonData );

        final Scene scene = new Scene( root,
                                       500,
                                       500 );
        // scene.getStylesheets().add(getClass().getResource("/de/herm_detlef/java/application/preferences/preferences.css").toExternalForm());
        stage.setScene(
            scene );
        stage.setResizable(
            false );
        stage.centerOnScreen();
        stage.sizeToScene();
        stage.initOwner(
            commonData.getPrimaryStage() );
        stage.initModality(
            Modality.APPLICATION_MODAL );

        commonData.getApplicationPreferences().setStagePositionAndSizeBasedOnUserPreferences(
            stage,
            "PreferencesStageOriginX",
            "PreferencesStageOriginY",
            "PreferencesStageWidth",
            "PreferencesStageHeight" );

        stage.show();
    }

    @Override
    public void initialize( URL location, ResourceBundle resources ) {

        languageChoice.getItems().addAll(
            Locale.US,
            Locale.GERMANY );

        languageChoice.setValue(
            commonData.getCurrentLocale() );

        languageChoice.setConverter(
            new StringConverter< Locale >() {

                @Override
                public String toString( Locale object ) {

                    return object.getDisplayLanguage(
                        commonData.getCurrentLocale() );
                }

                @Override
                public Locale fromString( String string ) {

                    return null;
                }
            } );

        languageChoice.valueProperty().addListener(
            ( obj, oldValue, newValue ) -> {
                commonData.setCurrentLocale(
                    newValue );
                commonData.getApplicationPreferences().getUserPreferencesNode().put(
                    "Locale",
                    newValue.toString() );
                reloadFXML();
            } );

        commonData.getApplicationPreferences().saveStagePositionAndSizeToUserPreferences(
            stage,
            "PreferencesStageOriginX",
            "PreferencesStageOriginY",
            "PreferencesStageWidth",
            "PreferencesStageHeight" );
    }

    @FXML
    void onLanguageChoice( ActionEvent event ) {

        if ( languageChoice.getValue() == null ) {
            commonData.setCurrentLocale(
                languageChoice.getValue() );
        }
    }

    private void reloadFXML() {

        stage.setTitle(
            ApplicationConstants.TITLE_OF_DIALOG_PREFERENCES );

        Parent root = Utilities.< PreferencesController, Parent >createSceneGraphObjectFromFXMLResource(
            this,
            "Preferences.fxml",
            "Preferences",
            commonData );

        Scene scene = new Scene( root,
                                 stage.getScene().getWidth(),
                                 stage.getScene().getHeight() );
        // scene.getStylesheets().add(getClass().getResource("/de/herm_detlef/java/application/preferences/preferences.css").toExternalForm());

        // change scene
        stage.setScene(
            scene );
    }
}
