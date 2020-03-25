package com.rvandoosselaer.jmeutils.input;

import com.jme3.input.event.MouseButtonEvent;
import com.jme3.scene.Spatial;
import com.simsilica.lemur.event.DefaultMouseListener;
import lombok.Getter;
import lombok.Setter;

/**
 * MouseListener implementation that calls an overridable {@link #doubleClick(MouseButtonEvent, Spatial, Spatial)}
 * method. A double click is registered when 2 consecutive click events happened in a given delay using the same mouse
 * button. The time between the 2 clicks is measured between the release events of the clicks.
 *
 * @author: rvandoosselaer
 */
public class DoubleClickMouseListener extends DefaultMouseListener {

    @Getter
    @Setter
    private float delay = 500f;
    private long lastClickTimestamp = -1;
    private int lastClickButtonIndex = -1;

    public DoubleClickMouseListener() {
    }

    public DoubleClickMouseListener(float delay) {
        this.delay = delay;
    }

    public DoubleClickMouseListener(int xClickThreshold, int yClickThreshold, float delay) {
        super(xClickThreshold, yClickThreshold);
        this.delay = delay;
    }

    @Override
    protected void click(MouseButtonEvent event, Spatial target, Spatial capture) {
        long timestamp = System.currentTimeMillis();
        int buttonIndex = event.getButtonIndex();

        // fire a double click event when 2 consecutive clicks happened in the set delay and when the 2 clicks
        // where performed with the same mouse button.
        boolean doubleClicked = timestamp - lastClickTimestamp <= delay && lastClickButtonIndex == buttonIndex;
        if (doubleClicked) {
            doubleClick(event, target, capture);
        }

        lastClickTimestamp = timestamp;
        lastClickButtonIndex = buttonIndex;
    }

    protected void doubleClick(MouseButtonEvent event, Spatial target, Spatial capture) {
    }

}
