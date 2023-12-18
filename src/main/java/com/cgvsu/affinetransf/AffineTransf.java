package com.cgvsu.affinetransf;
//Аффинные преобразования. В программе реализована только часть графического конвейера. Нет перегонки из локальных координат в мировые координаты сцены. Вам нужно реализовать её, то есть добавить аффинные преобразования: масштабирование, вращение, перенос. Можете использовать наработки
//        студентов из предыдущей задачи. И не забудьте про тесты, без них визуально
//        может быть сложно отследить баги.
//        4. Трансформация модели. После реализации всего конвейера, нужно добавить в
//        меню настройку модели. Необходима возможность масштабировать ее вдоль
//        каждой из осей, вокруг каждой из осей поворачивать и перемещать. При сохранении модели (см. работу другого студента) следует выбирать, учитывать
//        трансформации модели или нет. То есть нужна возможность сохранить как
//        исходную модель, так и модель после преобразований. Посоветуйтесь с человеком, отвечающим за интерфейс, он может выделить вам место под нужные
//        кнопки.

import com.cgvsu.model.Model;
import com.cgvsu.vectormath.matrix.Matrix4x4;
import com.cgvsu.vectormath.vector.Vector3D;

import java.util.ArrayList;

public class AffineTransf {

    //Перечисление отвечающее за порядок поворотов в каждой из плоскостей
    private OrderRotation or = OrderRotation.ZYX;
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

    private Matrix4x4 R = new Matrix4x4(1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1);
    private Matrix4x4 S;
    private Matrix4x4 T;
    private Matrix4x4 A = new Matrix4x4(1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1);

    public AffineTransf() {
    }

    public AffineTransf(OrderRotation or, float sx, float sy, float sz, float rx, float ry, float rz, float tx, float ty, float tz) {
        this.or = or;
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
        R = new Matrix4x4(1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1);

        //Вычисление матрицы переноса
        T = new Matrix4x4(1, 0, 0, Tx,
                0, 1, 0, Ty,
                0, 0, 1, Tz,
                0, 0, 0, 1);
        //Вычисление матрицы масштабирования
        S = new Matrix4x4(Sx, 0, 0, 0,
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
        Matrix4x4 Z = new Matrix4x4(cosY, sinY, 0, 0,
                -sinY, cosY, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1);


        Matrix4x4 Y = new Matrix4x4(cosB, 0, sinB, 0,
                0, 1, 0, 0,
                -sinB, 0, cosB, 0,
                0, 0, 0, 1);

        Matrix4x4 X = new Matrix4x4(1, 0, 0, 0,
                0, cosA, sinA, 0,
                0, -sinA, cosA, 0,
                0, 0, 0, 1);

        //Матрица аффинных преобразований принимается равной единице
        A = new Matrix4x4(T);

        //Перемножение матриц поворота согласно их порядку
        switch (or) {
            case ZYX -> {
                R = R.multiply(X);
                R = R.multiply(Y);
                R = R.multiply(Z);
            }
            case ZXY -> {
                R = R.multiply(Y);
                R = R.multiply(X);
                R = R.multiply(Z);
            }
            case YZX -> {
                R = R.multiply(X);
                R = R.multiply(Z);
                R = R.multiply(Y);
            }
            case YXZ -> {
                R = R.multiply(Z);
                R = R.multiply(X);
                R = R.multiply(Y);
            }
            case XZY -> {
                R = R.multiply(Y);
                R = R.multiply(Z);
                R = R.multiply(X);
            }
            case XYZ -> {
                R = R.multiply(Z);
                R = R.multiply(Y);
                R = R.multiply(X);
            }
            default -> R = R.multiply(1);
        }
        //Вычисление матрицы аффинных преобразований
        A = A.multiply(R);
        A = A.multiply(S);
    }

    public Vector3D transformVertex(Vector3D v) {
        return VectorMath.mullMatrix4x4OnVector3D(A, v);
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
            rez.normals.add(VectorMath.mullMatrix4x4OnVector3D(R,v));
            //На преобразование нормалей влияет только матрица поворота
        }

        return rez;
    }


    public OrderRotation getOr() {
        return or;
    }

    public void setOr(OrderRotation or) {
        this.or = or;
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

    public Matrix4x4 getR() {
        return R;
    }

    public Matrix4x4 getS() {
        return S;
    }

    public Matrix4x4 getT() {
        return T;
    }

    public Matrix4x4 getA() {
        return A;
    }
}
