package com.cgvsu.model;

import com.cgvsu.render_engine.rasterization.Texture;
import com.cgvsu.vectormath.vector.Vector2D;
import com.cgvsu.vectormath.vector.Vector3D;

import java.util.*;

public class Model {

    public ArrayList<Vector3D> vertices = new ArrayList<Vector3D>();
    public ArrayList<Vector2D> textureVertices = new ArrayList<Vector2D>();
    public ArrayList<Vector3D> normals = new ArrayList<Vector3D>();
    public ArrayList<Polygon> polygons = new ArrayList<Polygon>();
    private Texture texture = new Texture();

    public Texture getTexture() {
        return texture;
    }

    public Model copy() {
        Model result = new Model();
        result.vertices = new ArrayList<>(this.vertices);
        result.textureVertices = new ArrayList<>(this.textureVertices);
        result.normals = new ArrayList<>(this.normals);

        result.polygons = new ArrayList<>();
        final int nPolygons = this.polygons.size();
        for (int polygonInd = 0; polygonInd < nPolygons; ++polygonInd) {
            Polygon polygon = new Polygon();
            polygon.setLine(this.polygons.get(polygonInd).getLine());
            polygon.setNormal(this.polygons.get(polygonInd).getNormal());
            polygon.setVertexIndices(new ArrayList<>(this.polygons.get(polygonInd).getVertexIndices()));
            polygon.setTextureVertexIndices(new ArrayList<>(this.polygons.get(polygonInd).getTextureVertexIndices()));
            polygon.setNormalIndices(new ArrayList<>(this.polygons.get(polygonInd).getNormalIndices()));

            result.polygons.add(polygon);
        }

        result.texture = this.texture;
        return result;
    }
}
