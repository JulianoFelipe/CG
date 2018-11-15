/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.shader;

import javafx.scene.paint.Color;
import m.poligonos.Vertice;
import utils.math.VMath;

/**
 *
 * @author JFPS
 */
public class PointLight extends Light{
    
    public PointLight(Vertice pos, Color color) {
        super(pos, color);
    }

    public PointLight(Vertice posicao, float intensidade) {
        super(posicao, intensidade);
    }
    
    public double iluminacaoDifusaAcromatica(float coeficienteAcromatico, Vertice normal, Vertice incidente){
        if (!isChromatic){
            Vertice l = new Vertice(
                (posicao.getX()-incidente.getX()),
                (posicao.getY()-incidente.getY()),
                (posicao.getZ()-incidente.getZ())
            ); VMath.normalizar(l);
            double cos = VMath.produtoEscalar(l, normal);
            
            return red * coeficienteAcromatico * cos;
        } else
            return -1;
    }
    
    public double[] iluminacaoDifusa(float kR, float kG, float kB, Vertice normal, Vertice incidente){
        Vertice l = new Vertice(
                (posicao.getX()-incidente.getX()),
                (posicao.getY()-incidente.getY()),
                (posicao.getZ()-incidente.getZ())
            ); VMath.normalizar(l);
            double cos = VMath.produtoEscalar(l, normal);
        
        if (isChromatic){
            return new double[] {red*kR*cos, green*kG*cos, blue*kB*cos};
        } else
            return new double[] {red*kR*cos, red*kG*cos, red*kB*cos};
    }
    
    public double iluminacaoEspecularAcromatica(float coeficienteAcromatico, short n, Vertice normal, Vertice incidente,  Vertice observador){
        if (!isChromatic){
            Vertice l = new Vertice(
                (posicao.getX()-incidente.getX()),
                (posicao.getY()-incidente.getY()),
                (posicao.getZ()-incidente.getZ())
            ); VMath.normalizar(l);
            VMath.produto(l, 2);
            double esc = VMath.produtoEscalar(l, normal);
            Vertice newNormal = VMath.produto(normal, esc); //Vai alterar o vetor normal no caller
            
            newNormal.setAll(
                newNormal.getX() - l.getX(),
                newNormal.getY() - l.getY(),
                newNormal.getZ() - l.getZ()
            );
            
            Vertice s = new Vertice(
                (observador.getX()-incidente.getX()),
                (observador.getY()-incidente.getY()),
                (observador.getZ()-incidente.getZ())
            ); VMath.normalizar(s);
            
            double cos = VMath.produtoEscalar(s, newNormal);
            
            return red * coeficienteAcromatico * Math.pow(cos, n);
        } else
            return -1;
    }
    
    public double[] iluminacaoEspecular(float kR, float kG, float kB, short n, Vertice normal, Vertice incidente, Vertice observador){
            Vertice l = new Vertice(
                (posicao.getX()-incidente.getX()),
                (posicao.getY()-incidente.getY()),
                (posicao.getZ()-incidente.getZ())
            ); VMath.normalizar(l);
            VMath.produto(l, 2);
            double esc = VMath.produtoEscalar(l, normal);
            Vertice newNormal = VMath.produto(normal, esc); //Vai alterar o vetor normal no caller
            
            newNormal.setAll(
                newNormal.getX() - l.getX(),
                newNormal.getY() - l.getY(),
                newNormal.getZ() - l.getZ()
            );
            
            Vertice s = new Vertice(
                (observador.getX()-incidente.getX()),
                (observador.getY()-incidente.getY()),
                (observador.getZ()-incidente.getZ())
            ); VMath.normalizar(s);
            
            double cos = VMath.produtoEscalar(s, newNormal);
            double cosn = Math.pow(cos, n);
        
        if (isChromatic){
            return new double[] {
                red  * kR * cosn,
                green* kG * cosn,
                blue * kB * cosn
            };
        } else
            return new double[] {
                red  * kR * cosn,
                red  * kG * cosn,
                red  * kB * cosn
            };
    }

    public String descriptionString() {
        String ret = "XYZ("+posicao.getX()+";"+posicao.getY()+";"+posicao.getZ()+");";

        if (isChromatic){
            ret += String.format("RGB(%.2f;%.2f;%.2f) ", red, green, blue);
        } else {
            ret += String.format("Acrom.(%.2f)", red);
        }
        
        return ret;
    }
    
    
}

