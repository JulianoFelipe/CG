/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.shader;

import javafx.scene.paint.Color;
import m.poligonos.Vertice;

/**
 *
 * @author JFPS
 */
public abstract class Light {
    public static enum TipoAtenuacao{ Nulo, Distancia, Constantes};
    private static final float ATT_C1 = 1; //TipoAtt = Constantes
    private static final float ATT_C2 = 1;
    private static final float ATT_C3 = 1;
    
    protected double red;
    protected double green;
    protected double blue;
    
    protected Color color;
    protected final Vertice posicao;
    protected boolean isChromatic;
    
    protected Light(Vertice pos, Color color) {
        this.color = color;
        
        red = color.getRed();
        green = color.getGreen();
        blue = color.getBlue();
        
        this.posicao = pos;
        isChromatic = true;
    }

    protected Light(Vertice posicao, float intensidade) {
        this.posicao = posicao;
        red = intensidade;
        isChromatic = false;
    }

    public void setColor(Color color) {
        this.color = color;
        
        red = color.getRed();
        green = color.getGreen();
        blue = color.getBlue();
        
        isChromatic = true;
    }
    
    public void setIntensidade(float intensidade){
        isChromatic = false;
        red = intensidade;
    }
    
    public void setPosition(float x, float y, float z){
        posicao.setAll(x, y, z);
    }
    
    public double fatorAtenuacao(TipoAtenuacao tipo, double distanciaObjetoLuz){
        if (tipo == TipoAtenuacao.Nulo) 
            return 1;
        else if (tipo == TipoAtenuacao.Distancia) 
            return Math.min(1/Math.pow(distanciaObjetoLuz, 2), 1);
        else if (tipo == TipoAtenuacao.Constantes)
            return Math.min(
                (1/(ATT_C1 + (ATT_C2*distanciaObjetoLuz) + (Math.pow(distanciaObjetoLuz, 2)*ATT_C3))),
                    1
            );
        else
            return 1;
    }
    
    public void update(Light toCopyFrom){
        this.isChromatic = toCopyFrom.isChromatic;
        this.red = toCopyFrom.red;
        this.posicao.copyAttributes(toCopyFrom.posicao);
        
        if (isChromatic){
            this.green = toCopyFrom.green;
            this.blue  = toCopyFrom.blue;
            this.color = toCopyFrom.color;
        }
    }
}
