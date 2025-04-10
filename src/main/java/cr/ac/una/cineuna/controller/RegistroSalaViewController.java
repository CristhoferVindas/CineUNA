package cr.ac.una.cineuna.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import cr.ac.una.cineuna.idioma.Idioma;
import cr.ac.una.cineuna.model.SalaDto;
import cr.ac.una.cineuna.model.UsuarioDto;
import cr.ac.una.cineuna.service.SalaService;
import cr.ac.una.cineuna.util.AppContext;
import cr.ac.una.cineuna.util.FlowController;
import cr.ac.una.cineuna.util.Formato;
import cr.ac.una.cineuna.util.Mensaje;
import cr.ac.una.cineuna.util.Respuesta;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Cristhofer
 */
public class RegistroSalaViewController extends Controller implements Initializable {

    @FXML
    private JFXTextField txtNombre;
    @FXML
    private JFXTextField txtDetalle;
    @FXML
    private JFXTextField txtId;
    @FXML
    private JFXCheckBox chkActiva;
    @FXML
    private JFXCheckBox chkInactiva;

    SalaDto salaDto;
    List<Node> requeridos = new ArrayList<>();
    @FXML
    private ImageView imgAsiento;
    @FXML
    private ImageView imgFondo;
    @FXML
    private Label lblTitulo;
    @FXML
    private JFXButton btnNuevo;
    @FXML
    private JFXButton btnBuscar;
    @FXML
    private JFXButton btnEliminar;
    @FXML
    private JFXButton btnGuardar;
    @FXML
    private JFXButton btnDistribucionAsientos;
    @FXML
    private JFXButton btnBuscarAsiento;
    @FXML
    private JFXButton btnBuscarFondo;
    UsuarioDto usuarioDto;
    @FXML
    private Text lblFondo;
    @FXML
    private Text lblAsiento;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtId.setTextFormatter(Formato.getInstance().integerFormat());
        txtNombre.setTextFormatter(Formato.getInstance().cedulaFormat(30));
        txtDetalle.setTextFormatter(Formato.getInstance().maxLengthFormat(30));

        salaDto = new SalaDto();
        nuevoSala();
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

    private void bindSala(Boolean nuevo) {
        if (!nuevo) {
            txtId.textProperty().bind(salaDto.id);
        }
        txtNombre.textProperty().bindBidirectional(salaDto.nombre);
        txtDetalle.textProperty().bindBidirectional(salaDto.detalle);

    }

    private void unbindSala() {
        txtId.textProperty().unbind();
        txtNombre.textProperty().unbindBidirectional(salaDto.nombre);
        txtDetalle.textProperty().unbindBidirectional(salaDto.detalle);

    }

    @FXML
    private void OnActionBtnBuscarImagenAsiento(ActionEvent event) {
        if (usuarioDto.getIdioma().equals("E")) {
            buscarImagenes("Buscar Imagen de Asiento", "Asiento");
        } else {
            buscarImagenes("Search Seat Image", "Asiento");
        }

    }

    @FXML
    private void OnActionBtnBuscarImagenFondo(ActionEvent event) {
        if (usuarioDto.getIdioma().equals("E")) {
            buscarImagenes("Buscar Imagen de Fondo", "Fondo");
        } else {
            buscarImagenes("Search Background Image", "Fondo");
        }
    }

    @FXML
    private void OnActionBtnNuevo(ActionEvent event) {
        if (usuarioDto.getIdioma().equals("E")) {
            if (new Mensaje().showConfirmation("Sala", getStage(), "¿Limpiar el registro?")) {
                nuevoSala();
            }
        } else if (usuarioDto.getIdioma().equals("I")) {
            if (new Mensaje().showConfirmation("Room", getStage(), "clean the registry?")) {
                nuevoSala();
            }
        }

    }

    @FXML
    private void OnActionBtnBuscar(ActionEvent event) throws IOException {
        if (txtId.getText() != null && !txtId.getText().isEmpty()) {
            SalaService service = new SalaService();
            Respuesta respuesta = service.getSala(Long.valueOf(txtId.getText()));

            if (respuesta.getEstado()) {
                unbindSala();
                salaDto = (SalaDto) respuesta.getResultado("Sala");

                byte[] asientoB6 = Base64.getDecoder().decode(salaDto.getImagenAsiento());
                Image imagenAsiento = new Image(new ByteArrayInputStream(asientoB6));
                imgAsiento.setImage(imagenAsiento);

                byte[] fondoB64 = Base64.getDecoder().decode(salaDto.getImagenFondo());
                Image imagenFondo = new Image(new ByteArrayInputStream(fondoB64));
                imgFondo.setImage(imagenFondo);

                if (salaDto.getEstado().equals("A")) {
                    chkActiva.setSelected(true);
                    chkInactiva.setSelected(false);
                } else {
                    chkActiva.setSelected(false);
                    chkInactiva.setSelected(true);
                }

                bindSala(false);
                validarRequeridos();
            } else {
                if (usuarioDto.getIdioma().equals("E")) {
                    new Mensaje().showModal(Alert.AlertType.ERROR, "Cargar sala", getStage(), respuesta.getMensaje());
                } else if (usuarioDto.getIdioma().equals("I")) {
                    new Mensaje().showModal(Alert.AlertType.ERROR, "Load room", getStage(), respuesta.getMensaje());
                }
            }
        } else {
            if (usuarioDto.getIdioma().equals("E")) {
                new Mensaje().showModal(Alert.AlertType.INFORMATION, "Buscar Sala", getStage(), "Por favor digite un Id para buscar.");
            } else if (usuarioDto.getIdioma().equals("I")) {
                new Mensaje().showModal(Alert.AlertType.INFORMATION, "Search room", getStage(), "Please enter an ID to search.");
            }
        }
    }

    void buscarImagenes(String titulo, String tipo) {
        File imagenCargada;
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(titulo);

            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("jpg", "*.jpg"),
                    new FileChooser.ExtensionFilter("png", "*.png")
            );
            File cancela = imagenCargada = fileChooser.showOpenDialog(new Stage());

            if (cancela == null) {
                if (usuarioDto.getIdioma().equals("E")) {
                    new Mensaje().showModal(Alert.AlertType.ERROR, "", stage, "No eligio imagen");
                } else if (usuarioDto.getIdioma().equals("I")) {
                    new Mensaje().showModal(Alert.AlertType.ERROR, "", stage, "You did not choose the image");
                }
            } else {
                InputStream inputStream = new FileInputStream(imagenCargada.getAbsolutePath());
                byte[] bytesImagen = inputStream.readAllBytes();
                String encodedString = Base64.getEncoder().encodeToString(bytesImagen);
                Image i = new Image(new ByteArrayInputStream(bytesImagen));
                if (tipo.equals("Asiento")) {
                    salaDto.setImagenAsiento(encodedString);
                    imgAsiento.setImage(i);
                } else {
                    salaDto.setImagenFondo(encodedString);
                    imgFondo.setImage(i);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void OnActionBtnEliminar(ActionEvent event) {
        try {
            if (salaDto.getId() == null) {
                if (usuarioDto.getIdioma().equals("E")) {
                    new Mensaje().showModal(Alert.AlertType.ERROR, "Eliminar sala", getStage(), "Debe cargar el sala que desea eliminar.");

                } else if (usuarioDto.getIdioma().equals("I")) {
                    new Mensaje().showModal(Alert.AlertType.ERROR, "Delete room", getStage(), "You must load the room you want to delete.");
                }
            } else {

                SalaService service = new SalaService();
                Respuesta respuesta = service.eliminarSala(salaDto.getId());
                if (!respuesta.getEstado()) {
                    if (usuarioDto.getIdioma().equals("E")) {
                        new Mensaje().showModal(Alert.AlertType.ERROR, "Eliminar sala", getStage(), respuesta.getMensaje());

                    } else if (usuarioDto.getIdioma().equals("I")) {
                        new Mensaje().showModal(Alert.AlertType.ERROR, "Delete room", getStage(), respuesta.getMensaje());

                    }
                } else {
                    if (usuarioDto.getIdioma().equals("E")) {
                        new Mensaje().showModal(Alert.AlertType.INFORMATION, "Eliminar sala", getStage(), "Sala eliminado correctamente.");
                        nuevoSala();
                    } else if (usuarioDto.getIdioma().equals("I")) {
                        new Mensaje().showModal(Alert.AlertType.INFORMATION, "Delete room", getStage(), "Room successfully removed.");
                        nuevoSala();
                    }
                }
            }
        } catch (Exception ex) {
            if (usuarioDto.getIdioma().equals("E")) {
                Logger.getLogger(RegistroSalaViewController.class.getName()).log(Level.SEVERE, "Error eliminando la sala.", ex);
                new Mensaje().showModal(Alert.AlertType.ERROR, "Eliminar sala", getStage(), "Ocurrio un error eliminando la sala.");

            } else if (usuarioDto.getIdioma().equals("I")) {
                Logger.getLogger(RegistroSalaViewController.class.getName()).log(Level.SEVERE, "Error deleting the room.", ex);
                new Mensaje().showModal(Alert.AlertType.ERROR, "Delete room", getStage(), "An error occurred deleting the room.");
            }
        }
    }

    @FXML
    private void OnActionBtnGuardar(ActionEvent event) {
        try {
            String invalidos = validarRequeridos();
            if (!invalidos.isEmpty()) {
                if (usuarioDto.getIdioma().equals("E")) {
                    new Mensaje().showModal(Alert.AlertType.ERROR, "Guardar sala", getStage(), invalidos);
                } else if (usuarioDto.getIdioma().equals("I")) {
                    new Mensaje().showModal(Alert.AlertType.ERROR, "Save room", getStage(), invalidos);
                }
            } else {
                SalaService service = new SalaService();

                Respuesta respuesta = service.guardarSala(salaDto);
                if (!respuesta.getEstado()) {
                    if (usuarioDto.getIdioma().equals("E")) {
                        new Mensaje().showModal(Alert.AlertType.ERROR, "Guardar sala", getStage(), respuesta.getMensaje());

                    } else if (usuarioDto.getIdioma().equals("I")) {
                        new Mensaje().showModal(Alert.AlertType.ERROR, "Save room", getStage(), respuesta.getMensaje());
                    }
                } else {
                    unbindSala();
                    salaDto = (SalaDto) respuesta.getResultado("Sala");
                    //bindSala(false);
                    if (usuarioDto.getIdioma().equals("E")) {
                        new Mensaje().showModal(Alert.AlertType.INFORMATION, "Guardar sala", getStage(), "Sala actualizada correctamente.");

                    } else if (usuarioDto.getIdioma().equals("I")) {
                        new Mensaje().showModal(Alert.AlertType.INFORMATION, "Save room", getStage(), "Room updated successfully.");

                    }

                    nuevoSala();
                }
            }
        } catch (Exception ex) {
            if (usuarioDto.getIdioma().equals("E")) {
                Logger.getLogger(RegistroSalaViewController.class.getName()).log(Level.SEVERE, "Error guardando la sala.", ex);
                new Mensaje().showModal(Alert.AlertType.ERROR, "Guardar sala", getStage(), "Ocurrio un error guardando la sala.");

            } else if (usuarioDto.getIdioma().equals("I")) {
                Logger.getLogger(RegistroSalaViewController.class.getName()).log(Level.SEVERE, "Error saving the room.", ex);
                new Mensaje().showModal(Alert.AlertType.ERROR, "Save room", getStage(), "An error occurred saving the room.");
            }
        }
    }

    @FXML
    private void seleccionActiva(ActionEvent event) {
        chkInactiva.setSelected(false);
        salaDto.setEstado("A");
    }

    @FXML
    private void seleccionInactiva(ActionEvent event) {
        chkActiva.setSelected(false);
        salaDto.setEstado("I");
    }

    @FXML
    private void OnActionBtnDistribucionAsientos(ActionEvent event) {
        if (salaDto.getId() != null) {
            AppContext.getInstance().set("SalaSeleccionada", salaDto);
            DistribucionAsientosViewController asientosViewController = (DistribucionAsientosViewController) FlowController.getInstance().getController("DistribucionAsientosView");
            asientosViewController.CrearEspacios();
            FlowController.getInstance().goViewInWindowModal("DistribucionAsientosView", getStage(), true);
            nuevoSala();
        } else {
            if (usuarioDto.getIdioma().equals("E")) {
                new Mensaje().showModal(Alert.AlertType.ERROR, "Distribuir Asientos", getStage(), "Por favor cargue una sala.");

            } else {
                new Mensaje().showModal(Alert.AlertType.ERROR, "Seat Distribution", getStage(), "Please select a room.");
            }

        }
    }

    private void nuevoSala() {
        unbindSala();
        salaDto = new SalaDto();
        bindSala(true);
        txtId.clear();
        txtId.requestFocus();
        imgAsiento.setImage(null);
        imgFondo.setImage(null);
    }

    public void indicarRequeridos() {
        requeridos.clear();
        requeridos.addAll(Arrays.asList(txtNombre, imgAsiento, imgFondo, txtDetalle, imgAsiento));
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
            } else if (node instanceof ImageView && ((ImageView) node).getImage() == null) {
                if (validos) {
                    invalidos += ((ImageView) node).getId();
                } else {
                    invalidos += "," + ((ImageView) node).getId();
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

        lblTitulo.setText(prop.getProperty("txtTituloRegistroSala"));

        txtId.setPromptText(prop.getProperty("txtIdRegistroSala"));
        txtNombre.setPromptText(prop.getProperty("txtNombreRegistroSala"));

        chkActiva.setText(prop.getProperty("chkActivaRegistroSala"));
        chkInactiva.setText(prop.getProperty("chkInactivaRegistroSala"));

        lblAsiento.setText(prop.getProperty("txtAsientoRegistroSala"));
        lblFondo.setText(prop.getProperty("txtFondoRegistroSala"));
        txtDetalle.setPromptText(prop.getProperty("txtDetalleRegistroSala"));

        btnNuevo.setText(prop.getProperty("btnNuevo"));
        btnBuscar.setText(prop.getProperty("btnBuscar"));
        btnEliminar.setText(prop.getProperty("btnEliminar"));
        btnGuardar.setText(prop.getProperty("btnGuardar"));

        btnDistribucionAsientos.setText(prop.getProperty("btnDistribucionAsientosRegistroSala"));

        btnBuscarAsiento.setText(prop.getProperty("btnBuscarAsientoRegistroSala"));
        btnBuscarFondo.setText(prop.getProperty("btnBuscarFondoRegistroSala"));

    }
}
