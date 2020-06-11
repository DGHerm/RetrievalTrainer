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

import de.herm_detlef.java.application.mvc.controller.app.ApplicationController;
import de.herm_detlef.java.application.mvc.controller.app_menu.AppMenuController;
import de.herm_detlef.java.application.mvc.controller.edit.edit_menu.EditMenuController;
import de.herm_detlef.java.application.mvc.controller.exerciseitemlist_menu.ExerciseItemListMenuController;
import de.herm_detlef.java.application.mvc.controller.file_menu.FileMenuController;
import de.herm_detlef.java.application.mvc.controller.preferences.PreferencesController;
import de.herm_detlef.java.application.mvc.view.Viewer;

public interface Remote {
    ApplicationController getApplicationController();

    void setApplicationController(ApplicationController applicationController);

    AppMenuController getAppMenuController();

    void setAppMenuController(AppMenuController appMenuController);

    FileMenuController getFileMenuController();

    void setFileMenuController(FileMenuController fileMenuController);

    ExerciseItemListMenuController getExerciseMenuController();

    void setExerciseMenuController(ExerciseItemListMenuController exerciseMenuController);

    EditMenuController getEditMenuController();

    void setEditMenuController(EditMenuController editMenuController);

    Viewer getViewer();

    void setViewer(Viewer viewer);

    PreferencesController getPreferencesController();

    void setPreferencesController( PreferencesController preferencesController);
}
