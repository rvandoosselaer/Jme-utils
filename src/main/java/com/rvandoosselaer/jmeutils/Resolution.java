package com.rvandoosselaer.jmeutils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

/**
 * A POJO describing a screen resolution.
 *
 * @author remy
 */
@Getter
@RequiredArgsConstructor
public class Resolution {

    private final int width;
    private final int height;
    private final int bpp;

    @Override
    public String toString() {
        return width + "x" + height + ":" + bpp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resolution that = (Resolution) o;
        return width == that.width &&
                height == that.height &&
                bpp == that.bpp;
    }

    @Override
    public int hashCode() {
        return Objects.hash(width, height, bpp);
    }

    /**
     * Parses a string in the format: {width}x{height}:{depth} to a resolution object.
     * eg. 1280x800:24
     * @param resolution string representation of a resolution
     * @return resolution
     */
    public static Resolution fromString(String resolution) {
        if (!resolution.contains("x") || !resolution.contains(":") || resolution.indexOf("x") >= resolution.indexOf(":")) {
            throw new RuntimeException("Unable to parse the given resolution: " + resolution);
        }
        String width = resolution.substring(0, resolution.indexOf("x"));
        String height = resolution.substring(resolution.indexOf("x") + 1, resolution.indexOf(":"));
        String bpp = resolution.substring(resolution.indexOf(":") + 1);
        return new Resolution(Integer.parseInt(width), Integer.parseInt(height), Integer.parseInt(bpp));
    }

}
