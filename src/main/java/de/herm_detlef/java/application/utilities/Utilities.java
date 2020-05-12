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


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.ResourceBundle;

import de.herm_detlef.java.application.ApplicationConstants;
import de.herm_detlef.java.application.CommonData;
import de.herm_detlef.java.application.model.ExerciseItem;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

/* @formatter:off */

/**
 * TODO
 * @author Detlef G. Herm, 2016-09-26
 * @since 1.0
 *
 */
public class Utilities {

    public static void showErrorMessage( String header, String content ) {

        Alert alert = new Alert( AlertType.ERROR );
        alert.setTitle(
            ApplicationConstants.TITLE_OF_DIALOG_ERROR_MESSAGE );
        alert.setHeaderText(
            header );
        alert.setContentText(
            content );
        alert.showAndWait();
    }

    public static void showScoreMessage( String header, String content ) {

        Alert alert = new Alert( AlertType.INFORMATION );
        alert.setTitle(
            ApplicationConstants.TITLE_OF_DIALOG_SCORE_MESSAGE );
        alert.setHeaderText(
            header );
        alert.setContentText(
            content );
        alert.showAndWait();
    }

    public static void showWarningMessage( String header, String content ) {

        Alert alert = new Alert( AlertType.WARNING );
        alert.setTitle(null);
        alert.setHeaderText(
            header );
        alert.setContentText(
            content );
        alert.showAndWait();
    }

    public static boolean showConfirmationMessage( String header, String content ) {

        Alert alert = new Alert( AlertType.CONFIRMATION, content, ButtonType.YES, ButtonType.NO );
        alert.setTitle(null);
        alert.setHeaderText( header );

        Button yesButton = (Button) alert.getDialogPane().lookupButton( ButtonType.YES );
        yesButton.setDefaultButton( false );
        Button noButton = (Button) alert.getDialogPane().lookupButton( ButtonType.NO );
        noButton.setDefaultButton( true );

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.YES;
    }

    public static String createFilePath( Object obj, String fxmlResourceName ) {

        return Utilities.createFilePath(
            obj.getClass(),
            fxmlResourceName );
    }

    public static String createFilePath( Class< ? > cls, String fxmlResourceName ) {

        if ( fxmlResourceName == null ) return null;

        if ( cls.getPackage() == null ) return fxmlResourceName; // default package

        StringBuilder filePath = new StringBuilder( ApplicationConstants.FILE_NAME_SEPARATOR );

        for ( String name : cls.getPackage().getName().split(
            "\\" + ApplicationConstants.PACKAGE_NAME_SEPARATOR ) ) {
            filePath.append(
                name );
            filePath.append(
                ApplicationConstants.FILE_NAME_SEPARATOR );
        }

        filePath.append(
            fxmlResourceName );

        return filePath.toString();
    }

    public static String createPackagePath( Object obj, String resourceName ) {

        return Utilities.createPackagePath(
            obj.getClass(),
            resourceName );
    }

    public static String createPackagePath( Class< ? > cls, String resourceName ) {

        if ( resourceName == null ) return null;

        if ( cls.getPackage() == null ) return resourceName; // default package

        StringBuilder packagePath = new StringBuilder( cls.getPackage().getName() );
        packagePath.append( ApplicationConstants.PACKAGE_NAME_SEPARATOR )
                .append( resourceName );
        return packagePath.toString();
    }

    public static String readFile( final File fileObject ) {

        FileInputStream fileInStream = null;

        StringBuilder content = new StringBuilder();

        try {
            if ( fileObject != null ) {
                fileInStream = new FileInputStream( fileObject );
                int len = fileInStream.available();
                if ( len > 0 ) {
                    do {
                        byte[] buf = new byte[ len ];
                        fileInStream.read(
                            buf );

                        content.append(
                            new String( buf ) );

                        len = fileInStream.available();
                    } while ( len > 0 );
                }
            }

        } catch ( FileNotFoundException e ) {
            Utilities.showErrorMessage(
                e.getClass().getSimpleName(),
                e.getMessage() );
        } catch ( IOException e ) {
            Utilities.showErrorMessage(
                e.getClass().getSimpleName(),
                e.getMessage() );
            // e.printStackTrace();
        } finally {
            try {
                if ( fileInStream != null ) {
                    fileInStream.close();
                }
            } catch ( IOException e ) {
                Utilities.showErrorMessage(
                    e.getClass().getSimpleName(),
                    e.getMessage() );
                // e.printStackTrace();
            }
        }

        return content.toString();
    }

    public static boolean isOrderedByAscendingId( ObservableList< ExerciseItem > list ) {

        int prevId = 0;
        boolean result = true;

        for ( ExerciseItem item : list ) {
            int id = item.getItemId();
            assert id > 0;
            assert prevId != id;
            result = result && ( prevId < id );
            prevId = id;
        }

        return result;
    }

    public static < T, R > R createSceneGraphObjectFromFXMLResource( T controller,
                                                                     String xmlResourceName,
                                                                     String languageResourceBundleName,
                                                                     CommonData commonData ) {

        if ( controller == null || xmlResourceName == null ) {
            assert false;
            return null;
        }

        String languageResourceBundlePath = Utilities.createPackagePath(
            controller,
            languageResourceBundleName );

        ResourceBundle languageResourcesBundle = null;
        if ( languageResourceBundlePath != null ) {
            languageResourcesBundle = ResourceBundle.getBundle(
                languageResourceBundlePath,
                commonData.getCurrentLocale() );
        }

        String packagePath = Utilities.createFilePath(
            controller,
            xmlResourceName );

        FXMLLoader fxmlLoader = new FXMLLoader( controller.getClass().getResource(
            packagePath ),
                                                languageResourcesBundle );

        fxmlLoader.setController(
            controller );

        try {
            fxmlLoader.load(); // invokes method 'initialize' on return
        } catch ( IOException e ) {
            Utilities.showErrorMessage(
                e.getClass().getSimpleName(),
                e.getMessage() );
            e.printStackTrace();
            return null;
        }

        return fxmlLoader.getRoot();
    }
}
