/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.shader;

import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import m.poligonos.Aresta;
import m.poligonos.ArestaEixo;
import m.poligonos.CGObject;
import m.poligonos.Vertice;
import m.poligonos.we_edge.HE_Poliedro;
import m.poligonos.we_edge.WE_Aresta;

/**
 *
 * @author JFPS
 */
public class Wireframe extends CGShader{
    
    public Wireframe() {
        super(null, null);
    }
    
    @Override
    public void shade(List<CGObject> objetosSRT, GraphicsContext graphs, long selectedID, Color selectedColor) {
        graphs.setFill(Color.BLACK);
        graphs.setStroke(Color.BLACK);
        graphs.setLineWidth(1);

        objetosSRT.forEach((obj) -> {
            paintObject(graphs, obj, selectedID, selectedColor);
        });        
    }
    
    /**
     * Seleciona qual função de pintura específica chamar
     * @param graphics
     * @param obj
     */
    private void paintObject(GraphicsContext graphs, CGObject obj, long selectedID, Color selColor){        
        Paint stroke = graphs.getStroke();
        Paint fill   = graphs.getFill();
        if (selectedID!=-1 && obj.getID()==(selectedID)){
            if (selColor != null){
                graphs.setStroke(selColor);
                graphs.setFill(selColor);
            } else{
                graphs.setStroke(Color.RED);
                graphs.setFill(Color.RED);
            }
        }
        
        if (obj instanceof HE_Poliedro){
            HE_Poliedro poli = (HE_Poliedro) obj;
            List<List<WE_Aresta>> faces = (poli).getVisibleFaces();
            faces.forEach((face) -> {
                paintArestasConectadas(graphs, face);
            });
            if (selectedID!=-1 && obj.getID()==(selectedID)){
                super.paintPoints(poli.getVisiblePoints(), graphs);
            }
        } else if (obj instanceof ArestaEixo){
            ArestaEixo objA = (ArestaEixo) obj;
            graphs.setStroke(objA.getAxisColor());
            paintConectedPointList(graphs, obj.getPoints(), 0);
        } else {
            graphs.setStroke(selColor);
            graphs.setFill(selColor);
            paintConectedPointList(graphs, obj.getPoints(), 0);
        }
        
        graphs.setStroke(stroke);
        graphs.setFill(fill);
    }
    
    private void paintArestasConectadas(GraphicsContext graphs, List<? extends Aresta> lista){
        graphs.beginPath();
        
        lista.forEach((aresta) -> {
            Vertice ini = aresta.getvInicial();
            Vertice fin = aresta.getvFinal();
            graphs.strokeLine(ini.getX(), ini.getY(), fin.getX(), fin.getY());
        });
        
        graphs.closePath();
    }
    
    /**
     * Pinta os pontos como contínuos
     * @param graphs
     * @param lista
     */
    private void paintConectedPointList(GraphicsContext graphs, List<? extends Vertice> lista, int pointRadius){
        graphs.beginPath();
        
        Vertice point1 = lista.get(0);
        //System.out.println("Vista: " + vista.getVisao() + ". Point: " + point1);
        Vertice point2 = null;
        for (int i=1; i<lista.size(); i++){
            point2 = lista.get(i);
            graphs.strokeLine(point1.getX(), point1.getY(), point2.getX(), point2.getY());
            
            if (pointRadius > 0){
                graphs.fillOval(point1.getX()-(pointRadius/2), point1.getY()-(pointRadius/2), pointRadius, pointRadius);
            }
            
            point1 = point2;
        }
        graphs.strokeLine(point1.getX(), point1.getY(), lista.get(0).getX(),lista.get(0).getY());
        
        
        
        graphs.closePath();
    }        

    @Override
    public void setTipoAtenuacao(Light.TipoAtenuacao att) {
        
    }
}
