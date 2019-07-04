package org.randomstack.jme.math;

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

    public void set(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3i add(int x, int y, int z) {
        return new Vector3i(this.x + x, this.y + y, this.z + z);
    }

    public Vector3i add(Vector3i vec) {
        if (vec == null) {
            log.warn("Supplied vector is null, returning null.");
            return null;
        }

        return new Vector3i(this.x + vec.x, this.y + vec.y, this.z + vec.z);
    }

    public Vector3i addLocal(int x, int y, int z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

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

    @Override
    public Vector3i clone() {
        try {
            return (Vector3i) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Error cloning!");
        }
    }

}
