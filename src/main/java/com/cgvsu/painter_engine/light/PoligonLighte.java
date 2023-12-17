package com.cgvsu.painter_engine.light;

import com.cgvsu.vectormath.vector.Vector3D;
import javafx.scene.paint.Color;

public class PoligonLighte implements Lighter{
    @Override
    public Color setLight(Vector3D light, Color defColor, float alpha, float beta, float gama, Vector3D n1, Vector3D n2, Vector3D n3) {

        alpha = Math.min(1, Math.max(alpha, 0));
        beta = Math.min(1, Math.max(beta, 0));
        gama = Math.min(1, Math.max(gama, 0));

        Vector3D N = new Vector3D(0, 0, 0);
        N.addThis(n1);
        N.addThis(n2);
        N.addThis(n3);
        N = N.divide(3.0F);

        float k = 0.4F;
        float l = -1 * light.dotProduct(N);
        l = Math.min(1, Math.max(l, 0));
        float m = (1 - k) + k * l;


        return Color.color(defColor.getRed()*m, defColor.getGreen()*m,defColor.getBlue()*m);
    }
}
