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

/*
 *  see: http://docs.oracle.com/javafx/2/deployment/preloaders.htm
 */

package de.herm_detlef.java.application.preloader;


import com.sun.javafx.application.LauncherImpl;

import de.herm_detlef.java.application.ApplicationConstants;
import de.herm_detlef.java.application.Main;
import de.herm_detlef.java.application.StageNotification;
import de.herm_detlef.java.application.controller.about.AboutController;
import javafx.application.Preloader;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.stage.Modality;
import javafx.stage.Stage;

/* @formatter:off */

/**
 * TODO
 * @author Detlef G. Herm, 2016-09-26
 * @since 1.0
 *
 */
public class CustomPreloader extends Preloader {

    private AboutController ac;
    private ProgressBar     bar;
    private Stage           stage;
    private boolean         pretendProgress = true;

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    public void start( Stage stage ) throws Exception {

        this.stage = stage;

        ac = new AboutController();
        Scene scene = ac.createScene();
        if ( scene == null ) {
            assert false;
            return;
        }

        bar = ac.getProgressBar();
        if ( bar == null ) {
            assert false;
            return;
        }
        bar.setVisible(
            true );

        stage.setScene(
            scene );
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.setResizable(
            false );

        // prevent to be obscured by main application window:
        stage.setAlwaysOnTop(
            true );

        stage.show();
    }

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    @Override
    public void handleProgressNotification( ProgressNotification pn ) {

        // Application loading progress is rescaled to be first 50 percent.
        // Even if there is nothing to load, two events were in any case send:
        // the events relating to progress values 0 percent and 100 percent.
        if ( bar != null && ( pn.getProgress() != 1.0 || pretendProgress ) ) {
            bar.setProgress(
                pn.getProgress() / 2 );
            if ( pn.getProgress() > 0 ) {
                pretendProgress = false;
            }
        }
    }

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    @Override
    public void handleApplicationNotification( PreloaderNotification pn ) {

        if ( pn instanceof ProgressNotification ) {
            // Expect application to send us progress notifications
            // with progress ranging from 0 percent to 100 percent
            double v = ( ( ProgressNotification ) pn ).getProgress();
            if ( !pretendProgress ) {
                // If we were receiving loading progress notifications
                // then progress is already at 50 percent.
                // Rescale application progress to start from 50 percent
                v = 0.5 + v / 2;
            }
            if ( bar != null ) {
                bar.setProgress(
                    v );
            }
        }

        if ( pn instanceof StageNotification ) {
            changeStage(
                ( ( StageNotification ) pn ).stage );
        }
    }

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    private void changeStage( Stage primaryStage ) {

        if ( primaryStage == null || stage == null || bar == null ) {
            assert false;
            return;
        }

        Stage preloaderStage = stage;
        stage = new Stage();
        stage.setScene(
            preloaderStage.getScene() );
        stage.initOwner(
            primaryStage );
        stage.initModality(
            Modality.APPLICATION_MODAL );
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.setResizable(
            false );

        bar.setVisible(
            false );

        preloaderStage.close();
        stage.setAlwaysOnTop(
            true );
        stage.show();
    }

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    public static void main( String[] args ) {

        if ( !ApplicationConstants.VERSION_OF_JAVA_RUNTIME_ENVIRONMENT.startsWith(
            "1.8." ) ) {
            throw new RuntimeException( ApplicationConstants.INCOMPATIBLE_JAVA_RUNTIME_ENVIRONMENT_NOTICE );
        }

//         launch(args);
        LauncherImpl.launchApplicationWithArgs(
                null,
            Main.class.getCanonicalName(),
            CustomPreloader.class.getCanonicalName(),
            args );
    }
}
