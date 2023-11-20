package com.cgvsu;

import com.cgvsu.painter_engine.Normalization;
import com.cgvsu.painter_engine.Triangulation;
import com.cgvsu.render_engine.RenderEngine;
import com.cgvsu.vectormath.vector.Vector3D;
import javafx.fxml.FXML;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.io.File;
import javax.vecmath.Vector3f;

import com.cgvsu.model.Model;
import com.cgvsu.objreader.ObjReader;
import com.cgvsu.render_engine.Camera;

public class GuiController {

    final private float TRANSLATION = 0.5F;

    @FXML
    public CheckMenuItem is_triangle;

    @FXML
    AnchorPane anchorPane;

    @FXML
    private Canvas canvas;

    private Model default_model = null;
    private Model normal_model = null;
    private Model trianguled_model = null;

    private Camera camera = new Camera(
            new Vector3D(0, 00, 100),
            new Vector3D(0, 0, 0),
            1.0F, 1, 0.01F, 100);

    private Timeline timeline;

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);

        KeyFrame frame = new KeyFrame(Duration.millis(34), event -> {
            //Работа в 30 ФПС
            double width = canvas.getWidth();
            double height = canvas.getHeight();

            canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
            camera.setAspectRatio((float) (width / height));

            if (default_model != null) {
                if (is_triangle.isSelected()) {
                    RenderEngine.render(canvas.getGraphicsContext2D(), camera, trianguled_model, (int) width, (int) height);
                } else {
                    RenderEngine.render(canvas.getGraphicsContext2D(), camera, default_model, (int) width, (int) height);
                }
            }
        });

        timeline.getKeyFrames().add(frame);
        timeline.play();
    }

    private void loadModel(Path fileName){
        try {
            String fileContent = Files.readString(fileName);
            default_model = ObjReader.read(fileContent);
            normal_model = new Normalization(default_model).recalceNormales();
            trianguled_model = new Triangulation(normal_model).triangulate();
            // todo: обработка ошибок
        } catch (IOException exception) {

        }
    }

    @FXML
    private void onOpenModelMenuItemClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setTitle("Load Model");

        File file = fileChooser.showOpenDialog((Stage) canvas.getScene().getWindow());
        if (file == null) {
            return;
        }

        loadModel(Path.of(file.getAbsolutePath()));
    }

    @FXML
    private void loadCube() {
        loadModel(Path.of("primitives\\cube.obj"));
    }

    @FXML
    private void loadCilynder() {
        loadModel(Path.of("primitives\\cylinder.obj"));
    }

    @FXML
    private void loadCone() {
        loadModel(Path.of("primitives\\cone.obj"));
    }

    @FXML
    private void loadTorus() {
        loadModel(Path.of("primitives\\torus.obj"));
    }

    @FXML
    public void handleCameraForward(ActionEvent actionEvent) {
        camera.movePosition(new Vector3D(0, 0, -TRANSLATION));
    }

    @FXML
    public void handleCameraBackward(ActionEvent actionEvent) {
        camera.movePosition(new Vector3D(0, 0, TRANSLATION));
    }

    @FXML
    public void handleCameraLeft(ActionEvent actionEvent) {
        camera.movePosition(new Vector3D(TRANSLATION, 0, 0));
    }

    @FXML
    public void handleCameraRight(ActionEvent actionEvent) {
        camera.movePosition(new Vector3D(-TRANSLATION, 0, 0));
    }

    @FXML
    public void handleCameraUp(ActionEvent actionEvent) {
        camera.movePosition(new Vector3D(0, TRANSLATION, 0));
    }

    @FXML
    public void handleCameraDown(ActionEvent actionEvent) {
        camera.movePosition(new Vector3D(0, -TRANSLATION, 0));
    }
}