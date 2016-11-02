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


import de.herm_detlef.java.application.controller.ApplicationController;
import de.herm_detlef.java.application.utilities.Utilities;
import javafx.application.Application;
import javafx.application.Preloader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

/* @formatter:off */

/**
 * TODO
 * @author Detlef G. Herm, 2016-09-26
 * @since 1.0
 *
 */
public class Main extends Application {

    private ApplicationPreferences applicationPreferences;
    private CommonData             commonData;
    private Remote                 remote;

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    @Override
    public void init() {

        notifyPreloader(
            new Preloader.ProgressNotification( 0.2 ) );
    }

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    @Override
    public void start( Stage primaryStage ) {

        applicationPreferences = new ApplicationPreferences();
        commonData = new CommonData( primaryStage,
                                     applicationPreferences );
        remote = new Remote( commonData );

        primaryStage.setTitle(
            ApplicationConstants.TITLE_OF_MAIN_DIALOG );

        ApplicationController appController = new ApplicationController( commonData,
                                                                         remote );

        // System.out.println( Platform.isFxApplicationThread() );
        //
        // primaryStage.setFullScreen( true );
        // primaryStage.setFullScreenExitKeyCombination( ESC );
        // primaryStage.setAlwaysOnTop( true );

        Parent root = Utilities.createSceneGraphObjectFromFXMLResource(
            appController,
            "Application.fxml",
            null,
            commonData );

        Scene scene = new Scene( root,
                                 800,
                                 800 );
        scene.getStylesheets().add(
            getClass().getResource(
                "/de/herm_detlef/java/application/controller/application.css" ).toExternalForm() );
        primaryStage.setScene(
            scene );
        primaryStage.setResizable(
            true );
        primaryStage.centerOnScreen();
        // primaryStage.sizeToScene(); // TODO

        commonData.getApplicationPreferences().setStagePositionAndSizeBasedOnUserPreferences(
            primaryStage,
            "ApplicationStageOriginX",
            "ApplicationStageOriginY",
            "ApplicationStageWidth",
            "ApplicationStageHeight" );

        primaryStage.show();

        notifyPreloader(
            new StageNotification( primaryStage ) );
    }

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    public CommonData getCommonData() {

        return commonData;
    }

//    public static void main( String[] args ) {
//
//        if ( !ApplicationConstants.VERSION_OF_JAVA_RUNTIME_ENVIRONMENT.startsWith(
//            "1.8." ) ) {
//            throw new RuntimeException( ApplicationConstants.INCOMPATIBLE_JAVA_RUNTIME_ENVIRONMENT_NOTICE );
//        }
//
//        launch(
//            args );
//    }
}
