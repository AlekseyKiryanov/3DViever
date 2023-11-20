package com.cgvsu.painter_engine;

import com.cgvsu.model.Model;
import com.cgvsu.objreader.ObjReader;
import com.cgvsu.objwriter.ObjWriter;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class TriangulationTest {


    @Test
    void cube() throws IOException {
        Path fileName = Path.of("primitives\\cube.obj");
        String fileContent = Files.readString(fileName);

        Model model = ObjReader.read(fileContent);
        Model n_model = new Normalization(model).recalceNormales();
        Model t_model = new Triangulation(n_model).triangulate();

        ObjWriter.write("primitives\\test.obj", t_model);
    }

    @Test
    void cilinder() throws IOException {
        Path fileName = Path.of("primitives\\cylinder.obj");
        String fileContent = Files.readString(fileName);

        Model model = ObjReader.read(fileContent);
        Model n_model = new Normalization(model).recalceNormales();
        Model t_model = new Triangulation(n_model).triangulate();

        ObjWriter.write("primitives\\test.obj", t_model);
    }

    @Test
    void cone() throws IOException {
        Path fileName = Path.of("primitives\\cone.obj");
        String fileContent = Files.readString(fileName);

        Model model = ObjReader.read(fileContent);
        Model n_model = new Normalization(model).recalceNormales();
        Model t_model = new Triangulation(n_model).triangulate();

        ObjWriter.write("primitives\\test.obj", t_model);
    }

}