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

package de.herm_detlef.java.application.mvc.controller.edit_menu;


import java.net.URL;
import java.util.ResourceBundle;

import de.herm_detlef.java.application.ApplicationConstants;
import de.herm_detlef.java.application.CommonData;
import de.herm_detlef.java.application.Remote;
import de.herm_detlef.java.application.mvc.model.ExerciseItem;
import de.herm_detlef.java.application.utilities.Utilities;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static de.herm_detlef.java.application.ApplicationConstants.DEBUG;

/* @formatter:off */

/**
 * TODO
 *
 * @author Detlef G. Herm, 2016-09-26
 * @since 1.0
 *
 */
public class ToolsPanelController implements Initializable {

    private final CommonData                  commonData;
    private final Remote                      remote;

    private Stage                             stage;

    private final EventHandler< ActionEvent > onNewExerciseItem;
    private final EventHandler< ActionEvent > onDeleteCurrentExerciseItem;
    private final EventHandler< ActionEvent > onNewQuestionText;
    private final EventHandler< ActionEvent > onNewQuestionCode;
    private final EventHandler< ActionEvent > onNewAnswerText;
    private final EventHandler< ActionEvent > onNewSolutionText;
    private final EventHandler< ActionEvent > onDeleteItemPart;

    @FXML
    private Button                            buttonAppendNewExerciseItem;

    @FXML
    private Button                            buttonDeleteCurrentExerciseItem;

    @FXML
    private ToggleButton                      toggleButtonPreviewCurrentExerciseItem;

    @FXML
    private Button                            buttonAppendQuestionText;

    @FXML
    private Button                            buttonAppendQuestionCode;

    @FXML
    private Button                            buttonAppendAnswer;

    @FXML
    private RadioButton                       radioButtonSingleChoice;

    @FXML
    private RadioButton                       radioButtonMultipleChoice;

    @FXML
    private ToggleGroup                       answerMode;

    @FXML
    private Button                            buttonAppendSolution;

    @FXML
    private Button                            buttonDeleteItemPart;

    private ToolsPanelController( CommonData commonData,
                                  Remote remote,
                                  EventHandler< ActionEvent > onNewExerciseItem,
                                  EventHandler< ActionEvent > onDeleteCurrentExerciseItem,
                                  EventHandler< ActionEvent > onNewQuestionText,
                                  EventHandler< ActionEvent > onNewQuestionCode,
                                  EventHandler< ActionEvent > onNewAnswerText,
                                  EventHandler< ActionEvent > onNewSolutionText,
                                  EventHandler< ActionEvent > onDeleteItemPart ) {

        this.commonData                  = commonData;
        this.remote                      = remote;
        this.onNewExerciseItem           = onNewExerciseItem;
        this.onDeleteCurrentExerciseItem = onDeleteCurrentExerciseItem;
        this.onNewQuestionText           = onNewQuestionText;
        this.onNewQuestionCode           = onNewQuestionCode;
        this.onNewAnswerText             = onNewAnswerText;
        this.onNewSolutionText           = onNewSolutionText;
        this.onDeleteItemPart            = onDeleteItemPart;
    }

    public Stage getStage() {

        return stage;
    }

    public void setStage( Stage stage ) {

        this.stage = stage;
    }

    public static ToolsPanelController create( CommonData commonData,
                                               Remote remote,
                                               EventHandler< ActionEvent > onNewExerciseItem,
                                               EventHandler< ActionEvent > onDeleteCurrentExerciseItem,
                                               EventHandler< ActionEvent > onNewQuestionText,
                                               EventHandler< ActionEvent > onNewQuestionCode,
                                               EventHandler< ActionEvent > onNewAnswerText,
                                               EventHandler< ActionEvent > onNewSolutionText,
                                               EventHandler< ActionEvent > onDeleteItemPart ) {

        assert onNewExerciseItem != null;
        assert onDeleteCurrentExerciseItem != null;
        assert onNewQuestionText != null;
        assert onNewQuestionCode != null;
        assert onNewAnswerText != null;
        assert onNewSolutionText != null;
        assert onDeleteItemPart != null;

        ToolsPanelController tools = new ToolsPanelController( commonData,
                                                               remote,
                                                               onNewExerciseItem,
                                                               onDeleteCurrentExerciseItem,
                                                               onNewQuestionText,
                                                               onNewQuestionCode,
                                                               onNewAnswerText,
                                                               onNewSolutionText,
                                                               onDeleteItemPart );

        tools.setStage( new Stage() );
        tools.getStage().setTitle( ApplicationConstants.TITLE_OF_DIALOG_EDITOR_TOOLS );

        Parent root = Utilities.createSceneGraphObjectFromFXMLResource(
            tools,
            "ToolsPanelController.fxml",
            "ToolsPanel",
            commonData );

        if (DEBUG) assert root != null;
        if ( root == null ) return null;

        final Scene scene = new Scene( root );
        tools.getStage().setScene( scene );
        tools.getStage().setResizable( false );
        tools.getStage().centerOnScreen();
        tools.getStage().sizeToScene();
        tools.getStage().initOwner( commonData.getPrimaryStage() );
        tools.getStage().initModality( Modality.NONE );

        commonData.getApplicationPreferences().setStagePositionAndSizeBasedOnUserPreferences(
            tools.getStage(),
            "ToolsPanelStageOriginX",
            "ToolsPanelStageOriginY",
            "ToolsPanelStageWidth",
            "ToolsPanelStageHeight" );

        return tools;
    }

    @Override
    public void initialize( URL location, ResourceBundle resources ) {

        addReactionHandler();
        ExerciseItem.initializeGUI();

        buttonAppendNewExerciseItem    .setOnAction( onNewExerciseItem );
        buttonDeleteCurrentExerciseItem.setOnAction( onDeleteCurrentExerciseItem );
        buttonAppendQuestionText       .setOnAction( onNewQuestionText );
        buttonAppendQuestionCode       .setOnAction( onNewQuestionCode );
        buttonAppendAnswer             .setOnAction( onNewAnswerText );
        buttonAppendSolution           .setOnAction( onNewSolutionText );
        buttonDeleteItemPart           .setOnAction( onDeleteItemPart );

        toggleButtonPreviewCurrentExerciseItem.selectedProperty().bindBidirectional(
            remote.getEditMenuController().getCheckMenuItemPreviewExerciseItem().selectedProperty() );

        assert remote.getEditMenuController().getRadioMenuItemSingleChoice() != null;
        assert remote.getEditMenuController().getRadioMenuItemMultipleChoice() != null;

        radioButtonSingleChoice.setSelected( true );
        radioButtonSingleChoice.selectedProperty().addListener( ( observable, oldValue, newValue ) -> {
            if ( newValue ) {
                remote.getEditMenuController().getRadioMenuItemSingleChoice().setSelected( true );
            } else {
                remote.getEditMenuController().getRadioMenuItemMultipleChoice().setSelected( true );
            }
        } );

        commonData.getComponentsMarkedToBeDeleted().addListener(
            ( ListChangeListener< ? super ExerciseItem.ItemPart > ) c -> {
                if ( c.next() ) {
                    boolean isEmpty = commonData.getComponentsMarkedToBeDeleted().isEmpty();
                    buttonDeleteItemPart.setDisable( isEmpty );
                }
            } );

        commonData.getApplicationPreferences().saveStagePositionAndSizeToUserPreferences(
            stage,
            "ToolsPanelStageOriginX",
            "ToolsPanelStageOriginY",
            "ToolsPanelStageWidth",
            "ToolsPanelStageHeight" );

        stage.setOnCloseRequest( e -> remote.getEditMenuController().getCheckMenuItemShowTools().selectedProperty().set( false ) );

        commonData.getEditingModeProperty().addListener( ( obj, oldValue, newValue ) -> {
            buttonAppendNewExerciseItem.setDisable( !newValue );

            if ( commonData.getCurrentExerciseItem() != null ) {
                buttonDeleteCurrentExerciseItem       .setDisable( !newValue );
                toggleButtonPreviewCurrentExerciseItem.setDisable( !newValue );
                buttonAppendAnswer                    .setDisable( !newValue );
                radioButtonMultipleChoice             .setDisable( !newValue );
                radioButtonSingleChoice               .setDisable( !newValue );
            } else {
                buttonDeleteCurrentExerciseItem       .setDisable( true );
                toggleButtonPreviewCurrentExerciseItem.setDisable( true );
            }
        } );

    }

    private void addReactionHandler() {

        // in case of currentExerciseItem == null:
        ExerciseItem.addHandlerToInitializeGUI( () -> {
            buttonAppendNewExerciseItem           .setDisable( false );
            buttonDeleteCurrentExerciseItem       .setDisable( true );
            toggleButtonPreviewCurrentExerciseItem.setDisable( true );
            buttonAppendQuestionText              .setDisable( true );
            buttonAppendQuestionCode              .setDisable( true );
            buttonAppendAnswer                    .setDisable( true );
            radioButtonSingleChoice               .setDisable( true );
            radioButtonMultipleChoice             .setDisable( true );
            buttonAppendSolution                  .setDisable( true );
            buttonDeleteItemPart                  .setDisable( true );
        } );

        // TODO remove listener, if not editing mode
        commonData.getCurrentExerciseItemProperty().addListener( ( obj, oldValue, newValue ) -> {
            buttonAppendNewExerciseItem           .setDisable( !commonData.isEditingMode() );
            buttonDeleteCurrentExerciseItem       .setDisable( !commonData.isEditingMode() );
            toggleButtonPreviewCurrentExerciseItem.setDisable( !commonData.isEditingMode() );
        } );

        // -------------------
        // prototype

        commonData.getExerciseItemPrototype().addHandlerOnNewExerciseItem( () -> {
            buttonAppendNewExerciseItem           .setDisable( true );
            buttonDeleteCurrentExerciseItem       .setDisable( false );
            toggleButtonPreviewCurrentExerciseItem.setDisable( false );
            buttonAppendAnswer                    .setDisable( false );
            radioButtonSingleChoice               .setDisable( false );
            radioButtonMultipleChoice             .setDisable( false );
        } );

        commonData.getExerciseItemPrototype().addHandlerToPreventMoreQuestionText( () -> buttonAppendQuestionText.setDisable( true ) );

        commonData.getExerciseItemPrototype().addHandlerToAllowMoreQuestionText( () -> buttonAppendQuestionText.setDisable( false ) );

        commonData.getExerciseItemPrototype().addHandlerToPreventMoreQuestionCode( () -> buttonAppendQuestionCode.setDisable( true ) );

        commonData.getExerciseItemPrototype().addHandlerToAllowMoreQuestionCode( () -> buttonAppendQuestionCode.setDisable( false ) );

        commonData.getExerciseItemPrototype().addHandlerToPreventMoreSolutionText( () -> buttonAppendSolution.setDisable( true ) );

        commonData.getExerciseItemPrototype().addHandlerToAllowMoreSolutionText( () -> buttonAppendSolution.setDisable( false ) );
    }

    public void openDialog() {

        stage.show();
    }

    public void closeDialog() {

        if ( stage == null ) {
            assert false;
            return;
        }
        stage.close();
    }

    public boolean isDialogOpen() {

        if ( stage == null ) return false;
        return stage.isShowing();
    }

    public Button getButtonAppendNewExerciseItem() {

        return buttonAppendNewExerciseItem;
    }

    public ToggleButton getToggleButtonPreviewCurrentExerciseItem() {

        return toggleButtonPreviewCurrentExerciseItem;
    }

    public RadioButton getRadioButtonSingleChoice() {

        return radioButtonSingleChoice;
    }

    public RadioButton getRadioButtonMultipleChoice() {

        return radioButtonMultipleChoice;
    }

}
