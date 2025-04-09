/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.cineuna.service;

import cr.ac.una.cineuna.model.AlimentoxfacturaDto;
import cr.ac.una.cineuna.util.Request;
import cr.ac.una.cineuna.util.Respuesta;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Cristhofer
 */
public class AlimentoxfacturaService {
    
        public Respuesta getAlimentoxfactura(Long id) {
        try {
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            Request request = new Request("AlimentoxfacturaController/alimentoxfacturas", "/{id}", parametros);
            request.get();
            if(request.isError()){
               return new Respuesta(false, request.getError(), "");
           }
            AlimentoxfacturaDto alimentoxfactura = (AlimentoxfacturaDto)request.readEntity(AlimentoxfacturaDto.class);
            return new Respuesta(true, "", "", "Alimentoxfactura", alimentoxfactura);
        } catch (Exception ex) {
            Logger.getLogger(AlimentoxfacturaService.class.getName()).log(Level.SEVERE, "Error obteniendo el alimentoxfactura [" + id + "]", ex);
            return new Respuesta(false, "Error obteniendo el alimentoxfactura.", "getAlimentoxfactura " + ex.getMessage());
        }
    }
    
    
        public Respuesta guardarAlimentoxfactura(AlimentoxfacturaDto alimentoxfactura) {
        try {
            //Request request = new Request("AlimentoxfacturaController/alimentoxfacturas");
            Request request = new Request("AlimentoxfacturaController");
            request.post(alimentoxfactura);
            if(request.isError()){
               return new Respuesta(false, request.getError(), "");
           }
            
            AlimentoxfacturaDto alimentoxfacturaDto = (AlimentoxfacturaDto)request.readEntity(AlimentoxfacturaDto.class);

            return new Respuesta(true, "", "", "Alimentoxfactura", alimentoxfacturaDto);
        } catch (Exception ex) {
            Logger.getLogger(AlimentoxfacturaService.class.getName()).log(Level.SEVERE, "Error guardando el alimentoxfactura.", ex);
            return new Respuesta(false, "Error guardando el alimentoxfactura.", "guardarAlimentoxfactura " + ex.getMessage());
        }
    }
    
    public Respuesta eliminarAlimentoxfactura(Long id) {
        try {
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            Request request = new Request("AlimentoxfacturaController/alimentoxfacturas", "/{id}", parametros);
            request.delete();
            if(request.isError()){
               return new Respuesta(false, request.getError(), "");
           }
            return new Respuesta(true, "", "");
        } catch (Exception ex) {
            Logger.getLogger(AlimentoxfacturaService.class.getName()).log(Level.SEVERE, "Error eliminando el alimentoxfactura.", ex);
            return new Respuesta(false, "Error eliminando el alimentoxfactura.", "eliminarAlimentoxfactura " + ex.getMessage());
        }
    }
    
}
