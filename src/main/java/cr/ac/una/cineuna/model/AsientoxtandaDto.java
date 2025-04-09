/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.cineuna.model;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Cristhofer
 */
public class AsientoxtandaDto {
    
    public SimpleStringProperty id;
    private Long IDTanda;
    private Long  IDAsiento;
     private Long  IDFactura;
    private Boolean modificado;

    
    public AsientoxtandaDto() {
        this.modificado = false;
        this.id = new SimpleStringProperty();
        this.IDAsiento = Long.valueOf(0);
        this.IDTanda = Long.valueOf(0);
         this.IDFactura = Long.valueOf(0);
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

    public Boolean isModificado() {
        return modificado;
    }

    public void setModificado(Boolean modificado) {
        this.modificado = modificado;
    }

    public Long getIDTanda() {
        return IDTanda;
    }

    public void setIDTanda(Long IDTanda) {
        this.IDTanda = IDTanda;
    }

    public Long getIDAsiento() {
        return IDAsiento;
    }

    public void setIDAsiento(Long IDAsiento) {
        this.IDAsiento = IDAsiento;
    }

    public Long getIDFactura() {
        return IDFactura;
    }

    public void setIDFactura(Long IDFactura) {
        this.IDFactura = IDFactura;
    }

    
    
    @Override
    public String toString() {
        return "AsientoxtandaDto{" + "id=" + id  + ", modificado=" + modificado + '}';
    }
    
    
    
    
    
    
    
}
