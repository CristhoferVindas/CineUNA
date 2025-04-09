/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.cineuna.model;

import java.time.LocalDate;
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
public class PeliculaDto {
    
    public SimpleStringProperty id;
    public SimpleStringProperty nombreEspannol;
    public SimpleStringProperty nombreIngles;
    public ObjectProperty<String> idiomasDisponibles;
    public SimpleStringProperty trailerEspannol;
    public SimpleStringProperty trailerIngles;
    public SimpleStringProperty portadaEspannol;
    public SimpleStringProperty portadaIngles;
    public ObjectProperty<LocalDate> fechaEstreno;
    public ObjectProperty<LocalDate> fechaFin;
    public ObjectProperty<String> estado;
    public SimpleStringProperty resennaEspannol;
    public SimpleStringProperty resennaIngles;
    
    
    ObservableList<TandaDto> tandas;
    List<TandaDto> tandasEliminadas;

    public Boolean modificado;

    public PeliculaDto() {
        
        this.modificado = false;
        
        this.id = new SimpleStringProperty();
        this.nombreEspannol = new SimpleStringProperty();
        this.nombreIngles = new SimpleStringProperty();
        this.idiomasDisponibles = new SimpleObjectProperty("E");
        this.trailerEspannol = new SimpleStringProperty();
        this.trailerIngles = new SimpleStringProperty();
        this.portadaEspannol = new SimpleStringProperty();
        this.portadaIngles = new SimpleStringProperty();
        this.fechaEstreno = new SimpleObjectProperty();
        this.fechaFin = new SimpleObjectProperty();
        this.estado = new SimpleObjectProperty("D");
        this.resennaEspannol = new SimpleStringProperty();
        this.resennaIngles = new SimpleStringProperty();
        
        tandas = FXCollections.observableArrayList();
        tandasEliminadas = new ArrayList<>();
 
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

    public String getNombreEspannol() {
        return nombreEspannol.get();
    }

    public void setNombreEspannol(String nombreEspannol) {
        this.nombreEspannol.set(nombreEspannol);
    }

    public String getNombreIngles() {
        return nombreIngles.get();
    }

    public void setNombreIngles(String nombreIngles) {
        this.nombreIngles.set(nombreIngles);
    }

    public String getIdiomasDisponibles() {
        return idiomasDisponibles.get();
    }

    public void setIdiomasDisponibles(String idiomasDisponibles) {
        this.idiomasDisponibles.set(idiomasDisponibles);
    }

    public String getTrailerEspannol() {
        return trailerEspannol.get();
    }

    public void setTrailerEspannol(String trailerEspannol) {
        this.trailerEspannol.set(trailerEspannol);
    }

    public String getTrailerIngles() {
        return trailerIngles.get();
    }

    public void setTrailerIngles(String trailerIngles) {
        this.trailerIngles.set(trailerIngles);
    }

    public String getPortadaEspannol() {
        return portadaEspannol.get();
    }

    public void setPortadaEspannol(String portadaEspannol) {
        this.portadaEspannol.set(portadaEspannol);
    }

    public String getPortadaIngles() {
        return portadaIngles.get();
    }

    public void setPortadaIngles(String portadaIngles) {
        this.portadaIngles.set(portadaIngles);
    }

    public LocalDate getFechaEstreno() {
        return fechaEstreno.get();
    }

    public void setFechaEstreno(LocalDate fechaEstreno) {
        this.fechaEstreno.set(fechaEstreno);
    }

    public LocalDate getFechaFin() {
        return fechaFin.get();
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin.set(fechaFin);
    }
    
    

    public String getEstado() {
        return estado.get();
    }

    public void setEstado(String estado) {
        this.estado.set(estado);
    }

    public String getResennaEspannol() {
        return resennaEspannol.get();
    }

    public void setResennaEspannol(String resennaEspannol) {
        this.resennaEspannol.set(resennaEspannol);
    }

    public String getResennaIngles() {
        return resennaIngles.get();
    }

    public void setResennaIngles(String resennaIngles) {
        this.resennaIngles.set(resennaIngles);
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
    

    @Override
    public String toString() {
        return "PeliculaDto{" + "id=" + id + ", nombreEspannol=" + nombreEspannol + ", nombreIngles=" + nombreIngles + ", idiomasDisponibles=" + idiomasDisponibles + ", fechaEstreno=" + fechaEstreno + ", estado=" + estado + '}';
    }
    
   
    
}
