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

package de.herm_detlef.java.application.io;


import java.util.Hashtable;
import java.util.Locale;

/* @formatter:off */

/**
 * TODO
 * @author Detlef G. Herm, 2016-09-26
 * @since 1.0
 *
 */
enum TAG {
          CATALOG, ITEM, ID, QUESTION, SINGLE_CHOICE_ANSWER, MULTIPLE_CHOICE_ANSWER, SOLUTION, TEXT, CODE, TEXT2;

    private static Hashtable< String, TAG > initializeMapping() {

        // initial capacity: prime number !
        Hashtable< String, TAG > m = new Hashtable<>( 53 );
        for ( TAG enumerator : TAG.values() ) {
            m.put(
                enumerator.name(),
                enumerator );
            m.put(
                enumerator.name().toLowerCase(
                    Locale.ROOT ),
                enumerator );
        }
        return m;
    }

    private final static Hashtable< String, TAG > map = initializeMapping();

    public static TAG getValueOf( String s ) {

        return map.get(
            s );
    }
}
