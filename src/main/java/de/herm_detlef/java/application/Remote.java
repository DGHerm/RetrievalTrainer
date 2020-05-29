package de.herm_detlef.java.application;

import de.herm_detlef.java.application.mvc.controller.app.ApplicationController;
import de.herm_detlef.java.application.mvc.controller.app_menu.AppMenuController;
import de.herm_detlef.java.application.mvc.controller.edit_menu.EditMenuController;
import de.herm_detlef.java.application.mvc.controller.exerciseitemlist_menu.ExerciseItemListMenuController;
import de.herm_detlef.java.application.mvc.controller.file_menu.FileMenuController;
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
}
