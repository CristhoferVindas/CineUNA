package cr.ac.una.cineuna.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import cr.ac.una.cineuna.idioma.Idioma;
import cr.ac.una.cineuna.model.UsuarioDto;
import cr.ac.una.cineuna.service.UsuarioService;
import cr.ac.una.cineuna.util.AppContext;
import cr.ac.una.cineuna.util.Mensaje;
import cr.ac.una.cineuna.util.Respuesta;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Cristhofer
 */
public class RestablecerContrasennaViewController extends Controller implements Initializable {

    @FXML
    private JFXTextField txtUsuario;
    @FXML
    private JFXPasswordField txtNuevaContrasenna;
    @FXML
    private JFXPasswordField txtConfirmarContrasenna;

    private Object resultado;
    @FXML
    private JFXButton btnCancelar;
    @FXML
    private JFXButton btnAceptar;
    @FXML
    private Text lblTitulo;
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
        UsuarioDto usuarioDto = (UsuarioDto) AppContext.getInstance().get("restablecerContrasenna");
        txtUsuario.setText(usuarioDto.getUsuario());
    }

    public Object getResultado() {
        return resultado;
    }

    public void setResultado(Object resultado) {
        this.resultado = resultado;
    }

    @FXML
    private void OnActionBtnAceptar(ActionEvent event) {
        if (txtNuevaContrasenna.getText().isEmpty()) {

            if (usuarioDto.getIdioma().equals("E")) {
                new Mensaje().showModal(Alert.AlertType.INFORMATION, "Restablecer contraseña", getStage(), "Debe digitar una contraseña");
            } else if (usuarioDto.getIdioma().equals("I")) {
                new Mensaje().showModal(Alert.AlertType.INFORMATION, "Restore password", getStage(), "You must enter a password");
            }
        } else if (txtConfirmarContrasenna.getText().isEmpty()) {
            if (usuarioDto.getIdioma().equals("E")) {
                new Mensaje().showModal(Alert.AlertType.INFORMATION, "Restablecer contraseña", getStage(), "Debe confirmar la contraseña");
            } else if (usuarioDto.getIdioma().equals("I")) {
                new Mensaje().showModal(Alert.AlertType.INFORMATION, "Password", getStage(), "You must confirm the password");

            }

        } else {

            if (txtNuevaContrasenna.getText().equals(txtConfirmarContrasenna.getText())) {
                try {
                    UsuarioDto usuarioDto = (UsuarioDto) AppContext.getInstance().get("restablecerContrasenna");
                    usuarioDto.setContrasenna(txtNuevaContrasenna.getText());
                    usuarioDto.setRestablecido("S");
                    UsuarioService service = new UsuarioService();
                    Respuesta respuesta = service.guardarUsuario(usuarioDto);
                    if (usuarioDto.getIdioma().equals("E")) {
                        new Mensaje().showModal(Alert.AlertType.INFORMATION, "Restablecer usuario", getStage(), "Usuario restablecido correctamente.");
                    } else if (usuarioDto.getIdioma().equals("I")) {
                        new Mensaje().showModal(Alert.AlertType.INFORMATION, "Reset user", getStage(), "User reset successfully.");

                    }
                    getStage().close();
                } catch (Exception ex) {
                    if (usuarioDto.getIdioma().equals("E")) {
                        Logger.getLogger(RestablecerContrasennaViewController.class.getName()).log(Level.SEVERE, "Error restableciendo el usuario.", ex);
                        new Mensaje().showModal(Alert.AlertType.ERROR, "Restablecer usuario", getStage(), "Ocurrio un error restableciendo el usuario.");
                    } else if (usuarioDto.getIdioma().equals("I")) {
                        Logger.getLogger(RestablecerContrasennaViewController.class.getName()).log(Level.SEVERE, "Reset user", ex);
                        new Mensaje().showModal(Alert.AlertType.ERROR, "Reset user", getStage(), "Reset user");
                    }

                }

            } else {
                if (usuarioDto.getIdioma().equals("E")) {
                    new Mensaje().showModal(Alert.AlertType.INFORMATION, "Restablecer usuario", getStage(), "Las contraseñas no coinciden, inténtelo de nuevo.");
                } else if (usuarioDto.getIdioma().equals("I")) {
                    new Mensaje().showModal(Alert.AlertType.INFORMATION, "Reset user", getStage(), "The passwords do not match, please try again.");

                }

            }
        }
    }

    @FXML
    private void OnActionBtnCancelar(ActionEvent event) {
        getStage().close();
    }

    private void cambiarIdioma(String pIdioma) throws IOException {
        Idioma idiom = new Idioma(pIdioma);
        Properties prop = idiom.getProperties(pIdioma);
        lblTitulo.setText(prop.getProperty("TituloRestablecerContrasenna"));
        txtUsuario.setPromptText(prop.getProperty("txtUsuarioRestablecerContrasenna"));
        txtNuevaContrasenna.setPromptText(prop.getProperty("txtNuevaContrasennaRestablecerContrasenna"));
        txtConfirmarContrasenna.setPromptText(prop.getProperty("txtConfirmarContrasenna"));
        btnAceptar.setText(prop.getProperty("btnAceptar"));
        btnCancelar.setText(prop.getProperty("btnCancelar"));

    }
}
