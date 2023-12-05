package com.cgvsu.render_engine;
import com.cgvsu.vectormath.matrix.Matrix4x4;
import com.cgvsu.vectormath.vector.Vector2D;
import com.cgvsu.vectormath.vector.Vector3D;

import javax.vecmath.*;

public class GraphicConveyor {

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




        public static Vector2D vertexToPoint(final Vector3D vertex, final int width, final int height) {
        return new Vector2D((float) vertex.get(0) * width + width / 2.0F, (float) -vertex.get(1) * height + height / 2.0F);
    }
}
