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
import cr.ac.una.cineuna.util.AppContext;
import cr.ac.una.cineuna.util.Formato;
import cr.ac.una.cineuna.util.Mensaje;
import cr.ac.una.cineuna.util.Respuesta;
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
public class MantenimientoUsuarioViewController extends Controller implements Initializable {

    @FXML
    private JFXTextField txtId;
    @FXML
    private JFXTextField txtNombre;
    @FXML
    private JFXTextField txtPrimerApellido;
    @FXML
    private JFXTextField txtSegundoApellido;
    @FXML
    private JFXTextField txtUsuario;
    @FXML
    private JFXTextField txtCorreoElectronico;
    @FXML
    private JFXCheckBox chkCliente;
    @FXML
    private JFXCheckBox chkAdministrador;

    UsuarioDto usuarioDto;
    List<Node> requeridos = new ArrayList<>();
    @FXML
    private Label lblTitulo;
    @FXML
    private Label lblTipoDeUsuario;
    @FXML
    private JFXButton btnNuevo;
    @FXML
    private JFXButton btnBuscar;
    @FXML
    private JFXButton btnGuardar;

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
        txtCorreoElectronico.setTextFormatter(Formato.getInstance().maxLengthFormat(50));

        usuarioDto = new UsuarioDto();
        nuevoUsuario();
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

    private void bindUsuario(Boolean nuevo) {
        if (!nuevo) {
            txtId.textProperty().bind(usuarioDto.id);
        }
        txtNombre.textProperty().bindBidirectional(usuarioDto.nombre);
        txtPrimerApellido.textProperty().bindBidirectional(usuarioDto.pApellido);
        txtSegundoApellido.textProperty().bindBidirectional(usuarioDto.sApellido);
        txtUsuario.textProperty().bindBidirectional(usuarioDto.usuario);
        txtCorreoElectronico.textProperty().bindBidirectional(usuarioDto.correo);

    }

    private void unbindUsuario() {
        txtId.textProperty().unbind();
        txtNombre.textProperty().unbindBidirectional(usuarioDto.nombre);
        txtPrimerApellido.textProperty().unbindBidirectional(usuarioDto.pApellido);
        txtSegundoApellido.textProperty().unbindBidirectional(usuarioDto.sApellido);
        txtUsuario.textProperty().unbindBidirectional(usuarioDto.usuario);
        txtCorreoElectronico.textProperty().unbindBidirectional(usuarioDto.correo);

    }

    private void nuevoUsuario() {
        unbindUsuario();
        usuarioDto = new UsuarioDto();
        bindUsuario(true);
        txtId.clear();
        txtId.requestFocus();
    }

    @FXML
    private void seleccionCliente(ActionEvent event) {
        chkAdministrador.setSelected(false);
        usuarioDto.setTipo("C");
    }

    @FXML
    private void seleccionAdministrador(ActionEvent event) {
        chkCliente.setSelected(false);
        usuarioDto.setTipo("A");
    }

    @FXML
    private void OnActionBtnNuevo(ActionEvent event) {
        if (usuarioDto.getIdioma().equals("E")) {
            if (new Mensaje().showConfirmation("Usuario", getStage(), "¿Desea limpiar el registro?")) {
                nuevoUsuario();
            }
        } else if (usuarioDto.getIdioma().equals("I")) {
            if (new Mensaje().showConfirmation("User", getStage(), "Do you want to clean the registry?\"")) {
                nuevoUsuario();
            }
        }
    }

    @FXML
    private void OnActionBtnBuscar(ActionEvent event) {
        if (txtId.getText() != null && !txtId.getText().isEmpty()) {
            UsuarioService service = new UsuarioService();
            Respuesta respuesta = service.getUsuario(Long.valueOf(txtId.getText()));

            if (respuesta.getEstado()) {
                unbindUsuario();
                usuarioDto = (UsuarioDto) respuesta.getResultado("Usuario");
                bindUsuario(false);
                if (usuarioDto.getTipo().equals("C")) {
                    chkCliente.setSelected(true);
                    chkAdministrador.setSelected(false);
                } else {
                    chkCliente.setSelected(false);
                    chkAdministrador.setSelected(true);
                }
            } else {
                if (usuarioDto.getIdioma().equals("E")) {
                    new Mensaje().showModal(Alert.AlertType.ERROR, "Cargar usuario", getStage(), respuesta.getMensaje());
                } else if (usuarioDto.getIdioma().equals("I")) {
                    new Mensaje().showModal(Alert.AlertType.ERROR, "Load user", getStage(), respuesta.getMensaje());
                }
            }
        } else {
            if (usuarioDto.getIdioma().equals("E")) {
                new Mensaje().showModal(Alert.AlertType.INFORMATION, "Buscar Usuario", getStage(), "Por favor digite un Id para buscar.");

            } else if (usuarioDto.getIdioma().equals("I")) {
                new Mensaje().showModal(Alert.AlertType.INFORMATION, "Search User", getStage(), "Please enter an ID to search.");
            }

        }
    }

    @FXML
    private void OnActionBtnGuardar(ActionEvent event) {
        try {
            String invalidos = validarRequeridos();
            if (!invalidos.isEmpty()) {
                if (usuarioDto.getIdioma().equals("E")) {
                    new Mensaje().showModal(Alert.AlertType.ERROR, "Guardar Usuario", getStage(), invalidos);
                } else if (usuarioDto.getIdioma().equals("I")) {
                    new Mensaje().showModal(Alert.AlertType.ERROR, "Save User", getStage(), invalidos);
                }
            } else {
                UsuarioService service = new UsuarioService();
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
                    nuevoUsuario();
                    if (usuarioDto.getIdioma().equals("E")) {
                        new Mensaje().showModal(Alert.AlertType.INFORMATION, "Actualizar usuario", getStage(), "Usuario actualizado correctamente");
                    } else if (usuarioDto.getIdioma().equals("I")) {
                        new Mensaje().showModal(Alert.AlertType.INFORMATION, "Update user", getStage(), "User updated successfully");
                    }
                }
            }
        } catch (Exception ex) {
            if (usuarioDto.getIdioma().equals("E")) {
                Logger.getLogger(RegistroUsuarioViewController.class.getName()).log(Level.SEVERE, "Error guardando el usuario.", ex);
                new Mensaje().showModal(Alert.AlertType.ERROR, "Guardar usuario", getStage(), "Ocurrio un error guardando el usuario.");
            } else if (usuarioDto.getIdioma().equals("I")) {
                Logger.getLogger(RegistroUsuarioViewController.class.getName()).log(Level.SEVERE, "Error saving user.", ex);
                new Mensaje().showModal(Alert.AlertType.ERROR, "Save user", getStage(), "Error saving user.");
            }
        }

    }

    public void indicarRequeridos() {
        requeridos.clear();
        requeridos.addAll(Arrays.asList(txtNombre, txtPrimerApellido, txtSegundoApellido, txtUsuario, txtCorreoElectronico));
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

        lblTitulo.setText(prop.getProperty("txtTituloMantenimientoUsuario"));

        txtId.setText(prop.getProperty("txtIdMantenimientoUsuario"));
        txtNombre.setText(prop.getProperty("txtNombreRegistroUsuario"));

        txtPrimerApellido.setText(prop.getProperty("txtPrimerApellidoRegistroUsuario"));
        txtSegundoApellido.setText(prop.getProperty("txtSegundoApellidoRegistroUsuario"));

        txtUsuario.setText(prop.getProperty("txtUsuarioRegistroUsuario"));
        txtCorreoElectronico.setText(prop.getProperty("txtCorreoElectronicoRegistroUsuario"));
        lblTipoDeUsuario.setText(prop.getProperty("txtTipoUsuarioMantenimientoUsuario"));
        chkCliente.setText(prop.getProperty("chkClienteMantenimientoUsuario"));
        chkAdministrador.setText(prop.getProperty("chkAdministradorMantenimientoUsuario"));

        btnNuevo.setText(prop.getProperty("btnNuevo"));
        btnBuscar.setText(prop.getProperty("btnBuscar"));
        btnGuardar.setText(prop.getProperty("btnGuardar"));

    }

}
