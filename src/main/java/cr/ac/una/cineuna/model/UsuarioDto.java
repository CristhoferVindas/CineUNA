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
public class UsuarioDto {
    
    
    public SimpleStringProperty id;
    public SimpleStringProperty nombre;
    public SimpleStringProperty pApellido;
    public SimpleStringProperty sApellido;
    public SimpleStringProperty usuario;
    public SimpleStringProperty contrasenna;
    public SimpleStringProperty correo;
    public ObjectProperty<String> idioma;
    public ObjectProperty<String> tipo;
    public ObjectProperty<String> estado;
    public ObjectProperty<String> restablecido;
    
    private String token;
    private Boolean modificado;

    public UsuarioDto() {
        this.modificado = false;
        
        this.id = new SimpleStringProperty();
        this.nombre = new SimpleStringProperty();
        this.pApellido = new SimpleStringProperty();
        this.sApellido = new SimpleStringProperty();
        this.usuario = new SimpleStringProperty();
        this.contrasenna = new SimpleStringProperty();
        this.correo = new SimpleStringProperty();
        this.idioma = new SimpleObjectProperty("E");
        this.tipo = new SimpleObjectProperty("C");
        this.estado = new SimpleObjectProperty("A");
        this.restablecido = new SimpleObjectProperty("N");
        
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

    public String getpApellido() {
        return pApellido.get();
    }

    public void setpApellido(String pApellido) {
        this.pApellido.set(pApellido);
    }

    public String getsApellido() {
        return sApellido.get();
    }

    public void setsApellido(String sApellido) {
        this.sApellido.set(sApellido);
    }

    public String getUsuario() {
        return usuario.get();
    }

    public void setUsuario(String usuario) {
        this.usuario.set(usuario);
    }

    public String getContrasenna() {
        return contrasenna.get();
    }

    public void setContrasenna(String contrasenna) {
        this.contrasenna.set(contrasenna);
    }

    public String getCorreo() {
        return correo.get();
    }

    public void setCorreo(String correo) {
        this.correo.set(correo);
    }

    public String getIdioma() {
        return idioma.get();
    }

    public void setIdioma(String idioma) {
        this.idioma.set(idioma);
    }

    public String getTipo() {
        return tipo.get();
    }

    public void setTipo(String tipo) {
        this.tipo.set(tipo);
    }

    public String getEstado() {
        return estado.get();
    }

    public void setEstado(String estado) {
        this.estado.set(estado);
    }

    public String getRestablecido() {
        return restablecido.get();
    }

    public void setRestablecido(String restablecido) {
        this.restablecido.set(restablecido);
    }

    public Boolean isModificado() {
        return modificado;
    }

    public void setModificado(Boolean modificado) {
        this.modificado = modificado;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
    
    
    
}
