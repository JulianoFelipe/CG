/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.math;

/**
 * Matrix math
 * @author Juliano
 */
public class MMath {
    /**
     * Simples multiplicação de matrizes.
     * Tão simples que os princípios de 
     * localidade referencial não são
     * atendidos de nenhuma maneira. :D
     * @param A Primeira matriz.
     * @param B Segunda matriz.
     * @return  Produto das matrizes
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

        for (int i = 0; i < aRows; i++) { // aRow
            for (int j = 0; j < bColumns; j++) { // bColumn
                for (int k = 0; k < aColumns; k++) { // aColumn
                    C[i][j] += A[i][k] * B[k][j];
                }
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
    
    public static float[][] removeZ(float[][] matrix){
        float[][] newRes = new float[3][matrix[0].length];
        
        for (int j=0; j<matrix[0].length; j++){
            newRes[0][j] = matrix[0][j];
            newRes[1][j] = matrix[1][j];
            newRes[2][j] = matrix[3][j];
        }
        
        return newRes;
    }
    
    public static float[][] removeFactor(float[][] matrix){
        float[][] newRes = new float[3][matrix[0].length];
        
        for (int j=0; j<matrix[0].length; j++){
            newRes[0][j] = matrix[0][j];
            newRes[1][j] = matrix[1][j];
            newRes[2][j] = matrix[2][j];
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
}
