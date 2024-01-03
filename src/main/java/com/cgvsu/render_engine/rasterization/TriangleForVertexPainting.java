package com.cgvsu.render_engine.rasterization;

import com.cgvsu.vectormath.vector.Vector2D;
import com.cgvsu.vectormath.vector.Vector3D;

public record TriangleForVertexPainting(Vector2D point1, Vector2D point2, Vector2D point3, float z1, float z2, float z3, int number1, int number2, int number3) {
}
