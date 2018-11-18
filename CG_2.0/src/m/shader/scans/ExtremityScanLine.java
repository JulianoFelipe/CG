/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.shader.scans;

import java.util.ArrayList;
import java.util.List;
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
    private final List<Float> interceptX;
    
    public ExtremityScanLine(List<WE_Aresta> object) {
        this.object = object;
        scans = new ArrayList();
        interceptX = new ArrayList();
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
        //System.out.println("Minmax: " + min + " Max: " + max);
        return new float[]{min, max};
    }
     
    private void initialize(int firstScanLine){
        slope.clear();
        activeEdges.clear();
        scans.clear();
        interceptX.clear();
        nonProcessedEdges = object;
        
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
                    
                    if (slope.get(slope.size()-1) > 0) interceptX.add(a.getMinX()); //Slope positivo reta é /, pega Min X
                    else                               interceptX.add(a.getMaxX()); //Slope negativo reta é \, pega Max X
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
                    if (slope.get(slope.size()-1) > 0) interceptX.add(a.getMinX()); //Slope positivo reta é /, pega Min X
                    else                               interceptX.add(a.getMaxX()); //Slope negativo reta é \, pega Max X
                }
                nonProcessedEdges.remove(i); //Removes even if horizontal
                --i;
            }
        }
    }
    
    private void calculateScans(){
        float[] minMax = getMinMax();
        int min = Math.round(minMax[0]);
        int max = Math.round(minMax[1]);
        //System.out.println("Min: " + min + " Max: " + max);
        initialize(min);
        //System.out.println("MAX Y: " + max);
        for (int yScan=min+1; yScan<=max; yScan++){        
            int size = activeEdges.size();
            //System.out.println("Active Edges: " + activeEdges);
            //System.out.println("Intercepts: " + interceptX);
            for (int ps=0; ps<size; ps++) {  
                //System.out.print("X: " + interceptX.get(ps));
                interceptX.set(ps,   interceptX.get(ps)+slope.get(ps));
                
                //System.out.println(" New X: " + interceptX.get(ps) + " Slope: " + slope.get(ps));
            }
            
            List<Float> sortedIntercept = sortInterceptX();
            
            byte parity=0;
            for (int ps=0; ps<size-1; ps++){
                if (parity==0){
                    scans.add(
                        new ExtremityScan(                                                  //Scan "yScan"
                            Math.round(sortedIntercept.get(ps)), Math.round(sortedIntercept.get(ps+1)), 
                            yScan, yScan
                        )
                    );
                    
                    parity = 1;
                } else parity = 0;
            }
            
            
            updateFromScan(yScan+1);
        }
        //System.out.println("SCANS: " + scans.size());
        //System.out.println(scans);
        
        //Limpa todas as estruturas extras
        slope.clear();
        activeEdges.clear();
        interceptX.clear();
    }
    
    public void update(){
        calculateScans();
    }
    
    public List<ExtremityScan> getScans(){
        return scans;
    }
        
    private List<Float> sortInterceptX() { 
        int n = interceptX.size(); 
        List<Float> sorted = new ArrayList(n);
        
        for (int i=0; i<n; i++) sorted.add(interceptX.get(i)); //"Deep" Copy
        
        for (int i=1; i<n; ++i) {         
            float key = sorted.get(i);
            int j = i-1; 
  
            /* Move elements of arr[0..i-1], that are 
               greater than key, to one position ahead 
               of their current position */
            while (j>=0 && sorted.get(i) > key){ 
                //arr[j+1] = arr[j]; 
                sorted.set(j+1, sorted .get(j));
                
                j = j-1; 
            } 
            //arr[j+1] = key; 
            sorted.set(j+1, key);
        } 
        
        return sorted;
    } 
    
    //private final List<Float> slope;
    //private final List<WE_Aresta> activeEdges;
    //private final List<Float> interceptX;
}
