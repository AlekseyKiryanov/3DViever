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

class NormalizationTest {

    @Test
    void cube() throws IOException {
        Path fileName = Path.of("primitives\\cube");
        String fileContent = Files.readString(fileName);
        Model model = ObjReader.read(fileContent);

        Model new_model = new Normalization(model).recalculateNormals();

        ObjWriter.write("D:\\gps3.obj", new_model);

    }

    @Test
    void cilinder() throws IOException {
        Path fileName = Path.of("primitives\\cylinder.obj");
        String fileContent = Files.readString(fileName);
        Model model = ObjReader.read(fileContent);

        Model new_model = new Normalization(model).recalculateNormals();

        ObjWriter.write("primitives\\test.obj", new_model);

    }

    @Test
    void cone() throws IOException {
        Path fileName = Path.of("primitives\\cone.obj");
        String fileContent = Files.readString(fileName);
        Model model = ObjReader.read(fileContent);

        Model new_model = new Normalization(model).recalculateNormals();

        ObjWriter.write("primitives\\test.obj", new_model);

    }

    @Test
    void cilinderT() throws IOException {
        Path fileName = Path.of("primitives\\cylinder.obj");
        String fileContent = Files.readString(fileName);

        Model model = ObjReader.read(fileContent);
        Model t_model = new Triangulation(model).triangulate();
        Model n_model = new Normalization(t_model).recalculateNormals();

        ObjWriter.write("primitives\\test.obj", n_model);

    }

    @Test
    void torus() throws IOException {
        Path fileName = Path.of("primitives\\torus.obj");
        String fileContent = Files.readString(fileName);
        Model model = ObjReader.read(fileContent);

        Model new_model = new Normalization(model).recalculateNormals();

        ObjWriter.write("primitives\\test.obj", new_model);

    }


}