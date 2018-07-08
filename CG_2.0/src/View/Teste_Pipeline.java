/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import m.Camera;
import m.CGViewport;
import m.Visao;
import m.CGWindow;
import m.pipeline.OrtPipeline;
import m.poligonos.PointObject;
import m.poligonos.Vertice;
import utils.math.MMath;

/**
 *
 * @author JFPS
 */
public class Teste_Pipeline {
        
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Vertice ViewUp = new Vertice(0, 1, 0);
        Vertice VRP    = new Vertice((float) 10, (float) 50, (float) 30);
        Vertice P      = new Vertice(10, 50, 0);
        
        MMath.printMatrix(VRP.getPointMatrix());
        
        Camera cam = new Camera(ViewUp, VRP, P);
       
        CGWindow win    = new CGWindow(16, 10);
        CGViewport view = new CGViewport(new Vertice(0, 0),
                                     new Vertice(320, 240) );
        
        OrtPipeline pipe = new OrtPipeline(Visao.Topo, cam, win, view);
        //PersPipeline pipe = new PersPipeline(17, cam, win, view);
        
        System.out.println("\n\nMATRIZ SRU,SRC");
        MMath.printMatrix(pipe.getMatrizSRUsrc());
        
        System.out.println("\nMATRIZ PROJEÇÃO");
        MMath.printMatrix(pipe.getMatrixProj());
        
        System.out.println("\nMATRIZ JP");
        MMath.printMatrix(pipe.getMatrixJP());
        
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
        
        PointObject pol = new PointObject(pol_mat);
        
        System.out.println("\nMATRIZ DE PONTOS");
        MMath.printMatrix(pol_mat);
        
        //float[][] res = MMath.multiplicar(pipe.getMatrizSRUsrc(), pol_mat);
        
        //System.out.println("\nMATRIZ DE PONTOS NO SRC");
        //printMatrix(res);
        
        //res = MMath.multiplicar(pipe.getMatrixProj(), res);
        
        //System.out.println("\nMATRIZ DEPOIS DE PROJETADA");
        //printMatrix(res);
        
        //System.out.println("\n MATRIZ DEPOIS DE DIVIDIDA PELOS ESCALARES DOS FATORES HOMOGÊNEOS");
        //res = 
        
        //float [][] newRes = MMath.removeZ(res);
        //newRes = MMath.multiplicar(pipe.getMatrixJP(), newRes);
        
        pipe.convert2D(pol);
        float[][] newRes = pol.getPointMatrix();
        
        System.out.println("\nMATRIZ RESULTANTE");
        MMath.printMatrix(newRes);
    }
}
