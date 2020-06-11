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

public enum ViewResourcesPath {

    ABOUT_FXML("de/herm_detlef/java/application/mvc/view/about/about.fxml"),
    ABOUT_CSS("de/herm_detlef/java/application/mvc/view/about/about.css"),
    ABOUT_RESOURCE_BUNDLE("de/herm_detlef/java/application/mvc/view/about/about"),

    APP_FXML("de/herm_detlef/java/application/mvc/view/app/application.fxml"),
    APP_CSS("de/herm_detlef/java/application/mvc/view/app/application.css"),
    APP_RESOURCE_BUNDLE("de/herm_detlef/java/application/mvc/view/app/application"),

    APP_MENU_FXML("de/herm_detlef/java/application/mvc/view/app_menu/app_menu.fxml"),
    APP_MENU_RESOURCE_BUNDLE("de/herm_detlef/java/application/mvc/view/app_menu/app_menu"),

    EDIT_MENU_FXML( "de/herm_detlef/java/application/mvc/view/edit/edit_menu/edit_menu.fxml" ),
    EDIT_MENU_CSS( "de/herm_detlef/java/application/mvc/view/edit/edit_menu/edit_menu.css" ),
    EDIT_MENU_RESOURCE_BUNDLE( "de/herm_detlef/java/application/mvc/view/edit/edit_menu/edit_menu" ),

    TOOLS_PANEL_FXML( "de/herm_detlef/java/application/mvc/view/edit/tools_panel/tools_panel.fxml" ),
    TOOLS_PANEL_CSS( "de/herm_detlef/java/application/mvc/view/edit/tools_panel/tools_panel.css" ),
    TOOLS_PANEL_RESOURCE_BUNDLE( "de/herm_detlef/java/application/mvc/view/edit/tools_panel/tools_panel" ),

    EXERCISE_ITEM_LIST_MENU_FXML("de/herm_detlef/java/application/mvc/view/exerciseitemlist_menu/exercise_item_list_menu.fxml"),
    EXERCISE_ITEM_LIST_MENU_RESOURCE_BUNDLE("de/herm_detlef/java/application/mvc/view/exerciseitemlist_menu/exercise_item_list_menu"),

    FILE_MENU_FXML("de/herm_detlef/java/application/mvc/view/file_menu/file_menu.fxml"),
    FILE_MENU_RESOURCE_BUNDLE("de/herm_detlef/java/application/mvc/view/file_menu/file_menu"),

    FILES_AND_DIRECTORIES_FXML("de/herm_detlef/java/application/mvc/view/filesAndDirectories/files_and_directories.fxml"),
    FILES_AND_DIRECTORIES_CSS("de/herm_detlef/java/application/mvc/view/filesAndDirectories/files_and_directories.css"),
    FILES_AND_DIRECTORIES_RESOURCE_BUNDLE("de/herm_detlef/java/application/mvc/view/filesAndDirectories/files_and_directories"),

    NAVIGATION_FXML("de/herm_detlef/java/application/mvc/view/navigation/navigation.fxml"),
    NAVIGATION_RESOURCE_BUNDLE("de/herm_detlef/java/application/mvc/view/navigation/navigation"),

    PREFERENCES_FXML("de/herm_detlef/java/application/mvc/view/preferences/preferences.fxml"),
    PREFERENCES_CSS("de/herm_detlef/java/application/mvc/view/preferences/preferences.css"),
    PREFERENCES_RESOURCE_BUNDLE("de/herm_detlef/java/application/mvc/view/preferences/preferences");

    final String path;

    ViewResourcesPath(String path) {
        this.path = path;
    }

    public String path() {
        return path;
    }

    @Override
    public String toString() {
        return path;
    }
}
