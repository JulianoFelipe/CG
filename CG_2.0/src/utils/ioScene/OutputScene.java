/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.ioScene;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import m.poligonos.Poligono;

/**
 *
 * @author JFPS
 */
public class OutputScene {
    public static final String FILE_EXTENSION = ".cgjpab";
    private static final SimpleDateFormat DATE_F = new SimpleDateFormat("HH.mm.ss-dd.MM.yyyy", new Locale("pt", "BR"));
            
    public static void outputToFile(List<Poligono> lista) throws IOException{
        File out = new File("CG-" + DATE_F.format(new Date()) + FILE_EXTENSION);
        
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(out);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(OutputScene.class.getName()).log(Level.SEVERE, null, ex);
        }
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(lista);
        oos.close();
    }
    
    public static void outputToFile(List<Poligono> lista, File file) throws IOException{
        File out = null;
        if (file.isDirectory())
            out = new File(file.getCanonicalPath() + "\\CG-" + DATE_F.format(new Date()) + FILE_EXTENSION);
        else
            out = new File(file+FILE_EXTENSION);
        
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(out);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(OutputScene.class.getName()).log(Level.SEVERE, null, ex);
        }
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(lista);
        oos.close();
    }
}
