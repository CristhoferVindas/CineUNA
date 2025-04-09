/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.cineuna.service;

import cr.ac.una.cineuna.model.FacturaDto;
import cr.ac.una.cineuna.util.Request;
import cr.ac.una.cineuna.util.Respuesta;
import cr.ac.una.cineuna.util.TableViewFacturas;
import jakarta.ws.rs.core.GenericType;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Cristhofer
 */
public class FacturaService {

    public Respuesta getFactura(Long id) {
        try {
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            Request request = new Request("FacturaController/facturas", "/{id}", parametros);
            request.get();
            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }
            FacturaDto factura = (FacturaDto) request.readEntity(FacturaDto.class);
            return new Respuesta(true, "", "", "Factura", factura);
        } catch (Exception ex) {
            Logger.getLogger(FacturaService.class.getName()).log(Level.SEVERE, "Error obteniendo el factura [" + id + "]", ex);
            return new Respuesta(false, "Error obteniendo el factura.", "getFactura " + ex.getMessage());
        }
    }

    public Respuesta getdDetalleFactura(Long tanId) {
        try {
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("tanId", tanId);
            Request request = new Request("FacturaController/detallefacturas", "/{tanId}", parametros); //{fechaEstreno}
            request.get();
            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            List<TableViewFacturas> facturas;
            facturas = (List<TableViewFacturas>) request.readEntity(new GenericType<List<TableViewFacturas>>() {
            });
            return new Respuesta(true, "", "", "Facturas", facturas);
        } catch (Exception ex) {
            Logger.getLogger(FacturaService.class.getName()).log(Level.SEVERE, "Error obteniendo facturas.", ex);
            return new Respuesta(false, "Error obteniendo facturas.", "getFacturas " + ex.getMessage());
        }
    }

    public Respuesta getFechaFactura() {
        try {
            Map<String, Object> parametros = new HashMap<>();
            Request request = new Request("FacturaController/fechafacturas", "", parametros);
            request.get();
            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }
            LocalDateTime dateTime = (LocalDateTime) request.readEntity(LocalDateTime.class);
            return new Respuesta(true, "", "", "Factura", dateTime);
        } catch (Exception ex) {
            Logger.getLogger(FacturaService.class.getName()).log(Level.SEVERE, "Error obteniendo el factura [" + "]", ex);
            return new Respuesta(false, "Error obteniendo el factura.", "getFactura " + ex.getMessage());
        }
    }

    public Respuesta getComprobante(Long idFactura) {//, String fechaEstreno
        try {
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("idFactura", idFactura);
            Request request = new Request("FacturaController/comprobantefactura", "/{idFactura}", parametros); //{fechaEstreno}
            request.get();
            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }
            String reporte = (String) request.readEntity(String.class);
            return new Respuesta(true, "", "", "Facturas", reporte);
        } catch (Exception ex) {
            Logger.getLogger(FacturaService.class.getName()).log(Level.SEVERE, "Error obteniendo Facturas.", ex);
            return new Respuesta(false, "Error obteniendo Facturas.", "getFacturas " + ex.getMessage());
        }
    }
    public Respuesta getComprobanteAlimento(Long idFactura) {//, String fechaEstreno
        try {
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("idFactura", idFactura);
            Request request = new Request("FacturaController/comprobantefacturaalimento", "/{idFactura}", parametros); //{fechaEstreno}
            request.get();
            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }
            String reporte = (String) request.readEntity(String.class);
            return new Respuesta(true, "", "", "Facturas", reporte);
        } catch (Exception ex) {
            Logger.getLogger(FacturaService.class.getName()).log(Level.SEVERE, "Error obteniendo Facturas.", ex);
            return new Respuesta(false, "Error obteniendo Facturas.", "getFacturas " + ex.getMessage());
        }
    }
    
    public Respuesta guardarFactura(FacturaDto factura) {
        try {
            //Request request = new Request("FacturaController/facturas");
            Request request = new Request("FacturaController");
            request.post(factura);
            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            FacturaDto facturaDto = (FacturaDto) request.readEntity(FacturaDto.class);

            return new Respuesta(true, "", "", "Factura", facturaDto);
        } catch (Exception ex) {
            Logger.getLogger(FacturaService.class.getName()).log(Level.SEVERE, "Error guardando el factura.", ex);
            return new Respuesta(false, "Error guardando el factura.", "guardarFactura " + ex.getMessage());
        }
    }

    public Respuesta eliminarFactura(Long id) {
        try {
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            Request request = new Request("FacturaController/facturas", "/{id}", parametros);
            request.delete();
            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }
            return new Respuesta(true, "", "");
        } catch (Exception ex) {
            Logger.getLogger(FacturaService.class.getName()).log(Level.SEVERE, "Error eliminando el factura.", ex);
            return new Respuesta(false, "Error eliminando el factura.", "eliminarFactura " + ex.getMessage());
        }
    }

}
