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
    Frontal    (new Image("/resource/images/Frontal.png")),
    Lateral    (new Image("/resource/images/Lateral.png")),
    Topo       (new Image("/resource/images/Topo.png")),
    Perspectiva(new Image("/resource/images/Perspectiva.png"));
    
    private final Image image;
    private Visao (Image img){
        this.image = img;
    }

    public Image getImage() {
        return image;
    }
    
    
}
