package org.randomstack.jme.util;

import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

import java.util.Optional;

/**
 * Utility class for spatial objects
 *
 * @author remy
 */
public class SpatialUtils {

    /**
     * Perform a depthfirst traversal of the spatial to locate the first control of the given type.
     *
     * @param spatial the scene graph to traverse
     * @param type    the class of the control
     * @param <T>     the type of the control
     * @return the first control that matches the given class, or null if no control is found.
     */
    public static <T extends Control> T findFirstControl(Spatial spatial, Class<T> type) {
        if (spatial.getControl(type) != null) {
            return spatial.getControl(type);
        } else {
            if (spatial instanceof Node) {
                for (Spatial s : ((Node) spatial).getChildren()) {
                    T control = findFirstControl(s, type);
                    if (control != null) {
                        return control;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Perform a depthfirst traversal of the spatial to locate the first control of the given type.
     *
     * @param spatial the scene graph to traverse
     * @param type    the class of the control
     * @param <T>     the type of the control
     * @return the first control that matches the given class wrapped in an {@link Optional}, or an empty {@link Optional}
     */
    public static <T extends Control> Optional<T> getFirstControl(Spatial spatial, Class<T> type) {
        return Optional.ofNullable(findFirstControl(spatial, type));
    }

    /**
     * Perform a depthfirst traversal of the spatial, to locate the first geometry.
     *
     * @param spatial : the scene graph to traverse
     * @return the first geometry, or null if no geometry is found.
     */
    public static Geometry findFirstGeometry(Spatial spatial) {
        if (spatial instanceof Geometry) {
            return (Geometry) spatial;
        } else {
            if (spatial instanceof Node) {
                for (Spatial s : ((Node) spatial).getChildren()) {
                    Geometry geometry = findFirstGeometry(s);
                    if (geometry != null) {
                        return geometry;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Perform a depthfirst traversal of the spatial, to locate the first geometry.
     *
     * @param spatial the scene graph to traverse
     * @return the first geometry wrapped in an {@link Optional} or an empty {@link Optional}
     */
    public static Optional<Geometry> getFirstGeometry(Spatial spatial) {
        return Optional.ofNullable(findFirstGeometry(spatial));
    }

    /**
     * Perform a breadthfirst traversal of the spatial and return the spatial structure as a string representation.
     *
     * @param spatial the spatial to traverse
     * @return string representation of the spatial structure
     */
    public static String printTreeStructure(Spatial spatial) {
        return printTreeStructure(spatial, new StringBuilder(), "", "..");
    }

    /**
     * Perform a breadthfirst traversal of the spatial and return the spatial structure as a string representation.
     *
     * @param spatial      the spatial to traverse
     * @param sb           the stringbuilder that gets appended
     * @param indent       the indent to put in front of each new line
     * @param indentString the actual characters that will be used to visualize the indent
     * @return string representation of the spatial structure
     */
    public static String printTreeStructure(Spatial spatial, StringBuilder sb, String indent, String indentString) {
        sb.append(indent).append(spatial).append(System.lineSeparator());
        // variable in lambda should be final... so just do with a for loop
        for (int i = 0; i < spatial.getNumControls(); i++) {
            sb.append(indent).append(indentString).append(spatial.getControl(i).getClass().getSimpleName()).append(System.lineSeparator());
        }
        if (spatial instanceof Node) {
            indent += indentString;
            for (Spatial s : ((Node) spatial).getChildren()) {
                printTreeStructure(s, sb, indent, indentString);
            }
        }
        return sb.toString();
    }

}
