package com.cgvsu.render_engine.light;

import com.cgvsu.vectormath.vector.Vector3D;

public abstract class Light {

    protected float diffColor(Vector3D L, Vector3D N){
        float l = L.dotProduct(N);
        l = Math.min(1, Math.max(l, 0));
        float k = 0.4F;
        return (1 - k) + k * l;
    }

    protected float specColor(Vector3D L, Vector3D N, Vector3D V){
        float specPower = 8.0F;
        Vector3D R = N.multiply(2 * V.dotProduct(N)).subtract(V);
        return (float) Math.pow(Float.max(L.dotProduct(R), 0), specPower);
    }

    protected float rimmColor(Vector3D N, Vector3D V){
        float rimPower = 5.0F;
        float biass = 0.3F;
        return (float) Math.pow(1 + biass - Float.max(N.dotProduct(V), 0), rimPower);
    }

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
