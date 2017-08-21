/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Formatador de logs usado para
 * display de logs dentro do 
 * Sistema SIGMA.
 * @author Juliano Felipe da Silva
 */
public class ConsoleFormatter extends Formatter{
    private final Locale locale; 

    /**
     * Constrói um formatador de loggers
     * no padrão SIGMA com locale PT-BR.
     */
    public ConsoleFormatter() {
        this(new Locale("pt", "BR")); //Locale para o Brasil
    }

    /**
     * Constrói um formatador de loggers
     * no padrão SIGMA com locale passado.
     * @param locale Usado na formatação.
     */
    public ConsoleFormatter(Locale locale) {
        this.locale = locale;
    }

    /*
        Quick reference:
        - record.getClass: java.util.logging.LogRecord.
        - record.getThreadID: int
        - record.getLoggerName(): Telas.TelaPrincipal
        - record.getResourceBundleName():
        - getSourceClassName: Telas.TelaPrincipal
        - getSourceMethodName: <init>
        - 
    */
    @Override
    public String format(LogRecord record) {
        Date date=new Date(record.getMillis()); //HH para 0-23 hrs
        //SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy", locale);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss", locale);
        
        /*String ret = formatter.format(date) + " - " + record.getSourceClassName() +
               " " + record.getSourceMethodName() + 
              " - " + record.getLevel() + ": " + record.getMessage() + "\n";*/
        
        String ret = formatter.format(date) + " - " +
                record.getLevel() + ": " + record.getMessage() + "\n";
        
        return ret;
    }
    
}
