/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resource.description;

import javafx.scene.image.Image;

/**
 *
 * @author JFPS
 */
public enum Transformacoes {
    Rotacao      ("Rotação",      new Image(Transformacoes.class.getResourceAsStream("/resource/images/rotacao.png"))),
    Translacao   ("Translação",   new Image(Transformacoes.class.getResourceAsStream("/resource/images/translacao.png"))),
    Cisalhamento ("Cisalhamento", new Image(Transformacoes.class.getResourceAsStream("/resource/images/cisalhamento.png"))),
    Escala       ("Escala",       new Image(Transformacoes.class.getResourceAsStream("/resource/images/escala.png")));
    
    public final String NAME;
    public final Image ICON;

    private Transformacoes(String name, Image icon) {
        NAME = name;
        ICON = icon;
    }
    
    public static Transformacoes fromString(String name){
        for(Transformacoes ferr : Transformacoes.values()){
            if (ferr.NAME.equals(name))
                return ferr;
        }
        return null;
    }
    
    public static final String C_NAME = "Transformações";
}
