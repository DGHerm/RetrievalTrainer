package de.herm_detlef.java.application.preloader;

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