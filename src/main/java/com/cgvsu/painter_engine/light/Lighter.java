package com.cgvsu.painter_engine.light;

import com.cgvsu.vectormath.vector.Vector3D;
import javafx.scene.paint.Color;

public interface Lighter {
    Color setLight(Vector3D light, Vector3D defColor,
                   float alpha, float beta, float gama,
                   Vector3D n1, Vector3D n2, Vector3D n3);
}
