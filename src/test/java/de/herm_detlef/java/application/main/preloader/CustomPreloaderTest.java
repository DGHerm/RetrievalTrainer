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


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomPreloaderTest {

    @Test
    void checkVersionPatternJRE() {

        String version;
        CustomPreloader.Outcome out;

        version = "1.8.0_202";
        out = CustomPreloader.checkVersionPatternJRE( version );
        assertTrue( out.succeeded );

        version = "11.0.2";
        out = CustomPreloader.checkVersionPatternJRE( version );
        assertTrue( out.succeeded );

        version = "0.0.0";
        out = CustomPreloader.checkVersionPatternJRE( version );
        assertFalse( out.succeeded );

        version = "11";
        out = CustomPreloader.checkVersionPatternJRE( version );
        assertFalse( out.succeeded );

        version = "11.0";
        out = CustomPreloader.checkVersionPatternJRE( version );
        assertFalse( out.succeeded );

        version = "11.0.2.3";
        out = CustomPreloader.checkVersionPatternJRE( version );
        assertFalse( out.succeeded );

        version = "";
        out = CustomPreloader.checkVersionPatternJRE( version );
        assertFalse( out.succeeded );

        version = null;
        out = CustomPreloader.checkVersionPatternJRE( version );
        assertFalse( out.succeeded );

        version = "A.B.C";
        out = CustomPreloader.checkVersionPatternJRE( version );
        assertFalse( out.succeeded );
    }

    @Test
    void checkMinimumRequiredJRE() {

        String version;
        CustomPreloader.Outcome out;

        version = "11.0.2";
        out = CustomPreloader.checkMinimumRequiredJRE( version );
        assertTrue( out.succeeded );

        version = "14.0.1";
        out = CustomPreloader.checkMinimumRequiredJRE( version );
        assertTrue( out.succeeded );

        version = "10.0.2";
        out = CustomPreloader.checkMinimumRequiredJRE( version );
        assertFalse( out.succeeded );

        version = "1.8.0_202";
        out = CustomPreloader.checkMinimumRequiredJRE( version );
        assertFalse( out.succeeded );

        version = "";
        out = CustomPreloader.checkMinimumRequiredJRE( version );
        assertFalse( out.succeeded );

        version = null;
        out = CustomPreloader.checkMinimumRequiredJRE( version );
        assertFalse( out.succeeded );
    }
}