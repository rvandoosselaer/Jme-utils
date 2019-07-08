package org.randomstack.jme.math;

import com.jme3.math.Vector3f;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Slf4j
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class Vector3i implements Serializable, Cloneable {

    @EqualsAndHashCode.Include
    public int x;
    @EqualsAndHashCode.Include
    public int y;
    @EqualsAndHashCode.Include
    public int z;

    /**
     * Set the values of the current vector.
     *
     * @param x value
     * @param y value
     * @param z value
     */
    public void set(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Adds the provided values to this vector, storing the result in a new vector. The new vector is returned.
     *
     * @param x value to add
     * @param y value to add
     * @param z value to add
     * @return the result vector
     */
    public Vector3i add(int x, int y, int z) {
        return new Vector3i(this.x + x, this.y + y, this.z + z);
    }

    /**
     * Adds the provided vector to this vector, storing the result in a new vector. The new vector is returned.
     *
     * @param vec vector to add
     * @return the result vector
     */
    public Vector3i add(Vector3i vec) {
        if (vec == null) {
            log.warn("Supplied vector is null, returning null.");
            return null;
        }

        return new Vector3i(this.x + vec.x, this.y + vec.y, this.z + vec.z);
    }

    /**
     * Adds the provided values to this vector internally and returns a handle to this vector.
     *
     * @param x value to add
     * @param y value to add
     * @param z value to add
     * @return this
     */
    public Vector3i addLocal(int x, int y, int z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    /**
     * Adds the provided vector to this vector internally and returns a handle to this vector.
     *
     * @param vec vector to add
     * @return this
     */
    public Vector3i addLocal(Vector3i vec) {
        if (vec == null) {
            log.warn("Supplied vector is null, returning null.");
            return null;
        }

        this.x += vec.x;
        this.y += vec.y;
        this.z += vec.z;
        return this;
    }

    /**
     * Multiplies the provided values with this vector, storing the result in a new vector. The new vector is returned.
     *
     * @param x value to multiply
     * @param y value to multiply
     * @param z value to multiply
     * @return the result vector
     */
    public Vector3i mult(int x, int y, int z) {
        return new Vector3i(this.x * x, this.y * y, this.z * z);
    }

    /**
     * Multiply the provided vector with this vector, storing the result in a new vector. The new vector is returned.
     *
     * @param vec vector to multiply
     * @return the result vector
     */
    public Vector3i mult(Vector3i vec) {
        if (vec == null) {
            log.warn("Supplied vector is null, returning null.");
            return null;
        }
        return new Vector3i(this.x * vec.x, this.y * vec.y, this.z * vec.z);
    }

    /**
     * Multiply the provided value with this vector, storing the result in a new vector. The new vector is returned.
     *
     * @param value to multiply
     * @return the result vector
     */
    public Vector3i mult(int value) {
        return new Vector3i(this.x * value, this.y * value, this.z * value);
    }

    /**
     * Multiplies the provided values with this vector internally and returns a handle to this vector.
     *
     * @param x value to multiply
     * @param y value to multiply
     * @param z value to multiply
     * @return this
     */
    public Vector3i multLocal(int x, int y, int z) {
        this.x *= x;
        this.y *= y;
        this.z *= z;
        return this;
    }

    /**
     * Multiply the provided vector with this vector internally and returns a handle to this vector.
     *
     * @param vec vector to multiply
     * @return this
     */
    public Vector3i multLocal(Vector3i vec) {
        if (vec == null) {
            log.warn("Supplied vector is null, returning null.");
            return null;
        }

        this.x *= vec.x;
        this.y *= vec.y;
        this.z *= vec.z;
        return this;
    }

    /**
     * Multiply the provided value with this vector internally and returns a handle to this vector.
     *
     * @param value to multiply
     * @return this
     */
    public Vector3i multLocal(int value) {
        this.x *= value;
        this.y *= value;
        this.z *= value;
        return this;
    }

    /**
     * Return the vector as a new Vector3f object.
     *
     * @return vector3f
     */
    public Vector3f toVector3f() {
        return new Vector3f((float) x, (float) y, (float) z);
    }

    @Override
    public Vector3i clone() {
        try {
            return (Vector3i) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Error cloning!");
        }
    }

}
