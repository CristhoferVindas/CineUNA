package cr.ac.una.cineuna.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import cr.ac.una.cineuna.idioma.Idioma;
import cr.ac.una.cineuna.model.PeliculaDto;
import cr.ac.una.cineuna.model.UsuarioDto;
import cr.ac.una.cineuna.service.PeliculaService;
import cr.ac.una.cineuna.util.AppContext;
import cr.ac.una.cineuna.util.FlowController;
import cr.ac.una.cineuna.util.Mensaje;
import cr.ac.una.cineuna.util.Respuesta;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * FXML Controller class
 *
 * @author Cristhofer
 */
public class DetallePeliculaPorEstrenarseViewController extends Controller implements Initializable {

    @FXML
    private JFXTextField txtNombre;
    @FXML
    private JFXTextField txtFechaEstreno;
    @FXML
    private JFXTextField txtResenna;
    @FXML
    private WebView webView;
    @FXML
    private ImageView imgPortada;

    private boolean boton = false;
    PeliculaDto peliculaDto;
    private WebEngine engine;
    @FXML
    private JFXButton btnAceptar;
    UsuarioDto usuarioDto;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }

    @Override
    public void initialize() {
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
        peliculaDto = new PeliculaDto();
        peliculaDto = (PeliculaDto) AppContext.getInstance().get("PeliculaSeleccionada");
        cargarPelicula(peliculaDto.getId());

    }

    @FXML
    private void OnActionBtnEstrenos(ActionEvent event) {
        getStage().close();
    }

    private void cargarPelicula(Long id) {
        PeliculaService service = new PeliculaService();
        Respuesta respuesta = service.getPelicula(id);

        if (respuesta.getEstado()) {

            if (usuarioDto.getIdioma().equals("E")) {
                //-------------ESPAÑOL---------------
                peliculaDto = (PeliculaDto) respuesta.getResultado("Pelicula");
                txtNombre.setText(peliculaDto.getNombreEspannol());
                txtFechaEstreno.setText(peliculaDto.getFechaEstreno().toString());
                txtResenna.setText(peliculaDto.getResennaEspannol());
                engine = webView.getEngine();
                engine.load(peliculaDto.getTrailerEspannol());

                byte[] PotadaEspanolByte = Base64.getDecoder().decode(peliculaDto.getPortadaEspannol());
                Image imagePotadaEspañol = new Image(new ByteArrayInputStream(PotadaEspanolByte));
                imgPortada.setImage(imagePotadaEspañol);
            } else if (usuarioDto.getIdioma().equals("I")) {

                peliculaDto = (PeliculaDto) respuesta.getResultado("Pelicula");
                txtNombre.setText(peliculaDto.getNombreIngles());
                txtFechaEstreno.setText(peliculaDto.getFechaEstreno().toString());
                txtResenna.setText(peliculaDto.getNombreIngles());
                engine = webView.getEngine();
                engine.load(peliculaDto.getPortadaIngles());

                byte[] PotadaEspanolByte = Base64.getDecoder().decode(peliculaDto.getPortadaEspannol());
                Image imagePotadaIngles = new Image(new ByteArrayInputStream(PotadaEspanolByte));
                imgPortada.setImage(imagePotadaIngles);

            }

        } else {
            if (usuarioDto.getIdioma().equals("E")) {
                new Mensaje().showModal(Alert.AlertType.ERROR, "Cargar pelicula", getStage(), respuesta.getMensaje());
            } else if (usuarioDto.getIdioma().equals("I")){
                new Mensaje().showModal(Alert.AlertType.ERROR, "Consult movies", getStage(), respuesta.getMensaje());
            }
        }
    }

    private void cambiarIdioma(String pIdioma) throws IOException {
        Idioma idiom = new Idioma(pIdioma);
        Properties prop = idiom.getProperties(pIdioma);
        btnAceptar.setText(prop.getProperty("btnAceptar"));

    }

}
