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

package de.herm_detlef.java.application.controller.about;


import java.io.IOException;

import de.herm_detlef.java.application.ApplicationConstants;
import de.herm_detlef.java.application.utilities.Utilities;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

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

        String packagePath = Utilities.createFilePath(
            this,
            "About.fxml" );

        final FXMLLoader fxmlLoader = new FXMLLoader( getClass().getResource(
            packagePath ) );
        fxmlLoader.setController(
            this );

        try {
            fxmlLoader.load();
        } catch ( IOException e ) {
            Utilities.showErrorMessage(
                e.getClass().getSimpleName(),
                e.getMessage() );
            // e.printStackTrace();
            return null;
        }

        if ( licenseNotice == null || javaNotice == null || progressBar == null ) {
            assert false;
            return null;
        }

        assert licenseNotice.getText().isEmpty();
        assert javaNotice.getText().isEmpty();
        assert !progressBar.isVisible();

        licenseNotice.setText(
            ApplicationConstants.LICENSE_NOTICE );
        licenseNotice.setWrapText(
            true );
        licenseNotice.setEditable(
            false );
        licenseNotice.setFont(
            Font.font(
                "Verdana",
                13 ) );
        licenseNotice.setMouseTransparent(
            true );
        licenseNotice.setFocusTraversable(
            false );

        javaNotice.setText(
            ApplicationConstants.JAVA_VERSION_NOTICE );
        javaNotice.setWrapText(
            true );
        javaNotice.setEditable(
            false );
        javaNotice.setFont(
            Font.font(
                "Verdana",
                13 ) );
        javaNotice.setMouseTransparent(
            true );
        javaNotice.setFocusTraversable(
            false );

        Parent root = fxmlLoader.getRoot();
        Scene scene = new Scene(root,
                400,
                250);
        // scene.getStylesheets().add(getClass().getResource("/de/herm_detlef/java/application/about/about.css").toExternalForm());
        return scene;
    }

    public void showAbout( Stage primaryStage ) {

        assert primaryStage != null;

        final Stage stage = new Stage();
        stage.setTitle(
            "About" );
        Scene scene = createScene();
        if ( scene == null ) {
            assert false;
            return;
        }
        stage.setScene(
            scene );
        stage.setResizable(
            false );
        stage.centerOnScreen();
        stage.sizeToScene();
        stage.initOwner(
            primaryStage );
        stage.initModality(
            Modality.APPLICATION_MODAL );
        stage.show();
    }

    public ProgressBar getProgressBar() {

        assert progressBar != null;
        return progressBar;
    }

    public void hideProgressBar() {

        if ( progressBar == null ) {
            assert false;
            return;
        }
        progressBar.setVisible(
            false );
    }

    public void showProgressBar() {

        if ( progressBar == null ) {
            assert false;
            return;
        }
        progressBar.setVisible(
            true );
    }
}
