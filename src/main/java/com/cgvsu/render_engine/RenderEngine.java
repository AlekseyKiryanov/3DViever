package com.cgvsu.render_engine;

import com.cgvsu.logger.SimpleConsoleLogger;
import com.cgvsu.painter_engine.Rasterization;
import com.cgvsu.painter_engine.TriangleForPainting;
import com.cgvsu.painter_engine.TriangleTextureForPainting;
import com.cgvsu.painter_engine.light.Lighter;
import com.cgvsu.vectormath.matrix.Matrix4x4;
import com.cgvsu.vectormath.vector.Vector2D;
import com.cgvsu.vectormath.vector.Vector3D;
import javafx.scene.canvas.GraphicsContext;

import com.cgvsu.model.Model;
import javafx.scene.paint.Color;

import static com.cgvsu.render_engine.GraphicConveyor.*;
import static com.cgvsu.vectormath.matrix.Matrix4x4.multiplyMatrix4ByVector3;
import static com.cgvsu.vectormath.matrix.Matrix4x4.rotateScaleTranslate;

public class RenderEngine {

    private final static SimpleConsoleLogger log = SimpleConsoleLogger.getInstance();


    //private ProtoCurvePainter painter;

    public static void render(
            final Lighter lighte,
            final GraphicsContext graphicsContext,
            final Camera camera,
            final Model mesh,
            final int width,
            final int height) {


        if (log.isLoggable(System.Logger.Level.TRACE)) {
            log.log(System.Logger.Level.TRACE, "==Camera position: " + camera.getPosition() + "==");
        }
        Rasterization painter = new Rasterization(lighte, graphicsContext, width, height, camera.getPosition());
        Matrix4x4 modelMatrix = rotateScaleTranslate();
        Matrix4x4 viewMatrix = camera.getViewMatrix();
        Matrix4x4 projectionMatrix = camera.getProjectionMatrix();

        Matrix4x4 modelViewProjectionMatrix = new Matrix4x4(modelMatrix.getMatrix());
        modelViewProjectionMatrix = modelViewProjectionMatrix.multiply(viewMatrix);
        modelViewProjectionMatrix = modelViewProjectionMatrix.multiply(projectionMatrix);

        final int nPolygons = mesh.polygons.size();
        for (int polygonInd = 0; polygonInd < nPolygons; ++polygonInd) {


            Vector3D vertex1 = mesh.vertices.get(mesh.polygons.get(polygonInd).getVertexIndices().get(0));
            Vector2D texture1;
            try {
                texture1 = mesh.textureVertices.get(mesh.polygons.get(polygonInd).getTextureVertexIndices().get(0));
            } catch (Exception e) {
                texture1 = new Vector2D(Float.MAX_VALUE, Float.MAX_VALUE);
            }
            Vector2D resultPoint1 = vertexToPoint(multiplyMatrix4ByVector3(modelViewProjectionMatrix, vertex1), width, height);


            Vector3D vertex2 = mesh.vertices.get(mesh.polygons.get(polygonInd).getVertexIndices().get(1));
            Vector2D texture2;
            try {
                texture2 = mesh.textureVertices.get(mesh.polygons.get(polygonInd).getTextureVertexIndices().get(1));
            } catch (Exception e) {
                texture2 = new Vector2D(Float.MAX_VALUE, Float.MAX_VALUE);
            }
            Vector2D resultPoint2 = vertexToPoint(multiplyMatrix4ByVector3(modelViewProjectionMatrix, vertex2), width, height);


            Vector3D vertex3 = mesh.vertices.get(mesh.polygons.get(polygonInd).getVertexIndices().get(2));
            Vector2D texture3;
            try {
                texture3 = mesh.textureVertices.get(mesh.polygons.get(polygonInd).getTextureVertexIndices().get(2));
            } catch (Exception e) {
                texture3 = new Vector2D(Float.MAX_VALUE, Float.MAX_VALUE);
            }
            Vector2D resultPoint3 = vertexToPoint(multiplyMatrix4ByVector3(modelViewProjectionMatrix, vertex3), width, height);

            if (log.isLoggable(System.Logger.Level.TRACE)) {
                log.log(System.Logger.Level.TRACE, "Treangle " + polygonInd + " A=" + resultPoint1 + " B=" + resultPoint2 + " C=" + resultPoint3);
            }

            if (texture1 != null && texture2 != null && texture3 != null) {
                painter.paintTriangleTexture(new TriangleTextureForPainting(resultPoint1, resultPoint2, resultPoint3,
                        multiplyMatrix4ByVector3(modelViewProjectionMatrix, vertex1).get(2) + 3, multiplyMatrix4ByVector3(modelViewProjectionMatrix, vertex2).get(2) + 3, multiplyMatrix4ByVector3(modelViewProjectionMatrix, vertex3).get(2) + 3,
                        mesh.normals.get(mesh.polygons.get(polygonInd).getNormalIndices().get(0)), mesh.normals.get(mesh.polygons.get(polygonInd).getNormalIndices().get(1)), mesh.normals.get(mesh.polygons.get(polygonInd).getNormalIndices().get(2)),
                        texture1, texture2, texture3));

            } else {


                painter.paintTriangle(new TriangleForPainting(resultPoint1, resultPoint2, resultPoint3,
                        multiplyMatrix4ByVector3(modelViewProjectionMatrix, vertex1).get(2) + 3, multiplyMatrix4ByVector3(modelViewProjectionMatrix, vertex2).get(2) + 3, multiplyMatrix4ByVector3(modelViewProjectionMatrix, vertex3).get(2) + 3,
                        mesh.normals.get(mesh.polygons.get(polygonInd).getNormalIndices().get(0)), mesh.normals.get(mesh.polygons.get(polygonInd).getNormalIndices().get(1)), mesh.normals.get(mesh.polygons.get(polygonInd).getNormalIndices().get(2))));

            }
        }
    }
}