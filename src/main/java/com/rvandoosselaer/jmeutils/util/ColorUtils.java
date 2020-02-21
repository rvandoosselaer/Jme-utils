package com.rvandoosselaer.jmeutils.util;

import com.jme3.math.ColorRGBA;
import lombok.extern.slf4j.Slf4j;

/**
 * A helper class with utility methods for color related operations.
 *
 * @author rvandoosselaer
 */
@Slf4j
public class ColorUtils {

    public static ColorRGBA fromHex(String hex) {
        return fromHex(hex, false);
    }

    /**
     * Returns the hexadecimal representation of the supplied color object
     *
     * @param color the color
     * @return hexadecimal string representation of the color
     */
    public static String toHex(ColorRGBA color) {
        return toHex(color, true);
    }

    /**
     * Returns the hexadecimal representation of the supplied color
     *
     * @param color               the color
     * @param includeAlphaChannel whether or not the alpha channel should be returned
     * @return hexadecimal string
     */
    public static String toHex(ColorRGBA color, boolean includeAlphaChannel) {
        return includeAlphaChannel ?
                "#" + leftPad(Integer.toHexString((int) (color.r * 255))).toUpperCase()
                        + leftPad(Integer.toHexString((int) (color.g * 255))).toUpperCase()
                        + leftPad(Integer.toHexString((int) (color.b * 255))).toUpperCase()
                        + leftPad(Integer.toHexString((int) (color.a * 255))).toUpperCase()
                :
                "#" + leftPad(Integer.toHexString((int) (color.r * 255))).toUpperCase()
                        + leftPad(Integer.toHexString((int) (color.g * 255))).toUpperCase()
                        + leftPad(Integer.toHexString((int) (color.b * 255))).toUpperCase();
    }

    /**
     * Returns a ColorRGBA object from the passed hex string. When the color is not in linear space, the values will
     * be gamma corrected to be stored in linear space. GAMMA value is 2.2
     * When the color is picked from a color picker or GUI, the linearSpace boolean should probably be set to false.
     *
     * @param hex         the hex string
     * @param linearSpace if the color is in linear space
     * @return the color derived from the hex, or null when the parsing did not succeed
     */
    public static ColorRGBA fromHex(String hex, boolean linearSpace) {
        String cleaned = clean(hex);
        if (cleaned.length() == 6) {
            cleaned += "FF"; // add alpha
        }
        if (cleaned.length() != 8) {
            log.error("Invalid hex code: '{}' specified!", hex);
            return null;
        }
        try {
            return linearSpace ? new ColorRGBA(Integer.valueOf(cleaned.substring(0, 2), 16) / 255.0f, Integer.valueOf(cleaned.substring(2, 4), 16) / 255.0f, Integer.valueOf(cleaned.substring(4, 6), 16) / 255.0f, Integer.valueOf(cleaned.substring(6), 16) / 255.0f) :
                    new ColorRGBA().setAsSrgb(Integer.valueOf(cleaned.substring(0, 2), 16) / 255.0f, Integer.valueOf(cleaned.substring(2, 4), 16) / 255.0f, Integer.valueOf(cleaned.substring(4, 6), 16) / 255.0f, Integer.valueOf(cleaned.substring(6), 16) / 255.0f);
        } catch (Exception e) {
            log.error("Invalid hex code: '{}' specified!", hex);
            return null;
        }
    }

    /**
     * Returns a ColorRGBA object from the passed red, green, blue, alpha values.
     *
     * @param red   red value
     * @param green green value
     * @param blue  blue value
     * @param alpha alpha value
     * @return the color from the given input or null
     */
    public static ColorRGBA fromRGBA(int red, int green, int blue, int alpha) {
        return fromRGBA(red, green, blue, alpha, false);
    }

    /**
     * Returns a ColorRGBA object from the passed red, green, blue, alpha values. When the color is not in linear space,
     * the values will be gamma corrected to be stored in linear space. GAMMA value is 2.2
     * When the color is picked from a color picker or GUI, the linearSpace boolean should probably be set to false.
     *
     * @param red         red value
     * @param green       green value
     * @param blue        blue value
     * @param alpha       alpha value
     * @param linearSpace is the color in linear space
     * @return the color from the given input or null
     */
    public static ColorRGBA fromRGBA(int red, int green, int blue, int alpha, boolean linearSpace) {
        if (red < 0 || red > 255) {
            log.error("Invalid red value: '{}' specified!", red);
            return null;
        }
        if (green < 0 || green > 255) {
            log.error("Invalid green value: '{}' specified!", green);
            return null;
        }
        if (blue < 0 || blue > 255) {
            log.error("Invalid blue value: '{}' specified!", blue);
            return null;
        }
        if (alpha < 0 || alpha > 255) {
            log.error("Invalid alpha value: '{}' specified!", alpha);
            return null;
        }
        try {
            return linearSpace ? new ColorRGBA(red / 255f, green / 255f, blue / 255f, alpha / 255f) :
                    new ColorRGBA().setAsSrgb(red / 255f, green / 255f, blue / 255f, alpha / 255f);
        } catch (Exception e) {
            log.error("Invalid RGBA value: '{}, {}, {}, {}' specified!", red, green, blue, alpha);
            return null;
        }
    }

    private static String clean(String s) {
        String string = s.trim();
        string = string.startsWith("#") ? string.substring(1) : string;
        return string;
    }

    private static String leftPad(String s) {
        return s.length() == 1 ? "0" + s : s;
    }

}
