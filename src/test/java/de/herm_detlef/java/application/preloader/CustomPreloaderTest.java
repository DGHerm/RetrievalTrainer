package de.herm_detlef.java.application.preloader;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static de.herm_detlef.java.application.preloader.CustomPreloader.*;
import static org.junit.jupiter.api.Assertions.*;

class CustomPreloaderTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void checkVersionPatternJRE() {

        String version;
        Outcome out;

        version = "1.8.0_202";
        out = CustomPreloader.checkVersionPatternJRE( version );
        assertTrue( out.success );

        version = "11.0.2";
        out = CustomPreloader.checkVersionPatternJRE( version );
        assertTrue( out.success );

        version = "0.0.0";
        out = CustomPreloader.checkVersionPatternJRE( version );
        assertFalse( out.success );

        version = "11";
        out = CustomPreloader.checkVersionPatternJRE( version );
        assertFalse( out.success );

        version = "11.0";
        out = CustomPreloader.checkVersionPatternJRE( version );
        assertFalse( out.success );

        version = "11.0.2.3";
        out = CustomPreloader.checkVersionPatternJRE( version );
        assertFalse( out.success );

        version = "";
        out = CustomPreloader.checkVersionPatternJRE( version );
        assertFalse( out.success );

        version = null;
        out = CustomPreloader.checkVersionPatternJRE( version );
        assertFalse( out.success );

        version = "A.B.C";
        out = CustomPreloader.checkVersionPatternJRE( version );
        assertFalse( out.success );
    }

    @Test
    void checkMinimumRequiredJRE() {

        String version;
        Outcome out;

        version = "11.0.2";
        out = CustomPreloader.checkMinimumRequiredJRE( version );
        assertTrue( out.success );

        version = "14.0.1";
        out = CustomPreloader.checkMinimumRequiredJRE( version );
        assertTrue( out.success );

        version = "10.0.2";
        out = CustomPreloader.checkMinimumRequiredJRE( version );
        assertFalse( out.success );

        version = "1.8.0_202";
        out = CustomPreloader.checkMinimumRequiredJRE( version );
        assertFalse( out.success );

        version = "";
        out = CustomPreloader.checkMinimumRequiredJRE( version );
        assertFalse( out.success );

        version = null;
        out = CustomPreloader.checkMinimumRequiredJRE( version );
        assertFalse( out.success );
    }
}