package org.randomstack.jme;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.scene.Node;
import lombok.extern.slf4j.Slf4j;

/**
 * A singleton implementation that holds an {@link Application} object for easy retrieval.
 * The {@link #initialize(Application)} method should be called before this singleton can be used.
 * When the {@link JmeLauncher} class is used as a starting point, the {@link ApplicationGlobals} singleton will be
 * already correctly initialized.
 *
 * @author remy
 */
@Slf4j
public final class ApplicationGlobals {

    private static ApplicationGlobals instance;

    private Application application;

    private ApplicationGlobals(Application application) {
        this.application = application;
    }

    public static void initialize(Application application) {
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
        return ((SimpleApplication) application).getRootNode();
    }

    public Node getGuiNode() {
        return ((SimpleApplication) application).getGuiNode();
    }

}
