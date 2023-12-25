package com.cgvsu.vectormath.matrix;


import com.cgvsu.vectormath.vector.Vector3D;
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
    public Matrix4x4(Matrix4x4 matrix4x4) {

        this.matrix = matrix4x4.getMatrix();
    }
    public Matrix4x4(float m11, float m12, float m13, float m14, float m21, float m22, float m23, float m24, float m31, float m32, float m33, float m34, float m41, float m42, float m43, float m44) {
        matrix[0][0] = m11;
        matrix[0][1] = m12;
        matrix[0][2] = m13;
        matrix[0][3] = m14;
        matrix[1][0] = m21;
        matrix[1][1] = m22;
        matrix[1][2] = m23;
        matrix[1][3] = m24;
        matrix[2][0] = m31;
        matrix[2][1] = m32;
        matrix[2][2] = m33;
        matrix[2][3] = m34;
        matrix[3][0] = m41;
        matrix[3][1] = m42;
        matrix[3][2] = m43;
        matrix[3][3] = m44;
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

    public Matrix4x4 multiply(float num) {
        float[][] result = new float[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result[i][j] += this.matrix[i][j] * num;
            }
        }
        return new Matrix4x4(result);
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
    public static Matrix4x4 rotateScaleTranslate() {
        float[][] matrix = {
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}};
        return new Matrix4x4(matrix);
    }
    public static Matrix4x4 rotate(float angle, float axisX, float axisY, float axisZ) {
        float radians = (float) Math.toRadians(angle);
        float sin = (float) Math.sin(radians);
        float cos = (float) Math.cos(radians);
        float oneMinusCos = 1.0f - cos;

        float[][] rotationMatrix = {
                {cos + axisX * axisX * oneMinusCos, axisX * axisY * oneMinusCos - axisZ * sin, axisX * axisZ * oneMinusCos + axisY * sin, 0},
                {axisY * axisX * oneMinusCos + axisZ * sin, cos + axisY * axisY * oneMinusCos, axisY * axisZ * oneMinusCos - axisX * sin, 0},
                {axisZ * axisX * oneMinusCos - axisY * sin, axisZ * axisY * oneMinusCos + axisX * sin, cos + axisZ * axisZ * oneMinusCos, 0},
                {0, 0, 0, 1}
        };

        return new Matrix4x4(rotationMatrix);
    }

    public static Matrix4x4 lookAt(Vector3D eye, Vector3D target) {
        return lookAt(eye, target, new Vector3D(0F, 1.0F, 0F));
    }

    public static Matrix4x4 lookAt(Vector3D eye, Vector3D target, Vector3D up) {
        Vector3D resultX = new Vector3D(0, 0, 0);
        Vector3D resultY = new Vector3D(0, 0, 0);
        Vector3D resultZ = new Vector3D(0, 0, 0);

        resultZ.subtract(target, eye);
        resultX.crossProduct(up, resultZ);
        resultY.crossProduct(resultZ, resultX);

        resultX = resultX.normalize();
        resultY = resultY.normalize();
        resultZ = resultZ.normalize();

        float[][] matrix =
                {
                        {(float) resultX.get(0), (float) resultY.get(0), (float) resultZ.get(0), 0},
                        {(float) resultX.get(1), (float) resultY.get(1), (float) resultZ.get(1), 0},
                        {(float) resultX.get(2), (float) resultY.get(2), (float) resultZ.get(2), 0},
                        {-resultX.dotProduct(eye), -resultY.dotProduct(eye), -resultZ.dotProduct(eye), 1}};
        return new Matrix4x4(matrix);
        // trans
    }

    public static Vector3D multiplyMatrix4ByVector3(final Matrix4x4 matrix, final Vector3D vertex) {
        final float x = (float) ((vertex.get(0) * matrix.getElem(0,0)) + (vertex.get(1) * matrix.getElem(1, 0)) + (vertex.get(2) * matrix.getElem(2, 0)) + matrix.getElem(3, 0));
        final float y = (float) ((vertex.get(0) * matrix.getElem(0,1)) + (vertex.get(1) * matrix.getElem(1, 1)) + (vertex.get(2) * matrix.getElem(2, 1)) + matrix.getElem(3, 1));
        final float z = (float) ((vertex.get(0) * matrix.getElem(0,2)) + (vertex.get(1) * matrix.getElem(1, 2)) + (vertex.get(2) * matrix.getElem(2, 2)) + matrix.getElem(3, 2));
        final float w = (float) ((vertex.get(0) * matrix.getElem(0,3)) + (vertex.get(1) * matrix.getElem(1, 3)) + (vertex.get(2) * matrix.getElem(2, 3)) + matrix.getElem(3, 3));
        return new Vector3D(x / w, y / w, z / w);
    }
    public static Vector3D multMatrix4x4OnVector3D(Matrix4x4 m, Vector3D v) {
        return new Vector3D(m.getElem(0, 0) * v.get(0) + m.getElem(0, 1) * v.get(1) + m.getElem(0, 2) * v.get(2) + m.getElem(0, 3),
                m.getElem(1, 0) * v.get(0) + m.getElem(1, 1) * v.get(1) + m.getElem(1, 2) * v.get(2) + m.getElem(1, 3),
                m.getElem(2, 0) * v.get(0) + m.getElem(2, 1) * v.get(1) + m.getElem(2, 2) * v.get(2) + m.getElem(2, 3));
    }

    public static Matrix4x4 perspective(
            final float fov,
            final float aspectRatio,
            final float nearPlane,
            final float farPlane) {
        float[][] matrix = new float[4][4];

        float tangentMinusOnDegree = (float) (1.0F / (Math.tan(fov * 0.5F)));

        matrix[0][0] = tangentMinusOnDegree / aspectRatio;
        matrix[1][1] = tangentMinusOnDegree;
        matrix[2][2] = (farPlane + nearPlane) / (farPlane - nearPlane);
        matrix[2][3] = 1.0F;
        matrix[3][2] = 2 * (nearPlane * farPlane) / (nearPlane - farPlane);

        return new Matrix4x4(matrix);
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
