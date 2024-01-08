package com.cgvsu.render_engine.rasterization;

import com.cgvsu.model.Texture;
import com.cgvsu.render_engine.light.Light;
import com.cgvsu.render_engine.light.PrimeLight;
import com.cgvsu.vectormath.vector.Vector2D;
import com.cgvsu.vectormath.vector.Vector3D;
import javafx.scene.image.*;

import java.nio.IntBuffer;
import java.util.ArrayList;

public class Rasterization {


    private Rasterization() {

    }

    private static Rasterization INSTANCE;

    public static Rasterization getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Rasterization();
        }
        return INSTANCE;
    }


    private static final float EPS = 0.01F;
    private static final float EPS2 = 0.0001F;
    private final ArrayList<Integer> naturalNumbersList = new ArrayList<>();
    private int width;
    private int height;
    private IntBuffer buffer;
    private int[] pixels;

    private float[][] zBuffer;
    private int[][] vertexBuffer;
    private int[][] polygonBuffer;
    private Vector3D coordinateLight;
    private Vector3D coordinateEyes;
    private Light light = new PrimeLight();
    private Texture texture;
    private ArrayList<Integer> chosenVertexes = new ArrayList<>();
    private ArrayList<Integer> chosenPolygons = new ArrayList<>();

    private float z1;
    private float z2;
    private float z3;
    private float realZ1;
    private float realZ2;
    private float realZ3;
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
    private int numberPolygon;
    private int selfColor;


    public void clearScreen(int width, int height, int color1) {
        this.width = width;
        this.height = height;
        buffer = IntBuffer.allocate(width * height);
        pixels = buffer.array();
        vertexBuffer = null;
        polygonBuffer = null;
        zBuffer = new float[width][height];
        naturalNumbersList.clear();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                zBuffer[i][j] = -200;
                pixels[j * width + i] = color1;
            }
            naturalNumbersList.add(i);

        }
    }

    public Image paint() {
        PixelBuffer<IntBuffer> pixelBuffer = new PixelBuffer<>(width, height, buffer, PixelFormat.getIntArgbPreInstance());
        return new WritableImage(pixelBuffer);
    }

    public void resetVertexBuffer() {
        vertexBuffer = new int[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                vertexBuffer[i][j] = -1;
            }
        }
    }

    public void resetPolygonBuffer() {
        resetVertexBuffer();
        polygonBuffer = new int[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                polygonBuffer[i][j] = -1;
            }
        }
    }

    public void setLight(Light light) {
        this.light = light;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setCoordinateLight(Vector3D coordinateLight) {
        this.coordinateLight = coordinateLight;
    }

    public void setCoordinateEyes(Vector3D coordinateEyes) {
        this.coordinateEyes = coordinateEyes;
    }

    private void paintPointTexture(int x, int y) {
        float alpha = (float) ((x - x3) * caff4 - (y - y3) * caff5) / caff2;
        float beta = (float) (x - x3) / caff3 - alpha * caff1;
        float gama = 1 - alpha - beta;
        float summa = alpha + beta + gama;
        alpha /= summa;
        beta /= summa;
        gama /= summa;

        float z = (float) alpha * z1 + beta * z2 + gama * z3;
        if (x < 0 || y < 0 || x >= width || y >= height || !(z - zBuffer[x][y] > EPS2)) {
            return;
        }
        this.zBuffer[x][y] = z;

        alpha /= realZ1;
        beta /= realZ2;
        gama /= realZ3;
        summa = alpha + beta + gama;
        alpha /= summa;
        beta /= summa;
        gama /= summa;

        Vector2D t = new Vector2D(0, 0);
        t.addThis(t1.multiply(alpha));
        t.addThis(t2.multiply(beta));
        t.addThis(t3.multiply(gama));

        float u = t.get(0);
        float v = t.get(1);
        int C = texture.getColor(u, v);

        int color = light.setLight(coordinateLight, coordinateEyes, C, alpha, beta, gama, n1, n2, n3);

        pixels[y * width + x] = color;
    }

    private void paintPointSelfColored(int x, int y) {
        float alpha = (float) ((x - x3) * caff4 - (y - y3) * caff5) / caff2;
        float beta = (float) (x - x3) / caff3 - alpha * caff1;
        float gama = 1 - alpha - beta;
        float summa = alpha + beta + gama;
        alpha /= summa;
        beta /= summa;
        gama /= summa;

        float z = (float) alpha * z1 + beta * z2 + gama * z3;
        if (x < 0 || y < 0 || x >= width || y >= height || !(z - zBuffer[x][y] > EPS2)) {
            return;
        }
        this.zBuffer[x][y] = z;

        int color = light.setLight(coordinateLight, coordinateEyes, selfColor, alpha, beta, gama, n1, n2, n3);

        polygonBuffer[x][y] = numberPolygon;
        pixels[y * width + x] = color;
    }

    public void paintTriangleTexture(TriangleTextured triangle) {

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

        this.realZ1 = triangle.realZ1();
        this.realZ2 = triangle.realZ2();
        this.realZ3 = triangle.realZ3();

        this.t1 = triangle.t1();
        this.t2 = triangle.t2();
        this.t3 = triangle.t3();

        paintPointsTriangleTexture(Math.round(triangle.a().get(0)), Math.round(triangle.a().get(1)), Math.round(triangle.b().get(0)), Math.round(triangle.b().get(1)), Math.round(triangle.c().get(0)), Math.round(triangle.c().get(1)));
    }

    public void paintTriangleSelfColored(TriangleSelfColored triangle) {

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
        this.numberPolygon = triangle.number();
        this.selfColor = triangle.selfColor();

        this.n1 = triangle.n1();
        this.n2 = triangle.n2();
        this.n3 = triangle.n3();

        this.z1 = triangle.z1();
        this.z2 = triangle.z2();
        this.z3 = triangle.z3();

        paintTriangleSelfColored(Math.round(triangle.a().get(0)), Math.round(triangle.a().get(1)), Math.round(triangle.b().get(0)), Math.round(triangle.b().get(1)), Math.round(triangle.c().get(0)), Math.round(triangle.c().get(1)));
    }

    private void paintPointsTriangleTexture(int x1, int y1, int x2, int y2, int x3, int y3) {


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
            int finalI = i;
            int a = Integer.min(width - 1, Integer.max(0, Integer.min(Math.round(wx1), Math.round(wx2))));
            int b = Integer.min(width - 2, Integer.max(0, Integer.max(Math.round(wx1), Math.round(wx2))));
            naturalNumbersList.subList(a, b + 1).forEach(j -> paintPointTexture(j, finalI));
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
            int finalI = i;
            int a = Integer.min(width - 1, Integer.max(0, Integer.min(Math.round(wx1), Math.round(wx2))));
            int b = Integer.min(width - 2, Integer.max(0, Integer.max(Math.round(wx1), Math.round(wx2))));
            naturalNumbersList.subList(a, b + 1).forEach(j -> paintPointTexture(j, finalI));
            wx1 += _dx13;
            wx2 += dx23;
        }


    }

    private void paintTriangleSelfColored(int x1, int y1, int x2, int y2, int x3, int y3) {
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
            int finalI = i;
            int a = Integer.min(width - 1, Integer.max(0, Integer.min(Math.round(wx1), Math.round(wx2))));
            int b = Integer.min(width - 2, Integer.max(0, Integer.max(Math.round(wx1), Math.round(wx2))));
            naturalNumbersList.subList(a, b + 1).forEach(j -> paintPointSelfColored(j, finalI));
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
            int finalI = i;
            int a = Integer.min(width - 1, Integer.max(0, Integer.min(Math.round(wx1), Math.round(wx2))));
            int b = Integer.min(width - 2, Integer.max(0, Integer.max(Math.round(wx1), Math.round(wx2))));
            naturalNumbersList.subList(a, b + 1).forEach(j -> paintPointSelfColored(j, finalI));
            wx1 += _dx13;
            wx2 += dx23;
        }
    }


    public void paintDot(Vector2D p, float z, int n, int color) {
        int x = Math.round(p.get(0));
        int y = Math.round(p.get(1));

        if (x < 0 || y < 0 || x >= width || y >= height || (Math.abs(z - zBuffer[x][y]) > EPS) && zBuffer[x][y] != -200) {
            return;
        }

        for (int row = y - 1; row <= y + 1; ++row)
            for (int col = x - 1; col <= x + 1; ++col) {
                if (col < 0 || row < 0 || col >= width || row >= height) {
                    return;
                }
                pixels[row * width + col] = color;
                vertexBuffer[col][row] = n;
            }
    }

    public void chooseVertex(int a, int b) {
        if (vertexBuffer == null) {
            return;
        }
        if (vertexBuffer[a][b] == -1) {
            return;
        }
        chosenVertexes.add(vertexBuffer[a][b]);

    }

    public void cancelChooseVertex(int a, int b) {
        if (vertexBuffer == null) {
            return;
        }
        if (vertexBuffer[a][b] == -1) {
            return;
        }
        Integer x = vertexBuffer[a][b];
        chosenVertexes.remove(x);

    }

    public void cancelChoosePolygon(int a, int b) {
        if (polygonBuffer == null) {
            return;
        }
        if (polygonBuffer[a][b] == -1) {
            return;
        }
        Integer x = polygonBuffer[a][b];
        chosenPolygons.remove(x);

    }

    public ArrayList<Integer> getChosenVertexes() {
        return chosenVertexes;
    }

    public void resetChooseVertices() {
        chosenVertexes = new ArrayList<>();
    }


    public void choosePolygon(int a, int b) {

        if (polygonBuffer == null) {
            return;
        }
        if (polygonBuffer[a][b] == -1) {
            return;
        }
        chosenPolygons.add(polygonBuffer[a][b]);

    }

    public ArrayList<Integer> getChosenPolygons() {
        return chosenPolygons;

    }

    public void resetChosenPolygons() {
        chosenPolygons = new ArrayList<>();
    }

    public void paintPoints(TriangleVertices triangle) {

        paintDot(triangle.point1(), triangle.z1(), triangle.number1(), 0xFFFFA500);
        paintDot(triangle.point2(), triangle.z2(), triangle.number2(), 0xFFFFA500);
        paintDot(triangle.point3(), triangle.z3(), triangle.number3(), 0xFFFFA500);


        paintLine(triangle.point1(), triangle.point2(), triangle.z1(), triangle.z2(), 0xFFFFA500);
        paintLine(triangle.point2(), triangle.point3(), triangle.z2(), triangle.z3(), 0xFFFFA500);
        paintLine(triangle.point3(), triangle.point1(), triangle.z3(), triangle.z1(), 0xFFFFA500);
    }


    public void paintLine(Vector2D point1, Vector2D point2, float z1, float z2, int color) {

        float a1 = point1.get(0);
        float a2 = point2.get(0);
        float b1 = point1.get(1);
        float b2 = point2.get(1);

        boolean ishorizontal = Math.abs(a2 - a1) >= Math.abs(b2 - b1);


        int x1;
        int y1;
        int x2;
        int y2;
        float c1 = 0;
        float c2 = 0;

        if (ishorizontal) {
            if (a1 < a2) {
                x1 = Math.round(a1);
                y1 = Math.round(b1);
                x2 = Math.round(a2);
                y2 = Math.round(b2);
                c2 = z2;
                c1 = z1;
            } else {
                x1 = Math.round(a2);
                y1 = Math.round(b2);
                x2 = Math.round(a1);
                y2 = Math.round(b1);
                c2 = z1;
                c1 = z2;
            }

        } else {
            if (b1 < b2) {
                x1 = Math.round(b1);
                y1 = Math.round(a1);
                x2 = Math.round(b2);
                y2 = Math.round(a2);
                c2 = z2;
                c1 = z1;
            } else {
                x1 = Math.round(b2);
                y1 = Math.round(a2);
                x2 = Math.round(b1);
                y2 = Math.round(a1);
                c2 = z1;
                c1 = z2;
            }
        }


        int deltax = Math.abs(x1 - x2);
        int deltay = Math.abs(y1 - y2);

        int error = 0;
        int deltaerror = deltay + 1;
        int y = y1;
        int diry = y2 - y1;


        if (diry > 0) {
            diry = 1;
        } else {
            diry = -1;
        }


        for (int x = x1; x <= x2; x++) {

            float z = c1 + (c2 - c1) * (x - x1) / (x2 - x1 + 1);


            if (ishorizontal) {
                if (x >= 0 && y >= 0 && x < width && y < height && ((Math.abs(z - zBuffer[x][y]) < EPS) || zBuffer[x][y] == -200)) {
                    pixels[y * width + x] = color;
                }
            } else {
                if (y >= 0 && x >= 0 && y < width && x < height && ((Math.abs(z - zBuffer[y][x]) < EPS) || zBuffer[y][x] == -200)) {
                    pixels[x * width + y] = color;
                }
            }

            error = error + deltaerror;
            if (error >= deltax + 1) {
                y = y + diry;
                error = error - (deltax + 1);
            }
        }

    }


}
