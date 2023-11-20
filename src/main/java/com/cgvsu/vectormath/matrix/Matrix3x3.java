package com.cgvsu.vectormath.matrix;

import com.cgvsu.vectormath.vector.Vector3D;

import java.util.Arrays;

public class Matrix3x3{

    private float[][] matrix = new float[3][3];

    public Matrix3x3(float[][] data) {
        if (data.length != 3 || data[0].length != 3) {
            throw new IllegalArgumentException("Матрица должна быть 3x3");
        }
        this.matrix = data;
    }

    public float[][] getMatrix() {
        return matrix;
    }

    public void setElem(int indX, int indY, float value){
        matrix[indX][indY] = value;
    }
    public float getElem(int indX, int indY){
        return matrix[indX][indY];
    }
    // Сложение матриц
    public Matrix3x3 add(Matrix3x3 other) {
        float[][] result = new float[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                result[i][j] = this.matrix[i][j] + other.matrix[i][j];
            }
        }
        return new Matrix3x3(result);
    }

    // Вычитание матриц
    public Matrix3x3 subtract(Matrix3x3 other) {
        float[][] result = new float[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                result[i][j] = this.matrix[i][j] - other.matrix[i][j];
            }
        }
        return new Matrix3x3(result);
    }

    //Умножение на вектор3Д
    public Vector3D multiply(Vector3D vector) {
        if (vector == null) {
            throw new NullPointerException("Вектор не может быть нулевым");
        }
        float[] result = new float[3];
        for (int i = 0; i < 3; i++) {
            result[i] = 0;
            for (int j = 0; j < 3; j++) {
                result[i] += this.matrix[i][j] * vector.get(j);
            }
        }
        return new Vector3D(result[0],result[1], result[2]);
    }

    // Умножение на матрицу
    public Matrix3x3 multiply(Matrix3x3 other) {
        float[][] result = new float[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                result[i][j] = 0;
                for (int k = 0; k < 3; k++) {
                    result[i][j] += this.matrix[i][k] * other.matrix[k][j];
                }
            }
        }
        return new Matrix3x3(result);
    }

    // Транспонирование
    public Matrix3x3 transpose() {
        float[][] result = new float[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                result[i][j] = this.matrix[j][i];
            }
        }
        return new Matrix3x3(result);
    }

    // Задание единичной матрицы
    public static Matrix3x3 identity() {
        float[][] identityMatrix = new float[][]{
                {1, 0, 0},
                {0, 1, 0},
                {0, 0, 1}
        };
        return new Matrix3x3(identityMatrix);
    }

    // Задание нулевой матрицы
    public static Matrix3x3 zero() {
        float[][] zeroMatrix = new float[][]{
                {0, 0, 0},
                {0, 0, 0},
                {0, 0, 0}
        };
        return new Matrix3x3(zeroMatrix);
    }
    public float determinate(){
        return (matrix[0][0]*matrix[1][1]*matrix[2][2]-(matrix[0][2]*matrix[1][1]*matrix[2][0]) +matrix[0][1]*matrix[1][2]*matrix[2][0]-(matrix[0][1]*matrix[1][0]*matrix[2][2]) +matrix[0][2]*matrix[1][0]*matrix[2][1]-(matrix[0][0]*matrix[1][2]*matrix[2][1]));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Matrix3x3 matrix3x3 = (Matrix3x3) o;
        return Arrays.equals(matrix, matrix3x3.matrix);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(matrix);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("[");
        for (float[] floats : matrix) {
            str.append("[");
            for (int j = 0; j < matrix.length; j++) {
                str.append(floats[j]);
                str.append(" ");
            }
            str.append("]");
        }
        str.append("]");
        return str.toString();
        }
}
