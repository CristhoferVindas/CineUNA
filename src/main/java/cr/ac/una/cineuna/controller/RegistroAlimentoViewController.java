package cr.ac.una.cineuna.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import cr.ac.una.cineuna.idioma.Idioma;
import cr.ac.una.cineuna.model.AlimentoDto;
import cr.ac.una.cineuna.model.SalaDto;
import cr.ac.una.cineuna.model.UsuarioDto;
import cr.ac.una.cineuna.service.AlimentoService;
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
public class RegistroAlimentoViewController extends Controller implements Initializable {

    @FXML
    private JFXTextField txtNombre;
    @FXML
    private JFXTextField txtPrecio;
    @FXML
    private JFXCheckBox chkComida;
    @FXML
    private JFXCheckBox chkBebida;
    @FXML
    private JFXTextField txtId;

    AlimentoDto alimentoDto;
    List<Node> requeridos = new ArrayList<>();
    @FXML
    private Label lblTitulo;
    @FXML
    private Label lblTipoDeAlimento;
    @FXML
    private JFXButton btnNuevo;
    @FXML
    private JFXButton btnBuscar;
    @FXML
    private JFXButton btnEliminar;
    @FXML
    private JFXButton btnGuardar;
    UsuarioDto usuarioDto;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtId.setTextFormatter(Formato.getInstance().integerFormat());
        txtNombre.setTextFormatter(Formato.getInstance().letrasFormat(30));
        txtPrecio.setTextFormatter(Formato.getInstance().integerFormat());
        alimentoDto = new AlimentoDto();
        nuevoAlimento();
        indicarRequeridos();

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
    private void seleccionComida(ActionEvent event) {
        chkBebida.setSelected(false);
        alimentoDto.setTipo("C");

    }

    @FXML
    private void seleccionBebida(ActionEvent event) {
        chkComida.setSelected(false);
        alimentoDto.setTipo("B");
    }

    @FXML
    private void OnActionBtnNuevo(ActionEvent event) {

        if (usuarioDto.getIdioma().equals("E")) {
            if (new Mensaje().showConfirmation("alimento", getStage(), "¿Desea limpiar el registro?")) {
                nuevoAlimento();
            }
        } else if (usuarioDto.getIdioma().equals("I")) {
            if (new Mensaje().showConfirmation("Food", getStage(), "Do you want to clean the registry?")) {
                nuevoAlimento();
            }
        }
    }

    @FXML
    private void OnActionBtnBuscar(ActionEvent event) {
        if (txtId.getText() != null && !txtId.getText().isEmpty()) {
            AlimentoService service = new AlimentoService();
            Respuesta respuesta = service.getAlimento(Long.valueOf(txtId.getText()));

            if (respuesta.getEstado()) {
                unbindAlimento();
                alimentoDto = (AlimentoDto) respuesta.getResultado("Alimento");
                if (alimentoDto.getTipo().equals("C")) {
                    chkComida.setSelected(true);
                    chkBebida.setSelected(false);
                } else {
                    chkBebida.setSelected(true);
                    chkComida.setSelected(false);
                }
                bindAlimento(false);
                validarRequeridos();
            } else {
                if (usuarioDto.getIdioma().equals("E")) {
                    new Mensaje().showModal(Alert.AlertType.ERROR, "Cargar alimento", getStage(), respuesta.getMensaje());
                } else if (usuarioDto.getIdioma().equals("I")) {
                    new Mensaje().showModal(Alert.AlertType.ERROR, "load Food", getStage(), respuesta.getMensaje());

                }

            }
        } else {
            if (usuarioDto.getIdioma().equals("E")) {
                new Mensaje().showModal(Alert.AlertType.INFORMATION, "Buscar Alimento", getStage(), "Por favor digite un Id para buscar.");

            } else if (usuarioDto.getIdioma().equals("I")) {
                new Mensaje().showModal(Alert.AlertType.INFORMATION, "Search food", getStage(), "Please enter an ID to search.");
            }

        }

    }

    @FXML
    private void OnActionBtnEliminar(ActionEvent event) {
        try {
            if (alimentoDto.getId() == null) {
                if (usuarioDto.getIdioma().equals("E")) {
                    new Mensaje().showModal(Alert.AlertType.ERROR, "Eliminar Alimento", getStage(), "Debe cargar el Alimento que desea eliminar.");
                } else if (usuarioDto.getIdioma().equals("I")) {
                    new Mensaje().showModal(Alert.AlertType.ERROR, "delete food", getStage(), "You need to upload the Food you want to delete.");

                }

            } else {

                AlimentoService service = new AlimentoService();
                Respuesta respuesta = service.eliminarAlimento(alimentoDto.getId());
                if (!respuesta.getEstado()) {

                    if (usuarioDto.getIdioma().equals("E")) {
                        new Mensaje().showModal(Alert.AlertType.ERROR, "Eliminar Alimento", getStage(), respuesta.getMensaje());
                    } else if (usuarioDto.getIdioma().equals("I")) {
                        new Mensaje().showModal(Alert.AlertType.ERROR, "delete food", getStage(), respuesta.getMensaje());

                    }

                } else {
                    if (usuarioDto.getIdioma().equals("E")) {
                        new Mensaje().showModal(Alert.AlertType.INFORMATION, "Eliminar Alimento", getStage(), "Alimento eliminado correctamente.");
                        nuevoAlimento();
                    } else if (usuarioDto.getIdioma().equals("I")) {
                        new Mensaje().showModal(Alert.AlertType.INFORMATION, "delete food", getStage(), "Food removed correctly.");
                        nuevoAlimento();
                    }

                }
            }
        } catch (Exception ex) {

            if (usuarioDto.getIdioma().equals("E")) {
                Logger.getLogger(RegistroAlimentoViewController.class.getName()).log(Level.SEVERE, "Error eliminando el Alimento.", ex);
                new Mensaje().showModal(Alert.AlertType.ERROR, "Eliminar Alimento", getStage(), "Ocurrio un error eliminando el Alimento.");

            } else if (usuarioDto.getIdioma().equals("I")) {
                Logger.getLogger(RegistroAlimentoViewController.class.getName()).log(Level.SEVERE, "Error delete Food", ex);
                new Mensaje().showModal(Alert.AlertType.ERROR, "Delete food", getStage(), "An error occurred deleting the Food.");

            }

        }
    }

    @FXML
    private void OnActionBtnGuardar(ActionEvent event) {
        try {
            String invalidos = validarRequeridos();
            if (!invalidos.isEmpty()) {
                if (usuarioDto.getIdioma().equals("E")) {
                    new Mensaje().showModal(Alert.AlertType.ERROR, "Guardar Alimento", getStage(), invalidos);
                } else if (usuarioDto.getIdioma().equals("I")) {
                    new Mensaje().showModal(Alert.AlertType.ERROR, "Save foodo", getStage(), invalidos);

                }

            } else {
                AlimentoService service = new AlimentoService();

                Respuesta respuesta = service.guardarAlimento(alimentoDto);
                if (!respuesta.getEstado()) {
                    if (usuarioDto.getIdioma().equals("E")) {
                        new Mensaje().showModal(Alert.AlertType.ERROR, "Save food", getStage(), respuesta.getMensaje());
                    } else if (usuarioDto.getIdioma().equals("I")) {
                        new Mensaje().showModal(Alert.AlertType.ERROR, "Save food", getStage(), respuesta.getMensaje());

                    }

                } else {
                    unbindAlimento();
                    alimentoDto = (AlimentoDto) respuesta.getResultado("Alimento");
                    if (usuarioDto.getIdioma().equals("E")) {
                        new Mensaje().showModal(Alert.AlertType.INFORMATION, "Guardar Alimento", getStage(), "Alimento actualizado correctamente.");
                    } else if (usuarioDto.getIdioma().equals("I")) {
                        new Mensaje().showModal(Alert.AlertType.INFORMATION, "Save food", getStage(), "Food updated successfully.");
                    }

                    nuevoAlimento();

                }
            }
        } catch (Exception ex) {
            if (usuarioDto.getIdioma().equals("E")) {

                Logger.getLogger(RegistroAlimentoViewController.class.getName()).log(Level.SEVERE, "Error guardando el Alimento.", ex);
                new Mensaje().showModal(Alert.AlertType.ERROR, "Guardar Alimento", getStage(), "Ocurrio un error guardando el Alimento.");
            } else if (usuarioDto.getIdioma().equals("I")) {
                Logger.getLogger(RegistroAlimentoViewController.class.getName()).log(Level.SEVERE, "Error saving Food.", ex);
                new Mensaje().showModal(Alert.AlertType.ERROR, "Save food", getStage(), "Error saving Food.");

            }

        }
    }

    private void bindAlimento(Boolean nuevo) {
        if (!nuevo) {
            txtId.textProperty().bind(alimentoDto.id);
        }
        txtNombre.textProperty().bindBidirectional(alimentoDto.nombre);
        txtPrecio.textProperty().bindBidirectional(alimentoDto.precio);
    }

    private void unbindAlimento() {
        txtId.textProperty().unbind();
        txtNombre.textProperty().unbindBidirectional(alimentoDto.nombre);
        txtPrecio.textProperty().unbindBidirectional(alimentoDto.precio);
    }

    private void nuevoAlimento() {
        unbindAlimento();
        alimentoDto = new AlimentoDto();
        bindAlimento(true);
        txtId.clear();
        txtId.requestFocus();
    }

    public void indicarRequeridos() {
        requeridos.clear();
        requeridos.addAll(Arrays.asList(txtNombre, txtPrecio));
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

        lblTitulo.setText(prop.getProperty("txtTituloRegistroAlimento"));
        txtId.setPromptText(prop.getProperty("txtIdRegistroAlimento"));
        txtNombre.setPromptText(prop.getProperty("txtNombreRegistroAlimento"));
        txtPrecio.setPromptText(prop.getProperty("txtPrecioRegistroAlimento"));
        lblTipoDeAlimento.setText(prop.getProperty("txtTipoAlimentoRegistroAlimento"));
        chkComida.setText(prop.getProperty("chkComidaRegistroAlimento"));
        chkBebida.setText(prop.getProperty("chkBebidaRegistroAlimento"));

        btnNuevo.setText(prop.getProperty("btnNuevo"));
        btnBuscar.setText(prop.getProperty("btnBuscar"));
        btnEliminar.setText(prop.getProperty("btnEliminar"));
        btnGuardar.setText(prop.getProperty("btnGuardar"));

    }

}
