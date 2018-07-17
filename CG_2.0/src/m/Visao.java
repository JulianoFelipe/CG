/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m;

import javafx.scene.image.Image;

/**
 *
 * @author JFPS
 */
public enum Visao {   
    //https://stackoverflow.com/questions/5678309/illegal-forward-reference-and-enums
    Frontal    (new Image("/resource/images/Frontal.png")),
    Lateral    (new Image("/resource/images/Lateral.png")),
    Topo       (new Image("/resource/images/Topo.png")),
    Perspectiva(new Image("/resource/images/Perspectiva.png"));
    
    //                                                                                                   Width  Height PreserveRatio Smooth
    private static final Image ORT_AUTO_IMG  = new Image("/resource/images/Barra para Ortográficas.png",   250,    100,    true,     true);
    private static final Image PERS_AUTO_IMG = new Image("/resource/images/Barra para Perspectiva.png" ,   300,    100,    true,     true);
    
    private final Image image;
    private Visao (Image img){
        this.image = img;
    }

    public Image getImage() {
        return image;
    }
    
    public static Image getAutoHotkeyImage(Visao vis){
        if (vis == Perspectiva) return PERS_AUTO_IMG;
        else return ORT_AUTO_IMG;
    }
}
