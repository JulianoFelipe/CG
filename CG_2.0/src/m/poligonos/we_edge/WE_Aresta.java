/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.poligonos.we_edge;

import m.poligonos.Aresta;
import m.poligonos.Vertice;

/**
 *
 * @author JFPS
 */
public class WE_Aresta extends Aresta{
    
    private WE_Face esquerda,
                    direita;
    
    private WE_Aresta esq_predecessora,
                      esq_sucessora;
    
    private WE_Aresta dir_predecessora,
                      dir_sucessora; 
    
    public WE_Aresta(Vertice I, Vertice F) {
        super(I, F);
    }

    public WE_Aresta(WE_Aresta a) {
        super(a);
        
        esquerda = new WE_Face(a.esquerda);
        direita  = new WE_Face(a.direita);
    }
    
    //<editor-fold defaultstate="collapsed" desc="Getters e Setters">
    public WE_Face getFaceEsquerda() {
        return esquerda;
    }
    
    public void setFaceEsquerda(WE_Face esquerda) {
        this.esquerda = esquerda;
    }
    
    public WE_Face getFaceDireita() {
        return direita;
    }
    
    public void setFaceDireita(WE_Face direita) {
        this.direita = direita;
    }
    
    public WE_Aresta getEsquerdaPredecessora() {
        return esq_predecessora;
    }
    
    public void setEsquerdaPredecessora(WE_Aresta esq_predecessora) {
        this.esq_predecessora = esq_predecessora;
    }
    
    public WE_Aresta getEsquerdaSucessora() {
        return esq_sucessora;
    }
    
    public void setEsquerdaSucessora(WE_Aresta esq_sucessora) {
        this.esq_sucessora = esq_sucessora;
    }
    
    public WE_Aresta getDireitaPredecessora() {
        return dir_predecessora;
    }
    
    public void setDireitaPredecessora(WE_Aresta dir_predecessora) {
        this.dir_predecessora = dir_predecessora;
    }
    
    public WE_Aresta getDireitaSucessora() {
        return dir_sucessora;
    }
    
    public void setDireitaSucessora(WE_Aresta dir_sucessora) {
        this.dir_sucessora = dir_sucessora;
    }
    //</editor-fold>
    
    public String vertexIndicatorString(){
        return "" + super.vInicial + super.vFinal;
    }

    @Override
    public String toString() {
        if (esquerda==null) return super.toString();
        
        StringBuilder string = new StringBuilder("WE_Aresta ");
        string.append(ID).append(" {esquerda=");
        
        String toAppend="";
        if (esquerda==null){ toAppend = "null"; 
        } else { toAppend = "" + esquerda.ID; }
        string.append(toAppend).append(", direita=");
        
        if (direita==null){ toAppend = "null"; 
        } else { toAppend = "" + direita.ID; }
        string.append(toAppend).append(", EsqPRE=");
        
        if (esq_predecessora==null){ toAppend = "null"; 
        } else { toAppend = "" + esq_predecessora.ID; }
        string.append(toAppend).append(", EsqSUC=");
        
        if (esq_sucessora==null){ toAppend = "null"; 
        } else { toAppend = "" + esq_sucessora.ID; }
        string.append(toAppend).append(", DirPRE=");
        
        if (dir_predecessora==null){ toAppend = "null"; 
        } else { toAppend = "" + dir_predecessora.ID; }
        string.append(toAppend).append(", DirSUC=");
        
        if (dir_sucessora==null){ toAppend = "null"; 
        } else { toAppend = "" + dir_sucessora.ID; }
        string.append(toAppend).append("}");
        
        
        return string.toString();
        /*return "WE_Aresta{" + "esquerda=" + esquerda.ID + ", direita=" + direita.ID + ", esq_predecessora=" + esq_predecessora.ID + ", esq_sucessora=" + esq_sucessora.ID +
                ", dir_predecessora=" + dir_predecessora.ID + ", dir_sucessora=" + dir_sucessora.ID + '}';*/
    }
    
}
