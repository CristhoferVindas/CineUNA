package cr.ac.una.cineuna.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import cr.ac.una.cineuna.idioma.Idioma;
import cr.ac.una.cineuna.model.UsuarioDto;
import cr.ac.una.cineuna.service.UsuarioService;
import cr.ac.una.cineuna.util.FlowController;
import cr.ac.una.cineuna.util.Formato;
import cr.ac.una.cineuna.util.Mensaje;
import cr.ac.una.cineuna.util.Respuesta;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Cristhofer
 */
public class RegistroUsuarioViewController extends Controller implements Initializable {

    @FXML
    private JFXTextField txtNombre;
    @FXML
    private JFXTextField txtPrimerApellido;
    @FXML
    private JFXTextField txtSegundoApellido;
    @FXML
    private JFXTextField txtUsuario;
    @FXML
    private JFXPasswordField txtContrasenna;
    @FXML
    private JFXTextField txtCorreoElectronico;
    @FXML
    private JFXTextField txtId;
    @FXML
    private JFXCheckBox chkIngles;
    @FXML
    private JFXCheckBox chkEspannol;

    UsuarioDto usuarioDto;
    List<Node> requeridos = new ArrayList<>();
    @FXML
    private Label lblTitulo;
    @FXML
    private Label lblIdioma;
    @FXML
    private JFXButton btnInicio;
    @FXML
    private JFXButton btnNuevo;
    @FXML
    private JFXButton btnGuardar;
    LogInViewController logIn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtId.setTextFormatter(Formato.getInstance().integerFormat());
        txtNombre.setTextFormatter(Formato.getInstance().cedulaFormat(30));
        txtPrimerApellido.setTextFormatter(Formato.getInstance().letrasFormat(30));
        txtSegundoApellido.setTextFormatter(Formato.getInstance().letrasFormat(30));
        txtUsuario.setTextFormatter(Formato.getInstance().maxLengthFormat(30));
        txtContrasenna.setTextFormatter(Formato.getInstance().maxLengthFormat(30));
        txtCorreoElectronico.setTextFormatter(Formato.getInstance().maxLengthFormat(50));

        usuarioDto = new UsuarioDto();
        nuevoUsuario();
        indicarRequeridos();
    }

    private void bindUsuario(Boolean nuevo) {
        if (!nuevo) {
            txtId.textProperty().bind(usuarioDto.id);
        }
        txtNombre.textProperty().bindBidirectional(usuarioDto.nombre);
        txtPrimerApellido.textProperty().bindBidirectional(usuarioDto.pApellido);
        txtSegundoApellido.textProperty().bindBidirectional(usuarioDto.sApellido);
        txtUsuario.textProperty().bindBidirectional(usuarioDto.usuario);
        txtContrasenna.textProperty().bindBidirectional(usuarioDto.contrasenna);
        txtCorreoElectronico.textProperty().bindBidirectional(usuarioDto.correo);

    }

    private void unbindUsuario() {
        txtId.textProperty().unbind();
        txtNombre.textProperty().unbindBidirectional(usuarioDto.nombre);
        txtPrimerApellido.textProperty().unbindBidirectional(usuarioDto.pApellido);
        txtSegundoApellido.textProperty().unbindBidirectional(usuarioDto.sApellido);
        txtUsuario.textProperty().unbindBidirectional(usuarioDto.usuario);
        txtContrasenna.textProperty().unbindBidirectional(usuarioDto.contrasenna);
        txtCorreoElectronico.textProperty().unbindBidirectional(usuarioDto.correo);

    }

    @Override
    public void initialize() {
        if (logIn.getIdioma() == "I") {

            try {
                cambiarIdioma("ingles.properties");
            } catch (IOException ex) {
                new Mensaje().showModal(Alert.AlertType.ERROR, "Idioma", getStage(), "Error al obtener idioma");
            }

        } else if (logIn.getIdioma() == "E") {
            try {
                cambiarIdioma("espannol.properties");
            } catch (IOException ex) {
                new Mensaje().showModal(Alert.AlertType.ERROR, "Idioma", getStage(), "Error al obtener idioma");
            }

        }

    }

    private void OnActionBtnAtras(ActionEvent event) {
        FlowController.getInstance().goViewInWindow("HolaView");
        stage.close();
    }

    @FXML
    private void seleccionIngles(ActionEvent event) {
        chkEspannol.setSelected(false);
        usuarioDto.setIdioma("I");
    }

    @FXML
    private void seleccionEspannol(ActionEvent event) {
        chkIngles.setSelected(false);
        usuarioDto.setIdioma("E");
    }

    @FXML
    private void OnActionBtnNuevo(ActionEvent event) {
        if (usuarioDto.getIdioma().equals("E")) {
            if (new Mensaje().showConfirmation("Usuario", getStage(), "¿Limpiar el registro?")) {
                nuevoUsuario();
            } else if (usuarioDto.getIdioma().equals("I")) {
                if (new Mensaje().showConfirmation("User", getStage(), "clean the registry?")) {
                    nuevoUsuario();
                }
            }

        }
    }

    @FXML
    private void OnActionBtnGuardar(ActionEvent event) {
        try {
            String invalidos = validarRequeridos();
            if (!invalidos.isEmpty()) {
                if (usuarioDto.getIdioma().equals("E")) {
                    new Mensaje().showModal(Alert.AlertType.ERROR, "Guardar usuario", getStage(), invalidos);
                } else if (usuarioDto.getIdioma().equals("I")) {
                    new Mensaje().showModal(Alert.AlertType.ERROR, "Save user", getStage(), invalidos);
                }

            } else {
                UsuarioService service = new UsuarioService();
                usuarioDto.setTipo("C");
                usuarioDto.setEstado("I");
                usuarioDto.setRestablecido("S");
                Respuesta respuesta = service.guardarUsuario(usuarioDto);
                if (!respuesta.getEstado()) {
                    if (usuarioDto.getIdioma().equals("E")) {
                        new Mensaje().showModal(Alert.AlertType.ERROR, "Guardar usuario", getStage(), respuesta.getMensaje());
                    } else if (usuarioDto.getIdioma().equals("I")) {
                        new Mensaje().showModal(Alert.AlertType.ERROR, "Save user", getStage(), respuesta.getMensaje());

                    }

                } else {
                    unbindUsuario();
                    usuarioDto = (UsuarioDto) respuesta.getResultado("Usuario");
                    if (usuarioDto.getIdioma().equals("E")) {
                        new Mensaje().showModal(Alert.AlertType.INFORMATION, "Guardar usuario", getStage(), "User successfully registered."
                                + "\nPara activar el usuario, ingrese al link que ha sido enviado al correo: " + usuarioDto.getCorreo());
                    } else if (usuarioDto.getIdioma().equals("I")) {
                        new Mensaje().showModal(Alert.AlertType.INFORMATION, "Save user", getStage(), "."
                                + "\nTo activate the user, enter the link that has been sent to the email: " + usuarioDto.getCorreo());

                    }

                    try {
                        Properties propiedades = new Properties();
                        propiedades.setProperty("mail.smtp.host", "smtp.gmail.com");
                        propiedades.setProperty("mail.smtp.starttls.enable", "true");
                        propiedades.setProperty("mail.smtp.port", "587");
                        propiedades.setProperty("mail.smtp.auth", "true");

                        Session sesion = Session.getDefaultInstance(propiedades);
                        String correo_emisor = "cineuna32@gmail.com";
                        String contraseña_emisor = "wysyrjtzhpgxiuqp";

                        String correo_receptor = txtCorreoElectronico.getText();
                        if (usuarioDto.getIdioma().equals("E")) {
                            String asunto = "Activación de cuenta";
                            String mensaje = "Hola " + txtNombre.getText() + ", se envió un link de activación para su cuenta."
                                    + "\n\nLink de activación:  " + "http://localhost:9090/WsCineUNA/ws/UsuarioController/usuarioActivo/" + usuarioDto.getId()
                                    + "\n\nSi no realizó la solicitud de activación, ignore este mensaje."
                                    + "\n\nSaludos, "
                                    + "\nCineUNA";

                            MimeMessage message = new MimeMessage(sesion);
                            message.setFrom(new InternetAddress(correo_emisor));
                            message.addRecipient(Message.RecipientType.TO, new InternetAddress(correo_receptor));
                            message.setSubject(asunto);
                            message.setText(mensaje);

                            Transport transporte = sesion.getTransport("smtp");
                            transporte.connect(correo_emisor, contraseña_emisor);
                            transporte.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
                            transporte.close();
                        } else if (usuarioDto.getIdioma().equals("I")) {
                            String asunto = "Account activation";
                            String mensaje = "Hello " + txtNombre.getText() + ", An activation link has been sent for your account."
                                    + "\n\nLink: " + "http://localhost:9090/WsCineUNA/ws/UsuarioController/usuarioActivo/" + usuarioDto.getId()
                                    + "\n\nIf you did not make the activation request, please ignore this message."
                                    + "\nCineUNA";

                            MimeMessage message = new MimeMessage(sesion);
                            message.setFrom(new InternetAddress(correo_emisor));
                            message.addRecipient(Message.RecipientType.TO, new InternetAddress(correo_receptor));
                            message.setSubject(asunto);
                            message.setText(mensaje);

                            Transport transporte = sesion.getTransport("smtp");
                            transporte.connect(correo_emisor, contraseña_emisor);
                            transporte.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
                            transporte.close();

                        }

                    } catch (AddressException ex) {
                        if (usuarioDto.getIdioma().equals("E")) {
                            new Mensaje().showModal(Alert.AlertType.ERROR, "Correo", getStage(), "Error al enviar el correo");

                        } else if (usuarioDto.getIdioma().equals("I")) {
                            new Mensaje().showModal(Alert.AlertType.ERROR, "Email", getStage(), "Error sending mail");

                        }

                    }

                    FlowController.getInstance().goViewInWindow("LogInView");
                    getStage().close();
                    nuevoUsuario();
                }
            }
        } catch (Exception ex) {
            if (usuarioDto.getIdioma().equals("E")) {
                Logger.getLogger(RegistroUsuarioViewController.class.getName()).log(Level.SEVERE, "Error guardando el usuario.", ex);
                new Mensaje().showModal(Alert.AlertType.ERROR, "Guardar usuario", getStage(), "Ocurrio un error guardando el usuario.");
            } else if (usuarioDto.getIdioma().equals("I")) {
                Logger.getLogger(RegistroUsuarioViewController.class.getName()).log(Level.SEVERE, "Error saving user.", ex);
                new Mensaje().showModal(Alert.AlertType.ERROR, "Save user.", getStage(), "Error saving user..");

            }

        }
    }

    @FXML
    private void OnActionBtnInicio(ActionEvent event) {
        FlowController.getInstance().goViewInWindow("LogInView");
        stage.close();
    }

    private void nuevoUsuario() {
        unbindUsuario();
        usuarioDto = new UsuarioDto();
        bindUsuario(true);
        txtId.clear();
        txtId.requestFocus();
    }

    public void indicarRequeridos() {
        requeridos.clear();
        requeridos.addAll(Arrays.asList(txtNombre, txtPrimerApellido, txtSegundoApellido,
                txtUsuario, txtContrasenna, txtCorreoElectronico));
    }

    public String validarRequeridos() {
        Boolean validos = true;
        String invalidos = "";
        for (Node node : requeridos) {
            if (node instanceof JFXTextField && ((JFXTextField) node).getText() == null) {
                if (validos) {
                    invalidos += ((JFXTextField) node).getPromptText();
                } else {
                    invalidos += "," + ((JFXTextField) node).getPromptText();
                }
                validos = false;
            } else if (node instanceof JFXPasswordField && ((JFXPasswordField) node).getText() == null) {
                if (validos) {
                    invalidos += ((JFXPasswordField) node).getPromptText();
                } else {
                    invalidos += "," + ((JFXPasswordField) node).getPromptText();
                }
                validos = false;
            } else if (node instanceof JFXDatePicker && ((JFXDatePicker) node).getValue() == null) {
                if (validos) {
                    invalidos += ((JFXDatePicker) node).getAccessibleText();
                } else {
                    invalidos += "," + ((JFXDatePicker) node).getAccessibleText();
                }
                validos = false;
            } else if (node instanceof JFXComboBox && ((JFXComboBox) node).getSelectionModel().getSelectedIndex() < 0) {
                if (validos) {
                    invalidos += ((JFXComboBox) node).getPromptText();
                } else {
                    invalidos += "," + ((JFXComboBox) node).getPromptText();
                }
                validos = false;
            }
        }
        if (validos) {
            return "";
        } else {
            return "Campos requeridos o con problemas de formato [" + invalidos + "].";
        }
    }

    private void cambiarIdioma(String pIdioma) throws IOException {
        Idioma idiom = new Idioma(pIdioma);
        Properties prop = idiom.getProperties(pIdioma);

        lblTitulo.setText(prop.getProperty("txtTituloRegistroUsuario"));
        txtId.setPromptText(prop.getProperty("txtIdRegistroUsuario"));
        txtNombre.setPromptText(prop.getProperty("txtNombreRegistroUsuario"));
        txtPrimerApellido.setPromptText(prop.getProperty("txtPrimerApellidoRegistroUsuario"));
        txtSegundoApellido.setPromptText(prop.getProperty("txtSegundoApellidoRegistroUsuario"));
        txtUsuario.setPromptText(prop.getProperty("txtUsuarioRegistroUsuario"));
        txtContrasenna.setPromptText(prop.getProperty("txtContrasennaRegistroUsuario"));
        txtCorreoElectronico.setPromptText(prop.getProperty("txtCorreoElectronicoRegistroUsuario"));
        lblIdioma.setText(prop.getProperty("txtIdiomaRegistroUsuario"));
        chkEspannol.setText(prop.getProperty("chkEspannolRegistroUsuario"));
        chkIngles.setText(prop.getProperty("chkInglesRegistroUsuario"));

        btnNuevo.setText(prop.getProperty("btnNuevo"));
        btnInicio.setText(prop.getProperty("btnInicio"));
        btnGuardar.setText(prop.getProperty("btnGuardar"));

    }
}
