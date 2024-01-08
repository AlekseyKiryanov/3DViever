package com.cgvsu.render_engine.light;

import com.cgvsu.vectormath.vector.Vector3D;
import javafx.scene.paint.Color;

public abstract class Light {

    protected float k = 0.4F;

    protected static Vector3D calcNormal(float alpha, float beta, float gama, Vector3D n1,Vector3D n2, Vector3D n3){
        Vector3D N = new Vector3D(0, 0, 0);
        N.addThis(n1.multiply(alpha));
        N.addThis(n2.multiply(beta));
        N.addThis(n3.multiply(gama));
        N = N.normalize();
        return N;
    }

    abstract public int setLight(Vector3D light, Vector3D eyes, int defColor,
                          float alpha, float beta, float gama,
                          Vector3D n1, Vector3D n2, Vector3D n3);

    abstract public void setSpecColor(int color);

    abstract public void setReflectionColor(int color);
}
