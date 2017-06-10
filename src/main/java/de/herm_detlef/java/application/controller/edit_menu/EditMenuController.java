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

package de.herm_detlef.java.application.controller.edit_menu;


import java.net.URL;
import java.util.ResourceBundle;

import de.herm_detlef.java.application.ApplicationConstants;
import de.herm_detlef.java.application.CommonData;
import de.herm_detlef.java.application.Remote;
import de.herm_detlef.java.application.io.Export;
import de.herm_detlef.java.application.model.ExerciseItem;
import de.herm_detlef.java.application.model.ExerciseItem.MultipleChoiceAnswerText;
import de.herm_detlef.java.application.model.ExerciseItem.SingleChoiceAnswerText;
import de.herm_detlef.java.application.utilities.Utilities;
import de.herm_detlef.java.application.view.Navigation;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;

/* @formatter:off */

/**
 * TODO
 *
 * @author Detlef G. Herm, 2016-09-26
 * @since 1.0
 *
 */
public class EditMenuController implements Initializable {

    private final CommonData     commonData;
    private final Remote         remote;

    private ToolsPanelController tools;

    @FXML
    private Menu                 menuEdit;

    @FXML
    private MenuItem             menuItemNewExerciseItem;

    @FXML
    private MenuItem             menuItemDeleteExerciseItem;

    @FXML
    private CheckMenuItem        checkMenuItemPreviewExerciseItem;

    @FXML
    private MenuItem             menuItemNewQuestionText;

    @FXML
    private MenuItem             menuItemNewQuestionCode;

    @FXML
    private Menu                 menuAnswerMode;

    @FXML
    private RadioMenuItem        radioMenuItemSingleChoice;

    @FXML
    private RadioMenuItem        radioMenuItemMultipleChoice;

    @FXML
    private ToggleGroup          choicePresetting;

    @FXML
    private MenuItem             menuItemNewAnswerText;

    @FXML
    private MenuItem             menuItemNewSolutionText;

    @FXML
    private MenuItem             menuItemDeleteItemPart;

    @FXML
    private CheckMenuItem        checkMenuItemShowTools;

    private EditMenuController( CommonData commonData,
                                Remote remote ) {

        this.commonData = commonData;
        this.remote = remote;

        remote.setEditMenuController( this );
    }

    public static EditMenuController create( MenuBar menuBar, CommonData commonData, Remote remote ) {

        EditMenuController editor = new EditMenuController( commonData, remote );

        Menu root = Utilities.< EditMenuController, Menu >createSceneGraphObjectFromFXMLResource(
            editor,
            "EditMenu.fxml",
            "EditMenu",
            commonData );

        if ( menuBar != null ) {
            menuBar.getMenus().add( root );
        }

        return editor;
    }

    @Override
    public void initialize( URL location, ResourceBundle resources ) {

        addReactionHandler();

        // ExerciseItem.initializeGUI(); called by
        // ToolsPanelController.initialize

        tools = ToolsPanelController.create(
            commonData,
            remote,
            this::onNewExerciseItem,
            this::onDeleteExerciseItem,
            this::onNewQuestionText,
            this::onNewQuestionCode,
            this::onNewAnswerText,
            this::onNewSolutionText,
            this::onDeleteItemPart );

        commonData.setExerciseItemComponentsChangeListener( c -> {
            boolean isValid = Export.validateCurrentExerciseItem( commonData, false );
            menuItemNewExerciseItem.setDisable( !isValid );
            tools.getButtonAppendNewExerciseItem().setDisable( !isValid );
            remote.getApplicationController().updateView();
        } );

        checkMenuItemPreviewExerciseItem.selectedProperty().addListener( ( observable, oldValue, newValue ) -> {

            if ( newValue && !Export.validateCurrentExerciseItem( commonData, true ) ) {
                checkMenuItemPreviewExerciseItem.selectedProperty().set( false );
                if ( tools == null )
                    return;
                tools.getToggleButtonPreviewCurrentExerciseItem().selectedProperty().set( false );
            }

            commonData.setPreviewMode( newValue );
            remote.getApplicationController().updateView();
        } );

        radioMenuItemSingleChoice.setSelected(true);
        radioMenuItemSingleChoice.selectedProperty().addListener( ( observable, oldValue, newValue ) -> {
            if ( newValue ) {
                tools.getRadioButtonSingleChoice()  .setSelected( true );
            } else {
                tools.getRadioButtonMultipleChoice().setSelected( true );
            }
        } );

        commonData.getComponentsMarkedToBeDeleted().addListener(
            ( ListChangeListener< ? super ExerciseItem.ItemPart > ) c -> {
                if ( c.next() ) {
                    boolean isEmpty = commonData.getComponentsMarkedToBeDeleted().isEmpty();
                    menuItemDeleteItemPart.setDisable( isEmpty ? true : false );
                }
            } );

        checkMenuItemShowTools.selectedProperty().addListener( ( observable, oldValue, newValue ) -> {
            if ( newValue ) {
                openEditorToolsDialog();
            } else {
                closeEditorToolsDialog();
            }
        } );

        menuItemNewExerciseItem    .setOnAction( this::onNewExerciseItem );
        menuItemDeleteExerciseItem .setOnAction( this::onDeleteExerciseItem );
        menuItemNewQuestionText    .setOnAction( this::onNewQuestionText );
        menuItemNewQuestionCode    .setOnAction( this::onNewQuestionCode );
        menuItemNewAnswerText      .setOnAction( this::onNewAnswerText );
        menuItemNewSolutionText    .setOnAction( this::onNewSolutionText );
        menuItemDeleteItemPart     .setOnAction( this::onDeleteItemPart );

        commonData.getEditingModeProperty().addListener( ( obj, oldValue, newValue ) -> {
            menuItemNewExerciseItem.setDisable( !newValue );

            if ( commonData.getCurrentExerciseItem() != null ) {
                menuItemDeleteExerciseItem      .setDisable( !newValue );
                checkMenuItemPreviewExerciseItem.setDisable( !newValue );
                menuAnswerMode                  .setDisable( !newValue );
                menuItemNewAnswerText           .setDisable( !newValue );
                radioMenuItemMultipleChoice     .setDisable( !newValue );
                radioMenuItemSingleChoice       .setDisable( !newValue );
            } else {
                menuItemDeleteExerciseItem      .setDisable( true );
                checkMenuItemPreviewExerciseItem.setDisable( true );
            }
        } );

        radioMenuItemSingleChoice.selectedProperty().addListener( (obj, oldValue, newValue) -> {

            if ( newValue &&
                 commonData.getCurrentExerciseItem().getChoiceModel() instanceof MultipleChoiceAnswerText ) {
                commonData.getCurrentExerciseItem().createSingleChoiceModel();
            }
        });

        radioMenuItemMultipleChoice.selectedProperty().addListener( (obj, oldValue, newValue) -> {

            if ( newValue &&
                 commonData.getCurrentExerciseItem().getChoiceModel() instanceof SingleChoiceAnswerText ) {
                commonData.getCurrentExerciseItem().createMultipleChoiceModel();
            }
        });
    }

    private void addReactionHandler() {

        // in case of currentExerciseItem == null:
        ExerciseItem.addHandlerToInitializeGUI( () -> {
            menuItemNewExerciseItem         .setDisable( false );
            menuItemDeleteExerciseItem      .setDisable( true );
            checkMenuItemPreviewExerciseItem.setDisable( true );
            menuItemNewQuestionText         .setDisable( true );
            menuItemNewQuestionCode         .setDisable( true );
            radioMenuItemSingleChoice       .setDisable( true );
            radioMenuItemMultipleChoice     .setDisable( true );
            menuAnswerMode                  .setDisable( true );
            menuItemNewAnswerText           .setDisable( true );
            menuItemNewSolutionText         .setDisable( true );
        } );

        commonData.getCurrentExerciseItemProperty().addListener( ( obj, oldValue, newValue ) -> {
            menuItemNewExerciseItem         .setDisable( !commonData.isEditingMode() );
            menuItemDeleteExerciseItem      .setDisable( !commonData.isEditingMode() );
            checkMenuItemPreviewExerciseItem.setDisable( !commonData.isEditingMode() );

            if ( newValue == null || !commonData.isEditingMode() )
                return;

            if ( newValue.getChoiceModel() instanceof SingleChoiceAnswerText ) {
                radioMenuItemSingleChoice  .setSelected( true );
            } else {
                radioMenuItemMultipleChoice.setSelected( true );
            }
        } );

        //-------------
        // prototype

        commonData.getExerciseItemPrototype().addHandlerOnNewExerciseItem( () -> {
            menuItemNewExerciseItem         .setDisable( true );
            menuItemDeleteExerciseItem      .setDisable( false );
            checkMenuItemPreviewExerciseItem.setDisable( false );
            radioMenuItemSingleChoice       .setDisable( false );
            radioMenuItemMultipleChoice     .setDisable( false );
            menuAnswerMode                  .setDisable( false );
            menuItemNewAnswerText           .setDisable( false );
        } );

        commonData.getExerciseItemPrototype().addHandlerOnDeleteExerciseItem( () -> {
            ExerciseItem.initializeGUI();
        } );

        commonData.getExerciseItemPrototype().addHandlerOnPreviewExerciseItem( () -> {
            // TODO
        } );

        commonData.getExerciseItemPrototype().addHandlerToPreventMoreQuestionText( () -> {
            menuItemNewQuestionText.setDisable( true );
        } );

        commonData.getExerciseItemPrototype().addHandlerToAllowMoreQuestionText( () -> {
            menuItemNewQuestionText.setDisable( false );
        } );

        commonData.getExerciseItemPrototype().addHandlerToPreventMoreQuestionCode( () -> {
            menuItemNewQuestionCode.setDisable( true );
        } );

        commonData.getExerciseItemPrototype().addHandlerToAllowMoreQuestionCode( () -> {
            menuItemNewQuestionCode.setDisable( false );
        } );

//        commonData.getExerciseItemPrototype().addHandlerToPreventMoreSingleChoiceAnswerText( () -> {
//            menuItemSelectorSingleChoice.setDisable( true );
//        } );
//
//        commonData.getExerciseItemPrototype().addHandlerToAllowMoreSingleChoiceAnswerText( () -> {
//            menuItemSelectorSingleChoice.setDisable( false );
//        } );
//
//        commonData.getExerciseItemPrototype().addHandlerToPreventMoreMultipleChoiceAnswerText( () -> {
//            menuItemSelectorMultipleChoice.setDisable( true );
//        } );
//
//        commonData.getExerciseItemPrototype().addHandlerToAllowMoreMultipleChoiceAnswerText( () -> {
//            menuItemSelectorMultipleChoice.setDisable( false );
//        } );

        commonData.getExerciseItemPrototype().addHandlerOnChangeAnswerMode( () -> {
            remote.getApplicationController().updateView();
        });

        commonData.getExerciseItemPrototype().addHandlerToPreventMoreSolutionText( () -> {
            menuItemNewSolutionText.setDisable( true );
        } );

        commonData.getExerciseItemPrototype().addHandlerToAllowMoreSolutionText( () -> {
            menuItemNewSolutionText.setDisable( false );
        } );

        commonData.getExerciseItemPrototype().addHandlerOnModifiedExerciseItemPart( () -> {
            //remote.getApplicationController().getButtonCheck().setDisable( false );
        });
    }

    private void onNewExerciseItem( ActionEvent event ) {

        // append new instance of class ExerciseItem to exerciseItemListMaster

        commonData.createExerciseItem();

        commonData.getCurrentExerciseItem().integrityCheck();

        remote.getApplicationController().updateView();
    }

    private void onDeleteExerciseItem( ActionEvent event ) {

        boolean accept = Utilities.showConfirmationMessage( null, ApplicationConstants.CONFIRMATION_MESSAGE_DELETE_EXERCISEITEM);

        if ( ! accept )
            return;

        ExerciseItem candidate = commonData.getCurrentExerciseItem();

        if ( Navigation.rightDirection().hasNextStep() ) {
            Navigation.rightDirection().step();
        } else if ( Navigation.leftDirection().hasNextStep() ) {
            Navigation.leftDirection().step();
        }

        commonData.getExerciseItemListMaster().remove( candidate );

        if ( commonData.getExerciseItemListMaster().isEmpty() ) {

            commonData.setCurrentExerciseItem( null );
            ExerciseItem.initializeGUI();

        } else {

            // renumber the exercise items
            int id = 0;
            for ( ExerciseItem item : commonData.getExerciseItemListMaster() ) {
                item.setItemId( ++id );
            }
        }

        remote.getApplicationController().updateView();
    }

    private void onNewQuestionText( ActionEvent event ) {

        if ( commonData.getCurrentExerciseItem().isFinalQuestionPart() ) {
            commonData.getCurrentExerciseItem().addQuestionText2( null );
        } else {
            commonData.getCurrentExerciseItem().addQuestionText( null );
        }

        commonData.getCurrentExerciseItem().integrityCheck();
    }

    private void onNewQuestionCode( ActionEvent event ) {

        commonData.getCurrentExerciseItem().addQuestionCode( null );

        commonData.getCurrentExerciseItem().integrityCheck();
    }

    private void onNewAnswerText( ActionEvent event ) {

        if ( radioMenuItemSingleChoice.isSelected() ) {
            commonData.getCurrentExerciseItem().createSingleChoiceModel();
        }

        if ( radioMenuItemMultipleChoice.isSelected() ) {
            commonData.getCurrentExerciseItem().createMultipleChoiceModel();
        }

        commonData.getCurrentExerciseItem().addAnswerText( null, false );

        commonData.getCurrentExerciseItem().integrityCheck();
    }

    private void onNewSolutionText( ActionEvent event ) {

        commonData.getCurrentExerciseItem().addSolutionText( null );

        commonData.getCurrentExerciseItem().integrityCheck();
    }

    private void onDeleteItemPart( ActionEvent event ) {

        for ( ExerciseItem.ItemPart itemPart : commonData.getComponentsMarkedToBeDeleted() ) {
            commonData.getCurrentExerciseItem().getExerciseItemParts().remove( itemPart );
        }

        commonData.getComponentsMarkedToBeDeleted().clear();
        commonData.getCurrentExerciseItem().integrityCheck();
    }

    private void openEditorToolsDialog() {

        if ( tools == null ) {
            assert false;
            return;
        }

        if ( !tools.isDialogOpen() ) {
            tools.openDialog();
        }
    }

    private void closeEditorToolsDialog() {

        if ( tools == null ) {
            assert false;
            return;
        }

        if ( tools.isDialogOpen() ) {
            tools.closeDialog();
        }
    }

    public ToolsPanelController getTools() {

        return tools;
    }

    public MenuItem getMenuItemDeleteExerciseItem() {

        return menuItemDeleteExerciseItem;
    }

    public Menu getMenuEdit() {

        return menuEdit;
    }

    public MenuItem getMenuItemNewExerciseItem() {

        return menuItemNewExerciseItem;
    }

    public CheckMenuItem getCheckMenuItemPreviewExerciseItem() {

        return checkMenuItemPreviewExerciseItem;
    }

    public RadioMenuItem getRadioMenuItemSingleChoice() {

        return radioMenuItemSingleChoice;
    }

    public RadioMenuItem getRadioMenuItemMultipleChoice() {

        return radioMenuItemMultipleChoice;
    }

    public CheckMenuItem getCheckMenuItemShowTools() {

        return checkMenuItemShowTools;
    }
}
