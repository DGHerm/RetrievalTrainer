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

package de.herm_detlef.java.application.io;

import org.jdom2.input.JDOMParseException;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;


import static org.junit.jupiter.api.Assertions.*;

class ImportTest {

    @Test
    void importExerciseItemListFromFile() {

        // empty file
        String fileName01 = ImportTest.class.getResource("01.xml").getPath();

        Exception exception = assertThrows( JDOMParseException.class, () -> Import.importExerciseItemListFromFile( fileName01 ) );

        String className = exception.getClass().getName();
        assertEquals("org.jdom2.input.JDOMParseException", className );

        String message = exception.getMessage();
        assertTrue( message.startsWith("Error on line 1 of document ") );
        assertTrue( message.matches(".*(01[.]xml:).*") );
        assertTrue( message.endsWith(" Premature end of file.") );



        // xml node CATALOG has no children
        String fileName02 = ImportTest.class.getResource("02.xml").getPath();

        exception = assertThrows( SAXException.class, () -> Import.importExerciseItemListFromFile( fileName02 ) );

        className = exception.getClass().getName();
        assertEquals("org.xml.sax.SAXException", className );

        message = exception.getMessage();
        assertTrue( message.matches("^xml node CATALOG has no children$") );



        // xml node with invalid attribute value
        String fileName03 = ImportTest.class.getResource("03.xml").getPath();

        exception = assertThrows( JDOMParseException.class, () -> Import.importExerciseItemListFromFile( fileName03 ) );

        className = exception.getClass().getName();
        assertEquals("org.jdom2.input.JDOMParseException", className );

        message = exception.getMessage();
        assertTrue( message.startsWith("Error on line 10 of document ") );
        assertTrue( message.matches(".*(03[.]xml:).*") );
        assertTrue( message.endsWith(" cvc-datatype-valid.1.2.1: 'tru' is not a valid value for 'boolean'.") );



        // xml node ID has invalid content: value cannot be parsed as integer, too big
        String fileName04 = ImportTest.class.getResource("04.xml").getPath();

        exception = assertThrows( SAXException.class, () -> Import.importExerciseItemListFromFile( fileName04 ) );

        className = exception.getClass().getName();
        assertEquals("org.xml.sax.SAXException", className );

        message = exception.getMessage();
        assertTrue( message.matches("^content of xml node ID cannot be parsed as integer$") );



        // xml node ID has invalid content: value represents a negative Integer
        String fileName05 = ImportTest.class.getResource("05.xml").getPath();

        exception = assertThrows( JDOMParseException.class, () -> Import.importExerciseItemListFromFile( fileName05 ) );

        className = exception.getClass().getName();
        assertEquals("org.jdom2.input.JDOMParseException", className );

        message = exception.getMessage();
        assertTrue( message.startsWith("Error on line 4 of document ") );
        assertTrue( message.matches(".*(05[.]xml:).*") );
        assertTrue( message.endsWith(" cvc-minInclusive-valid: Value '-1' is not facet-valid with respect to minInclusive '1' for type 'positiveInteger'.") );
    }
}