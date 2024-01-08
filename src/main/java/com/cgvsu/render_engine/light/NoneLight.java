package com.cgvsu.render_engine.light;

import com.cgvsu.vectormath.vector.Vector3D;

public class NoneLight extends Light {
    @Override
    public int setLight(Vector3D light, Vector3D eyes, int defColor, float alpha, float beta, float gama, Vector3D n1, Vector3D n2, Vector3D n3) {
        return defColor;
    }

    @Override
    public void setSpecColor(int color) {

    }

    @Override
    public void setReflectionColor(int color) {

    }
}
