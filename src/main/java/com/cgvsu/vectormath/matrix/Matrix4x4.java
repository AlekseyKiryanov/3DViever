package com.cgvsu.vectormath.matrix;


import com.cgvsu.vectormath.vector.Vector4D;

import java.util.Arrays;

public class Matrix4x4{
    private float[][] matrix = new float[4][4];

    public Matrix4x4(float[][] data) {
        if (data.length != 4 || data[0].length != 4) {
            throw new IllegalArgumentException("Матрица должна быть 4x4");
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
    public Matrix4x4 add(Matrix4x4 other) {
        float[][] result = new float[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result[i][j] = this.matrix[i][j] + other.matrix[i][j];
            }
        }
        return new Matrix4x4(result);
    }

    // Вычитание матриц
    public Matrix4x4 subtract(Matrix4x4 other) {
        float[][] result = new float[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result[i][j] = this.matrix[i][j] - other.matrix[i][j];
            }
        }
        return new Matrix4x4(result);
    }

    // Умножение на вектор4Д
    public Vector4D multiply(Vector4D vector) {
        if (vector == null) {
            throw new NullPointerException("Вектор не может быть нулевым");
        }
        float[] result = new float[4];
        for (int i = 0; i < 4; i++) {
            result[i] = 0;
            for (int j = 0; j < 4; j++) {
                result[i] += this.matrix[i][j] * vector.get(j);
            }
        }
        return new Vector4D(result[0], result[1], result[2], result[3]);
    }

    // Умножение на матрицу
    public Matrix4x4 multiply(Matrix4x4 other) {
        float[][] result = new float[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result[i][j] = 0;
                for (int k = 0; k < 4; k++) {
                    result[i][j] += this.matrix[i][k] * other.matrix[k][j];
                }
            }
        }
        return new Matrix4x4(result);
    }

    // Транспонирование
    public Matrix4x4 transpose() {
        float[][] result = new float[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result[i][j] = this.matrix[j][i];
            }
        }
        return new Matrix4x4(result);
    }

    // Задание единичной матрицы
    public static Matrix4x4 identity() {
        float[][] identityMatrix = new float[][]{
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };
        return new Matrix4x4(identityMatrix);
    }

    // Задание нулевой матрицы
    public static Matrix4x4 zero() {
        float[][] zeroMatrix = new float[][]{
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };
        return new Matrix4x4(zeroMatrix);
    }
    public float determinate(){
        float[][] data1 = new float[3][3];
        float[][] data2 = new float[3][3];
        float[][] data3 = new float[3][3];
        float[][] data4 = new float[3][3];

        for(int i = 1; i < 4; i++){
            for(int j = 0; j < 4; j++){
                if (j != 0){
                    data1[i-1][j-1] = matrix[i][j];
                }
                if (j != 1){
                    if (j==0) {
                        data2[i-1][j] = matrix[i][j];
                    }else{
                        data2[i-1][j-1] = matrix[i][j];
                    }
                }
                if (j != 2){
                    if (j==0 || j==1) {
                        data3[i-1][j] = matrix[i][j];
                    }else{
                        data3[i-1][j-1] = matrix[i][j];
                    }
                }
                if (j != 3){
                    data4[i-1][j] = matrix[i][j];
                }
            }
        }

        Matrix3x3 m1 = new Matrix3x3(data1);
        Matrix3x3 m2 = new Matrix3x3(data2);
        Matrix3x3 m3 = new Matrix3x3(data3);
        Matrix3x3 m4 = new Matrix3x3(data4);

        return (matrix[0][0]*m1.determinate()-matrix[0][1]*m2.determinate()+matrix[0][2]*m3.determinate()-matrix[0][3]*m4.determinate());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Matrix4x4 matrix4x4 = (Matrix4x4) o;
        return Arrays.equals(matrix, matrix4x4.matrix);
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
