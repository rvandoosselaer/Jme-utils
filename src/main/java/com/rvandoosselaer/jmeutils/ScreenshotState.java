package com.rvandoosselaer.jmeutils;

import com.jme3.app.Application;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.post.SceneProcessor;
import com.jme3.profile.AppProfiler;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.Renderer;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.texture.FrameBuffer;
import com.jme3.texture.Image;
import com.jme3.texture.image.ColorSpace;
import com.jme3.util.BufferUtils;
import com.rvandoosselaer.jmeutils.util.ImageUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;

/**
 * An AppState that captures the framebuffer to which the scene was rendered and writes it to an image. The state
 * uses a SceneProcessor that is attached to the last post view port. Make sure it is attached after all the post
 * view ports are created.
 * <p>
 * An optional processImage function can be set to process the image before it's written.
 *
 * @author: rvandoosselaer
 */
@Slf4j
@RequiredArgsConstructor
public class ScreenshotState implements AppState {

    private final Path directory;
    private boolean capture;
    private ViewPort screenshotProcessorViewPort;
    private ScreenshotProcessor screenshotProcessor;
    private boolean initialized;
    @Getter
    private Image.Format format = Image.Format.RGBA8;
    @Getter
    @Setter
    private Function<Image, Image> processFunction = image -> image;
    @Getter
    @Setter
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd 'at' hh:mm:ss,SSS");
    @Getter
    @Setter
    private boolean addTimestamp = true;
    @Getter
    @Setter
    private String filename;
    @Getter
    @Setter
    private String defaultFilename = "Screenshot";
    private final String extension = ".png";

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        List<ViewPort> postViewPorts = app.getRenderManager().getPostViews();
        if (postViewPorts.isEmpty()) {
            // this should not happen, the gui view port is a post view port.
            throw new IllegalStateException("No post view ports available.");
        }
        screenshotProcessorViewPort = postViewPorts.get(postViewPorts.size() - 1);

        screenshotProcessor = new ScreenshotProcessor();
        screenshotProcessorViewPort.addProcessor(screenshotProcessor);

        initialized = true;
    }

    @Override
    public boolean isInitialized() {
        return initialized && screenshotProcessor.isInitialized();
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setEnabled(boolean active) {
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void stateAttached(AppStateManager stateManager) {

    }

    @Override
    public void stateDetached(AppStateManager stateManager) {

    }

    @Override
    public void update(float tpf) {

    }

    @Override
    public void render(RenderManager rm) {

    }

    @Override
    public void postRender() {

    }

    @Override
    public void cleanup() {
        screenshotProcessorViewPort.removeProcessor(screenshotProcessor);
        initialized = false;
    }

    public void takeScreenshot() {
        this.capture = true;
    }

    public void setFormat(Image.Format format) {
        this.format = format;
        if (screenshotProcessor.isInitialized()) {
            screenshotProcessor.formatUpdated();
        }
    }

    private String createFilename() {
        if (filename != null && !filename.isEmpty()) {
            return filename + extension;
        }

        String filename = defaultFilename;
        if (addTimestamp && dateTimeFormatter != null) {
            filename += " " + LocalDateTime.now().format(dateTimeFormatter);
        }
        return filename + extension;
    }

    private class ScreenshotProcessor implements SceneProcessor {

        private Renderer renderer;
        private int width;
        private int height;
        private ByteBuffer imageBuffer;
        private boolean initialized;

        @Override
        public void initialize(RenderManager rm, ViewPort vp) {
            renderer = rm.getRenderer();
            width = vp.getCamera().getWidth();
            height = vp.getCamera().getHeight();
            imageBuffer = createImageBuffer(width, height, format);

            initialized = true;
        }

        @Override
        public void reshape(ViewPort vp, int w, int h) {
            width = w;
            height = h;
            imageBuffer = createImageBuffer(w, h, format);
        }

        @Override
        public boolean isInitialized() {
            return initialized;
        }

        @Override
        public void preFrame(float tpf) {
        }

        @Override
        public void postQueue(RenderQueue rq) {
        }

        @Override
        public void postFrame(FrameBuffer out) {
            if (!capture) {
                return;
            }

            renderer.readFrameBufferWithFormat(out, imageBuffer, format);
            Image image = new Image(format, width, height, imageBuffer, ColorSpace.Linear);
            if (processFunction != null) {
                processFunction.apply(image);
                rewindBuffer(imageBuffer);
            }

            String filename = createFilename();
            Path path = directory.resolve(filename);
            log.info("Saving screenshot to {}", path);
            ImageUtils.writeImageSilently(image, path);

            capture = false;
        }

        @Override
        public void cleanup() {
            BufferUtils.destroyDirectBuffer(imageBuffer);
            initialized = false;
        }

        @Override
        public void setProfiler(AppProfiler profiler) {
        }

        public void formatUpdated() {
            imageBuffer = createImageBuffer(width, height, format);
        }

        private ByteBuffer createImageBuffer(int width, int height, Image.Format imageFormat) {
            return BufferUtils.createByteBuffer(width * height * (int) Math.ceil(imageFormat.getBitsPerPixel() / 8.0));
        }

        private void rewindBuffer(Buffer buffer) {
            // Since JDK 9, ByteBuffer class overrides some methods and their return type in the Buffer class. To
            // ensure compatibility with JDK 8, calling the 'rewindBuffer' method forces using the
            // JDK 8 Buffer's methods signature, and avoids explicit casts.
            buffer.rewind();
        }

    }

}
