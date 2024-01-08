package com.cgvsu.vectormath.matrix;

import com.cgvsu.vectormath.vector.Vector3D;
import com.cgvsu.vectormath.vector.Vector4D;

import static com.cgvsu.vectormath.matrix.Matrix4x4.multMatrix4x4OnVector3D;
import static com.cgvsu.vectormath.matrix.Matrix4x4.multiplyMatrix4ByVector3DWithW;
import static org.junit.jupiter.api.Assertions.*;

import org.testng.annotations.Test;


public class Matrix4x4Test {

    @Test
    public void testAddition() {
        float[][] data1 = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 16}
        };
        float[][] data2 = {
                {16, 15, 14, 13},
                {12, 11, 10, 9},
                {8, 7, 6, 5},
                {4, 3, 2, 1}
        };
        Matrix4x4 m1 = new Matrix4x4(data1);
        Matrix4x4 m2 = new Matrix4x4(data2);

        Matrix4x4 result = m1.add(m2);

        float[][] expectedData = {
                {17, 17, 17, 17},
                {17, 17, 17, 17},
                {17, 17, 17, 17},
                {17, 17, 17, 17}
        };
        assertArrayEquals(expectedData, result.getMatrix());
    }

    @Test
    public void testSubtraction() {
        float[][] data1 = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 16}
        };
        float[][] data2 = {
                {16, 15, 14, 13},
                {12, 11, 10, 9},
                {8, 7, 6, 5},
                {4, 3, 2, 1}
        };
        Matrix4x4 m1 = new Matrix4x4(data1);
        Matrix4x4 m2 = new Matrix4x4(data2);

        Matrix4x4 result = m1.subtract(m2);

        float[][] expectedData = {
                {-15, -13, -11, -9},
                {-7, -5, -3, -1},
                {1, 3, 5, 7},
                {9, 11, 13, 15}
        };
        assertArrayEquals(expectedData, result.getMatrix());
    }

    @Test
    public void testMatrixVectorMultiplication() {
        float[][] matrixData = {
                {2, 0, 0, 0},
                {0, 2, 0, 0},
                {0, 0, 2, 0},
                {0, 0, 0, 2}
        };
        Matrix4x4 matrix = new Matrix4x4(matrixData);

        float[] vectorData = {1, 2, 3, 4};
        Vector4D vector = new Vector4D(vectorData[0], vectorData[1],vectorData[2], vectorData[3]);

        Vector4D result = matrix.multiply(vector);

        assertEquals(2, result.get(0), 0.01);
        assertEquals(4, result.get(1), 0.01);
        assertEquals(6, result.get(2), 0.01);
        assertEquals(8, result.get(3), 0.01);
    }
    @Test
    public void testMatrixOnVector3DMultiplication() {
        float[][] matrixData = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {1, 2, 3, 4},
                {5, 6, 7, 8}
        };
        Matrix4x4 matrix = new Matrix4x4(matrixData);

        float[] vectorData = {1, 2, 3};
        Vector3D vector = new Vector3D(vectorData[0], vectorData[1],vectorData[2]);

        Vector3D result = multMatrix4x4OnVector3D(matrix, vector);

        assertEquals(18, result.get(0), 0.01);
        assertEquals(46, result.get(1), 0.01);
        assertEquals(18, result.get(2), 0.01);
    }
    @Test
    public void testMatrixByVector3DMultiplication() {
        float[][] matrixData = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {1, 2, 3, 4},
                {5, 6, 7, 8}
        };
        Matrix4x4 matrix = new Matrix4x4(matrixData);

        float[] vectorData = {1, 2, 3};
        Vector3D vector = new Vector3D(vectorData[0], vectorData[1],vectorData[2]);

        Vector4D result = multiplyMatrix4ByVector3DWithW(matrix, vector);
        final float w = ((vector.get(0) * matrix.getElem(3,0)) + (vector.get(1) * matrix.getElem(3, 1)) + (vector.get(2) * matrix.getElem(3, 2)) + matrix.getElem(3, 3));

        assertEquals(18/w, result.get(0), 0.01);
        assertEquals(46/w, result.get(1), 0.01);
        assertEquals(18/w, result.get(2), 0.01);
    }

    @Test
    public void testMatrixMatrixMultiplication() {
        float[][] matrixData1 = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 16}
        };
        Matrix4x4 matrix1 = new Matrix4x4(matrixData1);

        float[][] matrixData2 = {
                {16, 15, 14, 13},
                {12, 77, 10, 9},
                {8, 7, 6, 5},
                {4, 3, 2, 1}
        };
        Matrix4x4 matrix2 = new Matrix4x4(matrixData2);

        Matrix4x4 result = matrix1.multiply(matrix2);

        float[][] expectedData = {
                {80, 202, 60, 50},
                {240, 610, 188, 162},
                {400, 1018, 316, 274},
                {560, 1426, 444, 386}
        };
        assertArrayEquals(expectedData, result.getMatrix());
    }

    @Test
    public void testTranspose() {
        float[][] matrixData = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 16}
        };
        Matrix4x4 matrix = new Matrix4x4(matrixData);

        Matrix4x4 transposed = matrix.transpose();

        float[][] expectedData = {
                {1, 5, 9, 13},
                {2, 6, 10, 14},
                {3, 7, 11, 15},
                {4, 8, 12, 16}
        };
        assertArrayEquals(expectedData, transposed.getMatrix());
    }

    @Test
    public void testIdentityMatrix() {
        Matrix4x4 identity = Matrix4x4.identity();
        float[][] data = identity.getMatrix();

        float[][] expectedData = {
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };

        assertArrayEquals(expectedData, data);
    }

    @Test
    public void testZeroMatrix() {
        Matrix4x4 zero = Matrix4x4.zero();
        float[][] data = zero.getMatrix();

        float[][] expectedData = {
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };

        assertArrayEquals(expectedData, data);
    }
    @Test
    public void testDetermination() {
        float[][] data = {
                {10, 34, 5, 45},
                {28, 12, 93, 3},
                {7, 48, 200, 10},
                {41, 6, 23, 18}
        };
        Matrix4x4 matrix = new Matrix4x4(data);

        float result = matrix.determinate();
        assertEquals(308450, result, 0.001);
    }
    @Test
    public void testDetermination1() {
        float[][] data = {
                {1, 7, 1, 8},
                {4, 6, 11, 8},
                {1, 3, 1, 8},
                {2, 5, 9, 1}
        };
        Matrix4x4 matrix = new Matrix4x4(data);

        float result = matrix.determinate();
        assertEquals(252, result, 0.001);
    }
}
