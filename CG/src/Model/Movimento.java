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
}
