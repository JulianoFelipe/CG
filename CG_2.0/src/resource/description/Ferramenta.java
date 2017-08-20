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
public enum Ferramenta {
    Select("Seleção"),
    Delete("Exclusão"),
    Paint ("Pintura");
    
    private String name;
    private String desc;
    private Image icon;

    private Ferramenta(String desc) {
        this.desc = desc;
    }
    
}
