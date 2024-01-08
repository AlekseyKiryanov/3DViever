package com.cgvsu.vectormath.vector;

import java.util.Objects;

public class Vector4D{
    private float x;
    private float y;
    private float z;
    private float w;
    private static final float eps = 1e-4f;

    public Vector4D(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vector3D toVector3D(){
        return new Vector3D(this.x, this.y, this.z);
    }

    public float get(int index) {
        switch (index){
            case 0: return x;
            case 1: return y;
            case 2: return z;
            case 3: return w;
        }
        throw new IllegalArgumentException("Индекс выходит за границы");
    }
    // Сложение векторов
    public Vector4D add(Vector4D other) {
        return new Vector4D(this.x + other.x, this.y + other.y, this.z + other.z, this.w + other.w);
    }

    // Вычитание векторов
    public Vector4D subtract(Vector4D other) {
        return new Vector4D(this.x - other.x, this.y - other.y, this.z - other.z, this.w - other.w);
    }

    // Умножение на скаляр
    public Vector4D multiply(float scalar) {
        return new Vector4D(this.x * scalar, this.y * scalar, this.z * scalar, this.w * scalar);
    }

    // Деление на скаляр
    public Vector4D divide(float scalar) {
        if (Math.abs(scalar) < eps) {
            throw new ArithmeticException("Деление на ноль");
        }
        return new Vector4D(this.x / scalar, this.y / scalar, this.z / scalar, this.w / scalar);
    }

    // Вычисление длины вектора
    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z + w * w);
    }

    // Нормализация вектора
    public Vector4D normalize() {
        float length = length();
        if (Math.abs(length) < eps) {
            return new Vector4D(0, 0, 0, 0);
        }
        return new Vector4D(x / length, y / length, z / length, w / length);
    }

    // Скалярное произведение векторов
    public float dotProduct(Vector4D other) {
        return this.x * other.x + this.y * other.y + this.z * other.z + this.w * other.w;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector4D vector4D = (Vector4D) o;
        return Float.compare(vector4D.x, x) == 0 && Float.compare(vector4D.y, y) == 0 && Float.compare(vector4D.z, z) == 0 && Float.compare(vector4D.w, w) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, w);
    }

    @Override
    public String toString() {
        return "Vector4D{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", w=" + w +
                '}';
    }
}
