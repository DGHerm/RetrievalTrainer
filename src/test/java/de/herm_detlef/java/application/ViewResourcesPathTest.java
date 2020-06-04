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

package de.herm_detlef.java.application;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ViewResourcesPathTest implements ViewResourcesPath {

    @Test
    void values() {

        checkResourcePath( ABOUT );
        checkResourcePath( ABOUT_CSS );

        checkResourcePath( APP );
        checkResourcePath( APP_CSS );

        checkResourcePath( APP_MENU );

        checkResourcePath( EDIT_MENU );
        checkResourcePath( EDIT_MENU_CSS );

        checkResourcePath( TOOLS_PANEL );
        checkResourcePath( TOOLS_PANEL_CSS );

        checkResourcePath( EXERCISE_ITEM_LIST_MENU );

        checkResourcePath( FILE_MENU );

        checkResourcePath( FILES_AND_DIRECTORIES );
        checkResourcePath( FILES_AND_DIRECTORIES_CSS );

        checkResourcePath( NAVIGATION );

        checkResourcePath( PREFERENCES );
        checkResourcePath( PREFERENCES_CSS );
    }

    private void checkResourcePath( final String resourcePath ) {

        final String prefix = System.getProperty("user.dir") + "/src/main/resources/";

        Path absolutePath = Path.of(prefix, resourcePath);

        assertTrue( Files.exists(        absolutePath, NOFOLLOW_LINKS ) );
        assertTrue( Files.isRegularFile( absolutePath, NOFOLLOW_LINKS ) );

        String fileName = absolutePath.getFileName().toString();
        assertEquals( fileName.toLowerCase(), fileName );
    }

}