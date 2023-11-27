package com.cgvsu.render_engine;
import com.cgvsu.vectormath.matrix.Matrix4x4;
import com.cgvsu.vectormath.vector.Vector3D;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import javax.vecmath.Vector3f;
import javax.vecmath.Matrix4f;

import static com.cgvsu.render_engine.GraphicConveyor.multiplyMatrix4ByVector3;
import static com.cgvsu.render_engine.GraphicConveyor.rotate;

public class Camera {

    public Camera(
            final Vector3D position,
            final Vector3D target,
            final float fov,
            final float aspectRatio,
            final float nearPlane,
            final float farPlane) {
        this.position = position;
        this.target = target;
        this.fov = fov;
        this.aspectRatio = aspectRatio;
        this.nearPlane = nearPlane;
        this.farPlane = farPlane;
    }

    public void setPosition(final Vector3D position) {
        this.position = position;
    }

    public void setTarget(final Vector3D target) {
        this.target = target;
    }

    public void setAspectRatio(final float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public Vector3D getPosition() {
        return position;
    }

    public Vector3D getTarget() {
        return target;
    }

    public void movePosition(final Vector3D translation) {
        this.position.addThis(translation);
    }

    public void moveTarget(final Vector3f translation) {
        this.target.addThis(target);
    }

    Matrix4x4 getViewMatrix() {
        return GraphicConveyor.lookAt(position, target);
    }

    Matrix4x4 getProjectionMatrix() {
        return GraphicConveyor.perspective(fov, aspectRatio, nearPlane, farPlane);
    }

    private double mousePosX;
    private double mousePosY;
    public double mouseDeltaX;
    public double mouseDeltaY;

    public void handleMouseInput(double x, double y, boolean isPrimaryButtonDown, boolean isSecondaryButtonDown) {
        if (isPrimaryButtonDown) {
            // Вращение камеры вокруг объекта при зажатой левой кнопке мыши
            rotateCamera((float) (y - mousePosY), (float) (y - mousePosY));
        } else if (isSecondaryButtonDown) {
            // Передвижение камеры влево/вправо при зажатой правой кнопке мыши
            movePosition(new Vector3D(0, (float)(y - mousePosY) * 0.1f, 0));
        } else {
            // Передвижение камеры в зависимости от движения колесика мыши
            movePosition(new Vector3D(0, 0,(float) mouseDeltaY * 0.05f));
        }

        mousePosX = x;
        mousePosY = y;
    }

    private void rotateCamera(float dx, float dy) {
        float rotationX = -dy * 0.2f;
        float rotationY = -dx * 0.2f;

        Matrix4x4 rotationMatrixX = GraphicConveyor.rotate(rotationX, 1, 0, 0);
        Matrix4x4 rotationMatrixY = GraphicConveyor.rotate(rotationY, 0, 1, 0);

        Matrix4x4 rotationMatrix = rotationMatrixX.multiply(rotationMatrixY);

        position = multiplyMatrix4ByVector3(rotationMatrix, position);
    }
    private void rotateCamera(float dx, float dy, float dz) {
        float rotationX = -dy * 0.2f;
        float rotationY = -dx * 0.2f;
        float rotationZ = -dz * 0.2f;

        Matrix4x4 rotationMatrixX = GraphicConveyor.rotate(rotationX, 1, 0, 0);
        Matrix4x4 rotationMatrixY = GraphicConveyor.rotate(rotationY, 0, 1, 0);
        Matrix4x4 rotationMatrixZ = GraphicConveyor.rotate(rotationZ, 0, 0, 1);

        Matrix4x4 rotationMatrix1 = rotationMatrixX.multiply(rotationMatrixY);
        Matrix4x4 rotationMatrix = rotationMatrix1.multiply(rotationMatrixZ);

        position = multiplyMatrix4ByVector3(rotationMatrix, position);
    }

    private Vector3D position;
    private Vector3D target;
    private float fov;
    private float aspectRatio;
    private float nearPlane;
    private float farPlane;
}