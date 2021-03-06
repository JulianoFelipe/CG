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
public enum CriacaoPrevolucao {
    free     ("Livre",        new Image(Ferramentas.class.getResourceAsStream("/resource/images/GridFree.png"))),
    gridSnap ("Snap To Grid", new Image(Ferramentas.class.getResourceAsStream("/resource/images/GridSnap.png")));
    
    public final String NAME;
    public final Image ICON;

    private CriacaoPrevolucao(String name, Image icon) {
        NAME = name;
        ICON = icon;
    }
    
    public static CriacaoPrevolucao fromString(String name){
        for(CriacaoPrevolucao pol : CriacaoPrevolucao.values()){
            if (pol.NAME.equals(name))
                return pol;
        }
        return null;
    }
    
    public static final String C_NAME = "Criação p/ revolução";
}
