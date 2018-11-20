/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.shader;

import m.shader.scans.ExtremityScanLine;
import java.util.List;
import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import m.poligonos.ArestaEixo;
import m.poligonos.CGObject;
import m.poligonos.Vertice;
import m.poligonos.we_edge.HE_Poliedro;
import m.poligonos.we_edge.WE_Aresta;
import m.shader.scans.ExtremityScan;
import utils.math.VMath;

/**
 *
 * @author JFPS
 */
public class FlatZ extends CGShader{
    private float[][] zBuffer;
    private Light.TipoAtenuacao att = Light.TipoAtenuacao.Nulo;
    
    public FlatZ(Vertice observador, AmbientLight luzAmbiente) {
        super(observador, luzAmbiente);
    }
    
    public FlatZ(Vertice observador, AmbientLight luzAmbiente, List<PointLight> luzesPontuais) {
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
                ExtremityScanLine scn = new ExtremityScanLine(faces.get(i), graphs.getCanvas().getBoundsInLocal());
                paintFace(graphs, scn.getScans(), centroide.getZ(), cor);
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
    
    private void paintFace(GraphicsContext graphs, List<ExtremityScan> lista, double zCentroid, Color faceColor){
        //graphs.beginPath();
        Bounds bd = graphs.getCanvas().getBoundsInLocal();
        lista.forEach((scan) -> {
            //System.out.println("SCAN: " + scan.getyFin() + " Xs: " + scan.getxIni() + ", " + scan.getxFin());
            
            int y = scan.getyFin();
            for (int i=scan.getxIni(); i<scan.getxFin(); i++){
                if (bd.contains(i, y)){
                    if (zCentroid < zBuffer[i][y]){
                        zBuffer[i][y] = (float) zCentroid;
                        graphs.getPixelWriter().setColor(i, y, faceColor);
                    }
                }
            }
            
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
    
    //<editor-fold defaultstate="collapsed" desc="Point Illumination">
    private double[] iluminacaoDifusa(HE_Poliedro obj, Vertice normal, Vertice incidente){
        float[] kds = obj.getKd();
        
        double[] pintura = new double[]{0.0,0.0,0.0};
        for (int i=0; i<super.luzesPontuais.size(); i++){
            double[] ilum = luzesPontuais.get(i).iluminacaoDifusa(kds[0], kds[1], kds[2], normal, incidente);
            double atenuacao = luzesPontuais.get(i).fatorAtenuacao(att, VMath.distancia(luzesPontuais.get(i).getPosicao(), incidente));
            //System.out.println("ATT: " + atenuacao);
            pintura[0] += atenuacao*ilum[0];
            pintura[1] += atenuacao*ilum[1];
            pintura[2] += atenuacao*ilum[2];
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
            double atenuacao = luzesPontuais.get(i).fatorAtenuacao(att, VMath.distancia(luzesPontuais.get(i).getPosicao(), incidente));
            pintura[0] += atenuacao*ilum[0];
            pintura[1] += atenuacao*ilum[1];
            pintura[2] += atenuacao*ilum[2];
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
            double atenuacao = luzesPontuais.get(i).fatorAtenuacao(att, VMath.distancia(luzesPontuais.get(i).getPosicao(), incidente));
            pintura += atenuacao*ilum;
        }
        
        return pintura;
    }
    
    private double iluminacaoEspecularAcromatica(HE_Poliedro obj, Vertice normal, Vertice incidente){
        float[] kss = obj.getKs();
        
        double pintura = 0.;
        for (int i=0; i<super.luzesPontuais.size(); i++){
            double ilum = luzesPontuais.get(i).iluminacaoEspecularAcromatica(kss[0], (short) kss[3], normal, incidente, observador);
            double atenuacao = luzesPontuais.get(i).fatorAtenuacao(att, VMath.distancia(luzesPontuais.get(i).getPosicao(), incidente));
            pintura += atenuacao*ilum;
        }
        
        return pintura;
    }
    
    private Color iluminacaoTotal(double ka, double kd, double ks){
        double sum = ka+kd+ks;
        int sumInt = Math.min((int) Math.round(sum), 255);
        sumInt = Math.max(sumInt, 0);
        return Color.rgb(sumInt, sumInt, sumInt);
    }
//</editor-fold>
    
}
