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

/**
 *
 * @author Cristhofer
 */
public class FacturaDto {
    
    public SimpleStringProperty id;
    public ObjectProperty<LocalDate> fecha;
    public SimpleStringProperty hora;
    public SimpleStringProperty precioTotal;
    public Long IDUsuario;
    
    List<AsientoxtandaDto> asientosxTandas;
    List<AsientoxtandaDto> asientosxTandasEliminadas;
    
    List<AlimentoxfacturaDto> alimentoxFactura;
    List<AlimentoxfacturaDto> alimentoxFacturaEliminadas;
    
    private Boolean modificado;

    public FacturaDto() {
        this.modificado = false;
        
        this.id = new SimpleStringProperty();
        this.fecha = new SimpleObjectProperty();
        this.hora = new SimpleStringProperty();
        this.precioTotal = new SimpleStringProperty();
        this.IDUsuario = Long.valueOf(0);
        asientosxTandas = FXCollections.observableArrayList();
        asientosxTandasEliminadas = new ArrayList<>();
        
        alimentoxFactura = FXCollections.observableArrayList();
        alimentoxFacturaEliminadas = new ArrayList<>();
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

    public LocalDate getFecha() {
        return fecha.get();
    }

    public void setFecha(LocalDate fecha) {
        this.fecha.set(fecha);
    }

    public String getHora() {
        return hora.get();
    }

    public void setHora(String hora) {
        this.hora.set(hora);
    }

    public Long getPrecioTotal() {
        if(precioTotal.get()!=null && !precioTotal.get().isEmpty())
            return Long.valueOf(precioTotal.get());
        else
            return null;
    }

    public void setPrecioTotal(Long precioTotal) {
        this.precioTotal.set(precioTotal.toString());
    }

    public Long getIdUsuario() {
        return IDUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.IDUsuario = idUsuario;
    }

    public Boolean isModificado() {
        return modificado;
    }

    public void setModificado(Boolean modificado) {
        this.modificado = modificado;
    }

    public List<AsientoxtandaDto> getAsientosxTandas() {
        return asientosxTandas;
    }

    public void setAsientosxTandas(List<AsientoxtandaDto> asientosxTandas) {
        this.asientosxTandas = FXCollections.observableArrayList(asientosxTandas);
    }

    public List<AsientoxtandaDto> getAsientosxTandasEliminadas() {
        return asientosxTandasEliminadas;
    }

    public void setAsientosxTandasEliminadas(List<AsientoxtandaDto> asientosxTandasEliminadas) {
        this.asientosxTandasEliminadas = asientosxTandasEliminadas;
    }

    public List<AlimentoxfacturaDto> getAlimentoxFactura() {
        return alimentoxFactura;
    }

    public void setAlimentoxFactura(List<AlimentoxfacturaDto> alimentoxFactura) {
        this.alimentoxFactura = FXCollections.observableArrayList(alimentoxFactura);
    }

    public List<AlimentoxfacturaDto> getAlimentoxFacturaEliminadas() {
        return alimentoxFacturaEliminadas;
    }

    public void setAlimentoxFacturaEliminadas(List<AlimentoxfacturaDto> alimentoxFacturaEliminadas) {
        this.alimentoxFacturaEliminadas = alimentoxFacturaEliminadas;
    }
    
    @Override
    public String toString() {
        return "FacturaDto{" + "id=" + id + ", fecha=" + fecha + ", hora=" + hora + ", precioTotal=" + precioTotal + '}';
    }
    
}
