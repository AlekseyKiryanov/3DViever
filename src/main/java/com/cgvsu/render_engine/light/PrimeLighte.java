package com.cgvsu.render_engine.light;

import com.cgvsu.vectormath.vector.Vector3D;
import javafx.scene.paint.Color;

public class PrimeLighte implements Lighte {
    @Override
    public Color setLight(Vector3D light, Vector3D defColor, float alpha, float beta, float gama, Vector3D n1, Vector3D n2, Vector3D n3) {


        Vector3D N = new Vector3D(0, 0, 0);
        N.addThis(n1.multiply(alpha));
        N.addThis(n2.multiply(beta));
        N.addThis(n3.multiply(gama));

        float k = 0.3F;
        float l = -1 * light.dotProduct(N);
        l = Math.min(1, Math.max(l, 0));
        float m = (1 - k) + k * l;


        return Color.color(defColor.get(0) * m, defColor.get(1) * m, defColor.get(2) * m);
    }
}
