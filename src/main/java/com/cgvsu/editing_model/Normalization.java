package com.cgvsu.editing_model;

import com.cgvsu.model.Model;
import com.cgvsu.vectormath.vector.Vector3D;
import com.google.common.collect.ArrayListMultimap;

import java.util.ArrayList;

public class Normalization {

    private final Model workingModel;

    private final float LEVEL_OF_EDGE_SMOOTHING = 0.5F;

    public Normalization(Model workingModel) {
        this.workingModel = workingModel;
    }

    public Model recalculateNormals() {
        Model ansModel = new Model();
        ansModel.vertices = new ArrayList<>(workingModel.vertices);
        ansModel.textureVertices = new ArrayList<>(workingModel.textureVertices);
        ansModel.polygons = new ArrayList<>(workingModel.polygons);
        ansModel.normals = new ArrayList<>();

        ArrayListMultimap<Integer, Integer> vertexes_at_polygons = ArrayListMultimap.create();

        int p = workingModel.polygons.size();
        for (int i = 0; i < p; i++) {
            int l = workingModel.polygons.get(i).getVertexIndices().size();
            ArrayList<Integer> new_normals = new ArrayList<>();
            Vector3D sumNormals = new Vector3D(0, 0, 0);

            for (int j = 0; j < l; j++) {
                Vector3D vec1 = workingModel.vertices.get(workingModel.polygons.get(i).getVertexIndices().get((j + 1 + l) % l));
                Vector3D vec2 = workingModel.vertices.get(workingModel.polygons.get(i).getVertexIndices().get((j - 1 + l) % l));
                Vector3D point = workingModel.vertices.get(workingModel.polygons.get(i).getVertexIndices().get(j));

                Vector3D normal = vec1.subtract(point).crossProduct(vec2.subtract(point)).normalize();

                new_normals.add(ansModel.normals.size());
                ansModel.normals.add(normal);


                sumNormals = sumNormals.add(normal);

                vertexes_at_polygons.put(workingModel.polygons.get(i).getVertexIndices().get(j), i);

            }


            ansModel.polygons.get(i).setNormalIndices(new_normals);
            sumNormals = sumNormals.divide(l);
            ansModel.polygons.get(i).setNormal(sumNormals);


        }

        for (int i = 0; i < p; i++) {



            int l = ansModel.polygons.get(i).getVertexIndices().size();
            for (int j = 0; j < l; j++) {

                ArrayList<Integer> other_polygons = new ArrayList<>(vertexes_at_polygons.get(ansModel.polygons.get(i).getVertexIndices().get(j)));
                int m = other_polygons.size();
                Vector3D sumNormals = new Vector3D(0, 0, 0);
                int k = 0;
                for (int n = 0; n < m; n++) {

                    if (ansModel.polygons.get(i).getNormal().dotProduct(ansModel.polygons.get(other_polygons.get(n)).getNormal()) > LEVEL_OF_EDGE_SMOOTHING) {
                        //Усредняются только те нормали, между которыми угол достаточно острый.
                        sumNormals = sumNormals.add(ansModel.polygons.get(other_polygons.get(n)).getNormal());
                        k++;
                    }
                }

                if (k >= 1) {
                    ansModel.normals.set(   ansModel.polygons.get(i).getNormalIndices().get(j) , sumNormals.divide(k).normalize());
                }

            }


        }
        return ansModel;
    }
}
