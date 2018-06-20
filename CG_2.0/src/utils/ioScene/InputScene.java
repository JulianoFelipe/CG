/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.ioScene;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import m.poligonos.CGObject;

/**
 *
 * @author JFPS
 */
public class InputScene {

    public static List<CGObject> getListFromFile(File arquivo) throws IOException, ClassNotFoundException{
        if (!arquivo.getName().endsWith(OutputScene.FILE_EXTENSION))
            throw new IllegalArgumentException("Arquivo sem a extensão gerada pelo programa: " + arquivo.getName());
        
        FileInputStream fis = new FileInputStream(arquivo);
        ObjectInputStream ois = new ObjectInputStream(fis);
        List<CGObject> result = (List<CGObject>) ois.readObject();
        ois.close();
        
        return result;
    }
}
