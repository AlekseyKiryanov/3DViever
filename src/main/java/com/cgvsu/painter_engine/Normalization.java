package com.cgvsu.painter_engine;

import com.cgvsu.model.Model;
import com.cgvsu.vectormath.vector.Vector3D;
import com.google.common.collect.ArrayListMultimap;

import java.util.ArrayList;

public class Normalization {

    private final Model working_model;

    public Normalization(Model working_model) {
        this.working_model = working_model;
    }

    public Model recalceNormales() {
        Model ans_model = new Model();
        ans_model.vertices = new ArrayList<>(working_model.vertices);
        ans_model.textureVertices = new ArrayList<>(working_model.textureVertices);
        ans_model.polygons = new ArrayList<>(working_model.polygons);
        ans_model.normals = new ArrayList<>();

        ArrayListMultimap<Integer, Integer> vertexes_at_polygons = ArrayListMultimap.create();

        int p = working_model.polygons.size();
        for (int i = 0; i < p; i++) {
            int l = working_model.polygons.get(i).getVertexIndices().size();
            ArrayList<Integer> new_normals = new ArrayList<>();
            Vector3D sumNormals = new Vector3D(0, 0, 0);

            for (int j = 0; j < l; j++) {
                Vector3D vec1 = working_model.vertices.get(working_model.polygons.get(i).getVertexIndices().get((j + 1 + l) % l));
                Vector3D vec2 = working_model.vertices.get(working_model.polygons.get(i).getVertexIndices().get((j - 1 + l) % l));
                Vector3D point = working_model.vertices.get(working_model.polygons.get(i).getVertexIndices().get(j));

                Vector3D normal = vec1.subtract(point).crossProduct(vec2.subtract(point)).normalize();

                new_normals.add(ans_model.normals.size());
                ans_model.normals.add(normal);


                sumNormals = sumNormals.add(normal);

                vertexes_at_polygons.put(working_model.polygons.get(i).getVertexIndices().get(j), i);

            }


            ans_model.polygons.get(i).setNormalIndices(new_normals);
            sumNormals = sumNormals.divide(l);
            ans_model.polygons.get(i).setNormal(sumNormals);


        }

        for (int i = 0; i < p; i++) {



            int l = ans_model.polygons.get(i).getVertexIndices().size();
            for (int j = 0; j < l; j++) {

                ArrayList<Integer> other_polygons = new ArrayList<>(vertexes_at_polygons.get(ans_model.polygons.get(i).getVertexIndices().get(j)));
                int m = other_polygons.size();
                Vector3D sumNormals = new Vector3D(0, 0, 0);
                int k = 0;
                for (int n = 0; n < m; n++) {

                    if (ans_model.polygons.get(i).getNormal().dotProduct(ans_model.polygons.get(other_polygons.get(n)).getNormal()) > 0.5) {
                        sumNormals = sumNormals.add(ans_model.polygons.get(other_polygons.get(n)).getNormal());
                        k++;
                    }
                }

                if (k >= 1) {
                    ans_model.normals.set(   ans_model.polygons.get(i).getNormalIndices().get(j) , sumNormals.divide(k).normalize());
                }

            }


        }
        return ans_model;
    }
}
