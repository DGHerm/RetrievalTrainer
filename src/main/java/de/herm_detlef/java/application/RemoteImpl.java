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


import de.herm_detlef.java.application.mvc.controller.app.ApplicationController;
import de.herm_detlef.java.application.mvc.controller.app_menu.AppMenuController;
import de.herm_detlef.java.application.mvc.controller.edit.edit_menu.EditMenuController;
import de.herm_detlef.java.application.mvc.controller.exerciseitemlist_menu.ExerciseItemListMenuController;
import de.herm_detlef.java.application.mvc.controller.file_menu.FileMenuController;
import de.herm_detlef.java.application.mvc.controller.preferences.PreferencesController;
import de.herm_detlef.java.application.mvc.view.Viewer;

import javax.inject.Singleton;

/* @formatter:off */

/**
 * TODO
 * @author Detlef G. Herm, 2016-09-26
 * @since 1.0
 *
 */
@Singleton
class RemoteImpl implements Remote {

    private ApplicationController  applicationController;
    private AppMenuController      appMenuController;
    private FileMenuController     fileMenuController;
    private ExerciseItemListMenuController exerciseMenuController;
    private EditMenuController     editMenuController;
    private Viewer                 viewer;
    private PreferencesController  preferencesController;

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    @Override
    public ApplicationController getApplicationController() {

        return applicationController;
    }

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    @Override
    public void setApplicationController(ApplicationController applicationController) {

        this.applicationController = applicationController;
    }

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    @Override
    public AppMenuController getAppMenuController() {

        return appMenuController;
    }

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    @Override
    public void setAppMenuController(AppMenuController appMenuController) {

        this.appMenuController = appMenuController;
    }

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    @Override
    public FileMenuController getFileMenuController() {

        return fileMenuController;
    }

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    @Override
    public void setFileMenuController(FileMenuController fileMenuController) {

        this.fileMenuController = fileMenuController;
    }

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    @Override
    public ExerciseItemListMenuController getExerciseMenuController() {

        return exerciseMenuController;
    }

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    @Override
    public void setExerciseMenuController(ExerciseItemListMenuController exerciseMenuController) {

        this.exerciseMenuController = exerciseMenuController;
    }

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    @Override
    public EditMenuController getEditMenuController() {

        return editMenuController;
    }

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    @Override
    public void setEditMenuController(EditMenuController editMenuController) {

        this.editMenuController = editMenuController;
    }

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    @Override
    public Viewer getViewer() {

        return viewer;
    }

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    @Override
    public void setViewer(Viewer viewer) {

        this.viewer = viewer;
    }


    @Override
    public PreferencesController getPreferencesController() {
        return preferencesController;
    }

    @Override
    public void setPreferencesController(PreferencesController preferencesController) {

        this.preferencesController = preferencesController;
    }
}
