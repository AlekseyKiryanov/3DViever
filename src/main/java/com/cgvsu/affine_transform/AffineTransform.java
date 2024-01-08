package com.cgvsu.affine_transform;


import com.cgvsu.model.Model;
import com.cgvsu.vectormath.matrix.Matrix4x4;
import com.cgvsu.vectormath.vector.Vector3D;

import java.util.ArrayList;

import static com.cgvsu.vectormath.matrix.Matrix4x4.multMatrix4x4OnVector3D;

public class AffineTransform {

    private RotationOrder rotationOrder = RotationOrder.ZYX;

    private Vector3D translation = new Vector3D(0, 0, 0);
    private Vector3D rotation = new Vector3D(0, 0, 0);
    private Vector3D scale = new Vector3D(1, 1, 1);

    private Matrix4x4 RotationMatrix = new Matrix4x4(1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1);
    private Matrix4x4 ScaleMatrix;
    private Matrix4x4 TranslationMatrix;
    private Matrix4x4 AffineTransformMatrix = new Matrix4x4(1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1);

    public AffineTransform() {
    }

    public AffineTransform(RotationOrder rotationOrder, float sx, float sy, float sz, float rx, float ry, float rz, float tx, float ty, float tz) {
        this.rotationOrder = rotationOrder;
        scale.setAll(sx, sy, sz);
        rotation.setAll(rx, ry, rz);
        translation.setAll(tx, ty, tz);

        calculateAffineTransformMatrix();
    }

    private void calculateAffineTransformMatrix() {
        //Матрица поворота задается единичной
        RotationMatrix = new Matrix4x4(1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1);

        //Вычисление матрицы переноса
        TranslationMatrix = new Matrix4x4(1, 0, 0, translation.get(0),
                0, 1, 0, translation.get(1),
                0, 0, 1, translation.get(2),
                0, 0, 0, 1);
        //Вычисление матрицы масштабирования
        ScaleMatrix = new Matrix4x4(scale.get(0), 0, 0, 0,
                0, scale.get(1), 0, 0,
                0, 0, scale.get(2), 0,
                0, 0, 0, 1);
        //Вычисление тригонометрических функций
        float sinA = (float) Math.sin(rotation.get(0) * Math.PI / 180);
        float cosA = (float) Math.cos(rotation.get(0) * Math.PI / 180);

        float sinB = (float) Math.sin(rotation.get(1) * Math.PI / 180);
        float cosB = (float) Math.cos(rotation.get(1) * Math.PI / 180);

        float sinY = (float) Math.sin(rotation.get(2) * Math.PI / 180);
        float cosY = (float) Math.cos(rotation.get(2) * Math.PI / 180);

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
        AffineTransformMatrix = new Matrix4x4(TranslationMatrix);

        //Перемножение матриц поворота согласно их порядку
        switch (rotationOrder) {
            case ZYX -> {
                RotationMatrix = RotationMatrix.multiply(X);
                RotationMatrix = RotationMatrix.multiply(Y);
                RotationMatrix = RotationMatrix.multiply(Z);
            }
            case ZXY -> {
                RotationMatrix = RotationMatrix.multiply(Y);
                RotationMatrix = RotationMatrix.multiply(X);
                RotationMatrix = RotationMatrix.multiply(Z);
            }
            case YZX -> {
                RotationMatrix = RotationMatrix.multiply(X);
                RotationMatrix = RotationMatrix.multiply(Z);
                RotationMatrix = RotationMatrix.multiply(Y);
            }
            case YXZ -> {
                RotationMatrix = RotationMatrix.multiply(Z);
                RotationMatrix = RotationMatrix.multiply(X);
                RotationMatrix = RotationMatrix.multiply(Y);
            }
            case XZY -> {
                RotationMatrix = RotationMatrix.multiply(Y);
                RotationMatrix = RotationMatrix.multiply(Z);
                RotationMatrix = RotationMatrix.multiply(X);
            }
            case XYZ -> {
                RotationMatrix = RotationMatrix.multiply(Z);
                RotationMatrix = RotationMatrix.multiply(Y);
                RotationMatrix = RotationMatrix.multiply(X);
            }
            default -> RotationMatrix = RotationMatrix.multiply(1);
        }
        //Вычисление матрицы аффинных преобразований
        AffineTransformMatrix = AffineTransformMatrix.multiply(RotationMatrix);
        AffineTransformMatrix = AffineTransformMatrix.multiply(ScaleMatrix);
    }

    public Vector3D transformVertex(Vector3D v) {
        return Matrix4x4.multMatrix4x4OnVector3D(AffineTransformMatrix, v);
    }


    public Model transformModel(Model model) {


        for (int i = 0; i < model.vertices.size(); i++) {
            model.vertices.set(i, transformVertex(model.vertices.get(i)));
        }

        for (int i = 0; i < model.normals.size(); i++) {
            model.normals.set(i, multMatrix4x4OnVector3D(RotationMatrix.multiply(ScaleMatrix), model.normals.get(i)).normalize());
        }

        return model;
    }

    public RotationOrder getRotationOrder() {
        return rotationOrder;
    }

    public void setRotationOrder(RotationOrder rotationOrder) {
        this.rotationOrder = rotationOrder;
        calculateAffineTransformMatrix();
    }

    public float getScaleX() {
        return scale.get(0);
    }

    public void setScaleX(float scaleX) {
        scale.setX(scaleX);
        calculateAffineTransformMatrix();
    }

    public float getScaleY() {
        return scale.get(1);
    }

    public void setScaleY(float scaleY) {
        scale.setY(scaleY);
        calculateAffineTransformMatrix();
    }

    public float getScaleZ() {
        return scale.get(2);
    }

    public void setScaleZ(float scaleZ) {
        scale.setZ(scaleZ);
        calculateAffineTransformMatrix();
    }

    public void setScale(float scaleX, float scaleY, float scaleZ) {
        scale.setAll(scaleX, scaleY, scaleZ);
        calculateAffineTransformMatrix();
    }

    public float getRotationX() {
        return rotation.get(0);
    }

    public void setRotationX(float rotationX) {
        rotation.setX(rotationX);
        calculateAffineTransformMatrix();
    }

    public float getRotationY() {
        return rotation.get(1);
    }

    public void setRotationY(float rotationY) {
        rotation.setY(rotationY);
        calculateAffineTransformMatrix();
    }

    public float getRotationZ() {
        return rotation.get(2);
    }

    public void setRotationZ(float rotationZ) {
        rotation.setZ(rotationZ);
        calculateAffineTransformMatrix();
    }

    public void setRotation(float rotationX, float rotationY, float rotationZ) {
        scale.setAll(rotationX, rotationY, rotationZ);
        calculateAffineTransformMatrix();
    }

    public float getTranslationX() {
        return translation.get(0);
    }

    public void setTranslationX(float translationX) {
        translation.setX(translationX);
        calculateAffineTransformMatrix();
    }

    public float getTranslationY() {
        return translation.get(1);
    }

    public void setTranslationY(float translationY) {
        translation.setY(translationY);
        calculateAffineTransformMatrix();
    }

    public float getTranslationZ() {
        return translation.get(2);
    }

    public void setTranslationZ(float translationZ) {
        translation.setZ(translationZ);
        calculateAffineTransformMatrix();
    }

    public void setTranslation(float translationX, float translationY, float translationZ) {
        scale.setAll(translationX, translationY, translationZ);
        calculateAffineTransformMatrix();
    }

    public Matrix4x4 getRotationMatrix() {
        return RotationMatrix;
    }

    public Matrix4x4 getScaleMatrix() {
        return ScaleMatrix;
    }

    public Matrix4x4 getTranslationMatrix() {
        return TranslationMatrix;
    }

    public Matrix4x4 getAffineTransformMatrix() {
        return AffineTransformMatrix;
    }
}
