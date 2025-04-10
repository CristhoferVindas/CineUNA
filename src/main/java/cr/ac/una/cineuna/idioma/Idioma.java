/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.cineuna.idioma;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

/**
 *
 * @author Cristhofer
 */
public class Idioma extends Properties{
    private static final long serialVersionUID = 1L;
    
    public Idioma(String idioma) throws IOException{
        if(idioma == "Espanol")
            getProperties("espannol.properties");
        else if(idioma == "Ingles")
            getProperties("ingles.properties");
        else
            getProperties("espannol.properties");
    }
    
    public Properties getProperties(String idioma) throws IOException{
        InputStream inputStream = new FileInputStream("..\\CineUNA\\src\\main\\java\\cr\\ac\\una\\cineuna\\idioma\\"+idioma);
        Properties propiedades = new Properties();
        propiedades.load(inputStream);        
        return propiedades;
    }
}
