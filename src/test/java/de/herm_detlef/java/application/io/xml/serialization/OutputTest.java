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

package de.herm_detlef.java.application.io.xml.serialization;

import de.herm_detlef.java.application.CommonData;
import de.herm_detlef.java.application.io.xml.deserialization.Input;
import de.herm_detlef.java.application.mvc.model.ExerciseItem;
import javafx.collections.ObservableList;
import org.apache.commons.io.FileUtils;
import org.jdom2.JDOMException;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class OutputTest {

    @Test
    void writeExerciseItemListToFile() throws JDOMException, SAXException, IOException {

        String name = "01.xml";

        File file01 = FileUtils.toFile( OutputTest.class.getResource( name ) );
        List< ExerciseItem > exerciseItems01 = Input.readExerciseItemListFromFile( file01 );

        CommonData.prepareEditingMode( true, exerciseItems01 );
        CommonData.markSelectedAnswerPartItems( exerciseItems01 );

        File file = createTempFile( name );
        Output.writeExerciseItemListToFile( exerciseItems01, file );

        Scanner scanner1 = new Scanner( file01 );
        Scanner scanner2 = new Scanner( file );

        while ( scanner1.hasNext() && scanner2.hasNext() ) {
            String s1 = scanner1.next();
            String s2 = scanner2.next();
//            System.out.println( s1 + " ---- " + s2 );
            assertEquals( s1, s2 );
        }

        scanner1.close();
        scanner2.close();
    }

    private File createTempFile( String name ) throws IOException {
        String[] splittedName = name.split( "[.]" );
        return File.createTempFile( splittedName[0] + "_", "." + splittedName[1] );
    }

    @Test
    void validateCurrentExerciseItem() {
    }
}