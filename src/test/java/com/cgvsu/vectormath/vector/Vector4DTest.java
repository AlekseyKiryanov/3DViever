package com.cgvsu.vectormath.vector;

import static org.junit.jupiter.api.Assertions.*;

import org.testng.annotations.Test;

public class Vector4DTest {

    @Test
    public void testAddition() {
        Vector4D v1 = new Vector4D(1.0f, 2.0f, 3.0f, 4.0f);
        Vector4D v2 = new Vector4D(4.0f, 5.0f, 6.0f, 7.0f);
        Vector4D result = v1.add(v2);
        assertEquals(5.0f, result.get(0), 0.01);
        assertEquals(7.0f, result.get(1), 0.01);
        assertEquals(9.0f, result.get(2), 0.01);
        assertEquals(11.0f, result.get(3), 0.01);
    }

    @Test
    public void testSubtraction() {
        Vector4D v1 = new Vector4D(4.0f, 5.0f, 6.0f, 7.0f);
        Vector4D v2 = new Vector4D(1.0f, 2.0f, 3.0f, 4.0f);
        Vector4D result = v1.subtract(v2);
        assertEquals(3.0f, result.get(0), 0.01);
        assertEquals(3.0f, result.get(1), 0.01);
        assertEquals(3.0f, result.get(2), 0.01);
        assertEquals(3.0f, result.get(3), 0.01);
    }

    @Test
    public void testMultiplication() {
        Vector4D v1 = new Vector4D(2.0f, 3.0f, 4.0f, 5.0f);
        float scalar = 2.0f;
        Vector4D result = v1.multiply(scalar);
        assertEquals(4.0f, result.get(0), 0.01);
        assertEquals(6.0f, result.get(1), 0.01);
        assertEquals(8.0f, result.get(2), 0.01);
        assertEquals(10.0f, result.get(3), 0.01);
    }

    @Test
    public void testDivision() {
        Vector4D v1 = new Vector4D(6.0f, 8.0f, 10.0f, 12.0f);
        float scalar = 2.0f;
        Vector4D result = v1.divide(scalar);
        assertEquals(3.0f, result.get(0), 0.01);
        assertEquals(4.0f, result.get(1), 0.01);
        assertEquals(5.0f, result.get(2), 0.01);
        assertEquals(6.0f, result.get(3), 0.01);
    }

    @Test
    public void testLength() {
        Vector4D v = new Vector4D(3.0f, 4.0f, 5.0f, 6.0f);
        float result = v.length();
        float expected = 9.273618495495704f; // Примерное значение
        assertEquals(expected, result, 0.00001); // Учитывать погрешность
    }

    @Test
    public void testNormalization() {
        Vector4D v = new Vector4D(1.0f, 2.0f, 2.0f, 2.0f);
        Vector4D result = v.normalize();
        float length = result.length();
        assertTrue(Math.abs(length - 1.0f) < 0.00001);
    }

    @Test
    public void testDotProduct() {
        Vector4D v1 = new Vector4D(1.0f, 2.0f, 3.0f, 4.0f);
        Vector4D v2 = new Vector4D(4.0f, 5.0f, 6.0f, 7.0f);
        float result = v1.dotProduct(v2);
        float expected = 60.0f;
        assertEquals(expected, result, 0.00001); // Учитывать погрешность

    }
}
