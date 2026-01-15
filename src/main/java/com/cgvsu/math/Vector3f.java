package com.cgvsu.math;


import com.cgvsu.exceptions.MathException;

public class Vector3f {
    private float x;
    private float y;
    private float z;

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3f() {
        this(0, 0, 0);
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public float getZ() { return z; }

    // Сложение
    public Vector3f add(Vector3f other) {
        return new Vector3f(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    // Вычитание
    public Vector3f subtract(Vector3f other) {
        return new Vector3f(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    // Умножение на скаляр
    public Vector3f multiply(float scalar) {
        return new Vector3f(this.x * scalar, this.y * scalar, this.z * scalar);
    }

    // Деление на скаляр
    public Vector3f divide(float scalar) {
        if (Math.abs(scalar) < 1e-10) {
            throw new MathException("Деление на ноль");
        }
        return new Vector3f(this.x / scalar, this.y / scalar, this.z / scalar);
    }

    // Длина вектора
    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
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
    }

    // Возвращает новый нормализованный вектор
    public Vector3f normalized() {
        float len = length();
        if (len < 1e-10) {
            throw new MathException("Не удается нормализовать нулевой вектор");
        }
        return new Vector3f(
                this.x / len,
                this.y / len,
                this.z / len
        );
    }



    // Скалярное произведение
    public float dot(Vector3f other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    // Векторное произведение
    public Vector3f cross(Vector3f other) {
        float newX = this.y * other.z - this.z * other.y;
        float newY = this.z * other.x - this.x * other.z;
        float newZ = this.x * other.y - this.y * other.x;
        return new Vector3f(newX, newY, newZ);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vector3f Vector3f = (Vector3f) obj;
        return Math.abs(x - Vector3f.x) < 1e-10 &&
                Math.abs(y - Vector3f.y) < 1e-10 &&
                Math.abs(z - Vector3f.z) < 1e-10;
    }

    @Override
    public String toString() {
        return String.format("Vector3f(%.4f, %.4f, %.4f)", x, y, z);
    }
}
