/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.shader;

import java.util.Arrays;
import m.shader.scans.ExtremityScanLine;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import m.poligonos.ArestaEixo;
import m.poligonos.CGObject;
import m.poligonos.Vertice;
import m.poligonos.we_edge.HE_Poliedro;
import m.poligonos.we_edge.WE_Aresta;
import m.shader.scans.ExtremityScan;

/**
 *
 * @author JFPS
 */
public class Flat extends CGShader{

    public Flat(Vertice observador, AmbientLight luzAmbiente) {
        super(observador, luzAmbiente);
    }
    
    public Flat(Vertice observador, AmbientLight luzAmbiente, List<PointLight> luzesPontuais) {
        super(observador, luzAmbiente, luzesPontuais);
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
            
            boolean chromaticObj = obj.isChromatic();
            float[] kas = obj.getKa();
            
            List<List<WE_Aresta>> faces = poli.getVisibleFaces();
            List<Vertice> normais = poli.getVisibleNormais();
            
            Color cor = Color.BLACK;
            for (int i=0; i<faces.size(); i++){
                
                int count = faces.get(i).size();
                double avgX=0, avgY=0, avgZ=0;
                for (int j=0; j<count; j++){
                    Vertice ini = faces.get(i).get(j).getvInicial();
                    avgX += ini.getX();
                    avgY += ini.getY();
                    avgZ += ini.getZ();
                }

                Vertice centroide = new Vertice(
                    (float) (avgX/count),
                    (float) (avgY/count),
                    (float) (avgZ/count)
                );
                
                if (obj.isKset()){ //Se os Ks do objeto foram definidos, faz o shading. Se não, pinta de preto.
                    if (chromaticObj){
                        double[] ilumKA = super.luzAmbiente.iluminacaoAmbiente(kas[0], kas[1], kas[2]);
                        double[] ilumKD = iluminacaoDifusa(poli, normais.get(i), centroide);
                        double[] ilumKS = iluminacaoEspecular(poli, normais.get(i), centroide);
                        cor = iluminacaoTotal(ilumKA, ilumKD, ilumKS);       
                    } else {
                        double ilumKA = super.luzAmbiente.iluminacaoAmbienteAcromatica(kas[0]);
                        double ilumKD = iluminacaoDifusaAcromatica(poli, normais.get(i), centroide);
                        double ilumKS = iluminacaoEspecularAcromatica(poli, normais.get(i), centroide);
                        cor = iluminacaoTotal(ilumKA, ilumKD, ilumKS);
                    }
                }
                
                graphs.setStroke(cor);
                ExtremityScanLine scn = new ExtremityScanLine(faces.get(i));
                paintFace(graphs, scn.getScans());
            }
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
    
    private void paintFace(GraphicsContext graphs, List<ExtremityScan> lista){
        //graphs.beginPath();
        
        lista.forEach((scan) -> {
            //System.out.println("SCAN: " + scan);
            graphs.strokeLine(scan.getxIni(), scan.getyIni(), scan.getxFin(), scan.getyFin());
        });
        
        //graphs.closePath();
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
    
    private double[] iluminacaoDifusa(HE_Poliedro obj, Vertice normal, Vertice incidente){
        float[] kds = obj.getKd();
        
        double[] pintura = new double[]{0.0,0.0,0.0};
        for (int i=0; i<super.luzesPontuais.size(); i++){
            double[] ilum = luzesPontuais.get(i).iluminacaoDifusa(kds[0], kds[1], kds[2], normal, incidente);
            pintura[0] += ilum[0];
            pintura[1] += ilum[1];
            pintura[2] += ilum[2];
        }
        
        return pintura;
    }
    
    private double[] iluminacaoEspecular(HE_Poliedro obj, Vertice normal, Vertice incidente){
        float[] kss = obj.getKs();
        
        double[] pintura = new double[]{0.0,0.0,0.0};
        //System.out.println("SIZE: " + luzesPontuais.size());
        for (int i=0; i<super.luzesPontuais.size(); i++){
            double[] ilum = luzesPontuais.get(i).iluminacaoEspecular(kss[0], kss[1], kss[2], (short) kss[3], normal, incidente, observador);
            //System.out.println("ILUM: " + Arrays.toString(ilum));
            pintura[0] += ilum[0];
            pintura[1] += ilum[1];
            pintura[2] += ilum[2];
        }
        
        return pintura;
    }
    
    private Color iluminacaoTotal(double[] ka, double[] kd, double[] ks){
        /*System.out.println("TOTAL: ");
        System.out.println("KAs: " + Arrays.toString(ka));
        System.out.println("KDs: " + Arrays.toString(kd));
        System.out.println("KSs: " + Arrays.toString(ks));*/
        
        ka[0] += kd[0] + ks[0];
        ka[1] += kd[1] + ks[1];
        ka[2] += kd[2] + ks[2];
        
        int r = Math.min((int) Math.round(ka[0]), 255);
        int g = Math.min((int) Math.round(ka[1]), 255);
        int b = Math.min((int) Math.round(ka[2]), 255);
        
        r = Math.max(r, 0);
        g = Math.max(g, 0);
        b = Math.max(b, 0);
        
        return Color.rgb(r, g, b);
    }

    private double iluminacaoDifusaAcromatica(HE_Poliedro obj, Vertice normal, Vertice incidente){
        float[] kds = obj.getKd();
        
        double pintura = 0.0;
        for (int i=0; i<super.luzesPontuais.size(); i++){
            double ilum = luzesPontuais.get(i).iluminacaoDifusaAcromatica(kds[0], normal, incidente);
            pintura += ilum;
        }
        
        return pintura;
    }
    
    private double iluminacaoEspecularAcromatica(HE_Poliedro obj, Vertice normal, Vertice incidente){
        float[] kss = obj.getKs();
        
        double pintura = 0.;
        for (int i=0; i<super.luzesPontuais.size(); i++){
            double ilum = luzesPontuais.get(i).iluminacaoEspecularAcromatica(kss[0], (short) kss[3], normal, incidente, observador);
            pintura += ilum;
        }
        
        return pintura;
    }
    
    private Color iluminacaoTotal(double ka, double kd, double ks){
        double sum = ka+kd+ks;
        int sumInt = Math.min((int) Math.round(sum), 255);
        sumInt = Math.max(sumInt, 0);
        return Color.rgb(sumInt, sumInt, sumInt);
    }
    
}
