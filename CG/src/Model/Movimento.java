/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author JFPS
 */
public enum Movimento {
    Estatico (0),
    Cima     (1),
    Baixo    (2),
    Esquerda (4),
    Direita  (8),
    Cima_Esquerda  (Cima.flags  | Esquerda.flags),
    Cima_Direita   (Cima.flags  | Direita.flags),
    Baixo_Esquerda (Baixo.flags | Esquerda.flags),
    Baixo_Direita  (Baixo.flags | Direita.flags);
    
    private int flags;

    private Movimento(int flags) {
        this.flags = flags;
    }
    
    public Movimento decomporEmHorizontal(Movimento mov){
        Movimento rightDec = fromFlags(mov.flags & Direita.flags);
        Movimento leftDec  = fromFlags(mov.flags & Esquerda.flags);
        
        if (rightDec.flags == Direita.flags)
            return Direita;
        else if (leftDec.flags == Esquerda.flags)
            return Esquerda;
        else 
            return Estatico;
    }
    
    public Movimento decomporEmVertical(Movimento mov){
        Movimento cimaDec = fromFlags(mov.flags & Cima.flags);
        Movimento baixDec = fromFlags(mov.flags & Baixo.flags);
        
        if (cimaDec.flags == Cima.flags)
            return Cima;
        else if (baixDec.flags == Baixo.flags)
            return Baixo;
        else 
            return Estatico;
    }
    
    private Movimento fromFlags(int flags){
        for (Movimento m : Movimento.values())
            if (m.flags == flags)
                return m;
        return Estatico;
    }
}
