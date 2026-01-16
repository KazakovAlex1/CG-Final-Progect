package com.cgvsu.render_engine;

import com.cgvsu.math.Vector3f;
import com.cgvsu.math.Matrix4f;
import com.cgvsu.math.Vector2f;

public class GraphicConveyor {

    public static Matrix4f scale(float scaleX, float scaleY, float scaleZ) {
        Matrix4f result = Matrix4f.identity();
        result.set(0, 0, scaleX);
        result.set(1, 1, scaleY);
        result.set(2, 2, scaleZ);
        return result;
    }

    public static Matrix4f translate(float tx, float ty, float tz) {
        Matrix4f result = Matrix4f.identity();
        result.set(0, 3, tx);
        result.set(1, 3, ty);
        result.set(2, 3, tz);
        return result;
    }


    public static Matrix4f rotateX(float angle) {
        Matrix4f result = Matrix4f.identity();
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);

        result.set(1, 1, cos);
        result.set(1, 2, -sin);
        result.set(2, 1, sin);
        result.set(2, 2, cos);

        return result;
    }

    public static Matrix4f rotateY(float angle) {
        Matrix4f result = Matrix4f.identity();
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);

        result.set(0, 0, cos);
        result.set(0, 2, sin);
        result.set(2, 0, -sin);
        result.set(2, 2, cos);

        return result;
    }

    public static Matrix4f rotateZ(float angle) {
        Matrix4f result = Matrix4f.identity();
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);

        result.set(0, 0, cos);
        result.set(0, 1, -sin);
        result.set(1, 0, sin);
        result.set(1, 1, cos);

        return result;
    }

    public static Matrix4f lookAt(Vector3f eye, Vector3f target) {
        return lookAt(eye, target, new Vector3f(0F, 1.0F, 0F));
    }

    public static Matrix4f lookAt(Vector3f eye, Vector3f target, Vector3f up) {
        Vector3f za = target.subtract(eye).normalized();
        Vector3f xa = up.cross(za).normalized();
        Vector3f ya = za.cross(xa).normalized();

        Matrix4f translationMatrix = Matrix4f.identity();
        translationMatrix.set(0, 3, -eye.getX());
        translationMatrix.set(1, 3, -eye.getY());
        translationMatrix.set(2, 3, -eye.getZ());


        Matrix4f rotationMatrix = Matrix4f.identity();

        rotationMatrix.set(0, 0, xa.getX());
        rotationMatrix.set(0, 1, xa.getY());
        rotationMatrix.set(0, 2, xa.getZ());

        rotationMatrix.set(1, 0, ya.getX());
        rotationMatrix.set(1, 1, ya.getY());
        rotationMatrix.set(1, 2, ya.getZ());

        rotationMatrix.set(2, 0, za.getX());
        rotationMatrix.set(2, 1, za.getY());
        rotationMatrix.set(2, 2, za.getZ());

        // Видовая матрица: V = P * T
        return rotationMatrix.multiply(translationMatrix);
    }

    public static Matrix4f perspective(
            final float fov,
            final float aspectRatio,
            final float nearPlane,
            final float farPlane) {

        Matrix4f result = Matrix4f.zero();

        float tanHalfFov = (float) Math.tan(fov * 0.3F);
        float f = 1.0f / tanHalfFov;

        result.set(0, 0, f);
        result.set(1, 1, f / aspectRatio);
        result.set(2, 2, (farPlane + nearPlane) / (farPlane - nearPlane));
        result.set(2, 3, (2 * farPlane * nearPlane) / (nearPlane - farPlane));
        result.set(3, 2, 1.0f);

        return result;
    }

    public static Vector3f multiplyMatrix4ByVector3(final Matrix4f matrix, final Vector3f vertex) {

        float x = vertex.getX();
        float y = vertex.getY();
        float z = vertex.getZ();

        float resultX = matrix.get(0, 0) * x + matrix.get(0, 1) * y +
                matrix.get(0, 2) * z + matrix.get(0, 3);
        float resultY = matrix.get(1, 0) * x + matrix.get(1, 1) * y +
                matrix.get(1, 2) * z + matrix.get(1, 3);
        float resultZ = matrix.get(2, 0) * x + matrix.get(2, 1) * y +
                matrix.get(2, 2) * z + matrix.get(2, 3);
        float resultW = matrix.get(3, 0) * x + matrix.get(3, 1) * y +
                matrix.get(3, 2) * z + matrix.get(3, 3);

        if (Math.abs(resultW) > 1e-10) {
            return new Vector3f(resultX / resultW, resultY / resultW, resultZ / resultW);
        } else {
            return new Vector3f(resultX, resultY, resultZ);
        }
    }

    public static Vector2f vertexToPoint(final Vector3f vertex, final int width, final int height) {

        float screenX = ((width - 1) * 0.5f) * vertex.getX() + (width - 1) * 0.5f;
        float screenY = ((1 - height) * 0.5f) * vertex.getY() + (height - 1) * 0.5f;

        return new Vector2f(screenX, screenY);
    }

    public static Matrix4f createModelMatrix(Vector3f translation, Vector3f rotation, Vector3f scale) {
        //отдельные матрицы преобразований
        Matrix4f scaleMatrix = scale(scale.getX(), scale.getY(), scale.getZ());
        Matrix4f rotationXMatrix = rotateX(rotation.getX());
        Matrix4f rotationYMatrix = rotateY(rotation.getY());
        Matrix4f rotationZMatrix = rotateZ(rotation.getZ());
        Matrix4f translationMatrix = translate(translation.getX(), translation.getY(), translation.getZ());

        Matrix4f rotationMatrix = rotationXMatrix.multiply(rotationYMatrix.multiply(rotationZMatrix));

        // Для векторов-столбцов: M = T * R * S
        // Сначала масштабирование, потом вращение, потом перенос
        Matrix4f modelMatrix = translationMatrix.multiply(rotationMatrix.multiply(scaleMatrix));

        return modelMatrix;
    }

}