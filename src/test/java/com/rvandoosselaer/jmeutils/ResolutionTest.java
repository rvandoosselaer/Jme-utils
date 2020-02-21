package com.rvandoosselaer.jmeutils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author: rvandoosselaer
 */
public class ResolutionTest {

    @Test
    public void parseResolutionFromString() {
        String resolutionAsString = "1024x768:32";
        Resolution resolution = Resolution.fromString(resolutionAsString);

        assertEquals(1024, resolution.getWidth());
        assertEquals(768, resolution.getHeight());
        assertEquals(32, resolution.getBpp());
    }

    @Test
    public void parseResolutionFromStringThrowsException() {
        assertThrows(RuntimeException.class, () -> Resolution.fromString(null));
        assertThrows(RuntimeException.class, () -> Resolution.fromString("1024:768x32"));
        assertThrows(RuntimeException.class, () -> Resolution.fromString("1024 - 768 - 32"));
        assertThrows(RuntimeException.class, () -> Resolution.fromString("qwerty"));
    }
}
