/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.classificacao;

/**
 * Outros solidos,
 * que não entram
 * nas outras classificações.
 * @author Juliano
 */
public enum Cônicas {
    Circunferencia (0, "Circunferência");
    
    private final int codigo;
    private final String nome;
    private Cônicas(int codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
    }

    public static Cônicas porCodigo (int umCodigo){
        for (Cônicas codigo : Cônicas.values())
            if (umCodigo == codigo.codigo) return codigo;
        throw new IllegalArgumentException ("Código inválido. Limite excedido.");
    }
    
    public int getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }
}
