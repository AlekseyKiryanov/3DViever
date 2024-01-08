package com.cgvsu.render_engine.light;

import com.cgvsu.vectormath.vector.Vector3D;
import javafx.scene.paint.Color;

public class SpecularLight extends Light {

    private int specColor = 0xFFFFFFFF;

    @Override
    public void setSpecColor(int specColor) {
        this.specColor = specColor;
    }

    @Override
    public void setReflectionColor(int color) {

    }

    @Override
    public int setLight(Vector3D light, Vector3D eyes, int defColor, float alpha, float beta, float gama, Vector3D n1, Vector3D n2, Vector3D n3) {
        Vector3D N = calcNormal(alpha, beta, gama, n1, n2, n3);
        Vector3D L = light.normalize();
        Vector3D V = eyes.normalize();

        float d = diffColor(L, N);
        float s = specColor(L, N, V);

        int b_d = (defColor) & 0xFF;
        int g_d = (defColor >> 8) & 0xFF;
        int r_d = (defColor >> 16) & 0xFF;
        int a_d = defColor & 0xFF000000;

        int b_s = (specColor) & 0xFF;
        int g_s = (specColor >> 8) & 0xFF;
        int r_s = (specColor >> 16) & 0xFF;
        int a_s = specColor & 0xFF000000;

        int b = Integer.min(255, (int) (b_d * d + b_s * s));
        int g = Integer.min(255, (int) (g_d * d + g_s * s));
        int r = Integer.min(255, (int) (r_d * d + r_s * s));

        r = (r << 16) & 0x00FF0000;
        g = (g << 8) & 0x0000FF00;
        b = b & 0x000000FF;

        return a_d | r | g | b;


    }
}
