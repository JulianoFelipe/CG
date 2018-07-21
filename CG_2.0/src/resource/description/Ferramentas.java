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
    Select     ("Seleção",     new Image(Ferramentas.class.getResourceAsStream("/resource/images/select.png"))),
    LuzAmbiente("Luz Ambiente", new Image(Ferramentas.class.getResourceAsStream("/resource/images/light_ambient.png"))),
    LuzPontual ("Luzes Pontuais",  new Image(Ferramentas.class.getResourceAsStream("/resource/images/light_point.png")));
    
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
