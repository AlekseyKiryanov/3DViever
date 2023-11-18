package com.cgvsu.vectormath.vector;

import static org.junit.jupiter.api.Assertions.*;

import org.testng.annotations.Test;

public class Vector3DTest {

    @Test
    public void testAddition() {
        Vector3D v1 = new Vector3D(1.0f, 2.0f, 3.0f);
        Vector3D v2 = new Vector3D(4.0f, 5.0f, 6.0f);
        Vector3D result = v1.add(v2);
        assertEquals(5.0f, result.get(0), 0.01);
        assertEquals(7.0f, result.get(1), 0.01);
        assertEquals(9.0f, result.get(2), 0.01);
    }

    @Test
    public void testSubtraction() {
        Vector3D v1 = new Vector3D(4.0f, 5.0f, 6.0f);
        Vector3D v2 = new Vector3D(1.0f, 2.0f, 3.0f);
        Vector3D result = v1.subtract(v2);
        assertEquals(3.0f, result.get(0), 0.01);
        assertEquals(3.0f, result.get(1), 0.01);
        assertEquals(3.0f, result.get(2), 0.01);
    }

    @Test
    public void testMultiplication() {
        Vector3D v1 = new Vector3D(2.0f, 3.0f, 4.0f);
        float scalar = 2.0f;
        Vector3D result = v1.multiply(scalar);
        assertEquals(4.0f, result.get(0), 0.01);
        assertEquals(6.0f, result.get(1), 0.01);
        assertEquals(8.0f, result.get(2), 0.01);
    }

    @Test
    public void testDivision() {
        Vector3D v1 = new Vector3D(6.0f, 8.0f, 10.0f);
        float scalar = 2.0f;
        Vector3D result = v1.divide(scalar);
        assertEquals(3.0f, result.get(0), 0.01);
        assertEquals(4.0f, result.get(1), 0.01);
        assertEquals(5.0f, result.get(2), 0.01);
    }

    @Test
    public void testLength() {
        Vector3D v = new Vector3D(3.0f, 4.0f, 5.0f);
        float result = v.length();
        float expected = 7.0710678118654755f; // Примерное значение
        assertEquals(expected, result, 0.00001); // Учитывать погрешность
    }

    @Test
    public void testNormalization() {
        Vector3D v = new Vector3D(1.0f, 2.0f, 2.0f);
        Vector3D result = v.normalize();
        float length = result.length();
        assertTrue(Math.abs(length - 1.0f) < 0.00001);
    }

    @Test
    public void testDotProduct() {
        Vector3D v1 = new Vector3D(1.0f, 2.0f, 3.0f);
        Vector3D v2 = new Vector3D(4.0f, 5.0f, 6.0f);
        float result = v1.dotProduct(v2);
        float expected = 32.0f;
        assertEquals(expected, result, 0.00001); // Учитывать погрешность
    }

    @Test
    public void testCrossProduct() {
        Vector3D v1 = new Vector3D(1.0f, 0.0f, 0.0f);
        Vector3D v2 = new Vector3D(0.0f, 1.0f, 0.0f);
        Vector3D result = v1.crossProduct(v2);
        assertEquals(0.0f, result.get(0), 0.01);
        assertEquals(0.0f, result.get(1), 0.01);
        assertEquals(1.0f, result.get(2), 0.01);
    }
}
