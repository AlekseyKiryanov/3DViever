package com.cgvsu.obj_writer;

import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;
import com.cgvsu.vectormath.vector.Vector2D;
import com.cgvsu.vectormath.vector.Vector3D;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

public class ObjWriter {

    public static void write(String fileContent, Model model) {
        Locale.setDefault(Locale.US);

        try (FileWriter fw = new FileWriter(fileContent, false)) {
            for (Vector3D vertex : model.vertices) {
                fw.write(String.format("v %.4f %.4f %.4f%n", vertex.get(0), vertex.get(1), vertex.get(2)));
            }
            fw.write(String.format("# %d vertices%n%n", model.vertices.size()));

            for (Vector2D textureVertex : model.textureVertices) {
                fw.write(String.format("vt %.4f %.4f%n", textureVertex.get(0), textureVertex.get(1)));
            }
            fw.write(String.format("# %d texture coords%n%n", model.textureVertices.size()));

            for (Vector3D normal : model.normals) {
                fw.write(String.format("vn %.4f %.4f %.4f%n", normal.get(0), normal.get(1), normal.get(2)));
            }
            fw.write(String.format("# %d normals%n%n", model.normals.size()));

            int triangles = 0;
            for (Polygon polygon : model.polygons) {
                int k = polygon.getVertexIndices().size();
                if (k == 3) {
                    triangles++;
                }
                fw.append("f");
                if (polygon.getTextureVertexIndices().isEmpty() && polygon.getNormalIndices().isEmpty()) {
                    for (int i = 0; i < k; i++) {
                        fw.write(String.format(" %d", polygon.getVertexIndices().get(i)+1));
                    }

                } else if (!polygon.getTextureVertexIndices().isEmpty() && polygon.getNormalIndices().isEmpty()) {
                    for (int i = 0; i < k; i++) {
                        fw.write(String.format(" %d/%d", polygon.getVertexIndices().get(i)+1, polygon.getTextureVertexIndices().get(i)+1));
                    }
                } else if (polygon.getTextureVertexIndices().isEmpty() && !polygon.getNormalIndices().isEmpty()) {
                    for (int i = 0; i < k; i++) {
                        fw.write(String.format(" %d//%d", polygon.getVertexIndices().get(i)+1, polygon.getNormalIndices().get(i)+1));
                    }
                } else if (!polygon.getTextureVertexIndices().isEmpty() && !polygon.getNormalIndices().isEmpty()) {
                    for (int i = 0; i < k; i++) {
                        fw.write(String.format(" %d/%d/%d", polygon.getVertexIndices().get(i)+1, polygon.getTextureVertexIndices().get(i)+1, polygon.getNormalIndices().get(i)+1));
                    }
                }
                fw.append("\n");
            }
            fw.write(String.format("# %d polygons - %d triangles\n", model.polygons.size()-triangles, triangles));


            fw.flush();
        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }


    }


}
