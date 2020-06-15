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

package de.herm_detlef.java.application.io.xml.deserialization;

import org.apache.commons.io.FileUtils;
import org.jdom2.input.JDOMParseException;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;


import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class InputTest {

    @Test
    void importExerciseItemListFromFile() {

        // empty file
        File file01 = FileUtils.toFile( InputTest.class.getResource( "01.xml") );

        Exception exception = assertThrows( JDOMParseException.class, () -> Input.readExerciseItemListFromFile( file01 ) );

        String className = exception.getClass().getName();
        assertEquals("org.jdom2.input.JDOMParseException", className );

        String message = exception.getMessage();
        assertTrue( message.startsWith("Error on line 1 of document ") );
        assertTrue( message.matches(".*(01[.]xml:).*") );
        assertTrue( message.endsWith(" Premature end of file.") );



        // xml node CATALOG has no children
        File file02 = FileUtils.toFile( InputTest.class.getResource( "02.xml") );

        exception = assertThrows( SAXException.class, () -> Input.readExerciseItemListFromFile( file02 ) );

        className = exception.getClass().getName();
        assertEquals("org.xml.sax.SAXException", className );

        message = exception.getMessage();
        assertTrue( message.matches("^xml node CATALOG has no children$") );



        // xml node with invalid attribute value
        File file03 = FileUtils.toFile( InputTest.class.getResource( "03.xml") );

        exception = assertThrows( JDOMParseException.class, () -> Input.readExerciseItemListFromFile( file03 ) );

        className = exception.getClass().getName();
        assertEquals("org.jdom2.input.JDOMParseException", className );

        message = exception.getMessage();
        assertTrue( message.startsWith("Error on line 10 of document ") );
        assertTrue( message.matches(".*(03[.]xml:).*") );
        assertTrue( message.endsWith(" cvc-datatype-valid.1.2.1: 'tru' is not a valid value for 'boolean'.") );



        // xml node ID has invalid content: value cannot be parsed as integer, too big
        File file04 = FileUtils.toFile( InputTest.class.getResource( "04.xml") );

        exception = assertThrows( SAXException.class, () -> Input.readExerciseItemListFromFile( file04 ) );

        className = exception.getClass().getName();
        assertEquals("org.xml.sax.SAXException", className );

        message = exception.getMessage();
        assertTrue( message.matches("^content of xml node ID cannot be parsed as integer$") );



        // xml node ID has invalid content: value represents a negative Integer
        File file05 = FileUtils.toFile( InputTest.class.getResource( "05.xml") );

        exception = assertThrows( JDOMParseException.class, () -> Input.readExerciseItemListFromFile( file05 ) );

        className = exception.getClass().getName();
        assertEquals("org.jdom2.input.JDOMParseException", className );

        message = exception.getMessage();
        assertTrue( message.startsWith("Error on line 4 of document ") );
        assertTrue( message.matches(".*(05[.]xml:).*") );
        assertTrue( message.endsWith(" cvc-minInclusive-valid: Value '-1' is not facet-valid with respect to minInclusive '1' for type 'positiveInteger'.") );
    }
}