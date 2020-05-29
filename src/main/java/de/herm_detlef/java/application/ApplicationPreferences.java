package de.herm_detlef.java.application;

import javafx.stage.Stage;

import java.util.prefs.Preferences;

public interface ApplicationPreferences {

    Preferences getUserPreferencesNode();

    void saveStagePositionAndSizeToUserPreferences(final Stage stage,
                                                   final String keyStageOriginX,
                                                   final String keyStageOriginY,
                                                   final String keyStageWidth,
                                                   final String keyStageHeight);

    void setStagePositionAndSizeBasedOnUserPreferences(final Stage stage,
                                                       final String keyStageOriginX,
                                                       final String keyStageOriginY,
                                                       final String keyStageWidth,
                                                       final String keyStageHeight);
}
