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

package de.herm_detlef.java.application.mvc.controller.preferences;


import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import com.google.inject.Inject;
import de.herm_detlef.java.application.*;
import de.herm_detlef.java.application.utilities.Utilities;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import javax.inject.Named;

import static de.herm_detlef.java.application.ApplicationConstants.*;
import static de.herm_detlef.java.application.ControllerNames.PREFERENCES;
import static de.herm_detlef.java.application.ViewResourcesPath.PREFERENCES_FXML;
import static de.herm_detlef.java.application.ViewResourcesPath.PREFERENCES_RESOURCE_BUNDLE;
import static javafx.stage.Modality.APPLICATION_MODAL;

/* @formatter:off */

/**
 * TODO
 * @author Detlef G. Herm, 2016-09-26
 * @since 1.0
 *
 */
public class PreferencesControllerImpl implements PreferencesController, Initializable {

    private final Stage stage;
    private final CommonData commonData;

    @Inject
    private final ApplicationPreferences applicationPreferences;

    @FXML
    private Parent root;

    @FXML
    private ChoiceBox<Locale> languageChoice;
    @FXML
    private CheckBox selectRandomly;
    @FXML
    private TextField maxNumberOfItems;


    @Inject
    public PreferencesControllerImpl(@Named(PREFERENCES) Stage stage, CommonData commonData, Remote remote) {

        this.commonData = commonData;
        this.stage = stage;

        remote.setPreferencesController(this);

        applicationPreferences = commonData.getApplicationPreferences();
    }

    public void openDialog() {

        if ( commonData.getPrimaryStage() == null ) {
            assert false;
            return;
        }

        stage.setTitle(TITLE_OF_DIALOG_PREFERENCES);

        Parent root = Utilities.createSceneGraphObjectFromFXMLResource(
                this,
                PREFERENCES_FXML.path(),
                PREFERENCES_RESOURCE_BUNDLE.path(),
                commonData);

        if (DEBUG) assert root != null;
        if (root == null) return;

        final Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.sizeToScene();
        stage.initOwner(commonData.getPrimaryStage());
        stage.initModality(APPLICATION_MODAL);

        applicationPreferences.setStagePositionAndSizeBasedOnUserPreferences(
                stage,
                "PreferencesStageOriginX",
                "PreferencesStageOriginY",
                "PreferencesStageWidth",
                "PreferencesStageHeight");

        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        languageChoice.getItems().addAll(
                Locale.US,
                Locale.GERMANY);

        languageChoice.setValue( commonData.getCurrentLocale() );

        languageChoice.setConverter(
                new StringConverter<Locale>() {

                    @Override
                    public String toString(Locale object) {

                        return object.getDisplayLanguage( commonData.getCurrentLocale() );
                    }

                    @Override
                    public Locale fromString(String string) {
                        return null;
                    }
                });

        languageChoice.valueProperty().addListener(
                (obj, oldValue, newValue) -> {
                    commonData.setCurrentLocale(newValue);
                    USER_PREFERENCES_NODE.put(
                            "Locale",
                            newValue.toString());
                    reloadFXML();
                });

        applicationPreferences.saveStagePositionAndSizeToUserPreferences(
                stage,
                "PreferencesStageOriginX",
                "PreferencesStageOriginY",
                "PreferencesStageWidth",
                "PreferencesStageHeight");

        selectRandomly.setSelected(commonData.isShuffledSubset());

        maxNumberOfItems.setDisable(!commonData.isShuffledSubset());

        selectRandomly.selectedProperty().addListener((obj, oldValue, newValue) -> {
            commonData.setShuffledSubset(newValue);
            USER_PREFERENCES_NODE.put("RandomOrder", newValue.toString());
            maxNumberOfItems.setDisable(!newValue);
        });

        maxNumberOfItems.setText(Integer.toString(commonData.getMaxLengthOfSubset()));

        maxNumberOfItems.addEventFilter(KeyEvent.KEY_TYPED, keyEvent -> {
            String digits = "0123456789";
            if (!digits.contains(keyEvent.getCharacter()))
                keyEvent.consume();
            if (maxNumberOfItems.getText().isEmpty())
                return;
            if (maxNumberOfItems.getText().length() > 2)
                keyEvent.consume();
        });

        maxNumberOfItems.textProperty().addListener((obj, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                commonData.setMaxLengthOfSubset(Integer.parseInt(newValue));
                USER_PREFERENCES_NODE.put( "MaximumLengthOfSubset", newValue );
            }
        });
    }

    @FXML
    void onLanguageChoice(ActionEvent event) {

        if ( languageChoice.getValue() == null ) {
            commonData.setCurrentLocale( languageChoice.getValue() );
        }
    }

    private void reloadFXML() {

        stage.setTitle(TITLE_OF_DIALOG_PREFERENCES);

        Parent root = Utilities.createSceneGraphObjectFromFXMLResource(
                this,
                PREFERENCES_FXML.path(),
                PREFERENCES_RESOURCE_BUNDLE.path(),
                commonData);

        if (DEBUG) assert root != null;
        if (root == null) return;

        Scene scene = new Scene(
                root,
                stage.getScene().getWidth(),
                stage.getScene().getHeight());

        // change scene
        stage.setScene(scene);
    }
}
