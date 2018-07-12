/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.poligonos.we_edge;

import m.poligonos.Vertice;

/**
 *
 * @author JFPS
 */
public class WE_Vertice extends Vertice{

    private WE_Aresta arestaIncidente;
    
    public WE_Vertice(float x, float y, float z, float w) {
        super(x, y, z, w);
    }

    public WE_Vertice(float x, float y, float z) {
        super(x, y, z);
    }

    public WE_Vertice(WE_Vertice v) {
        super(v);
        arestaIncidente = new WE_Aresta(v.arestaIncidente);
    }
    
    public WE_Aresta getArestaIncidente() {
        return arestaIncidente;
    }

    public void setArestaIncidente(WE_Aresta arestaIncidente) {
        this.arestaIncidente = arestaIncidente;
    }
    
    

    
    
}
