package com.rvandoosselaer.jmeutils.util;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.debug.Arrow;
import com.rvandoosselaer.jmeutils.ApplicationGlobals;

/**
 * Utility class for geometry objects.
 *
 * @author rvandoosselaer
 */
public class GeometryUtils {

    /**
     * Creates a {@link Geometry} of the given color.
     *
     * @param mesh of the geometry
     * @param color of the material
     * @param lit if this geometry requires a light source
     * @return the geometry
     */
    public static Geometry createGeometry(Mesh mesh, ColorRGBA color, boolean lit) {
        Geometry geometry = new Geometry(mesh.toString(), mesh);
        geometry.setMaterial(createMaterial(color, lit));
        if (color.a != 1) {
            geometry.getMaterial().getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
            geometry.setQueueBucket(RenderQueue.Bucket.Transparent);
        }

        return geometry;
    }

    /**
     * Creates a node with 3 arrows that represent the coordinate axes. Where the red arrow represents the X axis,
     * the green arrow represents the Y axis and the blue arrow represents the Z axis.
     *
     * @return the node holding the 3 arrows
     */
    public static Node createCoordinateAxes() {
        Geometry xAxis = createGeometry(new Arrow(Vector3f.UNIT_X), ColorRGBA.Red, false);
        xAxis.setName("X-axis");
        Geometry yAxis = createGeometry(new Arrow(Vector3f.UNIT_Y), ColorRGBA.Green, false);
        xAxis.setName("Y-axis");
        Geometry zAxis = createGeometry(new Arrow(Vector3f.UNIT_Z), ColorRGBA.Blue, false);
        xAxis.setName("Z-axis");

        Node node = new Node("Coordinate axes");
        node.attachChild(xAxis);
        node.attachChild(yAxis);
        node.attachChild(zAxis);

        return node;
    }

    private static Material createMaterial(ColorRGBA color, boolean lit) {
        Material material;
        if (lit) {
            // create a material based on the Lighting definition
            material = new Material(getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
            material.setColor("Diffuse", color);
            material.setBoolean("UseMaterialColors", true);
        } else {
            // create an unshaded material
            material = new Material(getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
            material.setColor("Color", color);
        }

        return material;
    }

    private static AssetManager getAssetManager() {
        ApplicationGlobals appGlobals = ApplicationGlobals.getInstance();
        if (appGlobals == null) {
            throw new RuntimeException("ApplicationGlobals are not initialized!");
        }

        return appGlobals.getApplication().getAssetManager();
    }

}
