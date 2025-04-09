/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.cineuna.service;

import cr.ac.una.cineuna.model.AlimentoDto;
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
public class AlimentoService {
    public Respuesta getAlimento(Long id) {
        try {
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            Request request = new Request("AlimentoController/alimentos", "/{id}", parametros);
            request.get();
            if(request.isError()){
               return new Respuesta(false, request.getError(), "");
           }
            AlimentoDto alimento = (AlimentoDto)request.readEntity(AlimentoDto.class);
            return new Respuesta(true, "", "", "Alimento", alimento);
        } catch (Exception ex) {
            Logger.getLogger(AlimentoService.class.getName()).log(Level.SEVERE, "Error obteniendo el alimento [" + id + "]", ex);
            return new Respuesta(false, "Error obteniendo el alimento.", "getAlimento " + ex.getMessage());
        }
    }
    public Respuesta getAlimentos() {
        try {
            Map<String, Object> parametros = new HashMap<>();

            Request request = new Request("AlimentoController/alimentos", "", parametros);
            request.get();
            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }
            List<AlimentoDto> Alimentos = (List<AlimentoDto>) request.readEntity(new GenericType<List<AlimentoDto>>() {
            });
            return new Respuesta(true, "", "", "Alimentos", Alimentos);
        } catch (Exception ex) {
            Logger.getLogger(AlimentoService.class.getName()).log(Level.SEVERE, "Error obteniendo Alimentos.", ex);
            return new Respuesta(false, "Error obteniendo Alimentos.", "getAlimentos " + ex.getMessage());
        }
    }
    
        public Respuesta guardarAlimento(AlimentoDto alimento) {
        try {
            //Request request = new Request("AlimentoController/alimentos");
            Request request = new Request("AlimentoController");
            request.post(alimento);
            if(request.isError()){
               return new Respuesta(false, request.getError(), "");
           }
            
            AlimentoDto alimentoDto = (AlimentoDto)request.readEntity(AlimentoDto.class);

            return new Respuesta(true, "", "", "Alimento", alimentoDto);
        } catch (Exception ex) {
            Logger.getLogger(AlimentoService.class.getName()).log(Level.SEVERE, "Error guardando el alimento.", ex);
            return new Respuesta(false, "Error guardando el alimento.", "guardarAlimento " + ex.getMessage());
        }
    }
    
    public Respuesta eliminarAlimento(Long id) {
        try {
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            Request request = new Request("AlimentoController/alimentos", "/{id}", parametros);
            request.delete();
            if(request.isError()){
               return new Respuesta(false, request.getError(), "");
           }
            return new Respuesta(true, "", "");
        } catch (Exception ex) {
            Logger.getLogger(AlimentoService.class.getName()).log(Level.SEVERE, "Error eliminando el alimento.", ex);
            return new Respuesta(false, "Error eliminando el alimento.", "eliminarAlimento " + ex.getMessage());
        }
    }
}
