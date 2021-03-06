/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.shader;

import javafx.scene.paint.Color;

/**
 *
 * @author JFPS
 */
public class AmbientLight extends Light{
    
    public AmbientLight(Color color) {
        super(null, color);
    }

    public AmbientLight(float intensidade) {
        super(null, intensidade);
    }
    
    public double iluminacaoAmbienteAcromatica(float coeficienteAcromatico){
        if (!isChromatic)
            return red * coeficienteAcromatico;
        else
            return -1;
    }
    
    public double[] iluminacaoAmbiente(float kR, float kG, float kB){
        if (isChromatic)
            return new double[] {red*kR, green*kG, blue*kB };
        else
            return new double[] {red*kR, red*kG, red*kB };
    }
}
