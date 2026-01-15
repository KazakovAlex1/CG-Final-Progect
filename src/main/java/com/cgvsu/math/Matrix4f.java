package com.cgvsu.math;


import com.cgvsu.exceptions.MathException;


/**
 * Класс {@code Matrix4f} представляет квадратную матрицу размером 4x4
 * и предоставляет основные операции линейной алгебры:
 * сложение, вычитание, умножение и транспонирование.
 */
public class Matrix4f {

    /**_
     * Внутреннее представление матрицы в виде двумерного массива 4x4.
     */
    private final float[][] data;

    /**
     * Создаёт матрицу 4x4 на основе переданного двумерного массива.
     *
     * @param data двумерный массив размером 4x4
     * @throws MathException если размер массива не равен 4x4
     */
    public Matrix4f(float[][] data) {
        if (data.length != 4 || data[0].length != 4) {
            throw new MathException("Матрица должна быть размером 4x4");
        }
        this.data = new float[4][4];
        for (int i = 0; i < 4; i++) {
            System.arraycopy(data[i], 0, this.data[i], 0, 4);
        }
    }
    /*private Matrix4f(float[][] d, boolean x) {
        this.data = d;
    }*/

    /**
     * Создаёт единичную матрицу 4x4.
     */
    public Matrix4f() {
        this.data = new float[4][4];
        for (int i = 0; i < 4; i++) {
            data[i][i] = 1.0F;
        }
    }

    /**
     * Создаёт нулевую матрицу 4x4.
     *
     * @return нулевая матрица
     */
    public static Matrix4f zero() {
        return new Matrix4f(new float[4][4]);
    }

    /**
     * Создаёт единичную матрицу 4x4.
     *
     * @return единичная матрица
     */
    public static Matrix4f identity() {
        return new Matrix4f();
    }

    /**
     * Возвращает элемент матрицы по заданным индексам.
     *
     * @param row номер строки (0..3)
     * @param col номер столбца (0..3)
     * @return значение элемента матрицы
     */
    public float get(int row, int col) {
        return data[row][col];
    }

    /**
     * Складывает текущую матрицу с другой матрицей 4x4.
     *
     * @param other матрица-слагаемое
     * @return новая матрица — результат сложения
     */
    public Matrix4f add(Matrix4f other) {
        return new Matrix4f(
                MatrixMath.add(this.data, other.data, 4)
        );
    }

    /**
     * Устанавливает значение элемента матрицы по индексам.
     *
     * @param row номер строки (0..3)
     * @param col номер столбца (0..3)
     * @param value новое значение элемента
     */
    public void set(int row, int col, float value) {
        data[row][col] = value;
    }


    /**
     * Вычитает из текущей матрицы другую матрицу 4x4.
     *
     * @param other матрица-вычитаемое
     * @return новая матрица — результат вычитания
     */
    public Matrix4f subtract(Matrix4f other) {
        return new Matrix4f(
                MatrixMath.subtract(this.data, other.data, 4)
        );
    }

    /**
     * Умножает текущую матрицу на другую матрицу 4x4.
     *
     * @param other матрица-множитель
     * @return новая матрица  результат умножения
     */
    public Matrix4f multiply(Matrix4f other) {
        return new Matrix4f(
                MatrixMath.multiply(this.data, other.data, 4)
        );
    }

    /**
     * Умножает матрицу 4x4 на вектор размерности 4.
     *
     * @param v вектор-множитель
     * @return новый вектор — результат умножения
     */
    public Vector4f multiply(Vector4f v) {
        float x = data[0][0] * v.getX()
                + data[0][1] * v.getY()
                + data[0][2] * v.getZ()
                + data[0][3] * v.getW();

        float y = data[1][0] * v.getX()
                + data[1][1] * v.getY()
                + data[1][2] * v.getZ()
                + data[1][3] * v.getW();

        float z = data[2][0] * v.getX()
                + data[2][1] * v.getY()
                + data[2][2] * v.getZ()
                + data[2][3] * v.getW();

        float w = data[3][0] * v.getX()
                + data[3][1] * v.getY()
                + data[3][2] * v.getZ()
                + data[3][3] * v.getW();

        return new Vector4f(x, y, z, w);
    }

    /**
     * Возвращает транспонированную матрицу.
     *
     * @return новая матрица - транспонированная к текущей
     */
    public Matrix4f transpose() {
        return new Matrix4f(
                MatrixMath.transpose(this.data, 4)
        );
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

        Matrix4f other = (Matrix4f) obj;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
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
        for (int i = 0; i < 4; i++) {
            sb.append("[");
            for (int j = 0; j < 4; j++) {
                sb.append(String.format("%.4f", data[i][j]));
                if (j < 3) sb.append(", ");
            }
            sb.append("]\n");
        }
        return sb.toString();
    }


}
