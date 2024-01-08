package com.cgvsu.render_engine.light;

import com.cgvsu.vectormath.vector.Vector3D;

public class PrimeLight extends Light {
    @Override
    public int setLight(Vector3D light, Vector3D eyes, int defColor, float alpha, float beta, float gama, Vector3D n1, Vector3D n2, Vector3D n3) {

        Vector3D N = calcNormal(alpha, beta, gama, n1, n2, n3);

        light = light.normalize();

        float m = diffColor(light, N);


        int b = (defColor) & 0xFF;
        int g = (defColor >> 8) & 0xFF;
        int r = (defColor >> 16) & 0xFF;
        int a = defColor & 0xFF000000;

        b = (int) (b * m);
        g = (int) (g * m);
        r = (int) (r * m);

        r = (r << 16) & 0x00FF0000;
        g = (g << 8) & 0x0000FF00;
        b = b & 0x000000FF;

        return a | r | g | b;
    }

    @Override
    public void setSpecColor(int color) {

    }

    @Override
    public void setReflectionColor(int color) {

    }
}
