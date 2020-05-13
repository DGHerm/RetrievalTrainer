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
        assertTrue( out.failure );

        version = "11.0.2";
        out = CustomPreloader.checkVersionPatternJRE( version );
        assertTrue( out.failure );

        version = "0.0.0";
        out = CustomPreloader.checkVersionPatternJRE( version );
        assertFalse( out.failure );

        version = "11";
        out = CustomPreloader.checkVersionPatternJRE( version );
        assertFalse( out.failure );

        version = "11.0";
        out = CustomPreloader.checkVersionPatternJRE( version );
        assertFalse( out.failure );

        version = "11.0.2.3";
        out = CustomPreloader.checkVersionPatternJRE( version );
        assertFalse( out.failure );

        version = "";
        out = CustomPreloader.checkVersionPatternJRE( version );
        assertFalse( out.failure );

        version = null;
        out = CustomPreloader.checkVersionPatternJRE( version );
        assertFalse( out.failure );

        version = "A.B.C";
        out = CustomPreloader.checkVersionPatternJRE( version );
        assertFalse( out.failure );
    }

    @Test
    void checkMinimumRequiredJRE() {
    }
}