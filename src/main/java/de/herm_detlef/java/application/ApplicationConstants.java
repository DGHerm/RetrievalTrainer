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


import java.io.IOException;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import de.herm_detlef.java.application.utilities.Utilities;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.text.Font;
import org.jdom2.JDOMException;
import org.jdom2.input.sax.XMLReaderJDOMFactory;
import org.jdom2.input.sax.XMLReaderXSDFactory;

import javax.xml.transform.stream.StreamSource;

import static de.herm_detlef.java.application.ViewResourcesPath.*;

/* @formatter:off */

/**
 * TODO
 * @author Detlef G. Herm, 2016-09-26
 * @since 1.0
 *
 */
public class ApplicationConstants {

    public static final Logger LOGGER = Logger.getLogger( Logger.GLOBAL_LOGGER_NAME );

    // -----------------------------------------
    // ------------ for debugging --------------
    // -----------------------------------------
    public static boolean DEBUG;
    public static boolean ASSERTS;

    static {
        DEBUG = false;
        assert DEBUG = true;

        ASSERTS = false;
        assert ASSERTS = true;

        // see JLS, 14.21
    }

    // --------------------------
    public static final Preferences USER_PREFERENCES_NODE = Preferences.userNodeForPackage( ApplicationConstants.class );

    // --------------------------
    public static final Locale CURRENT_LOCALE;
    static {
        String[] lang = USER_PREFERENCES_NODE.get( "Locale", "en_US" ).split( "_" );
        CURRENT_LOCALE = new Locale( lang[ 0 ], lang[ 1 ] );
    }

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

    public static final XMLReaderJDOMFactory XML_READER_JDOM_FACTORY;

    static {
        XMLReaderJDOMFactory tmp = null;
        try ( InputStream in = ApplicationConstants.class.getResourceAsStream( XML_SCHEMA_DEFINITION ) ) {
             tmp = new XMLReaderXSDFactory( new StreamSource( in ) );
        } catch ( JDOMException | IOException e ) {
            if (DEBUG) e.printStackTrace();
            LOGGER.severe( e.getClass().getSimpleName() + ": " + e.getMessage() );
            Utilities.showErrorMessage(
                    e.getClass().getSimpleName(),
                    e.getMessage());
            System.exit(0);
        }
        XML_READER_JDOM_FACTORY = Objects.requireNonNull( tmp );// null indicates uninitialized status
    }

    // --------------------------
    public static final String         XML_SCHEMA_INSTANCE                          = "http://www.w3.org/2001/XMLSchema-instance";

    // --------------------------
    public static final String         XML_NAMESPACE                                = "RetrievalTrainer";

    // --------------------------
    public static final String         NAME_OF_XML_ATTRIBUTE_ANSWER_TEXT_MARK       = "mark";

    // --------------------------
    public static final String LINE_SEPARATOR;

    static {
        // see javafx/application/Preloader.java,
        String prop = null;
        try {
            prop = AccessController.doPrivileged( (PrivilegedAction< String >) () -> System.getProperty( "line.separator" ) );
        } catch ( Exception e ) {
            if (DEBUG) e.printStackTrace();
            LOGGER.severe( e.getClass().getSimpleName() + ": " + e.getMessage() );
            Utilities.showErrorMessage(
                    e.getClass().getSimpleName(),
                    e.getMessage());
            System.exit(0);
        }
        LINE_SEPARATOR = Objects.requireNonNull( prop );
    }

    // --------------------------
    public static final String LICENSE_NOTICE;

    static {
        StringBuilder licenseText = new StringBuilder();
        licenseText.append( "Copyright 2016 Detlef Gregor Herm" )
                .append( LINE_SEPARATOR )
                .append( LINE_SEPARATOR )
                .append( "Licensed under the Apache License, Version 2.0" );
        LICENSE_NOTICE = licenseText.toString();
    }

    // --------------------------
    public static final String VERSION_OF_JAVA_RUNTIME_ENVIRONMENT;

    static {
        String prop = null;
        try {
            prop = AccessController.doPrivileged( (PrivilegedAction< String >) () -> System.getProperty( "java.version" ) );
        } catch ( Exception e ) {
            if (DEBUG) e.printStackTrace();
            LOGGER.severe( e.getClass().getSimpleName() + ": " + e.getMessage() );
            Utilities.showErrorMessage(
                    e.getClass().getSimpleName(),
                    e.getMessage());
            System.exit(0);
        }
        VERSION_OF_JAVA_RUNTIME_ENVIRONMENT = Objects.requireNonNull( prop );
    }

    // --------------------------
    public static final String JAVA_VERSION_NOTICE;

    static {
        StringBuilder javaVersion = new StringBuilder();
        javaVersion.append( String.format( "Java Version: %s", VERSION_OF_JAVA_RUNTIME_ENVIRONMENT ) )
                .append( LINE_SEPARATOR )
                .append( String.format( "JavaFX Version: %s", FXMLLoader.JAVAFX_VERSION ) );
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
    public static final String TITLE_OF_DIALOG_FILE_CHOOSER_OPEN;

    static {
        TITLE_OF_DIALOG_FILE_CHOOSER_OPEN = ResourceBundle
                .getBundle( FILES_AND_DIRECTORIES_RESOURCE_BUNDLE.path(), CURRENT_LOCALE )
                .getString("L10N_TITLE_OF_DIALOG_FILE_CHOOSER_OPEN" );
    }

    // --------------------------
    public static final String TITLE_OF_DIALOG_FILE_CHOOSER_SAVE;

    static {
        TITLE_OF_DIALOG_FILE_CHOOSER_SAVE = ResourceBundle
                .getBundle( FILES_AND_DIRECTORIES_RESOURCE_BUNDLE.path(), CURRENT_LOCALE )
                .getString("L10N_TITLE_OF_DIALOG_FILE_CHOOSER_SAVE" );
    }

    // --------------------------
    public static final String TITLE_OF_DIALOG_PREFERENCES;

    static {
        TITLE_OF_DIALOG_PREFERENCES = ResourceBundle
                .getBundle( PREFERENCES_RESOURCE_BUNDLE.path(), CURRENT_LOCALE )
                .getString("L10N_Stage_Preferences" );
    }

    // --------------------------
    public static final String TITLE_OF_DIALOG_ERROR_MESSAGE = "Error Message";

    // --------------------------
    public static final String TITLE_OF_DIALOG_SCORE_MESSAGE;

    static {
        TITLE_OF_DIALOG_SCORE_MESSAGE = ResourceBundle
                .getBundle( APP_RESOURCE_BUNDLE.path(), CURRENT_LOCALE )
                .getString("L10N_ToolBar_Button_Score" );
    }

    // --------------------------
    public static final String PLACEHOLDER_QUESTION_TEXT;

    static {
        PLACEHOLDER_QUESTION_TEXT = ResourceBundle
                .getBundle( APP_RESOURCE_BUNDLE.path(), CURRENT_LOCALE )
                .getString("L10N_PLACEHOLDER_QUESTION_TEXT" );
    }

    // --------------------------
    public static final String PLACEHOLDER_QUESTION_CODE;

    static {
        PLACEHOLDER_QUESTION_CODE = ResourceBundle
                .getBundle( APP_RESOURCE_BUNDLE.path(), CURRENT_LOCALE )
                .getString("L10N_PLACEHOLDER_QUESTION_CODE" );
    }

    // --------------------------
    public static final String PLACEHOLDER_ANSWER_TEXT;

    static {
        PLACEHOLDER_ANSWER_TEXT = ResourceBundle
                .getBundle( APP_RESOURCE_BUNDLE.path(), CURRENT_LOCALE )
                .getString("L10N_PLACEHOLDER_ANSWER_TEXT" );
    }

    // --------------------------
    public static final String PLACEHOLDER_SOLUTION_TEXT;

    static {
        PLACEHOLDER_SOLUTION_TEXT = ResourceBundle
                .getBundle( APP_RESOURCE_BUNDLE.path(), CURRENT_LOCALE )
                .getString("L10N_PLACEHOLDER_SOLUTION_TEXT" );
    }

    // ----- macOS specific -----
    public static final String NAME_OF_OPERATING_SYSTEM;

    static {
        String prop = null;
        try {
            prop = AccessController.doPrivileged( (PrivilegedAction< String >) () -> System.getProperty( "os.name" ) );
        } catch ( Exception e ) {
            if (DEBUG) e.printStackTrace();
            LOGGER.severe( e.getClass().getSimpleName() + ": " + e.getMessage() );
            Utilities.showErrorMessage(
                    e.getClass().getSimpleName(),
                    e.getMessage());
            System.exit(0);
        }
        NAME_OF_OPERATING_SYSTEM = Objects.requireNonNull( prop );
    }

    public static final String[]  NAME_OF_OPERATING_SYSTEM_OSX = { "Mac OS X", "OS X", "macOS" };
    public static final boolean USE_SYSTEM_MENU_BAR          = true;

    // --------------------------

    public static final Font    FONT_OF_QUESTION_CODE        = Font.font("Verdana",13 );

    // --------------------------
    public static final String  FORM_OF_SCORE_MESSAGE;

    static {
        StringBuilder msg = new StringBuilder();
        msg.append( "count of questions: %d" )
                .append( LINE_SEPARATOR )
                .append( "count of correct answers:  %d" )
                .append( LINE_SEPARATOR )
                .append( "success rate: %3.1f%%" );
        FORM_OF_SCORE_MESSAGE = msg.toString();
    }

    // --------------------------
    public static final String TITLE_OF_DIALOG_EDITOR_TOOLS = "Editor Panel";

    // --------------------------
    public static final String WARNING_MESSAGE_UNSAVED_MODIFICATIONS = "Some modifications need to be saved to disc";

    // --------------------------
    public static final String CONFIRMATION_MESSAGE_DISCARD_UNSAVED_MODIFICATIONS = "Do you want to discard the modifications?";

    // --------------------------
    public static final String CONFIRMATION_MESSAGE_DELETE_EXERCISEITEM = "Are you sure you want to delete the exercise item?";

    // --------------------------
    public static final int MAXIMUM_NUMBER_OF_EXERCISE_ITEMS = 999;
}
