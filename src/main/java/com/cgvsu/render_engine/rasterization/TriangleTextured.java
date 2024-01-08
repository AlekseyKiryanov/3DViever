package com.cgvsu.render_engine.rasterization;

import com.cgvsu.vectormath.vector.Vector2D;
import com.cgvsu.vectormath.vector.Vector3D;

public record TriangleTextured(Vector2D a, Vector2D b, Vector2D c,
                               float realZ1, float realZ2, float realZ3,
                               float z1, float z2, float z3,
                               Vector3D n1, Vector3D n2, Vector3D n3,
                               Vector2D t1, Vector2D t2, Vector2D t3) {
}
