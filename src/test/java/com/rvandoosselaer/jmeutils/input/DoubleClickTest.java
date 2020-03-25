package com.rvandoosselaer.jmeutils.input;

import com.jme3.input.event.MouseButtonEvent;
import com.jme3.scene.Spatial;
import com.rvandoosselaer.jmeutils.JmeLauncher;
import com.rvandoosselaer.jmeutils.gui.GuiUtils;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Insets3f;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.event.MouseEventControl;
import com.simsilica.lemur.style.BaseStyles;

/**
 * @author: rvandoosselaer
 */
public class DoubleClickTest extends JmeLauncher {

    private Label label;

    public static void main(String[] args) {
        DoubleClickTest doubleClickTest = new DoubleClickTest();
        doubleClickTest.start();
    }

    @Override
    public void init() {
        BaseStyles.loadGlassStyle();
        GuiGlobals.getInstance().getStyles().setDefaultStyle(BaseStyles.GLASS);

        label = new Label("Double click me!");
        label.setInsets(new Insets3f(5, 5, 5, 5));

        MouseEventControl.addListenersToSpatial(label, new DoubleClickMouseListener() {
            @Override
            protected void doubleClick(MouseButtonEvent event, Spatial target, Spatial capture) {
                System.out.println("Double clicked mouse button: " + event.getButtonIndex());
            }
        });

        GuiUtils.center(label);
        getGuiNode().attachChild(label);
    }

}
