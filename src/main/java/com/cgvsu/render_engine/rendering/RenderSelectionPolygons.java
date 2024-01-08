package com.cgvsu.render_engine.rendering;

import com.cgvsu.logger.SimpleConsoleLogger;
import com.cgvsu.model.Model;
import com.cgvsu.render_engine.rasterization.Rasterization;
import com.cgvsu.render_engine.rasterization.TriangleVerteces;
import com.cgvsu.render_engine.rasterization.TriangleSelfColored;
import com.cgvsu.vectormath.matrix.Matrix4x4;
import com.cgvsu.vectormath.vector.Vector2D;
import com.cgvsu.vectormath.vector.Vector3D;
import com.cgvsu.vectormath.vector.Vector4D;

import java.util.HashSet;

import static com.cgvsu.vectormath.matrix.Matrix4x4.multiplyMatrix4ByVector3DWithW;
import static com.cgvsu.vectormath.vector.Vector3D.vertexToPoint;

public class RenderSelectionPolygons implements Render {
    private final static SimpleConsoleLogger log = SimpleConsoleLogger.getInstance();
    private final Rasterization painter = Rasterization.getInstance();

    @Override
    public void render(Matrix4x4 modelViewProjectionMatrix, Model mesh, int width, int height, Rasterization painter) {
        HashSet<TriangleVerteces> triangles = new HashSet<>();
        painter.resetPolygonBuffer();

        final int nPolygons = mesh.polygons.size();
        for (int polygonInd = 0; polygonInd < nPolygons; ++polygonInd) {

            Vector3D vertex1 = mesh.vertices.get(mesh.polygons.get(polygonInd).getVertexIndices().get(0));
            Vector4D vertex1M = multiplyMatrix4ByVector3DWithW(modelViewProjectionMatrix, vertex1);

            Vector2D resultPoint1 = vertexToPoint(vertex1M, width, height);
            float z1 = vertex1M.get(2);
            Vector3D normal1 = mesh.normals.get(mesh.polygons.get(polygonInd).getNormalIndices().get(0));


            Vector3D vertex2 = mesh.vertices.get(mesh.polygons.get(polygonInd).getVertexIndices().get(1));
            Vector4D vertex2M = multiplyMatrix4ByVector3DWithW(modelViewProjectionMatrix, vertex2);

            Vector2D resultPoint2 = vertexToPoint(vertex2M, width, height);
            float z2 = vertex2M.get(2);
            Vector3D normal2 = mesh.normals.get(mesh.polygons.get(polygonInd).getNormalIndices().get(1));


            Vector3D vertex3 = mesh.vertices.get(mesh.polygons.get(polygonInd).getVertexIndices().get(2));
            Vector4D vertex3M = multiplyMatrix4ByVector3DWithW(modelViewProjectionMatrix, vertex3);

            Vector2D resultPoint3 = vertexToPoint(vertex3M, width, height);
            float z3 = vertex3M.get(2);
            Vector3D normal3 = mesh.normals.get(mesh.polygons.get(polygonInd).getNormalIndices().get(2));


            if (log.isLoggable(System.Logger.Level.TRACE)) {
                log.log(System.Logger.Level.TRACE, "Treangle " + polygonInd + " A=" + resultPoint1 + " B=" + resultPoint2 + " C=" + resultPoint3);
            }

            if (painter.getChosenPolygons().contains(polygonInd)) {
                painter.paintTriangleSelfColored(new TriangleSelfColored(polygonInd, resultPoint1, resultPoint2, resultPoint3,
                        z1, z2, z3,
                        normal1, normal2, normal3,
                        0xFFFF0000));
            } else {
                painter.paintTriangleSelfColored(new TriangleSelfColored(polygonInd, resultPoint1, resultPoint2, resultPoint3,
                        z1, z2, z3,
                        normal1, normal2, normal3,
                        mesh.getSelfColor()));
            }


            triangles.add(new TriangleVerteces(resultPoint1, resultPoint2, resultPoint3, z1, z2, z3,
                    mesh.polygons.get(polygonInd).getVertexIndices().get(0),
                    mesh.polygons.get(polygonInd).getVertexIndices().get(1),
                    mesh.polygons.get(polygonInd).getVertexIndices().get(2)));


        }


        triangles.forEach(painter::paintPoints);


    }

}
