/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.math;

import java.util.Arrays;

/**
 * Matrix math
 *
 * @author Juliano
 */
public class MMath {

    /**
     * Simples multiplicação de matrizes. Tão simples que os princípios de
     * localidade referencial não são atendidos de nenhuma maneira. :D
     *
     * @param A Primeira matriz.
     * @param B Segunda matriz.
     * @return Produto das matrizes
     */
    public static double[][] multiplicar(double[][] A, double[][] B) {
        int aRows = A.length;
        int aColumns = A[0].length;
        int bRows = B.length;
        int bColumns = B[0].length;

        if (aColumns != bRows) {
            throw new IllegalArgumentException("A:Rows: " + aColumns + " did not match B:Columns " + bRows + ".");
        }

        double[][] C = new double[aRows][bColumns];

        for (int i = 0; i < aRows; i++) { // aRow
            for (int j = 0; j < bColumns; j++) { // bColumn
                for (int k = 0; k < aColumns; k++) { // aColumn
                    C[i][j] += A[i][k] * B[k][j];
                }
            }
        }

        return C;
    }

    /*Amazing precision*/
    public static float[][] multiplicar(float[][] A, float[][] B) {
        int aRows = A.length;
        int aColumns = A[0].length;
        int bRows = B.length;
        int bColumns = B[0].length;

        if (aColumns != bRows) {
            throw new IllegalArgumentException("A:Rows: " + aColumns + " did not match B:Columns " + bRows + ".");
        }

        float[][] C = new float[aRows][bColumns];
        float localSum = 0;
        for (int i = 0; i < aRows; i++) { // aRow
            for (int j = 0; j < bColumns; j++) { // bColumn
                for (int k = 0; k < aColumns; k++) { // aColumn
                    localSum += A[i][k] * B[k][j];
                }
                C[i][j] = localSum;
                localSum = 0;
            }
        }

        return C;
    }

    public static float[][] multiplicar(float[][] A, float B) {
        int aRows = A.length;

        float[][] result = new float[aRows][aRows];
        for (int i = 0; i < aRows; i++) {
            for (int j = 0; j < aRows; j++) {
                result[i][j] = A[i][j] * B;
            }
        }

        return result;
    }

    public static float[][] dividir(float[][] A, float B) {
        int aRows = A.length;

        float[][] result = new float[aRows][aRows];
        for (int i = 0; i < aRows; i++) {
            for (int j = 0; j < aRows; j++) {
                result[i][j] = A[i][j] / B;
            }
        }

        return result;
    }

    public static float[][] removeZ(float[][] matrix) {
        float[][] newRes = new float[3][matrix[0].length];

        for (int j = 0; j < matrix[0].length; j++) {
            newRes[0][j] = matrix[0][j];
            newRes[1][j] = matrix[1][j];
            newRes[2][j] = matrix[3][j];
        }

        return newRes;
    }

    public static float[][] removeFactor(float[][] matrix) {
        float[][] newRes = new float[3][matrix[0].length];

        for (int j = 0; j < matrix[0].length; j++) {
            newRes[0][j] = matrix[0][j];
            newRes[1][j] = matrix[1][j];
            newRes[2][j] = matrix[2][j];
        }

        return newRes;
    }
    
    public static float[][] addFactor(float[][] matrix) {
        float[][] newRes = new float[4][matrix[0].length];

        for (int j = 0; j < matrix[0].length; j++) {
            newRes[0][j] = matrix[0][j];
            newRes[1][j] = matrix[1][j];
            newRes[2][j] = matrix[2][j];
            newRes[2][j] = 1;
        }

        return newRes;
    }

    //https://stackoverflow.com/questions/5061912/printing-out-a-2-d-array-in-matrix-format
    public static void printMatrix(float[][] m) {
        try {
            int rows = m.length;
            int columns = m[0].length;
            String str = "|\t";
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    str += m[i][j] + "\t";
                }
                System.out.println(str + "|");
                str = "|\t";
            }
        } catch (Exception e) {
            System.out.println("Matrix is empty!!");
        }
    }

    //https://stackoverflow.com/questions/1148309/inverting-a-4x4-matrix
    //Código adaptado da fonte acima.
    public static float[][] invert4x4Matrix(float[][] mat) {
        float[][] out = new float[4][4];

        double[] inv = new double[16];
        double det;

        inv[0] = colMajor(mat, 5) * colMajor(mat, 10) * colMajor(mat, 15)
                - colMajor(mat, 5) * colMajor(mat, 11) * colMajor(mat, 14)
                - colMajor(mat, 9) * colMajor(mat, 6) * colMajor(mat, 15)
                + colMajor(mat, 9) * colMajor(mat, 7) * colMajor(mat, 14)
                + colMajor(mat, 13) * colMajor(mat, 6) * colMajor(mat, 11)
                - colMajor(mat, 13) * colMajor(mat, 7) * colMajor(mat, 10);

        inv[4] = -colMajor(mat, 4) * colMajor(mat, 10) * colMajor(mat, 15)
                + colMajor(mat, 4) * colMajor(mat, 11) * colMajor(mat, 14)
                + colMajor(mat, 8) * colMajor(mat, 6) * colMajor(mat, 15)
                - colMajor(mat, 8) * colMajor(mat, 7) * colMajor(mat, 14)
                - colMajor(mat, 12) * colMajor(mat, 6) * colMajor(mat, 11)
                + colMajor(mat, 12) * colMajor(mat, 7) * colMajor(mat, 10);

        inv[8] = colMajor(mat, 4) * colMajor(mat, 9) * colMajor(mat, 15)
                - colMajor(mat, 4) * colMajor(mat, 11) * colMajor(mat, 13)
                - colMajor(mat, 8) * colMajor(mat, 5) * colMajor(mat, 15)
                + colMajor(mat, 8) * colMajor(mat, 7) * colMajor(mat, 13)
                + colMajor(mat, 12) * colMajor(mat, 5) * colMajor(mat, 11)
                - colMajor(mat, 12) * colMajor(mat, 7) * colMajor(mat, 9);

        inv[12] = -colMajor(mat, 4) * colMajor(mat, 9) * colMajor(mat, 14)
                + colMajor(mat, 4) * colMajor(mat, 10) * colMajor(mat, 13)
                + colMajor(mat, 8) * colMajor(mat, 5) * colMajor(mat, 14)
                - colMajor(mat, 8) * colMajor(mat, 6) * colMajor(mat, 13)
                - colMajor(mat, 12) * colMajor(mat, 5) * colMajor(mat, 10)
                + colMajor(mat, 12) * colMajor(mat, 6) * colMajor(mat, 9);

        inv[1] = -colMajor(mat, 1) * colMajor(mat, 10) * colMajor(mat, 15)
                + colMajor(mat, 1) * colMajor(mat, 11) * colMajor(mat, 14)
                + colMajor(mat, 9) * colMajor(mat, 2) * colMajor(mat, 15)
                - colMajor(mat, 9) * colMajor(mat, 3) * colMajor(mat, 14)
                - colMajor(mat, 13) * colMajor(mat, 2) * colMajor(mat, 11)
                + colMajor(mat, 13) * colMajor(mat, 3) * colMajor(mat, 10);

        inv[5] = colMajor(mat, 0) * colMajor(mat, 10) * colMajor(mat, 15)
                - colMajor(mat, 0) * colMajor(mat, 11) * colMajor(mat, 14)
                - colMajor(mat, 8) * colMajor(mat, 2) * colMajor(mat, 15)
                + colMajor(mat, 8) * colMajor(mat, 3) * colMajor(mat, 14)
                + colMajor(mat, 12) * colMajor(mat, 2) * colMajor(mat, 11)
                - colMajor(mat, 12) * colMajor(mat, 3) * colMajor(mat, 10);

        inv[9] = -colMajor(mat, 0) * colMajor(mat, 9) * colMajor(mat, 15)
                + colMajor(mat, 0) * colMajor(mat, 11) * colMajor(mat, 13)
                + colMajor(mat, 8) * colMajor(mat, 1) * colMajor(mat, 15)
                - colMajor(mat, 8) * colMajor(mat, 3) * colMajor(mat, 13)
                - colMajor(mat, 12) * colMajor(mat, 1) * colMajor(mat, 11)
                + colMajor(mat, 12) * colMajor(mat, 3) * colMajor(mat, 9);

        inv[13] = colMajor(mat, 0) * colMajor(mat, 9) * colMajor(mat, 14)
                - colMajor(mat, 0) * colMajor(mat, 10) * colMajor(mat, 13)
                - colMajor(mat, 8) * colMajor(mat, 1) * colMajor(mat, 14)
                + colMajor(mat, 8) * colMajor(mat, 2) * colMajor(mat, 13)
                + colMajor(mat, 12) * colMajor(mat, 1) * colMajor(mat, 10)
                - colMajor(mat, 12) * colMajor(mat, 2) * colMajor(mat, 9);

        inv[2] = colMajor(mat, 1) * colMajor(mat, 6) * colMajor(mat, 15)
                - colMajor(mat, 1) * colMajor(mat, 7) * colMajor(mat, 14)
                - colMajor(mat, 5) * colMajor(mat, 2) * colMajor(mat, 15)
                + colMajor(mat, 5) * colMajor(mat, 3) * colMajor(mat, 14)
                + colMajor(mat, 13) * colMajor(mat, 2) * colMajor(mat, 7)
                - colMajor(mat, 13) * colMajor(mat, 3) * colMajor(mat, 6);

        inv[6] = -colMajor(mat, 0) * colMajor(mat, 6) * colMajor(mat, 15)
                + colMajor(mat, 0) * colMajor(mat, 7) * colMajor(mat, 14)
                + colMajor(mat, 4) * colMajor(mat, 2) * colMajor(mat, 15)
                - colMajor(mat, 4) * colMajor(mat, 3) * colMajor(mat, 14)
                - colMajor(mat, 12) * colMajor(mat, 2) * colMajor(mat, 7)
                + colMajor(mat, 12) * colMajor(mat, 3) * colMajor(mat, 6);

        inv[10] = colMajor(mat, 0) * colMajor(mat, 5) * colMajor(mat, 15)
                - colMajor(mat, 0) * colMajor(mat, 7) * colMajor(mat, 13)
                - colMajor(mat, 4) * colMajor(mat, 1) * colMajor(mat, 15)
                + colMajor(mat, 4) * colMajor(mat, 3) * colMajor(mat, 13)
                + colMajor(mat, 12) * colMajor(mat, 1) * colMajor(mat, 7)
                - colMajor(mat, 12) * colMajor(mat, 3) * colMajor(mat, 5);

        inv[14] = -colMajor(mat, 0) * colMajor(mat, 5) * colMajor(mat, 14)
                + colMajor(mat, 0) * colMajor(mat, 6) * colMajor(mat, 13)
                + colMajor(mat, 4) * colMajor(mat, 1) * colMajor(mat, 14)
                - colMajor(mat, 4) * colMajor(mat, 2) * colMajor(mat, 13)
                - colMajor(mat, 12) * colMajor(mat, 1) * colMajor(mat, 6)
                + colMajor(mat, 12) * colMajor(mat, 2) * colMajor(mat, 5);

        inv[3] = -colMajor(mat, 1) * colMajor(mat, 6) * colMajor(mat, 11)
                + colMajor(mat, 1) * colMajor(mat, 7) * colMajor(mat, 10)
                + colMajor(mat, 5) * colMajor(mat, 2) * colMajor(mat, 11)
                - colMajor(mat, 5) * colMajor(mat, 3) * colMajor(mat, 10)
                - colMajor(mat, 9) * colMajor(mat, 2) * colMajor(mat, 7)
                + colMajor(mat, 9) * colMajor(mat, 3) * colMajor(mat, 6);

        inv[7] = colMajor(mat, 0) * colMajor(mat, 6) * colMajor(mat, 11)
                - colMajor(mat, 0) * colMajor(mat, 7) * colMajor(mat, 10)
                - colMajor(mat, 4) * colMajor(mat, 2) * colMajor(mat, 11)
                + colMajor(mat, 4) * colMajor(mat, 3) * colMajor(mat, 10)
                + colMajor(mat, 8) * colMajor(mat, 2) * colMajor(mat, 7)
                - colMajor(mat, 8) * colMajor(mat, 3) * colMajor(mat, 6);

        inv[11] = -colMajor(mat, 0) * colMajor(mat, 5) * colMajor(mat, 11)
                + colMajor(mat, 0) * colMajor(mat, 7) * colMajor(mat, 9)
                + colMajor(mat, 4) * colMajor(mat, 1) * colMajor(mat, 11)
                - colMajor(mat, 4) * colMajor(mat, 3) * colMajor(mat, 9)
                - colMajor(mat, 8) * colMajor(mat, 1) * colMajor(mat, 7)
                + colMajor(mat, 8) * colMajor(mat, 3) * colMajor(mat, 5);

        inv[15] = colMajor(mat, 0) * colMajor(mat, 5) * colMajor(mat, 10)
                - colMajor(mat, 0) * colMajor(mat, 6) * colMajor(mat, 9)
                - colMajor(mat, 4) * colMajor(mat, 1) * colMajor(mat, 10)
                + colMajor(mat, 4) * colMajor(mat, 2) * colMajor(mat, 9)
                + colMajor(mat, 8) * colMajor(mat, 1) * colMajor(mat, 6)
                - colMajor(mat, 8) * colMajor(mat, 2) * colMajor(mat, 5);

        det = colMajor(mat, 0) * inv[0] + colMajor(mat, 1) * inv[4] + colMajor(mat, 2) * inv[8] + colMajor(mat, 3) * inv[12];

        if (det == 0) {
            return null;
        }

        det = 1.0 / det;

        int i, j, index;
        for (index = 0; index < 16; index++) {
            i = index % 4;
            j = index / 4;
            out[i][j] = (float) (inv[index] * det);
        }

        return out;
    }

    //http://mathworld.wolfram.com/images/equations/MatrixInverse/NumberedEquation4.gif
    public static float[][] invert3x3Matrix(float[][] mat) {
        float[][] out = new float[3][3];

        double temp1 = mat[1][1] * mat[2][2] - mat[2][1] * mat[1][2];

        double det = mat[0][0] * (temp1)
                - mat[0][1] * (mat[1][0] * mat[2][2] - mat[2][2] * mat[2][0])
                + mat[0][2] * (mat[1][0] * mat[2][1] - mat[2][1] * mat[2][0]);

        double invdet = 1 / det;

        out[0][0] = (float) (temp1 * invdet);
        out[0][1] = (float) ((mat[0][2] * mat[2][1] - mat[0][1] * mat[2][2]) * invdet);
        out[0][2] = (float) ((mat[0][1] * mat[1][2] - mat[0][2] * mat[1][1]) * invdet);
        out[1][0] = (float) ((mat[1][2] * mat[2][0] - mat[1][0] * mat[2][2]) * invdet);
        out[1][1] = (float) ((mat[0][0] * mat[2][2] - mat[0][2] * mat[2][0]) * invdet);
        out[1][2] = (float) ((mat[1][0] * mat[0][2] - mat[0][0] * mat[1][2]) * invdet);
        out[2][0] = (float) ((mat[1][0] * mat[2][1] - mat[2][0] * mat[1][1]) * invdet);
        out[2][1] = (float) ((mat[2][0] * mat[0][1] - mat[0][0] * mat[2][1]) * invdet);
        out[2][2] = (float) ((mat[0][0] * mat[1][1] - mat[1][0] * mat[0][1]) * invdet);

        return out;
    }

    private static float rowMajor(float[][] mat, int index) {
        int i = index / mat[0].length;
        int j = index % mat[0].length;
        return mat[i][j];
    }

    private static float colMajor(float[][] mat, int index) {
        int i = index % mat[0].length;
        int j = index / mat[0].length;
        return mat[i][j];
    }

    //https://stackoverflow.com/questions/1148309/inverting-a-4x4-matrix
    private static boolean invert(double[] mat) {
        double[] inv = new double[16];
        double det;
        int i;

        inv[0] = mat[5] * mat[10] * mat[15]
                - mat[5] * mat[11] * mat[14]
                - mat[9] * mat[6] * mat[15]
                + mat[9] * mat[7] * mat[14]
                + mat[13] * mat[6] * mat[11]
                - mat[13] * mat[7] * mat[10];

        inv[4] = -mat[4] * mat[10] * mat[15]
                + mat[4] * mat[11] * mat[14]
                + mat[8] * mat[6] * mat[15]
                - mat[8] * mat[7] * mat[14]
                - mat[12] * mat[6] * mat[11]
                + mat[12] * mat[7] * mat[10];

        inv[8] = mat[4] * mat[9] * mat[15]
                - mat[4] * mat[11] * mat[13]
                - mat[8] * mat[5] * mat[15]
                + mat[8] * mat[7] * mat[13]
                + mat[12] * mat[5] * mat[11]
                - mat[12] * mat[7] * mat[9];

        inv[12] = -mat[4] * mat[9] * mat[14]
                + mat[4] * mat[10] * mat[13]
                + mat[8] * mat[5] * mat[14]
                - mat[8] * mat[6] * mat[13]
                - mat[12] * mat[5] * mat[10]
                + mat[12] * mat[6] * mat[9];

        inv[1] = -mat[1] * mat[10] * mat[15]
                + mat[1] * mat[11] * mat[14]
                + mat[9] * mat[2] * mat[15]
                - mat[9] * mat[3] * mat[14]
                - mat[13] * mat[2] * mat[11]
                + mat[13] * mat[3] * mat[10];

        inv[5] = mat[0] * mat[10] * mat[15]
                - mat[0] * mat[11] * mat[14]
                - mat[8] * mat[2] * mat[15]
                + mat[8] * mat[3] * mat[14]
                + mat[12] * mat[2] * mat[11]
                - mat[12] * mat[3] * mat[10];

        inv[9] = -mat[0] * mat[9] * mat[15]
                + mat[0] * mat[11] * mat[13]
                + mat[8] * mat[1] * mat[15]
                - mat[8] * mat[3] * mat[13]
                - mat[12] * mat[1] * mat[11]
                + mat[12] * mat[3] * mat[9];

        inv[13] = mat[0] * mat[9] * mat[14]
                - mat[0] * mat[10] * mat[13]
                - mat[8] * mat[1] * mat[14]
                + mat[8] * mat[2] * mat[13]
                + mat[12] * mat[1] * mat[10]
                - mat[12] * mat[2] * mat[9];

        inv[2] = mat[1] * mat[6] * mat[15]
                - mat[1] * mat[7] * mat[14]
                - mat[5] * mat[2] * mat[15]
                + mat[5] * mat[3] * mat[14]
                + mat[13] * mat[2] * mat[7]
                - mat[13] * mat[3] * mat[6];

        inv[6] = -mat[0] * mat[6] * mat[15]
                + mat[0] * mat[7] * mat[14]
                + mat[4] * mat[2] * mat[15]
                - mat[4] * mat[3] * mat[14]
                - mat[12] * mat[2] * mat[7]
                + mat[12] * mat[3] * mat[6];

        inv[10] = mat[0] * mat[5] * mat[15]
                - mat[0] * mat[7] * mat[13]
                - mat[4] * mat[1] * mat[15]
                + mat[4] * mat[3] * mat[13]
                + mat[12] * mat[1] * mat[7]
                - mat[12] * mat[3] * mat[5];

        inv[14] = -mat[0] * mat[5] * mat[14]
                + mat[0] * mat[6] * mat[13]
                + mat[4] * mat[1] * mat[14]
                - mat[4] * mat[2] * mat[13]
                - mat[12] * mat[1] * mat[6]
                + mat[12] * mat[2] * mat[5];

        inv[3] = -mat[1] * mat[6] * mat[11]
                + mat[1] * mat[7] * mat[10]
                + mat[5] * mat[2] * mat[11]
                - mat[5] * mat[3] * mat[10]
                - mat[9] * mat[2] * mat[7]
                + mat[9] * mat[3] * mat[6];

        inv[7] = mat[0] * mat[6] * mat[11]
                - mat[0] * mat[7] * mat[10]
                - mat[4] * mat[2] * mat[11]
                + mat[4] * mat[3] * mat[10]
                + mat[8] * mat[2] * mat[7]
                - mat[8] * mat[3] * mat[6];

        inv[11] = -mat[0] * mat[5] * mat[11]
                + mat[0] * mat[7] * mat[9]
                + mat[4] * mat[1] * mat[11]
                - mat[4] * mat[3] * mat[9]
                - mat[8] * mat[1] * mat[7]
                + mat[8] * mat[3] * mat[5];

        inv[15] = mat[0] * mat[5] * mat[10]
                - mat[0] * mat[6] * mat[9]
                - mat[4] * mat[1] * mat[10]
                + mat[4] * mat[2] * mat[9]
                + mat[8] * mat[1] * mat[6]
                - mat[8] * mat[2] * mat[5];

        det = mat[0] * inv[0] + mat[1] * inv[4] + mat[2] * inv[8] + mat[3] * inv[12];
        System.out.println("DET: " + det);
        if (det == 0) {
            return false;
        }

        det = 1.0 / det;

        for (i = 0; i < 16; i++) {
            inv[i] = inv[i] * det;
        }
        System.out.println(Arrays.toString(inv));

        return true;
    }

    //public static void main(String... args) {
        /*float[][] pol_mat = {
            {  (float) 0.4472,  (float) 0.0,     (float)-0.8944,  (float) 4.4721},
            {  (float)-0.2318,  (float) 0.9658,  (float)-0.1159,  (float) 0.5795},
            {  (float) 0.8639,  (float) 0.2591,  (float) 0.4319,  (float)-60.038},
            {   0,   0,   0,   1}
            //  A    B    C    D    E
        };
        
        printMatrix(pol_mat);
        
        float[][] ret = invert4x4Matrix(pol_mat);
        
        printMatrix(ret);*/

        // Resultado obtido com inverte matriz do Excel 2018
        //0,447200765	-0,231760835	0,86389276	50,0007724
        //-7,92831E-06	0,965880408	0,259176773	15,00076285
        //-0,894467596	-0,115880417	0,43194638	30,00049801
        //0	0	0	1
        /*double[] pol_arr = new double[]{
            0.4472,     0.0,  -0.8944,   4.4721,
           -0.2318,  0.9658,  -0.1159,   0.5795,
            0.8639,  0.2591,   0.4319, -60.038 ,
                0.,      0.,       0.,       1.
        };
            //  A    B    C    D    E
        
        System.out.println(Arrays.toString(pol_arr));
        invert(pol_arr);*/
        
        /*float[][] matJP = {
            {1, 0, 272},
            {0, -1, 187},
            {0, 0, 1}
        };
        System.out.println("JP");
        printMatrix(matJP);

        matJP = invert3x3Matrix(matJP);

        System.out.println("\nInv JP");
        printMatrix(matJP);

        System.out.println("\n Point: ");
        float[][] point = {
            {230},
            {151},
            {0}
        };
        printMatrix(point);

        float[][] res = multiplicar(matJP, point);

        System.out.println("RES");
        printMatrix(res);*/
    //}
}
