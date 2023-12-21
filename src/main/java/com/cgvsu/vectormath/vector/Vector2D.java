package com.cgvsu.vectormath.vector;

import java.util.Objects;

public class Vector2D{
    private float x;
    private float y;
    private static float eps = 1e-4f;

    public Vector2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float get(int index) {
        switch (index){
            case 0: return x;
            case 1: return y;
        }
    throw new IllegalArgumentException("Индекс выходит за границы");
    }

    // Сложение векторов
    public Vector2D add(Vector2D other) {
        return new Vector2D(this.x + other.x, this.y + other.y);
    }

    public final void addThis(Vector2D other1) {
        this.x += other1.x;
        this.y += other1.y;
    }

    // Вычитание векторов
    public Vector2D subtract(Vector2D other) {
        return new Vector2D(this.x - other.x, this.y - other.y);
    }

    // Умножение на скаляр
    public Vector2D multiply(float scalar) {
        return new Vector2D(this.x * scalar, this.y * scalar);
    }

    // Деление на скаляр
    public Vector2D divide(float scalar) {
        if (Math.abs(scalar) < eps) {
            throw new ArithmeticException("Деление на ноль");
        }
        return new Vector2D(this.x / scalar, this.y / scalar);
    }

    // Вычисление длины вектора
    public float length() {
        return (float) Math.sqrt(x * x + y * y);
    }

    // Нормализация вектора
    public Vector2D normalize() {
        float len = length();
        if (Math.abs(len) < eps) {
            return new Vector2D(0, 0);
        }
        return new Vector2D(x / len, y / len);
    }

    // Скалярное произведение векторов
    public float dotProduct(Vector2D other) {
        return this.x * other.x + this.y * other.y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2D vector2D = (Vector2D) o;
        return Float.compare(vector2D.x, x) == 0 && Float.compare(vector2D.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Vector2D{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
