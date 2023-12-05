package com.cgvsu.painter_engine;

import com.cgvsu.vectormath.vector.Vector2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class ProtoCurvePainter {


    private final GraphicsContext graphicsContext;
    private final PixelWriter pixelWriter;
    private float caff1; // (xa-xc)/(xb-xc)
    private float caff2; // ((xa-xc)*(yb-yc)-(ya-yc)*(xb-xc))
    private float caff3;
    private float caff4;
    private float caff5;

    private float z1;
    private float z2;
    private float z3;

    private float x1;
    private float x2;
    private float x3;

    private float y1;
    private float y2;
    private float y3;
    private int width;
    private int height;

    private float[][] z_boofer;

    private Color C;

    public ProtoCurvePainter(GraphicsContext graphicsContext, int width, int height) {
        this.graphicsContext = graphicsContext;
        this.pixelWriter = graphicsContext.getPixelWriter();
        this.width = width;
        this.height = height;
        z_boofer = new float[width][height];
        for (int i = 0; i <width; i++){
            for (int j = 0; j <height; j++){
                z_boofer[i][j] = -200;
            }
        }
    }

    public void paintDot(double a, double b, Color color) {

        int x = (int) a;
        int y = (int) b;

        final PixelWriter pixelWriter = graphicsContext.getPixelWriter();
        int dotSize = 3;
        for (int row = y - dotSize / 2 - 1; row < y + dotSize; ++row)
            for (int col = x - dotSize / 2 - 1; col < x + dotSize; ++col)
                pixelWriter.setColor(col, row, color);
    }

    private void paintPoint(int x, int y) {
        float alpha = (float) ((x - x3) * caff4 - (y - y3) * caff5) / caff2;
        //System.out.println((float) ((x-xc)*(yb-yc)-(y-yc)*(xb-xc)) );
        float beta = (float) (x - x3) / caff3 - alpha * caff1;
        float gama = 1 - alpha - beta;
        //System.out.println(alpha + " " + beta + " " + gama);
    //    alpha = Math.min(1, Math.max(alpha, 0));
    //    beta = Math.min(1, Math.max(beta, 0));
    //    gama = Math.min(1, Math.max(gama, 0));

        float z = (float) alpha*z1 + beta*z2 + gama*z3;
        if (z > z_boofer[x][y]){
            //System.out.println(z);
            this.z_boofer[x][y] = z;
            //System.out.println(this.z_boofer[x][y]);
            pixelWriter.setColor(x, y, C);
        }


    }

    public void paintTriangle(Vector2D a, Vector2D b, Vector2D c, float z1, float z2, float z3){
        paintTriangle(Math.round(a.get(0)) ,Math.round(a.get(1)),Math.round(b.get(0)), Math.round(b.get(1)),Math.round(c.get(0)),Math.round(c.get(1)), z1, z2, z3);
    }

    public void paintTriangle(int x1, int y1, int x2, int y2, int x3, int y3, float z1, float z2, float z3) {


        this.z1 = z1;
        this.z2 = z2;
        this.z3 = z3;

        this.x1 = x1;
        this.x2 = x2;
        this.x3 = x3;

        this.y1 = y1;
        this.y2 = y2;
        this.y3 = y3;
        C = Color.color(Math.random(), Math.random(), Math.random());
       //paintDot(x1, y1, Color.YELLOWGREEN);
      // paintDot(x2, y2, Color.YELLOWGREEN);
      //paintDot(x3, y3, Color.YELLOWGREEN);
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

        if (flag){
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
                // pixelWriter.setColor(j, i, Color.AQUAMARINE);
            }
            wx1 += dx13;
            wx2 += dx12;
        }
        if (y1 == y2) {
            wx1 = (float) x1;
            wx2 = (float) x2;
        }
        if (_dx13 < dx23) {
            float c = dx23;
            dx23 = _dx13;
            _dx13 = c;
        }

        for (int i = y2; i <= y3; i++) {
            for (int j = Math.round(wx1); j <= Math.round(wx2); j++) {
                paintPoint(j, i);
                //pixelWriter.setColor(j, i, Color.AQUA);
            }
            wx1 += _dx13;
            wx2 += dx23;
        }


    }

    public void clear() {
        graphicsContext.clearRect(0, 0, 8000, 6000);
        for (int i = 0; i <width; i++){
            for (int j = 0; j <height; j++){
                z_boofer[i][j] = -200;
            }
        }
    }


}
