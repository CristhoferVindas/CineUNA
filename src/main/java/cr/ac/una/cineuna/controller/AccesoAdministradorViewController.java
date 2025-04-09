package cr.ac.una.cineuna.controller;

import com.jfoenix.controls.JFXButton;
import cr.ac.una.cineuna.idioma.Idioma;
import cr.ac.una.cineuna.model.UsuarioDto;
import cr.ac.una.cineuna.util.AppContext;
import cr.ac.una.cineuna.util.FlowController;
import cr.ac.una.cineuna.util.Mensaje;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Cristhofer
 */
public class AccesoAdministradorViewController extends Controller implements Initializable {

    @FXML
    private BorderPane root;
    @FXML
    private JFXButton btnReportes;
    @FXML
    private Label lblTitulo;
    @FXML
    private JFXButton btnRegistrarPelicula;
    @FXML
    private JFXButton btnRegistrarSala;
    @FXML
    private JFXButton btnRegistrarTanda;
    @FXML
    private JFXButton btnRegistrarAlimento;
    @FXML
    private JFXButton BtnMantenimientoUsuarios;
    UsuarioDto usuarioDto;
    @FXML
    private JFXButton btnCerrarSesion;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        usuarioDto = new UsuarioDto();
        usuarioDto = (UsuarioDto) AppContext.getInstance().get("Usuario");
        if (usuarioDto.getIdioma().equals("I")) {
            try {
                cambiarIdioma("ingles.properties");
            } catch (IOException ex) {
                new Mensaje().showModal(Alert.AlertType.ERROR, "Idioma", getStage(), "Error al obtener idioma");
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
    private void OnActionBtnRegistrarPelicula(ActionEvent event) {
        FlowController.getInstance().goView("RegistroPeliculaView");
        
    }

    @FXML
    private void OnActionBtnRegistrarSala(ActionEvent event) {
        FlowController.getInstance().goView("RegistroSalaView");
    }

    @FXML
    private void OnActionBtnRegistrarTanda(ActionEvent event) {
        FlowController.getInstance().goView("RegistroTandaView");
    }

    @FXML
    private void OnActionBtnRegistrarAlimento(ActionEvent event) {
        FlowController.getInstance().goView("RegistroAlimentoView");
    }

    @FXML
    private void OnActionBtnCerrarSesion(ActionEvent event) {
        FlowController.getInstance().initialize();
        FlowController.getInstance().goViewInWindow("LoginView");
        ((Stage) btnCerrarSesion.getScene().getWindow()).close();
    }

    @FXML
    private void OnActionBtnReportes(ActionEvent event) {
        FlowController.getInstance().goView("ReporteView");
        
    }

    @FXML
    private void OnActionBtnMantenimientoUsuario(ActionEvent event) {
         FlowController.getInstance().goView("MantenimientoUsuarioView");
    }
    
        private void cambiarIdioma(String pIdioma) throws IOException {
        Idioma idiom = new Idioma(pIdioma);
        Properties prop = idiom.getProperties(pIdioma);
        lblTitulo.setText(prop.getProperty("txtTituloAccesoAdministrador"));
        btnRegistrarPelicula.setText(prop.getProperty("btnRegistrarPelicula"));
        btnRegistrarSala.setText(prop.getProperty("btnRegistrarSala"));
        btnRegistrarTanda.setText(prop.getProperty("btnRegistrarTanda"));
        btnRegistrarAlimento.setText(prop.getProperty("btnRegistrarAlimento"));
        BtnMantenimientoUsuarios.setText(prop.getProperty("btnMantenimientoUsuario"));
        btnReportes.setText(prop.getProperty("btnReportesAdmin"));
        btnCerrarSesion.setText(prop.getProperty("btnCerrarSesionAdmin"));

    }
    
}
