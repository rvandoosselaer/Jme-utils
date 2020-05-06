package com.rvandoosselaer.jmeutils.lemur;

import com.jme3.bounding.BoundingBox;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.VAlignment;
import com.simsilica.lemur.component.AbstractGuiComponent;
import com.simsilica.lemur.core.GuiControl;

/**
 * A Lemur GuiComponent that holds a spatial.
 *
 * @author rvandoosselaer
 */
public class SpatialComponent extends AbstractGuiComponent {

    private float xMargin = 0;
    private float yMargin = 0;
    private float zOffset = 0.1f;
    private HAlignment hAlign = HAlignment.Left;
    private VAlignment vAlign = VAlignment.Center;
    private Vector3f offset = null;
    private Spatial spatial;
    private Vector3f spatialSize;

    public SpatialComponent(Spatial spatial) {
        this(spatial, new Vector3f(1, 1, 1));
    }

    public SpatialComponent(Spatial spatial, Vector3f spatialSize) {
        this.spatial = spatial;
        this.spatialSize = spatialSize;

        refreshSpatial();
    }

    @Override
    public void attach(GuiControl parent) {
        super.attach(parent);
        if (spatial != null) {
            getNode().attachChild(spatial);
        }
    }

    @Override
    public void detach(GuiControl parent) {
        if (spatial != null) {
            getNode().detachChild(spatial);
        }
        super.detach(parent);
    }

    @Override
    public void calculatePreferredSize(Vector3f size) {
        // The preferred size depends on the alignment and the size of the spatial
        float width = spatialSize.x;
        float height = spatialSize.y;

        switch (vAlign) {
            case Top:
            case Bottom:
                // Both of these will add to the existing size
                size.y += height;
                break;
            case Center:
                // This will only increase the size if it isn't
                // big enough
                size.y = Math.max(height, size.y);
                break;
        }

        switch (hAlign) {
            case Left:
            case Right:
                // Both of these will add to the existing size
                size.x += width;
                break;
            case Center:
                // This will only increase the size if it isn't
                // big enough
                size.x = Math.max(width, size.x);
                break;
        }

        size.z += Math.abs(zOffset);
    }

    @Override
    public void reshape(Vector3f pos, Vector3f size) {
        float width = spatialSize.x;
        float height = spatialSize.y;

        float boxWidth = width + xMargin * 2;
        float boxHeight = height + yMargin * 2;

        float cx = 0;
        float cy = 0;

        switch (hAlign) {
            case Left:
                cx = pos.x + boxWidth * 0.5f + (width * 0.5f);
                pos.x += boxWidth;
                size.x -= boxWidth;
                break;
            case Right:
                cx = (pos.x + size.x) - (boxWidth * 0.5f) + (width * 0.5f);
                ;
                size.x -= boxWidth;
                break;
            case Center:
                cx = pos.x + size.x * 0.5f + (width * 0.5f);
                break;
        }

        switch (vAlign) {
            case Top:
                cy = pos.y - (boxHeight * 0.5f) + (height * 0.5f);
                pos.y -= boxHeight - (height * 0.5f);
                size.y -= boxHeight;
                break;
            case Bottom:
                cy = (pos.y - size.y) + (boxWidth * 0.5f) + (height * 0.5f);
                pos.y -= boxHeight - (height * 0.5f);
                size.y -= boxHeight;
                break;
            case Center:
                cy = pos.y - (size.y * 0.5f) + (height * 0.5f);
                break;
        }

        spatial.setLocalTranslation(cx - width * 0.5f, cy - height * 0.5f, pos.z);
        if (offset != null) {
            spatial.move(offset);
        }

        pos.z += zOffset;
        size.z -= Math.abs(zOffset);
    }

    public Spatial getSpatial() {
        return spatial;
    }

    public void setSpatial(Spatial spatial) {
        this.spatial = spatial;

        refreshSpatial();
        invalidate();
    }

    public Vector3f getSpatialSize() {
        return spatialSize;
    }

    public void setSpatialSize(Vector3f spatialSize) {
        this.spatialSize = spatialSize;

        refreshSpatial();
        invalidate();
    }

    public void setHAlignment(HAlignment a) {
        if (hAlign == a) {
            return;
        }
        hAlign = a;
        resetAlignment();
    }

    public HAlignment getHAlignment() {
        return hAlign;
    }

    public void setVAlignment(VAlignment a) {
        if (vAlign == a) {
            return;
        }
        vAlign = a;
        resetAlignment();
    }

    public VAlignment getVAlignment() {
        return vAlign;
    }

    public void setMargin(float x, float y) {
        this.xMargin = x;
        this.yMargin = y;

        invalidate();
    }

    public Vector2f getMargin() {
        return new Vector2f(xMargin, yMargin);
    }

    public void setZOffset(float z) {
        this.zOffset = z;
        invalidate();
    }

    public float getZOffset() {
        return zOffset;
    }

    public void setOffset(Vector3f v) {
        this.offset = v;
    }

    public Vector3f getOffset() {
        return offset;
    }

    protected void resetAlignment() {
        invalidate();
    }

    protected void refreshSpatial() {
        Vector3f bounding = ((BoundingBox) spatial.getWorldBound()).getExtent(null).multLocal(2);

        float factorX = 1 / bounding.x;
        float factorY = 1 / bounding.y;
        float factorZ = 1 / bounding.z;

        spatial.scale(spatialSize.x * factorX, spatialSize.y * factorY, spatialSize.z * factorZ);

        // Just in case but it should never happen
        if (isAttached()) {
            getNode().attachChild(spatial);
        }
    }

}