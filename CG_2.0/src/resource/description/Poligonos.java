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
public enum Poligonos {
    Regular  ("Regular",   new Image(Ferramentas.class.getResourceAsStream("/resource/images/regularPolygon.png"))),
    Irregular("Irregular", new Image(Ferramentas.class.getResourceAsStream("/resource/images/Irregular.jpg")));
    
    public final String NAME;
    public final Image ICON;

    private Poligonos(String name, Image icon) {
        NAME = name;
        ICON = icon;
    }
    
    public static Poligonos fromString(String name){
        for(Poligonos pol : Poligonos.values()){
            if (pol.NAME.equals(name))
                return pol;
        }
        return null;
    }
    
    public static final String C_NAME = "Polígonos";
}
