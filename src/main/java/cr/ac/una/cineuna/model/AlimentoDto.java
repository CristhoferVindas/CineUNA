/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.cineuna.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Cristhofer
 */
public class AlimentoDto {
    public SimpleStringProperty id;
    public SimpleStringProperty nombre;
    public SimpleStringProperty precio;
    public ObjectProperty<String> tipo;
    
    
    private Boolean modificado;

    public AlimentoDto() {
        this.modificado = false;
        
        this.id = new SimpleStringProperty();
        this.nombre = new SimpleStringProperty();
        this.precio = new SimpleStringProperty();
        this.tipo = new SimpleObjectProperty("C"); 
    }
    public Long getId() {
        if(id.get()!=null && !id.get().isEmpty())
            return Long.valueOf(id.get());
        else
            return null;
    }

    public void setId(Long id) {
        this.id.set(id.toString());
    }

    public String getNombre() {
        return nombre.get();
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public Long getPrecio() {
        if(precio.get()!=null && !precio.get().isEmpty())
            return Long.valueOf(precio.get());
        else
            return null;
    }

    public void setPrecio(Long precio) {
        this.precio.set(precio.toString());
    }


    public String getTipo() {
        return tipo.get();
    }

    public void setTipo(String tipo) {
        this.tipo.set(tipo);
    }

    public Boolean isModificado() {
        return modificado;
    }

    public void setModificado(Boolean modificado) {
        this.modificado = modificado;
    }

    @Override
    public String toString() {
        return nombre.get();
    }

   
    
}
