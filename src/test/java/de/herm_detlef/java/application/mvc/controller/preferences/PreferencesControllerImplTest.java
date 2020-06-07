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

package de.herm_detlef.java.application.mvc.controller.preferences;

import org.junit.jupiter.api.Test;

import static de.herm_detlef.java.application.BindingAnnotationNames.PREFERENCES;
import static org.junit.jupiter.api.Assertions.*;

class PreferencesControllerImplTest {

    @Test
    void bindingAnnotationName() {
        assertEquals( PreferencesControllerImpl.class.getName(), PREFERENCES );
    }
}
