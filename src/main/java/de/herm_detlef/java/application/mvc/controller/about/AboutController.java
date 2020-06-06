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

package de.herm_detlef.java.application.mvc.controller.about;



import de.herm_detlef.java.application.ApplicationConstants;
import de.herm_detlef.java.application.ViewResourcesPath;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static de.herm_detlef.java.application.ApplicationConstants.DEBUG;
import static de.herm_detlef.java.application.ViewResourcesPath.ABOUT_FXML;
import static de.herm_detlef.java.application.utilities.Utilities.createSceneGraphObjectFromFXMLResource;

/* @formatter:off */

/**
 * TODO
 * @author Detlef G. Herm, 2016-09-26
 * @since 1.0
 *
 */
public class AboutController {

    @FXML
    private TextArea    licenseNotice;

    @FXML
    private TextArea    javaNotice;

    @FXML
    private ProgressBar progressBar;

    public AboutController() {
    }

    public Scene createScene() {

        Parent root = createSceneGraphObjectFromFXMLResource( this, ABOUT_FXML.path() );

        if ( licenseNotice == null || javaNotice == null || progressBar == null ) {
            assert false;
            return null;
        }

        if (DEBUG) {
            assert licenseNotice.getText().isEmpty();
            assert javaNotice.getText().isEmpty();
            assert ! progressBar.isVisible();
        }

        licenseNotice.setText( ApplicationConstants.LICENSE_NOTICE );
        licenseNotice.setWrapText( true );
        licenseNotice.setEditable( false );
        licenseNotice.setFont( Font.font("Verdana",13 ) );
        licenseNotice.setMouseTransparent( true );
        licenseNotice.setFocusTraversable( false );

        javaNotice.setText( ApplicationConstants.JAVA_VERSION_NOTICE );
        javaNotice.setWrapText( true );
        javaNotice.setEditable( false );
        javaNotice.setFont( Font.font("Verdana", 13 ) );
        javaNotice.setMouseTransparent( true );
        javaNotice.setFocusTraversable( false );

        return new Scene( root,400,250 );
    }

    public void showAbout( Stage primaryStage ) {

        assert primaryStage != null;

        final Stage stage = new Stage();
        stage.setTitle( "About" );
        Scene scene = createScene();
        if ( scene == null ) {
            assert false;
            return;
        }
        stage.setScene( scene );
        stage.setResizable( false );
        stage.centerOnScreen();
        stage.sizeToScene();
        stage.initOwner( primaryStage );
        stage.initModality( Modality.APPLICATION_MODAL );
        stage.show();
    }

    public ProgressBar getProgressBar() {

        assert progressBar != null;
        return progressBar;
    }

}
