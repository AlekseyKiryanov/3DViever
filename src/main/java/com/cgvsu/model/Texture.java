package com.cgvsu.model;

import com.cgvsu.vectormath.vector.Vector3D;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

public class Texture {
    private final float EPS = 0.02F;

    private int[][] pixels;

    int width;
    int height;

    public Texture(File file) {
        try {
            BufferedImage bf = ImageIO.read(file);

            width = bf.getWidth();
            height = bf.getHeight();
            pixels = new int[height][width];


            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    pixels[height - row - 1][col] = bf.getRGB(col, row);
                }
            }

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Проблемка");
            alert.setHeaderText("Эту текстуру невозможно прочитать");

            alert.showAndWait();
        }
    }

    public int getColor(float u, float v) {
        if (u < 0) {
            u = 1 + u;
        }
        if (v < 0) {
            v = 1 + v;
        }
        if (u > 1 && u < 1 + EPS){
            u = 1;
        }
        if (v > 1 && v < 1 + EPS){
            v = 1;
        }
        if (u < 0 || v < 0 || u > 1 || v > 1) {
            return 0xFFFDFDFD;
        }
        int a = (int) (v * height);
        if (a == height) {
            a--;
        }
        int b = (int) (u * width);
        if (b == width) {
            b--;
        }

        return pixels[a][b];
    }


}
