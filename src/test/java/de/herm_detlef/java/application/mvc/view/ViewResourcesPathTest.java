/*
 *   Copyright 2016 Detlef Gregor Herm
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package de.herm_detlef.java.application.mvc.view;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static de.herm_detlef.java.application.mvc.view.ViewResourcesPath.*;
import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ViewResourcesPathTest {

    final String resourcePathPrefix = System.getProperty("user.dir") + "/src/main/resources/";

    final String failureMessagePrefix = "checked resource path: ";


    @Test
    void values() {

        checkResourcePath( ABOUT_FXML.path() );
        checkResourcePath( ABOUT_CSS.path() );
        checkResourcePath( ABOUT_RESOURCE_BUNDLE.path() + ".properties" );

        checkResourcePath( APP_FXML.path() );
        checkResourcePath( APP_CSS.path() );
        checkResourcePath( APP_RESOURCE_BUNDLE.path() + ".properties" );
        checkResourcePath( APP_RESOURCE_BUNDLE.path() + "_de.properties" );
        checkResourcePath( APP_RESOURCE_BUNDLE.path() + "_en.properties" );

        checkResourcePath( APP_MENU_FXML.path() );
        checkResourcePath( APP_MENU_RESOURCE_BUNDLE.path() + ".properties" );
        checkResourcePath( APP_MENU_RESOURCE_BUNDLE.path() + "_de.properties" );
        checkResourcePath( APP_MENU_RESOURCE_BUNDLE.path() + "_en.properties" );

        checkResourcePath( EDIT_MENU_FXML.path() );
        checkResourcePath( EDIT_MENU_CSS.path() );
        checkResourcePath( EDIT_MENU_RESOURCE_BUNDLE.path() + ".properties" );
        checkResourcePath( EDIT_MENU_RESOURCE_BUNDLE.path() + "_de.properties" );
        checkResourcePath( EDIT_MENU_RESOURCE_BUNDLE.path() + "_en.properties" );

        checkResourcePath( TOOLS_PANEL_FXML.path() );
        checkResourcePath( TOOLS_PANEL_CSS.path() );
        checkResourcePath( TOOLS_PANEL_RESOURCE_BUNDLE.path() + ".properties" );
        checkResourcePath( TOOLS_PANEL_RESOURCE_BUNDLE.path() + "_de.properties" );
        checkResourcePath( TOOLS_PANEL_RESOURCE_BUNDLE.path() + "_en.properties" );

        checkResourcePath( EXERCISE_ITEM_LIST_MENU_FXML.path() );
        checkResourcePath( EXERCISE_ITEM_LIST_MENU_RESOURCE_BUNDLE.path() + ".properties" );
        checkResourcePath( EXERCISE_ITEM_LIST_MENU_RESOURCE_BUNDLE.path() + "_de.properties" );
        checkResourcePath( EXERCISE_ITEM_LIST_MENU_RESOURCE_BUNDLE.path() + "_en.properties" );

        checkResourcePath( FILE_MENU_FXML.path() );
        checkResourcePath( FILE_MENU_RESOURCE_BUNDLE.path() + ".properties" );
        checkResourcePath( FILE_MENU_RESOURCE_BUNDLE.path() + "_de.properties" );
        checkResourcePath( FILE_MENU_RESOURCE_BUNDLE.path() + "_en.properties" );

        checkResourcePath( FILES_AND_DIRECTORIES_FXML.path() );
        checkResourcePath( FILES_AND_DIRECTORIES_CSS.path() );
        checkResourcePath( FILES_AND_DIRECTORIES_RESOURCE_BUNDLE.path() + ".properties" );
        checkResourcePath( FILES_AND_DIRECTORIES_RESOURCE_BUNDLE.path() + "_de.properties" );
        checkResourcePath( FILES_AND_DIRECTORIES_RESOURCE_BUNDLE.path() + "_en.properties" );

        checkResourcePath( NAVIGATION_FXML.path() );
        checkResourcePath( NAVIGATION_RESOURCE_BUNDLE.path() + ".properties" );
        checkResourcePath( NAVIGATION_RESOURCE_BUNDLE.path() + "_de.properties" );
        checkResourcePath( NAVIGATION_RESOURCE_BUNDLE.path() + "_en.properties" );

        checkResourcePath( PREFERENCES_FXML.path() );
        checkResourcePath( PREFERENCES_CSS.path() );
        checkResourcePath( PREFERENCES_RESOURCE_BUNDLE.path() + ".properties" );
        checkResourcePath( PREFERENCES_RESOURCE_BUNDLE.path() + "_de.properties" );
        checkResourcePath( PREFERENCES_RESOURCE_BUNDLE.path() + "_en.properties" );
    }


    private void checkResourcePath( final String resourcePath ) {

        final String msg = getFailureMessage( resourcePath );

        final Path absolutePath = Path.of( resourcePathPrefix, resourcePath );

        assertTrue( Files.exists(        absolutePath, NOFOLLOW_LINKS ), msg );
        assertTrue( Files.isRegularFile( absolutePath, NOFOLLOW_LINKS ), msg );

        final String fileName = absolutePath.getFileName().toString();
        assertEquals( fileName.toLowerCase(), fileName, msg );
    }


    private String getFailureMessage( final String resourcePath ) {

        final StringBuilder failureMessage = new StringBuilder(150);

        return failureMessage
                .append( failureMessagePrefix )
                .append( "\"" )
                .append( resourcePath )
                .append( "\"" )
                .toString();
    }

}