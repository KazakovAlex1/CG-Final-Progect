package com.cgvsu.math;


import com.cgvsu.exceptions.MathException;

/**
 * Вспомогательный класс с общей математикой для квадратных матриц.
 * Предоставляет методы сложения, вычитания, умножения, транспонирования
 * и вычисления определителя для использования в {@link Matrix3f} и {@link Matrix4f}.
 */
final class MatrixMath {

    /**
     * Запрещает создание экземпляров класса.
     */
    private MatrixMath() {
    }

    /**
     * Проверяет размер матрицы.
     *
     * @param a матрица для проверки
     * @param n ожидаемый размер (n x n)
     * @throws MathException если размер матрицы не равен n x n
     */
    static void checkSize(float[][] a, int n) {
        if (a.length != n) {
            throw new MathException("Неверный размер матрицы");
        }
        for (int i = 0; i < n; i++) {
            if (a[i].length != n) {
                throw new MathException("Неверный размер матрицы");
            }
        }
    }

    /**
     * Складывает две матрицы одинакового размера.
     *
     * @param a первая матрица
     * @param b вторая матрица
     * @param n размер матрицы (n x n)
     * @return новая матрица — результат сложения
     */
    static float[][] add(float[][] a, float[][] b, int n) {
        checkSize(a, n);
        checkSize(b, n);

        float[][] result = new float[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result[i][j] = a[i][j] + b[i][j];
            }
        }
        return result;
    }

    /**
     * Вычитает одну матрицу из другой.
     *
     * @param a первая матрица
     * @param b вторая матрица
     * @param n размер матрицы (n x n)
     * @return новая матрица — результат вычитания
     */
    static float[][] subtract(float[][] a, float[][] b, int n) {
        checkSize(a, n);
        checkSize(b, n);

        float[][] result = new float[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result[i][j] = a[i][j] - b[i][j];
            }
        }
        return result;
    }

    /**
     * Умножает две матрицы одинакового размера.
     *
     * @param a первая матрица
     * @param b вторая матрица
     * @param n размер матрицы (n x n)
     * @return новая матрица — результат умножения
     */
    static float[][] multiply(float[][] a, float[][] b, int n) {
        checkSize(a, n);
        checkSize(b, n);

        float[][] result = new float[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    result[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return result;
    }

    /**
     * Возвращает транспонированную матрицу.
     *
     * @param a исходная матрица
     * @param n размер матрицы (n x n)
     * @return новая матрица — транспонированная
     */
    static float[][] transpose(float[][] a, int n) {
        checkSize(a, n);

        float[][] result = new float[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result[i][j] = a[j][i];
            }
        }
        return result;
    }

    /**
     * Вычисляет определитель матрицы 3x3.
     *
     * @param a матрица 3x3
     * @return значение определителя
     */
    static float determinant3(float[][] a) {
        checkSize(a, 3);

        return a[0][0] * a[1][1] * a[2][2]
                + a[0][1] * a[1][2] * a[2][0]
                + a[0][2] * a[1][0] * a[2][1]
                - a[0][2] * a[1][1] * a[2][0]
                - a[0][0] * a[1][2] * a[2][1]
                - a[0][1] * a[1][0] * a[2][2];
    }
}
