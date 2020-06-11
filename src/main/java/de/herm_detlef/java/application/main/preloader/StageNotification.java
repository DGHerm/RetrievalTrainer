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

package de.herm_detlef.java.application.main.preloader;


import javafx.application.Preloader;
import javafx.stage.Stage;

/* @formatter:off */

/**
 * TODO
 * @author Detlef G. Herm, 2016-09-26
 * @since 1.0
 *
 */
public class StageNotification implements Preloader.PreloaderNotification {

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    public final Stage stage;

    /**
     * TODO
     * <p>
     * @since 1.0
     */
    public StageNotification( Stage stage ) {
        this.stage = stage;
    }
}
