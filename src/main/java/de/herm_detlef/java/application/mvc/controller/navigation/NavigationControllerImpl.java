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

package de.herm_detlef.java.application.mvc.controller.navigation;


import java.util.Objects;

import de.herm_detlef.java.application.CommonData;
import de.herm_detlef.java.application.Remote;
import de.herm_detlef.java.application.utilities.Utilities;
import de.herm_detlef.java.application.mvc.view.Navigation;
import javafx.event.ActionEvent;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;

import javax.inject.Inject;
import javax.inject.Singleton;

import static de.herm_detlef.java.application.ViewResourcesPath.NAVIGATION_FXML;
import static de.herm_detlef.java.application.ViewResourcesPath.NAVIGATION_RESOURCE_BUNDLE;

/* @formatter:off */

/**
 * TODO
 * @author Detlef G. Herm, 2016-09-26
 * @since 1.0
 *
 */
@Singleton
class NavigationControllerImpl implements NavigationController {

    @FXML
    private Button           first;

    @FXML
    private Button           previous;

    @FXML
    private Button           next;

    @FXML
    private Button           last;

    @FXML
    private Label            exerciseItemNumber;

    private final CommonData commonData;
    private final Remote     remote;

    @Inject
    private NavigationControllerImpl( CommonData commonData,
                                      Remote remote ) {

        this.commonData = commonData;
        this.remote = remote;
    }

    @Override
    public HBox create( CommonData commonData,
                        Remote remote ) {

        HBox root = Utilities.createSceneGraphObjectFromFXMLResource(
                this,
                NAVIGATION_FXML.path(),
                NAVIGATION_RESOURCE_BUNDLE.path(),
                commonData );

        return Objects.requireNonNull(root);
    }

    @Override
    public void addEventHandler(ToolBar toolBar) {

        toolBar.addEventHandler(
                ActionEvent.ACTION,
                e -> {
                    final EventTarget target = e.getTarget();
                    if ( target == first || target == previous || target == next || target == last ) {

                        // show solution
                        remote.getApplicationController().getShowSolution().setSelected( false );
                    }
                } );
    }

    @FXML
    private void initialize() {

        commonData.getCurrentExerciseItemProperty().addListener( (obj, oldValue, newValue) -> {
            if ( newValue != null ) {
                exerciseItemNumber.setText( Integer.toString( newValue.getItemId() ) );
            }
        });

        first.setOnAction(
            this::onFirst );
        previous.setOnAction(
            this::onPrevious );
        next.setOnAction(
            this::onNext );
        last.setOnAction(
            this::onLast );
    }

    @Override
    public Button getFirst() {

        return first;
    }

    @Override
    public Button getPrevious() {

        return previous;
    }

    @Override
    public Button getNext() {

        return next;
    }

    @Override
    public Button getLast() {

        return last;
    }

    private void onFirst( ActionEvent event ) {

        Navigation.leftDirection().jumpEnd();
    }

    private void onPrevious( ActionEvent event ) {

        Navigation.leftDirection().step();
    }

    private void onNext( ActionEvent event ) {

        Navigation.rightDirection().step();
    }

    private void onLast( ActionEvent event ) {

        Navigation.rightDirection().jumpEnd();
    }

    @Override
    public void setDisable(boolean buttonFirst, boolean buttonPrevious, boolean buttonNext, boolean buttonLast) {

        first.setDisable(
            buttonFirst );
        previous.setDisable(
            buttonPrevious );
        next.setDisable(
            buttonNext );
        last.setDisable(
            buttonLast );
    }

}
