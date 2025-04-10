package cr.ac.una.cineuna.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
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
import cr.ac.una.cineuna.util.Formato;
import cr.ac.una.cineuna.util.Mensaje;
import cr.ac.una.cineuna.util.Respuesta;
import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
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
public class RegistroTandaViewController extends Controller implements Initializable {

    @FXML
    private JFXTextField txtId;
    @FXML
    private JFXTextField txtCostoEntrada;
    @FXML
    private JFXTimePicker txtHoraInicio;
    @FXML
    private JFXTimePicker txtHoraFin;
    @FXML
    private JFXDatePicker dpFechaInicio;
    @FXML
    private JFXCheckBox chkIngles;
    @FXML
    private JFXCheckBox chkEspannol;
    @FXML
    private JFXTextField txtPelicula;
    @FXML
    private JFXTextField txtSala;

    TandaDto tandaDto;
    PeliculaDto peliculaDto;
    SalaDto salaDto;
    List<Node> requeridos = new ArrayList<>();
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
    private JFXButton btnBuscarPelicula;
    @FXML
    private JFXButton btnBuscarSala;
    UsuarioDto usuarioDto;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtId.setTextFormatter(Formato.getInstance().integerFormat());
        txtCostoEntrada.setTextFormatter(Formato.getInstance().integerFormat());

        peliculaDto = new PeliculaDto();
        salaDto = new SalaDto();
        tandaDto = new TandaDto();
        nuevaTanda();
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
    private void seleccionIngles(ActionEvent event) {
        chkEspannol.setSelected(false);

        if ((chkEspannol.isSelected() == false) && (chkIngles.isSelected() == false)) {
            chkEspannol.setSelected(true);
        }
    }

    @FXML
    private void seleccionEspannol(ActionEvent event) {
        chkIngles.setSelected(false);
        if ((chkEspannol.isSelected() == false) && (chkIngles.isSelected() == false)) {
            chkEspannol.setSelected(true);
        }
    }

    @FXML
    private void OnActionBtnNuevo(ActionEvent event) {
        if (usuarioDto.getIdioma().equals("E")) {
            if (new Mensaje().showConfirmation("Tanda", getStage(), "clean the registry?")) {
                nuevaTanda();
            } else if (usuarioDto.getIdioma().equals("I")) {
                if (new Mensaje().showConfirmation("Batch", getStage(), "clean the registry?")) {
                    nuevaTanda();
                }
            }}
    }

    @FXML
    private void OnActionBtnBuscar(ActionEvent event) {
        if (txtId.getText() != null && !txtId.getText().isEmpty()) {
            TandaService service = new TandaService();
            Respuesta respuesta = service.getTanda(Long.valueOf(txtId.getText()));

            if (respuesta.getEstado()) {
                unbindTanda();
                tandaDto = (TandaDto) respuesta.getResultado("Tanda");
                bindTanda(false);
                txtHoraInicio.setValue(LocalTime.parse(tandaDto.getHoraInicio()));
                txtHoraFin.setValue(LocalTime.parse(tandaDto.getHoraFin()));
                cargarPelicula(tandaDto.getIDPelicula());
                cargarSala(tandaDto.getIDSala());

                validarRequeridos();
            } else {
                if(usuarioDto.getIdioma().equals("E")){
                    new Mensaje().showModal(Alert.AlertType.ERROR, "Cargar Tanda", getStage(), respuesta.getMensaje());

                } else if (usuarioDto.getIdioma().equals("I")) {
                    new Mensaje().showModal(Alert.AlertType.ERROR, "Batch", getStage(), respuesta.getMensaje());

                }

            }
        } else {
            if (usuarioDto.getIdioma().equals("E")) {
                new Mensaje().showModal(Alert.AlertType.INFORMATION, "Buscar Tanda", getStage(), "Por favor digite un Id para buscar.");

            } else if (usuarioDto.getIdioma().equals("I")) {
                new Mensaje().showModal(Alert.AlertType.INFORMATION, "Search batch", getStage(), "Please enter an ID to search the batch");

            }


        }
    }

    @FXML
    private void OnActionBtnEliminar(ActionEvent event) {
        try {
            if (tandaDto.getId() == null) {
               if(usuarioDto.getIdioma().equals("E")){
                       new Mensaje().showModal(Alert.AlertType.ERROR, "Eliminar tanda", getStage(), "Debe cargar la tanda que desea eliminar.");
                    }else if(usuarioDto.getIdioma().equals("I")){
                        new Mensaje().showModal(Alert.AlertType.ERROR, "delete batch", getStage(), "You need to upload the batch you want to delete.");
                        
                    }
            } else {

                TandaService service = new TandaService();
                Respuesta respuesta = service.eliminarTanda(tandaDto.getId());
                if (!respuesta.getEstado()) {
                    
                    if(usuarioDto.getIdioma().equals("E")){
                       new Mensaje().showModal(Alert.AlertType.ERROR, "Eliminar Tanda", getStage(), respuesta.getMensaje());
                    }else if(usuarioDto.getIdioma().equals("I")){
                        new Mensaje().showModal(Alert.AlertType.ERROR, "Delete batch", getStage(), respuesta.getMensaje());
                        
                    }
            
                    
                } else {
                    if(usuarioDto.getIdioma().equals("E")){
                       new Mensaje().showModal(Alert.AlertType.INFORMATION, "Eliminar Tanda", getStage(), "Tanda eliminado correctamente.");
                    nuevaTanda();
                    }else if(usuarioDto.getIdioma().equals("I")){
                        new Mensaje().showModal(Alert.AlertType.INFORMATION, "Delete batch", getStage(), "Batch removed correctly.");
                    nuevaTanda();
                    }
            
                      }
            }
        } catch (Exception ex) {
            if (usuarioDto.getIdioma().equals("E")) {
                Logger.getLogger(RegistroTandaViewController.class.getName()).log(Level.SEVERE, "Error eliminando la Tanda.", ex);
                new Mensaje().showModal(Alert.AlertType.ERROR, "Eliminar Tanda", getStage(), "Ocurrio un error eliminando la Tanda.");

            } else if (usuarioDto.getIdioma().equals("I")) {
                Logger.getLogger(RegistroTandaViewController.class.getName()).log(Level.SEVERE, "Error delete batch.", ex);
                new Mensaje().showModal(Alert.AlertType.ERROR, "Delete batch", getStage(), "An error occurred deleting the batch");

            }


        }
    }

    @FXML
    private void OnActionBtnGuardar(ActionEvent event) {
        try {
            String invalidos = validarRequeridos();
            if (!invalidos.isEmpty()) {
                if(usuarioDto.getIdioma().equals("E")){
                       new Mensaje().showModal(Alert.AlertType.ERROR, "Guardar tanda", getStage(), invalidos);
                    }else if(usuarioDto.getIdioma().equals("I")){
                        new Mensaje().showModal(Alert.AlertType.ERROR, "Save batch", getStage(), invalidos);
                        
                    }
  
            } else {
                TandaService service = new TandaService();

                tandaDto.setHoraFin(String.valueOf(txtHoraFin.getValue()));
                tandaDto.setHoraInicio(txtHoraFin.getValue().toString());

                Respuesta respuesta = service.guardarTanda(tandaDto);
                if (!respuesta.getEstado()) {
                    if(usuarioDto.getIdioma().equals("E")){
                       new Mensaje().showModal(Alert.AlertType.ERROR, "Guardar tanda", getStage(), respuesta.getMensaje());
                    }else if(usuarioDto.getIdioma().equals("I")){
                        new Mensaje().showModal(Alert.AlertType.ERROR, "Save batch", getStage(), respuesta.getMensaje());
                        
                    }
            
                    
                    
                } else {
                    unbindTanda();
                    tandaDto = (TandaDto) respuesta.getResultado("Tanda");
                    salaDto.getTandas().add(tandaDto);
                    SalaService salaService = new SalaService();
                    salaService.guardarSala(salaDto);

                    peliculaDto.getTandas().add(tandaDto);
                    PeliculaService peliculaService = new PeliculaService();
                    peliculaService.guardarPelicula(peliculaDto);
                    if(usuarioDto.getIdioma().equals("E")){
                       new Mensaje().showModal(Alert.AlertType.INFORMATION, "Guardar tanda", getStage(), "Tanda actualizada correctamente.");
                    nuevaTanda();
                    }else if(usuarioDto.getIdioma().equals("I")){
                        new Mensaje().showModal(Alert.AlertType.INFORMATION, "Save batch", getStage(), "Batch updated successfully");
                    nuevaTanda();
                    }
            
                    
                }
            }
        } catch (Exception ex) {
            if(usuarioDto.getIdioma().equals("E")){
                       Logger.getLogger(RegistroTandaViewController.class.getName()).log(Level.SEVERE, "Error guardando la Tanda.", ex);
            new Mensaje().showModal(Alert.AlertType.ERROR, "Guardar Tanda", getStage(), "Ocurrio un error guardando la Tanda.");
                    }else if(usuarioDto.getIdioma().equals("I")){
                        Logger.getLogger(RegistroTandaViewController.class.getName()).log(Level.SEVERE, "Error saving batch.", ex);
            new Mensaje().showModal(Alert.AlertType.ERROR, "Saving batch", getStage(), "Error saving batch.");
                        
                    }
            
            
        }
    }

    private void bindTanda(Boolean nuevo) {
        if (!nuevo) {
            txtId.textProperty().bind(tandaDto.id);
        }
        txtCostoEntrada.textProperty().bindBidirectional(tandaDto.costoEntrada);
        dpFechaInicio.valueProperty().bindBidirectional(tandaDto.fecha);
    }

    private void unbindTanda() {
        txtId.textProperty().unbind();
        txtCostoEntrada.textProperty().unbindBidirectional(tandaDto.costoEntrada);
        dpFechaInicio.valueProperty().unbindBidirectional(tandaDto.fecha);
    }

    private void nuevaTanda() {
        unbindTanda();
        tandaDto = new TandaDto();
        bindTanda(true);
        peliculaDto = new PeliculaDto();
        salaDto = new SalaDto();
        txtHoraInicio.setValue(null);
        txtHoraFin.setValue(null);
        txtId.clear();
        txtId.requestFocus();

        txtPelicula.clear();
        txtSala.clear();
    }

    public void indicarRequeridos() {
        requeridos.clear();
        requeridos.addAll(Arrays.asList(txtCostoEntrada, txtHoraFin, txtHoraInicio, txtPelicula, txtSala, dpFechaInicio));
    }

    public String validarRequeridos() {
        Boolean validos = true;
        String invalidos = "";
        for (Node node : requeridos) {
            if (node instanceof JFXTextField && (((JFXTextField) node).getText() == null || ((JFXTextField) node).getText().equals(""))) {
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
                    invalidos += ((JFXDatePicker) node).getPromptText();
                } else {
                    invalidos += "," + ((JFXDatePicker) node).getPromptText();
                }
                validos = false;
            } else if (node instanceof JFXComboBox && ((JFXComboBox) node).getSelectionModel().getSelectedIndex() < 0) {
                if (validos) {
                    invalidos += ((JFXComboBox) node).getPromptText();
                } else {
                    invalidos += "," + ((JFXComboBox) node).getPromptText();
                }
                validos = false;
            } else if (node instanceof JFXTimePicker && ((JFXTimePicker) node).getValue() == null) {
                if (validos) {
                    invalidos += ((JFXTimePicker) node).getPromptText();
                } else {
                    invalidos += "," + ((JFXTimePicker) node).getPromptText();
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

    @FXML
    private void OnActionBtnBuscarPelicula(ActionEvent event) {
        
            BusquedaViewController busquedaViewController = (BusquedaViewController) FlowController.getInstance().getController("BusquedaView");
            busquedaViewController.busquedaPeliculas();
            FlowController.getInstance().goViewInWindowModal("BusquedaView", getStage(), true);
            PeliculaDto peliculaDto = (PeliculaDto) busquedaViewController.getResultado();
            if (peliculaDto != null) {
                cargarPelicula(peliculaDto.getId());
            }
        
    }

    private void cargarPelicula(Long id) {
        PeliculaService service = new PeliculaService();
        Respuesta respuesta = service.getPelicula(id);

        if (respuesta.getEstado()) {
            peliculaDto = (PeliculaDto) respuesta.getResultado("Pelicula");

            if (chkEspannol.isSelected()) {
                txtPelicula.setText(peliculaDto.getNombreEspannol());
            } else if (chkIngles.isSelected()) {
                txtPelicula.setText(peliculaDto.getNombreIngles());
            }
        } else {
            if(usuarioDto.getIdioma().equals("E")){
                        new Mensaje().showModal(Alert.AlertType.ERROR, "Cargar tanda", getStage(), respuesta.getMensaje());
                    }else if(usuarioDto.getIdioma().equals("I")){
                         new Mensaje().showModal(Alert.AlertType.ERROR, "Load batch", getStage(), respuesta.getMensaje());
                        
                    }
            
           
        }
    }

    @FXML
    private void OnActionBtnBuscarSala(ActionEvent event) {
        AppContext.getInstance().set("SalaSeleccionada", salaDto);
        BusquedaViewController busquedaViewController = (BusquedaViewController) FlowController.getInstance().getController("BusquedaView");
        busquedaViewController.busquedaSalas();
        FlowController.getInstance().goViewInWindowModal("BusquedaView", getStage(), true);
        SalaDto salaDto = (SalaDto) busquedaViewController.getResultado();
        if (salaDto != null) {
            cargarSala(salaDto.getId());
        }
    }

    private void cargarSala(Long id) {
        SalaService service = new SalaService();
        Respuesta respuesta = service.getSala(id);

        if (respuesta.getEstado()) {
            salaDto = (SalaDto) respuesta.getResultado("Sala");
            txtSala.setText(salaDto.getNombre());
        } else {
            new Mensaje().showModal(Alert.AlertType.ERROR, "Cargar sala", getStage(), respuesta.getMensaje());
        }
    }
    
        
        private void cambiarIdioma(String pIdioma) throws IOException {
        Idioma idiom = new Idioma(pIdioma);
        Properties prop = idiom.getProperties(pIdioma);
        
        lblTitulo.setText(prop.getProperty("txtTituloRegistroTanda"));
        txtId.setPromptText(prop.getProperty("txtIdRegistroTanda"));
        txtCostoEntrada.setPromptText(prop.getProperty("txtCostoEntradaRegistroTanda"));
        txtHoraInicio.setPromptText(prop.getProperty("txtHoraInicioRegistroTanda"));
        txtHoraFin.setPromptText(prop.getProperty("txtHoraFinRegistroTanda"));
        dpFechaInicio.setPromptText(prop.getProperty("dpFechaInicioRegistroTanda"));
        chkIngles.setText(prop.getProperty("chkInglesRegistroTanda"));
        chkEspannol.setText(prop.getProperty("chkEspannolRegistroTanda"));
        txtPelicula.setPromptText(prop.getProperty("txtPeliculaRegistroTanda"));
        txtSala.setPromptText(prop.getProperty("txtSalaRegistroTanda"));

        btnNuevo.setText(prop.getProperty("btnNuevo"));
        btnBuscar.setText(prop.getProperty("btnBuscar"));
        btnEliminar.setText(prop.getProperty("btnEliminar"));
        btnGuardar.setText(prop.getProperty("btnGuardar"));
        
        btnBuscarPelicula.setText(prop.getProperty("btnBuscarPeliculaRegistroTanda"));
        btnBuscarSala.setText(prop.getProperty("btnBuscarSalaRegistroTanda"));
        
    }

}
