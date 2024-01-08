package com.cgvsu.editing_model;

import com.cgvsu.model.Model;

import java.util.ArrayList;
import java.util.Stack;

public class Deletion {
    private Deletion() {

    }

    private static Deletion INSTANCE;

    public static Deletion getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Deletion();
        }
        return INSTANCE;
    }

    private Stack<Model> oldModels = new Stack<>();

    public void resetStack() {
        oldModels = new Stack<>();
    }


    public void deleteVertices(Model model, ArrayList<Integer> vertices) {
        oldModels.add(model.copy());

        vertices.sort(Integer::compareTo);
        for (int i = vertices.size() - 1; i >= 0; i--) {
            deleteVertex(model, vertices.get(i));
        }
    }

    private void deleteVertex(Model model, int numberVertex) {
        model.polygons.removeIf(polygon -> polygon.getVertexIndices().contains(numberVertex));
        model.vertices.remove(numberVertex);
        final int nPolygons = model.polygons.size();
        for (int polygonInd = 0; polygonInd < nPolygons; ++polygonInd) {
            for (int i = 0; i < model.polygons.get(polygonInd).getVertexIndices().size(); i++) {
                if (model.polygons.get(polygonInd).getVertexIndices().get(i) > numberVertex) {

                    model.polygons.get(polygonInd).getVertexIndices().set(i, model.polygons.get(polygonInd).getVertexIndices().get(i) - 1);

                }
            }
        }
    }

    public void deletePolygons(Model model, ArrayList<Integer> numberPolygons) {
        oldModels.add(model.copy());
        numberPolygons.sort(Integer::compareTo);

        for (int i = numberPolygons.size() - 1; i >= 0; i--) {
            model.polygons.remove((int) numberPolygons.get(i));
        }
    }

    public Model returnOldModel(Model model) {
        if (oldModels.isEmpty()) {
            return model;
        } else {
            return oldModels.pop();
        }
    }


}
