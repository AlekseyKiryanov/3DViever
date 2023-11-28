package com.cgvsu.affinetransf;

import com.cgvsu.vectormath.vector.Vector3D;

import javax.vecmath.Matrix4f;

public class VectorMath {
    public static Vector3D mullMatrix4fOnVector3f(Matrix4f m, Vector3D v) {
        return new Vector3D(m.m00 * v.get(0) + m.m01 * v.get(1) + m.m02 * v.get(2) + m.m03,
                m.m10 * v.get(0) + m.m11 * v.get(1) + m.m12 * v.get(2) + m.m13,
                m.m20 * v.get(0) + m.m21 * v.get(1) + m.m22 * v.get(2) + m.m23);
    }
}