package com.cgvsu.vectormath.vector;

import static org.junit.jupiter.api.Assertions.*;
import org.testng.annotations.Test;

public class Vector2DTest {

    @Test
    public void testAdd() {
        Vector2D vector1 = new Vector2D(3.0f, 4.0f);
        Vector2D vector2 = new Vector2D(1.0f, 2.0f);
        Vector2D result = vector1.add(vector2);
        assertEquals(4.0f, result.get(0), 0.01);
        assertEquals(6.0f, result.get(1), 0.01);
    }

    @Test
    public void testSubtract() {
        Vector2D vector1 = new Vector2D(3.0f, 4.0f);
        Vector2D vector2 = new Vector2D(1.0f, 2.0f);
        Vector2D result = vector1.subtract(vector2);
        assertEquals(2.0f, result.get(0), 0.01);
        assertEquals(2.0f, result.get(1), 0.01);
    }

    @Test
    public void testMultiply() {
        Vector2D vector = new Vector2D(3.0f, 4.0f);
        Vector2D result = vector.multiply(2.0f);
        assertEquals(6.0f, result.get(0), 0.01);
        assertEquals(8.0f, result.get(1), 0.01);
    }

    @Test
    public void testDivide() {
        Vector2D vector = new Vector2D(6.0f, 8.0f);
        Vector2D result = vector.divide(2.0f);
        assertEquals(3.0f, result.get(0), 0.01);
        assertEquals(4.0f, result.get(1), 0.01);
    }

    @Test
    public void testLength() {
        Vector2D vector = new Vector2D(3.0f, 4.0f);
        float length = vector.length();
        assertEquals(5.0f, length, 0.01);
    }

    @Test
    public void testNormalize() {
        Vector2D vector = new Vector2D(3.0f, 4.0f);
        Vector2D result = vector.normalize();
        assertEquals(0.6f, result.get(0), 0.01);
        assertEquals(0.8f, result.get(1), 0.01);
    }

    @Test
    public void testDotProduct() {
        Vector2D vector1 = new Vector2D(3.0f, 4.0f);
        Vector2D vector2 = new Vector2D(1.0f, 2.0f);
        float dotProduct = vector1.dotProduct(vector2);
        assertEquals(11.0, dotProduct, 0.01);
    }
}
