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

package de.herm_detlef.java.application.mvc.controller.filesAndDirectories;


import java.io.File;

import de.herm_detlef.java.application.ApplicationConstants;
import de.herm_detlef.java.application.CommonData;
import de.herm_detlef.java.application.utilities.Utilities;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

import javax.inject.Singleton;

import static de.herm_detlef.java.application.ApplicationConstants.DEBUG;
import static de.herm_detlef.java.application.ViewResourcesPath.FILES_AND_DIRECTORIES_FXML;

/* @formatter:off */

/**
 * TODO
 * @author Detlef G. Herm, 2016-09-26
 * @since 1.0
 *
 */
@Singleton
class FilesAndDirectoriesControllerImpl implements FilesAndDirectoriesController {

    @Override
    public File openFileChooserDialog(final Stage primaryStage, CommonData commonData) {

        final Stage stage = new Stage();
        stage.setTitle( ApplicationConstants.TITLE_OF_DIALOG_FILE_CHOOSER_OPEN );

        prepareStageForFileChooser( primaryStage, commonData, stage );

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle( ApplicationConstants.TITLE_OF_DIALOG_FILE_CHOOSER_OPEN );
        ExtensionFilter filterXml = new ExtensionFilter("Text Files (*.xml)", "*.xml");
        ExtensionFilter filterTxt = new ExtensionFilter("Text Files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().addAll( filterXml, filterTxt, new ExtensionFilter("All Files", "*.*") );
        fileChooser.setSelectedExtensionFilter( filterXml );

        stage.show();

        File selectedFile = fileChooser.showOpenDialog( stage );

        stage.close();

        if ( selectedFile != null ) {
            commonData.setRecentlyOpenedFile( selectedFile );
        }

        return selectedFile;
    }

    private void prepareStageForFileChooser(Stage primaryStage, CommonData commonData, Stage stage) {
        Parent root = Utilities.createSceneGraphObjectFromFXMLResource(
            this,
                FILES_AND_DIRECTORIES_FXML.path(),
            null,
            commonData );

        if (DEBUG) assert root != null;
        if ( root == null ) return;

        final Scene scene = new Scene( root,712,0 );
        stage.setScene( scene );
        stage.setResizable( false );
        stage.centerOnScreen();
        stage.sizeToScene();
        stage.initOwner( primaryStage );
        stage.initModality( Modality.APPLICATION_MODAL );
    }

    @Override
    public File openFileSaveDialog(final Stage primaryStage, final CommonData commonData) {

        final Stage stage = new Stage();
        stage.setTitle( ApplicationConstants.TITLE_OF_DIALOG_FILE_CHOOSER_SAVE );

        prepareStageForFileChooser( primaryStage, commonData, stage );

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(ApplicationConstants.TITLE_OF_DIALOG_FILE_CHOOSER_SAVE);
        ExtensionFilter filter = new ExtensionFilter("Text Files (*.xml)", "*.xml");
        fileChooser.setSelectedExtensionFilter(filter);
        fileChooser.setInitialFileName("untitled.xml");

        stage.show();

        File saveToFile = fileChooser.showSaveDialog( stage );

        stage.close();

        if ( saveToFile != null ) {
            commonData.setRecentlySavedFile( saveToFile );
        }

        return saveToFile;
    }
}
