package cr.ac.una.cineuna.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import cr.ac.una.cineuna.idioma.Idioma;
import cr.ac.una.cineuna.model.UsuarioDto;
import cr.ac.una.cineuna.util.AppContext;
import cr.ac.una.cineuna.util.Mensaje;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * FXML Controller class
 *
 * @author Cristhofer
 */
public class RegistroYouTubeViewController extends Controller implements Initializable {

    @FXML
    private WebView webView;
    @FXML
    public JFXTextField txtEnlace;
    private WebEngine engine;
    RegistroPeliculaViewController registroPelicula;
    private static String trailerPrueba;

    private String resultado;
    @FXML
    private JFXButton btnObtenerEnlace;
    @FXML
    private JFXButton btnAceptar;
    UsuarioDto usuarioDto;

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
        engine = webView.getEngine();
        engine.load("https://www.youtube.com/");
        txtEnlace.setText("");
        resultado = null;
    }

    @FXML
    private void OnActionBtnObtenerEnlace(ActionEvent event) {
        txtEnlace.setText(engine.getLocation());
        trailerPrueba = txtEnlace.getText();
    }

    public static String getTrailerPrueba() {
        return trailerPrueba;
    }

    public static void setTrailerPrueba(String trailerPrueba) {
        RegistroYouTubeViewController.trailerPrueba = trailerPrueba;
    }

    public String getResultado() {
        return resultado;
    }

    @FXML
    private void OnActionBtnAceptar(ActionEvent event) {
        resultado = engine.getLocation();

        if (resultado.equals("https://www.youtube.com/")) {
            new Mensaje().showModal(Alert.AlertType.INFORMATION, "", getStage(), "Debe cargar un trailer");
            resultado = null;
        } else {
            engine.load("");
            getStage().close();
        }
    }
    public void confirmarSalida(){
        engine.load("");
        
    }
    
        private void cambiarIdioma(String pIdioma) throws IOException {
        Idioma idiom = new Idioma(pIdioma);
        Properties prop = idiom.getProperties(pIdioma);
        btnObtenerEnlace.setText(prop.getProperty("btnObtenerEnlaceRegistroYouTube"));
        btnAceptar.setText(prop.getProperty("btnAceptarRegistroYouTube"));

    }
}
