package com.rvandoosselaer.jmeutils;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.scene.Node;
import lombok.extern.slf4j.Slf4j;

/**
 * A singleton implementation that holds a {@link SimpleApplication} object for easy retrieval.
 * The {@link #initialize(SimpleApplication)} method should be called before this singleton can be used.
 * When the {@link JmeLauncher} class is used as a starting point, the {@link ApplicationGlobals} singleton will be
 * already correctly initialized.
 *
 * @author rvandoosselaer
 */
@Slf4j
public final class ApplicationGlobals {

    private static ApplicationGlobals instance;

    private SimpleApplication application;

    private ApplicationGlobals(SimpleApplication application) {
        this.application = application;
    }

    public static void initialize(SimpleApplication application) {
        instance = new ApplicationGlobals(application);
        log.debug("Initializing ApplicationGlobals with {}", application);
    }

    public static ApplicationGlobals getInstance() {
        return instance;
    }

    public Application getApplication() {
        return application;
    }

    public Node getRootNode() {
        return application.getRootNode();
    }

    public Node getGuiNode() {
        return application.getGuiNode();
    }

}
