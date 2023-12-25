package com.cgvsu.editing_model;

import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;
import com.cgvsu.vectormath.vector.Vector2D;
import com.cgvsu.vectormath.vector.Vector3D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Triangulation {
    private final Model working_model;

    private final double EPS = 0e-5;
    private Model ansModel;

    public Triangulation(Model working_model) {
        this.working_model = working_model;
    }

    public Model triangulate() {
        ansModel = new Model();
        ansModel.vertices = new ArrayList<>(working_model.vertices);
        ansModel.normals = new ArrayList<>(working_model.normals);
        ansModel.textureVertices = new ArrayList<>(working_model.textureVertices);

        int p = working_model.polygons.size();
        for (int i = 0; i < p; i++) {
            if (working_model.polygons.get(i).getVertexIndices().size() == 3) {
                ansModel.polygons.add(working_model.polygons.get(i));
            } else {
                triangulatePolygon(working_model.polygons.get(i));
            }
        }


        return ansModel;
    }

    private void triangulatePolygon(Polygon p) {
        int first_param = -1;
        int second_param = -1;
        int l = p.getVertexIndices().size();

        double delta = 0;
        for (int i = 1; i < l; i++) {
            delta += Math.abs(working_model.vertices.get(p.getVertexIndices().get(i)).get(0) - working_model.vertices.get(p.getVertexIndices().get(i - 1)).get(0));
        }
        if (delta > EPS) {
            first_param = 0;
        }

        delta = 0;
        for (int i = 1; i < l; i++) {
            delta += Math.abs(working_model.vertices.get(p.getVertexIndices().get(i)).get(1) - working_model.vertices.get(p.getVertexIndices().get(i - 1)).get(1));
        }
        if (delta > EPS) {
            if (first_param == -1) {
                first_param = 1;
            } else {
                second_param = 1;
            }
        }

        delta = 0;
        for (int i = 1; i < l; i++) {
            delta += Math.abs(working_model.vertices.get(p.getVertexIndices().get(i)).get(2) - working_model.vertices.get(p.getVertexIndices().get(i - 1)).get(2));
        }
        if (delta > EPS && second_param != 1) {
            second_param = 2;
        }


        ArrayList<Vector2D> points = new ArrayList<>();
        ArrayList<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < l; i++) {
            points.add(new Vector2D(working_model.vertices.get(p.getVertexIndices().get(i)).get(first_param), working_model.vertices.get(p.getVertexIndices().get(i)).get(second_param)));
            indexes.add(i);

        }


        boolean clockwise = isClockwise(points);
        int index = 0;


        while (points.size() > 3) {

            Vector2D p1 = points.get((index) % points.size());
            Vector2D p2 = points.get((index + 1) % points.size());
            Vector2D p3 = points.get((index + 2) % points.size());


            Vector2D v1 = p2.subtract(p1);
            Vector2D v2 = p3.subtract(p1);

            double cross = v1.get(0) * v2.get(1) - v1.get(1) * v2.get(0);


            Triangle triangle = new Triangle(p1, p2, p3);


            if (!clockwise && cross >= 0 && validTriangle(triangle, p1, p2, p3, points)) {
                Polygon rez = new Polygon();
                rez.setNormalIndices(new ArrayList<>(Arrays.asList(p.getNormalIndices().get(indexes.get((index) % points.size())), p.getNormalIndices().get(indexes.get((index + 1) % points.size())), p.getNormalIndices().get(indexes.get((index + 2) % points.size())))));
                rez.setVertexIndices(new ArrayList<>(Arrays.asList(p.getVertexIndices().get(indexes.get((index) % points.size())), p.getVertexIndices().get(indexes.get((index + 1) % points.size())), p.getVertexIndices().get(indexes.get((index + 2) % points.size())))));
                if (!p.getTextureVertexIndices().isEmpty()) {
                    rez.setTextureVertexIndices(new ArrayList<>(Arrays.asList(p.getTextureVertexIndices().get(indexes.get((index) % points.size())), p.getTextureVertexIndices().get(indexes.get((index + 1) % points.size())), p.getTextureVertexIndices().get(indexes.get((index + 2) % points.size())))));
                }


                indexes.remove((index + 1) % points.size());
                points.remove(p2);

                ansModel.polygons.add(rez);


            } else if (clockwise && cross <= 0 && validTriangle(triangle, p1, p2, p3, points)) {
                Polygon rez = new Polygon();
                rez.setNormalIndices(new ArrayList<>(Arrays.asList(p.getNormalIndices().get(indexes.get((index) % points.size())), p.getNormalIndices().get(indexes.get((index + 1) % points.size())), p.getNormalIndices().get(indexes.get((index + 2) % points.size())))));
                rez.setVertexIndices(new ArrayList<>(Arrays.asList(p.getVertexIndices().get(indexes.get((index) % points.size())), p.getVertexIndices().get(indexes.get((index + 1) % points.size())), p.getVertexIndices().get(indexes.get((index + 2) % points.size())))));
                if (!p.getTextureVertexIndices().isEmpty()) {
                    rez.setTextureVertexIndices(new ArrayList<>(Arrays.asList(p.getTextureVertexIndices().get(indexes.get((index) % points.size())), p.getTextureVertexIndices().get(indexes.get((index + 1) % points.size())), p.getTextureVertexIndices().get(indexes.get((index + 2) % points.size())))));
                }


                indexes.remove((index + 1) % points.size());
                points.remove(p2);

                ansModel.polygons.add(rez);

            } else {
                index++;
            }

            if (index > l * l * l * l) {
                break;
                //throw new BadPoligonException("неверный порядок обхода (перекрест ребер)", p.getLine());
            }

        }

        if (points.size() == 3) {


            index = 2;

            Polygon rez = new Polygon();
            rez.setNormalIndices(new ArrayList<>(Arrays.asList(p.getNormalIndices().get(indexes.get((index) % points.size())), p.getNormalIndices().get(indexes.get((index + 1) % points.size())), p.getNormalIndices().get(indexes.get((index + 2) % points.size())))));
            rez.setVertexIndices(new ArrayList<>(Arrays.asList(p.getVertexIndices().get(indexes.get((index) % points.size())), p.getVertexIndices().get(indexes.get((index + 1) % points.size())), p.getVertexIndices().get(indexes.get((index + 2) % points.size())))));
            if (!p.getTextureVertexIndices().isEmpty()) {
                rez.setTextureVertexIndices(new ArrayList<>(Arrays.asList(p.getTextureVertexIndices().get(indexes.get((index) % points.size())), p.getTextureVertexIndices().get(indexes.get((index + 1) % points.size())), p.getTextureVertexIndices().get(indexes.get((index + 2) % points.size())))));
            }

            ansModel.polygons.add(rez);
        }

        points.clear();

    }

    public boolean isClockwise(List<Vector2D> points) {
        double sum = 0;
        for (int i = 0; i < points.size(); i++) {
            Vector2D p1 = points.get(i);
            Vector2D p2 = points.get((i + 1) % points.size());
            sum += (p2.get(0) - p1.get(0)) * (p2.get(1) + p1.get(1));
        }
        return sum >= 0;
    }

    private boolean validTriangle(Triangle triangle, Vector2D p1, Vector2D p2, Vector2D p3, List<Vector2D> points) {
        if (points.size() == 3) return true;
        for (Vector2D p : points) {
            if (!p.equals(p1) && !p.equals(p2) && !p.equals(p3) && triangle.contains(p)) {
                return false;
            }
        }
        return true;
    }

    private class Triangle {
        Vector2D A;
        Vector2D B;
        Vector2D C;

        Triangle(Vector2D a, Vector2D b, Vector2D c) {
            A = a;
            B = b;
            C = c;
        }

        boolean contains(Vector2D P) {

            return Math.abs(this.area() - new Triangle(P, this.A, this.B).area() - new Triangle(P, this.A, this.C).area() - new Triangle(P, this.C, this.B).area()) <= EPS;
        }

        double area() {
            Vector3D a = new Vector3D(A.get(0) - B.get(0), A.get(1) - B.get(1), 0);
            Vector3D b = new Vector3D(C.get(0) - B.get(0), C.get(1) - B.get(1), 0);
            return a.crossProduct(b).length();
        }
    }
}
