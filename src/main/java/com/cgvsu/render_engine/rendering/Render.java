package com.cgvsu.render_engine.rendering;

import com.cgvsu.model.Model;
import com.cgvsu.render_engine.rasterization.Rasterization;
import com.cgvsu.vectormath.matrix.Matrix4x4;

public interface Render {


    void render (final Matrix4x4 modelViewProjectionMatrix, Model mesh, int width, int height, Rasterization painter);

}
