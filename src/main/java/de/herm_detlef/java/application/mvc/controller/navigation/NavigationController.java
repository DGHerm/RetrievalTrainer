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


import java.net.URL;
import java.util.ResourceBundle;

import de.herm_detlef.java.application.CommonData;
import de.herm_detlef.java.application.Remote;
import de.herm_detlef.java.application.utilities.Utilities;
import de.herm_detlef.java.application.mvc.view.Navigation;
import javafx.event.ActionEvent;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;

import static de.herm_detlef.java.application.ApplicationConstants.DEBUG;

/* @formatter:off */

/**
 * TODO
 * @author Detlef G. Herm, 2016-09-26
 * @since 1.0
 *
 */
public class NavigationController implements Initializable {

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

    private final ToolBar    toolBar;
    private final CommonData commonData;
    private final Remote     remote;

    private NavigationController( ToolBar toolBar,
                                  CommonData commonData,
                                  Remote remote ) {

        this.toolBar = toolBar;
        this.commonData = commonData;
        this.remote = remote;
    }

    public static NavigationController create( final ToolBar toolBar,
                                               final CommonData commonData,
                                               final Remote remote ) {

        NavigationController navi = new NavigationController( toolBar,
                                                              commonData,
                                                              remote );

        HBox root = Utilities.createSceneGraphObjectFromFXMLResource(
            navi,
            "Navigation.fxml",
            "Navigation",
            commonData );

        if (DEBUG) assert root != null;
        if ( root == null ) return null;

        toolBar.getItems().add(0, root );

        return navi;
    }

    @Override
    public void initialize( URL location, ResourceBundle resources ) {

        commonData.getCurrentExerciseItemProperty().addListener( (obj, oldValue, newValue) -> {
            if ( newValue != null ) {
                exerciseItemNumber.setText( Integer.toString( newValue.getItemId() ) );
            }
        });

        toolBar.addEventHandler(
            ActionEvent.ACTION,
            e -> {
                final EventTarget target = e.getTarget();
                if ( target == first || target == previous || target == next || target == last ) {

                    // show solution
                    remote.getApplicationController().getShowSolution().setSelected(
                        false );
                }
            } );

        first.setOnAction(
            this::onFirst );
        previous.setOnAction(
            this::onPrevious );
        next.setOnAction(
            this::onNext );
        last.setOnAction(
            this::onLast );
    }

    public Button getFirst() {

        return first;
    }

    public Button getPrevious() {

        return previous;
    }

    public Button getNext() {

        return next;
    }

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

    public void setDisable( boolean buttonFirst, boolean buttonPrevious, boolean buttonNext, boolean buttonLast ) {

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
