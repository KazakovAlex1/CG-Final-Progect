package com.cgvsu.math;

import com.cgvsu.exceptions.MathException;

public class Vector4f {
    private float x;
    private float y;
    private float z;
    private float w;

    public Vector4f(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vector4f() {
        this(0, 0, 0, 0);
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public float getZ() { return z; }
    public float getW() { return w; }

    // Сложение
    public Vector4f add(Vector4f other) {
        return new Vector4f(
                this.x + other.x,
                this.y + other.y,
                this.z + other.z,
                this.w + other.w
        );
    }

    // Вычитание
    public Vector4f subtract(Vector4f other) {
        return new Vector4f(
                this.x - other.x,
                this.y - other.y,
                this.z - other.z,
                this.w - other.w
        );
    }

    // Умножение на скаляр
    public Vector4f multiply(float scalar) {
        return new Vector4f(
                this.x * scalar,
                this.y * scalar,
                this.z * scalar,
                this.w * scalar
        );
    }

    // Деление на скаляр
    public Vector4f divide(float scalar) {
        if (Math.abs(scalar) < 1e-10) {
            throw new MathException("Деление на ноль");
        }
        return new Vector4f(
                this.x / scalar,
                this.y / scalar,
                this.z / scalar,
                this.w / scalar
        );
    }

    // Длина вектора
    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z + w * w);
    }

    // Нормализация
    public void normalize() {
        float len = length();
        if (len < 1e-10) {
            throw new MathException("Не удается нормализовать нулевой вектор");
        }
        this.x /= len;
        this.y /= len;
        this.z /= len;
        this.w /= len;
    }

    // Возвращает новый нормализованный вектор
    public Vector4f normalized() {
        float len = length();
        if (len < 1e-10) {
            throw new MathException("Не удается нормализовать нулевой вектор");
        }
        return new Vector4f(
                this.x / len,
                this.y / len,
                this.z / len,
                this.w / len
        );
    }

    // Скалярное произведение
    public float dot(Vector4f other) {
        return this.x * other.x + this.y * other.y +
                this.z * other.z + this.w * other.w;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vector4f Vector4f = (Vector4f) obj;
        return Math.abs(x - Vector4f.x) < 1e-10 &&
                Math.abs(y - Vector4f.y) < 1e-10 &&
                Math.abs(z - Vector4f.z) < 1e-10 &&
                Math.abs(w - Vector4f.w) < 1e-10;
    }

    @Override
    public String toString() {
        return String.format("Vector4f(%.4f, %.4f, %.4f, %.4f)", x, y, z, w);
    }
}