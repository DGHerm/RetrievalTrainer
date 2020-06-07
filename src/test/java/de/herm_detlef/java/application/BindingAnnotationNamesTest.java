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

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BindingAnnotationNamesTest {

    @Test
    void uniqueNames() {

        long count = Arrays.stream( BindingAnnotationNames.class.getFields() )
                .map( BindingAnnotationNamesTest::valueOf )
                .filter( Optional::isPresent )
                .map( Optional::get )
                .count();

        long distinctCount = Arrays.stream( BindingAnnotationNames.class.getFields() )
                .map( BindingAnnotationNamesTest::valueOf )
                .filter( Optional::isPresent )
                .map( Optional::get )
                .distinct()
                .count();

        assertEquals(count, distinctCount);
    }

    private static Optional<String> valueOf(Field f ) {
        String val = "";
        try {
            return Optional.of( (String) f.get(val) );
        } catch (IllegalAccessException e) {
            return Optional.empty();
        }
    }

}