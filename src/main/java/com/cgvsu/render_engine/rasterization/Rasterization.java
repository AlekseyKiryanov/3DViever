package com.cgvsu.render_engine.rasterization;

import com.cgvsu.render_engine.light.Lighte;
import com.cgvsu.vectormath.vector.Vector2D;
import com.cgvsu.vectormath.vector.Vector3D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class Rasterization {


    private final GraphicsContext graphicsContext;
    private final PixelWriter pixelWriter;
    private final int width;
    private final int height;
    private final float[][] zBuffer;
    private final Vector3D coordinateLight;


    private float z1;
    private float z2;
    private float z3;
    private float x3;
    private float y3;
    private float caff1; // (xa-xc)/(xb-xc)
    private float caff2; // ((xa-xc)*(yb-yc)-(ya-yc)*(xb-xc))
    private float caff3;
    private float caff4;
    private float caff5;

    private Vector3D n1;
    private Vector3D n2;
    private Vector3D n3;
    private Vector2D t1;
    private Vector2D t2;
    private Vector2D t3;
    private final Lighte lighte;
    private final Texture texture;


    public Rasterization(Lighte lighte, GraphicsContext graphicsContext, int width, int height, Vector3D coordinateLight, Texture texture) {
        this.texture = texture;
        this.lighte = lighte;
        this.graphicsContext = graphicsContext;
        this.pixelWriter = graphicsContext.getPixelWriter();
        this.width = width;
        this.height = height;
        this.coordinateLight = coordinateLight.normalize();
        zBuffer = new float[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                zBuffer[i][j] = -200;
            }
        }
    }

    private void paintPoint(int x, int y) {
        float alpha = (float) ((x - x3) * caff4 - (y - y3) * caff5) / caff2;


        float beta = (float) (x - x3) / caff3 - alpha * caff1;

        float gama = 1 - alpha - beta;


        float summa = alpha + beta + gama;

        alpha /= summa;
        beta /= summa;
        gama /= summa;




        float z = (float) alpha * z1 + beta * z2 + gama * z3;

        if (x < 0 || y < 0 || x >= width || y >= height || !(z > zBuffer[x][y])) {
            return;
        }
        this.zBuffer[x][y] = z;


        Vector2D t = new Vector2D(0, 0);
        t.addThis(t1.multiply(alpha));
        t.addThis(t2.multiply(beta));
        t.addThis(t3.multiply(gama));


        float  u = t.get(0);
        float v = t.get(1);


        Vector3D C = texture.getColor(u , v);

        Color color = lighte.setLight(coordinateLight, C, alpha,beta,gama, n1, n2, n3);

        pixelWriter.setColor(x, y, color);

    }

    public void paintTriangleTexture(TriangleTextureForPainting triangle) {

        if (triangle.a().get(0) < 0 && triangle.b().get(0) < 0 && triangle.c().get(0) < 0) {
            return;
        }
        if (triangle.a().get(1) < 0 && triangle.b().get(1) < 0 && triangle.c().get(1) < 0) {
            return;
        }
        if (triangle.a().get(0) >= width && triangle.b().get(0) >= width && triangle.c().get(0) >= width) {
            return;
        }
        if (triangle.a().get(1) >= height && triangle.b().get(1) >= height && triangle.c().get(1) >= height) {
            return;
        }

        this.n1 = triangle.n1();
        this.n2 = triangle.n2();
        this.n3 = triangle.n3();


        this.z1 = triangle.z1();
        this.z2 = triangle.z2();
        this.z3 = triangle.z3();

        this.t1 = triangle.t1();
        this.t2 = triangle.t2();
        this.t3 = triangle.t3();

        paintPointsTriangle(Math.round(triangle.a().get(0)), Math.round(triangle.a().get(1)), Math.round(triangle.b().get(0)), Math.round(triangle.b().get(1)), Math.round(triangle.c().get(0)), Math.round(triangle.c().get(1)));

    }

    private void paintPointsTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {


        this.x3 = x3;
        this.y3 = y3;

        boolean flag = false;
        if (x2 == x3) {
            x3++;
            flag = true;
        }
        caff1 = (float) (x1 - x3) / (x2 - x3);
        caff2 = (float) ((x1 - x3) * (y2 - y3) - (y1 - y3) * (x2 - x3));
        caff3 = (float) (x2 - x3);

        caff4 = (float) (y2 - y3);
        caff5 = (float) (x2 - x3);

        if (flag) {
            x3--;
        }


        if (y2 < y1) {
            y1 = y1 ^ y2 ^ (y2 = y1);
            x1 = x1 ^ x2 ^ (x2 = x1);
        }
        if (y3 < y1) {
            y1 = y1 ^ y3 ^ (y3 = y1);
            x1 = x1 ^ x3 ^ (x3 = x1);
        }
        if (y2 > y3) {
            y2 = y2 ^ y3 ^ (y3 = y2);
            x2 = x2 ^ x3 ^ (x3 = x2);
        }


        float dx13 = 0F;
        float dx12 = 0F;
        float dx23 = 0F;
        if (y3 != y1) {
            dx13 = (float) (x3 - x1) / (y3 - y1);
        }
        if (y2 != y1) {
            dx12 = (float) (x2 - x1) / (y2 - y1);
        }
        if (y3 != y2) {
            dx23 = (float) (x3 - x2) / (y3 - y2);
        }
        float wx1 = (float) x1;
        float wx2 = wx1;
        float _dx13 = dx13;
        if (dx13 > dx12) {
            float c = dx12;
            dx12 = dx13;
            dx13 = c;
        }
        for (int i = y1; i < y2; i++) {
            for (int j = Math.round(wx1); j <= Math.round(wx2); j++) {
                paintPoint(j, i);
            }
            wx1 += dx13;
            wx2 += dx12;
        }
        if (y1 == y2) {
            wx1 = (float) Math.min(x1, x2);
            wx2 = (float) Math.max(x1, x2);
        }
        if (_dx13 < dx23) {
            float c = dx23;
            dx23 = _dx13;
            _dx13 = c;
        }

        for (int i = y2; i <= y3; i++) {
            for (int j = Math.round(wx1); j <= Math.round(wx2); j++) {
                paintPoint(j, i);
            }
            wx1 += _dx13;
            wx2 += dx23;
        }


    }
}
