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


import java.util.prefs.Preferences;

import javafx.stage.Stage;

import javax.inject.Singleton;

import static de.herm_detlef.java.application.ApplicationConstants.USER_PREFERENCES_NODE;

/* @formatter:off */

/**
 * TODO
 *
 * @author Detlef G. Herm, 2016-09-26
 * @since 1.0
 *
 */
@Singleton
public class ApplicationPreferencesImpl implements ApplicationPreferences {


    /**
     * TODO
     * <p>
     *
     * @since 1.0
     */
    public void saveStagePositionAndSizeToUserPreferences( final Stage stage,
                                                           final String keyStageOriginX,
                                                           final String keyStageOriginY,
                                                           final String keyStageWidth,
                                                           final String keyStageHeight ) {

        stage.xProperty().addListener( ( observable, oldValue, newValue ) -> {
            if ( !stage.isShowing() ) return;
            USER_PREFERENCES_NODE.put( keyStageOriginX, Integer.toString( newValue.intValue() ) );
        } );

        stage.yProperty().addListener( ( observable, oldValue, newValue ) -> {
            if ( !stage.isShowing() ) return;
            USER_PREFERENCES_NODE.put( keyStageOriginY, Integer.toString( newValue.intValue() ) );
        } );

        stage.widthProperty().addListener( ( observable, oldValue, newValue ) -> {
            if ( !stage.isShowing() ) return;
            USER_PREFERENCES_NODE.put( keyStageWidth, Integer.toString( newValue.intValue() ) );
        } );

        stage.heightProperty().addListener( ( observable, oldValue, newValue ) -> {
            if ( !stage.isShowing() ) return;
            USER_PREFERENCES_NODE.put( keyStageHeight, Integer.toString( newValue.intValue() ) );
        } );
    }

    /**
     * TODO
     * <p>
     *
     * @since 1.0
     */
    public void setStagePositionAndSizeBasedOnUserPreferences( final Stage stage,
                                                               final String keyStageOriginX,
                                                               final String keyStageOriginY,
                                                               final String keyStageWidth,
                                                               final String keyStageHeight ) {

        double originX = Double.parseDouble(
                USER_PREFERENCES_NODE.get( keyStageOriginX, Double.toString( stage.getX() ) ) );
        double originY = Double.parseDouble(
                USER_PREFERENCES_NODE.get( keyStageOriginY, Double.toString( stage.getY() ) ) );
        double width = Double.parseDouble(
                USER_PREFERENCES_NODE.get( keyStageWidth, Double.toString( stage.getWidth() ) ) );
        double height = Double.parseDouble(
                USER_PREFERENCES_NODE.get( keyStageHeight, Double.toString( stage.getHeight() ) ) );

        stage.setX( originX );
        stage.setY( originY );
        stage.setWidth( width );
        stage.setHeight( height );
    }
}
