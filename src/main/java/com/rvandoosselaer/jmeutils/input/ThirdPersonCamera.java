package com.rvandoosselaer.jmeutils.input;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.input.AnalogFunctionListener;
import com.simsilica.lemur.input.Axis;
import com.simsilica.lemur.input.Button;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputMapper;
import com.simsilica.lemur.input.InputState;
import com.simsilica.lemur.input.StateFunctionListener;

/**
 * A third person camera implementation using Lemur's {@link InputMapper}. This AppState can be used as a starting point
 * for other camera implementations.
 *
 * @author rvandoosselaer
 */
public class ThirdPersonCamera extends BaseAppState implements AnalogFunctionListener, StateFunctionListener {

    public static final String INPUT_GROUP = "thirdPersonCamera.input";
    public static final FunctionId FUNCTION_X_ROTATE = new FunctionId(INPUT_GROUP, "x-rotate");
    public static final FunctionId FUNCTION_Y_ROTATE = new FunctionId(INPUT_GROUP, "y-rotate");
    public static final FunctionId FUNCTION_ZOOM = new FunctionId(INPUT_GROUP, "zoom");
    public static final FunctionId FUNCTION_DRAG = new FunctionId(INPUT_GROUP, "drag");

    private InputMapper.Mapping mouseX;
    private InputMapper.Mapping mouseY;
    private InputMapper.Mapping zoom;
    private InputMapper.Mapping drag;

    private float distance = 20f;
    private float minDistance = 1f;
    private float maxDistance = 40f;
    private float rotationSpeed = 2f;
    private float maximumRotationSpeed = 4f;
    private float zoomSpeed = 1f;
    // rotation on the Y axis
    private float yaw = 0;
    // rotation on the X axis
    private float pitch = 0;
    private float minPitch = 0;
    private float maxPitch = FastMath.HALF_PI;
    private final Vector3f cameraPosition = new Vector3f();
    private boolean dragToRotate = false;

    private InputMapper inputMapper;
    private Camera camera;
    private Vector3f upVector;
    private Vector3f offset = new Vector3f();
    private Vector3f targetLocation = new Vector3f();
    private boolean dragging = false;

    @Override
    protected void initialize(Application app) {
        camera = app.getCamera();
        upVector = camera.getUp(new Vector3f());

        inputMapper = GuiGlobals.getInstance().getInputMapper();

        mouseX = inputMapper.map(FUNCTION_X_ROTATE, Axis.MOUSE_X);
        mouseY = inputMapper.map(FUNCTION_Y_ROTATE, Axis.MOUSE_Y);
        zoom = inputMapper.map(FUNCTION_ZOOM, Axis.MOUSE_WHEEL);
        drag = inputMapper.map(FUNCTION_DRAG, Button.MOUSE_BUTTON1);

        inputMapper.addAnalogListener(this, FUNCTION_X_ROTATE, FUNCTION_Y_ROTATE, FUNCTION_ZOOM);
        inputMapper.addStateListener(this, FUNCTION_DRAG);
    }

    @Override
    protected void cleanup(Application app) {
        inputMapper.removeMapping(mouseX);
        inputMapper.removeMapping(mouseY);
        inputMapper.removeMapping(zoom);
        inputMapper.removeMapping(drag);

        inputMapper.removeAnalogListener(this, FUNCTION_X_ROTATE, FUNCTION_Y_ROTATE, FUNCTION_ZOOM);
        inputMapper.removeStateListener(this, FUNCTION_DRAG);

        // reset the camera position
        camera.setLocation(new Vector3f(0, 0, 10));
        camera.lookAt(Vector3f.ZERO, upVector);
    }

    @Override
    protected void onEnable() {
        inputMapper.activateGroup(INPUT_GROUP);

        if (!dragToRotate) {
            GuiGlobals.getInstance().setCursorEventsEnabled(false);

            // A 'bug' in Lemur causes it to miss turning the cursor off if
            // we are enabled before the MouseAppState is initialized.
            getApplication().getInputManager().setCursorVisible(false);
        }
    }

    @Override
    protected void onDisable() {
        inputMapper.deactivateGroup(INPUT_GROUP);

        if (!dragToRotate) {
            GuiGlobals.getInstance().setCursorEventsEnabled(true);
            getApplication().getInputManager().setCursorVisible(true);
        }
    }

    @Override
    public void valueActive(FunctionId func, double value, double tpf) {
        if (func == FUNCTION_X_ROTATE && (dragging || !dragToRotate)) {
            // for yaw movement we don't clamp the value
            yaw += value * tpf * rotationSpeed;
            if (yaw < 0) {
                yaw += FastMath.TWO_PI;
            }
            if (yaw > FastMath.TWO_PI) {
                yaw -= FastMath.TWO_PI;
            }
        } else if (func == FUNCTION_Y_ROTATE && (dragging || !dragToRotate)) {
            // try to scale the input value, when you make a sudden mouse movement, the value can become very high
            value = value > 0 ? Math.min(value, maximumRotationSpeed) : Math.max(value, -maximumRotationSpeed);
            pitch += -value * tpf * rotationSpeed;
            if (pitch < minPitch) {
                pitch = minPitch;
            }
            if (pitch > maxPitch) {
                pitch = maxPitch;
            }
        } else if (func == FUNCTION_ZOOM) {
            distance += -value * tpf * zoomSpeed;
            if (distance < minDistance) {
                distance = minDistance;
            }
            if (distance > maxDistance) {
                distance = maxDistance;
            }
        }
    }

    @Override
    public void valueChanged(FunctionId func, InputState value, double tpf) {
        if (func == FUNCTION_DRAG && dragToRotate) {
            // update the dragging boolean and set the cursor accordingly
            dragging = value != InputState.Off;
            GuiGlobals.getInstance().setCursorEventsEnabled(!dragging);
        }
    }

    @Override
    public void update(float tpf) {
        Vector3f target = targetLocation.add(offset);

        float hDistance = (distance) * FastMath.sin(FastMath.HALF_PI - pitch);
        cameraPosition.set(hDistance * FastMath.cos(yaw), (distance) * FastMath.sin(pitch), hDistance * FastMath.sin(yaw));

        // add the target position
        cameraPosition.addLocal(target);

        // set camera location and facing
        camera.setLocation(cameraPosition);
        camera.lookAt(target, upVector);
    }

    public void invertY(boolean invert) {
        mouseY.setScale(invert ? -1 : 1);
    }

    public boolean isInvertY() {
        return mouseY.getScale() < 0;
    }

    public void invertX(boolean invert) {
        mouseX.setScale(invert ? -1 : 1);
    }

    public boolean isInvertX() {
        return mouseX.getScale() < 0;
    }

    public float getDistance() {
        return distance;
    }

    public ThirdPersonCamera setDistance(float distance) {
        this.distance = distance;
        return this;
    }

    public float getMinDistance() {
        return minDistance;
    }

    public ThirdPersonCamera setMinDistance(float minDistance) {
        this.minDistance = minDistance;
        return this;
    }

    public float getMaxDistance() {
        return maxDistance;
    }

    public ThirdPersonCamera setMaxDistance(float maxDistance) {
        this.maxDistance = maxDistance;
        return this;
    }

    public float getRotationSpeed() {
        return rotationSpeed;
    }

    public ThirdPersonCamera setRotationSpeed(float rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
        return this;
    }

    public float getZoomSpeed() {
        return zoomSpeed;
    }

    public ThirdPersonCamera setZoomSpeed(float zoomSpeed) {
        this.zoomSpeed = zoomSpeed;
        return this;
    }

    public float getYaw() {
        return yaw;
    }

    public ThirdPersonCamera setYaw(float yaw) {
        this.yaw = yaw;
        return this;
    }

    public float getPitch() {
        return pitch;
    }

    public ThirdPersonCamera setPitch(float pitch) {
        this.pitch = pitch;
        return this;
    }

    public float getMinPitch() {
        return minPitch;
    }

    public ThirdPersonCamera setMinPitch(float minPitch) {
        this.minPitch = minPitch;
        return this;
    }

    public float getMaxPitch() {
        return maxPitch;
    }

    public ThirdPersonCamera setMaxPitch(float maxPitch) {
        this.maxPitch = maxPitch;
        return this;
    }

    public Vector3f getOffset() {
        return offset;
    }

    public ThirdPersonCamera setOffset(Vector3f offset) {
        this.offset = offset;
        return this;
    }

    public Vector3f getTargetLocation() {
        return targetLocation;
    }

    public ThirdPersonCamera setTargetLocation(Vector3f targetLocation) {
        this.targetLocation = targetLocation;
        return this;
    }

    public float getMaximumRotationSpeed() {
        return maximumRotationSpeed;
    }

    public ThirdPersonCamera setMaximumRotationSpeed(float maximumRotationSpeed) {
        this.maximumRotationSpeed = maximumRotationSpeed;
        return this;
    }

    public boolean isDragToRotate() {
        return dragToRotate;
    }

    public ThirdPersonCamera setDragToRotate(boolean dragToRotate) {
        this.dragToRotate = dragToRotate;
        if (isInitialized()) {
            GuiGlobals.getInstance().setCursorEventsEnabled(dragToRotate);
            getApplication().getInputManager().setCursorVisible(dragToRotate);
        }
        return this;
    }

}

