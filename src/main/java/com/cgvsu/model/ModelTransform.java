package com.cgvsu.model;

import com.cgvsu.math.Vector3f;

public class ModelTransform {
    private Vector3f translation;
    private Vector3f rotation;
    private Vector3f scale;

    public ModelTransform() {
        this.translation = new Vector3f(0, 0, 0);
        this.rotation = new Vector3f(0, 0, 0);
        this.scale = new Vector3f(1, 1, 1);
    }


    public Vector3f getTranslation() { return translation; }
    public void setTranslation(Vector3f translation) { this.translation = translation; }

    public Vector3f getRotation() { return rotation; }
    public void setRotation(Vector3f rotation) { this.rotation = rotation; }

    public Vector3f getScale() { return scale; }
    public void setScale(Vector3f scale) { this.scale = scale; }


}