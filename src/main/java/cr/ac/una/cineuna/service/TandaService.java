/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.cineuna.service;

import cr.ac.una.cineuna.model.TandaDto;
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
public class TandaService {
        public Respuesta getTanda(Long id) {
        try {
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            Request request = new Request("TandaController/tandas", "/{id}", parametros);
            request.get();
            if(request.isError()){
               return new Respuesta(false, request.getError(), "");
           }
            TandaDto tanda = (TandaDto)request.readEntity(TandaDto.class);
            return new Respuesta(true, "", "", "Tanda", tanda);
        } catch (Exception ex) {
            Logger.getLogger(TandaService.class.getName()).log(Level.SEVERE, "Error obteniendo el tanda [" + id + "]", ex);
            return new Respuesta(false, "Error obteniendo el tanda.", "getTanda " + ex.getMessage());
        }
    }
        
        public Respuesta guardarTanda(TandaDto tanda) {
        try {
            //Request request = new Request("TandaController/tandas");
            Request request = new Request("TandaController");
            request.post(tanda);
            if(request.isError()){
               return new Respuesta(false, request.getError(), "");
           }
            
            TandaDto tandaDto = (TandaDto)request.readEntity(TandaDto.class);

            return new Respuesta(true, "", "", "Tanda", tandaDto);
        } catch (Exception ex) {
            Logger.getLogger(TandaService.class.getName()).log(Level.SEVERE, "Error guardando el tanda.", ex);
            return new Respuesta(false, "Error guardando el tanda.", "guardarTanda " + ex.getMessage());
        }
    }
    
    public Respuesta eliminarTanda(Long id) {
        try {
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            Request request = new Request("TandaController/tandas", "/{id}", parametros);
            request.delete();
            if(request.isError()){
               return new Respuesta(false, request.getError(), "");
           }
            return new Respuesta(true, "", "");
        } catch (Exception ex) {
            Logger.getLogger(TandaService.class.getName()).log(Level.SEVERE, "Error eliminando el tanda.", ex);
            return new Respuesta(false, "Error eliminando el tanda.", "eliminarTanda " + ex.getMessage());
        }
    }
    
    public Respuesta getTandas(Long pelicula) {
        try {
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("pelicula", pelicula);
            Request request = new Request("TandaController/tandasP", "/{pelicula}", parametros);
            request.get();
            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }
            List<TandaDto> tandas = (List<TandaDto>) request.readEntity(new GenericType<List<TandaDto>>() {
            });
            return new Respuesta(true, "", "", "Tandas", tandas);
        } catch (Exception ex) {
            Logger.getLogger(PeliculaService.class.getName()).log(Level.SEVERE, "Error obteniendo tandas.", ex);
            return new Respuesta(false, "Error obteniendo tandas.", "getTandas " + ex.getMessage());
        }
    }
}
