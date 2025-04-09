/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.cineuna.model;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Cristhofer
 */
public class SalaDto {
    
    
    public SimpleStringProperty id;
    public SimpleStringProperty nombre;
    public ObjectProperty<String> estado;
    public SimpleStringProperty imagenAsiento;
    public SimpleStringProperty imagenFondo;
    public SimpleStringProperty detalle;
    
    ObservableList<TandaDto> tandas;
    List<TandaDto> tandasEliminadas;
    
    ObservableList<AsientoDto> asientos;
    List<AsientoDto> asientosEliminadas;
    
    private Boolean modificado;

    public SalaDto() {
        
        this.modificado = false;
        
        this.id = new SimpleStringProperty();
        this.nombre = new SimpleStringProperty();
        this.estado = new SimpleObjectProperty("A");
        this.imagenAsiento = new SimpleStringProperty();
        this.imagenFondo = new SimpleStringProperty();
        this.detalle = new SimpleStringProperty();
        tandas = FXCollections.observableArrayList();
        tandasEliminadas = new ArrayList<>();
        asientos = FXCollections.observableArrayList();
        asientosEliminadas = new ArrayList<>();
        
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

    public String getEstado() {
        return estado.get();
    }

    public void setEstado(String estado) {
        this.estado.set(estado);
    }

    public String getImagenAsiento() {
        return imagenAsiento.get();
    }

    public void setImagenAsiento(String imagenAsiento) {
        this.imagenAsiento.set(imagenAsiento);
    }

    public String getImagenFondo() {
        return imagenFondo.get();
    }

    public void setImagenFondo(String imagenFondo) {
        this.imagenFondo.set(imagenFondo);
    }

    public String getDetalle() {
        return detalle.get();
    }

    public void setDetalle(String detalle) {
        this.detalle.set(detalle);
    }

    public Boolean isModificado() {
        return modificado;
    }

    public void setModificado(Boolean modificado) {
        this.modificado = modificado;
    }

    public ObservableList<TandaDto> getTandas() {
        return tandas;
    }

//    public void setTandas(ObservableList<TandaDto> tandas) {
//        this.tandas = tandas;
//    }
//    
      public void setTandas(List<TandaDto> tandas) {
        this.tandas = FXCollections.observableArrayList(tandas);
    }

    public List<TandaDto> getTandasEliminadas() {
        return tandasEliminadas;
    }

    public void setTandasEliminadas(List<TandaDto> tandasEliminadas) {
        this.tandasEliminadas = tandasEliminadas;
    }

    public ObservableList<AsientoDto> getAsientos() {
        return asientos;
    }

//    public void setAsientos(ObservableList<TandaDto> asientos) {
//        this.asientos = asientos;
//    }
    public void setAsientos(List<AsientoDto> asientos) {
        this.asientos = FXCollections.observableArrayList(asientos);
    }

    public List<AsientoDto> getAsientosEliminadas() {
        return asientosEliminadas;
    }

    public void setAsientosEliminadas(List<AsientoDto> asientosEliminadas) {
        this.asientosEliminadas = asientosEliminadas;
    }
    
    
    
    
}
