/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.visualizacao;

import java.io.Serializable;

/**
 *
 * @author Juliano
 */
public final class Ambiente implements Serializable {
    private static final int MAXINSTANCES=1;
    private static int INSTANCES;
    private static Ambiente SINGLETON;

    private Ambiente() {
        if (INSTANCES > MAXINSTANCES) 
            throw new IllegalStateException("Limite de instâncias de "
                    +  this.getClass() + " excedido.");
        else{
            INSTANCES++;
            ///PROCEDER COM CODIGO
        }
    }
    
    public static Ambiente getAmbiente (){
        if (SINGLETON == null){
            return new Ambiente();
        } else {
            return SINGLETON;
        }
    }
    
}
