package com.cgvsu.painter_engine;

import com.cgvsu.model.Model;
import com.cgvsu.objreader.ObjReader;
import com.cgvsu.objwriter.ObjWriter;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class RasterizationTest {


    @Test
    void cube() throws IOException {
        Path fileName = Path.of("D:\\cubeN.obj");
        String fileContent = Files.readString(fileName);
        Model model = ObjReader.read(fileContent);

        Model new_model = new Rasterization(model).rasterizate();

        ObjWriter.write("D:\\cubeR.obj", new_model);

    }

    @Test
    void cilinder() throws IOException {
        Path fileName = Path.of("D:\\cilinderN.obj");
        String fileContent = Files.readString(fileName);
        Model model = ObjReader.read(fileContent);

        Model new_model = new Rasterization(model).rasterizate();

        ObjWriter.write("D:\\cilinderR.obj", new_model);

    }

    @Test
    void cone() throws IOException {
        Path fileName = Path.of("D:\\coneN.obj");
        String fileContent = Files.readString(fileName);
        Model model = ObjReader.read(fileContent);

        Model new_model = new Rasterization(model).rasterizate();

        ObjWriter.write("D:\\coneR.obj", new_model);

    }

}