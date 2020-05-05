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


import java.security.AccessController;
import java.security.PrivilegedAction;

import javafx.fxml.FXMLLoader;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.text.Font;

/* @formatter:off */

/**
 * TODO
 * @author Detlef G. Herm, 2016-09-26
 * @since 1.0
 *
 */
public class ApplicationConstants {

    // --------------------------
    public static final KeyCombination ESC                                          = new KeyCodeCombination( KeyCode.ESCAPE );

    // --------------------------
    /**
     * TODO
     * <p>
     * value: {@value de.herm_detlef.java.application.ApplicationConstants#XML_SCHEMA_DEFINITION}
     * @since 1.0
     *
     *
     */
    public static final String         XML_SCHEMA_DEFINITION                        = "RetrievalTrainerXMLSchemaDefinition.xsd";

    // --------------------------
    public static final String         XML_SCHEMA_INSTANCE                          = "http://www.w3.org/2001/XMLSchema-instance";

    // --------------------------
    public static final String         XML_NAMESPACE                                = "RetrievalTrainer";

    // --------------------------
    public static final String         NAME_OF_XML_ATTRIBUTE_ANSWER_TEXT_MARK       = "mark";

    // --------------------------
    public static final String         FILE_NAME_SEPARATOR                          = "/";

        // see javafx/application/Preloader.java,

    // --------------------------
    public static final String PACKAGE_NAME_SEPARATOR = ".";

    // --------------------------
    public static final String LINE_SEPARATOR;

    static {
        // see javafx/application/Preloader.java,
        String prop = AccessController.doPrivileged(
            ( PrivilegedAction< String > ) () -> System.getProperty(
                "line.separator" ) );
        LINE_SEPARATOR = prop != null ? prop : "\n";
    }

    // --------------------------
    public static final String LICENSE_NOTICE;

    static {
        StringBuilder licenseText = new StringBuilder();
        licenseText.append(
            "Copyright 2016 Detlef Gregor Herm" );
        licenseText.append(
            LINE_SEPARATOR );
        licenseText.append(
            LINE_SEPARATOR );
        licenseText.append(
            "Licensed under the Apache License, Version 2.0" );
        LICENSE_NOTICE = licenseText.toString();
    }

    // --------------------------
    public static final String VERSION_OF_JAVA_RUNTIME_ENVIRONMENT;

    static {
        String prop = AccessController.doPrivileged(
            ( PrivilegedAction< String > ) () -> System.getProperty(
                "java.version" ) );
        VERSION_OF_JAVA_RUNTIME_ENVIRONMENT = prop != null ? prop : "data not retrievable";
    }

    // --------------------------
    public static final String JAVA_VERSION_NOTICE;

    static {
        StringBuilder javaVersion = new StringBuilder();
        javaVersion.append(
            String.format(
                "Java Version: %s",
                VERSION_OF_JAVA_RUNTIME_ENVIRONMENT ) );
        javaVersion.append(
            LINE_SEPARATOR );
        javaVersion.append(
            String.format(
                "JavaFX Version: %s",
                FXMLLoader.JAVAFX_VERSION ) );
        JAVA_VERSION_NOTICE = javaVersion.toString();
    }

    // --------------------------

    public static final Integer MINIMUM_REQUIRED_JAVA_RUNTIME_ENVIRONMENT = 11;

    // --------------------------
    public static final String INCOMPATIBLE_JAVA_RUNTIME_ENVIRONMENT_NOTICE;

    static {
        StringBuilder msg = new StringBuilder();
        msg.append( "JRE " )
                .append( MINIMUM_REQUIRED_JAVA_RUNTIME_ENVIRONMENT.toString() )
                .append( " or higher is required to run this application. You have JRE " )
                .append( VERSION_OF_JAVA_RUNTIME_ENVIRONMENT );
        INCOMPATIBLE_JAVA_RUNTIME_ENVIRONMENT_NOTICE = msg.toString();
    }

    // --------------------------
    public static final String TITLE_OF_MAIN_DIALOG          = "RetrievalTrainer";

    // --------------------------
    public static final String TITLE_OF_DIALOG_FILE_CHOOSER_OPEN  = "Choose Resource File To Open";

    // --------------------------
    public static final String TITLE_OF_DIALOG_FILE_CHOOSER_SAVE  = "Save Resource File As";

    // --------------------------
    public static final String TITLE_OF_DIALOG_PREFERENCES   = "Preferences";

    // --------------------------
    public static final String TITLE_OF_DIALOG_ERROR_MESSAGE = "Error Message";

    // --------------------------
    public static final String TITLE_OF_DIALOG_SCORE_MESSAGE = "Score";

    // --------------------------
    public static final String PLACEHOLDER_QUESTION_TEXT     = "type your question text here";

    // --------------------------
    public static final String PLACEHOLDER_QUESTION_CODE     = "type your code example here";

    // --------------------------
    public static final String PLACEHOLDER_ANSWER_TEXT       = "type your answer text here";

    // --------------------------
    public static final String PLACEHOLDER_SOLUTION_TEXT     = "type your solution text here";

    // --------------------------
    public static final String NAME_OF_OPERATING_SYSTEM;

    static {
        String prop = AccessController.doPrivileged(
            ( PrivilegedAction< String > ) () -> System.getProperty(
                "os.name" ) );
        NAME_OF_OPERATING_SYSTEM = prop != null ? prop : "data not retrievable";
    }

    // --------------------------
    public static final String  NAME_OF_OPERATING_SYSTEM_OSX = "Mac OS X";

    // --------------------------

    public static final boolean USE_SYSTEM_MENU_BAR          = false;

    // --------------------------

    public static final Font    FONT_OF_QUESTION_CODE        = Font.font(
        "Verdana",
        13 );

    // --------------------------
    public static final String  FORM_OF_SCORE_MESSAGE;

    static {
        StringBuilder msg = new StringBuilder();
        msg.append(
            "count of questions: %d" );
        msg.append(
            LINE_SEPARATOR );
        msg.append(
            "count of correct answers:  %d" );
        msg.append(
            LINE_SEPARATOR );
        msg.append(
            "success rate: %3.1f%%" );
        FORM_OF_SCORE_MESSAGE = msg.toString();
    }

    // --------------------------
    public static final String TITLE_OF_DIALOG_EDITOR_TOOLS = "Editor Panel";

    // --------------------------
    public static final String WARNING_MESSAGE_UNSAVED_MODIFICATIONS = "Some modifications need to be saved to disc";

    // --------------------------
    public static final String CONFIRMATION_MESSAGE_DELETE_EXERCISEITEM = "Are you sure you want to delete the exercise item?";

    public static final int MAXIMUM_NUMBER_OF_EXERCISE_ITEMS = 999;
}
