package com.cgvsu;


import com.cgvsu.affine_transform.AffineTransform;
import com.cgvsu.editing_model.Deletion;
import com.cgvsu.editing_model.Normalization;
import com.cgvsu.editing_model.Triangulation;
import com.cgvsu.logger.SimpleConsoleLogger;
import com.cgvsu.obj_writer.ObjWriter;
import com.cgvsu.render_engine.camera.Camera;
import com.cgvsu.render_engine.light.*;
import com.cgvsu.render_engine.rasterization.Rasterization;
import com.cgvsu.render_engine.rendering.RenderEngine;
import com.cgvsu.render_engine.rendering.RenderType;
import com.cgvsu.vectormath.vector.Vector3D;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.File;
import java.util.List;

import com.cgvsu.model.Model;
import com.cgvsu.obj_reader.ObjReader;


import javafx.animation.AnimationTimer;

public class GuiController {

    final private int RELOAD_MILLISECONDS = 100; //Время перерисовки кадра.


    final private float TRANSLATION = 0.5F;


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

    private final AffineTransform affineTransform = new AffineTransform();

    private boolean isAutoRotate = false;

    private RenderType renderType = RenderType.NO_MESH;
    private Model originalModel = null;
    private Model trianguledModel = null;

    private Light lighte = new PrimeLight();

    private final SimpleConsoleLogger log = SimpleConsoleLogger.getInstance();
    private final Rasterization rasterization = Rasterization.getInstance();
    private final Deletion deletion = Deletion.getInstance();


    private Camera camera = new Camera(
            new Vector3D(0, 0, 100),
            new Vector3D(0, 0, 0),
            1.0F, 1, -20, 20);

    private final RenderEngine renderEngine = new RenderEngine();

    private Timeline timeline;

    @FXML
    private void initialize() {

        mainColor.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (trianguledModel != null) {
                    trianguledModel.getTexture().setDefaultColor(mainColor.getValue());
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Проблемка");
                    alert.setHeaderText("Чтобы изменить сначала загрузите модель");

                    alert.showAndWait();
                }

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
            canvas.setOnMouseClicked(event -> {
                switch (event.getButton()) {
                    case PRIMARY -> handlePrimaryClick(event);
                    case SECONDARY -> handleRightClick(event);
                }
            });

            // Установка обработчиков событий для кнопок
            rotateButton.setOnAction(e -> {

                if (trianguledModel != null) {
                    startRotationAnimation();
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Проблемка");
                    alert.setHeaderText("Чтобы вращать сначала загрузите модель");

                    alert.showAndWait();
                }

            });

            affButton.setOnAction(e -> {

                if (trianguledModel != null) {

                    applyTransformations(
                            parseTextField(SxField, false), parseTextField(SyField, false), parseTextField(SzField, false),
                            parseTextField(RxField, false), parseTextField(RyField, false), parseTextField(RzField, false),
                            parseTextField(TxField, true), parseTextField(TyField, true), parseTextField(TzField, true)
                    );
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Проблемка");
                    alert.setHeaderText("Чтобы изменять сначала загрузите модель");

                    alert.showAndWait();
                }

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

        KeyFrame frame = new KeyFrame(Duration.millis(RELOAD_MILLISECONDS), event -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();

            canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
            camera.setAspectRatio((float) (height / width));

            rasterization.clearScreen(canvas.getGraphicsContext2D(), (int) width, (int) height);
            rasterization.setLighte(lighte);
            rasterization.setCoordinateLight(camera.getPosition());

            if (trianguledModel != null) {

                rasterization.setTexture(trianguledModel.getTexture());
                renderEngine.render(camera, trianguledModel, (int) width, (int) height);
                log.setSameLevel(System.Logger.Level.OFF);
            }
        });

        timeline.getKeyFrames().add(frame);
        timeline.play();
    }

    private void handlePrimaryClick(MouseEvent event) {
        if (renderType == RenderType.SELECTION_VERTICES) {
            rasterization.chooseVertex((int) event.getX(), (int) event.getY());
        }
        if (renderType == RenderType.SELECTION_POLYGONS) {
            rasterization.choosePolygon((int) event.getX(), (int) event.getY());
        }
    }

    private void handleRightClick(MouseEvent event) {
        if (renderType == RenderType.SELECTION_VERTICES) {
            rasterization.cancelChooseVertex((int) event.getX(), (int) event.getY());
        }
        if (renderType == RenderType.SELECTION_POLYGONS) {
            rasterization.cancelChoosePolygon((int) event.getX(), (int) event.getY());
        }
    }

    private void applyTransformations(
            float scaleX, float scaleY, float scaleZ,
            float rotateX, float rotateY, float rotateZ,
            float translateX, float translateY, float translateZ) {

        // Установка параметров трансформаций
        affineTransform.setScaleX(scaleX);
        affineTransform.setScaleY(scaleY);
        affineTransform.setScaleZ(scaleZ);
        affineTransform.setRotationX(rotateX);
        affineTransform.setRotationY(rotateY);
        affineTransform.setRotationZ(rotateZ);
        affineTransform.setTranslationX(translateX);
        affineTransform.setTranslationY(translateY);
        affineTransform.setTranslationZ(translateZ);
        trianguledModel = affineTransform.transformModel(trianguledModel);
    }

    private void saveModelInFile() {
        saveModel(trianguledModel);
    }

    private void saveModel(Model model) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Model");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Wavefront OBJ files (*.obj)", "*.obj"));

        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            String chosenFilePath = file.getAbsolutePath();

            if (!chosenFilePath.toLowerCase().endsWith(".obj")) {
                chosenFilePath += ".obj";
            }

            ObjWriter.write(chosenFilePath, model);
        }
    }

    private void returnOriginalModel() {
        trianguledModel = originalModel.copy();
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
        rasterization.resetChooseVertices();
        rasterization.resetChosenPolygons();
        deletion.resetStack();
        try {
            String fileContent = Files.readString(fileName);

            Model default_model = ObjReader.read(fileContent);
            Model normal_model = new Normalization(default_model).recalculateNormals();
            trianguledModel = new Triangulation(normal_model).triangulate();
            originalModel = trianguledModel.copy();
        } catch (Exception exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Проблемка");
            alert.setHeaderText("Эту модель невозможно прочитать");

            alert.showAndWait();
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
        if (trianguledModel != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Изображение", List.of("*.png", "*.bmp", "*.jpg", "*.jpeg")));
            fileChooser.setTitle("Выбрать текстуру");

            File file = fileChooser.showOpenDialog((Stage) canvas.getScene().getWindow());
            if (file == null) {
                return;
            }

            trianguledModel.getTexture().setTexture(file);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Проблемка");
            alert.setHeaderText("Чтобы выбрать текстуру сначала загрузите модель");

            alert.showAndWait();

        }
    }

    @FXML
    private void resetTextureMenuItemClick() {
        if (trianguledModel != null) {
            trianguledModel.getTexture().reverseTexture();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Проблемка");
            alert.setHeaderText("Чтобы показывать и скрывать текстуры сначала загрузите модель");

            alert.showAndWait();
        }
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
    private ColorPicker mainColor;

    @FXML
    private void setPrimeLighte() {
        lighte = new PrimeLight();
    }

    @FXML
    private void setPoligonLighte() {
        lighte = new PolygonLight();
    }

    @FXML
    private void setBlikeLighte() {
        lighte = new SpecularLight();
    }

    @FXML
    private void setBorderLighte() {
        lighte = new BorderLight();
    }

    @FXML
    private RadioMenuItem noMesh;

    @FXML
    private RadioMenuItem selectionVertices;

    @FXML
    private RadioMenuItem selectionPolygons;


    @FXML
    private void setNoMeshRender() {
        renderType = RenderType.NO_MESH;
        renderEngine.setRender(RenderType.NO_MESH);
        selectionPolygons.setSelected(false);
        selectionVertices.setSelected(false);
        noMesh.setSelected(true);
    }

    @FXML
    private void setRenderSelectionVertices() {
        renderType = RenderType.SELECTION_VERTICES;
        renderEngine.setRender(RenderType.SELECTION_VERTICES);
        selectionPolygons.setSelected(false);
        selectionVertices.setSelected(true);
        noMesh.setSelected(false);
    }

    @FXML
    private void setRenderSelectionPolygons() {
        renderType = RenderType.SELECTION_POLYGONS;
        renderEngine.setRender(RenderType.SELECTION_POLYGONS);
        selectionPolygons.setSelected(true);
        selectionVertices.setSelected(false);
        noMesh.setSelected(false);
    }

    @FXML
    public void activeTraceLog() {
        log.setSameLevel(System.Logger.Level.TRACE);
    }

    @FXML
    public void deleteVertices() {
        deletion.deleteVerteces(trianguledModel, rasterization.getChosenVertexes());
        rasterization.resetChooseVertices();
    }

    @FXML
    public void deletePolygons() {
        deletion.deletePolygons(trianguledModel, rasterization.getChosenPolygons());
        rasterization.resetChosenPolygons();
    }

    @FXML
    public void cancelDeletion(){
        trianguledModel = deletion.returnOldModel(trianguledModel);
    }
}