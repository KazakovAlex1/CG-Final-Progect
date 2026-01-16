package com.cgvsu;

import com.cgvsu.render_engine.RenderEngine;
import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Model;
import com.cgvsu.objreader.ObjReader;
import com.cgvsu.render_engine.Camera;

import javafx.fxml.FXML;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.io.File;

public class GuiController {

    final private float TRANSLATION = 2.5F;
    final private float STEP = 0.5F;

    @FXML
    AnchorPane anchorPane;

    @FXML
    private Canvas canvas;

    @FXML
    private Label translateXLabel;
    @FXML
    private Label translateYLabel;
    @FXML
    private Label translateZLabel;

    @FXML
    private Label rotateXLabel;
    @FXML
    private Label rotateYLabel;
    @FXML
    private Label rotateZLabel;

    @FXML
    private Label scaleXLabel;
    @FXML
    private Label scaleYLabel;
    @FXML
    private Label scaleZLabel;

    @FXML
    private Button resetTransformButton;

    @FXML
    private Button translateXMinusBtn, translateXPlusBtn;
    @FXML
    private Button translateYMinusBtn, translateYPlusBtn;
    @FXML
    private Button translateZMinusBtn, translateZPlusBtn;

    @FXML
    private Button rotateXMinusBtn, rotateXPlusBtn;
    @FXML
    private Button rotateYMinusBtn, rotateYPlusBtn;
    @FXML
    private Button rotateZMinusBtn, rotateZPlusBtn;

    @FXML
    private Button scaleXMinusBtn, scaleXPlusBtn;
    @FXML
    private Button scaleYMinusBtn, scaleYPlusBtn;
    @FXML
    private Button scaleZMinusBtn, scaleZPlusBtn;

    private float translateX = 0.0f;
    private float translateY = 0.0f;
    private float translateZ = 0.0f;

    private float rotateX = 0.0f;
    private float rotateY = 0.0f;
    private float rotateZ = 0.0f;

    private float scaleX = 1.0f;
    private float scaleY = 1.0f;
    private float scaleZ = 1.0f;

    private Model mesh = null;

    private Camera camera = new Camera(
            new Vector3f(0, 0, 100),
            new Vector3f(0, 0, 0),
            1.0F, 1F, 0.01F, 100F);

    private Timeline timeline;

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        updateAllLabels();

        setTransformationControlsEnabled(false);

        setupRenderingTimeline();
    }

    private void updateAllLabels() {
        translateXLabel.setText(String.format("%.2f", translateX));
        translateYLabel.setText(String.format("%.2f", translateY));
        translateZLabel.setText(String.format("%.2f", translateZ));

        rotateXLabel.setText(String.format("%.2f", rotateX));
        rotateYLabel.setText(String.format("%.2f", rotateY));
        rotateZLabel.setText(String.format("%.2f", rotateZ));

        scaleXLabel.setText(String.format("%.2f", scaleX));
        scaleYLabel.setText(String.format("%.2f", scaleY));
        scaleZLabel.setText(String.format("%.2f", scaleZ));
    }

    private void updateModelTransform() {
        if (mesh != null && mesh.getTransform() != null) {
            mesh.getTransform().setTranslation(new Vector3f(translateX, translateY, translateZ));
            mesh.getTransform().setRotation(new Vector3f(rotateX, rotateY, rotateZ));
            mesh.getTransform().setScale(new Vector3f(scaleX, scaleY, scaleZ));
        }
    }

    private void setTransformationControlsEnabled(boolean enabled) {
        translateXMinusBtn.setDisable(!enabled);
        translateXPlusBtn.setDisable(!enabled);
        translateYMinusBtn.setDisable(!enabled);
        translateYPlusBtn.setDisable(!enabled);
        translateZMinusBtn.setDisable(!enabled);
        translateZPlusBtn.setDisable(!enabled);

        rotateXMinusBtn.setDisable(!enabled);
        rotateXPlusBtn.setDisable(!enabled);
        rotateYMinusBtn.setDisable(!enabled);
        rotateYPlusBtn.setDisable(!enabled);
        rotateZMinusBtn.setDisable(!enabled);
        rotateZPlusBtn.setDisable(!enabled);

        scaleXMinusBtn.setDisable(!enabled);
        scaleXPlusBtn.setDisable(!enabled);
        scaleYMinusBtn.setDisable(!enabled);
        scaleYPlusBtn.setDisable(!enabled);
        scaleZMinusBtn.setDisable(!enabled);
        scaleZPlusBtn.setDisable(!enabled);

        resetTransformButton.setDisable(!enabled);
    }

    private void setupRenderingTimeline() {
        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);

        KeyFrame frame = new KeyFrame(Duration.millis(15), event -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();

            canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
            camera.setAspectRatio((float) (width / height));

            if (mesh != null) {
                RenderEngine.render(canvas.getGraphicsContext2D(), camera, mesh, (int) width, (int) height);
            }
        });

        timeline.getKeyFrames().add(frame);
        timeline.play();
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

        Path fileName = Path.of(file.getAbsolutePath());

        try {
            String fileContent = Files.readString(fileName);
            mesh = ObjReader.read(fileContent);

            setTransformationControlsEnabled(true);

            resetTransformButton.fire();

        } catch (IOException exception) {
            showAlert("Ошибка загрузки", "Не удалось загрузить модель: " + exception.getMessage());
            exception.printStackTrace();
        } catch (Exception e) {
            showAlert("Ошибка", "Ошибка при чтении файла: " + e.getMessage());
        }
    }

    @FXML
    private void onResetTransformButtonClick() {
        translateX = 0.0f;
        translateY = 0.0f;
        translateZ = 0.0f;

        rotateX = 0.0f;
        rotateY = 0.0f;
        rotateZ = 0.0f;

        scaleX = 1.0f;
        scaleY = 1.0f;
        scaleZ = 1.0f;

        updateAllLabels();
        updateModelTransform();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void onTranslateXMinus() { translateX -= STEP; updateAllLabels(); updateModelTransform(); }
    @FXML
    private void onTranslateXPlus() { translateX += STEP; updateAllLabels(); updateModelTransform(); }
    @FXML
    private void onTranslateYMinus() { translateY -= STEP; updateAllLabels(); updateModelTransform(); }
    @FXML
    private void onTranslateYPlus() { translateY += STEP; updateAllLabels(); updateModelTransform(); }
    @FXML
    private void onTranslateZMinus() { translateZ -= STEP; updateAllLabels(); updateModelTransform(); }
    @FXML
    private void onTranslateZPlus() { translateZ += STEP; updateAllLabels(); updateModelTransform(); }

    @FXML
    private void onRotateXMinus() { rotateX -= STEP; updateAllLabels(); updateModelTransform(); }
    @FXML
    private void onRotateXPlus() { rotateX += STEP; updateAllLabels(); updateModelTransform(); }
    @FXML
    private void onRotateYMinus() { rotateY -= STEP; updateAllLabels(); updateModelTransform(); }
    @FXML
    private void onRotateYPlus() { rotateY += STEP; updateAllLabels(); updateModelTransform(); }
    @FXML
    private void onRotateZMinus() { rotateZ -= STEP; updateAllLabels(); updateModelTransform(); }
    @FXML
    private void onRotateZPlus() { rotateZ += STEP; updateAllLabels(); updateModelTransform(); }

    @FXML
    private void onScaleXMinus() { scaleX -= STEP; if (scaleX < 0.1f) scaleX = 0.1f; updateAllLabels(); updateModelTransform(); }
    @FXML
    private void onScaleXPlus() { scaleX += STEP; updateAllLabels(); updateModelTransform(); }
    @FXML
    private void onScaleYMinus() { scaleY -= STEP; if (scaleY < 0.1f) scaleY = 0.1f; updateAllLabels(); updateModelTransform(); }
    @FXML
    private void onScaleYPlus() { scaleY += STEP; updateAllLabels(); updateModelTransform(); }
    @FXML
    private void onScaleZMinus() { scaleZ -= STEP; if (scaleZ < 0.1f) scaleZ = 0.1f; updateAllLabels(); updateModelTransform(); }
    @FXML
    private void onScaleZPlus() { scaleZ += STEP; updateAllLabels(); updateModelTransform(); }

    @FXML
    public void handleCameraForward(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, 0, -TRANSLATION));
    }

    @FXML
    public void handleCameraBackward(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, 0, TRANSLATION));
    }

    @FXML
    public void handleCameraLeft(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(TRANSLATION, 0, 0));
    }

    @FXML
    public void handleCameraRight(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(-TRANSLATION, 0, 0));
    }

    @FXML
    public void handleCameraUp(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, TRANSLATION, 0));
    }

    @FXML
    public void handleCameraDown(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, -TRANSLATION, 0));
    }
}