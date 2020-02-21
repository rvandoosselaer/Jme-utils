package com.rvandoosselaer.jmeutils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author: rvandoosselaer
 */
public class ResolutionTest {

    @Test
    public void parseResolutionFromString() {
        String resolutionAsString = "1024x768:32";
        Resolution resolution = Resolution.fromString(resolutionAsString);

        Assertions.assertEquals(1024, resolution.getWidth());
        Assertions.assertEquals(768, resolution.getHeight());
        Assertions.assertEquals(32, resolution.getBpp());
    }

    @Test
    public void parseResolutionFromStringThrowsException() {
        Assertions.assertThrows(RuntimeException.class, () -> Resolution.fromString(null));
        Assertions.assertThrows(RuntimeException.class, () -> Resolution.fromString("1024:768x32"));
        Assertions.assertThrows(RuntimeException.class, () -> Resolution.fromString("1024 - 768 - 32"));
        Assertions.assertThrows(RuntimeException.class, () -> Resolution.fromString("qwerty"));
    }
}
