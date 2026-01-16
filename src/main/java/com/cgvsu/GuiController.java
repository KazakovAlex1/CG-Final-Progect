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
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.File;

public class GuiController {

    private static final float TRANSLATION = 2.5F;
    private static final float STEP = 0.5F;

    /* ===================== FXML ===================== */

    @FXML private AnchorPane anchorPane;
    @FXML private Canvas canvas;

    @FXML private Label translateXLabel, translateYLabel, translateZLabel;
    @FXML private Label rotateXLabel, rotateYLabel, rotateZLabel;
    @FXML private Label scaleXLabel, scaleYLabel, scaleZLabel;

    @FXML private Button resetTransformButton;

    @FXML private Button translateXMinusBtn, translateXPlusBtn;
    @FXML private Button translateYMinusBtn, translateYPlusBtn;
    @FXML private Button translateZMinusBtn, translateZPlusBtn;

    @FXML private Button rotateXMinusBtn, rotateXPlusBtn;
    @FXML private Button rotateYMinusBtn, rotateYPlusBtn;
    @FXML private Button rotateZMinusBtn, rotateZPlusBtn;

    @FXML private Button scaleXMinusBtn, scaleXPlusBtn;
    @FXML private Button scaleYMinusBtn, scaleYPlusBtn;
    @FXML private Button scaleZMinusBtn, scaleZPlusBtn;

    /* ===================== STATE ===================== */

    private float translateX, translateY, translateZ;
    private float rotateX, rotateY, rotateZ;
    private float scaleX = 1, scaleY = 1, scaleZ = 1;

    private Model mesh;

    private final Camera camera = new Camera(
            new Vector3f(0, 0, 100),
            new Vector3f(0, 0, 0),
            1.0F, 1F, 0.01F, 100F
    );

    private Timeline timeline;

    /* ===================== INIT ===================== */

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty()
                .addListener((o, a, b) -> canvas.setWidth(b.doubleValue()));
        anchorPane.prefHeightProperty()
                .addListener((o, a, b) -> canvas.setHeight(b.doubleValue()));

        updateAllLabels();
        setTransformationControlsEnabled(false);
        setupRenderingTimeline();

        anchorPane.setFocusTraversable(true);
        anchorPane.requestFocus();
    }

    /* ===================== KEYBOARD ===================== */

    public void registerKeyboard(Scene scene) {
        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case W, UP -> camera.movePosition(new Vector3f(0, 0, -TRANSLATION));
                case S, DOWN -> camera.movePosition(new Vector3f(0, 0, TRANSLATION));
                case A, LEFT -> camera.movePosition(new Vector3f(TRANSLATION, 0, 0));
                case D, RIGHT -> camera.movePosition(new Vector3f(-TRANSLATION, 0, 0));
                case Q -> camera.movePosition(new Vector3f(0, TRANSLATION, 0));
                case E -> camera.movePosition(new Vector3f(0, -TRANSLATION, 0));
            }
        });
    }

    /* ===================== RENDER ===================== */

    private void setupRenderingTimeline() {
        timeline = new Timeline(new KeyFrame(Duration.millis(15), e -> {
            double w = canvas.getWidth();
            double h = canvas.getHeight();
            canvas.getGraphicsContext2D().clearRect(0, 0, w, h);
            camera.setAspectRatio((float) (w / h));
            if (mesh != null)
                RenderEngine.render(canvas.getGraphicsContext2D(), camera, mesh, (int) w, (int) h);
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    /* ===================== MODEL ===================== */

    @FXML
    private void onOpenModelMenuItemClick() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("OBJ", "*.obj"));
        File file = fc.showOpenDialog((Stage) canvas.getScene().getWindow());
        if (file == null) return;

        try {
            mesh = ObjReader.read(Files.readString(Path.of(file.getAbsolutePath())));
            setTransformationControlsEnabled(true);
            onResetTransformButtonClick();
        } catch (Exception ex) {
            showAlert("Ошибка", ex.getMessage());
        }
    }

    /* ===================== TRANSFORMS ===================== */

    private void updateModelTransform() {
        if (mesh == null) return;
        mesh.getTransform().setTranslation(new Vector3f(translateX, translateY, translateZ));
        mesh.getTransform().setRotation(new Vector3f(rotateX, rotateY, rotateZ));
        mesh.getTransform().setScale(new Vector3f(scaleX, scaleY, scaleZ));
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

    private void setTransformationControlsEnabled(boolean enabled) {
        for (Button b : new Button[]{
                translateXMinusBtn, translateXPlusBtn, translateYMinusBtn, translateYPlusBtn,
                translateZMinusBtn, translateZPlusBtn, rotateXMinusBtn, rotateXPlusBtn,
                rotateYMinusBtn, rotateYPlusBtn, rotateZMinusBtn, rotateZPlusBtn,
                scaleXMinusBtn, scaleXPlusBtn, scaleYMinusBtn, scaleYPlusBtn,
                scaleZMinusBtn, scaleZPlusBtn, resetTransformButton
        }) b.setDisable(!enabled);
    }

    /* ===================== BUTTON HANDLERS ===================== */

    @FXML private void onTranslateXMinus(){ translateX -= STEP; updateAllLabels(); updateModelTransform(); }
    @FXML private void onTranslateXPlus(){ translateX += STEP; updateAllLabels(); updateModelTransform(); }
    @FXML private void onTranslateYMinus(){ translateY -= STEP; updateAllLabels(); updateModelTransform(); }
    @FXML private void onTranslateYPlus(){ translateY += STEP; updateAllLabels(); updateModelTransform(); }
    @FXML private void onTranslateZMinus(){ translateZ -= STEP; updateAllLabels(); updateModelTransform(); }
    @FXML private void onTranslateZPlus(){ translateZ += STEP; updateAllLabels(); updateModelTransform(); }

    @FXML private void onRotateXMinus(){ rotateX -= STEP; updateAllLabels(); updateModelTransform(); }
    @FXML private void onRotateXPlus(){ rotateX += STEP; updateAllLabels(); updateModelTransform(); }
    @FXML private void onRotateYMinus(){ rotateY -= STEP; updateAllLabels(); updateModelTransform(); }
    @FXML private void onRotateYPlus(){ rotateY += STEP; updateAllLabels(); updateModelTransform(); }
    @FXML private void onRotateZMinus(){ rotateZ -= STEP; updateAllLabels(); updateModelTransform(); }
    @FXML private void onRotateZPlus(){ rotateZ += STEP; updateAllLabels(); updateModelTransform(); }

    @FXML private void onScaleXMinus(){ scaleX = Math.max(0.1f, scaleX - STEP); updateAllLabels(); updateModelTransform(); }
    @FXML private void onScaleXPlus(){ scaleX += STEP; updateAllLabels(); updateModelTransform(); }
    @FXML private void onScaleYMinus(){ scaleY = Math.max(0.1f, scaleY - STEP); updateAllLabels(); updateModelTransform(); }
    @FXML private void onScaleYPlus(){ scaleY += STEP; updateAllLabels(); updateModelTransform(); }
    @FXML private void onScaleZMinus(){ scaleZ = Math.max(0.1f, scaleZ - STEP); updateAllLabels(); updateModelTransform(); }
    @FXML private void onScaleZPlus(){ scaleZ += STEP; updateAllLabels(); updateModelTransform(); }

    @FXML
    private void onResetTransformButtonClick() {
        translateX = translateY = translateZ = 0;
        rotateX = rotateY = rotateZ = 0;
        scaleX = scaleY = scaleZ = 1;
        updateAllLabels();
        updateModelTransform();
    }

    private void showAlert(String t, String m) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(t);
        a.setHeaderText(null);
        a.setContentText(m);
        a.showAndWait();
    }

    @FXML
    private void handleCameraForward(ActionEvent event) {
        camera.movePosition(new Vector3f(0, 0, -TRANSLATION));
    }

    @FXML
    private void handleCameraBackward(ActionEvent event) {
        camera.movePosition(new Vector3f(0, 0, TRANSLATION));
    }

    @FXML
    private void handleCameraLeft(ActionEvent event) {
        camera.movePosition(new Vector3f(TRANSLATION, 0, 0));
    }

    @FXML
    private void handleCameraRight(ActionEvent event) {
        camera.movePosition(new Vector3f(-TRANSLATION, 0, 0));
    }

    @FXML
    private void handleCameraUp(ActionEvent event) {
        camera.movePosition(new Vector3f(0, TRANSLATION, 0));
    }

    @FXML
    private void handleCameraDown(ActionEvent event) {
        camera.movePosition(new Vector3f(0, -TRANSLATION, 0));
    }
}
