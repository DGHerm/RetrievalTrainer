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
import de.herm_detlef.java.application.controller.app_menu.AppMenuController;
import de.herm_detlef.java.application.controller.edit_menu.EditMenuController;
import de.herm_detlef.java.application.controller.edit_menu.ToolsPanelController;
import de.herm_detlef.java.application.controller.exerciseitemlist_menu.ExerciseItemListMenuController;
import de.herm_detlef.java.application.controller.file_menu.FileMenuController;
import de.herm_detlef.java.application.controller.navigation.NavigationController;
import de.herm_detlef.java.application.view.Navigation;
import de.herm_detlef.java.application.view.Viewer;

/* @formatter:off */

/**
 * TODO
 * @author Detlef G. Herm, 2016-09-26
 * @since 1.0
 *
 */
public class Remote {

    private ApplicationController  applicationController;
    private AppMenuController      appMenuController;
    private FileMenuController     fileMenuController;
    private ExerciseItemListMenuController exerciseMenuController;
    private EditMenuController     editMenuController;
    private Viewer                 viewer;

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    public ApplicationController getApplicationController() {

        return applicationController;
    }

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    public void setApplicationController( ApplicationController applicationController ) {

        this.applicationController = applicationController;
    }

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    public AppMenuController getAppMenuController() {

        return appMenuController;
    }

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    public void setAppMenuController( AppMenuController appMenuController ) {

        this.appMenuController = appMenuController;
    }

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    public FileMenuController getFileMenuController() {

        return fileMenuController;
    }

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    public void setFileMenuController( FileMenuController fileMenuController ) {

        this.fileMenuController = fileMenuController;
    }

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    public ExerciseItemListMenuController getExerciseMenuController() {

        return exerciseMenuController;
    }

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    public void setExerciseMenuController( ExerciseItemListMenuController exerciseMenuController ) {

        this.exerciseMenuController = exerciseMenuController;
    }

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    public EditMenuController getEditMenuController() {

        return editMenuController;
    }

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    public void setEditMenuController( EditMenuController editMenuController ) {

        this.editMenuController = editMenuController;
    }

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    public Viewer getViewer() {

        return viewer;
    }

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    public void setViewer( Viewer viewer ) {

        this.viewer = viewer;
    }
}
