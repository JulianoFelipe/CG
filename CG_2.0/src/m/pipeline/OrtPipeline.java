/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.pipeline;

import View.Teste_Pipeline;
import java.util.List;
import java.util.Observable;
import m.Camera;
import m.Viewport;
import m.Visao;
import m.Window;
import m.poligonos.CGObject;
import utils.math.MMath;

/**
 *
 * @author JFPS
 */
public class OrtPipeline extends CGPipeline{
    private final Visao vista;
    
    private float[][] matrizVistaOrt;
    private boolean changed = true;
    
    public OrtPipeline(Visao visao, Camera cam, Window win, Viewport view) {
        super(cam, win, view);
        this.vista = visao;
        matrizVistaOrt = MatrizOrtografica.getMatrizOrt(visao);
    }

    @Override
    public void convert2D(List<CGObject> lista) {
        lista.forEach((object) -> {
            float[][] retPoints = multiply3D(get3DPipelineMatrix(), object.getPointMatrix());
            retPoints = multiply2D(get2DPipelineMatrix(), retPoints);
            object.setPointMatrix(retPoints);
        });
    }
    
    @Override
    public void convert2D(CGObject object) {
        System.out.println("VISTA: " + vista);
        MMath.printMatrix(object.getPointMatrix());
        float[][] retPoints = multiply3D(get3DPipelineMatrix(), object.getPointMatrix());
        /*System.out.println("\n Matriz PIPELINE 3D");
        MMath.printMatrix(get3DPipelineMatrix());
        System.out.println("\n MATRIZ DEPOIS DE PIPE 3D");
        MMath.printMatrix(retPoints);*/
        retPoints = multiply2D(get2DPipelineMatrix(), retPoints);
        /*System.out.println("\n Matriz PIPELINE 2D");
        MMath.printMatrix(get2DPipelineMatrix());
        System.out.println("\n MATRIZ DEPOIS DE PIPE 2D (Window e Viewport)");
        MMath.printMatrix(retPoints);*/
        object.setPointMatrix(retPoints);
    }

    @Override
    public void update(Observable o, Object arg) {
        //if observable is window ou viewport, update matrix jp
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public float[][] get3DPipelineMatrix(){
        //Retorna a matriz final do pipeline
        //Se receber update de camera, alterar changed e calc tudo de novo
        //Concatena as budega
        
        if (changed == false){
            return matrizVistaOrt;
        } else {
            ///Matriz vista = Msru,src * Mvista    --- (Mjp é separada, já que é 2D)
            //Concatena por multiplicar ao contrário
            matrizVistaOrt = MMath.multiplicar(MatrizOrtografica.getMatrizOrt(vista), super.getMatrizSRUsrc());
            changed = false;
            return matrizVistaOrt;
        }
    }
    
    public float[][] get2DPipelineMatrix(){
        return getMatrixJP();
    }
    
    public float[][] getMatrixProj(){
        return MatrizOrtografica.getMatrizOrt(vista);
    }

    @Override
    public Visao getVisao() {
        return vista;
    }
        
    protected static final class MatrizOrtografica{
        protected static final float[][] MAT_TOPO = {
            { 1, 0, 0, 0},
            { 0, 0, 1, 0},
            { 0, 0, 0, 0},
            { 0, 0, 0, 1}
        };
        
        protected static final float[][] MAT_LATERAL = {
            { 0, 0, 1, 0},
            { 0, 1, 0, 0},
            { 0, 0, 0, 0},
            { 0, 0, 0, 1}
        };
        
        protected static final float[][] MAT_FRENTE = {
            { 1, 0, 0, 0},
            { 0, 1, 0, 0},
            { 0, 0, 0, 0},
            { 0, 0, 0, 1}
        };
        
        protected static float[][] getMatrizOrt(Visao vista){            
            switch(vista){
                case Frontal:
                    return MAT_FRENTE;
                case Lateral:
                    return MAT_LATERAL;
                case Topo:
                    return MAT_TOPO;
                default:
                    throw new IllegalArgumentException("Vista ortográfica não tem matriz correspondente.");
            }
        }
    };

}
