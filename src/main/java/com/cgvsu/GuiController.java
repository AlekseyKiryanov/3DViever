package com.cgvsu;

import com.cgvsu.affinetransf.AffineTransf;
import com.cgvsu.logger.SimpleConsoleLogger;
import com.cgvsu.objwriter.ObjWriter;
import com.cgvsu.painter_engine.Normalization;
import com.cgvsu.painter_engine.Texture;
import com.cgvsu.painter_engine.Triangulation;
import com.cgvsu.painter_engine.light.*;
import com.cgvsu.vectormath.vector.Vector3D;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.io.File;
import java.util.List;

import com.cgvsu.model.Model;
import com.cgvsu.objreader.ObjReader;
import com.cgvsu.render_engine.Camera;

import static com.cgvsu.render_engine.RenderEngine.render;

import javafx.animation.AnimationTimer;

public class GuiController {

    final private int RELOAD_MILISECONDS = 00100; //Время перерисовки кадра.

    final private float TRANSLATION = 0.5F;
    @FXML
    public CheckMenuItem is_triangle;
    @FXML
    AnchorPane anchorPane;
    @FXML
    private Canvas canvas;
    @FXML
    private Button affButton;
    @FXML
    private Button returnButton;
    @FXML
    private Button affButtonSave;
    @FXML
    private Button rotateButton;
    @FXML
    private TextField SxField;
    @FXML
    private TextField SyField;
    @FXML
    private TextField SzField;
    @FXML
    private TextField RxField;
    @FXML
    private TextField RyField;
    @FXML
    private TextField RzField;
    @FXML
    private TextField TxField;
    @FXML
    private TextField TyField;
    @FXML
    private TextField TzField;
    private AffineTransf affineTransf = new AffineTransf();
    private boolean isAutoRotate = false;
    private Model default_model = null;
    private Model original_model = null;
    private Model normal_model = null;
    private Model trianguled_model = null;

    private Lighter lighte = new PrimeLighte();

    private final SimpleConsoleLogger log = SimpleConsoleLogger.getInstance();

    private final Texture texture = Texture.getInstance();


    private Camera camera = new Camera(
            new Vector3D(0, 00, 100),
            new Vector3D(0, 0, 0),
            1.0F, 1, 0.01F, 100);

    private Timeline timeline;

    @FXML
    private void initialize() {

       mainColor.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                texture.setDefaultColor(mainColor.getValue());
            }
        });


        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        if (canvas != null) {
            canvas.setOnMouseMoved(event2 -> camera.handleMouseInput(event2.getX(), event2.getY(), false, false));
            canvas.setOnMouseDragged(event2 -> camera.handleMouseInput(event2.getX(), event2.getY(), event2.isPrimaryButtonDown(), event2.isSecondaryButtonDown()));
            canvas.setOnScroll(event2 -> {
                camera.mouseDeltaY = event2.getDeltaY();
                camera.handleMouseInput(event2.getX(), event2.getY(), false, false);
            });
            // Установка обработчиков событий для кнопок
            rotateButton.setOnAction(e -> {
                startRotationAnimation();
            });

            affButton.setOnAction(e -> {
                applyTransformations(
                        parseTextField(SxField, false), parseTextField(SyField, false), parseTextField(SzField, false),
                        parseTextField(RxField, false), parseTextField(RyField, false), parseTextField(RzField, false),
                        parseTextField(TxField, true), parseTextField(TyField, true), parseTextField(TzField, true)
                );
            });
            affButtonSave.setOnAction(e -> {
                saveModelInFile();
            });
            returnButton.setOnAction(e -> {
                returnOriginalModel();
            });
        }

        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);

        KeyFrame frame = new KeyFrame(Duration.millis(RELOAD_MILISECONDS), event -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();

            canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
            camera.setAspectRatio((float) (width / height));

            if (default_model != null) {
                render(lighte, canvas.getGraphicsContext2D(), camera, trianguled_model, (int) width, (int) height);
                log.setSameLevel(System.Logger.Level.OFF);
            }
        });

        timeline.getKeyFrames().add(frame);
        timeline.play();
    }

    private void applyTransformations(
            float scaleX, float scaleY, float scaleZ,
            float rotateX, float rotateY, float rotateZ,
            float translateX, float translateY, float translateZ) {

        // Установка параметров трансформаций
        affineTransf.setSx(scaleX);
        affineTransf.setSy(scaleY);
        affineTransf.setSz(scaleZ);
        affineTransf.setRx(rotateX);
        affineTransf.setRy(rotateY);
        affineTransf.setRz(rotateZ);
        affineTransf.setTx(translateX);
        affineTransf.setTy(translateY);
        affineTransf.setTz(translateZ);

        // Применение трансформаций к модели
      //  if (is_triangle.isSelected()) {
            trianguled_model = affineTransf.transformModel(trianguled_model);
      //  }
         default_model = affineTransf.transformModel(default_model);

    }

    private void saveModelInFile() {
        if (is_triangle.isSelected()) {
            saveModel(trianguled_model);
        } else {
            saveModel(default_model);
        }
    }

    private void saveModel(Model model) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Model");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Wavefront OBJ files (*.obj)", "*.obj"));

        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            String chosenFilePath = file.getAbsolutePath();

            // Проверка наличия расширения .obj
            if (!chosenFilePath.toLowerCase().endsWith(".obj")) {
                chosenFilePath += ".obj";
            }

            ObjWriter.write(chosenFilePath, model);
        }
    }

    private void returnOriginalModel() {
        if (is_triangle.isSelected()) {
            default_model = original_model;
            normal_model = new Normalization(default_model).recalceNormales();
            trianguled_model = new Triangulation(normal_model).triangulate();
            is_triangle.setSelected(false);

        } else {
            default_model = original_model;
        }
    }

    private float parseTextField(TextField textField, boolean isTranslate) {
        try {
            return Float.parseFloat(textField.getText());
        } catch (NumberFormatException e) {
            if (!isTranslate) {
                return 1;
            }
            return 0;
        }
    }


    private void loadModel(Path fileName) {
        try {
            String fileContent = Files.readString(fileName);
            original_model = ObjReader.read(fileContent);
            default_model = ObjReader.read(fileContent);
            normal_model = new Normalization(default_model).recalceNormales();
            trianguled_model = new Triangulation(normal_model).triangulate();
            // todo: обработка ошибок
        } catch (IOException exception) {

        }
    }

    private long lastTime = 0;

    private void startRotationAnimation() {
        if (!isAutoRotate) {
            isAutoRotate = true;
        } else {
            isAutoRotate = false;
            lastTime = 0; // Сброс значения lastTime при остановке анимации
        }
        AnimationTimer rotationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (isAutoRotate) {
                    if (lastTime != 0) {
                        final double elapsedSeconds = (now - lastTime) / 1e9;

                        final float rotateSpeed = 10;
                        float rotateAngle = (float) (elapsedSeconds * rotateSpeed);

                        applyTransformations(1, 1, 1, 0, rotateAngle, 0, 0, 0, 0);

                    }
                    lastTime = now;
                } else {
                    lastTime = 0;
                    applyTransformations(1, 1, 1, 0, 0, 0, 0, 0, 0);
                }
            }
        };
        if (isAutoRotate) {
            rotationTimer.start();
        } else {
            rotationTimer.stop();
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
    private void onOpenTextureMenuItemClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Изображение", List.of("*.png","*.bmp","*.jpg","*.jpeg")));
        fileChooser.setTitle("Выбрать текстуру");

        File file = fileChooser.showOpenDialog((Stage) canvas.getScene().getWindow());
        if (file == null) {
            return;
        }

        texture.setTexture(file);
    }

    @FXML
    private void resetTextureMenuItemClick() {
        texture.reverseTexture();
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

    @FXML
    private ColorPicker mainColor;

    @FXML
    private void setPrimeLighte() {
        lighte = new PrimeLighte();
    }

    @FXML
    private void setPoligonLighte() {
        lighte = new PoligonLighte();
    }

    @FXML
    private void setBlikeLighte() {
        lighte = new BlikeLighte();
    }

    @FXML
    private void setBorderLighte() {
        lighte = new BorderLighte();
    }

    @FXML
    public void activeTraceLog(ActionEvent actionEvent) {
        log.setSameLevel(System.Logger.Level.TRACE);
    }
}