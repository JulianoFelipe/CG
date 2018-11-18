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
import m.poligonos.ArestaEixo;
import m.poligonos.CGObject;
import m.poligonos.Vertice;
import m.poligonos.we_edge.HE_Poliedro;
import m.shader.scans.FullScan;
import m.shader.scans.FullScanLine;

/**
 *
 * @author JFPS
 */
public class Gouraud extends CGShader{

    private Light.TipoAtenuacao att;
    
    /* Resumo:
       - Iluminação nos vértices
       - Normal dos vértices (Média das normais das faces)
       - Interpolar iluminação nas arestas
       - Com iluminação das arestas, interpolar nas scans
    */
    
    public Gouraud(Vertice observador, AmbientLight luzAmbiente) {
        super(observador, luzAmbiente);
    }
    
    public Gouraud(Vertice observador, AmbientLight luzAmbiente, List<PointLight> luzesPontuais) {
        super(observador, luzAmbiente, luzesPontuais);
    }
    
    @Override
    public void setTipoAtenuacao(Light.TipoAtenuacao atenuacao){
        att = atenuacao;
    }
    
    @Override
    public void shade(List<CGObject> objetosSRT, GraphicsContext graphs, long selectedID, Color selectedColor) {
        graphs.setFill  (Color.BLACK);
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
        if (obj instanceof HE_Poliedro){
            HE_Poliedro poli = (HE_Poliedro) obj;
            
            FullScanLine scn = new FullScanLine(poli, luzAmbiente, luzesPontuais, observador, att );
            paintObject(graphs, scn.getScans());
            
            if (selectedID!=-1 && obj.getID()==(selectedID)){
                Paint fill = graphs.getFill();
                graphs.setFill(selColor);
                super.paintPoints(poli.getVisiblePoints(), graphs);
                graphs.setFill(fill);
            }
        } else if (obj instanceof ArestaEixo){
            ArestaEixo objA = (ArestaEixo) obj;
            graphs.setStroke(objA.getAxisColor());
            paintConectedPointList(graphs, obj.getPoints(), 0);
        } else {
            paintConectedPointList(graphs, obj.getPoints(), 0);
        }
        
        if (selectedID!=-1 && obj.getID()==(selectedID)){
            if (!(obj instanceof HE_Poliedro)) super.paintPoints(obj.getPoints(), graphs);
        }
    }
    
    private void paintObject(GraphicsContext graphs, List<FullScan> lista){
        Paint fill   = graphs.getFill();
        Paint stroke = graphs.getStroke();
        
        lista.forEach((scan) -> {
            for (int i=0; i<scan.getEntries(); i++){
                //System.out.println("Scan: " + scan.getY() + " X: " + scan.getX(i) + " Color: " + scan.getColor(i).getRed()+ ", " + scan.getColor(i).getGreen() + ", " + scan.getColor(i).getBlue());
                graphs.setStroke(scan.getColor(i));
                graphs.strokeLine(scan.getX(i), scan.getY(), scan.getX(i), scan.getY());
            }
            //scan.
            //graphs.strokeLine(scan.getxIni(), scan.getyIni(), scan.getxFin(), scan.getyFin());
        });
        
        graphs.setFill(fill);
        graphs.setStroke(stroke);
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
}
