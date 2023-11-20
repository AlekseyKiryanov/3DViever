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
        Path fileName = Path.of("primitives\\cube.obj");
        String fileContent = Files.readString(fileName);
        Model model = ObjReader.read(fileContent);

        Model new_model = new Normalization(model).recalceNormales();

        ObjWriter.write("primitives\\test.obj", new_model);

    }

    @Test
    void cilinder() throws IOException {
        Path fileName = Path.of("primitives\\cylinder.obj");
        String fileContent = Files.readString(fileName);
        Model model = ObjReader.read(fileContent);

        Model new_model = new Normalization(model).recalceNormales();

        ObjWriter.write("primitives\\test.obj", new_model);

    }

    @Test
    void cone() throws IOException {
        Path fileName = Path.of("primitives\\cone.obj");
        String fileContent = Files.readString(fileName);
        Model model = ObjReader.read(fileContent);

        Model new_model = new Normalization(model).recalceNormales();

        ObjWriter.write("primitives\\test.obj", new_model);

    }

    @Test
    void cilinderT() throws IOException {
        Path fileName = Path.of("primitives\\cylinder.obj");
        String fileContent = Files.readString(fileName);

        Model model = ObjReader.read(fileContent);
        Model t_model = new Triangulation(model).triangulate();
        Model n_model = new Normalization(t_model).recalceNormales();

        ObjWriter.write("primitives\\test.obj", n_model);

    }

    @Test
    void torus() throws IOException {
        Path fileName = Path.of("primitives\\torus.obj");
        String fileContent = Files.readString(fileName);
        Model model = ObjReader.read(fileContent);

        Model new_model = new Normalization(model).recalceNormales();

        ObjWriter.write("primitives\\test.obj", new_model);

    }


}