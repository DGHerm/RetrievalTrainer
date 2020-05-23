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
    }
}