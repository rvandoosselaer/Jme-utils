package com.rvandoosselaer.jmeutils.util;

import com.jme3.math.ColorRGBA;
import com.jme3.texture.Image;
import com.jme3.texture.image.ColorSpace;
import com.jme3.texture.image.ImageRaster;
import com.jme3.util.BufferUtils;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Utility class for image manipulations.
 *
 * @author: rvandoosselaer
 */
@Slf4j
public class ImageUtils {

    private ImageUtils() {
    }

    public static Image createImage(int width, int height) {
        return createImage(Image.Format.RGBA8, width, height);
    }

    public static Image createImage(Image.Format format, int width, int height) {
        int bufferSize = width * height * (int) Math.ceil(format.getBitsPerPixel() / 8.0);
        return new Image(format, width, height, BufferUtils.createByteBuffer(bufferSize), ColorSpace.Linear);
    }

    public static void writeImage(Image image, Path path) throws IOException {
        writeImage(image, Files.newOutputStream(path));
    }

    public static void writeImage(Image image, OutputStream out) throws IOException {
        ImageIO.write(imageToBufferedImage(image), "png", out);
    }

    public static void writeImageSilently(Image image, Path path) {
        try {
            writeImage(image, Files.newOutputStream(path));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static void writeImageSilently(Image image, OutputStream out) {
        try {
            ImageIO.write(imageToBufferedImage(image), "png", out);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static BufferedImage imageToBufferedImage(Image image) {
        boolean alpha;
        int A = 0;
        int R = 0;
        int G = 0;
        int B = 0;
        switch (image.getFormat()) {
            case RGBA8:
                R = 0;
                G = 1;
                B = 2;
                A = 3;
                alpha = true;
                break;
            case ABGR8:
                A = 0;
                B = 1;
                G = 2;
                R = 3;
                alpha = true;
                break;
            case ARGB8:
                A = 0;
                R = 1;
                G = 2;
                B = 3;
                alpha = true;
                break;
            case BGRA8:
                B = 0;
                G = 1;
                R = 2;
                A = 3;
                alpha = true;
                break;
            case RGB8:
                R = 0;
                G = 1;
                B = 2;
                alpha = false;
                break;
            case BGR8:
                B = 0;
                G = 1;
                R = 2;
                alpha = false;
                break;
            default:
                throw new IllegalArgumentException(image.getFormat() + " not yet supported.");
        }

        log.debug("Converting {} image to {}", image.getFormat(), Image.Format.ARGB8);

        ByteBuffer bb = image.getData(0);
        clearBuffer(bb);

        int bufferedImageFormat = alpha ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(), image.getHeight(), bufferedImageFormat);

        int channels = alpha ? 4 : 3;
        int steps = bb.limit() / channels;
        int[] raw = new int[bb.limit()];

        for (int i = 0; i < steps; i++) {
            if (alpha) {
                raw[i] = ((bb.get(channels * i + B) & 0xFF)) |
                        ((bb.get(channels * i + A) & 0xFF) << 24) |
                        ((bb.get(channels * i + R) & 0xFF) << 16) |
                        ((bb.get(channels * i + G) & 0xFF) << 8);
            } else {
                raw[i] = ((bb.get(3 * i + R) & 0xFF) << 16) |
                        ((bb.get(3 * i + G) & 0xFF) << 8) |
                        ((bb.get(3 * i + B) & 0xFF));
            }
        }

        bufferedImage.setRGB(0, 0, image.getWidth(), image.getHeight(), raw, 0, image.getWidth());
        flipVertical(bufferedImage);

        return bufferedImage;
    }

    public static BufferedImage flipVertical(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        // we only need to loop through half of the image. otherwise we would be putting the swapped pixels back.
        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height * 0.5; h++) {
                // swap the opposite y pixels
                int pixel = image.getRGB(w, h);
                int oppositePixel = image.getRGB(w, (height - 1) - h);

                image.setRGB(w, h, oppositePixel);
                image.setRGB(w, (height - 1) - h, pixel);
            }
        }

        return image;
    }

    public static void replaceColors(Image image, ColorRGBA oldColor, ColorRGBA newColor) {
        int width = image.getWidth();
        int height = image.getHeight();

        ImageRaster imageRaster = ImageRaster.create(image);

        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                ColorRGBA pixel = imageRaster.getPixel(w, h);
                if (Objects.equals(pixel, oldColor)) {
                    imageRaster.setPixel(w, h, newColor);
                }
            }
        }
    }

    private static void clearBuffer(Buffer buffer) {
        // Since JDK 9, ByteBuffer class overrides some methods and their return type in the Buffer class. To
        // ensure compatibility with JDK 8, calling the 'clear' method forces using the
        // JDK 8 Buffer's methods signature, and avoids explicit casts.
        buffer.clear();
    }

}
