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
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Cristhofer
 */
public class AccesoUsuarioViewController extends Controller implements Initializable {

    @FXML
    private BorderPane root;
    @FXML
    private JFXButton BtnCerrarSesion;
    @FXML
    private Text lblTitulo;
    @FXML
    private JFXButton btnEstrenos;
    @FXML
    private JFXButton btnCartelera;
    UsuarioDto usuarioDto;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
    private void OnActionBtnEstrenos(ActionEvent event) {
        FlowController.getInstance().goView("PeliculasPorEstrenarseView");
        //stage.close();
    }

    @FXML
    private void OnActionBtnCartelera(ActionEvent event) {
        FlowController.getInstance().goView("PeliculasEnCarteleraView");
        //stage.close();
    }

    @FXML
    private void OnActionBtnCerrarSesion(ActionEvent event) {
        FlowController.getInstance().initialize();
        FlowController.getInstance().goViewInWindow("LoginView");
        ((Stage) BtnCerrarSesion.getScene().getWindow()).close();
    }
    
            private void cambiarIdioma(String pIdioma) throws IOException {
        Idioma idiom = new Idioma(pIdioma);
        Properties prop = idiom.getProperties(pIdioma);

        lblTitulo.setText(prop.getProperty("txtTituloAccesoUsuario"));
        btnEstrenos.setText(prop.getProperty("btnEstrenosAccesoUsuario"));
        btnCartelera.setText(prop.getProperty("btnCarteleraAccesoUsuario"));
        BtnCerrarSesion.setText(prop.getProperty("BtnCerrarSesionAccesoUsuario"));

    }
}
