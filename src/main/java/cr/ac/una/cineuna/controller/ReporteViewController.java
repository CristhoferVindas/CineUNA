package cr.ac.una.cineuna.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import cr.ac.una.cineuna.idioma.Idioma;
import cr.ac.una.cineuna.model.PeliculaDto;
import cr.ac.una.cineuna.model.UsuarioDto;
import cr.ac.una.cineuna.service.PeliculaService;
import cr.ac.una.cineuna.util.AppContext;
import cr.ac.una.cineuna.util.FlowController;
import cr.ac.una.cineuna.util.Mensaje;
import cr.ac.una.cineuna.util.Respuesta;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Cristhofer
 */
public class ReporteViewController extends Controller implements Initializable {

    @FXML
    private JFXDatePicker dtpFechaInicio;
    @FXML
    private JFXDatePicker dtpFechaFin;
    @FXML
    private JFXButton btnReportePorFecha;
    @FXML
    private JFXButton btnReportePorPeicula;
    @FXML
    private Label lblFechaInicio;
    @FXML
    private Label lblFechaFin;
    UsuarioDto usuarioDto;
    PeliculaDto peliculaDto;
    @FXML
    private JFXTextField txtNombrePeliculaEspanol;
    @FXML
    private JFXTextField txtNombrePeliculaIngles;
    @FXML
    private JFXButton btnBuscarPelicula;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        peliculaDto = new PeliculaDto();
        usuarioDto = new UsuarioDto();
        usuarioDto = (UsuarioDto) AppContext.getInstance().get("Usuario");
        if (usuarioDto.getIdioma().equals("I")) {
            try {
                cambiarIdioma("ingles.properties");
            } catch (IOException ex) {
                new Mensaje().showModal(Alert.AlertType.ERROR, "Idioma", getStage(), "Error al obtener idioma");//Poner en inglés
            }
        } else if (usuarioDto.getIdioma().equals("E")) {
            try {
                cambiarIdioma("espannol.properties");
            } catch (IOException ex) {
                new Mensaje().showModal(Alert.AlertType.ERROR, "Idioma", getStage(), "Error al obtener idioma");
            }
        }

    }

    @Override
    public void initialize() {
    }

    @FXML
    private void OnActionBtnReportePorFecha(ActionEvent event) throws FileNotFoundException, IOException {
        if ((dtpFechaInicio.getValue() == null || dtpFechaFin.getValue() == null) &&(usuarioDto.getIdioma().equals("E"))) {
            new Mensaje().showModal(Alert.AlertType.WARNING,"", stage, "Por favor digite ambas fechas para poder generar el reporte");
        }else if ((dtpFechaInicio.getValue() == null || dtpFechaFin.getValue() == null) &&(usuarioDto.getIdioma().equals("I"))) {
            new Mensaje().showModal(Alert.AlertType.WARNING,"", stage, "Enter both dates to generate the report");
        } 
        else {
            if (dtpFechaInicio.getValue().isAfter(dtpFechaFin.getValue()) &&(usuarioDto.getIdioma().equals("E"))) {
                new Mensaje().showModal(Alert.AlertType.WARNING,"", stage, "La fecha de Inicio no puede ser mayor a la Fecha de Fin");
            } else if (dtpFechaInicio.getValue().isAfter(dtpFechaFin.getValue()) &&(usuarioDto.getIdioma().equals("I"))) {
                new Mensaje().showModal(Alert.AlertType.WARNING,"", stage, "The Start date cannot be greater than the end Date");
            }else {
                File portadasDir = new File("..\\CineUNA\\Reportes"); //"C:\\Users\\Cristhofer\\Desktop\\UNA\\CineUNA(Cliente-Servidor)\\WsCineUNA\\Imagenes\\+"
            if (!portadasDir.exists()){
                portadasDir.mkdir();
                portadasDir.setWritable(true);
            }
                PeliculaService service = new PeliculaService();
                Respuesta respuesta = service.getReporteBtwFecha(dtpFechaInicio.getValue().toString(), dtpFechaFin.getValue().toString(), "C");
                if (respuesta.getEstado()) {
                    String pdf = (String) respuesta.getResultado("Peliculas");
                    byte[] pdfDecode = Base64.getDecoder().decode(pdf);
                    File pdfFile = new File("..\\CineUNA\\Reportes\\ReporteEntrefechas.pdf");
                    FileOutputStream pdfOutputStream = new FileOutputStream(pdfFile);
                    pdfOutputStream.write(pdfDecode);
                    pdfOutputStream.close();
                    String path = System.getProperty("user.dir");
                    path += "\\Reportes\\ReporteEntrefechas.pdf";
                    Process p = Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL " + path);
                } else {
                    new Mensaje().showModal(Alert.AlertType.ERROR, "", stage, respuesta.getMensaje());
                }
            }
        }
    }

    @FXML
    private void OnActionBtnReportePorPelicula(ActionEvent event) throws IOException {
 if ((txtNombrePeliculaEspanol.getText() == null || txtNombrePeliculaEspanol.getText().isEmpty()) && (txtNombrePeliculaIngles.getText() == null || txtNombrePeliculaIngles.getText().isEmpty())) {
           if(usuarioDto.getIdioma().equals("E")){
               new Mensaje().showModal(Alert.AlertType.INFORMATION, "", stage, "Por favor seleccione una pelicula");
           }else{
               new Mensaje().showModal(Alert.AlertType.INFORMATION, "", stage, "Please select a movie");
           }
 }else{
     PeliculaService service = new PeliculaService();
        Respuesta respuesta = service.getReporteByPelicula(txtNombrePeliculaEspanol.getText(), txtNombrePeliculaIngles.getText());
        
        if (respuesta.getEstado()) {
             String pdf = (String) respuesta.getResultado("Peliculas");
                byte[] pdfDecode = Base64.getDecoder().decode(pdf);
                File pdfFile = new File("..\\CineUNA\\Reportes\\ReportePorpelicula.pdf");
                FileOutputStream pdfOutputStream = new FileOutputStream(pdfFile);
                pdfOutputStream.write(pdfDecode);
                pdfOutputStream.close();
                String path = System.getProperty("user.dir");
                path += "\\Reportes\\ReportePorpelicula.pdf";
                Process p = Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL " + path);
        } else {
            new Mensaje().showModal(Alert.AlertType.ERROR, "", stage, respuesta.getMensaje());
        }
 }
        
    }
    
    private void cambiarIdioma(String pIdioma) throws IOException {
        Idioma idiom = new Idioma(pIdioma);
        Properties prop = idiom.getProperties(pIdioma);

        lblFechaInicio.setText(prop.getProperty("lblFechaInicio"));
        lblFechaFin.setText(prop.getProperty("lblFechaFin"));
        btnReportePorFecha.setText(prop.getProperty("btnGenerarReporte"));
        txtNombrePeliculaIngles.setText(prop.getProperty("txtNombrePeliculaIngles"));
        btnBuscarPelicula.setText(prop.getProperty("btnBuscarPelicula"));
        txtNombrePeliculaEspanol.setText(prop.getProperty("txtNombrePeliculaEspanol"));
        btnReportePorPeicula.setText(prop.getProperty("btnGenerarReporte"));

    }

    @FXML
    private void OnActionBtnReporteBuscarPelicula(ActionEvent event) {
        BusquedaViewController busquedaViewController = (BusquedaViewController) FlowController.getInstance().getController("BusquedaView");
        busquedaViewController.busquedaPeliculasReporte();
        FlowController.getInstance().goViewInWindowModal("BusquedaView", getStage(), true);
        PeliculaDto pelicculaDto = (PeliculaDto) busquedaViewController.getResultado();
        if (pelicculaDto != null) {
            this.peliculaDto = pelicculaDto;
            txtNombrePeliculaEspanol.setText(pelicculaDto.getNombreEspannol());
            txtNombrePeliculaIngles.setText(pelicculaDto.getNombreIngles());
        }
    }

}
