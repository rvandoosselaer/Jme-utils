package org.randomstack.jme;

import com.jme3.system.AppSettings;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

/**
 * A factory class that creates an {@link AppSettings} object from parameters retrieved from the {@link ApplicationProperties}.
 * file.
 *
 * @author remy
 */
@Slf4j
public class ApplicationSettingsFactory {

    public static final String NAME = "name";
    public static final String VERSION = "version";
    public static final String BUILD = "build";
    public static final String TITLE = "title";
    public static final String VSYNC = "vsync";
    public static final String GAMMA_CORRECTION = "gammaCorrection";
    public static final String FRAMERATE = "framerate";
    public static final String ANTI_ALIASING = "antiAliasing";
    public static final String RENDERER = "renderer";
    public static final String RESOLUTION = "resolution";
    public static final String FULLSCREEN = "fullscreen";

    private static final boolean VSYNC_DEFAULT = true;
    private static final boolean GAMMA_CORRECTION_DEFAULT = true;
    private static final int FRAMERATE_DEFAULT = 60;
    private static final int ANTI_ALIASING_DEFAULT = 0;
    private static final String RENDERER_DEFAULT = AppSettings.LWJGL_OPENGL2;
    private static final String RESOLUTION_DEFAULT = ResolutionHelper.getFirstHDResolution().toString();
    private static final boolean FULLSCREEN_DEFAULT = false;

    public static AppSettings getAppSettings() {
        log.info("Loading application settings");
        log.debug("Supported resolutions: {}", ResolutionHelper.getSupportedResolutions());

        GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode displayMode = graphicsDevice.getDisplayMode();

        ApplicationProperties props = ApplicationProperties.getInstance();

        AppSettings settings = new AppSettings(true);
        settings.setFrequency(displayMode.getRefreshRate());
        settings.setTitle(getTitle());
        settings.setGammaCorrection(props.get(GAMMA_CORRECTION, GAMMA_CORRECTION_DEFAULT));
        settings.setFrameRate(props.get(FRAMERATE, FRAMERATE_DEFAULT));
        settings.setVSync(props.get(VSYNC, VSYNC_DEFAULT));
        settings.setSamples(props.get(ANTI_ALIASING, ANTI_ALIASING_DEFAULT));
        settings.setRenderer(props.get(RENDERER, RENDERER_DEFAULT));
        settings.setFullscreen(props.get(FULLSCREEN, FULLSCREEN_DEFAULT));

        Resolution resolution = Resolution.fromString(props.get(RESOLUTION, RESOLUTION_DEFAULT));
        settings.setWidth(resolution.getWidth());
        settings.setHeight(resolution.getHeight());
        settings.setBitsPerPixel(resolution.getBpp());

        log.debug("Settings: {}", settings);
        return settings;
    }

    /**
     * The title is either the 'title' field in the application.properties file, or a concatenation of the name,
     * version and build
     *
     * @return the title
     */
    private static String getTitle() {
        ApplicationProperties props = ApplicationProperties.getInstance();

        String title = props.get(TITLE, "");
        if (title.isEmpty()) {
            String name = props.get(NAME, "Jme3-utils");
            String version = props.get(VERSION, "0.1");
            String build = props.get(BUILD, "");
            title = build.isEmpty() ? name + " - " + version : name + " - " + version + " build " + build;
        }
        return title;
    }

}
