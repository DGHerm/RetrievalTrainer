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

package de.herm_detlef.java.application.view;


import java.util.IdentityHashMap;
import java.util.function.Function;

import de.herm_detlef.java.application.ApplicationConstants;
import de.herm_detlef.java.application.CommonData;
import de.herm_detlef.java.application.Remote;
import de.herm_detlef.java.application.model.ExerciseItem;
import de.herm_detlef.java.application.model.ExerciseItem.AnswerText;
import de.herm_detlef.java.application.model.ExerciseItem.ItemPart;
import de.herm_detlef.java.application.model.ExerciseItem.MultipleChoiceAnswerText;
import de.herm_detlef.java.application.model.ExerciseItem.QuestionCode;
import de.herm_detlef.java.application.model.ExerciseItem.QuestionText;
import de.herm_detlef.java.application.model.ExerciseItem.QuestionText2;
import de.herm_detlef.java.application.model.ExerciseItem.SingleChoiceAnswerText;
import de.herm_detlef.java.application.model.ExerciseItem.SolutionText;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/* @formatter:off */

/**
 * TODO
 * @author Detlef G. Herm, 2016-09-26
 * @since 1.0
 *
 */
public class Viewer {

    private final CommonData commonData;
    private final Remote     remote;

    private final VBox       questionPart;
    private final VBox       answerPart;
    private final VBox       solutionPart;

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    public class Choice {

        private final ToggleGroup                               toggleGroup     = new ToggleGroup();
        private final ObservableList< ? extends Toggle >        radioButtonList = toggleGroup.getToggles();
        private final ObservableList< CheckBox >                checkBoxList    = FXCollections.observableArrayList();
        private final IdentityHashMap< ButtonBase, AnswerText > map             = new IdentityHashMap<>();

        /**
         * TODO
         * <p>
         * @since 1.0
         */
        public void setChoiceImmutable( boolean immutable ) {

            if ( commonData.getCurrentExerciseItem() == null || ( immutable && commonData.isEditingMode() ) ) {
                return;
            }

            for ( Toggle rb : radioButtonList ) {
                if ( rb instanceof RadioButton ) {
                    ( ( RadioButton ) rb ).setDisable( immutable );
                }
            }
            for ( CheckBox cb : checkBoxList ) {
                cb.setDisable( immutable );
            }
            commonData.getCurrentExerciseItem().setSelectionImmutable( immutable );
        }

        /**
         * TODO
         * <p>
         * @since 1.0
         */
        public ToggleGroup getToggleGroup() {

            return toggleGroup;
        }

        /**
         * TODO
         * <p>
         * @since 1.0
         */
        public ObservableList< CheckBox > getCheckBoxList() {

            return checkBoxList;
        }

        /**
         * TODO
         * <p>
         * @since 1.0
         */
        public IdentityHashMap< ButtonBase, AnswerText > getMap() {

            return map;
        }
    }

    Choice choice;


    public Viewer( CommonData commonData,
                   Remote remote,
                   VBox questionPart,
                   VBox answerPart,
                   VBox solutionPart ) {

        this.commonData   = commonData;
        this.remote       = remote;
        this.questionPart = questionPart;
        this.answerPart   = answerPart;
        this.solutionPart = solutionPart;

        remote.setViewer( this );

        initialize();
    }

    private void initialize() {

        questionPart.getChildren().clear();
        answerPart.getChildren().clear();
        solutionPart.getChildren().clear();

        commonData.getExerciseItemListInitialMaster().clear();
        commonData.getExerciseItemListMaster().clear();
        commonData.getExerciseItemListShuffledSubset().clear();

        commonData.setCurrentExerciseItem( null );

        remote.getApplicationController().getShowSolution().selectedProperty().addListener(
            ( observable, oldValue, newValue ) -> {
                if ( newValue ) {
                    // TODO
                    remote.getViewer().getChoice().setChoiceImmutable( true );
                    solutionPart.setVisible( true );
                } else {
                    solutionPart.setVisible( false );
                }
            } );

        remote.getApplicationController().getShowSolution().setVisible( false );
    }

    public int prepareExerciseItem( ExerciseItem item ) {

        assert item != null;

        int index = commonData.getExerciseItemListShuffledSubset().indexOf( item );

        if ( index == -1 ) {
            assert false;
            return -1;
        }

        prepareExerciseItem(
            index );

        return index;
    }

    public ExerciseItem prepareExerciseItem( int itemNumber ) {

        questionPart.getChildren().clear();
        answerPart.getChildren().clear();
        solutionPart.getChildren().clear();
        remote.getApplicationController().getShowSolution().setVisible( false );

        if ( itemNumber == -1 ) return null;

        setFunctionQuestionText();
        setFunctionQuestionCode();
        setFunctionAnswerText();
        setFunctionSolutionText();

        commonData.setCurrentExerciseItem(
            commonData.getExerciseItemListShuffledSubset().get(
                itemNumber ) );

        for ( ItemPart itemPart : commonData.getCurrentExerciseItem().getExerciseItemParts() ) {
            Object obj = itemPart.get();

            if ( obj == null ) {
                assert false : String.format(
                    "%s",
                    itemPart.getClass() ); // TODO
                continue;
            }

            if ( itemPart instanceof QuestionText
                 || itemPart instanceof QuestionCode
                 || itemPart instanceof QuestionText2 ) {
                questionPart.getChildren().add( ( Node ) obj );
            }

            if ( itemPart instanceof AnswerText ) {
                answerPart.getChildren().add( ( Node ) obj );
            }

            if ( itemPart instanceof SolutionText ) {
                solutionPart.getChildren().add( ( Node ) obj );
            }
        }

        if ( !solutionPart.getChildren().isEmpty() ) {
            remote.getApplicationController().getShowSolution().setVisible( true );
        }

        // needed for method "init" of class Navigation:
        return commonData.getCurrentExerciseItem();
    }

    private void setFunctionQuestionText() {

        Function<ItemPart, Node> func = obj -> {
            TextArea q;

            if ( commonData.isEditingMode() && ! commonData.isPreviewMode() ) {
                q = new TextArea();
                q.textProperty().bindBidirectional(
                    obj.getStr() );
            } else {
                q = TextAreaHelper.create( obj.getStr().get() );
            }

            q.setWrapText( true );

            q.setPromptText( ApplicationConstants.PLACEHOLDER_QUESTION_TEXT );

            q.setEditable(
                commonData.isEditingMode() && ! commonData.isPreviewMode() ? true : false );
            q.setMouseTransparent(
                commonData.isEditingMode() && ! commonData.isPreviewMode() ? false : true );
            q.setFocusTraversable(
                false );

            return addCheckBoxDeleteMark( q, obj );
        };

        ExerciseItem.QuestionText.< Node >setFunction( Node.class, func );
        ExerciseItem.QuestionText2.< Node >setFunction( Node.class, func );
    }

    private void setFunctionQuestionCode() {

        ExerciseItem.QuestionCode.< Node >setFunction(
            Node.class,
            obj -> {
                TextArea q;

                if ( commonData.isEditingMode() && ! commonData.isPreviewMode() ) {
                    q = new TextArea();
                    q.textProperty().bindBidirectional(
                        obj.getStr() );
                } else {
                    q = TextAreaHelper.create( obj.getStr().get() );
                }

                q.setWrapText( true );

                q.setPromptText( ApplicationConstants.PLACEHOLDER_QUESTION_CODE );

                q.setEditable(
                    commonData.isEditingMode() && ! commonData.isPreviewMode() ? true : false );
                q.setMouseTransparent(
                    commonData.isEditingMode() && ! commonData.isPreviewMode() ? false : true );
                q.setFocusTraversable(
                    false );
                q.setFont(
                    ApplicationConstants.FONT_OF_QUESTION_CODE );

                return addCheckBoxDeleteMark( q, obj );
            } );
    }

    private void setFunctionAnswerText() {

        choice = new Choice();

        ExerciseItem.AnswerText.< Node >setFunction(
            Node.class,
            obj -> {
                boolean isSingleChoiceAnswerText = obj instanceof SingleChoiceAnswerText;
                boolean isMultipleChoiceAnswerText = obj instanceof MultipleChoiceAnswerText;

                if ( !( isSingleChoiceAnswerText || isMultipleChoiceAnswerText ) ) {
                    assert false : String.format(
                        "%s",
                        obj.getClass() ); // TODO
                    return null;
                }

                HBox answerNode = new HBox();
                answerNode.setStyle( "-fx-alignment: top-left" );
                VBox btnContainer = new VBox();
                btnContainer.setStyle( "-fx-alignment: top-center" );
                btnContainer.setPadding( new Insets( 5, 0, 0, 0 ) );
                VBox txtContainer = new VBox();
                txtContainer.setStyle( "-fx-alignment: top-center" );
                answerNode.getChildren().addAll(
                    btnContainer,
                    txtContainer );
                VBox.setMargin( answerNode, new Insets( 0, 0, 20, 40 ) );

                ButtonBase btn = null;

                if ( isSingleChoiceAnswerText ) {
                    RadioButton rb = new RadioButton();
                    rb.setToggleGroup( choice.getToggleGroup() );

                    rb.selectedProperty().addListener( ( observable, oldValue, newValue ) -> {
                        ( ( AnswerText ) obj ).setSelected( newValue );
                    });

                    if ( ( ( AnswerText ) obj ).isSelected() && commonData.isEditingMode() ) {
                        choice.getToggleGroup().selectToggle( rb );
                    }

                    btn = rb;
                }

                if ( isMultipleChoiceAnswerText ) {
                    CheckBox cb = new CheckBox();
                    choice.getCheckBoxList().add( cb );
                    
//                    cb.selectedProperty().bindBidirectional(
//                        ( ( AnswerText ) obj ).getSelected() );
                    
                    cb.selectedProperty().addListener( ( observable, oldValue, newValue ) -> {
                        ( ( AnswerText ) obj ).setSelected( newValue );
                    });

                    if ( ( ( AnswerText ) obj ).isSelected() && commonData.isEditingMode() ) {
                        cb.setSelected( true );
                    }

                    btn = cb;
                }

//                if ( ( ( AnswerText ) obj ).isMarked() && commonData.isEditingMode() ) {
//                    // btn.setStyle("-fx-border-color: red;");
//                    btn.getStyleClass().add(
//                        "donald-duck" ); // TODO
//                }

                if ( commonData.getCurrentExerciseItem().isSelectionImmutable() ) {
                    btn.setDisable( true );
                }
                btnContainer.getChildren().add( btn );
                choice.getMap().put( btn, ( AnswerText ) obj );

                TextArea a;

                if ( commonData.isEditingMode() && ! commonData.isPreviewMode() ) {
                    a = new TextArea();
                    a.textProperty().bindBidirectional( obj.getStr() );
                } else {
                    a = TextAreaHelper.create( obj.getStr().get() );
                }

                a.setWrapText( true );

                a.setPromptText( ApplicationConstants.PLACEHOLDER_ANSWER_TEXT );

                a.setEditable(
                    commonData.isEditingMode() && ! commonData.isPreviewMode() ? true : false );

                a.setMouseTransparent(
                    commonData.isEditingMode() && ! commonData.isPreviewMode() ? false : true );

                a.setFocusTraversable( false );

                txtContainer.getChildren().add(
                    addCheckBoxDeleteMark( a, obj ) );

                return answerNode;
            } );
    }

    private void setFunctionSolutionText() {

        ExerciseItem.SolutionText.< Node >setFunction(
            Node.class,
            obj -> {
                TextArea s;

                if ( commonData.isEditingMode() && ! commonData.isPreviewMode() ) {
                    s = new TextArea();
                    s.textProperty().bindBidirectional(
                        obj.getStr() );
                } else {
                    s = TextAreaHelper.create( obj.getStr().get() );
                }

                s.setWrapText( true );

                s.setPromptText( ApplicationConstants.PLACEHOLDER_SOLUTION_TEXT );

                s.setEditable(
                    commonData.isEditingMode() && ! commonData.isPreviewMode() ? true : false );
                s.setMouseTransparent(
                    commonData.isEditingMode() && ! commonData.isPreviewMode() ? false : true );
                s.setFocusTraversable(
                    false );
                solutionPart.setVisible(
                    commonData.isEditingMode() ? true : false );

                return addCheckBoxDeleteMark( s, obj );
            } );
    }

    private Node addCheckBoxDeleteMark( TextArea q, ItemPart itemPart ) {

        if ( commonData.isEditingMode() && ! commonData.isPreviewMode() ) {
            HBox hb = new HBox();
            CheckBox cb = new CheckBox();
            hb.getChildren().addAll( q, cb );
            hb.setStyle( "-fx-alignment: bottom-left" );

            cb.selectedProperty().addListener( (objX, oldValueX, newValueX) -> {
                StringBuilder style = new StringBuilder();
                style.append( String.format( "-fx-background-color: %s;", newValueX ? "red" : "transparent" ) );
                style.append( "-fx-alignment: bottom-left;" );
                hb.setStyle( style.toString() );

                if ( newValueX ) {
                    commonData.getComponentsMarkedToBeDeleted().add( itemPart );
                } else {
                    commonData.getComponentsMarkedToBeDeleted().remove( itemPart );
                }
            });

            return hb;
        }

        return q;
    }


    public Choice getChoice() {

        return choice;
    }
}
