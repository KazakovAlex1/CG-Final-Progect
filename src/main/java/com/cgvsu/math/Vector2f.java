package com.cgvsu.math;


import com.cgvsu.exceptions.MathException;

public class Vector2f {
    private float x;
    private float y;

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2f() {
        this(0, 0);
    }

    public float getX() { return x; }
    public float getY() { return y; }

    /***
     * Сложение двух векторов
     * @param other другой вектор для сложения
     * @return Новый вектор -- результат сложения
     */
    public Vector2f add(Vector2f other) {
        return new Vector2f(this.x + other.x, this.y + other.y);
    }

    // Вычитание
    public Vector2f subtract(Vector2f other) {
        return new Vector2f(this.x - other.x, this.y - other.y);
    }

    // Умножение на скаляр
    public Vector2f multiply(float scalar) {
        return new Vector2f(this.x * scalar, this.y * scalar);
    }

    // Деление на скаляр
    public Vector2f divide(float scalar) {
        if (Math.abs(scalar) < 1e-10) {
            throw new MathException("Деление на ноль");
        }
        return new Vector2f(this.x / scalar, this.y / scalar);
    }

    // Длина вектора
    public float length() {
        return (float) Math.hypot(x, y);
    }

    // Нормализация
    public void normalize() {
        float len = length();
        if (len < 1e-10) {
            throw new MathException("Не удается нормализовать нулевой вектор");
        }
        this.x /= len;
        this.y /= len;
    }

    // Возвращает новый нормализованный вектор
    public Vector2f normalized() {
        float len = length();
        if (len < 1e-10) {
            throw new MathException("Не удается нормализовать нулевой вектор");
        }
        return new Vector2f(this.x / len, this.y / len);
    }


    // Скалярное произведение
    public float dot(Vector2f other) {
        return this.x * other.x + this.y * other.y;
    }

    // Проверка на равенство (для тестов)
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vector2f Vector2f = (Vector2f) obj;
        return Math.abs(x - Vector2f.x) < 1e-10 &&
                Math.abs(y - Vector2f.y) < 1e-10;
    }

    @Override
    public String toString() {
        return String.format("Vector2f(%.4f, %.4f)", x, y);
    }
}
