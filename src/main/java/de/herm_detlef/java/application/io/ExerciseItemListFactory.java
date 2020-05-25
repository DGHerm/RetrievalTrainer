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


import java.io.File;
import java.util.ArrayList;

import de.herm_detlef.java.application.CommonData;
import de.herm_detlef.java.application.mvc.model.ExerciseItem;
import de.herm_detlef.java.application.utilities.Utilities;

import static de.herm_detlef.java.application.ApplicationConstants.DEBUG;
import static de.herm_detlef.java.application.ApplicationConstants.LOGGER;

/* @formatter:off */

/**
 * TODO
 * @author Detlef G. Herm, 2016-09-26
 * @since 1.0
 *
 */
public class ExerciseItemListFactory {

    private ExerciseItemListFactory() {}

    public static ArrayList<ExerciseItem> importExerciseItemListFromFile( String filename ) {
        try {
            return Import.importExerciseItemListFromFile( filename );
        } catch ( Exception e ) {
            if (DEBUG) e.printStackTrace();
            LOGGER.severe( e.getClass().getSimpleName() + ": " + e.getMessage() );
            Utilities.showErrorMessage(
                    e.getClass().getSimpleName(),
                    e.getMessage());
            return new ArrayList<>(0);
        }
    }

    public static void exportExerciseItemListToFile( CommonData commonData, File file ) {
        try {
            Export.exportExerciseItemListToFile( commonData, file );
        } catch ( Exception e ) {
            if (DEBUG) e.printStackTrace();
            LOGGER.severe( e.getClass().getSimpleName() + ": " + e.getMessage() );
            Utilities.showErrorMessage(
                    e.getClass().getSimpleName(),
                    e.getMessage());
        }
    }
}
