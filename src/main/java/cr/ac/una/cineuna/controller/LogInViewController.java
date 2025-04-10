package cr.ac.una.cineuna.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import cr.ac.una.cineuna.idioma.Idioma;
import cr.ac.una.cineuna.model.UsuarioDto;
import cr.ac.una.cineuna.service.UsuarioService;
import cr.ac.una.cineuna.util.AppContext;
import cr.ac.una.cineuna.util.FlowController;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Cristhofer
 */
public class LogInViewController extends Controller implements Initializable {

    @FXML
    private JFXTextField txtUsuario;
    @FXML
    private JFXPasswordField txtContrasena;
    private JFXButton BtnIniciarSeseion;
    @FXML
    private AnchorPane root;
    @FXML
    private ImageView imvFondo;
    @FXML
    private JFXCheckBox chkAdministrador;
    @FXML
    private Text lblTitulo;
    @FXML
    private JFXButton BtnIniciarSesion;
    @FXML
    private Text lblOlvidoContrasenna;
    @FXML
    private JFXButton btnRecuperarContrasenna;
    @FXML
    private Text lblNuevoEnLaAplicacion;
    @FXML
    private JFXButton btnCrearCuenta;
    @FXML
    private Text lblIdioma;
    @FXML
    private JFXCheckBox chkEspannol;
    @FXML
    private JFXCheckBox chkIngles;
    private static String idioma;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        imvFondo.fitHeightProperty().bind(root.heightProperty());
        imvFondo.fitWidthProperty().bind(root.widthProperty());
    }

    @Override
    public void initialize() {

    }

    @FXML
    private void OnActionBtnCrearCuenta(ActionEvent event) {
        FlowController.getInstance().goViewInWindow("RegistroUsuarioView");
        stage.close();
    }

    @FXML
    private void OnActionBtnIniciarSesion(ActionEvent event) {
        try {
            if (txtUsuario.getText() == null || txtUsuario.getText().isEmpty()) {

                if (chkEspannol.isSelected() == true) {
                    new Mensaje().showModal(Alert.AlertType.ERROR, "Usuario", (Stage) BtnIniciarSesion.getScene().getWindow(), "Digite un usuario");

                } else if (chkIngles.isSelected() == true) {
                    new Mensaje().showModal(Alert.AlertType.ERROR, "User", (Stage) BtnIniciarSesion.getScene().getWindow(), "Enter a user");

                }

            } else if (txtContrasena.getText() == null || txtContrasena.getText().isEmpty()) {

                if (chkEspannol.isSelected() == true) {
                    new Mensaje().showModal(Alert.AlertType.ERROR, "Usuario", (Stage) BtnIniciarSesion.getScene().getWindow(), "Digite una contraseña");

                } else if (chkIngles.isSelected() == true) {
                    new Mensaje().showModal(Alert.AlertType.ERROR, "User", (Stage) BtnIniciarSesion.getScene().getWindow(), "Enter the password");

                }

            } else {
                UsuarioService UsuarioService = new UsuarioService();
                Respuesta respuesta = UsuarioService.getUsuario(txtUsuario.getText(), txtContrasena.getText());
                if (respuesta.getEstado()) {
                    UsuarioDto usuarioDto = (UsuarioDto) respuesta.getResultado("Usuario");
                    AppContext.getInstance().set("Usuario", usuarioDto);
                    AppContext.getInstance().set("Token", usuarioDto.getToken());
                    if (getStage().getOwner() == null) {

                        if ((usuarioDto.getEstado().equals("A")) && (usuarioDto.getRestablecido().equals("S"))) {

                            if ((usuarioDto.getTipo().equals("C") && (chkAdministrador.isSelected() == true))) {

                                if (chkEspannol.isSelected() == true) {
                                    new Mensaje().showModal(Alert.AlertType.INFORMATION, "Acceso", getStage(), "No cuenta con los permisos para ingresar como administrador");

                                } else if (chkIngles.isSelected() == true) {
                                    new Mensaje().showModal(Alert.AlertType.INFORMATION, "Acceso", getStage(), "No admin permission");

                                }

                            } else if ((usuarioDto.getTipo().equals("C") && (chkAdministrador.isSelected() == false))) {

                                FlowController.getInstance().goMain("AccesoUsuarioView");
                                getStage().close();
                            } else if ((usuarioDto.getTipo().equals("A") && (chkAdministrador.isSelected() == true))) {
                                FlowController.getInstance().goMain("AccesoAdministradorView");
                                getStage().close();
                            } else if ((usuarioDto.getTipo().equals("A") && (chkAdministrador.isSelected() == false))) {
                                FlowController.getInstance().goMain("AccesoUsuarioView");
                                getStage().close();
                            }

                        } else if ((usuarioDto.getEstado().equals("I"))) {

                            if (chkEspannol.isSelected() == true) {
                                new Mensaje().showModal(Alert.AlertType.INFORMATION, "Acceso", getStage(), "Ingrese al enlace  enviando al correo: "
                                        + "\n" + usuarioDto.getCorreo() + " para poder activar el usuario");
                            } else if (chkIngles.isSelected() == true) {
                                new Mensaje().showModal(Alert.AlertType.INFORMATION, "Access", getStage(), "Enter the link by sending to the email: "
                                        + "\n" + usuarioDto.getCorreo() + " to activate the user");
                            }

                        } else if ((usuarioDto.getRestablecido().equals("N"))) {

                            if (chkEspannol.isSelected() == true) {
                                new Mensaje().showModal(Alert.AlertType.INFORMATION, "Acceso", getStage(), "Debe restablecer la contraseña ");

                            } else if (chkIngles.isSelected() == true) {
                                new Mensaje().showModal(Alert.AlertType.INFORMATION, "Acceso", getStage(), "You must reset the password");
                            }

                            RestablecerContrasennaViewController restablecerContrasennaViewController = (RestablecerContrasennaViewController) FlowController.getInstance().getController("RestablecerContrasennaView");
                            AppContext.getInstance().set("restablecerContrasenna", usuarioDto);
                            usuarioDto = (UsuarioDto) AppContext.getInstance().get("restablecerContrasenna");
                            FlowController.getInstance().goViewInWindowModal("RestablecerContrasennaView", getStage(), false);
                            txtContrasena.setText("");

                        }
                    }

                } else {
                    if (chkEspannol.isSelected() == true) {
                        new Mensaje().showModal(Alert.AlertType.ERROR, "Acceso", getStage(), respuesta.getMensaje());

                    } else if (chkIngles.isSelected() == true) {
                        new Mensaje().showModal(Alert.AlertType.ERROR, "Access", getStage(), respuesta.getMensaje());

                    }

                }
            }
        } catch (Exception ex) {

            if (chkEspannol.isSelected() == true) {
                Logger.getLogger(LogInViewController.class.getName()).log(Level.SEVERE, "Error ingresando.", ex);

            } else if (chkIngles.isSelected() == true) {
                Logger.getLogger(LogInViewController.class.getName()).log(Level.SEVERE, "Error entering.", ex);

            }

        }
    }

    private void OnActionBtnAccesoAdministrador(ActionEvent event) {
        FlowController.getInstance().goMain("AccesoAdministradorView");
        stage.close();
    }

    private void OnActionBtnAccesoUsuario(ActionEvent event) {
        FlowController.getInstance().goMain("AccesoUsuarioView");
        stage.close();
    }

    @FXML
    private void OnActionBtnRecuperarContrasenna(ActionEvent event) {
        FlowController.getInstance().goViewInWindowModal("RecuperarContrasenaView", getStage(), true);
    }

    @FXML
    private void chkEspannol(ActionEvent event) throws IOException {
        chkIngles.setSelected(false);
        cambiarIdioma("espannol.properties");
        setIdioma("E");
    }

    @FXML
    private void chkIngles(ActionEvent event) throws IOException {
        chkEspannol.setSelected(false);
        cambiarIdioma("ingles.properties");
        setIdioma("I");
    }
    
        private void cambiarIdioma(String pIdioma) throws IOException {
        Idioma idiom = new Idioma(pIdioma);
        Properties prop = idiom.getProperties(pIdioma);
        lblTitulo.setText(prop.getProperty("txtTitulo"));
        chkAdministrador.setText(prop.getProperty("chkAdministrador"));
        txtUsuario.setPromptText(prop.getProperty("txtUsuario"));
        txtContrasena.setPromptText(prop.getProperty("txtContrasenna"));
        lblIdioma.setText(prop.getProperty("txtIdioma"));
        chkEspannol.setText(prop.getProperty("chkEspannol"));
        chkIngles.setText(prop.getProperty("chkIngles"));
        BtnIniciarSesion.setText(prop.getProperty("btnIniciarSesion"));
        lblOlvidoContrasenna.setText(prop.getProperty("txtOlvidoSuContrasenna"));
        btnRecuperarContrasenna.setText(prop.getProperty("btnRecuperarContrasenna"));
        lblNuevoEnLaAplicacion.setText(prop.getProperty("txtNuevoEnLaAplicacion"));
        btnCrearCuenta.setText(prop.getProperty("btnCrearCuenta"));

    }

    public static String getIdioma() {
        return idioma;
    }

    public static void setIdioma(String idioma) {
        LogInViewController.idioma = idioma;
    }

}
