/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m;

/**
 *
 * @author JFPS
 */
public enum Eixo {
    Eixo_X  (1),
    Eixo_Y  (2),
    Eixo_Z  (4),
    Eixo_XY (Eixo_X.flags | Eixo_Y.flags),
    Eixo_XZ (Eixo_X.flags | Eixo_Z.flags),
    Eixo_YZ (Eixo_Y.flags | Eixo_Z.flags),
    Eixo_XYZ(Eixo_X.flags | Eixo_Y.flags | Eixo_Z.flags);
    
    private int flags;
    
    private Eixo(int flags) {
        this.flags = flags;
    }
}
