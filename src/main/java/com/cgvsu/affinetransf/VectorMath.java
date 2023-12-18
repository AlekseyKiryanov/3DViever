package com.cgvsu.affinetransf;

import com.cgvsu.vectormath.matrix.Matrix4x4;
import com.cgvsu.vectormath.vector.Vector3D;

import javax.vecmath.Matrix4f;

public class VectorMath {
    public static Vector3D mullMatrix4x4OnVector3D(Matrix4x4 m, Vector3D v) {
        return new Vector3D(m.getElem(0, 0) * v.get(0) + m.getElem(0, 1) * v.get(1) + m.getElem(0, 2) * v.get(2) + m.getElem(0, 3),
                m.getElem(1, 0) * v.get(0) + m.getElem(1, 1) * v.get(1) + m.getElem(1, 2) * v.get(2) + m.getElem(1, 3),
                m.getElem(2, 0) * v.get(0) + m.getElem(2, 1) * v.get(1) + m.getElem(2, 2) * v.get(2) + m.getElem(2, 3));
    }
}