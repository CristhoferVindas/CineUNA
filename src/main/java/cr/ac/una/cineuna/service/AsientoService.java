/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.cineuna.service;

import cr.ac.una.cineuna.model.AsientoDto;
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
public class AsientoService {

    public Respuesta getAsiento(Long id) {
        try {
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            Request request = new Request("AsientoController/asientos", "/{id}", parametros);
            request.get();
            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }
            AsientoDto asiento = (AsientoDto) request.readEntity(AsientoDto.class);
            return new Respuesta(true, "", "", "Asiento", asiento);
        } catch (Exception ex) {
            Logger.getLogger(AsientoService.class.getName()).log(Level.SEVERE, "Error obteniendo el asiento [" + id + "]", ex);
            return new Respuesta(false, "Error obteniendo el asiento.", "getAsiento " + ex.getMessage());
        }
    }

    public Respuesta getAsientos() {
        try {
            Map<String, Object> parametros = new HashMap<>();

            Request request = new Request("AsientoController/asientos", "", parametros);
            request.get();
            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }
            List<AsientoDto> asientos = (List<AsientoDto>) request.readEntity(new GenericType<List<AsientoDto>>() {
            });
            return new Respuesta(true, "", "", "Asiento", asientos);
        } catch (Exception ex) {
            Logger.getLogger(AsientoService.class.getName()).log(Level.SEVERE, "Error obteniendo el asiento ", ex);
            return new Respuesta(false, "Error obteniendo el asiento.", "getAsiento " + ex.getMessage());
        }
    }

    public Respuesta guardarAsiento(AsientoDto asiento) {
        try {
            //Request request = new Request("AsientoController/asientos");
            Request request = new Request("AsientoController");
            request.post(asiento);
            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            AsientoDto asientoDto = (AsientoDto) request.readEntity(AsientoDto.class);

            return new Respuesta(true, "", "", "Asiento", asientoDto);
        } catch (Exception ex) {
            Logger.getLogger(AsientoService.class.getName()).log(Level.SEVERE, "Error guardando el asiento.", ex);
            return new Respuesta(false, "Error guardando el asiento.", "guardarAsiento " + ex.getMessage());
        }
    }

    public Respuesta eliminarAsiento(Long id) {
        try {
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            Request request = new Request("AsientoController/asientos", "/{id}", parametros);
            request.delete();
            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }
            return new Respuesta(true, "", "");
        } catch (Exception ex) {
            Logger.getLogger(AsientoService.class.getName()).log(Level.SEVERE, "Error eliminando el asiento.", ex);
            return new Respuesta(false, "Error eliminando el asiento.", "eliminarAsiento " + ex.getMessage());
        }
    }

}
