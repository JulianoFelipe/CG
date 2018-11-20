/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.shader;

import View.CG_20;
import java.util.List;
import javafx.geometry.Bounds;
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
    private float[][] zBuffer;
    private Light.TipoAtenuacao att;
        
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

        int width  = (int) graphs.getCanvas().getWidth();  ++width;
        int height = (int) graphs.getCanvas().getHeight(); ++height;
        
        graphs.getPixelWriter().setColor(width, width, selectedColor);       
        zBuffer     = new float[width][height];
        
        for (int i=0; i<width; i++){
            for (int j=0; j<height; j++){
                zBuffer[i][j] = Float.MAX_VALUE;
            }
        }
        
        objetosSRT.forEach((obj) -> {
            paintObject(graphs, obj, selectedID, selectedColor);
        });  
                
        //graphs.drawImage(img, 0, 0); //Para desenhar a imagem após o zbuffer
        
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * Seleciona qual função de pintura específica chamar
     * @param graphics
     * @param obj
     */
    private void paintObject(GraphicsContext graphs, CGObject obj, long selectedID, Color selColor){               
        if (obj instanceof HE_Poliedro){
            HE_Poliedro poli = (HE_Poliedro) obj;

            double zCentroid = getZCentroid(poli);
            
            FullScanLine scn = new FullScanLine(poli, luzAmbiente, luzesPontuais, observador, att, graphs.getCanvas().getBoundsInLocal());
            paintObject(graphs, scn.getScans(), zCentroid);
            
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
    
    private void paintObject(GraphicsContext graphs, List<FullScan> lista, double zCentroid){
        Paint fill   = graphs.getFill();
        Paint stroke = graphs.getStroke();
        
        Bounds bd = graphs.getCanvas().getBoundsInLocal();
        lista.forEach((scan) -> {
            int y = scan.getY();
            for (int i=0; i<scan.getEntries(); i++){
                int x = scan.getX(i);
                if (bd.contains(x, y)){
                    if (zCentroid < zBuffer[x][y]){
                        zBuffer[x][y] = (float) zCentroid;
                        graphs.getPixelWriter().setColor(x, y, scan.getColor(i));
                    }
                }
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
    
    private double getZCentroid(CGObject object){
        CGObject mundoOBJ = CG_20.main.getMundo().getObject(object);
        
        double zAcc = 0;
        int i;
        for (i=0; i<mundoOBJ.getNumberOfPoints(); i++){
            zAcc += mundoOBJ.get(i).getZ();
        }
        
        return zAcc/i;
    }  
}
