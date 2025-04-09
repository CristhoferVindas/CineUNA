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
public class TandaDto {
    
    
    public SimpleStringProperty id;
    public SimpleStringProperty costoEntrada;
    public SimpleStringProperty horaInicio;
    public SimpleStringProperty horaFin;
    public ObjectProperty<LocalDate> fecha;
    public ObjectProperty<String> idioma;
    
    public Long IDPelicula;
    public Long IDSala;
   
    private Boolean modificado;
    
    ObservableList<AsientoDto> asientosxTandas;
    List<AsientoDto> asientosxTandasEliminadas;

    public TandaDto() {
        this.modificado = false;
        
        this.id = new SimpleStringProperty();
        this.costoEntrada = new SimpleStringProperty();
        this.horaInicio = new SimpleStringProperty();
        this.horaFin = new SimpleStringProperty();
        this.fecha = new SimpleObjectProperty();
        this.idioma = new SimpleObjectProperty("E");
        this.IDPelicula = Long.valueOf(0);
        this.IDSala = Long.valueOf(0);
        asientosxTandas = FXCollections.observableArrayList();
        asientosxTandasEliminadas = new ArrayList<>();
        
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

    public Long getCostoEntrada() {
        if(costoEntrada.get()!=null && !costoEntrada.get().isEmpty())
            return Long.valueOf(costoEntrada.get());
        else
            return null;
    }

    public void setCostoEntrada(Long costoEntrada) {
        this.costoEntrada.set(costoEntrada.toString());
    }

    public String getHoraInicio() {
        return horaInicio.get();
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio.set(horaInicio);
    }

    public String getHoraFin() {
        return horaFin.get();
    }

    public void setHoraFin(String horaFin) {
        this.horaFin.set(horaFin);
    }

    public LocalDate getFecha() {
        return fecha.get();
    }

    public void setFecha(LocalDate fechaInicio) {
        this.fecha.set(fechaInicio);
    }

    public String getIdioma() {
        return idioma.get();
    }

    public void setIdioma(String idioma) {
        this.idioma.set(idioma);
    }

    public Boolean isModificado() {
        return modificado;
    }

    public void setModificado(Boolean modificado) {
        this.modificado = modificado;
    }

    public Long getIDPelicula() {
        return IDPelicula;
    }

    public void setIDPelicula(Long IDPelicula) {
        this.IDPelicula = IDPelicula;
    }

    public Long getIDSala() {
        return IDSala;
    }

    public void setIDSala(Long IDSala) {
        this.IDSala = IDSala;
    }

     public ObservableList<AsientoDto> getAsientosxTandas() {
        return asientosxTandas;
    }

//    public void setAsientosxTandas(ObservableList<AsientoDto> asientosxTandas) {
//        this.asientosxTandas = asientosxTandas;
//    }

     public void setAsientosxTandas(List<AsientoDto> asientosxTandas) {
        this.asientosxTandas = FXCollections.observableArrayList(asientosxTandas);
    }
    public List<AsientoDto> getAsientosxTandasEliminadas() {
        return asientosxTandasEliminadas;
    }

    public void setAsientosxTandasEliminadas(List<AsientoDto> asientosxTandasEliminadas) {
        this.asientosxTandasEliminadas = asientosxTandasEliminadas;
    }
    
    
    
    
    
    
}
