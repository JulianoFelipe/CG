/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.shader.scans;

import java.util.ArrayList;
import java.util.List;
import m.poligonos.Vertice;
import m.poligonos.we_edge.WE_Aresta;

/**
 *
 * @author JFPS
 */
public class ExtremityScanLine {
    private List<WE_Aresta> object;

    private final List<ExtremityScan> scans;
    
    private final List<Float> slope;
    private List<WE_Aresta> nonProcessedEdges;
    private final List<WE_Aresta> activeEdges;
    
    public ExtremityScanLine(List<WE_Aresta> object) {
        this.object = object;
        scans = new ArrayList();
        
        slope = new ArrayList();
        activeEdges = new ArrayList();
        
        calculateScans();
    }
    
    private float[] getMinMax(){
        float min = object.get(0).getMinY(),
              max = object.get(0).getMaxY();
        
        for (int i=1; i<object.size(); i++){
            WE_Aresta a = object.get(i);
            
            if (a.getMinY() < min) min = a.getMinY();
            if (a.getMaxY() > max) max = a.getMaxY();
        }
        
        return new float[]{min, max};
    }
     
    private void initialize(int firstScanLine){
        slope.clear();
        activeEdges.clear();
        scans.clear();
        nonProcessedEdges = object;
        
        List<Vertice> firstScn = new ArrayList<>();
        
        for(int i=0; i<nonProcessedEdges.size(); i++){
            WE_Aresta a = nonProcessedEdges.get(i);
            System.out.println("Edge non proc: " + a + " Size: " + nonProcessedEdges.size());
            if (firstScanLine >= Math.round(a.getMinY()) &&
                firstScanLine <= Math.round(a.getMaxY())){
                System.out.println("Inside scan y="+firstScanLine);
                float ty = Math.round(Math.abs(a.getvInicial().getY() - a.getvFinal().getY()));
                if (ty == Float.POSITIVE_INFINITY){ System.out.println("TY Inf"); ty = 0; }
                
                if (ty > 0){ //Aresta não é horizontal, então adiciona
                    System.out.println("TY="+ty);
                    float tx = Math.round(Math.abs(a.getvInicial().getX() - a.getvFinal().getX()));
                    System.out.println("TX="+tx);
                    if (tx == Float.POSITIVE_INFINITY){ System.out.println("TX INF"); tx = 1; }
                    if (tx == 0.) slope.add((float) 0.);
                    else          slope.add(ty/tx);
                    activeEdges.add(a);

                    if(Math.round(a.getvInicial().getY()) == firstScanLine){
                        System.out.println("VINI SCAN");
                        firstScn.add(a.getvInicial()); firstScn.add(a.getvFinal());
                    } else if (Math.round(a.getvFinal().getY()) == firstScanLine){
                        System.out.println("VFIN SCAN");
                        firstScn.add(a.getvInicial()); firstScn.add(a.getvFinal());
                    }
                }
                nonProcessedEdges.remove(i); //Removes even if horizontal
                i--;
            }
        }
        System.out.println("FIRST ARR: " + firstScn);
        for (int i=0; i<firstScn.size(); i+=2){
            Vertice ini = firstScn.get(i);
            Vertice fin = firstScn.get(i+1);
            System.out.println("Initial scan: (" + Math.round(ini.getX()) + ", " + Math.round(ini.getY()) + "); (" + Math.round(fin.getX()) + ", " + Math.round(fin.getY()) + ")");
            scans.add(
                new ExtremityScan(
                    (ini.getX()), (fin.getX()),
                    (ini.getY()), (fin.getY())
                )
            );
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
        System.out.println("UPDATE : y : " + scanline);
        for(int i=0; i<activeEdges.size(); i++){
            WE_Aresta a = activeEdges.get(i);
            if (scanline <  Math.round(a.getMinY()) ||
                scanline >  Math.round(a.getMaxY())){
                System.out.println("Remove edge: " + a);
                activeEdges.remove(i);
                slope.remove(i);
            }
        }
        
        for(int i=0; i<nonProcessedEdges.size(); i++){
            WE_Aresta a = nonProcessedEdges.get(i);
            if (scanline >  Math.round(a.getMinY()) &&
                scanline <= Math.round(a.getMaxY())){
                System.out.println("Add edge: " + a);
                float ty = Math.round(Math.abs(a.getvInicial().getY() - a.getvFinal().getY()));
                if (ty == Float.POSITIVE_INFINITY){ System.out.println("TY Inf"); ty = 0; }
                
                if (ty > 0){
                    float tx = Math.round(Math.abs(a.getvInicial().getX() - a.getvFinal().getX()));
                    if (tx == Float.POSITIVE_INFINITY){ System.out.println("TX INF"); tx = 1; }
                    if (tx == 0.) slope.add((float) 0.);
                    else          slope.add(ty/tx);

                    System.out.println("Added slope: " + (ty/tx));
                    activeEdges.add(a);
                }
                nonProcessedEdges.remove(i); //Removes even if horizontal
            }
        }
    }
    
    private void calculateScans(){
        float[] minMax = getMinMax();
        int min = Math.round(minMax[0]);
        int max = Math.round(minMax[1]);
        //System.out.println("Min: " + min + " Max: " + max);
        initialize(min);
        
        for (int yScan=min+1; yScan<=max; yScan++){        
            System.out.println("SCNS LIST: " + scans);
            //byte parity = 0;
            int size = activeEdges.size();
            int offset = scans.size()-(int)(size/2);
            for (int ps=0; ps <size; ps+=2, offset++) {
                ExtremityScan active1 = scans.get(offset); //Problemas com faces com "buracos", já que pega apenas a scan ligeiramente anterior
                System.out.println("xIni: " + (active1.getxIni()+slope.get(ps)) + "  xFin: " + (active1.getxFin()+slope.get(ps+1)));
                System.out.println("Active: " + active1);
                System.out.println("Slope: " + slope.get(ps));
                System.out.println("Slope2: " + slope.get(ps+1));
                scans.add(
                    new ExtremityScan(                                                  //Scan "yScan"
                        active1.getXinicial()+slope.get(ps), active1.getXfinal()+slope.get(ps+1), 
                        yScan, yScan
                    )
                );
            }
            
            
            updateFromScan(yScan+1);
        }
        System.out.println("SCANS: " + scans.size());
    }
    
    public void update(){
        calculateScans();
    }
    
    public List<ExtremityScan> getScans(){
        return scans;
    }
}
