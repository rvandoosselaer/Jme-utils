package org.randomstack.jme;

import com.jme3.app.FlyCamAppState;
import com.jme3.app.StatsAppState;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;

/**
 * An application for testing the LightingColorKey material definition. This extension of Lighting.j3md adds the ability
 * to mark parts of a texture and colorize these parts with a new color. The map to mark the parts is specified with the
 * 'KeyMap' texture parameter, the color is specified with the 'KeyColor' parameter.
 * Press 'G' to change a part of the color of the texture.
 *
 * @author remy
 */
public class LightingColorKeyTest extends JmeLauncher implements ActionListener {

    private Material material;

    public static void main(String[] args) {
        LightingColorKeyTest lightingColorKeyTest = new LightingColorKeyTest();
        lightingColorKeyTest.start();
    }

    @Override
    public void init() {
        material = new Material(assetManager, "MatDefs/LightingColorKey.j3md");
        material.setTexture("DiffuseMap", assetManager.loadTexture("texture.png"));
        material.setTexture("KeyMap", assetManager.loadTexture("colorkey-map.png"));
        material.setColor("KeyColor", ColorRGBA.Green);
        material.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);

        Geometry sprite = new Geometry("sprite", new Quad(2, 2));
        sprite.setMaterial(material);
        sprite.move(-1, -1, 0);
        sprite.setQueueBucket(RenderQueue.Bucket.Transparent);

        rootNode.attachChild(sprite);
        rootNode.addLight(new AmbientLight(ColorRGBA.White));
        rootNode.addLight(new DirectionalLight(Vector3f.UNIT_Y.negate(), ColorRGBA.White));

        stateManager.attachAll(new FlyCamAppState(), new StatsAppState());

        inputManager.addMapping("changeColor", new KeyTrigger(KeyInput.KEY_G));
        inputManager.addListener(this, "changeColor");

        viewPort.setBackgroundColor(ColorRGBA.Cyan);
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (!isPressed) {
            material.setColor("KeyColor", ColorRGBA.randomColor());
        }
    }
}
