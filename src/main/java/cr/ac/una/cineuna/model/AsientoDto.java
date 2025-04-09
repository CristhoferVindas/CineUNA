/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.cineuna.model;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Cristhofer
 */
public class AsientoDto {
    public SimpleStringProperty id;
    public SimpleStringProperty fila;
    public SimpleStringProperty numero;
    public Long IDsala;
    
    private Boolean modificado;
    
    ObservableList<AsientoDto> asientosxTandas;
    List<AsientoDto> asientosxTandasEliminadas;

    public AsientoDto() {
        this.modificado = false;
        
        this.id = new SimpleStringProperty();
        this.fila = new SimpleStringProperty();
        this.numero = new SimpleStringProperty();
        this.IDsala = Long.valueOf(0);
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

    public String getFila() {
        return fila.get();
    }

    public void setFila(String fila) {
        this.fila.set(fila);
    }

    public Long getNumero() {
        if(numero.get()!=null && !numero.get().isEmpty())
            return Long.valueOf(numero.get());
        else
            return null;
    }

    public void setNumero(Long numero) {
        this.numero.set(numero.toString());
    }

    public Long getIdsala() {
        return IDsala;
    }

    public void setIdsala(Long Idsala) {
        this.IDsala = Idsala;
    }
    
    public Boolean getModificado() {
        return modificado;
    }

    public void setModificado(Boolean modificado) {
        this.modificado = modificado;
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
