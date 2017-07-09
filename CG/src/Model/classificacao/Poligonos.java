/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.classificacao;

/**
 * Classificação de poligonos
 * segundo o número de lados.
 * @author Juliano
 */
public enum Poligonos {
    Triangulo    ( 3, "Triângulo"),
    Quadrado     ( 4, "Quadrado"),
    Pentagono    ( 5, "Pentágono"),
    Hexagono     ( 6, "Hexágono"),
    Heptagono    ( 7, "Heptágono"),
    Octagono     ( 8, "Octágono"),
    Eneagono     ( 9, "Eneágono"),
    Decagono     (10, "Decágono"),
    Undecagono   (11, "Undecágono"),
    Dodecagono   (12, "Dodecágono"),
    Tridecagono  (13, "Tridecágono"),
    Tetradecagono(14, "Tetradecágono"),
    Pentadecagono(15, "Pentadecágono"),
    Hexadecagono (16, "Hexadecágono"),
    Heptadecagono(17, "Heptadecágono"),
    Octadecagono (18, "Octadecágono"),
    Eneadecagono (19, "Eneadecágono"),
    Icosagono    (20, "Icoságono");
    
    
    private final int codigo;
    private final String nome;
    private Poligonos(int codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
    }

    public static Poligonos porCodigo (int umCodigo){
        for (Poligonos codigo : Poligonos.values())
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