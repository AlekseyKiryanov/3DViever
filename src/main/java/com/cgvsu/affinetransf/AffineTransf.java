package com.cgvsu.affinetransf;


import com.cgvsu.model.Model;
import com.cgvsu.vectormath.vector.Vector3D;

import javax.vecmath.Matrix4f;
import java.util.ArrayList;

public class AffineTransf {

    //Перечесисление отвечающее за порядок поворотов в каждой из плоскостей
    private OrderRotation orderRotation = OrderRotation.ZYX;
    //Параметры масштабирования
    private float Sx = 1;
    private float Sy = 1;
    private float Sz = 1;
    //Параметры поворота
    //УГЛЫ ПОВОРОТА ЗАДАЮТСЯ ПО ЧАСОВОЙ СРЕЛКЕ В ГРАДУСАХ
    private float Rx = 0;
    private float Ry = 0;
    private float Rz = 0;
    //Параметры переноса
    private float Tx = 0;
    private float Ty = 0;
    private float Tz = 0;

    private Matrix4f R = new Matrix4f(1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1);
    private Matrix4f S;
    private Matrix4f T;
    private Matrix4f A = new Matrix4f(1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1);

    public AffineTransf() {
    }

    public AffineTransf(OrderRotation orderRotation, float sx, float sy, float sz, float rx, float ry, float rz, float tx, float ty, float tz) {
        this.orderRotation = orderRotation;
        Sx = sx;
        Sy = sy;
        Sz = sz;
        Rx = rx;
        Ry = ry;
        Rz = rz;
        Tx = tx;
        Ty = ty;
        Tz = tz;

        calculateA();
    }

    private void calculateA() {
        //Матрица поворота задается единичной
        R = new Matrix4f(1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1);

        //Вычисление матрицы переноса
        T = new Matrix4f(1, 0, 0, Tx,
                0, 1, 0, Ty,
                0, 0, 1, Tz,
                0, 0, 0, 1);
        //Вычисление матрицы масштабирования
        S = new Matrix4f(Sx, 0, 0, 0,
                0, Sy, 0, 0,
                0, 0, Sz, 0,
                0, 0, 0, 1);
        //Вычисление тригонометрических функций
        float sinA = (float) Math.sin(Rx * Math.PI / 180);
        float cosA = (float) Math.cos(Rx * Math.PI / 180);

        float sinB = (float) Math.sin(Ry * Math.PI / 180);
        float cosB = (float) Math.cos(Ry * Math.PI / 180);

        float sinY = (float) Math.sin(Rz * Math.PI / 180);
        float cosY = (float) Math.cos(Rz * Math.PI / 180);

        //Матрицы поворота в каждой из плоскостей
        Matrix4f Z = new Matrix4f(cosY, sinY, 0, 0,
                -sinY, cosY, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1);


        Matrix4f Y = new Matrix4f(cosB, 0, sinB, 0,
                0, 1, 0, 0,
                -sinB, 0, cosB, 0,
                0, 0, 0, 1);

        Matrix4f X = new Matrix4f(1, 0, 0, 0,
                0, cosA, sinA, 0,
                0, -sinA, cosA, 0,
                0, 0, 0, 1);

        //Матрица афинных преобразований принимается равной единице
        A = new Matrix4f(T);

        //Перемножение матриц поворота согласно их порядку
        switch (orderRotation) {
            case ZYX -> {
                R.mul(X);
                R.mul(Y);
                R.mul(Z);
            }
            case ZXY -> {
                R.mul(Y);
                R.mul(X);
                R.mul(Z);
            }
            case YZX -> {
                R.mul(X);
                R.mul(Z);
                R.mul(Y);
            }
            case YXZ -> {
                R.mul(Z);
                R.mul(X);
                R.mul(Y);
            }
            case XZY -> {
                R.mul(Y);
                R.mul(Z);
                R.mul(X);
            }
            case XYZ -> {
                R.mul(Z);
                R.mul(Y);
                R.mul(X);
            }
            default -> R.mul(1);
        }
        //Вычисление матрицы афинных преобразований
        A.mul(R);
        A.mul(S);
    }

    public Vector3D transformVertex(Vector3D v) {
        return VectorMath.mullMatrix4fOnVector3f(A, v);
    }

    public Model transformModel(Model m) {
        Model rez = new Model();
        rez.polygons = new ArrayList<>(m.polygons);
        rez.textureVertices = new ArrayList<>(m.textureVertices);
        //Полигоны и текстурные вершины не изменяются

        rez.vertices = new ArrayList<>();
        for (Vector3D v : m.vertices) {
            rez.vertices.add(transformVertex(v));
        }

        for (Vector3D v : m.normals) {
            rez.normals.add(VectorMath.mullMatrix4fOnVector3f(R,v));
            //На преобразование нормалей влимяет только матрица поворота
        }

        return rez;
    }


    public OrderRotation getOrderRotation() {
        return orderRotation;
    }

    public void setOrderRotation(OrderRotation orderRotation) {
        this.orderRotation = orderRotation;
        calculateA();
    }

    public float getSx() {
        return Sx;
    }

    public void setSx(float sx) {
        Sx = sx;
        calculateA();
    }

    public float getSy() {
        return Sy;
    }

    public void setSy(float sy) {
        Sy = sy;
        calculateA();
    }

    public float getSz() {
        return Sz;
    }

    public void setSz(float sz) {
        Sz = sz;
        calculateA();
    }

    public float getRx() {
        return Rx;
    }

    public void setRx(float rx) {
        Rx = rx;
        calculateA();
    }

    public float getRy() {
        return Ry;
    }

    public void setRy(float ry) {
        Ry = ry;
        calculateA();
    }

    public float getRz() {
        return Rz;
    }

    public void setRz(float rz) {
        Rz = rz;
        calculateA();
    }

    public float getTx() {
        return Tx;
    }

    public void setTx(float tx) {
        Tx = tx;
        calculateA();
    }

    public float getTy() {
        return Ty;
    }

    public void setTy(float ty) {
        Ty = ty;
        calculateA();
    }

    public float getTz() {
        return Tz;
    }

    public void setTz(float tz) {
        Tz = tz;
        calculateA();
    }

    public Matrix4f getR() {
        return R;
    }

    public Matrix4f getS() {
        return S;
    }

    public Matrix4f getT() {
        return T;
    }

    public Matrix4f getA() {
        return A;
    }
}
