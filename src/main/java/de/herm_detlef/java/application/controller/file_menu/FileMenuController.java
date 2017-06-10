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

package de.herm_detlef.java.application.controller.file_menu;


import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

import de.herm_detlef.java.application.CommonData;
import de.herm_detlef.java.application.Remote;
import de.herm_detlef.java.application.controller.filesAndDirectories.FilesAndDirectoriesController;
import de.herm_detlef.java.application.io.ExerciseItemListFactory;
import de.herm_detlef.java.application.model.ExerciseItem;
import de.herm_detlef.java.application.utilities.Utilities;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

/* @formatter:off */

/**
 * TODO
 *
 * @author Detlef G. Herm, 2016-09-26
 * @since 1.0
 *
 */
public class FileMenuController implements Initializable {

    private final static String xmlResourceName            = "FileMenu.fxml";
    private final static String languageResourceBundleName = "FileMenu";

    private final CommonData    commonData;
    private final Remote        remote;

    @FXML
    private MenuItem            menuItemSave;

    @FXML
    private MenuItem            menuItemSaveAs;

    private FileMenuController( CommonData commonData,
                                Remote remote ) {
        this.commonData = commonData;
        this.remote = remote;

        remote.setFileMenuController( this );
    }

    public static FileMenuController create( final MenuBar menuBar, CommonData commonData, Remote remote ) {

        FileMenuController fileMenu = new FileMenuController( commonData, remote );

        Menu root = Utilities.< FileMenuController, Menu >createSceneGraphObjectFromFXMLResource(
            fileMenu,
            FileMenuController.xmlResourceName,
            FileMenuController.languageResourceBundleName,
            commonData );

        menuBar.getMenus().add( root );

        return fileMenu;
    }

    @Override
    public void initialize( URL location, ResourceBundle resources ) {

        menuItemSave.setDisable( true );
        menuItemSaveAs.setDisable( true );

        commonData.getRecentlyOpenedFileProperty().addListener( ( obj, oldValue, newValue ) -> {
            menuItemSave.setDisable( newValue == null );
            menuItemSaveAs.setDisable( newValue == null );
        } );

        commonData.getRecentlySavedFileProperty().addListener( ( obj, oldValue, newValue ) -> {
            menuItemSave.setDisable( newValue == null );
            menuItemSaveAs.setDisable( newValue == null );
        } );

        menuItemSave.setOnAction( this::onSave );
        menuItemSaveAs.setOnAction( this::onSaveAs );
    }

    @FXML
    private void onOpenFileChooser( ActionEvent event ) {

        boolean needsSave = remote.getApplicationController().checkExerciseItemListMasterNeedsSave();
        if ( needsSave ) {
            event.consume();
            return;
        }

        final FilesAndDirectoriesController fdCtrl = new FilesAndDirectoriesController();

        final File selectedFile = fdCtrl.openFileChooserDialog( commonData.getPrimaryStage(), commonData );
        if ( selectedFile == null ) return;

        ArrayList< ExerciseItem > list = null;

        try {
            if ( commonData.isEditingMode() ) {
                commonData.removeExerciseItemListMasterChangeListener();
            }

            list = ExerciseItemListFactory.importExerciseItemListFromFile( selectedFile.getAbsolutePath() );
            if ( list == null ) {
                menuItemSave.setDisable( true );
                menuItemSaveAs.setDisable( true );
                return;
            }

        } finally {
            if ( commonData.isEditingMode() ) {
                commonData.addExerciseItemListMasterChangeListener();
            }
        }

        commonData.setCurrentExerciseItem( null );

        commonData.getExerciseItemListMaster().clear();
        commonData.getExerciseItemListMaster().addAll( list );

        commonData.getExerciseItemListInitialMaster().clear();
        commonData.getExerciseItemListInitialMaster().addAll( commonData.getExerciseItemListMaster() );

        commonData.getExerciseItemListShuffledSubset().clear();
        commonData.getExerciseItemListShuffledSubset().addAll( commonData.getExerciseItemListMaster() );

        if ( commonData.isShuffledSubset() ) {
            Collections.shuffle( commonData.getExerciseItemListShuffledSubset() );
        // ); // TODO preferences
            int size   = commonData.getExerciseItemListShuffledSubset().size();
            int maxLen = commonData.getMaxLengthOfSubset();
            if ( size > maxLen ) {
                commonData.getExerciseItemListShuffledSubset().remove( maxLen, size );
            }
        }

        remote.getExerciseMenuController().getCheckMenuItemEditExercise().setDisable( false );

        remote.getApplicationController().updateView();
    }

    private void onSave( ActionEvent event ) {

        if ( ! commonData.checkNeedsSave() )
            return;

        if ( commonData.getRecentlyOpenedFileProperty().get() == null )
            return;

        File file = commonData.getRecentlyOpenedFileProperty().get();

        if ( commonData.getRecentlySavedFileProperty().get() != null ) {
            file = commonData.getRecentlySavedFileProperty().get();
        }

        ExerciseItemListFactory.exportExerciseItemListToFile(
            commonData,
            file );

        remote.getApplicationController().updateView();
    }

    private void onSaveAs( ActionEvent event ) {

        final FilesAndDirectoriesController fdCtrl = new FilesAndDirectoriesController();

        final File saveToFile = fdCtrl.openFileSaveDialog( commonData.getPrimaryStage(), commonData );

        if ( saveToFile == null ) return;

        ExerciseItemListFactory.exportExerciseItemListToFile(
            commonData,
            saveToFile );

        remote.getApplicationController().updateView();
    }

    public MenuItem getMenuItemSave() {

        return menuItemSave;
    }

    public MenuItem getMenuItemSaveAs() {

        return menuItemSaveAs;
    }
}
