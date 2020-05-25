package de.herm_detlef.java.application.io;

import de.herm_detlef.java.application.CommonData;
import de.herm_detlef.java.application.mvc.model.ExerciseItem;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class ExportTest {

    @Test
    void exportExerciseItemListToFile() {
    }

    @Test
    void validateCurrentExerciseItem() {

        ExerciseItem exerciseItem = new ExerciseItem();
        exerciseItem.addQuestionText("");
        exerciseItem.createSingleChoiceModel();
        exerciseItem.addAnswerText("", false );
        exerciseItem.addAnswerText("", true );
        exerciseItem.addSolutionText("");

        CommonData commonData = new CommonData(null, null );
        commonData.setCurrentExerciseItem( exerciseItem );
        commonData.getExerciseItemListMaster().add( exerciseItem );

        assertTrue( Export.validateCurrentExerciseItem( commonData, false ) );
    }
}