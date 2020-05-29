package de.herm_detlef.java.application.mvc.controller.app;

import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;

import java.net.URL;
import java.util.ResourceBundle;

public interface ApplicationController {
    void initialize(URL location, ResourceBundle resources);

    void updateView();

    boolean checkExerciseItemListMasterNeedsSave();

    ToggleButton getShowSolution();

    Button getButtonCheck();
}
