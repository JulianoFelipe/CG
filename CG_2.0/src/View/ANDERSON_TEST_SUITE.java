/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import m.Camera;
import m.Viewport;
import m.Window;
import m.anderson.Poligono;
import m.anderson.Vertice;
import m.pipeline.OrtPipeline;
import utils.math.MMath;

/**
 *
 * @author JFPS
 */
public class ANDERSON_TEST_SUITE {
    
    //https://stackoverflow.com/questions/5061912/printing-out-a-2-d-array-in-matrix-format
    public static void printMatrix(float[][] m){
        try{
            int rows = m.length;
            int columns = m[0].length;
            String str = "|\t";

            for(int i=0;i<rows;i++){
                for(int j=0;j<columns;j++){
                    str += m[i][j] + "\t";
                }

                System.out.println(str + "|");
                str = "|\t";
            }

        }catch(Exception e){System.out.println("Matrix is empty!!");}
    }
    
    public static float[][] removeZ(float[][] res){
        float[][] newRes = new float[3][res[0].length];
        
        for (int j=0; j<res[0].length; j++){
            newRes[0][j] = res[0][j];
            newRes[1][j] = res[1][j];
            newRes[2][j] = res[3][j];
        }
        
        return newRes;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Vertice ViewUp = new Vertice(0, 1, 0);
        Vertice VRP    = new Vertice(50, 15, 30);
        Vertice P      = new Vertice(20, 6, 15);
       
        Camera cam = new Camera(ViewUp, VRP, P);
       
        Window win = new Window(16, 10);
        Viewport view = new Viewport(new Vertice(0, 0),
                                     new Vertice(320, 240) );
        
        OrtPipeline pipe = new OrtPipeline(OrtPipeline.VistaOrtografica.Frontal, cam, win, view);
        
        System.out.println("\n\nMATRIZ SRU,SRC");
        printMatrix(pipe.getMatrizSRUsrc());
        
        System.out.println("\nMATRIZ PROJEÇÃO");
        printMatrix(pipe.getMatrizProj());
        
        System.out.println("\nMATRIZ JP");
        printMatrix(pipe.getMatrixJP());
        
        /*System.out.println("\nMATRIZ CONCATENADA FINAL");
        float [][] fin = pipe.getPipelineMatrix();
        printMatrix(fin);*/
        

        float[][] pol_mat = {
            {  30,  35,  25,  20,  30},
            {   2,   4,   3,   1,  10},
            {  25,  20,  18,  23,  (float) 22.5},
            {   1,   1,   1,   1,   1}
            //  A    B    C    D    E
        };
        
        System.out.println("\nMATRIZ DE PONTOS");
        printMatrix(pol_mat);
        
        float[][] res = MMath.multiplicar(pipe.getMatrizSRUsrc(), pol_mat);
        
        System.out.println("\nMATRIZ DE PONTOS NO SRC");
        printMatrix(res);
        
        res = MMath.multiplicar(pipe.getMatrizProj(), res);
        
        System.out.println("\nMATRIZ DEPOIS DE PROJETADA");
        printMatrix(res);
        
        float [][] newRes = removeZ(res);
        newRes = MMath.multiplicar(pipe.getMatrixJP(), newRes);
        
        System.out.println("\nMATRIZ DEPOIS DE JP");
        printMatrix(newRes);
    }
}
