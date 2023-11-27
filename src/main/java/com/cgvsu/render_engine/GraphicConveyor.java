package com.cgvsu.render_engine;
import com.cgvsu.vectormath.matrix.Matrix4x4;
import com.cgvsu.vectormath.vector.Vector3D;

import javax.vecmath.*;

public class GraphicConveyor {

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

        public static Vector3D multiplyMatrix4ByVector3(final Matrix4x4 matrix, final Vector3D vertex) {
        final float x = (float) ((vertex.get(0) * matrix.getElem(0,0)) + (vertex.get(1) * matrix.getElem(1, 0)) + (vertex.get(2) * matrix.getElem(2, 0)) + matrix.getElem(3, 0));
        final float y = (float) ((vertex.get(0) * matrix.getElem(0,1)) + (vertex.get(1) * matrix.getElem(1, 1)) + (vertex.get(2) * matrix.getElem(2, 1)) + matrix.getElem(3, 1));
        final float z = (float) ((vertex.get(0) * matrix.getElem(0,2)) + (vertex.get(1) * matrix.getElem(1, 2)) + (vertex.get(2) * matrix.getElem(2, 2)) + matrix.getElem(3, 2));
        final float w = (float) ((vertex.get(0) * matrix.getElem(0,3)) + (vertex.get(1) * matrix.getElem(1, 3)) + (vertex.get(2) * matrix.getElem(2, 3)) + matrix.getElem(3, 3));
        return new Vector3D(x / w, y / w, z / w);
    }



        public static Point2f vertexToPoint(final Vector3D vertex, final int width, final int height) {
        return new Point2f((float) vertex.get(0) * width + width / 2.0F, (float) -vertex.get(1) * height + height / 2.0F);
    }
}
