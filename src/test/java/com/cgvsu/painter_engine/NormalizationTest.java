package com.cgvsu.painter_engine;

import com.cgvsu.model.Model;
import com.cgvsu.objreader.ObjReader;
import com.cgvsu.objwriter.ObjWriter;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class NormalizationTest {

    @Test
    void cube() throws IOException {
        Path fileName = Path.of("D:\\cube.obj");
        String fileContent = Files.readString(fileName);
        Model model = ObjReader.read(fileContent);

        Model new_model = new Normalization(model).recalceNormales();

        ObjWriter.write("D:\\cubeN.obj", new_model);

    }

    @Test
    void cilinder() throws IOException {
        Path fileName = Path.of("D:\\cilinder.obj");
        String fileContent = Files.readString(fileName);
        Model model = ObjReader.read(fileContent);

        Model new_model = new Normalization(model).recalceNormales();

        ObjWriter.write("D:\\cilinderN.obj", new_model);

    }

    @Test
    void cone() throws IOException {
        Path fileName = Path.of("D:\\cone.obj");
        String fileContent = Files.readString(fileName);
        Model model = ObjReader.read(fileContent);

        Model new_model = new Normalization(model).recalceNormales();

        ObjWriter.write("D:\\coneN.obj", new_model);

    }

    @Test
    void cilinderR() throws IOException {
        Path fileName = Path.of("D:\\cilinderR.obj");
        String fileContent = Files.readString(fileName);
        Model model = ObjReader.read(fileContent);

        Model new_model = new Normalization(model).recalceNormales();

        ObjWriter.write("D:\\cilinderRN.obj", new_model);

    }

    @Test
    void tram() throws IOException {
        Path fileName = Path.of("D:\\tatraT6B5_1.obj");
        String fileContent = Files.readString(fileName);
        Model model = ObjReader.read(fileContent);

        Model new_model = new Normalization(model).recalceNormales();

        ObjWriter.write("D:\\tatraT6B5_1N.obj", new_model);

    }

    @Test
    void torus() throws IOException {
        Path fileName = Path.of("D:\\torus.obj");
        String fileContent = Files.readString(fileName);
        Model model = ObjReader.read(fileContent);

        Model new_model = new Normalization(model).recalceNormales();

        ObjWriter.write("D:\\tprusN.obj", new_model);

    }


}