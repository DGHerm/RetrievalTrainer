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

package de.herm_detlef.java.application;


import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import de.herm_detlef.java.application.io.Export;
import de.herm_detlef.java.application.mvc.controller.app.ApplicationControllerImpl;
import de.herm_detlef.java.application.mvc.model.ExerciseItem;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import javax.inject.Inject;
import javax.inject.Singleton;

import static de.herm_detlef.java.application.ApplicationConstants.DEBUG;

/* @formatter:off */

/**
 * TODO
 * @author Detlef G. Herm, 2016-09-26
 * @since 1.0
 *
 */
@Singleton
public class CommonDataImpl implements CommonData {

    private final ApplicationPreferences                        applicationPreferences;

    private final Stage                                         primaryStage;
    private final ArrayList< ExerciseItem >                     exerciseItemListInitialMaster;
    private final ObservableList< ExerciseItem >                exerciseItemListMaster;         // master
    private final ObservableList< ExerciseItem >                exerciseItemListShuffledSubset; // slave
    private final ObjectProperty< ExerciseItem >                currentExerciseItemProperty;
    private final ObservableList< ExerciseItem.ItemPart >       componentsMarkedToBeDeleted;
    private final ExerciseItem                                  exerciseItemPrototype;
    private final BooleanProperty                               editingModeProperty;
    private final BooleanProperty                               previewModeProperty;

    private final ListChangeListener< ? super ExerciseItem >    exerciseItemListMasterChangeListener;
    private ListChangeListener< ? super ExerciseItem.ItemPart > exerciseItemComponentsChangeListener = c -> {};

    private Locale                                              currentLocale;

    private final ObjectProperty< File >                        recentlyOpenedFileProperty;
    private final ObjectProperty< File >                        recentlySavedFileProperty;

    private boolean                                             convenient = true;
    private boolean                                             shuffledSubset;
    private int                                                 maxLengthOfSubset;

    /**
     * TODO
     * <p>
     * @param primaryStage TODO
     * @since 1.0
     */
    @Inject
    public CommonDataImpl(Stage primaryStage,
                          ApplicationPreferences theApplicationPreferences ) {

        this.primaryStage              = primaryStage;
        applicationPreferences         = theApplicationPreferences;

        exerciseItemListInitialMaster  = new ArrayList<>();
        exerciseItemListMaster         = FXCollections.observableArrayList();
        exerciseItemListShuffledSubset = FXCollections.observableArrayList();
        currentExerciseItemProperty    = new SimpleObjectProperty<>();
        componentsMarkedToBeDeleted    = FXCollections.observableArrayList();
        exerciseItemPrototype          = new ExerciseItem();
        editingModeProperty            = new SimpleBooleanProperty();
        previewModeProperty            = new SimpleBooleanProperty();

        recentlyOpenedFileProperty     = new SimpleObjectProperty<>();
        recentlySavedFileProperty      = new SimpleObjectProperty<>();
        shuffledSubset                 = Boolean.parseBoolean(
                                            applicationPreferences
                                                .getUserPreferencesNode()
                                                .get( "RandomOrder", "false" ) );

        maxLengthOfSubset              = Integer.parseInt(
                                            applicationPreferences
                                                .getUserPreferencesNode()
                                                .get( "MaximumLengthOfSubset", 
                                                      Integer.toString( ApplicationConstants.MAXIMUM_NUMBER_OF_EXERCISE_ITEMS ) ) );
        exerciseItemListMasterChangeListener = c -> {

            if ( c.next() ) {

                if ( this.getCurrentExerciseItem() == null ) {
                    ExerciseItem.initializeGUI();
                    return;
                }

                exerciseItemListShuffledSubset.clear();
                exerciseItemListShuffledSubset.addAll( exerciseItemListMaster );

                if ( c.wasAdded() ) {
                    for ( Runnable r : this.getCurrentExerciseItem().getHandlersOnNewExerciseItem() ) {
                        r.run();
                    }
                }

                if ( c.wasRemoved() ) {
                    for ( Runnable r : this.getCurrentExerciseItem().getHandlersOnDeleteExerciseItem() ) {
                        r.run();
                    }
                }
            }
        };

        initialize();
    }

    private void initialize() {

        exerciseItemPrototype.createSingleChoiceModel();

        editingModeProperty.addListener(
            ( obj, oldValue, newValue ) -> {

                componentsMarkedToBeDeleted.clear();

                if ( newValue ) {
                    exerciseItemListMaster.addListener(
                        exerciseItemListMasterChangeListener );
                } else {
                    exerciseItemListMaster.removeListener(
                        exerciseItemListMasterChangeListener );
                }

                prepareEditingMode( newValue );
            } );

        currentExerciseItemProperty.addListener( (obj, oldValue, newValue ) -> {

            componentsMarkedToBeDeleted.clear();

            if ( oldValue != null ) {
                oldValue.getExerciseItemParts().removeListener(
                    exerciseItemComponentsChangeListener );
                oldValue.removeAllHandlersOfPrototype( exerciseItemPrototype );
            }
            if ( newValue != null ) {
                newValue.getExerciseItemParts().addListener(
                    exerciseItemComponentsChangeListener );
                newValue.addAllHandlersOfPrototype( exerciseItemPrototype );
            }
        });
    }

    private void prepareEditingMode( boolean isEditingMode) {

        for ( ExerciseItem exItem : exerciseItemListMaster ) {
            for ( ExerciseItem.ItemPart itemPart : exItem.getExerciseItemParts() ) {
                if ( itemPart instanceof ExerciseItem.AnswerText ) {
                    ExerciseItem.AnswerText answerPart = (ExerciseItem.AnswerText) itemPart;
                    answerPart.setSelected( isEditingMode && answerPart.isInitialMark() );
                }
            }
        }
    }

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    @Override
    public void addExerciseItemListMasterChangeListener() {

        exerciseItemListMaster.addListener(
            exerciseItemListMasterChangeListener );
    }

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    @Override
    public void removeExerciseItemListMasterChangeListener() {

        exerciseItemListMaster.removeListener(
            exerciseItemListMasterChangeListener );
    }

    /**
     * <em>Getter</em> method.
     * <p>
     * Returns a reference to the instance of class {@code Stage},
     * which has been given as argument to method {@link de.herm_detlef.java.application.Main#start start}
     * of class {@link de.herm_detlef.java.application.Main Main}.
     * <p>
     * This stage uses the scene for the root node, which is
     * controlled by the instance of class {@link ApplicationControllerImpl ApplicationController}
     * <p>
     * @since 1.0
     */
    @Override
    public Stage getPrimaryStage() {

        return primaryStage;
    }

    @Override
    public ApplicationPreferences getApplicationPreferences() {
        return applicationPreferences;
    }

    /**
     * <em>Getter</em> method.
     * <p>
     * @since 1.0
     */
    @Override
    public ArrayList< ExerciseItem > getExerciseItemListInitialMaster() {

        return exerciseItemListInitialMaster;
    }

    /**
     * <em>Getter</em> method.
     * <p>
     * @since 1.0
     */
    @Override
    public ObservableList< ExerciseItem > getExerciseItemListMaster() {

        return exerciseItemListMaster;
    }

    /**
     * <em>Getter</em> method.
     * <p>
     * @since 1.0
     */
    @Override
    public ObservableList< ExerciseItem > getExerciseItemListShuffledSubset() {

        return exerciseItemListShuffledSubset;
    }

    /**
     * <em>Getter</em> method.
     * <p>
     * @since 1.0
     */
    @Override
    public ExerciseItem getExerciseItemPrototype() {

        return exerciseItemPrototype;
    }

    /**
     * <em>Getter</em> method.
     * <p>
     * @since 1.0
     */
    @Override
    public ExerciseItem getCurrentExerciseItem() {

        return currentExerciseItemProperty.getValue();
    }

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    @Override
    public void setCurrentExerciseItem(ExerciseItem currentExerciseItem) {

        if (DEBUG) {
            assert currentExerciseItemProperty.getValue() == null
                    || currentExerciseItemProperty.getValue().getItemId() > 0;
        }
        currentExerciseItemProperty.setValue( currentExerciseItem );
    }

    /**
     * <em>Getter</em> method.
     * <p>
     * @return {@code ObjectProperty<ExerciseItem>}
     * @since 1.0
     */
    @Override
    public ObjectProperty< ExerciseItem > getCurrentExerciseItemProperty() {

        return currentExerciseItemProperty;
    }

    /**
     * <em>Getter</em> method.
     * <p>
     * @since 1.0
     */
    @Override
    public ObservableList< ExerciseItem.ItemPart > getComponentsMarkedToBeDeleted() {

        return componentsMarkedToBeDeleted;
    }

    /**
     * <em>Getter</em> method.
     * <p>
     * @since 1.0
     */
    @Override
    public boolean isEditingMode() {

        return editingModeProperty.get();
    }

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    @Override
    public void setEditingMode(boolean editingMode) {

        this.editingModeProperty.set(
            editingMode );
    }

    /**
     * <em>Getter</em> method.
     * <p>
     * @since 1.0
     */
    @Override
    public BooleanProperty getEditingModeProperty() {
        return editingModeProperty;
    }

    /**
     * <em>Getter</em> method.
     * <p>
     * @since 1.0
     */
    @Override
    public boolean isPreviewMode() {

        return previewModeProperty.get();
    }

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    @Override
    public void setPreviewMode(boolean previewMode) {

        previewModeProperty.set(
            previewMode );
    }

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    @Override
    public void createExerciseItem() {

        ExerciseItem newItem = new ExerciseItem( exerciseItemPrototype );
        newItem.setItemId(
            exerciseItemListMaster.size() + 1 ); // TODO
        newItem.getExerciseItemParts().addListener(
            exerciseItemComponentsChangeListener );
        setCurrentExerciseItem(
            newItem );
        exerciseItemListMaster.add(
                newItem );
        exerciseItemListShuffledSubset.addAll(
                exerciseItemListMaster );

        if ( isConvenient() ) {
            newItem.addQuestionText(null);

            if ( newItem.getChoiceModel() instanceof ExerciseItem.SingleChoiceAnswerText ) {
                newItem.createSingleChoiceModel();
            }

            if ( newItem.getChoiceModel() instanceof ExerciseItem.MultipleChoiceAnswerText ) {
                newItem.createMultipleChoiceModel();
            }

            newItem.addAnswerText( null, false );
            newItem.addAnswerText( null, false );

            newItem.addSolutionText(null);

            Export.validateCurrentExerciseItem( this, false );
        }
    }

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    @Override
    public void markSelectedAnswerPartItems() {

        for ( ExerciseItem exItem : exerciseItemListMaster ) {
            exItem.markSelectedAnswerPartItems();
        }
    }


    /**
     * TODO
     * <p>
     * @since 1.0
     */
    @Override
    public boolean checkNeedsSave() {

        if ( ! isEditingMode() )
            return false;

        boolean needsSave = ! exerciseItemListMaster.containsAll( exerciseItemListInitialMaster );
        needsSave = needsSave || ! exerciseItemListInitialMaster.containsAll( exerciseItemListMaster );

        for ( ExerciseItem exItem : exerciseItemListMaster ) {
            for ( ExerciseItem.ItemPart itemPart : exItem.getExerciseItemParts() ) {
                needsSave = needsSave || itemPart.needsSave();
                if ( needsSave )
                    return true;
            }
        }

        return false;
    }

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    @Override
    public void savedExerciseItemListMaster() {

        for ( ExerciseItem exItem : exerciseItemListMaster ) {
            for ( ExerciseItem.ItemPart itemPart : exItem.getExerciseItemParts() ) {
                itemPart.saved();
            }
        }
    }

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    @Override
    public void setExerciseItemComponentsChangeListener(ListChangeListener<? super ExerciseItem.ItemPart> exerciseItemComponentsChangeListener) {

        this.exerciseItemComponentsChangeListener = exerciseItemComponentsChangeListener;
    }

    /**
     * <em>Getter</em> method.
     * <p>
     * see method {@link CommonDataImpl#setCurrentLocale setCurrentLocale}
     * <p>
     * @since 1.0
     */
    @Override
    public Locale getCurrentLocale() {

        return currentLocale;
    }

    /**
     * <em>Setter</em> method.
     * <p>
     * see method {@link CommonDataImpl#getCurrentLocale getCurrentLocale}
     * <p>
     * @since 1.0
     */
    @Override
    public void setCurrentLocale(Locale currentLocale) {

        this.currentLocale = currentLocale;
    }

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    @Override
    public ObjectProperty< File > getRecentlyOpenedFileProperty() {
        return recentlyOpenedFileProperty;
    }

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    @Override
    public void setRecentlyOpenedFile(File recentlyOpenedFile) {
        recentlyOpenedFileProperty.set( recentlyOpenedFile );
    }

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    @Override
    public ObjectProperty<File> getRecentlySavedFileProperty() {
        return recentlySavedFileProperty;
    }

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    @Override
    public void setRecentlySavedFile(File recentlySavedFile) {
        recentlySavedFileProperty.set( recentlySavedFile );
    }

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    @Override
    public boolean isConvenient() {

        return convenient;
    }

    @Override
    public boolean isShuffledSubset() {
        return shuffledSubset;
    }
    @Override
    public void setShuffledSubset(boolean shuffledSubset) {
        this.shuffledSubset = shuffledSubset;
    }
    @Override
    public int getMaxLengthOfSubset() {
        return maxLengthOfSubset;
    }
    @Override
    public void setMaxLengthOfSubset(int maxLengthOfSubset) {
        this.maxLengthOfSubset = maxLengthOfSubset;
    }
}
