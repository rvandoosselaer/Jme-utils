package com.rvandoosselaer.jmeutils.gui;

import com.jme3.app.Application;
import com.jme3.cursors.plugins.JmeCursor;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.rvandoosselaer.jmeutils.ApplicationGlobals;
import com.simsilica.lemur.Panel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author rvandoosselaer
 */
@Slf4j
public class GuiUtils {

    /**
     * Updates the location of the panel if the panel goes outside the screen boundaries. This can be useful if you want
     * to make sure that draggable panels can't be dragged outside the screen.
     *
     * @param panel the panel to check
     */
    public static void clampBoundaries(Panel panel) {
        Vector3f location = new Vector3f(panel.getLocalTranslation());
        location.setX(FastMath.clamp(location.x, 0, getWidth() - panel.getPreferredSize().x));
        location.setY(FastMath.clamp(location.y, panel.getPreferredSize().y, getHeight()));

        panel.setLocalTranslation(location);
    }

    /**
     * Set the location of the panel to the center (horizontal and vertical) of the screen
     *
     * @param panel the panel to center
     */
    public static void center(Panel panel) {
        if (panel == null) {
            return;
        }

        Camera cam = getApplication().getCamera();
        int width = cam.getWidth();
        int height = cam.getHeight();
        panel.setLocalTranslation((width / 2) - (panel.getPreferredSize().getX() / 2), (height / 2) + (panel.getPreferredSize().getY() / 2), panel.getLocalTranslation().getZ());
    }

    /**
     * @return the height/resolution of the display
     */
    public static int getHeight() {
        return getApplication().getCamera().getHeight();
    }

    /**
     * @return the width/resolution of the display
     */
    public static int getWidth() {
        return getApplication().getCamera().getWidth();
    }

    /**
     * Sets a mouse cursor image. When a null string is given the default system cursor is set.
     *
     * @param image path to the cursor in the assets folder, or null for the default system cursor
     */
    public static void setMouseCursor(String image) {
        if (image != null) {
            log.trace("Setting mouse cursor: {}", image);
            getApplication().getInputManager().setMouseCursor((JmeCursor) getApplication().getAssetManager().loadAsset(image));
        } else {
            log.trace("Setting default system cursor");
            getApplication().getInputManager().setMouseCursor(null);
        }
    }

    private static Application getApplication() {
        ApplicationGlobals appGlobals = ApplicationGlobals.getInstance();
        if (appGlobals == null) {
            throw new RuntimeException("ApplicationGlobals are not initialized!");
        }

        return appGlobals.getApplication();
    }

}
