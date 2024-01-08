package com.cgvsu.painter_engine;

import com.cgvsu.model.Model;
import com.cgvsu.obj_reader.ObjReader;
import com.cgvsu.obj_writer.ObjWriter;
import com.cgvsu.editing_model.Normalization;
import com.cgvsu.editing_model.Triangulation;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class TriangulationTest {


    @Test
    void cube() throws IOException {
        Path fileName = Path.of("collection\\cube.obj");
        String fileContent = Files.readString(fileName);

        Model model = ObjReader.read(fileContent);
        Model n_model = new Normalization(model).recalculateNormals();
        Model t_model = new Triangulation(n_model).triangulate();

        ObjWriter.write("collection\\test.obj", t_model);
    }

    @Test
    void cilinder() throws IOException {
        Path fileName = Path.of("collection\\cylinder.obj");
        String fileContent = Files.readString(fileName);

        Model model = ObjReader.read(fileContent);
        Model n_model = new Normalization(model).recalculateNormals();
        Model t_model = new Triangulation(n_model).triangulate();

        ObjWriter.write("collection\\test.obj", t_model);
    }

    @Test
    void cone() throws IOException {
        Path fileName = Path.of("collection\\cone.obj");
        String fileContent = Files.readString(fileName);

        Model model = ObjReader.read(fileContent);
        Model n_model = new Normalization(model).recalculateNormals();
        Model t_model = new Triangulation(n_model).triangulate();

        ObjWriter.write("collection\\test.obj", t_model);
    }

}