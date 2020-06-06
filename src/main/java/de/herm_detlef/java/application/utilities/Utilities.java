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

package de.herm_detlef.java.application.utilities;


import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.ResourceBundle;

import de.herm_detlef.java.application.CommonData;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

import static de.herm_detlef.java.application.ApplicationConstants.*;
import static javafx.scene.control.Alert.AlertType.*;
import static javafx.scene.control.ButtonType.NO;
import static javafx.scene.control.ButtonType.YES;

/* @formatter:off */

/**
 * TODO
 * @author Detlef G. Herm, 2016-09-26
 * @since 1.0
 *
 */
public class Utilities {

    public static void showErrorMessage( String header, String content ) {

        Alert alert = new Alert( ERROR );
        alert.setTitle( TITLE_OF_DIALOG_ERROR_MESSAGE );
        alert.setHeaderText( header );
        alert.setContentText( content );
        alert.showAndWait();
    }

    public static void showScoreMessage( String header, String content ) {

        Alert alert = new Alert( INFORMATION );
        alert.setTitle( TITLE_OF_DIALOG_SCORE_MESSAGE );
        alert.setHeaderText( header );
        alert.setContentText( content );
        alert.showAndWait();
    }

    public static boolean showConfirmationMessage( String header, String content ) {

        Alert alert = new Alert( CONFIRMATION, content, YES, NO );
        alert.setTitle(null);
        alert.setHeaderText( header );

        Button yesButton = (Button) alert.getDialogPane().lookupButton( YES );
        yesButton.setDefaultButton( false );
        Button noButton = (Button) alert.getDialogPane().lookupButton( NO );
        noButton.setDefaultButton( true );

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == YES;
    }

    public static < T, R > R createSceneGraphObjectFromFXMLResource( T controller,
                                                                     String fxmlResourcePath ) {
        return createSceneGraphObjectFromFXMLResource(
                controller,
                fxmlResourcePath,
                null,
                null );
    }

    public static < T, R > R createSceneGraphObjectFromFXMLResource( T controller,
                                                                     String fxmlResourcePath,
                                                                     String languageResourceBundlePath,
                                                                     CommonData commonData ) {

        if ( controller == null || fxmlResourcePath == null ) {
            assert false;
            return null;
        }

        FXMLLoader fxmlLoader = new FXMLLoader();

        fxmlLoader.setController( controller );

        if ( languageResourceBundlePath != null
                && commonData != null ) {

            ResourceBundle languageResourcesBundle =
                    ResourceBundle.getBundle(
                            languageResourceBundlePath,
                            commonData.getCurrentLocale(),
                            controller.getClass().getClassLoader() );

            fxmlLoader.setResources( languageResourcesBundle );
        }

        try ( InputStream inputstream = controller
                .getClass()
                .getClassLoader()
                .getResourceAsStream( fxmlResourcePath ) ) {

            fxmlLoader.load( inputstream ); // invokes method 'initialize' on return

        } catch ( IOException e ) {
            Utilities.showErrorMessage(
                e.getClass().getSimpleName(),
                e.getMessage() );
            if (DEBUG) e.printStackTrace();
            return null;
        }

        return fxmlLoader.getRoot();
    }
}
