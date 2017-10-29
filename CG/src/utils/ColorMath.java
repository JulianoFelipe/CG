/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.awt.Color;

/**
 *
 * @author JFPS
 */
public class ColorMath {
    
    public static boolean isThisColorCloseToThatOne(Color thisC, Color that){
             if (thisC == Color.RED)   return isColorCloseToRed  (that);
        else if (thisC == Color.GREEN) return isColorCloseToGreen(that);
        else if (thisC == Color.BLUE)  return isColorCloseToBlue (that);
        else throw new IllegalArgumentException("Naïveté did not got that far...");
    }
    
    public static boolean isColorCloseToRed(Color color){
        if (color.getRed() < 200) return false;
        /*
            Função naive para determinar se uma cor é próxima a vermelho,
            que é a cor primária usada para poligonos selecionados.
        
            P.S.: Os valores "mágicos" são os valores observados no Color Chooser
            e julgados aproximadamente adequados para a situação.
        */
        
        if (color.getBlue()>60 || color.getGreen()>60) return false;
        
        return color.getAlpha() >= 130;
    }
    
    public static boolean isColorCloseToGreen(Color color){
        if (color.getRed() < 200) return false;
        if (color.getBlue()>60 || color.getGreen()>60) return false;
        return color.getAlpha() >= 130;
    }
    
    public static boolean isColorCloseToBlue(Color color){
        if (color.getRed() < 200) return false;
        if (color.getBlue()>60 || color.getGreen()>60) return false;
        return color.getAlpha() >= 130;
    }
    
    //https://stackoverflow.com/questions/13522229/detect-if-color-is-in-range
}
