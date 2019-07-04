package org.randomstack.jme.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Vector3iTest {

    @Test
    public void testAddition() {
        Vector3i v = new Vector3i(0, 0, 0);
        v.addLocal(5, 3, 1);

        Assertions.assertEquals(new Vector3i(5, 3, 1), v);

        v.addLocal(new Vector3i(0, -2, 3));

        Assertions.assertEquals(new Vector3i(5, 1, 4), v);

        v = new Vector3i(1, 1, 1);
        Vector3i result = v.add(2, 3, 4);

        Assertions.assertEquals(new Vector3i(1, 1, 1), v);
        Assertions.assertEquals(new Vector3i(3, 4, 5), result);

        result = v.add(new Vector3i(3, 2, 4));
        Assertions.assertEquals(new Vector3i(1, 1, 1), v);
        Assertions.assertEquals(new Vector3i(4, 3, 5), result);

        Assertions.assertNull(new Vector3i().add(null));
        Assertions.assertNull(new Vector3i().addLocal(null));
    }
}
