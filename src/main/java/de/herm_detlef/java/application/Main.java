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


import com.google.inject.Guice;
import com.google.inject.Injector;
import de.herm_detlef.java.application.mvc.controller.app.ApplicationController;
import de.herm_detlef.java.application.utilities.Utilities;
import javafx.application.Application;
import javafx.application.Preloader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

import static de.herm_detlef.java.application.ApplicationConstants.DEBUG;

/* @formatter:off */

/**
 * TODO
 * @author Detlef G. Herm, 2016-09-26
 * @since 1.0
 *
 */
public class Main extends Application {

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    @Override
    public void init() {

        notifyPreloader( new Preloader.ProgressNotification( 0.2 ) );
    }

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    @Override
    public void start( Stage primaryStage ) {

        primaryStage.setTitle( ApplicationConstants.TITLE_OF_MAIN_DIALOG );

        Injector injector = Guice.createInjector(
                new StageModule<>( Stage.class, primaryStage ),
                new BasicModule() );

        CommonData commonData = injector.getInstance(CommonData.class);

        ApplicationController appController = injector.getInstance( ApplicationController.class );

        // System.out.println( Platform.isFxApplicationThread() );
        //
        // primaryStage.setFullScreen( true );
        // primaryStage.setFullScreenExitKeyCombination( ESC );
        // primaryStage.setAlwaysOnTop( true );

        Parent root = Utilities.createSceneGraphObjectFromFXMLResource(
                appController,
                "Application.fxml",
                null,
                commonData);

        if (DEBUG) assert root != null;
        if ( root == null ) return;

        Scene scene = new Scene( root, 800, 800 );
        scene.getStylesheets()
                .add( ApplicationController.class
                        .getResource("application.css")
                        .toExternalForm() );

        primaryStage.setScene( scene );
        primaryStage.setResizable( true );
        primaryStage.centerOnScreen();
        // primaryStage.sizeToScene(); // TODO

        injector.getInstance( ApplicationPreferences.class )
                .setStagePositionAndSizeBasedOnUserPreferences(
                        primaryStage,
                        "ApplicationStageOriginX",
                        "ApplicationStageOriginY",
                        "ApplicationStageWidth",
                        "ApplicationStageHeight" );

        primaryStage.show();

        notifyPreloader( new StageNotification( primaryStage ) );
    }

//    public static void main( String[] args ) {
//
//         // --- see CustomPreloader.main ---
//
//         launch(args);
//    }
}
