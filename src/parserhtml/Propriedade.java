/*
 * To change this license header, choose License Headers in Project Propriedade.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parserhtml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

public class Propriedade {
    private Properties prop = new Properties();
    private OutputStream output = null;
    private InputStream input = null;
    private String caminho;
    
    public Propriedade(String caminho) {
        this.caminho = caminho;
    }
    
    public void abrir() {
        
    }
    
    public void fechar() {
        try {
            output.close();
        } catch (IOException ex) {
            getLogger(Propriedade.class.getName()).log(SEVERE, null, ex);
        }
    }
    
    public void escrever(String indice, String valor) {
        try {
            output = new FileOutputStream(caminho);            
        } catch (FileNotFoundException ex) {
            getLogger(Propriedade.class.getName()).log(SEVERE, null, ex);
        }
        
        prop.setProperty(indice, valor);
        
        try {
            prop.store(output, null);
        } catch (IOException ex) {
            getLogger(Propriedade.class.getName()).log(SEVERE, null, ex);
        } finally {
            try {
                output.close();
            } catch (IOException ex) {
                getLogger(Propriedade.class.getName()).log(SEVERE, null, ex);
            }
        }
    }
    
    public String ler(String indice) {
       try {
            input = new FileInputStream(caminho);            
        } catch (FileNotFoundException ex) {
            getLogger(Propriedade.class.getName()).log(SEVERE, null, ex);
        } finally {
           try {
               input.close();
           } catch (IOException ex) {
                getLogger(Propriedade.class.getName()).log(SEVERE, null, ex);
           }
       }
        
        return prop.getProperty(indice);
                
    }
    
}
