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

package de.herm_detlef.java.application.model;


import java.util.ArrayList;
import java.util.function.Function;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

/* @formatter:off */

/**
 * TODO
 *
 * @author Detlef G. Herm, 2016-09-26
 * @since 1.0
 *
 */
public class ExerciseItem {

    public static abstract class ItemPart {

        private final ExerciseItem parent;

        private StringProperty     str = new SimpleStringProperty();
        private int                initialHashCodeOfStr;

        private ItemPart( String str, ExerciseItem parent ) {
            this.str.set( str == null ? "" : str );
            this.parent = parent;

            initialHashCodeOfStr = this.str.get().hashCode();

            this.str.addListener( (obj, oldValue, newValue) -> {
                //currentHashCodeOfStr = newValue.hashCode();
                for ( Runnable r : parent.handlersOnModifiedExerciseItemPart ) {
                    r.run();
                }
            });
        }

        public final StringProperty getStr() {

            return str;
        }

        public abstract Object get();

        private static Object get( ItemPart itemPart, Class< ? > type, Function< ItemPart, ? > function ) {

            if ( function == null || type == null ) return null;
            Object obj = function.apply( itemPart );
            return type.isInstance( obj ) ? obj : null;
        }


        public ExerciseItem getParent() {

            return parent;
        }

        public boolean needsSave() {
            return initialHashCodeOfStr != str.get().hashCode();
        }

        public void saved() {
            initialHashCodeOfStr = str.get().hashCode();
        }
    }

    public static class QuestionText extends ItemPart {

        private static Class< ? >              type;
        private static Function< ItemPart, ? > function;

        public QuestionText( String str, ExerciseItem parent ) {
            super( str, parent );
        }

        public static < R > void setFunction( Class< R > type, Function< ItemPart, R > function ) {

            QuestionText.type = type;
            QuestionText.function = function;
        }

        @Override
        public Object get() {

            return ItemPart.get( this, type, function );
        }
    }

    public static class QuestionText2 extends ItemPart {

        private static Class< ? >              type;
        private static Function< ItemPart, ? > function;

        public QuestionText2( String str, ExerciseItem parent ) {
            super( str, parent );
        }

        public static < R > void setFunction( Class< R > type, Function< ItemPart, R > function ) {

            QuestionText2.type = type;
            QuestionText2.function = function;
        }

        @Override
        public Object get() {

            return ItemPart.get( this, type, function );
        }
    }

    public static class QuestionCode extends ItemPart {

        private static Class< ? >              type;
        private static Function< ItemPart, ? > function;

        public QuestionCode( String str, ExerciseItem parent ) {
            super( str, parent );
        }

        public static < R > void setFunction( Class< R > type, Function< ItemPart, R > function ) {

            QuestionCode.type = type;
            QuestionCode.function = function;
        }

        @Override
        public Object get() {

            return ItemPart.get( this, type, function );
        }
    }

    public static abstract class AnswerText extends ItemPart {

        private static Class< ? >              type;
        private static Function< ItemPart, ? > function;
        private BooleanProperty                selected = new SimpleBooleanProperty( false );

        // value of xml node attribute "mark" at reading data from disc
        private boolean                        initialMark;

        // gets the xml node attribute "mark" at saving data to disc
        private boolean                        mark;

        public AnswerText( String str,
                           boolean initialMark,
                           ExerciseItem parent ) {

            super( str, parent );
            this.initialMark = initialMark;
        }

        public static < R > void setFunction( Class< R > type, Function< ItemPart, R > function ) {
            AnswerText.type = type;
            AnswerText.function = function;
        }

        @Override
        public Object get() {
            return ItemPart.get( this, type, function );
        }

        // needed for multiple choice ( bindBidirectional CheckBox )
        public BooleanProperty getSelected() {
            return selected;
        }

        public void setSelected( boolean selected ) {
            this.selected.setValue( selected );
        }

        public boolean isSelected() {
            return selected.getValue();
        }

        public boolean isInitialMark() {
            return initialMark;
        }

        public void setMark( boolean mark ) {
            this.mark = mark;
        }

        public boolean isMarked() {
            return mark;
        }

        public boolean needsSave() {
            return ( initialMark != selected.getValue() ) || super.needsSave();
        }

        public void prepareSave() {
            // prior to xml output
            mark = selected.getValue();
        }

        public void saved() {
            super.saved();
            initialMark = mark;
            selected.setValue( initialMark );
        }
    }

    public static class SingleChoiceAnswerText extends AnswerText {

        public SingleChoiceAnswerText( String str,
                                       boolean initialMark,
                                       ExerciseItem parent ) {
            super( str, initialMark, parent );
        }
    }

    public static class MultipleChoiceAnswerText extends AnswerText {

        public MultipleChoiceAnswerText( String str,
                                         boolean initialMark,
                                         ExerciseItem parent ) {
            super( str, initialMark, parent );
        }
    }

    public static class SolutionText extends ItemPart {

        private static Class< ? >              type;
        private static Function< ItemPart, ? > function;

        public SolutionText( String str, ExerciseItem parent ) {
            super( str, parent );
        }

        public static < R > void setFunction( Class< R > type, Function< ItemPart, R > function ) {

            SolutionText.type = type;
            SolutionText.function = function;
        }

        @Override
        public Object get() {

            return ItemPart.get( this, type, function );
        }
    }

    private final ObservableList< ItemPart >   components                                    = FXCollections.observableArrayList();

    private int                                itemId                                        = -1;

    private final ObjectProperty< AnswerText > choiceModelProperty;

    private boolean                            immutableSelection                            = false;

    private final static ArrayList< Runnable > handlersToInitializeGUI                       = new ArrayList<>();

    private final ArrayList< Runnable >        handlersOnNewExerciseItem                     = new ArrayList<>();

    private final ArrayList< Runnable >        handlersOnDeleteExerciseItem                  = new ArrayList<>();

    private final ArrayList< Runnable >        handlersOnPreviewExerciseItem                 = new ArrayList<>();

    private final ArrayList< Runnable >        handlersToPreventMoreQuestionText             = new ArrayList<>();

    private final ArrayList< Runnable >        handlersToAllowMoreQuestionText               = new ArrayList<>();

    private final ArrayList< Runnable >        handlersToPreventMoreQuestionCode             = new ArrayList<>();

    private final ArrayList< Runnable >        handlersToAllowMoreQuestionCode               = new ArrayList<>();

    private final ArrayList< Runnable >        handlersToPreventMoreSingleChoiceAnswerText   = new ArrayList<>();

    private final ArrayList< Runnable >        handlersToAllowMoreSingleChoiceAnswerText     = new ArrayList<>();

    private final ArrayList< Runnable >        handlersToPreventMoreMultipleChoiceAnswerText = new ArrayList<>();

    private final ArrayList< Runnable >        handlersToAllowMoreMultipleChoiceAnswerText   = new ArrayList<>();

    private final ArrayList< Runnable >        handlersOnConvertAnswerTextParts              = new ArrayList<>();

    private final ArrayList< Runnable >        handlersToPreventMoreSolutionText             = new ArrayList<>();

    private final ArrayList< Runnable >        handlersToAllowMoreSolutionText               = new ArrayList<>();

    private final ArrayList< Runnable >        handlersOnModifiedExerciseItemPart            = new ArrayList<>();

    public ExerciseItem() {
        choiceModelProperty = new SimpleObjectProperty<>();
        initialize();
    }

    public ExerciseItem( ExerciseItem prototype ) {
        choiceModelProperty = new SimpleObjectProperty<>();
        choiceModelProperty.set( prototype.getChoiceModel() );
        initialize();
        addAllHandlersOfPrototype( prototype );
    }

    private void initialize() {

        choiceModelProperty.addListener( (obj, oldValue, newValue) -> {
            if ( newValue == null || newValue.get() == null )
                return;
            convertAnswerTextParts();
        });
    }

    private void convertAnswerTextParts() {

        FilteredList< ItemPart > filteredList = components.filtered( ( item ) -> item instanceof AnswerText );

        if ( filteredList.isEmpty() )
            return;

        ArrayList< ItemPart > answerPartList = new ArrayList<>();
        answerPartList.addAll( filteredList );

        components.removeAll( filteredList );

        for ( ItemPart itemPart : answerPartList ) {
            addAnswerText( itemPart.getStr().get(), false );
        }

        for ( Runnable r : handlersOnConvertAnswerTextParts ) {
            r.run();
        }
    }

    public void addAllHandlersOfPrototype( ExerciseItem prototype ) {

        handlersOnNewExerciseItem                    .addAll( prototype.getHandlersOnNewExerciseItem() );
        handlersOnDeleteExerciseItem                 .addAll( prototype.getHandlersOnDeleteExerciseItem() );
        handlersOnPreviewExerciseItem                .addAll( prototype.getHandlersOnPreviewExerciseItem() );
        handlersToPreventMoreQuestionText            .addAll( prototype.getHandlersToPreventMoreQuestionText() );
        handlersToAllowMoreQuestionText              .addAll( prototype.getHandlersToAllowMoreQuestionText() );
        handlersToPreventMoreQuestionCode            .addAll( prototype.getHandlersToPreventMoreQuestionCode() );
        handlersToAllowMoreQuestionCode              .addAll( prototype.getHandlersToAllowMoreQuestionCode() );
        handlersToPreventMoreSingleChoiceAnswerText  .addAll( prototype.getHandlersToPreventMoreSingleChoiceAnswerText() );
        handlersToAllowMoreSingleChoiceAnswerText    .addAll( prototype.getHandlersToAllowMoreSingleChoiceAnswerText() );
        handlersToPreventMoreMultipleChoiceAnswerText.addAll( prototype.getHandlersToPreventMoreMultipleChoiceAnswerText() );
        handlersToAllowMoreMultipleChoiceAnswerText  .addAll( prototype.getHandlersToAllowMoreMultipleChoiceAnswerText() );
        handlersOnConvertAnswerTextParts             .addAll( prototype.getHandlersOnConvertAnswerTextParts() );
        handlersToPreventMoreSolutionText            .addAll( prototype.getHandlersToPreventMoreSolutionText() );
        handlersToAllowMoreSolutionText              .addAll( prototype.getHandlersToAllowMoreSolutionText() );
        handlersOnModifiedExerciseItemPart           .addAll( prototype.getHandlersOnModifiedExerciseItemPart() );
    }

    public void removeAllHandlersOfPrototype( ExerciseItem prototype ) {

        handlersOnNewExerciseItem                    .removeAll( prototype.getHandlersOnNewExerciseItem() );
        handlersOnDeleteExerciseItem                 .removeAll( prototype.getHandlersOnDeleteExerciseItem() );
        handlersOnPreviewExerciseItem                .removeAll( prototype.getHandlersOnPreviewExerciseItem() );
        handlersToPreventMoreQuestionText            .removeAll( prototype.getHandlersToPreventMoreQuestionText() );
        handlersToAllowMoreQuestionText              .removeAll( prototype.getHandlersToAllowMoreQuestionText() );
        handlersToPreventMoreQuestionCode            .removeAll( prototype.getHandlersToPreventMoreQuestionCode() );
        handlersToAllowMoreQuestionCode              .removeAll( prototype.getHandlersToAllowMoreQuestionCode() );
        handlersToPreventMoreSingleChoiceAnswerText  .removeAll( prototype.getHandlersToPreventMoreSingleChoiceAnswerText() );
        handlersToAllowMoreSingleChoiceAnswerText    .removeAll( prototype.getHandlersToAllowMoreSingleChoiceAnswerText() );
        handlersToPreventMoreMultipleChoiceAnswerText.removeAll( prototype.getHandlersToPreventMoreMultipleChoiceAnswerText() );
        handlersToAllowMoreMultipleChoiceAnswerText  .removeAll( prototype.getHandlersToAllowMoreMultipleChoiceAnswerText() );
        handlersOnConvertAnswerTextParts             .removeAll( prototype.getHandlersOnConvertAnswerTextParts() );
        handlersToPreventMoreSolutionText            .removeAll( prototype.getHandlersToPreventMoreSolutionText() );
        handlersToAllowMoreSolutionText              .removeAll( prototype.getHandlersToAllowMoreSolutionText() );
        handlersOnModifiedExerciseItemPart           .removeAll( prototype.getHandlersOnModifiedExerciseItemPart() );
    }

    public static void addHandlerToInitializeGUI( Runnable handler ) {

        handlersToInitializeGUI.add( handler );
    }

    public void addHandlerOnNewExerciseItem( Runnable onNewExerciseItem ) {

        handlersOnNewExerciseItem.add( onNewExerciseItem );
    }

    public void addHandlerOnDeleteExerciseItem( Runnable onDeleteExerciseItem ) {

        handlersOnDeleteExerciseItem.add( onDeleteExerciseItem );
    }

    public void addHandlerOnPreviewExerciseItem( Runnable onPreviewExerciseItem ) {

        handlersOnPreviewExerciseItem.add( onPreviewExerciseItem );
    }

    public void addHandlerToPreventMoreQuestionText( Runnable preventMoreQuestionText ) {

        handlersToPreventMoreQuestionText.add( preventMoreQuestionText );
    }

    public void addHandlerToAllowMoreQuestionText( Runnable allowMoreQuestionText ) {

        handlersToAllowMoreQuestionText.add( allowMoreQuestionText );
    }

    public void addHandlerToPreventMoreQuestionCode( Runnable preventMoreQuestionCode ) {

        handlersToPreventMoreQuestionCode.add( preventMoreQuestionCode );
    }

    public void addHandlerToAllowMoreQuestionCode( Runnable allowMoreQuestionCode ) {

        handlersToAllowMoreQuestionCode.add( allowMoreQuestionCode );
    }

    public void addHandlerToPreventMoreSingleChoiceAnswerText( Runnable preventMoreSingleChoiceAnswerText ) {

        handlersToPreventMoreSingleChoiceAnswerText.add( preventMoreSingleChoiceAnswerText );
    }

    public void addHandlerToAllowMoreSingleChoiceAnswerText( Runnable allowMoreSingleChoiceAnswerText ) {

        handlersToAllowMoreSingleChoiceAnswerText.add( allowMoreSingleChoiceAnswerText );
    }

    public void addHandlerToPreventMoreMultipleChoiceAnswerText( Runnable preventMoreMultipleChoiceAnswerText ) {

        handlersToPreventMoreMultipleChoiceAnswerText.add( preventMoreMultipleChoiceAnswerText );
    }

    public void addHandlerToAllowMoreMultipleChoiceAnswerText( Runnable allowMoreMultipleChoiceAnswerText ) {

        handlersToAllowMoreMultipleChoiceAnswerText.add( allowMoreMultipleChoiceAnswerText );
    }

    public void addHandlerOnChangeAnswerMode( Runnable changeAnswerMode ) {
        handlersOnConvertAnswerTextParts.add( changeAnswerMode );
    }

    public void addHandlerToPreventMoreSolutionText( Runnable preventMoreSolutionText ) {

        handlersToPreventMoreSolutionText.add( preventMoreSolutionText );
    }

    public void addHandlerToAllowMoreSolutionText( Runnable allowMoreSolutionText ) {

        handlersToAllowMoreSolutionText.add( allowMoreSolutionText );
    }

    public void addHandlerOnModifiedExerciseItemPart( Runnable modifiedExerciseItemPart ) {

        handlersOnModifiedExerciseItemPart.add( modifiedExerciseItemPart );
    }

    public ArrayList< Runnable > getHandlersOnNewExerciseItem() {

        return handlersOnNewExerciseItem;
    }

    public ArrayList< Runnable > getHandlersOnDeleteExerciseItem() {

        return handlersOnDeleteExerciseItem;
    }

    public ArrayList< Runnable > getHandlersOnPreviewExerciseItem() {

        return handlersOnDeleteExerciseItem;
    }

    public ArrayList< Runnable > getHandlersToPreventMoreQuestionText() {

        return handlersToPreventMoreQuestionText;
    }

    public ArrayList< Runnable > getHandlersToAllowMoreQuestionText() {

        return handlersToAllowMoreQuestionText;
    }

    public ArrayList< Runnable > getHandlersToPreventMoreQuestionCode() {

        return handlersToPreventMoreQuestionCode;
    }

    public ArrayList< Runnable > getHandlersToAllowMoreQuestionCode() {

        return handlersToAllowMoreQuestionCode;
    }

    public ArrayList< Runnable > getHandlersToPreventMoreSingleChoiceAnswerText() {

        return handlersToPreventMoreSingleChoiceAnswerText;
    }

    public ArrayList< Runnable > getHandlersToAllowMoreSingleChoiceAnswerText() {

        return handlersToAllowMoreSingleChoiceAnswerText;
    }

    public ArrayList< Runnable > getHandlersToPreventMoreMultipleChoiceAnswerText() {

        return handlersToPreventMoreMultipleChoiceAnswerText;
    }

    public ArrayList< Runnable > getHandlersToAllowMoreMultipleChoiceAnswerText() {

        return handlersToAllowMoreMultipleChoiceAnswerText;
    }

    public ArrayList< Runnable > getHandlersOnConvertAnswerTextParts() {

        return handlersOnConvertAnswerTextParts;
    }

    public ArrayList< Runnable > getHandlersToPreventMoreSolutionText() {

        return handlersToPreventMoreSolutionText;
    }

    public ArrayList< Runnable > getHandlersToAllowMoreSolutionText() {

        return handlersToAllowMoreSolutionText;
    }

    public ArrayList< Runnable > getHandlersOnModifiedExerciseItemPart() {

        return handlersOnModifiedExerciseItemPart;
    }


    private void ruleDrivenAdd( Class< ? extends ItemPart > type, ItemPart part ) {

        if ( type.isInstance( part ) ) {

            int size = components.size();

            // ********************************************************
            //
            // DO NOT CHANGE THE ORDER OF FOLLOWING RULES !
            //
            // DO NOT CHANGE THE ORDER OF THE PARTS OF ANY RULE !
            //
            // ********************************************************

            // Rule 1: 'Empty Components List' -----------------
            if ( size == 0 ) {
                components.add( part );
                return;
            }

            // Rule 2: 'QuestionText' --------------------------
            if ( part instanceof QuestionText ) {

                if ( contains( QuestionText.class, 0 ) ) {

                    // always at the beginning
                    components.add( 0, part );
                    return;
                }
            }

            // Rule 3: 'QuestionCode' --------------------------
            if ( part instanceof QuestionCode ) {

                if ( contains( QuestionCode.class, 0 ) ) {

                    if ( contains( QuestionText.class, 0 ) ) {
                        // always before AnswerText or SolutionText
                        components.add( 0, part );
                    } else if ( size == 1 && contains( QuestionText.class, 1 ) ) {

                        components.add( part );
                    }
                    else {
                        // after QuestionText
                        components.add( 1, part );
                    }
                    return;
                }
            }

            // Rule 4: 'QuestionText2' --------------------------
            if ( part instanceof QuestionText2 ) {

                if ( contains( QuestionText.class, 1 ) && contains( QuestionCode.class, 1 ) ) {

                    for ( int index = size - 1; index >= 0; --index ) {

                        if ( components.get( index ) instanceof QuestionCode ) {
                            // always directly after QuestionCode
                            if ( index == size - 1 ) {
                                components.add( part );
                            } else {
                                components.add( index + 1, part );
                            }
                            return;
                        }
                    }
                }
            }

            // Rule 5: 'AnswerText' ----------------------------
            if ( part instanceof AnswerText ) {

                if ( ( part instanceof MultipleChoiceAnswerText && containsAtLeast( SingleChoiceAnswerText.class, 1 ) )
                     || ( part instanceof SingleChoiceAnswerText )
                        && containsAtLeast( MultipleChoiceAnswerText.class, 1 ) ) {}

                if ( contains( SolutionText.class, 1 ) ) {
                    // before SolutionText
                    components.add( components.size() - 1, part );
                } else {
                    components.add( part );
                }
                return;
            }

            // Rule 5: 'SolutionText' --------------------------
            if ( part instanceof SolutionText ) {

                if ( contains( SolutionText.class, 0 ) ) {
                    // always at the end of components list
                    components.add( part );
                }
            }
        }
    }

    private boolean contains(Class< ? extends ItemPart > type, int count ) {

        FilteredList< ItemPart > filteredList = components.filtered( type::isInstance );
        return filteredList.size() == count;
    }

    private boolean containsAtLeast(Class< ? extends ItemPart > type, int count ) {

        FilteredList< ItemPart > filteredList = components.filtered( type::isInstance );
        return filteredList.size() >= count;
    }

    public static boolean contains( ExerciseItem item, Class< ? extends ItemPart > type, int count ) {

        return item.contains( type, count );
    }

    public static boolean containsAtLeast( ExerciseItem item, Class< ? extends ItemPart > type, int count ) {

        return item.containsAtLeast( type, count );
    }

    public static void initializeGUI() {

        for ( Runnable r : handlersToInitializeGUI ) {
            r.run();
        }
    }

    public void integrityCheck() {

        for ( Runnable r : contains( QuestionText.class, 0 ) && contains( QuestionCode.class, 0 )
                           || contains( QuestionText.class, 0 ) && contains( QuestionCode.class, 1 )
                           || contains( QuestionText.class, 1 ) && contains( QuestionCode.class, 1 ) && contains( QuestionText2.class, 0 )
                           ? handlersToAllowMoreQuestionText
                           : handlersToPreventMoreQuestionText ) {
            r.run();
        }

        for ( Runnable r : contains( QuestionCode.class, 0 )
                           ? handlersToAllowMoreQuestionCode
                           : handlersToPreventMoreQuestionCode ) {
            r.run();
        }

        for ( Runnable r : contains( SolutionText.class, 0 )
                           ? handlersToAllowMoreSolutionText
                           : handlersToPreventMoreSolutionText ) {
            r.run();
        }
    }

    public boolean isSelectionImmutable() {

        return immutableSelection;
    }

    public void setSelectionImmutable( boolean immutableSelection ) {

        this.immutableSelection = immutableSelection;
    }

    public ObservableList< ItemPart > getExerciseItemParts() {

        return components;
    }

    public int getItemId() {

        return itemId;
    }

    public void setItemId( int itemId ) {

        this.itemId = itemId;// value of node ID in catalog_of_questions.xml
    }

    public ItemPart addQuestionText( String str ) {

        ItemPart part = new QuestionText( str, this );
        ruleDrivenAdd( QuestionText.class, part );
        return part;
    }

    public ItemPart addQuestionText2( String str ) {

        ItemPart part = new QuestionText2( str, this );
        ruleDrivenAdd( QuestionText2.class, part );
        return part;
    }

    public ItemPart addQuestionCode( String str ) {

        ItemPart part = new QuestionCode( str, this );
        ruleDrivenAdd( QuestionCode.class, part );
        return part;
    }

    public void createSingleChoiceModel() {

        choiceModelProperty.set( new SingleChoiceAnswerText( null, false, this ) );
    }

    public void createMultipleChoiceModel() {

        choiceModelProperty.set( new MultipleChoiceAnswerText( null, false, this ) );
    }

    public ObjectProperty< AnswerText > getChoiceModelProperty() {

        return choiceModelProperty;
    }

    public AnswerText getChoiceModel() {

        return choiceModelProperty.getValue();
    }

    public ItemPart addAnswerText( String str, boolean initialMark ) {

        assert choiceModelProperty != null
               && ( choiceModelProperty.get() instanceof SingleChoiceAnswerText
                    || choiceModelProperty.get() instanceof MultipleChoiceAnswerText );

        ItemPart part = null;

        if ( choiceModelProperty.get() instanceof SingleChoiceAnswerText ) {
            part = new SingleChoiceAnswerText( str, initialMark, this );
            ruleDrivenAdd( SingleChoiceAnswerText.class, part );
        }

        if ( choiceModelProperty.get() instanceof MultipleChoiceAnswerText ) {
            part = new MultipleChoiceAnswerText( str, initialMark, this );
            ruleDrivenAdd( MultipleChoiceAnswerText.class, part );
        }

        return part;
    }

    public ItemPart addSolutionText( String str ) {

        ItemPart part = new SolutionText( str, this );
        ruleDrivenAdd( SolutionText.class, part );
        return part;
    }

    public boolean isFinalQuestionPart() {
        return contains( QuestionText.class, 1 ) && contains( QuestionCode.class, 1 );
    }

    public void markSelectedAnswerPartItems() {
        for ( ItemPart itemPart : components ) {
            if ( itemPart instanceof AnswerText )  {
                ((AnswerText) itemPart).prepareSave();
            }
        }
    }

    public boolean hasMarkedAnswerPartItems() {

        for ( ItemPart itemPart : components ) {
            if ( itemPart instanceof AnswerText )  {
                if ( ((AnswerText) itemPart).isMarked() )
                    return true;
            }
        }

        return false;
    }
}
