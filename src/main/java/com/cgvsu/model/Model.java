package com.cgvsu.model;

import com.cgvsu.render_engine.rendering.RenderType;
import com.cgvsu.vectormath.vector.Vector2D;
import com.cgvsu.vectormath.vector.Vector3D;

import java.util.*;

public class Model {

    private String name;

    private int selfColor = 0xFF0041C2;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSelfColor() {
        return selfColor;
    }

    public void setSelfColor(int selfColor) {
        this.selfColor = selfColor;
    }

    private boolean isShowTexture = false;

    public void reverseTexture() {
        if (texture == null || renderType == RenderType.SELECTION_POLYGONS ||
                renderType == RenderType.SELECTION_VERTICES) {
            isShowTexture = false;
        } else {
            this.isShowTexture = !this.isShowTexture;
            setRenderNoMesh();
        }
    }

    public boolean isShowTexture() {
        return isShowTexture;
    }

    private RenderType renderType = RenderType.SELF_COLORED;

    public RenderType getRenderType() {
        return renderType;
    }

    public void setRenderType(RenderType renderType) {
        this.renderType = renderType;
    }

    public void setRenderNoMesh() {
        if (isShowTexture) {
            this.renderType = RenderType.TEXTURE;
        } else {
            this.renderType = RenderType.SELF_COLORED;
        }

    }

    public ArrayList<Vector3D> vertices = new ArrayList<Vector3D>();
    public ArrayList<Vector2D> textureVertices = new ArrayList<Vector2D>();
    public ArrayList<Vector3D> normals = new ArrayList<Vector3D>();
    public ArrayList<Polygon> polygons = new ArrayList<Polygon>();

    private Texture texture = null;

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
        renderType = RenderType.TEXTURE;
        isShowTexture = true;
    }

    public Model copy() {
        Model result = new Model();
        result.name = this.name;
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

    @Override
    public String toString() {
        return name;
    }
}
