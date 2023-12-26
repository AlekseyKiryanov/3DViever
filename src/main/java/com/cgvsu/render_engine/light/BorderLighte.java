package com.cgvsu.render_engine.light;

import com.cgvsu.vectormath.vector.Vector3D;
import javafx.scene.paint.Color;

public class BorderLighte implements Lighte {
    @Override
    public Color setLight(Vector3D light, Vector3D defColor, float alpha, float beta, float gama, Vector3D n1, Vector3D n2, Vector3D n3) {

        Vector3D N = new Vector3D(0, 0, 0);
        N.addThis(n1.multiply(  Math.abs(alpha)));
        N.addThis(n2.multiply(Math.abs(beta)));
        N.addThis(n3.multiply(Math.abs(gama)));

        N = N.normalize();
        N.multiplyThis(-1);
        Vector3D L = light.normalize();
        Vector3D V = light.normalize();
        Vector3D R = N.multiply(2 * V.dotProduct(N)).subtract(V);

        Vector3D specColor = new Vector3D(1, 1, 1);


        float specPower = 8.0F;
        specColor.multiplyThis((float) Math.pow(Float.max(L.dotProduct(R), 0), specPower));
        float rimPower = 8.0F;
        float biass = 0.2F;
        Vector3D rimColor = new Vector3D(1, 1, 1);
        rimColor.multiplyThis((float) Math.pow(1 + biass - Float.max(N.dotProduct(V), 0), rimPower));
        specColor.addThis(rimColor);

        N.multiplyThis(-1);
        float k = 0.4F;
        float l = -1 * light.dotProduct(N);
        l = Math.min(1, Math.max(l, 0));
        float m = (1 - k) + k * l;


        return Color.color(Math.min(1, Math.max(specColor.get(0) + defColor.get(0) * m, 0)), Math.min(1, Math.max(specColor.get(1) + defColor.get(1) * m, 0)), Math.min(1, Math.max(specColor.get(2) + defColor.get(2) * m, 0))); //diffColor.get(0), diffColor.get(1), diffColor.get(2));
    }
}
