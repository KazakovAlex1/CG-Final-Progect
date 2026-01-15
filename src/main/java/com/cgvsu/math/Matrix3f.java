package com.cgvsu.math;

import com.cgvsu.exceptions.MathException;

/**
 * Класс {@code Matrix3f} представляет квадратную матрицу размером 3x3
 * и предоставляет основные операции линейной алгебры:
 * сложение, вычитание, умножение, транспонирование и вычисление определителя.
 */
public class Matrix3f {

    /**
     * Внутреннее представление матрицы в виде двумерного массива 3x3.
     */
    private final float[][] data;

    /**
     * Создаёт матрицу 3x3 на основе переданного двумерного массива.
     *
     * @param data двумерный массив размером 3x3
     * @throws MathException если размер массива не равен 3x3
     */
    public Matrix3f(float[][] data) {
        if (data.length != 3 || data[0].length != 3) {
            throw new MathException("Матрица должна быть размером 3x3");
        }
        this.data = new float[3][3];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(data[i], 0, this.data[i], 0, 3);
        }
    }

    /**
     * Создаёт единичную матрицу 3x3.
     */
    public Matrix3f() {
        this.data = new float[3][3];
        for (int i = 0; i < 3; i++) {
            data[i][i] = 1.0F;
        }
    }

    /**
     * Создаёт нулевую матрицу 3x3.
     *
     * @return нулевая матрица
     */
    public static Matrix3f zero() {
        return new Matrix3f(new float[3][3]);
    }

    /**
     * Создаёт единичную матрицу 3x3.
     *
     * @return единичная матрица
     */
    public static Matrix3f identity() {
        return new Matrix3f();
    }

    /**
     * Возвращает элемент матрицы по заданным индексам.
     *
     * @param row номер строки (0..2)
     * @param col номер столбца (0..2)
     * @return значение элемента матрицы
     */
    public float get(int row, int col) {
        return data[row][col];
    }

    /**
     * Складывает текущую матрицу с другой матрицей 3x3.
     *
     * @param other матрица-слагаемое
     * @return новая матрица - результат сложения
     */
    public Matrix3f add(Matrix3f other) {
        return new Matrix3f(
                MatrixMath.add(this.data, other.data, 3)
        );
    }

    /**
     * Устанавливает значение элемента матрицы по индексам.
     *
     * @param row номер строки (0..2)
     * @param col номер столбца (0..2)
     * @param value новое значение элемента
     */
    public void set(int row, int col, float value) {
        data[row][col] = value;
    }


    /**
     * Вычитает из текущей матрицы другую матрицу 3x3.
     *
     * @param other матрица-вычитаемое
     * @return новая матрица - результат вычитания
     */
    public Matrix3f subtract(Matrix3f other) {
        return new Matrix3f(
                MatrixMath.subtract(this.data, other.data, 3)
        );
    }

    /**
     * Умножает текущую матрицу на другую матрицу 3x3.
     *
     * @param other матрица-множитель
     * @return новая матрица - результат умножения
     */
    public Matrix3f multiply(Matrix3f other) {
        return new Matrix3f(
                MatrixMath.multiply(this.data, other.data, 3)
        );
    }

    /**
     * Умножает матрицу 3x3 на вектор размерности 3.
     *
     * @param v вектор-множитель
     * @return новый вектор - результат умножения
     */
    public Vector3f multiply(Vector3f v) {
        float x = data[0][0] * v.getX()
                + data[0][1] * v.getY()
                + data[0][2] * v.getZ();

        float y = data[1][0] * v.getX()
                + data[1][1] * v.getY()
                + data[1][2] * v.getZ();

        float z = data[2][0] * v.getX()
                + data[2][1] * v.getY()
                + data[2][2] * v.getZ();

        return new Vector3f(x, y, z);
    }

    /**
     * Возвращает транспонированную матрицу.
     *
     * @return новая матрица - транспонированная к текущей
     */
    public Matrix3f transpose() {
        return new Matrix3f(
                MatrixMath.transpose(this.data, 3)
        );
    }

    /**
     * Вычисляет определитель матрицы 3x3.
     *
     * @return значение определителя
     */
    public float determinant() {
        return MatrixMath.determinant3(this.data);
    }

    /**
     * Сравнивает текущую матрицу с другой на равенство
     * с учётом погрешности вычислений с плавающей точкой.
     *
     * @param obj объект для сравнения
     * @return {@code true}, если матрицы равны
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Matrix3f other = (Matrix3f) obj;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (Math.abs(this.data[i][j] - other.data[i][j]) >= 1e-10) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Возвращает строковое представление матрицы.
     *
     * @return строка с элементами матрицы
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            sb.append("[");
            for (int j = 0; j < 3; j++) {
                sb.append(String.format("%.4f", data[i][j]));
                if (j < 2) sb.append(", ");
            }
            sb.append("]\n");
        }
        return sb.toString();
    }
}
