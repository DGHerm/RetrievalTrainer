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

package de.herm_detlef.java.application.controller.exerciseitemlist_menu;


import java.net.URL;
import java.util.ResourceBundle;

import de.herm_detlef.java.application.CommonData;
import de.herm_detlef.java.application.Remote;
import de.herm_detlef.java.application.utilities.Utilities;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckMenuItem;
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
public class ExerciseItemListMenuController implements Initializable {

    private final CommonData commonData;
    private final Remote     remote;

    private final BooleanProperty revertUncheck = new SimpleBooleanProperty();

    @FXML
    private MenuItem         menuItemNewExerciseItemList;

    @FXML
    private CheckMenuItem    checkMenuItemEditExercise;

    private ExerciseItemListMenuController( CommonData commonData,
                                            Remote remote ) {

        this.commonData = commonData;
        this.remote = remote;

        remote.setExerciseMenuController( this );
    }

    public static ExerciseItemListMenuController create( final MenuBar menuBar, CommonData commonData, Remote remote ) {

        ExerciseItemListMenuController exerciseMenu = new ExerciseItemListMenuController( commonData, remote );

        Menu root = Utilities.< ExerciseItemListMenuController, Menu >createSceneGraphObjectFromFXMLResource(
            exerciseMenu,
            "ExerciseItemListMenu.fxml",
            "ExerciseItemListMenu",
            commonData );

        menuBar.getMenus().add( root );

        return exerciseMenu;
    }

    @Override
    public void initialize( URL location, ResourceBundle resources ) {
        // TODO Auto-generated method stub

        checkMenuItemEditExercise.selectedProperty().addListener( ( observable, oldValue, newValue ) -> {
            if ( revertUncheck.getValue() ) {
                revertUncheck.setValue( false );
                return;
            }

            if ( commonData.getExerciseItemListShuffledSubset() == null ) {
                assert false;
                return;
            }

            if ( remote.getApplicationController().checkExerciseItemListMasterNeedsSave() ) {
                revertUncheck.setValue( true );
                checkMenuItemEditExercise.selectedProperty().setValue( true );
                return;
            }

            commonData.setEditingMode( newValue );

            if ( newValue ) {
                if ( commonData.getExerciseItemListMaster().isEmpty() ) {

                    commonData.createExerciseItem();

                } else {

                    // discard shuffled list
                    commonData.getExerciseItemListShuffledSubset().clear();
                    commonData.getExerciseItemListShuffledSubset().addAll( commonData.getExerciseItemListMaster() );
                }

                commonData.getCurrentExerciseItem().integrityCheck();

                // in case the choice is immutable, because the user looked at
                // the solution
                remote.getViewer().getChoice().setChoiceImmutable( false );
            }

            remote.getApplicationController().getButtonCheck().setVisible( newValue );

            remote.getFileMenuController().getMenuItemSaveAs().setDisable( !newValue );

            remote.getApplicationController().updateView();
        } );

        menuItemNewExerciseItemList.setOnAction( this::onNewExerciseItemList );
    }

    private void onNewExerciseItemList( ActionEvent event ) {

        boolean needsSave = remote.getApplicationController().checkExerciseItemListMasterNeedsSave();
        if ( needsSave ) {
            event.consume();
            return;
        }

        commonData.getExerciseItemListMaster().clear();
        commonData.getExerciseItemListInitialMaster().clear();

        checkMenuItemEditExercise.setDisable( false );
        checkMenuItemEditExercise.selectedProperty().set( true );

        commonData.setRecentlyOpenedFile( null );
        remote.getFileMenuController().getMenuItemSave().setDisable( true );
        remote.getFileMenuController().getMenuItemSaveAs().setDisable( false );
        remote.getApplicationController().updateView();
    }

    public CheckMenuItem getCheckMenuItemEditExercise() {

        return checkMenuItemEditExercise;
    }
}
