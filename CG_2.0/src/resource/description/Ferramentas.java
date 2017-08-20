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
public enum Ferramentas {
    Select("Seleção",  new Image(Ferramentas.class.getResourceAsStream("/resource/images/select.png"))),
    Delete("Exclusão", new Image(Ferramentas.class.getResourceAsStream("/resource/images/delete.png"))),
    Paint ("Pintura",  new Image(Ferramentas.class.getResourceAsStream("/resource/images/paint.png")));
    
    public final String NAME;
    public final Image ICON;

    private Ferramentas(String name, Image icon) {
        NAME = name;
        ICON = icon;
    }
    
    public static Ferramentas fromString(String name){
        for(Ferramentas ferr : Ferramentas.values()){
            if (ferr.NAME.equals(name))
                return ferr;
        }
        return null;
    }
    
    public static final String C_NAME = "Ferramentas";
}
