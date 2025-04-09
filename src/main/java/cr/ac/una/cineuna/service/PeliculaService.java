/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.cineuna.service;

import cr.ac.una.cineuna.model.PeliculaDto;
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
public class PeliculaService {

    public Respuesta getPelicula(Long id) {
        try {
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            Request request = new Request("PeliculaController/peliculas", "/{id}", parametros);
            request.get();
            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }
            PeliculaDto pelicula = (PeliculaDto) request.readEntity(PeliculaDto.class);
            return new Respuesta(true, "", "", "Pelicula", pelicula);
        } catch (Exception ex) {
            Logger.getLogger(PeliculaService.class.getName()).log(Level.SEVERE, "Error obteniendo el pelicula [" + id + "]", ex);
            return new Respuesta(false, "Error obteniendo el pelicula.", "getPelicula " + ex.getMessage());
        }
    }

    public Respuesta getAllPeliculas() {
        try {
            Map<String, Object> parametros = new HashMap<>();
            Request request = new Request("PeliculaController/peliculas", "", parametros); 
            request.get();
            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }
            List<PeliculaDto> peliculas = (List<PeliculaDto>) request.readEntity(new GenericType<List<PeliculaDto>>() {
            });
            return new Respuesta(true, "", "", "Peliculas", peliculas);
        } catch (Exception ex) {
            Logger.getLogger(PeliculaService.class.getName()).log(Level.SEVERE, "Error obteniendo peliculas.", ex);
            return new Respuesta(false, "Error obteniendo peliculas.", "getPeliculas " + ex.getMessage());
        }
    }
    
    public Respuesta getPeliculas(String nombreEspannol, String nombreIngles) {//, String fechaEstreno
        try {
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("nombreEspannol", nombreEspannol);
            parametros.put("nombreIngles", nombreIngles);
            Request request = new Request("PeliculaController/peliculas", "/{nombreEspannol}/{nombreIngles}", parametros); //{fechaEstreno}
            request.get();
            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }
            List<PeliculaDto> peliculas = (List<PeliculaDto>) request.readEntity(new GenericType<List<PeliculaDto>>() {
            });
            return new Respuesta(true, "", "", "Peliculas", peliculas);
        } catch (Exception ex) {
            Logger.getLogger(PeliculaService.class.getName()).log(Level.SEVERE, "Error obteniendo peliculas.", ex);
            return new Respuesta(false, "Error obteniendo peliculas.", "getPeliculas " + ex.getMessage());
        }
    }
    
    public Respuesta getReporteBtwFecha(String fechainicio, String fechafin, String estado) {//, String fechaEstreno
        try {
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("fechainicio", fechainicio);
            parametros.put("fechafin", fechafin);
            parametros.put("estado", estado);
            Request request = new Request("PeliculaController/rpeliculas", "/{fechainicio}/{fechafin}/{estado}", parametros); //{fechaEstreno}
            request.get();
            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }
            String reporte = (String) request.readEntity(String.class);
            return new Respuesta(true, "", "", "Peliculas", reporte);
        } catch (Exception ex) {
            Logger.getLogger(PeliculaService.class.getName()).log(Level.SEVERE, "Error obteniendo peliculas.", ex);
            return new Respuesta(false, "Error obteniendo peliculas.", "getPeliculas " + ex.getMessage());
        }
    }
    
    public Respuesta getReporteByPelicula(String peliculaEspanol, String peliculaIngles) {
        try {
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("peliculaEspanol", peliculaEspanol);
            parametros.put("peliculaIngles", peliculaIngles);
            Request request = new Request("PeliculaController/rpeliculas", "/{peliculaEspanol}/{peliculaIngles}", parametros);
            request.get();
            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }
            String reporte = (String) request.readEntity(String.class);
            return new Respuesta(true, "", "", "Peliculas", reporte);
        } catch (Exception ex) {
            Logger.getLogger(PeliculaService.class.getName()).log(Level.SEVERE, "Error obteniendo peliculas.", ex);
            return new Respuesta(false, "Error obteniendo peliculas.", "getPeliculas " + ex.getMessage());
        }
    }

    public Respuesta getPeliculasPorEstrenarse(String estado) {
        try {
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("estado", estado);
            Request request = new Request("PeliculaController/peliculasE", "/{estado}", parametros);
            request.get();
            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }
            List<PeliculaDto> peliculas = (List<PeliculaDto>) request.readEntity(new GenericType<List<PeliculaDto>>() {
            });
            return new Respuesta(true, "", "", "Peliculas", peliculas);
        } catch (Exception ex) {
            Logger.getLogger(PeliculaService.class.getName()).log(Level.SEVERE, "Error obteniendo peliculas.", ex);
            return new Respuesta(false, "Error obteniendo peliculas.", "getPeliculas " + ex.getMessage());
        }
    }

    public Respuesta guardarPelicula(PeliculaDto pelicula) {
        try {
            Request request = new Request("PeliculaController");
            request.post(pelicula);
            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            PeliculaDto peliculaDto = (PeliculaDto) request.readEntity(PeliculaDto.class);

            return new Respuesta(true, "", "", "Pelicula", peliculaDto);
        } catch (Exception ex) {
            Logger.getLogger(PeliculaService.class.getName()).log(Level.SEVERE, "Error guardando el pelicula.", ex);
            return new Respuesta(false, "Error guardando el pelicula.", "guardarPelicula " + ex.getMessage());
        }
    }

    public Respuesta eliminarPelicula(Long id) {
        try {
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            Request request = new Request("PeliculaController/peliculas", "/{id}", parametros);
            request.delete();
            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }
            return new Respuesta(true, "", "");
        } catch (Exception ex) {
            Logger.getLogger(PeliculaService.class.getName()).log(Level.SEVERE, "Error eliminando el pelicula.", ex);
            return new Respuesta(false, "Error eliminando el pelicula.", "eliminarPelicula " + ex.getMessage());
        }
    }

}
