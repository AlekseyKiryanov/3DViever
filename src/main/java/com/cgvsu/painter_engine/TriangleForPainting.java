package com.cgvsu.painter_engine;

import com.cgvsu.vectormath.vector.Vector2D;
import com.cgvsu.vectormath.vector.Vector3D;

public record TriangleForPainting(Vector2D a, Vector2D b, Vector2D c,
                                  float z1, float z2, float z3,
                                  Vector3D n1, Vector3D n2, Vector3D n3) {
}
