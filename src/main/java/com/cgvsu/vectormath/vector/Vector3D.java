package com.cgvsu.vectormath.vector;

import java.util.Objects;

public class Vector3D {
    private float x;
    private float y;
    private float z;
    private static final float eps = 1e-4f;

    public Vector3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float get(int index) {
        switch (index) {
            case 0:
                return x;
            case 1:
                return y;
            case 2:
                return z;
        }
        throw new IllegalArgumentException("Индекс выходит за границы");
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public void setAll(float valueX, float valueY, float valueZ) {
        this.x = valueX;
        this.y = valueY;
        this.z = valueZ;
    }

    // Сложение векторов
    public Vector3D add(Vector3D other) {
        return new Vector3D(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    // Вычитание векторов
    public Vector3D subtract(Vector3D other) {
        return new Vector3D(this.x - other.x, this.y - other.y, this.z - other.z);

    }

    // Умножение на скаляр
    public Vector3D multiply(float scalar) {
        return new Vector3D(this.x * scalar, this.y * scalar, this.z * scalar);

    }

    // Деление на скаляр
    public Vector3D divide(float scalar) {
        if (Math.abs(scalar) < eps) {
            throw new ArithmeticException("Деление на ноль");
        }
        return new Vector3D(this.x / scalar, this.y / scalar, this.z / scalar);
    }

    // Вычисление длины вектора
    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    // Нормализация вектора
    public Vector3D normalize() {
        float len = length();
        float var1 = (float) (1.0 / Math.sqrt((this.x * this.x + this.y * this.y + this.z * this.z)));
        if (Math.abs(len) < eps) {
            return new Vector3D(0, 0, 0);

        }
        return new Vector3D(x / len, y / len, z / len);
//        return new Vector3D(x * var1, y * var1, z * var1);

    }

    // Скалярное произведение векторов
    public float dotProduct(Vector3D other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    // Векторное произведение векторов
    public Vector3D crossProduct(Vector3D other) {
        return new Vector3D(
                this.y * other.z - this.z * other.y,
                this.z * other.x - this.x * other.z,
                this.x * other.y - this.y * other.x
        );
    }

    // Сложение векторов
    public final void add(Vector3D other1, Vector3D other2) {
        this.x = other1.x + other2.x;
        this.y = other1.y + other2.y;
        this.z = other1.z + other2.z;
    }

    // Вычитание векторов
    public final void subtract(Vector3D other1, Vector3D other2) {
        this.x = other1.x - other2.x;
        this.y = other1.y - other2.y;
        this.z = other1.z - other2.z;
    }

    // Сложение векторов
    public final void addThis(Vector3D other1) {
        this.x += other1.x;
        this.y += other1.y;
        this.z += other1.z;
    }

    // Вычитание векторов
    public final void subtractThis(Vector3D other1) {
        this.x -= other1.x;
        this.y -= other1.y;
        this.z -= other1.z;
    }

    // Умножение на скаляр
    public final void multiplyThis(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
    }

    // Векторное произведение векторов
    public final void crossProduct(Vector3D other1, Vector3D other2) {
        float v1 = other1.y * other2.z - other1.z * other2.y;
        float v2 = other1.z * other2.x - other1.x * other2.z;
        this.z = other1.x * other2.y - other1.y * other2.x;
        this.x = v1;
        this.y = v2;
    }

 /*   public static Vector2D vertexToPoint(final Vector3D vertex, final int width, final int height) {
        return new Vector2D((float) vertex.get(0) * width + width / 2.0F, (float) -vertex.get(1) * height + height / 2.0F);
    }*/

    public static Vector2D vertexToPoint(final Vector4D vertex, final int width, final int height) {
        return new Vector2D((vertex.get(0) + 1) * ((width - 1) / 2.0F),
                (vertex.get(1) - 1) * ((1 - height) / 2.0F));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector3D vector3D = (Vector3D) o;

        if (Math.abs(x - vector3D.x) > eps) return false;
        if (Math.abs(y - vector3D.y) > eps) return false;
        return Math.abs(z - vector3D.z) < eps;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return "Vector3D{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
