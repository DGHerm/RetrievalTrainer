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

package de.herm_detlef.java.application.mvc.controller.app;


import java.util.Arrays;

import de.herm_detlef.java.application.ApplicationConstants;
import de.herm_detlef.java.application.CommonData;
import de.herm_detlef.java.application.Remote;
import de.herm_detlef.java.application.mvc.controller.app_menu.AppMenuController;
import de.herm_detlef.java.application.mvc.controller.edit.edit_menu.EditMenuController;
import de.herm_detlef.java.application.mvc.controller.exerciseitemlist_menu.ExerciseItemListMenuController;
import de.herm_detlef.java.application.mvc.controller.file_menu.FileMenuController;
import de.herm_detlef.java.application.mvc.controller.navigation.NavigationController;
import de.herm_detlef.java.application.mvc.model.ExerciseItem;
import de.herm_detlef.java.application.mvc.model.ExerciseItem.AnswerText;
import de.herm_detlef.java.application.mvc.model.ExerciseItem.ItemPart;
import de.herm_detlef.java.application.utilities.Utilities;
import de.herm_detlef.java.application.mvc.view.Navigation;
import de.herm_detlef.java.application.mvc.view.Viewer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Singleton;

import static de.herm_detlef.java.application.ApplicationConstants.CURRENT_LOCALE;

/* @formatter:off */

/**
 * TODO
 * @author Detlef G. Herm, 2016-09-26
 * @since 1.0
 *
 */
@Singleton
class ApplicationControllerImpl implements ApplicationController {

    private final NavigationController navigationController;
    private final ExerciseItemListMenuController exerciseItemListMenuController;
    private final EditMenuController editMenuController;
    private final AppMenuController appMenuController;
    private final FileMenuController fileMenuController;
    private final CommonData       commonData;
    private final Remote remote;

    private Viewer                 viewer;

    @FXML
    private Parent                 root;

    @FXML
    private MenuBar                menuBar;

    @FXML
    private ToolBar                toolBar;

    @FXML
    private VBox                   questionPart;

    @FXML
    private VBox                   answerPart;

    @FXML
    private VBox                   solutionPart;

    @FXML
    private ToggleButton           showSolution;

    @FXML
    private Button                 buttonScore;

    @FXML
    private Button                 buttonCheck;



    /**
     * TODO
     * <p>
     * @since 1.0
     */
    @Inject
    private ApplicationControllerImpl( NavigationController navigationController,
                                       ExerciseItemListMenuController exerciseItemListMenuController,
                                       EditMenuController editMenuController,
                                       AppMenuController appMenuController,
                                       FileMenuController fileMenuController,
                                       CommonData commonData,
                                       Remote remote ) {

        this.navigationController = navigationController;
        this.exerciseItemListMenuController = exerciseItemListMenuController;
        this.editMenuController = editMenuController;
        this.appMenuController = appMenuController;
        this.fileMenuController = fileMenuController;
        this.commonData = commonData;
        this.remote = remote;

        remote.setApplicationController( this );

        commonData.setCurrentLocale( CURRENT_LOCALE );
    }

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    @FXML
    private void initialize() {

        commonData.getPrimaryStage().setOnCloseRequest( e -> {
            e.consume();
            remote.getAppMenuController().quit();
        });

        commonData.getEditingModeProperty().addListener( (obj, oldValue, newValue) -> buttonScore.setDisable( newValue ) );

        menuBar.getMenus().add( appMenuController.create( commonData, remote ) );

        menuBar.getMenus().add( fileMenuController.create( commonData, remote ) );

        menuBar.getMenus().add( exerciseItemListMenuController.create( commonData, remote ) );

        menuBar.getMenus().add( editMenuController.create() );

        toolBar.getItems().add(0, navigationController.create( commonData, remote ) );
        navigationController.addEventHandler(toolBar);

        viewer = new Viewer( commonData, remote, questionPart, answerPart, solutionPart );

        if ( Arrays.stream(ApplicationConstants.NAME_OF_OPERATING_SYSTEM_OSX)
                .filter( name -> name.equals(ApplicationConstants.NAME_OF_OPERATING_SYSTEM) )
                .count() == 1 ) {
            menuBar.setUseSystemMenuBar( ApplicationConstants.USE_SYSTEM_MENU_BAR );
        }

        commonData.getApplicationPreferences().saveStagePositionAndSizeToUserPreferences(
            commonData.getPrimaryStage(),
            "ApplicationStageOriginX",
            "ApplicationStageOriginY",
            "ApplicationStageWidth",
            "ApplicationStageHeight" );

        updateView();
    }

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    @Override
    public void updateView() {

        if ( commonData.getExerciseItemListShuffledSubset().size() == 0 ) {

            viewer.prepareExerciseItem( -1 );

            navigationController.setDisable( true, true, true, true );

            buttonScore.setDisable( true );

        } else if ( commonData.getCurrentExerciseItem() != null ) {

            int index = viewer.prepareExerciseItem( commonData.getCurrentExerciseItem() );

            navigationController.setDisable(
                    index == 0,
                    index == 0,
                    commonData.getExerciseItemListShuffledSubset().size() - 1 == index,
                    commonData.getExerciseItemListShuffledSubset().size() - 1 == index);

            buttonScore.setDisable( commonData.isEditingMode() );

            Navigation.init( commonData, remote, navigationController, viewer::prepareExerciseItem, root );

        } else {

            viewer.prepareExerciseItem( 0 );

            navigationController.setDisable(
                    true,
                    true,
                    commonData.getExerciseItemListShuffledSubset().size() <= 1,
                    commonData.getExerciseItemListShuffledSubset().size() <= 1);

            buttonScore.setDisable( commonData.isEditingMode() );

            Navigation.init( commonData, remote, navigationController, viewer::prepareExerciseItem, root );
        }

        remote.getEditMenuController().getMenuEdit().setVisible( commonData.isEditingMode() );
        showSolution.setSelected( commonData.isEditingMode() );
    }

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    @FXML
    void onScore( ActionEvent event ) {

        //choice.save();

        if ( commonData.getExerciseItemListShuffledSubset().isEmpty() ) {
            assert false;
            return;
        }

        int successCount = 0;
        for ( ExerciseItem item : commonData.getExerciseItemListShuffledSubset() ) {

            boolean success = true;

            for ( ItemPart part : item.getExerciseItemParts() ) {

                if ( part instanceof AnswerText ) {
                    success = success && ( ( ( AnswerText ) part ).isInitialMark() == ( ( AnswerText ) part ).isSelected() );
                }
            }

            if ( success ) {
                ++successCount;
            }
        }

        Utilities.showScoreMessage(
            null,
            String.format(
                ApplicationConstants.FORM_OF_SCORE_MESSAGE,
                commonData.getExerciseItemListShuffledSubset().size(),
                successCount,
                100.0f * successCount / commonData.getExerciseItemListShuffledSubset().size() ) );
    }

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    @FXML
    void onCheck(ActionEvent event) {

        checkExerciseItemListMasterNeedsSave();
    }

    @Override
    public boolean checkExerciseItemListMasterNeedsSave() {
        boolean needsSave = commonData.checkNeedsSave();

        if ( ! needsSave ) return false;

        final boolean discard = Utilities.showConfirmationMessage(
                ApplicationConstants.WARNING_MESSAGE_UNSAVED_MODIFICATIONS,
                ApplicationConstants.CONFIRMATION_MESSAGE_DISCARD_UNSAVED_MODIFICATIONS);

        remote.getFileMenuController().getMenuItemSave().setDisable( discard );

        return ! discard;
    }

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    @Override
    public ToggleButton getShowSolution() {

        return showSolution;
    }

    @Override
    public Button getButtonCheck() {

        return buttonCheck;
    }
}
