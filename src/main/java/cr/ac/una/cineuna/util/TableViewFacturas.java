/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.cineuna.util;

import java.time.LocalDate;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Cristhofer
 */
public class TableViewFacturas {

    public SimpleStringProperty fila;
    public SimpleStringProperty numero;
    public SimpleStringProperty nombreSala;
    public ObjectProperty<LocalDate> fecha;
    public SimpleStringProperty horainicio;
    public SimpleStringProperty peliculanombreEspañol;
    public SimpleStringProperty peliculanombreIngles;
    public SimpleStringProperty precio;
    private boolean modificado;

    public TableViewFacturas() {
        this.modificado = false;
        this.fila = new SimpleStringProperty();
        this.numero = new SimpleStringProperty();
        this.nombreSala = new SimpleStringProperty();
        this.horainicio = new SimpleStringProperty();
        this.fecha = new SimpleObjectProperty();
        this.peliculanombreEspañol = new SimpleStringProperty();
        this.peliculanombreIngles = new SimpleStringProperty();
        this.precio = new SimpleStringProperty();
    }

    public String getFila() {
        return fila.get();
    }

    public void setFila(String fila) {
        this.fila.set(fila);
    }

    public Integer getNumero() {
        return Integer.valueOf(numero.get());
    }

    public void setNumero(Integer numero) {
        this.numero.set(numero.toString());
    }

    public String getNombreSala() {
        return nombreSala.get();
    }

    public void setNombreSala(String nombreSala) {
        this.nombreSala.set(nombreSala);
    }

    public LocalDate getFecha() {
        return fecha.get();
    }

    public void setFecha(LocalDate fecha) {
        this.fecha.set(fecha);
    }

    public String getHorainicio() {
        return horainicio.get();
    }

    public void setHorainicio(String horainicio) {
        this.horainicio.set(horainicio);
    }

    public String getPeliculanombreEspañol() {
        return peliculanombreEspañol.get();
    }

    public void setPeliculanombreEspañol(String peliculanombreEspañol) {
        this.peliculanombreEspañol.set(peliculanombreEspañol);
    }

    public String getPeliculanombreIngles() {
        return peliculanombreIngles.get();
    }

    public void setPeliculanombreIngles(String peliculanombreIngles) {
        this.peliculanombreIngles.set(peliculanombreIngles);
    }

    public Double getPrecio() {
        return Double.valueOf(precio.get());
    }

    public void setPrecio(Double precio) {
        this.precio.set(precio.toString());
    }

    public boolean isModificado() {
        return modificado;
    }

    public void setModificado(boolean modificado) {
        this.modificado = modificado;
    }

}
