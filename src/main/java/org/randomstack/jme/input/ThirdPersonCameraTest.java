package org.randomstack.jme.input;

import com.jme3.app.StatsAppState;
import com.jme3.input.KeyInput;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.input.Button;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputMapper;
import org.randomstack.jme.JmeLauncher;
import org.randomstack.jme.util.GeometryUtils;

/**
 * An example implementation of the {@link ThirdPersonCamera}.
 * key mappings:
 * x : invert mouse x
 * y : invert mouse y
 * i : set isometric-like camera
 * l : set a random target location
 * d : toggle drag to rotate
 */
public class ThirdPersonCameraTest extends JmeLauncher {

    private FunctionId invertY = new FunctionId("invert-y");
    private FunctionId invertX = new FunctionId("invert-x");
    private FunctionId isometric = new FunctionId("isometric");
    private FunctionId randLocation = new FunctionId("randLocation");
    private FunctionId toggleDrag = new FunctionId("toggleDragToRotate");

    private boolean isometricCamera = false;

    public ThirdPersonCameraTest() {
        super(new ThirdPersonCamera()
                        .setDistance(10)
                        .setMinDistance(2)
                        .setMaxDistance(30)
                        .setPitch(30 * FastMath.DEG_TO_RAD)
                        .setMinPitch(-89 * FastMath.DEG_TO_RAD)
                        .setMaxPitch(89 * FastMath.DEG_TO_RAD)
                        .setYaw(-90 * FastMath.DEG_TO_RAD)
                        .setRotationSpeed(5)
                        .setZoomSpeed(10)
                        .setOffset(new Vector3f(0, 1.5f, 0)),
                new StatsAppState());
    }

    public static void main(String[] args) {
        new ThirdPersonCameraTest().start();
    }

    @Override
    public void init() {
        Geometry floor = GeometryUtils.createGeometry(new Quad(32, 32), ColorRGBA.LightGray, false);
        floor.rotate(-FastMath.HALF_PI, 0, 0);
        floor.move(-16, 0, 16);

        Geometry box = GeometryUtils.createGeometry(new Box(0.5f, 0.5f, 0.5f), ColorRGBA.Green, false);
        box.move(0, 0.5f, 0);

        getRootNode().attachChild(floor);
        getRootNode().attachChild(box);

        InputMapper inputMapper = GuiGlobals.getInstance().getInputMapper();

        inputMapper.map(invertY, KeyInput.KEY_Y);
        inputMapper.map(invertX, KeyInput.KEY_X);
        inputMapper.map(isometric, KeyInput.KEY_I);
        inputMapper.map(randLocation, KeyInput.KEY_L);
        inputMapper.map(toggleDrag, KeyInput.KEY_D);
        inputMapper.addDelegate(invertY, this, "invertY");
        inputMapper.addDelegate(invertX, this, "invertX");
        inputMapper.addDelegate(isometric, this, "toggleIsometric");
        inputMapper.addDelegate(randLocation, this, "randomLocation");
        inputMapper.addDelegate(toggleDrag, this, "toggleDrag");

        // remove mappings
        //inputMapper.getMappings(ThirdPersonCamera.FUNCTION_DRAG).forEach(inputMapper::removeMapping);

        // add mapping for dragging with middle mouse
        inputMapper.map(ThirdPersonCamera.FUNCTION_DRAG, Button.MOUSE_BUTTON3);

    }

    public void invertY() {
        getStateManager().getState(ThirdPersonCamera.class).invertY(!getStateManager().getState(ThirdPersonCamera.class).isInvertY()
        );
    }

    public void invertX() {
        getStateManager().getState(ThirdPersonCamera.class).invertX(!getStateManager().getState(ThirdPersonCamera.class).isInvertX()
        );
    }

    public void toggleIsometric() {
        isometricCamera = !isometricCamera;

        ThirdPersonCamera thirdPersonCamera = getStateManager().getState(ThirdPersonCamera.class);
        if (isometricCamera) {
            thirdPersonCamera.setDistance(10);
            thirdPersonCamera.setMinDistance(10);
            thirdPersonCamera.setMaxDistance(10);
            thirdPersonCamera.setPitch(FastMath.DEG_TO_RAD * 60);
            thirdPersonCamera.setMinPitch(FastMath.DEG_TO_RAD * 60);
            thirdPersonCamera.setMaxPitch(FastMath.DEG_TO_RAD * 60);
            thirdPersonCamera.setOffset(Vector3f.ZERO);
        } else {
            thirdPersonCamera.setDistance(10);
            thirdPersonCamera.setMinDistance(1);
            thirdPersonCamera.setMaxDistance(40);
            thirdPersonCamera.setPitch(FastMath.DEG_TO_RAD * 60);
            thirdPersonCamera.setMinPitch(0);
            thirdPersonCamera.setMaxPitch(FastMath.HALF_PI);
            thirdPersonCamera.setOffset(new Vector3f(0, 2, 0));
        }
    }

    public void randomLocation() {
        getStateManager().getState(ThirdPersonCamera.class).setTargetLocation(
                new Vector3f(FastMath.nextRandomInt(0, 10), FastMath.nextRandomInt(0, 10), FastMath.nextRandomInt(0, 10))
        );
    }

    public void toggleDrag() {
        ThirdPersonCamera thirdPersonCamera = getStateManager().getState(ThirdPersonCamera.class);
        thirdPersonCamera.setDragToRotate(!thirdPersonCamera.isDragToRotate());
    }

}

