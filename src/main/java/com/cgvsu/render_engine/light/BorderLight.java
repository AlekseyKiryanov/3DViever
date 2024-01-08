package com.cgvsu.render_engine.light;

import com.cgvsu.vectormath.vector.Vector3D;
import javafx.scene.paint.Color;

public class BorderLight extends Light {

    private int specColor = 0xFFFFFFFF;

    private int rimmColor = 0xFFFFFFFF;
    @Override
    public int  setLight(Vector3D light, Vector3D eyes, int defColor, float alpha, float beta, float gama, Vector3D n1, Vector3D n2, Vector3D n3) {

        Vector3D N = calcNormal(alpha, beta, gama, n1, n2, n3);
        Vector3D L = light.normalize();
        Vector3D V = eyes.normalize();

        float diff = diffColor(L, N);
        float spec = specColor(L, N, V);
        float rimm = rimmColor(N, V);

        int b_diff = (defColor) & 0xFF;
        int g_diff = (defColor >> 8) & 0xFF;
        int r_diff = (defColor >> 16) & 0xFF;
        int a_diff = defColor & 0xFF000000;

        int b_spec = (specColor) & 0xFF;
        int g_spec = (specColor >> 8) & 0xFF;
        int r_spec = (specColor >> 16) & 0xFF;
        int a_spec = specColor & 0xFF000000;

        int b_rimm = (rimmColor) & 0xFF;
        int g_rimm = (rimmColor >> 8) & 0xFF;
        int r_rimm = (rimmColor >> 16) & 0xFF;
        int a_rimm = rimmColor & 0xFF000000;

        int b = Integer.min(255, (int) (b_diff * diff + b_spec * spec + b_rimm * rimm));
        int g = Integer.min(255, (int) (g_diff * diff + g_spec * spec + g_rimm * rimm));
        int r = Integer.min(255, (int) (r_diff * diff + r_spec * spec + r_rimm * rimm));

        r = (r << 16) & 0x00FF0000;
        g = (g << 8) & 0x0000FF00;
        b = b & 0x000000FF;

        return a_diff | r | g | b;}

    @Override
    public void setSpecColor(int color) {
        this.specColor = color;
    }

    @Override
    public void setReflectionColor(int color) {
        this.rimmColor = color;
    }
}
