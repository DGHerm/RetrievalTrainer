package de.herm_detlef.java.application.io;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class ImportTest {

    @Test
    void importExerciseItemListFromFile() {

        // empty file
        String fileName01 = ImportTest.class.getResource("01.xml").getPath();

        Exception exception = assertThrows( Exception.class, () -> Import.importExerciseItemListFromFile( fileName01 ) );

        String className = exception.getClass().getName();
        assertEquals("org.jdom2.input.JDOMParseException", className );

        String message = exception.getMessage();
        assertTrue( message.startsWith("Error on line 1 of document ") );
        assertTrue( message.matches(".*(01[.]xml:).*") );
        assertTrue( message.endsWith(" Premature end of file.") );
    }
}