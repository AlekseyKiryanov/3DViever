package com.cgvsu;


import com.cgvsu.affine_transform.AffineTransform;
import com.cgvsu.editing_model.Deletion;
import com.cgvsu.editing_model.Normalization;
import com.cgvsu.editing_model.Triangulation;
import com.cgvsu.logger.SimpleConsoleLogger;
import com.cgvsu.model.Texture;
import com.cgvsu.obj_writer.ObjWriter;
import com.cgvsu.render_engine.camera.Camera;
import com.cgvsu.render_engine.light.*;
import com.cgvsu.render_engine.rasterization.Rasterization;
import com.cgvsu.render_engine.rendering.*;
import com.cgvsu.vectormath.vector.Vector3D;
import javafx.fxml.FXML;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import com.cgvsu.model.Model;
import com.cgvsu.obj_reader.ObjReader;


import javafx.animation.AnimationTimer;

public class GuiController {
    final private int RELOAD_MILLISECONDS = 100;
    private long lastTime = 0;
    @FXML
    AnchorPane anchorPane;
    @FXML
    private ImageView imageCanvas;
    @FXML
    private Button affButton;
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
    @FXML
    private Button newCamera;
    @FXML
    private Button setCameraLight;
    @FXML
    private Button delCamera;
    @FXML
    private Label modelStatistic;
    @FXML
    private Label worldStatistic;
    private final AffineTransform affineTransform = new AffineTransform();
    private boolean isAutoRotate = false;
    private Model activeModel = null;
    private final SimpleConsoleLogger log = SimpleConsoleLogger.getInstance();
    private final Rasterization rasterization = Rasterization.getInstance();
    private final Deletion deletion = Deletion.getInstance();
    private final LightFactory lightFactory = new LightFactory();
    private Camera activeCamera;
    private Camera lightCamera;
    private int numberCamera = 0;
    @FXML
    private ComboBox<Camera> camerasComboBox = new ComboBox<>();
    @FXML
    private ComboBox<Model> modelsComboBox = new ComboBox<>();
    @FXML
    private CheckMenuItem showTextureCheck;
    private HashMap<Model, Model> originalModels = new HashMap<>();
    private final RenderEngine renderEngine = new RenderEngine();
    private Timeline timeline;
    @FXML
    private ColorPicker modelColor;

    @FXML
    private ColorPicker backgroundColor;

    @FXML
    private ColorPicker specularColor;

    @FXML
    private ColorPicker reflectionColor;
    @FXML
    private RadioMenuItem noMeshRadioMenuItem;

    @FXML
    private RadioMenuItem selectionVerticesRadioMenuItem;

    @FXML
    private RadioMenuItem selectionPolygonsRadioMenuItem;

    private int colorToInt(Color c) {
        int alpha = 255;
        int red = (int) (c.getRed() * 255);
        int green = (int) (c.getGreen() * 255);
        int blue = (int) (c.getBlue() * 255);
        return alpha << 24 | red << 16 | green << 8 | blue;
    }

    private Color intToColor(int argb) {
        int b = (argb) & 0xFF;
        int g = (argb >> 8) & 0xFF;
        int r = (argb >> 16) & 0xFF;
        int a = (argb >> 24) & 0xFF;
        return new Color((float) r / 255, (float) g / 255, (float) b / 255, (float) a / 255);
    }

    @FXML
    private void initialize() {

        makeNewCamera();

        newCamera.setOnAction(event -> makeNewCamera());

        camerasComboBox.setOnAction(event -> activeCamera = camerasComboBox.getValue());

        modelsComboBox.setOnAction(event -> selectModel());

        setCameraLight.setOnAction(event -> lightCamera = activeCamera);

        delCamera.setOnAction(event -> {
            if (camerasComboBox.getItems().size() == 1) {
                makeNewCamera();
                camerasComboBox.getItems().remove(0);
            } else if (camerasComboBox.getItems().get(0) == camerasComboBox.getValue()) {
                activeCamera = camerasComboBox.getItems().get(1);
                lightCamera = activeCamera;
                camerasComboBox.setValue(activeCamera);
                camerasComboBox.getItems().remove(0);
            } else {
                Camera oldCamera = camerasComboBox.getValue();
                activeCamera = camerasComboBox.getItems().get(0);
                lightCamera = activeCamera;
                camerasComboBox.getItems().remove(oldCamera);
                camerasComboBox.setValue(activeCamera);

            }
        });

        modelColor.setOnAction(event -> {
            if (activeModel != null) {
                activeModel.setSelfColor(colorToInt(modelColor.getValue()));
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Проблемка");
                alert.setHeaderText("Чтобы изменить сначала загрузите модель");
                alert.showAndWait();
            }

        });

        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> imageCanvas.setFitWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> imageCanvas.setFitHeight(newValue.doubleValue()));

        if (imageCanvas != null) {
            imageCanvas.setOnMouseMoved(event -> activeCamera.handleMouseInput(event.getX(), event.getY(), false, false));
            imageCanvas.setOnMouseDragged(event -> activeCamera.handleMouseInput(event.getX(), event.getY(), event.isPrimaryButtonDown(), event.isSecondaryButtonDown()));
            imageCanvas.setOnScroll(event2 -> {
                activeCamera.mouseDeltaY = event2.getDeltaY();
                activeCamera.handleMouseInput(event2.getX(), event2.getY(), false, false);
            });
            imageCanvas.setOnMouseClicked(event -> {
                switch (event.getButton()) {
                    case PRIMARY -> handlePrimaryClick(event);
                    case SECONDARY -> handleRightClick(event);
                }
            });

            rotateButton.setOnAction(event -> {

                if (activeModel != null) {
                    isAutoRotate = !isAutoRotate;
                    if (!isAutoRotate) {
                        lastTime = 0;
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Проблемка");
                    alert.setHeaderText("Чтобы вращать сначала загрузите модель");
                    alert.showAndWait();
                }
            });

            affButton.setOnAction(event -> {

                if (activeModel != null) {
                    applyTransformations(parseTextField(SxField, false), parseTextField(SyField, false), parseTextField(SzField, false), parseTextField(RxField, true), parseTextField(RyField, true), parseTextField(RzField, true), parseTextField(TxField, true), parseTextField(TyField, true), parseTextField(TzField, true));
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Проблемка");
                    alert.setHeaderText("Чтобы изменять сначала загрузите модель");
                    alert.showAndWait();
                }
            });
        }

        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);

        KeyFrame frame = new KeyFrame(Duration.millis(RELOAD_MILLISECONDS), event -> {

            double width = imageCanvas.getFitWidth();
            double height = imageCanvas.getFitHeight();

            activeCamera.setAspectRatio((float) (height / width));

            rasterization.clearScreen((int) imageCanvas.getFitWidth(), (int) imageCanvas.getFitHeight(), colorToInt(backgroundColor.getValue()));
            rasterization.setCoordinateLight(lightCamera.getPosition());
            rasterization.setCoordinateEyes(activeCamera.getPosition());
            if (activeModel != null) {
                activeModel.setSelfColor(colorToInt(modelColor.getValue()));
                modelStatistic.setText(String.format("Модель: %d вершин, %d треугольников", activeModel.vertices.size(), activeModel.polygons.size()));
                setChecks();
            }
            lightFactory.setSpecColor(colorToInt(specularColor.getValue()));
            lightFactory.setReflectionColor(colorToInt(reflectionColor.getValue()));
            int a = 0;
            int b = 0;

            for (Model model : modelsComboBox.getItems()) {
                rasterization.setTexture(model.getTexture());
                renderEngine.setRender(model.getRenderType());
                renderEngine.render(activeCamera, model, (int) width, (int) height);
                a += model.vertices.size();
                b += model.polygons.size();
            }

            imageCanvas.setImage(rasterization.paint());
            log.setSameLevel(System.Logger.Level.OFF);

            worldStatistic.setText(String.format("Сцена: %d вершин, %d треугольников", a, b));

            if (isAutoRotate) {
                if (lastTime != 0) {
                    final double elapsedSeconds = (System.nanoTime() - lastTime) / 1e9;

                    final float rotateSpeed = 10;
                    float rotateAngle = (float) (elapsedSeconds * rotateSpeed);

                    applyTransformations(1, 1, 1, 0, rotateAngle, 0, 0, 0, 0);
                }
                lastTime = System.nanoTime();
            } else {
                lastTime = 0;
            }
        });

        timeline.getKeyFrames().add(frame);
        timeline.play();
    }

    private void makeNewCamera() {

        numberCamera++;
        Camera camera = new Camera(numberCamera, new Vector3D(0, 0, 100), new Vector3D(0, 0, 0), 1.0F, 1, -20, 20);
        camerasComboBox.getItems().add(camera);
        camerasComboBox.setValue(camera);
        activeCamera = camera;
        lightCamera = camera;

    }

    private void handlePrimaryClick(MouseEvent event) {
        if (modelsComboBox.getItems().size() != 1) {
            return;
        }
        if (activeModel.getRenderType() == RenderType.SELECTION_VERTICES) {
            rasterization.chooseVertex((int) event.getX(), (int) event.getY());
        }
        if (activeModel.getRenderType() == RenderType.SELECTION_POLYGONS) {
            rasterization.choosePolygon((int) event.getX(), (int) event.getY());
        }
    }

    private void handleRightClick(MouseEvent event) {
        if (modelsComboBox.getItems().size() != 1) {
            return;
        }
        if (activeModel.getRenderType() == RenderType.SELECTION_VERTICES) {
            rasterization.cancelChooseVertex((int) event.getX(), (int) event.getY());
        }
        if (activeModel.getRenderType() == RenderType.SELECTION_POLYGONS) {
            rasterization.cancelChoosePolygon((int) event.getX(), (int) event.getY());
        }
    }

    private void applyTransformations(float scaleX, float scaleY, float scaleZ, float rotateX, float rotateY, float rotateZ, float translateX, float translateY, float translateZ) {

        affineTransform.setScaleX(scaleX);
        affineTransform.setScaleY(scaleY);
        affineTransform.setScaleZ(scaleZ);
        affineTransform.setRotationX(rotateX);
        affineTransform.setRotationY(rotateY);
        affineTransform.setRotationZ(rotateZ);
        affineTransform.setTranslationX(translateX);
        affineTransform.setTranslationY(translateY);
        affineTransform.setTranslationZ(translateZ);
        activeModel = affineTransform.transformModel(activeModel);
    }

    private void applyTransformations2(float scaleX, float scaleY, float scaleZ, float rotateX, float rotateY, float rotateZ, float translateX, float translateY, float translateZ) {

        affineTransform.setScaleX(scaleX);
        affineTransform.setScaleY(scaleY);
        affineTransform.setScaleZ(scaleZ);
        affineTransform.setRotationX(rotateX);
        affineTransform.setRotationY(rotateY);
        affineTransform.setRotationZ(rotateZ);
        affineTransform.setTranslationX(translateX);
        affineTransform.setTranslationY(translateY);
        affineTransform.setTranslationZ(translateZ);
        renderEngine.setModelMatrix(affineTransform.getRotationMatrix());
    }

    @FXML
    public void saveModelInFile() {

        if (activeModel != null) {
            saveModel(activeModel);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Проблемка");
            alert.setHeaderText("Сохранять нечего");

            alert.showAndWait();
        }

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

    @FXML
    public void returnOriginalModel() {

        if (activeModel != null) {
            Model newModel = originalModels.get(activeModel).copy();
            Collections.replaceAll(modelsComboBox.getItems(), activeModel, newModel);
            originalModels.remove(activeModel);
            activeModel = newModel;
            modelsComboBox.setValue(newModel);
            originalModels.put(activeModel, activeModel.copy());
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Проблемка");
            alert.setHeaderText("Пока не открыто ни одной модели");

            alert.showAndWait();
        }
    }

    @FXML
    public void closeAllModels() {
        originalModels = new HashMap<>();
        activeModel = null;
        modelsComboBox.getItems().clear();
        isAutoRotate = false;
    }

    @FXML
    public void closeModel() {
        isAutoRotate = false;
        if (modelsComboBox.getItems().isEmpty()) {
            return;
        }
        if (modelsComboBox.getItems().size() == 1) {
            closeAllModels();
        } else if (modelsComboBox.getItems().get(0) == activeModel) {
            originalModels.remove(activeModel);
            activeModel = modelsComboBox.getItems().get(1);
            modelsComboBox.setValue(activeModel);
            modelsComboBox.getItems().remove(0);
        } else {
            originalModels.remove(activeModel);
            Model oldModel = activeModel;
            activeModel = modelsComboBox.getItems().get(0);
            modelsComboBox.getItems().remove(oldModel);
            modelsComboBox.setValue(activeModel);

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

    private void loadModel(Path fileName, String name) {
        rasterization.resetChooseVertices();
        rasterization.resetChosenPolygons();
        deletion.resetStack();
        try {
            String fileContent = Files.readString(fileName);

            Model default_model = ObjReader.read(fileContent);
            Model normal_model = new Normalization(default_model).recalculateNormals();
            activeModel = new Triangulation(normal_model).triangulate();
            activeModel.setName(name);
            modelsComboBox.getItems().add(activeModel);
            modelsComboBox.setValue(activeModel);
            originalModels.put(activeModel, activeModel.copy());
            showTextureCheck.setSelected(false);
            noMeshRadioMenuItem.setSelected(true);
            selectionVerticesRadioMenuItem.setSelected(false);
            selectionPolygonsRadioMenuItem.setSelected(false);
        } catch (Exception exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Проблемка");
            alert.setHeaderText("Эту модель невозможно прочитать");

            alert.showAndWait();
        }
    }

    private void startRotationAnimation() {
        if (!isAutoRotate) {
            isAutoRotate = true;
        } else {
            isAutoRotate = false;
            lastTime = 0;
        }
        AnimationTimer rotationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (isAutoRotate) {
                    if (lastTime != 0) {
                        final double elapsedSeconds = (now - lastTime) / 1e9;

                        final float rotateSpeed = 10;
                        float rotateAngle = (float) (elapsedSeconds * rotateSpeed);

                        applyTransformations2(1, 1, 1, 0, rotateAngle, 0, 0, 0, 0);

                    }
                    lastTime = now;
                } else {
                    lastTime = 0;
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

        File file = fileChooser.showOpenDialog((Stage) imageCanvas.getScene().getWindow());
        if (file == null) {
            return;
        }

        loadModel(Path.of(file.getAbsolutePath()), file.getAbsolutePath());
    }

    @FXML
    private void onOpenTextureMenuItemClick() {
        if (activeModel != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Изображение", List.of("*.png", "*.bmp", "*.jpg", "*.jpeg")));
            fileChooser.setTitle("Выбрать текстуру");

            File file = fileChooser.showOpenDialog((Stage) imageCanvas.getScene().getWindow());
            if (file == null) {
                return;
            }

            activeModel.setTexture(new Texture(file));
        } else {
            alertNoModelForTexture();
        }
    }

    @FXML
    private void resetTextureMenuItemClick() {
        if (activeModel != null) {
            activeModel.reverseTexture();
            showTextureCheck.setSelected(activeModel.isShowTexture());
        } else {
            showTextureCheck.setSelected(false);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Проблемка");
            alert.setHeaderText("Чтобы показывать и скрывать текстуры сначала загрузите модель");

            alert.showAndWait();
        }
    }

    private void alertBadCollection() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Проблемка");
        alert.setHeaderText("Невозможно загрузить файл из коллекции. Возможно, программа установлена неправильно.");
        alert.showAndWait();
    }

    private void alertNoModelForTexture() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Проблемка");
        alert.setHeaderText("Чтобы выбрать текстуру сначала загрузите модель");
        alert.showAndWait();
    }

    @FXML
    private void loadCube() {
        try {
            File model = new File(Objects.requireNonNull(GuiController.class.getResource("collection/cube.obj")).toURI());
            loadModel(Path.of(model.getPath()), "Куб");
        } catch (Exception e) {
            alertBadCollection();
            return;
        }
        try {
            File texture = new File(Objects.requireNonNull(GuiController.class.getResource("collection/cube.png")).toURI());
            activeModel.setTexture(new Texture(texture));
        } catch (Exception e) {
            alertBadCollection();
        }
    }

    @FXML
    private void loadSphere() {
        try {
            File model = new File(Objects.requireNonNull(GuiController.class.getResource("collection/sphere.obj")).toURI());
            loadModel(Path.of(model.getPath()), "Шар");
        } catch (Exception e) {
            alertBadCollection();
            return;
        }
        try {
            File texture = new File(Objects.requireNonNull(GuiController.class.getResource("collection/sphere.png")).toURI());
            activeModel.setTexture(new Texture(texture));
        } catch (Exception e) {
            alertBadCollection();
        }
    }

    @FXML
    private void loadCone() {
        try {
            File model = new File(Objects.requireNonNull(GuiController.class.getResource("collection/cone.obj")).toURI());
            loadModel(Path.of(model.getPath()), "Конус");
        } catch (Exception e) {
            alertBadCollection();
            return;
        }
        try {
            File texture = new File(Objects.requireNonNull(GuiController.class.getResource("collection/cone.png")).toURI());
            activeModel.setTexture(new Texture(texture));
        } catch (Exception e) {
            alertBadCollection();
        }
    }

    @FXML
    private void loadTorus() {
        try {
            File model = new File(Objects.requireNonNull(GuiController.class.getResource("collection/torus.obj")).toURI());
            loadModel(Path.of(model.getPath()), "Тор");
        } catch (Exception e) {
            alertBadCollection();
            return;
        }
        try {
            File texture = new File(Objects.requireNonNull(GuiController.class.getResource("collection/torus.png")).toURI());
            activeModel.setTexture(new Texture(texture));
        } catch (Exception e) {
            alertBadCollection();
        }
    }

    @FXML
    private void loadTexture3() {
        if (activeModel != null) {
            try {
                File texture = new File(Objects.requireNonNull(GuiController.class.getResource("collection/texture3.png")).toURI());
                activeModel.setTexture(new Texture(texture));
            } catch (Exception e) {
                alertBadCollection();
            }
        } else {
            alertNoModelForTexture();
        }
    }

    @FXML
    private void loadTexture2() {
        if (activeModel != null) {
            try {
                File texture = new File(Objects.requireNonNull(GuiController.class.getResource("collection/texture2.png")).toURI());
                activeModel.setTexture(new Texture(texture));
            } catch (Exception e) {
                alertBadCollection();
            }
        } else {
            alertNoModelForTexture();
        }
    }

    @FXML
    private void loadTexture1() {
        if (activeModel != null) {
            try {
                File texture = new File(Objects.requireNonNull(GuiController.class.getResource("collection/texture1.png")).toURI());
                activeModel.setTexture(new Texture(texture));
            } catch (Exception e) {
                alertBadCollection();
            }
        } else {
            alertNoModelForTexture();
        }
    }

    @FXML
    private void setPrimeLight() {
        rasterization.setLight(lightFactory.createLight(LightType.PRIMARY));
    }

    @FXML
    private void setPolygonLight() {
        rasterization.setLight(lightFactory.createLight(LightType.POLYGON));
    }

    @FXML
    private void setSpecularLight() {
        rasterization.setLight(lightFactory.createLight(LightType.SPECULAR));
    }

    @FXML
    private void setBorderLight() {
        rasterization.setLight(lightFactory.createLight(LightType.BORDER));
    }

    @FXML
    private void setNoneLight() {
        rasterization.setLight(lightFactory.createLight(LightType.NONE));
    }

    @FXML
    private void setNoMeshRender() {
        activeModel.setRenderNoMesh();
        selectionPolygonsRadioMenuItem.setSelected(false);
        selectionVerticesRadioMenuItem.setSelected(false);
        noMeshRadioMenuItem.setSelected(true);
    }

    @FXML
    private void setRenderSelectionVertices() {
        activeModel.setRenderType(RenderType.SELECTION_VERTICES);
        selectionPolygonsRadioMenuItem.setSelected(false);
        selectionVerticesRadioMenuItem.setSelected(true);
        noMeshRadioMenuItem.setSelected(false);
    }

    @FXML
    private void setRenderSelectionPolygons() {
        activeModel.setRenderType(RenderType.SELECTION_POLYGONS);
        selectionPolygonsRadioMenuItem.setSelected(true);
        selectionVerticesRadioMenuItem.setSelected(false);
        noMeshRadioMenuItem.setSelected(false);
    }

    @FXML
    public void activeTraceLog() {
        log.setSameLevel(System.Logger.Level.TRACE);
    }

    @FXML
    public void deleteVertices() {
        if (activeModel != null) {
            deletion.deleteVerteces(activeModel, rasterization.getChosenVertexes());
            rasterization.resetChooseVertices();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Проблемка");
            alert.setHeaderText("Чтобы удалять вершины загрузите модель");

            alert.showAndWait();
        }
    }

    @FXML
    public void deletePolygons() {

        if (activeModel != null) {
            deletion.deletePolygons(activeModel, rasterization.getChosenPolygons());
            rasterization.resetChosenPolygons();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Проблемка");
            alert.setHeaderText("Чтобы удалять сначала загрузите модель");

            alert.showAndWait();
        }
    }

    @FXML
    public void cancelDeletion() {

        if (activeModel != null) {
            Model newModel = deletion.returnOldModel(activeModel);
            Collections.replaceAll(modelsComboBox.getItems(), activeModel, newModel);
            originalModels.remove(activeModel);
            activeModel = newModel;
            modelsComboBox.setValue(newModel);
            originalModels.put(activeModel, activeModel.copy());
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Проблемка");
            alert.setHeaderText("Пока не открыто ни одной модели");

            alert.showAndWait();
        }
    }

    private void selectModel() {

        activeModel = modelsComboBox.getValue();
        if (activeModel != null) {
            modelColor.setValue(intToColor(activeModel.getSelfColor()));
        }
    }

    private void setChecks() {
        showTextureCheck.setSelected(activeModel.isShowTexture());

        switch (activeModel.getRenderType()) {
            case TEXTURE, SELF_COLORED -> {
                noMeshRadioMenuItem.setSelected(true);
                selectionVerticesRadioMenuItem.setSelected(false);
                selectionPolygonsRadioMenuItem.setSelected(false);
            }
            case SELECTION_POLYGONS -> {
                noMeshRadioMenuItem.setSelected(false);
                selectionVerticesRadioMenuItem.setSelected(false);
                selectionPolygonsRadioMenuItem.setSelected(true);
            }
            case SELECTION_VERTICES -> {
                noMeshRadioMenuItem.setSelected(false);
                selectionVerticesRadioMenuItem.setSelected(true);
                selectionPolygonsRadioMenuItem.setSelected(false);
            }
            default -> {
            }
        }
    }
}