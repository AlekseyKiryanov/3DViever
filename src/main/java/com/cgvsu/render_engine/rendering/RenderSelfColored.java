package com.cgvsu.render_engine.rendering;

import com.cgvsu.logger.SimpleConsoleLogger;
import com.cgvsu.model.Model;
import com.cgvsu.render_engine.rasterization.Rasterization;
import com.cgvsu.render_engine.rasterization.TriangleSelfColored;
import com.cgvsu.vectormath.matrix.Matrix4x4;
import com.cgvsu.vectormath.vector.Vector2D;
import com.cgvsu.vectormath.vector.Vector3D;
import com.cgvsu.vectormath.vector.Vector4D;

import static com.cgvsu.vectormath.matrix.Matrix4x4.multiplyMatrix4ByVector3DWithW;
import static com.cgvsu.vectormath.vector.Vector3D.vertexToPoint;

public class RenderSelfColored implements Render {
    private final static SimpleConsoleLogger log = SimpleConsoleLogger.getInstance();

    @Override
    public void render(Matrix4x4 modelViewProjectionMatrix, Model mesh, int width, int height, Rasterization painter) {
        painter.resetPolygonBuffer();

        mesh.polygons.forEach(polygon -> {

            Vector3D vertex1 = mesh.vertices.get(polygon.getVertexIndices().get(0));
            Vector4D vertex1M = multiplyMatrix4ByVector3DWithW(modelViewProjectionMatrix, vertex1);

            Vector2D resultPoint1 = vertexToPoint(vertex1M, width, height);
            float z1 = vertex1M.get(2);
            Vector3D normal1 = mesh.normals.get(polygon.getNormalIndices().get(0));


            Vector3D vertex2 = mesh.vertices.get(polygon.getVertexIndices().get(1));
            Vector4D vertex2M = multiplyMatrix4ByVector3DWithW(modelViewProjectionMatrix, vertex2);

            Vector2D resultPoint2 = vertexToPoint(vertex2M, width, height);
            float z2 = vertex2M.get(2);
            Vector3D normal2 = mesh.normals.get(polygon.getNormalIndices().get(1));


            Vector3D vertex3 = mesh.vertices.get(polygon.getVertexIndices().get(2));
            Vector4D vertex3M = multiplyMatrix4ByVector3DWithW(modelViewProjectionMatrix, vertex3);

            Vector2D resultPoint3 = vertexToPoint(vertex3M, width, height);
            float z3 = vertex3M.get(2);
            Vector3D normal3 = mesh.normals.get(polygon.getNormalIndices().get(2));


            if (log.isLoggable(System.Logger.Level.TRACE)) {
                log.log(System.Logger.Level.TRACE, " A=" + resultPoint1 + " B=" + resultPoint2 + " C=" + resultPoint3);
            }

            painter.paintTriangleSelfColored(new TriangleSelfColored(0, resultPoint1, resultPoint2, resultPoint3,
                    z1, z2, z3,
                    normal1, normal2, normal3,
                    mesh.getSelfColor()));

        });


    }

}
