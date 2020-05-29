package de.herm_detlef.java.application;

import de.herm_detlef.java.application.mvc.model.ExerciseItem;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

public interface CommonData {
    void addExerciseItemListMasterChangeListener();

    void removeExerciseItemListMasterChangeListener();

    Stage getPrimaryStage();

    ApplicationPreferences getApplicationPreferences();

    ArrayList<ExerciseItem> getExerciseItemListInitialMaster();

    ObservableList< ExerciseItem > getExerciseItemListMaster();

    ObservableList< ExerciseItem > getExerciseItemListShuffledSubset();

    ExerciseItem getExerciseItemPrototype();

    ExerciseItem getCurrentExerciseItem();

    void setCurrentExerciseItem(ExerciseItem currentExerciseItem);

    ObjectProperty< ExerciseItem > getCurrentExerciseItemProperty();

    ObservableList< ExerciseItem.ItemPart > getComponentsMarkedToBeDeleted();

    boolean isEditingMode();

    void setEditingMode(boolean editingMode);

    BooleanProperty getEditingModeProperty();

    boolean isPreviewMode();

    void setPreviewMode(boolean previewMode);

    void createExerciseItem();

    void markSelectedAnswerPartItems();

    boolean checkNeedsSave();

    void savedExerciseItemListMaster();

    void setExerciseItemComponentsChangeListener(ListChangeListener<? super ExerciseItem.ItemPart> exerciseItemComponentsChangeListener);

    Locale getCurrentLocale();

    void setCurrentLocale(Locale currentLocale);

    ObjectProperty<File> getRecentlyOpenedFileProperty();

    void setRecentlyOpenedFile(File recentlyOpenedFile);

    ObjectProperty<File> getRecentlySavedFileProperty();

    void setRecentlySavedFile(File recentlySavedFile);

    boolean isConvenient();

    boolean isShuffledSubset();

    void setShuffledSubset(boolean shuffledSubset);

    int getMaxLengthOfSubset();

    void setMaxLengthOfSubset(int maxLengthOfSubset);
}
