package com.cgvsu.model;
import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.vectormath.vector.Vector2D;
import com.cgvsu.vectormath.vector.Vector3D;

import java.util.*;

public class Model {

    public ArrayList<Vector3D> vertices = new ArrayList<Vector3D>();
    public ArrayList<Vector2D> textureVertices = new ArrayList<Vector2D>();
    public ArrayList<Vector3D> normals = new ArrayList<Vector3D>();
    public ArrayList<Polygon> polygons = new ArrayList<Polygon>();
}
