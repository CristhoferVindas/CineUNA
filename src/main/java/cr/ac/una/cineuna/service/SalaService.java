/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.cineuna.service;

import cr.ac.una.cineuna.model.SalaDto;
import cr.ac.una.cineuna.util.Request;
import cr.ac.una.cineuna.util.Respuesta;
import jakarta.ws.rs.core.GenericType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Cristhofer
 */
public class SalaService {

    public Respuesta getSala(Long id) {
        try {
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            Request request = new Request("SalaController/salas", "/{id}", parametros);
            request.get();
            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }
            SalaDto sala = (SalaDto) request.readEntity(SalaDto.class);
            return new Respuesta(true, "", "", "Sala", sala);
        } catch (Exception ex) {
            Logger.getLogger(SalaService.class.getName()).log(Level.SEVERE, "Error obteniendo el sala [" + id + "]", ex);
            return new Respuesta(false, "Error obteniendo el sala.", "getSala " + ex.getMessage());
        }
    }
public Respuesta getSalas(String nombre, String detalle) {
        try {
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("nombre", nombre);
            parametros.put("detalle", detalle);

            Request request = new Request("SalaController/salas", "/{nombre}/{detalle}", parametros);
            request.get();
            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }
            List<SalaDto> salas = (List<SalaDto>) request.readEntity(new GenericType<List<SalaDto>>() {
            });
            return new Respuesta(true, "", "", "Salas", salas);
        } catch (Exception ex) {
            Logger.getLogger(SalaService.class.getName()).log(Level.SEVERE, "Error obteniendo salas.", ex);
            return new Respuesta(false, "Error obteniendo salas.", "getSalas " + ex.getMessage());
        }
    }
    

    public Respuesta guardarSala(SalaDto sala) {
        try {
            //Request request = new Request("SalaController/salas");
            Request request = new Request("SalaController");
            request.post(sala);
            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            SalaDto salaDto = (SalaDto) request.readEntity(SalaDto.class);

            return new Respuesta(true, "", "", "Sala", salaDto);
        } catch (Exception ex) {
            Logger.getLogger(SalaService.class.getName()).log(Level.SEVERE, "Error guardando el sala.", ex);
            return new Respuesta(false, "Error guardando el sala.", "guardarSala " + ex.getMessage());
        }
    }

    public Respuesta eliminarSala(Long id) {
        try {
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            Request request = new Request("SalaController/salas", "/{id}", parametros);
            request.delete();
            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }
            return new Respuesta(true, "", "");
        } catch (Exception ex) {
            Logger.getLogger(SalaService.class.getName()).log(Level.SEVERE, "Error eliminando el sala.", ex);
            return new Respuesta(false, "Error eliminando el sala.", "eliminarSala " + ex.getMessage());
        }
    }

}
