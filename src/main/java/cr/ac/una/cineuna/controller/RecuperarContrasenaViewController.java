package cr.ac.una.cineuna.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import cr.ac.una.cineuna.idioma.Idioma;
import cr.ac.una.cineuna.model.AlimentoDto;
import cr.ac.una.cineuna.model.UsuarioDto;
import cr.ac.una.cineuna.service.AlimentoService;
import cr.ac.una.cineuna.service.UsuarioService;
import cr.ac.una.cineuna.util.Mensaje;
import cr.ac.una.cineuna.util.Respuesta;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.net.URL;
import java.security.SecureRandom;
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
public class RecuperarContrasenaViewController extends Controller implements Initializable {

    @FXML
    private JFXTextField txtCorreo;
    UsuarioDto usuarioDto;
    String contraGlobal;
    @FXML
    private Text lblDescripcion;
    @FXML
    private JFXButton btnCancelar;
    @FXML
    private JFXButton btnAceptar;
    LogInViewController logIn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        usuarioDto = new UsuarioDto();
        txtCorreo.setText("");

    }

    @Override
    public void initialize() {
        txtCorreo.setText("");
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

    public String ContrasennaAleatoria() {

        String chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 10; i++) { // lo hacemos con 10 caracteres
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));  //lo agrega a la instancia 
        }
        contraGlobal = sb.toString();
        return sb.toString();
    }

    @FXML
    private void OnActionBtnCancelar(ActionEvent event) {
        getStage().close();
    }

    @FXML
    private void OnActionBtnAceptar(ActionEvent event) {
        if ((txtCorreo.getText() == "") || (txtCorreo.getText().isEmpty())) {
            if (usuarioDto.getIdioma().equals("E")) {
                new Mensaje().showModal(Alert.AlertType.INFORMATION, "Ingresar correo", getStage(), "Ingrese un correo electrónico");
            } else if (usuarioDto.getIdioma().equals("I")) {
                new Mensaje().showModal(Alert.AlertType.INFORMATION, "Email", getStage(), "Enter an email");

            }

        } else {
            try {
                Properties propiedades = new Properties();
                propiedades.setProperty("mail.smtp.host", "smtp.gmail.com");
                propiedades.setProperty("mail.smtp.starttls.enable", "true");
                propiedades.setProperty("mail.smtp.port", "587");
                propiedades.setProperty("mail.smtp.auth", "true");

                Session sesion = Session.getDefaultInstance(propiedades);
                String correo_emisor = "cineuna32@gmail.com";
                String contraseña_emisor = "wysyrjtzhpgxiuqp";

                String correo_receptor = txtCorreo.getText();
                if (usuarioDto.getIdioma().equals("E")) {
                    
                    String asunto = "Recuperar contraseña";
                    String mensaje = "Hola, se solicitó un restablecimiento de contraseña para su cuenta."
                            + "\n\nSu nueva contraseña es:  " + ContrasennaAleatoria()
                            + "\n\nSi no realizó la solicitud de cambio de contraseña, ignore este mensaje."
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
                    String asunto = "Recover password";
                    String mensaje = "Hello, a password reset has been requested for your account.."
                            + "\n\nYour new password is:  " + ContrasennaAleatoria()
                            + "\n\nIf you did not make the password change request, please ignore this message."
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

                

                UsuarioService service = new UsuarioService();
                String correo = txtCorreo.getText();
                Respuesta respuesta = service.getUsuarioPorCorreo(correo);
                if (respuesta.getEstado()) {
                    usuarioDto = (UsuarioDto) respuesta.getResultado("Usuario");
                    usuarioDto.setRestablecido("N");
                    usuarioDto.setContrasenna(contraGlobal);
                    respuesta = service.guardarUsuario(usuarioDto);
                    usuarioDto = (UsuarioDto) respuesta.getResultado("Usuario");
                    getStage().close();
                } else {
                    if(usuarioDto.getIdioma().equals("E")){
                         new Mensaje().showModal(Alert.AlertType.ERROR, "Restablecer contraseña", getStage(), "Error restablecliendo la contraseña");
                       
                         
                    }else if(usuarioDto.getIdioma().equals("I")){
                         new Mensaje().showModal(Alert.AlertType.ERROR, "Restore password", getStage(), "Password reset error");
                        
                    }
            
                   

                }

            } catch (AddressException ex) {
                if (usuarioDto.getIdioma().equals("E")) {
                    new Mensaje().showModal(Alert.AlertType.ERROR, "Correo", getStage(), "Error al enviar el correo");
                } else if (usuarioDto.getIdioma().equals("I")) {
                    new Mensaje().showModal(Alert.AlertType.ERROR, "Email", getStage(), "Error sending mail");
                }

            } catch (MessagingException ex) {
                if (usuarioDto.getIdioma().equals("E")) {
                    new Mensaje().showModal(Alert.AlertType.ERROR, "Correo", getStage(), "Error al enviar el correo");
                } else if (usuarioDto.getIdioma().equals("I")) {
                    new Mensaje().showModal(Alert.AlertType.ERROR, "Email", getStage(), "Error sending mail");
                }
            }
        }
    }

    private void cambiarIdioma(String pIdioma) throws IOException {
        Idioma idiom = new Idioma(pIdioma);
        Properties prop = idiom.getProperties(pIdioma);
        lblDescripcion.setText(prop.getProperty("txtInformacionRecuperarContrasenna"));
        btnCancelar.setText(prop.getProperty("btnCancelar"));
        btnAceptar.setText(prop.getProperty("btnAceptar"));

    }
}
