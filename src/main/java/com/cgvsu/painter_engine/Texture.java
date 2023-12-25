package com.cgvsu.painter_engine;

import com.cgvsu.vectormath.vector.Vector3D;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

public class Texture {

    private Texture() {
    }

    private static Texture INSTANCE;

    private Vector3D defaultColor = new Vector3D(1, 1, 1);

    private Vector3D[][] pixels;
    int width;
    int height;

    private boolean hasTexture = false;
    private boolean showTexture = true;

    public void reverseTexture(){
        this.showTexture = !this.showTexture;
    }

    public static Texture getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Texture();
        }
        return INSTANCE;
    }

    public void setTexture(File file) {
        try {
            BufferedImage bf = ImageIO.read(file);

            width = bf.getWidth();
            height = bf.getHeight();
            pixels = new Vector3D[height][width];


            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    int argb = bf.getRGB(col, row);
                    int alpha = (argb >> 24) & 0xff;
                    int red = (argb >> 16) & 0xff;
                    int green = (argb >> 8) & 0xff;
                    int blue = (argb) & 0xff;

                    pixels[height - row - 1][col] = new Vector3D((float) (red) / 255, (float) (green) / 255, (float) (blue) / 255);
                }
            }
            hasTexture = true;

            //System.out.println(Arrays.deepToString(pixels));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setDefaultColor(Color defaultColor) {
        this.defaultColor = new Vector3D((float) defaultColor.getRed(), (float) defaultColor.getGreen(), (float) defaultColor.getBlue());
    }

    public Vector3D getColor(float x, float y) {
        if (!(hasTexture && showTexture)) {
            return defaultColor;
        }
        if (x < 0 || y < 0 || x > 1 || y > 1) {
            return new Vector3D(0.9567F, 0.9567F, 0.9567F);
        }
        int a = (int) (y * height);
        if (a == height) {
            a--;
        }

        int b = (int) (x * width);
        if (b == width) {
            b--;
        }

        // System.out.println(a+" "+b);

        return pixels[a][b];
    }


}
