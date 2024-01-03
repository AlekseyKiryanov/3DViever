package com.cgvsu.render_engine.rasterization;

import com.cgvsu.vectormath.vector.Vector2D;
import com.cgvsu.vectormath.vector.Vector3D;
import javafx.scene.paint.Color;

public record TriangleNumberPolygon(int number,
                                    Vector2D a, Vector2D b, Vector2D c,
                                    float z1, float z2, float z3,
                                    Vector3D n1, Vector3D n2, Vector3D n3,
                                    Color color) {
}
