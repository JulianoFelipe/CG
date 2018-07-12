/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.poligonos.we_edge;

/**
 *
 * @author JFPS
 */
public class WE_Face {
    private static int INSTANCES=0;
    public final int ID=INSTANCES++;
    private WE_Aresta arestaDaFace;

    public WE_Face() {
    }

    public WE_Face(WE_Face copy) {
        arestaDaFace = new WE_Aresta(arestaDaFace);
    }

    public WE_Aresta getArestaDaFace() {
        return arestaDaFace;
    }

    public void setArestaDaFace(WE_Aresta arestaDaFace) {
        this.arestaDaFace = arestaDaFace;
    }

    @Override
    public String toString() {
        if (arestaDaFace == null){
            return "WE_Face " + ID + "{arestaDaFace=null";
        } else {
            return "WE_Face " + ID + "{arestaDaFace=" + arestaDaFace.vertexIndicatorString() + '}';
        }
    }
}
