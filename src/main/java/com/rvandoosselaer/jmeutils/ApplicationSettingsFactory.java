package com.rvandoosselaer.jmeutils;

import com.jme3.system.AppSettings;
import com.jme3.system.JmeVersion;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

/**
 * A factory class that creates an {@link AppSettings} object from parameters retrieved from the {@link ApplicationProperties}.
 * file.
 *
 * @author rvandoosselaer
 */
@Slf4j
public class ApplicationSettingsFactory {

    public static final String TITLE = "title";
    public static final String VSYNC = "vsync";
    public static final String GAMMA_CORRECTION = "gammaCorrection";
    public static final String FRAMERATE = "framerate";
    public static final String ANTI_ALIASING = "antiAliasing";
    public static final String RENDERER = "renderer";
    public static final String RESOLUTION = "resolution";
    public static final String FULLSCREEN = "fullscreen";
    public static final String FREQUENCY = "frequency";
    public static final String RESIZABLE = "resizable";

    public static AppSettings getAppSettings() {
        log.info("Loading application settings ...");
        log.debug("Supported resolutions: {}", ResolutionHelper.getSupportedResolutions());

        GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode displayMode = graphicsDevice.getDisplayMode();

        ApplicationProperties props = ApplicationProperties.getInstance();

        AppSettings settings = new AppSettings(true);
        settings.setFrequency(props.get(FREQUENCY, displayMode.getRefreshRate()));
        settings.setTitle(props.get(TITLE, JmeVersion.FULL_NAME));
        settings.setGammaCorrection(props.get(GAMMA_CORRECTION, true));
        settings.setFrameRate(props.get(FRAMERATE, -1));
        settings.setVSync(props.get(VSYNC, false));
        settings.setSamples(props.get(ANTI_ALIASING, 0));
        settings.setRenderer(props.get(RENDERER, AppSettings.LWJGL_OPENGL2));
        settings.setFullscreen(props.get(FULLSCREEN, false));
        settings.setResizable(props.get(RESIZABLE, false));

        Resolution resolution = Resolution.fromString(props.get(RESOLUTION, ResolutionHelper.getFirstHDResolution().toString()));
        settings.setWidth(resolution.getWidth());
        settings.setHeight(resolution.getHeight());
        settings.setBitsPerPixel(resolution.getBpp());

        log.debug("Settings: {}", settings);
        return settings;
    }

}
