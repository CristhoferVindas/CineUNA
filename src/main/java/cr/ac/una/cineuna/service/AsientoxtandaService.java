/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.cineuna.service;

import cr.ac.una.cineuna.model.AsientoxtandaDto;
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
public class AsientoxtandaService {
    
        public Respuesta getAsientoxtanda(Long id) {
        try {
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            Request request = new Request("AsientoxtandaController/asientoxtandas", "/{id}", parametros);
            request.get();
            if(request.isError()){
               return new Respuesta(false, request.getError(), "");
           }
            AsientoxtandaDto asientoxtanda = (AsientoxtandaDto)request.readEntity(AsientoxtandaDto.class);
            return new Respuesta(true, "", "", "Asientoxtanda", asientoxtanda);
        } catch (Exception ex) {
            Logger.getLogger(AsientoxtandaService.class.getName()).log(Level.SEVERE, "Error obteniendo el asientoxtanda [" + id + "]", ex);
            return new Respuesta(false, "Error obteniendo el asientoxtanda.", "getAsientoxtanda " + ex.getMessage());
        }
    }
    
        public Respuesta getAsientosxtandas(Long idTanda) {
        try {
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("idTanda", idTanda);

            Request request = new Request("AsientoxtandaController/asientosxtandas", "/{idTanda}", parametros);
            request.get();
            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }
            List<AsientoxtandaDto> asientoxtandas = (List<AsientoxtandaDto>) request.readEntity(new GenericType<List<AsientoxtandaDto>>() {
            });
            return new Respuesta(true, "", "", "Asientoxtandas", asientoxtandas);
        } catch (Exception ex) {
            Logger.getLogger(AsientoxtandaService.class.getName()).log(Level.SEVERE, "Error obteniendo asientoxtandas.", ex);
            return new Respuesta(false, "Error obteniendo asientoxtandas.", "getAsientoxtandas " + ex.getMessage());
        }
    }
        public Respuesta getAsientosxtandas() {
        try {
            Map<String, Object> parametros = new HashMap<>();

            Request request = new Request("AsientoxtandaController/asientosxtandas", "", parametros);
            request.get();
            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }
            List<AsientoxtandaDto> asientoxtandas = (List<AsientoxtandaDto>) request.readEntity(new GenericType<List<AsientoxtandaDto>>() {
            });
            return new Respuesta(true, "", "", "Asientoxtandas", asientoxtandas);
        } catch (Exception ex) {
            Logger.getLogger(AsientoxtandaService.class.getName()).log(Level.SEVERE, "Error obteniendo asientoxtandas.", ex);
            return new Respuesta(false, "Error obteniendo asientoxtandas.", "getAsientoxtandas " + ex.getMessage());
        }
    }
    
        public Respuesta guardarAsientoxtanda(AsientoxtandaDto asientoxtanda) {
        try {
            //Request request = new Request("AsientoxtandaController/asientoxtandas");
            Request request = new Request("AsientoxtandaController");
            request.post(asientoxtanda);
            if(request.isError()){
               return new Respuesta(false, request.getError(), "");
           }
            
            AsientoxtandaDto asientoxtandaDto = (AsientoxtandaDto)request.readEntity(AsientoxtandaDto.class);

            return new Respuesta(true, "", "", "Asientoxtandas", asientoxtandaDto);
        } catch (Exception ex) {
            Logger.getLogger(AsientoxtandaService.class.getName()).log(Level.SEVERE, "Error guardando el asientoxtanda.", ex);
            return new Respuesta(false, "Error guardando el asientoxtanda.", "guardarAsientoxtanda " + ex.getMessage());
        }
    }
    
    public Respuesta eliminarAsientoxtanda(Long id) {
        try {
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            Request request = new Request("AsientoxtandaController/asientoxtandas", "/{id}", parametros);
            request.delete();
            if(request.isError()){
               return new Respuesta(false, request.getError(), "");
           }
            return new Respuesta(true, "", "");
        } catch (Exception ex) {
            Logger.getLogger(AsientoxtandaService.class.getName()).log(Level.SEVERE, "Error eliminando el asientoxtanda.", ex);
            return new Respuesta(false, "Error eliminando el asientoxtanda.", "eliminarAsientoxtanda " + ex.getMessage());
        }
    }
    
    
}
