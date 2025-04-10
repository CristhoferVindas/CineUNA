package cr.ac.una.cineuna.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import cr.ac.una.cineuna.idioma.Idioma;
import cr.ac.una.cineuna.model.PeliculaDto;
import cr.ac.una.cineuna.model.SalaDto;
import cr.ac.una.cineuna.model.TandaDto;
import cr.ac.una.cineuna.model.UsuarioDto;
import cr.ac.una.cineuna.service.PeliculaService;
import cr.ac.una.cineuna.service.SalaService;
import cr.ac.una.cineuna.service.TandaService;
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
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * FXML Controller class
 *
 * @author Cristhofer
 */
public class DetallePeliculaCarteleraViewController extends Controller implements Initializable {

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
    @FXML
    private Label lblCosto;
    @FXML
    private Label lblFechaInicio;
    @FXML
    private Label lblHoraFin;
    @FXML
    private Label lblHoraInicio;

    PeliculaDto peliculaDto;
    TandaDto tandaDto;
    private WebEngine engine;
    @FXML
    private JFXButton btnComprarEntradas;
    @FXML
    private JFXButton btnBuscarTanda;
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
        tandaDto = new TandaDto();
        peliculaDto = new PeliculaDto();
        peliculaDto = (PeliculaDto) AppContext.getInstance().get("PeliculaSeleccionada");
        cargarPelicula(peliculaDto.getId());
        webView.getEngine().load(peliculaDto.getTrailerIngles());
    }

    private void cargarTanda(Long id) {
        TandaService service = new TandaService();
        Respuesta respuesta = service.getTanda(id);
        if (respuesta.getEstado()) {
            tandaDto = (TandaDto) respuesta.getResultado("Tanda");
            lblCosto.setText(tandaDto.getCostoEntrada().toString());
            lblFechaInicio.setText(tandaDto.getFecha().toString());
            lblHoraInicio.setText(tandaDto.getHoraInicio());
            lblHoraFin.setText(tandaDto.getHoraFin());
        } else {
            if (usuarioDto.getIdioma().equals("E")) {
                new Mensaje().showModal(Alert.AlertType.ERROR, "Cargar tanda", getStage(), respuesta.getMensaje());
            } else if (usuarioDto.getIdioma().equals("I")) {
                new Mensaje().showModal(Alert.AlertType.ERROR, "Load batch", getStage(), respuesta.getMensaje());
            }

        }

    }

    private void cargarPelicula(Long id) {
        PeliculaService service = new PeliculaService();
        Respuesta respuesta = service.getPelicula(id);

        if (respuesta.getEstado()) {

            if (usuarioDto.getIdioma().equals("E")) {
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
            } else {
                new Mensaje().showModal(Alert.AlertType.ERROR, "Load movies", getStage(), respuesta.getMensaje());
            }
        }
    }

    @FXML
    private void OnActionBtnTanda(ActionEvent event) {
        SeleccionTandaViewController seleccionTandaViewController = (SeleccionTandaViewController) FlowController.getInstance().getController("SeleccionTandaView");
        seleccionTandaViewController.initialize();
        FlowController.getInstance().goViewInWindowModal("SeleccionTandaView", getStage(), true);
        TandaDto tandaDto = (TandaDto) seleccionTandaViewController.getResultado();
        if (tandaDto != null) {
            this.tandaDto = tandaDto;
            cargarTanda(tandaDto.getId());
        }
    }

    @FXML
    private void OnActionBtnComprarEntradas(ActionEvent event) {
        if (tandaDto.getId() != null) {
            SalaService service = new SalaService();
            Respuesta respuesta = service.getSala(tandaDto.getIDSala());

            if (respuesta.getEstado()) {
                AppContext.getInstance().set("SalaSeleccionada", (SalaDto) respuesta.getResultado("Sala"));
                AppContext.getInstance().set("TandaSeleccionada", tandaDto);
                FlowController.getInstance().initialize();
                FlowController.getInstance().goViewInWindowModal("SeleccionAsientoView", getStage(), true);
                stage.close();
            }
        } else {
            new Mensaje().showModal(Alert.AlertType.ERROR, "Comprar entradas", getStage(), "Por favor seleccione una tanda para continuar.");
        }
    }

    private void cambiarIdioma(String pIdioma) throws IOException {
        Idioma idiom = new Idioma(pIdioma);
        Properties prop = idiom.getProperties(pIdioma);
        btnBuscarTanda.setText(prop.getProperty("btnBuscarTanda"));
        btnComprarEntradas.setText(prop.getProperty("btnComprarEntradas"));

    }
}
