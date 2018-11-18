/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.shader.scans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.paint.Color;
import m.poligonos.Vertice;
import m.poligonos.we_edge.HE_Poliedro;
import m.poligonos.we_edge.WE_Aresta;
import m.poligonos.we_edge.WE_Vertice;
import m.shader.AmbientLight;
import m.shader.PointLight;
import utils.math.VMath;

/**
 *
 * @author JFPS
 */
public class FullScanLine {
    private final HE_Poliedro object;
    private final AmbientLight luzAmbiente;
    private final List<PointLight> luzesPontuais;
    private final Vertice observador;
    
    private final List<FullScan> scans;
    
    private final List<Float> slope;
    private List<WE_Aresta> nonProcessedEdges;
    private final List<WE_Aresta> activeEdges;
    private final List<Float> interceptX;
    private final List<Float> rIntensity;
    private final List<Float> gIntensity;
    private final List<Float> bIntensity;

    private final Map<Long, Float> rIlum;
    private final Map<Long, Float> gIlum;
    private final Map<Long, Float> bIlum;
    
    private List<Float> sortedIntercept;
    private List<Float> sortedR;
    private List<Float> sortedG;
    private List<Float> sortedB;
    
    /* Processamento:
       - Face por face;
       - Calcula a normal/iluminação tem todos os vértices da face
       - Armazena em um Map ligando ID do Vértice com ilum.
       - Interpola tudo
       - Limpa as estruturas extras
    
    */
    
    public FullScanLine(HE_Poliedro object, AmbientLight luzAmbiente, List<PointLight> luzesPontuais, Vertice observador) {
        this.object = object;
        scans       = new ArrayList();
        interceptX  = new ArrayList();
        slope       = new ArrayList();
        activeEdges = new ArrayList();
        
        rIntensity = new ArrayList<>();
        gIntensity = new ArrayList<>();
        bIntensity = new ArrayList<>();
        
        int size = object.getVisiblePoints().size();
        rIlum = new HashMap(size);
        gIlum = new HashMap(size);
        bIlum = new HashMap(size);
        
        sortedIntercept = new ArrayList<>();
        sortedR = new ArrayList<>();
        sortedG = new ArrayList<>();
        sortedB = new ArrayList<>();
        
        this.luzAmbiente = luzAmbiente;
        this.luzesPontuais = luzesPontuais;
        this.observador = observador;
        
        calculateScans();
    }
    
    private float[] getMinMax(List<WE_Aresta> face){
        float min = face.get(0).getMinY(),
              max = face.get(0).getMaxY();
        
        for (int i=1; i<face.size(); i++){
            WE_Aresta a = face.get(i);
            
            if (a.getMinY() < min) min = a.getMinY();
            if (a.getMaxY() > max) max = a.getMaxY();
        }
        //System.out.println("MINMAX: " + min + " Max: " + max);
        return new float[]{min, max};
    }
       
    private void initialize(int firstScanLine, List<WE_Aresta> workingFace){
        slope.clear();
        activeEdges.clear();
        interceptX.clear();
        rIntensity.clear();
        gIntensity.clear();
        bIntensity.clear();
        nonProcessedEdges = workingFace;
            
        calculateIlumination();
        
        for(int i=0; i<nonProcessedEdges.size(); i++){
            WE_Aresta a = nonProcessedEdges.get(i);
            //System.out.println("Edge non proc: " + a + " Size: " + nonProcessedEdges.size());
            if (firstScanLine >= Math.round(a.getMinY()) &&
                firstScanLine <= Math.round(a.getMaxY())){
                //System.out.println("Inside scan y="+firstScanLine);
                float ty = Math.round((a.getvFinal().getY() - a.getvInicial().getY()));
                if (ty == Float.POSITIVE_INFINITY){ ty = 0;}
                
                if (ty != 0){ //Aresta não é horizontal, então adiciona
                    float tx = Math.round((a.getvFinal().getX() - a.getvInicial().getX()));
                    //System.out.println("TX="+tx+" TY="+ty);
                    if (tx == Float.POSITIVE_INFINITY){ tx = 1; }
                    if (tx == 0.) slope.add((float) 0.);
                    else          slope.add(tx/ty);
                    
                    activeEdges.add(a);  
                    interpolateIlumination(a);
                }
                nonProcessedEdges.remove(i); //Removes even if horizontal
                --i;
            }
        }
    }
    
    /**
     * Coloca arestas na lista de arestas ativas,
     * retira da mesma lista, e retira da lista de
     * não processadas conforme scan line.
     * 
     * @param scanline 
     */
    private void updateFromScan(int scanline){
        //System.out.println("UPDATE : y : " + scanline);
        for(int i=0; i<activeEdges.size(); i++){
            WE_Aresta a = activeEdges.get(i);
            if (scanline <  Math.round(a.getMinY()) ||
                scanline >  Math.round(a.getMaxY())){
                //System.out.println("Remove edge: " + a);
                activeEdges.remove(i);
                slope.remove(i);
                interceptX.remove(i);
                
                if (object.isKset()){
                    rIntensity.remove(i);
                    if (gIntensity != null){
                        gIntensity.remove(i);
                        bIntensity.remove(i);
                    } 
                }
                --i;
            }
        }
        
        for(int i=0; i<nonProcessedEdges.size(); i++){
            WE_Aresta a = nonProcessedEdges.get(i);
            if (scanline >  Math.round(a.getMinY()) &&
                scanline <= Math.round(a.getMaxY())){
                //System.out.println("Add edge: " + a);
                float ty = Math.round((a.getvFinal().getY() - a.getvInicial().getY()));
                if (ty == Float.POSITIVE_INFINITY){ ty = 0; }
                
                if (ty != 0){
                    float tx = Math.round((a.getvFinal().getX() - a.getvInicial().getX()));
                    if (tx == Float.POSITIVE_INFINITY){ tx = 1;}
                    if (tx == 0.) slope.add((float) 0.);
                    else          slope.add(tx/ty);

                    //System.out.println("Added slope: " + (ty/tx));
                    activeEdges.add(a);  
                    interpolateIlumination(a);
                }
                nonProcessedEdges.remove(i); //Removes even if horizontal
                --i;
            }
        }
    }
    
    private void calculateScans(){       
        List<List<WE_Aresta>> visibleFaces = object.getVisibleFaces();

        for (int i=0; i<visibleFaces.size(); i++){

            float[] minMax = getMinMax(visibleFaces.get(i));
            int min = Math.round(minMax[0]);
            int max = Math.round(minMax[1]);
            //System.out.println("MIN: " + min + " Max: " + max);
            initialize(min, visibleFaces.get(i)); //Coloca edges ativas inicias e calcula a iluminação em todos os vértices
                       
            for (int yScan=min+1; yScan<=max; yScan++){        
                int size = activeEdges.size();
                //System.out.println("Active Edges: " + activeEdges);
                //System.out.println("Intercepts: " + interceptX);
                for (int ps=0; ps<size; ps++) {  
                    //System.out.print("Y: " + yScan + " X: " + interceptX.get(ps));
                    interceptX.set(ps,   interceptX.get(ps)+slope.get(ps));

                    //System.out.println(" New X: " + interceptX.get(ps) + " Slope: " + slope.get(ps));
                }

                sortInterceptX();

                byte parity=0;
                for (int ps=0; ps<size-1; ps++){
                    if (parity==0){
                        
                        FullScan scan = new FullScan(yScan);
                        
                        int begin = Math.round(sortedIntercept.get(ps));
                        int end   = Math.round(sortedIntercept.get(ps+1));
                        float rIncr = Float.MAX_VALUE, gIncr = Float.MAX_VALUE, bIncr = Float.MAX_VALUE; //Valores para induzir erro
                        //System.out.println("Y: " + yScan + " Begin: " + begin + " end: " + end);
                        if (object.isKset()){
                            rIncr = (sortedR.get(ps+1)-sortedR.get(ps))/(end-begin);
                            if (gIntensity != null){
                                gIncr = (sortedG.get(ps+1)-sortedG.get(ps))/(end-begin);
                                bIncr = (sortedB.get(ps+1)-sortedB.get(ps))/(end-begin);
                            }
                        }
                        
                        
                        for (; begin<=end; begin++){
                            if (gIntensity != null){
                                //System.out.println("R: " + sortedR.get(ps) + " Incr: " + rIncr);
                                if (object.isKset()){
                                    sortedR.set(ps, sortedR.get(ps)+rIncr);
                                    sortedG.set(ps, sortedG.get(ps)+gIncr);
                                    sortedB.set(ps, sortedB.get(ps)+bIncr);

                                    scan.addEntry(
                                        begin,
                                        corIluminacao(new double[]{
                                                        sortedR.get(ps),
                                                        sortedG.get(ps),
                                                        sortedB.get(ps)
                                                      })
                                    );
                                } else {
                                    scan.addEntry(
                                        begin,
                                        Color.BLACK
                                    );
                                }
                                
                            } else {
                                if (object.isKset()){
                                    sortedR.set(ps, sortedR.get(ps)+rIncr);
                                    scan.addEntry(begin, corIluminacao(sortedR.get(ps)));
                                } else {
                                    scan.addEntry(begin, Color.BLACK);
                                }
                            }
                        }                  
                        //System.out.println("SCAN ADDED: " + yScan + " Size: " + size);
                        scans.add(scan);
                        parity = 1;
                    } else parity = 0;
                }

                updateFromScan(yScan+1);
            }
        }
        
        rIlum.clear();
        gIlum.clear();
        bIlum.clear();
        sortedIntercept.clear();
        sortedR.clear();
        sortedG.clear();
        sortedB.clear();
        
    }
    
    public void update(){
        scans.clear();
        calculateScans();
    }
    
    public List<FullScan> getScans(){
        return scans;
    }
        
    private Vertice obtainFirstPointNormal(WE_Aresta aresta){
        Vertice incidente = aresta.getvInicial();
        List<WE_Aresta> arestas = object.getAllArestas();
        
        int counter = 1;
        Vertice normalTotal = new Vertice(aresta.getFaceDireita().getNormalDaFace());
        
        if (aresta.getFaceEsquerda()!= null){
            VMath.soma(normalTotal, aresta.getFaceEsquerda().getNormalDaFace());
            ++counter;
        }
        
        for (WE_Aresta arr : arestas){
            if (arr.getvFinal() == incidente){
                if (arr.getFaceEsquerda()!= null){
                    VMath.soma(normalTotal, arr.getFaceEsquerda().getNormalDaFace());
                    ++counter;
                }
            }
        }
        
        VMath.dividirEscalar(normalTotal, counter);
        
        return normalTotal;
    }
    
    private void interpolateIlumination(WE_Aresta aresta){  
        //System.out.println("HEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE " + (aresta));
        //System.out.println("SLOPE: " + slope.get(slope.size()-1));
        Vertice higher, lower;
        if (slope.get(slope.size()-1) > 0){ //Slope positivo reta é /, pega Min X
            interceptX.add(aresta.getMinX());
            lower  = aresta.getMinXvertice();
            higher = aresta.getMaxXvertice();
        } else { //Slope negativo reta é \, pega Max X  
            interceptX.add(aresta.getMaxX());
            lower  = aresta.getMaxXvertice();
            higher = aresta.getMinXvertice();
        } 
        //System.out.println("LOWER: " + lower);
        //System.out.println("HIGHER: " + higher);
        float heightDiff = (higher.getY() - lower.getY());
        //System.out.println("Lower: " + rIlum.get(lower.getID()));
        //System.out.println("Highr: " + rIlum.get(higher.getID()));
        if (object.isKset()){ //Se os Ks do objeto foram definidos, faz o shading. Se não, pinta de preto.
            rIntensity.add(
                (((higher.getY()+1-lower.getY())/heightDiff)*rIlum.get(higher.getID())) +
                (((                    -1      )/heightDiff)*rIlum.get( lower.getID()))
            ); //(rIlum.get(lower.getID()) - rIlum.get(higher.getID())) / heightDiff
            
            
            if (object.isChromatic()){          
                gIntensity.add(
                    (((higher.getY()+1-lower.getY())/heightDiff)*gIlum.get(higher.getID())) +
                    (((                    -1      )/heightDiff)*gIlum.get( lower.getID()))
                ); //(gIlum.get(lower.getID()) - gIlum.get(higher.getID())) / heightDiff
                bIntensity.add(
                    (((higher.getY()+1-lower.getY())/heightDiff)*bIlum.get(higher.getID())) +
                    (((                    -1      )/heightDiff)*bIlum.get( lower.getID()))
                ); //(bIlum.get(lower.getID()) - bIlum.get(higher.getID())) / heightDiff
            } 
        }
    }
    
    private void sortInterceptX() { 
        int n = interceptX.size(); 
        sortedIntercept.clear();
        sortedR.clear();
        sortedG.clear();
        sortedB.clear();
        
        for (int i=0; i<n; i++){
            sortedIntercept.add(interceptX.get(i));
            if (object.isKset()){
                sortedR.add(rIntensity.get(i));
                if (gIntensity != null){
                    sortedG.add(gIntensity.get(i));
                    sortedB.add(bIntensity.get(i));
                }
            }
            
        } //"Deep" Copy
        
        for (int i=1; i<n; i++) {         
            float key  = sortedIntercept.get(i);
            float rKey = 0, gKey = 0, bKey = 0;
            
            if (object.isKset()){
               rKey = sortedR.get(i); 
               if (gIntensity != null){ gKey = sortedG.get(i); bKey = sortedB.get(i); }
            }
            
            
            int j = i-1; 
  
            /* Move elements of arr[0..i-1], that are 
               greater than key, to one position ahead 
               of their current position */
            while (j>=0 && sortedIntercept.get(j) > key){ 
                //arr[j+1] = arr[j]; 
                sortedIntercept.set(j+1, sortedIntercept .get(j));
                if (object.isKset()){
                    sortedR        .set(j+1, sortedR         .get(j));
                    if (gIntensity != null){
                        sortedG    .set(j+1, sortedG         .get(j));
                        sortedB    .set(j+1, sortedB         .get(j));
                    }
                }
                
                j = j-1; 
            } 
            //arr[j+1] = key; 
            sortedIntercept.set(j+1, key);
            if (object.isKset()){
                sortedR        .set(j+1, rKey);
                if (gIntensity != null){ sortedG.set(j+1, gKey); sortedB.set(j+1, bKey); }
            }
        }
        
        //System.out.println("SORTED INTER: " + sortedIntercept);
        //System.out.println("SORTED R: " + sortedR);
        //System.out.println("Sorted G: " + sortedG);
        //System.out.println("Sorted B: " + sortedB);
    } 
           
    //<editor-fold defaultstate="collapsed" desc="Point Illumination">
        
    private void singleVertexIlumination(WE_Vertice incidente){
        Vertice normal1 = obtainFirstPointNormal(incidente.getArestaIncidente());

        float rInten1,
              gInten1,
              bInten1;
        
        float[] kas = object.getKa();
        if (object.isKset()){ //Se os Ks do objeto foram definidos, faz o shading. Se não, pinta de preto.
            if (object.isChromatic()){
                double[] ilumKA = luzAmbiente.iluminacaoAmbiente(kas[0], kas[1], kas[2]);
                double[] ilumKD = iluminacaoDifusa   (object, normal1, incidente);
                double[] ilumKS = iluminacaoEspecular(object, normal1, incidente);
                rInten1 = ((float) (ilumKA[0]+ilumKD[0]+ilumKS[0]));
                gInten1 = ((float) (ilumKA[1]+ilumKD[1]+ilumKS[1]));
                bInten1 = ((float) (ilumKA[2]+ilumKD[2]+ilumKS[2])); 
                
                rIlum.put(incidente.getID(), rInten1);
                gIlum.put(incidente.getID(), gInten1);
                bIlum.put(incidente.getID(), bInten1);
            } else {
                double ilumKA = luzAmbiente.iluminacaoAmbienteAcromatica(kas[0]);
                double ilumKD = iluminacaoDifusaAcromatica   (object, normal1, incidente);
                double ilumKS = iluminacaoEspecularAcromatica(object, normal1, incidente);
                rInten1 = ((float) (ilumKA+ilumKD+ilumKS));
                
                rIlum.put(incidente.getID(), rInten1);
            }
        }
    }
    
    private void calculateIlumination(){
        for (WE_Vertice incidente : object.getVisiblePoints()){
            singleVertexIlumination(incidente);
        }
    }
    
    private double[] iluminacaoDifusa(HE_Poliedro obj, Vertice normal, Vertice incidente){
        float[] kds = obj.getKd();
        
        double[] pintura = new double[]{0.0,0.0,0.0};
        for (int i=0; i<luzesPontuais.size(); i++){
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
        for (int i=0; i<luzesPontuais.size(); i++){
            double[] ilum = luzesPontuais.get(i).iluminacaoEspecular(kss[0], kss[1], kss[2], (short) kss[3], normal, incidente, observador);
            //System.out.println("ILUM: " + Arrays.toString(ilum));
            pintura[0] += ilum[0];
            pintura[1] += ilum[1];
            pintura[2] += ilum[2];
        }
        
        return pintura;
    }
    
    private double iluminacaoDifusaAcromatica(HE_Poliedro obj, Vertice normal, Vertice incidente){
        float[] kds = obj.getKd();
        
        double pintura = 0.0;
        for (int i=0; i<luzesPontuais.size(); i++){
            double ilum = luzesPontuais.get(i).iluminacaoDifusaAcromatica(kds[0], normal, incidente);
            pintura += ilum;
        }
        
        return pintura;
    }
    
    private double iluminacaoEspecularAcromatica(HE_Poliedro obj, Vertice normal, Vertice incidente){
        float[] kss = obj.getKs();
        
        double pintura = 0.;
        for (int i=0; i<luzesPontuais.size(); i++){
            double ilum = luzesPontuais.get(i).iluminacaoEspecularAcromatica(kss[0], (short) kss[3], normal, incidente, observador);
            pintura += ilum;
        }
        
        return pintura;
    }
    
    private Color corIluminacao(double[] intensidade){
        int r = Math.min((int) Math.round(intensidade[0]), 255);
        int g = Math.min((int) Math.round(intensidade[1]), 255);
        int b = Math.min((int) Math.round(intensidade[2]), 255);
        
        r = Math.max(r, 0);
        g = Math.max(g, 0);
        b = Math.max(b, 0);
        //System.out.println("Color. R: " + r + " G: " + g + " B: " + b);
        return Color.rgb(r, g, b);
    }
    
    private Color corIluminacao(double intensidade){
        int sumInt = Math.min((int) Math.round(intensidade), 255);
        sumInt = Math.max(sumInt, 0);
        return Color.rgb(sumInt, sumInt, sumInt);
    }
//</editor-fold>
}
