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

package de.herm_detlef.java.application.mvc.controller.edit.tools_panel;


import java.util.Objects;

import de.herm_detlef.java.application.ApplicationConstants;
import de.herm_detlef.java.application.CommonData;
import de.herm_detlef.java.application.Remote;
import de.herm_detlef.java.application.mvc.model.ExerciseItem;
import de.herm_detlef.java.application.utilities.Utilities;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import static de.herm_detlef.java.application.BindingAnnotationNames.TOOLS_PANEL;
import static de.herm_detlef.java.application.ViewResourcesPath.TOOLS_PANEL_FXML;
import static de.herm_detlef.java.application.ViewResourcesPath.TOOLS_PANEL_RESOURCE_BUNDLE;

/* @formatter:off */

/**
 * TODO
 *
 * @author Detlef G. Herm, 2016-09-26
 * @since 1.0
 *
 */
@Singleton
class ToolsPanelControllerImpl implements ToolsPanelController {

    private final CommonData                  commonData;
    private final Remote                      remote;

    private final Stage                       stage;

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

    @Inject
    private ToolsPanelControllerImpl( @Named(TOOLS_PANEL) Stage stage,
                                      CommonData commonData,
                                      Remote remote ) {

        this.stage       = stage;
        this.commonData  = commonData;
        this.remote      = remote;
    }

    @Override
    public Parent create( CommonData commonData ) {

        stage.setTitle( ApplicationConstants.TITLE_OF_DIALOG_EDITOR_TOOLS );

        Parent root = Utilities
                .createSceneGraphObjectFromFXMLResource(this,
                                                        TOOLS_PANEL_FXML.path(),
                                                        TOOLS_PANEL_RESOURCE_BUNDLE.path(),
                                                        commonData );

        Scene scene = stage.getScene();

        if ( scene == null ) {
            stage.setScene( new Scene( root ) );
        }

        stage.setResizable( false );
        stage.centerOnScreen();
        stage.sizeToScene();
        stage.initOwner( commonData.getPrimaryStage() );
        stage.initModality( Modality.NONE );

        commonData
                .getApplicationPreferences()
                .setStagePositionAndSizeBasedOnUserPreferences( stage,
                                                                "ToolsPanelStageOriginX",
                                                                "ToolsPanelStageOriginY",
                                                                "ToolsPanelStageWidth",
                                                                "ToolsPanelStageHeight" );
        return Objects.requireNonNull(root);
    }

    @FXML
    private void initialize() {

        addReactionHandler();
        ExerciseItem.initializeGUI();

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

    @Override
    public void openDialog() {

        stage.show();
    }

    @Override
    public void closeDialog() {

        if ( stage == null ) {
            assert false;
            return;
        }
        stage.close();
    }

    @Override
    public boolean isDialogOpen() {

        if ( stage == null ) return false;
        return stage.isShowing();
    }

    @Override
    public Button getButtonAppendNewExerciseItem() {

        return buttonAppendNewExerciseItem;
    }

    @Override
    public Button getButtonDeleteCurrentExerciseItem() {
        return buttonDeleteCurrentExerciseItem;
    }

    @Override
    public Button getButtonAppendQuestionText() {
        return buttonAppendQuestionText;
    }

    @Override
    public Button getButtonAppendQuestionCode() {
        return buttonAppendQuestionCode;
    }

    @Override
    public Button getButtonAppendAnswer() {
        return buttonAppendAnswer;
    }

    @Override
    public Button getButtonAppendSolution() {
        return buttonAppendSolution;
    }

    @Override
    public Button getButtonDeleteItemPart() {
        return buttonDeleteItemPart;
    }

    @Override
    public ToggleButton getToggleButtonPreviewCurrentExerciseItem() {

        return toggleButtonPreviewCurrentExerciseItem;
    }

    @Override
    public RadioButton getRadioButtonSingleChoice() {

        return radioButtonSingleChoice;
    }

    @Override
    public RadioButton getRadioButtonMultipleChoice() {

        return radioButtonMultipleChoice;
    }

}
