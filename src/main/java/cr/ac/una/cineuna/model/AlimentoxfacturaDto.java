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
public class AlimentoxfacturaDto {
    public SimpleStringProperty id;
    public SimpleStringProperty cantidad;
    public SimpleStringProperty precio;   
    public Long idFactura;
    public Long idAlimento;

    private Boolean modificado;

    public AlimentoxfacturaDto() {
        this.modificado = false;
        this.id = new SimpleStringProperty();
        this.cantidad = new SimpleStringProperty();
        this.precio = new SimpleStringProperty();
        
        this.idFactura = Long.valueOf(0);
        this.idAlimento = Long.valueOf(0);
   
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

    public Long getCantidad() {
        if(cantidad.get()!=null && !cantidad.get().isEmpty())
            return Long.valueOf(cantidad.get());
        else
            return null;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad.set(cantidad.toString());
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

    
//    public Long getIdFactura() {
//        if(idFactura.get()!=null && !idFactura.get().isEmpty())
//            return Long.valueOf(idFactura.get());
//        else
//            return null;
//    }
//
//    public void setIdFactura(Long idFactura) {
//        this.idFactura.set(idFactura.toString());
//    }
//
//    public Long getIdAlimento() {
//        if(idAlimento.get()!=null && !idAlimento.get().isEmpty())
//            return Long.valueOf(idAlimento.get());
//        else
//            return null;
//    }
//
//    public void setIdAlimento(Long idAlimento) {
//        this.idAlimento.set(idAlimento.toString());
//    }

    public Long getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(Long idFactura) {
        this.idFactura = idFactura;
    }

    public Long getIdAlimento() {
        return idAlimento;
    }

    public void setIdAlimento(Long idAlimento) {
        this.idAlimento = idAlimento;
    }
    
    public Boolean getModificado() {
        return modificado;
    }

    public void setModificado(Boolean modificado) {
        this.modificado = modificado;
    }

    @Override
    public String toString() {
        return "AlimentoxfacturaDto{" + "id=" + id + ", cantidad=" + cantidad + ", precio=" + precio + '}';
    }
    
}
