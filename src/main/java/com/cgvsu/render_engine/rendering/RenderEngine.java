package com.cgvsu.render_engine.rendering;

import com.cgvsu.affine_transform.AffineTransform;
import com.cgvsu.logger.SimpleConsoleLogger;
import com.cgvsu.render_engine.camera.Camera;
import com.cgvsu.render_engine.rasterization.Rasterization;
import com.cgvsu.vectormath.matrix.Matrix4x4;

import com.cgvsu.model.Model;

public class RenderEngine {

    private final static SimpleConsoleLogger log = SimpleConsoleLogger.getInstance();
    private final RenderFactory renderFactory = RenderFactory.getInstance();
    private final Rasterization painter = Rasterization.getInstance();
    private Render render = renderFactory.createRender(RenderType.TEXTURE);

    private Matrix4x4 modelMatrix = new Matrix4x4(1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1);

    public void setModelMatrix(Matrix4x4 modelMatrix) {
        this.modelMatrix = modelMatrix;
    }

    public void setRender(RenderType type) {
        this.render = renderFactory.createRender(type);
    }

    public void render(
            final Camera camera,
            final Model mesh,
            int width, int height) {

        if (log.isLoggable(System.Logger.Level.TRACE)) {
            log.log(System.Logger.Level.TRACE, "==Camera position: " + camera.getPosition() + "==");
        }
        //Matrix4x4 modelMatrix = affineTransform.getAffineTransformMatrix();
        Matrix4x4 viewMatrix = camera.getViewMatrix();
        Matrix4x4 projectionMatrix = camera.getProjectionMatrix();

        Matrix4x4 modelViewProjectionMatrix = new Matrix4x4(modelMatrix.getMatrix());
        modelViewProjectionMatrix = modelViewProjectionMatrix.multiply(viewMatrix);
        modelViewProjectionMatrix = modelViewProjectionMatrix.multiply(projectionMatrix);

        render.render(modelViewProjectionMatrix.transpose(), mesh, width, height, painter);
    }
}