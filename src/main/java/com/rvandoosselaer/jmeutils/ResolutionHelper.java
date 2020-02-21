package com.rvandoosselaer.jmeutils;

import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A helper class to retrieve supported resolutions for the default screen device.
 *
 * @author rvandoosselaer
 */
public class ResolutionHelper {

    /**
     * Returns a list of all supported resolutions for the default screen device retrieved from the {@link GraphicsEnvironment}
     *
     * @return a list of resolutions
     */
    public static List<Resolution> getSupportedResolutions() {
        DisplayMode[] displayModes = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayModes();

        return Arrays.stream(displayModes)
                .map(displayMode -> new Resolution(displayMode.getWidth(), displayMode.getHeight(), displayMode.getBitDepth()))
                .collect(Collectors.toList());
    }

    /**
     * Picks the first supported HD resolution for the default screen device. When a HD resolution isn't available, the
     * resolution of the current display mode is returned.
     *
     * @return the screen resolution
     */
    public static Resolution getFirstHDResolution() {
        DisplayMode displayMode = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();

        return getSupportedResolutions().stream()
                .filter(r -> r.getBpp() == displayMode.getBitDepth() && r.getWidth() >= 1280)
                .min(Comparator.comparingInt(Resolution::getWidth))
                .orElse(new Resolution(displayMode.getWidth(), displayMode.getHeight(), displayMode.getBitDepth()));
    }

}
