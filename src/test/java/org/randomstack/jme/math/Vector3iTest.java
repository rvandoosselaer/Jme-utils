package org.randomstack.jme.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class Vector3iTest {

    @Test
    public void testAddition() {
        Vector3i v = new Vector3i(0, 0, 0);
        v.addLocal(5, 3, 1);

        assertEquals(new Vector3i(5, 3, 1), v);

        v.addLocal(new Vector3i(0, -2, 3));

        assertEquals(new Vector3i(5, 1, 4), v);

        v = new Vector3i(1, 1, 1);
        Vector3i result = v.add(2, 3, 4);

        assertEquals(new Vector3i(1, 1, 1), v);
        assertEquals(new Vector3i(3, 4, 5), result);

        result = v.add(new Vector3i(3, 2, 4));
        assertEquals(new Vector3i(1, 1, 1), v);
        assertEquals(new Vector3i(4, 3, 5), result);

        assertNull(new Vector3i().add(null));
        assertNull(new Vector3i().addLocal(null));
    }

    @Test
    public void testMult() {
        Vector3i v = new Vector3i(1, 2, 3);
        v.multLocal(3);

        assertEquals(new Vector3i(1 * 3, 2 * 3, 3 * 3), v);

        v = new Vector3i(1, 2, 3);
        v.multLocal(new Vector3i(3, 2, 1));

        assertEquals(new Vector3i(1 * 3, 2 * 2, 3 * 1), v);

        v = new Vector3i(1, 2, 3);
        assertNull(v.multLocal(null));

        v = new Vector3i(1, 2, 3);
        v.multLocal(4, 5, 6);

        assertEquals(new Vector3i(1 * 4, 2 * 5, 3 * 6), v);

        v = new Vector3i(1, 2, 3);
        Vector3i result = v.mult(3);

        assertEquals(new Vector3i(1, 2, 3), v);
        assertEquals(new Vector3i(1 * 3, 2 * 3, 3 * 3), result);

        v = new Vector3i(1, 2, 3);
        result = v.mult(new Vector3i(3, 2, 1));

        assertEquals(new Vector3i(1, 2, 3), v);
        assertEquals(new Vector3i(1 * 3, 2 * 2, 3 * 1), result);

        v = new Vector3i(1, 2, 3);
        result = v.mult(null);

        assertNull(result);

        v = new Vector3i(1, 2, 3);
        result = v.mult(4, 5, 6);

        assertEquals(new Vector3i(1, 2, 3), v);
        assertEquals(new Vector3i(1 * 4, 2 * 5, 3 * 6), result);
    }

}
