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


public interface BindingAnnotationNames {

    // Google Guice: binding annotation @Named takes a constant string ( attribute value must be constant )

    String MAIN
            = "de.herm_detlef.java.application.main.Main";
    String PREFERENCES
            = "de.herm_detlef.java.application.mvc.controller.preferences.PreferencesControllerImpl";
    String TOOLS_PANEL
            = "de.herm_detlef.java.application.mvc.controller.edit.edit_menu.ToolsPanelControllerImpl";
}
