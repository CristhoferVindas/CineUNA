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
import cr.ac.una.cineuna.model.UsuarioDto;
import cr.ac.una.cineuna.service.PeliculaService;
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
public class RegistroPeliculaViewController extends Controller implements Initializable {

    @FXML
    private JFXTextField txtNombreEspannol;
    @FXML
    private JFXTextField txtNombreIngles;
    @FXML
    private JFXTextField txtTrailerEspannol;
    @FXML
    private JFXTextField txtTrailerIngles;
    @FXML
    private JFXDatePicker dpFechaEstreno;
    @FXML
    private JFXTextField txtResennaEspannol;
    @FXML
    private JFXTextField txtResennaIngles;
    @FXML
    private JFXTextField txtId;
    @FXML
    private JFXCheckBox chkEspannol;
    @FXML
    private JFXCheckBox chkIngles;
    @FXML
    private JFXCheckBox chkAmbos;
    @FXML
    private JFXCheckBox chkDisponiblePronto;
    @FXML
    private JFXCheckBox chkEnCartelera;
    @FXML
    private JFXCheckBox chkInactiva;

    PeliculaDto peliculaDto;
    List<Node> requeridos = new ArrayList<>();
    @FXML
    private ImageView imgPortadaEspannol;
    @FXML
    private ImageView imgPortadaIngles;
    @FXML
    private JFXDatePicker dtpFechaFin;
    @FXML
    private Label lblTitulo;
    @FXML
    private JFXButton btnBuscar;
    @FXML
    private JFXButton btnNuevo;
    @FXML
    private JFXButton btnEliminar;
    @FXML
    private JFXButton btnGuardar;
    @FXML
    private Text txtPortadaEspannolRegistroPelicula;
    @FXML
    private Text txtPortadaInglesRegistroPelicula;
    @FXML
    private JFXButton btnBuscarTrailerEspanol;
    @FXML
    private JFXButton btnBuscarTrailerIngles;
    @FXML
    private JFXButton btnBuscarPortadaEspanol;
    @FXML
    private JFXButton btnBuscarPortadaIngles;
    UsuarioDto usuarioDto;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtId.setTextFormatter(Formato.getInstance().integerFormat());
        txtNombreEspannol.setTextFormatter(Formato.getInstance().letrasFormat(30));
        txtResennaEspannol.setTextFormatter(Formato.getInstance().letrasFormat(15));

        txtNombreIngles.setTextFormatter(Formato.getInstance().letrasFormat(30));
        txtResennaIngles.setTextFormatter(Formato.getInstance().maxLengthFormat(8));

        peliculaDto = new PeliculaDto();
        nuevaPelicula();
        indicarRequeridos();
        
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
    }

    private void bindPelicula(Boolean nuevo) {
        if (!nuevo) {
            txtId.textProperty().bind(peliculaDto.id);
        }
        txtNombreEspannol.textProperty().bindBidirectional(peliculaDto.nombreEspannol);
        txtTrailerEspannol.textProperty().bindBidirectional(peliculaDto.trailerEspannol);
        txtResennaEspannol.textProperty().bindBidirectional(peliculaDto.resennaEspannol);

        txtNombreIngles.textProperty().bindBidirectional(peliculaDto.nombreIngles);
        txtTrailerIngles.textProperty().bindBidirectional(peliculaDto.trailerIngles);
        txtResennaIngles.textProperty().bindBidirectional(peliculaDto.resennaIngles);
        dpFechaEstreno.valueProperty().bindBidirectional(peliculaDto.fechaEstreno);
        dtpFechaFin.valueProperty().bindBidirectional(peliculaDto.fechaFin);

    }

    private void unbindPelicula() {
        txtId.textProperty().unbind();
        txtNombreEspannol.textProperty().unbindBidirectional(peliculaDto.nombreEspannol);
        txtTrailerEspannol.textProperty().unbindBidirectional(peliculaDto.trailerEspannol);
        txtResennaEspannol.textProperty().unbindBidirectional(peliculaDto.resennaEspannol);

        txtNombreIngles.textProperty().unbindBidirectional(peliculaDto.nombreIngles);
        txtTrailerIngles.textProperty().unbindBidirectional(peliculaDto.trailerIngles);
        txtResennaIngles.textProperty().unbindBidirectional(peliculaDto.resennaIngles);

        dpFechaEstreno.valueProperty().unbindBidirectional(peliculaDto.fechaEstreno);
        dtpFechaFin.valueProperty().bindBidirectional(peliculaDto.fechaFin);
    }

    @Override
    public void initialize() {

    }

    @FXML
    private void OnActionBtnBuscarTrailerEspannol(ActionEvent event) {
        RegistroYouTubeViewController registroYouTubeViewController = (RegistroYouTubeViewController) FlowController.getInstance().getController("RegistroYouTubeView");
        registroYouTubeViewController.initialize();
        FlowController.getInstance().goViewInWindowModal("RegistroYouTubeView", getStage(), true);
        String trailer = registroYouTubeViewController.getResultado();
        if (trailer != null) {
            txtTrailerEspannol.setText(trailer);
        }
    }

    @FXML
    private void OnActionBtnBuscarTrailerIngles(ActionEvent event) {
        RegistroYouTubeViewController registroYouTubeViewController = (RegistroYouTubeViewController) FlowController.getInstance().getController("RegistroYouTubeView");
        registroYouTubeViewController.initialize();
        FlowController.getInstance().goViewInWindowModal("RegistroYouTubeView", getStage(), true);
        String trailer = registroYouTubeViewController.getResultado();
        if (trailer != null) {
            txtTrailerIngles.setText(trailer);
        }
    }

    @FXML
    private void OnActionBtnBuscarPortadaEspannol(ActionEvent event) {
        buscarImagenes("Buscar Portada en Español", "Español");
    }

    @FXML
    private void OnActionBtnBuscarPortadaIngles(ActionEvent event) {
        buscarImagenes("Buscar Portada en Inglés", "Ingles");
    }
    void buscarImagenes(String titulo, String Idioma) {
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
                new Mensaje().showModal(Alert.AlertType.ERROR, "", stage, "No eligio Imagen");
            } else {
                InputStream inputStream = new FileInputStream(imagenCargada.getAbsolutePath());
                byte[] bytesImagen = inputStream.readAllBytes();
                String encodedString = Base64.getEncoder().encodeToString(bytesImagen);
                Image i = new Image(new ByteArrayInputStream(bytesImagen));
                if (Idioma.equals("Español")) {
                    peliculaDto.setPortadaEspannol(encodedString);
                    imgPortadaEspannol.setImage(i);
                } else {
                    peliculaDto.setPortadaIngles(encodedString);
                    imgPortadaIngles.setImage(i);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void seleccionEspannol(ActionEvent event) {
        chkAmbos.setSelected(false);
        chkIngles.setSelected(false);
        peliculaDto.setIdiomasDisponibles("E");

    }

    @FXML
    private void seleccionIngles(ActionEvent event) {
        chkAmbos.setSelected(false);
        chkEspannol.setSelected(false);
        peliculaDto.setIdiomasDisponibles("I");
    }

    @FXML
    private void seleccionAmbos(ActionEvent event) {
        chkEspannol.setSelected(false);
        chkIngles.setSelected(false);
        peliculaDto.setIdiomasDisponibles("A");
    }

    @FXML
    private void seleccionDisponiblePronto(ActionEvent event) {
        chkEnCartelera.setSelected(false);
        chkInactiva.setSelected(false);
        peliculaDto.setEstado("D");
    }

    @FXML
    private void seleccionEnCartelera(ActionEvent event) {
        chkDisponiblePronto.setSelected(false);
        chkInactiva.setSelected(false);
        peliculaDto.setEstado("C");
    }

    @FXML
    private void seleccionInactiva(ActionEvent event) {
        chkEnCartelera.setSelected(false);
        chkDisponiblePronto.setSelected(false);
        peliculaDto.setEstado("I");
    }

    @FXML
    private void OnActionBtnNuevo(ActionEvent event) {
        if (usuarioDto.getIdioma().equals("E")) {
            if (new Mensaje().showConfirmation("Pelicula", getStage(), "¿Limpiar el registro?")) {
                nuevaPelicula();
            }
        } else if (usuarioDto.getIdioma().equals("I")) {
            if (new Mensaje().showConfirmation("Movie", getStage(), "clean the registry?")) {
                nuevaPelicula();
            }
        }
    }


    @FXML
    private void OnActionBtnBuscar(ActionEvent event) {
        if (txtId.getText() != null && !txtId.getText().isEmpty()) {
            PeliculaService service = new PeliculaService();
            Respuesta respuesta = service.getPelicula(Long.valueOf(txtId.getText()));

            if (respuesta.getEstado()) {
                unbindPelicula();
                peliculaDto = (PeliculaDto) respuesta.getResultado("Pelicula");

                byte[] PotadaEspanolByte = Base64.getDecoder().decode(peliculaDto.getPortadaEspannol());
                Image imagePotadaEspañol = new Image(new ByteArrayInputStream(PotadaEspanolByte));
                imgPortadaEspannol.setImage(imagePotadaEspañol);

                byte[] PotadaInglesBytes = Base64.getDecoder().decode(peliculaDto.getPortadaIngles());
                Image imagePotadaIngles = new Image(new ByteArrayInputStream(PotadaInglesBytes));
                imgPortadaIngles.setImage(imagePotadaIngles);

                if (peliculaDto.getEstado().equals("C")) {
                    chkEnCartelera.setSelected(true);
                    chkDisponiblePronto.setSelected(false);
                    chkInactiva.setSelected(false);
                } else if (peliculaDto.getEstado().equals("D")) {
                    chkDisponiblePronto.setSelected(true);
                    chkEnCartelera.setSelected(false);
                    chkInactiva.setSelected(false);
                } else {
                    chkInactiva.setSelected(true);
                    chkEnCartelera.setSelected(false);
                    chkDisponiblePronto.setSelected(false);
                }

                if (peliculaDto.getIdiomasDisponibles().equals("E")) {
                    chkEspannol.setSelected(true);
                    chkIngles.setSelected(false);
                    chkAmbos.setSelected(false);
                } else if (peliculaDto.getIdiomasDisponibles().equals("I")) {
                    chkIngles.setSelected(true);
                    chkEspannol.setSelected(false);
                    chkAmbos.setSelected(false);
                } else {
                    chkAmbos.setSelected(true);
                    chkIngles.setSelected(false);
                    chkEspannol.setSelected(false);

                }

                bindPelicula(false);
                validarRequeridos();
            } else {
                if(usuarioDto.getIdioma().equals("E")){
                       new Mensaje().showModal(Alert.AlertType.ERROR, "Pelicula", getStage(), respuesta.getMensaje());
                    }else if(usuarioDto.getIdioma().equals("I")){
                        new Mensaje().showModal(Alert.AlertType.ERROR, "Movie", getStage(), respuesta.getMensaje());
                        
                    } }
        } else {
            if(usuarioDto.getIdioma().equals("E")){
                       new Mensaje().showModal(Alert.AlertType.INFORMATION, "buscar pelicula", getStage(), "Por favor digite un Id para buscar la pelicula.");
            
                    }else if(usuarioDto.getIdioma().equals("I")){
                        new Mensaje().showModal(Alert.AlertType.INFORMATION, "search movie", getStage(), "Please enter an ID to search the movie.");
                    } }
    }

    @FXML
    private void OnActionBtnEliminar(ActionEvent event
    ) {
        try {
            if (peliculaDto.getId() == null) {
                if(usuarioDto.getIdioma().equals("E")){
                       new Mensaje().showModal(Alert.AlertType.ERROR, "Eliminar pelicula", getStage(), "Debe cargar ls pelicula que desea eliminar.");
                    }else if(usuarioDto.getIdioma().equals("I")){
                        new Mensaje().showModal(Alert.AlertType.ERROR, "delete movie", getStage(), "You need to upload the movie you want to delete.");
                        
                    }
            } else {

                PeliculaService service = new PeliculaService();
                Respuesta respuesta = service.eliminarPelicula(peliculaDto.getId());
                if (!respuesta.getEstado()) {
                   if(usuarioDto.getIdioma().equals("E")){
                           new Mensaje().showModal(Alert.AlertType.ERROR, "Eliminar pelicula", getStage(), respuesta.getMensaje());
                    }else if(usuarioDto.getIdioma().equals("I")){
                            new Mensaje().showModal(Alert.AlertType.ERROR, "delete movie", getStage(), respuesta.getMensaje());
                        
                    }
                } else {
                    if(usuarioDto.getIdioma().equals("E")){
                       new Mensaje().showModal(Alert.AlertType.INFORMATION, "Eliminar pelicula", getStage(), "Pelicula eliminada correctamente.");
                    nuevaPelicula();
                    }else if(usuarioDto.getIdioma().equals("I")){
                        new Mensaje().showModal(Alert.AlertType.INFORMATION, "delete movie", getStage(), "Movie removed correctly.");
                    nuevaPelicula();
                    }
                }
            }
        } catch (Exception ex) {if(usuarioDto.getIdioma().equals("E")){
                 Logger.getLogger(RegistroAlimentoViewController.class.getName()).log(Level.SEVERE, "Error eliminando la pelicula", ex);
            new Mensaje().showModal(Alert.AlertType.ERROR, "Eliminar pelicula", getStage(), "Ocurrio un error eliminando la pelicula.");
                       
                    }else if(usuarioDto.getIdioma().equals("I")){
                         Logger.getLogger(RegistroAlimentoViewController.class.getName()).log(Level.SEVERE, "Error delete Food", ex);
            new Mensaje().showModal(Alert.AlertType.ERROR, "Delete movie", getStage(), "An error occurred deleting the movie.");
                        
                    }
        }
    }

    @FXML
    private void OnActionBtnGuardar(ActionEvent event
    ) {
        try {
            String invalidos = validarRequeridos();
            if (!invalidos.isEmpty()) {
               if(usuarioDto.getIdioma().equals("E")){
                        new Mensaje().showModal(Alert.AlertType.ERROR, "Guardar pelicula", getStage(), invalidos);
                    }else if(usuarioDto.getIdioma().equals("I")){
                         new Mensaje().showModal(Alert.AlertType.ERROR, "Save movie", getStage(), invalidos);
                        
                    }
            } else {
                PeliculaService service = new PeliculaService();
                Respuesta respuesta = service.guardarPelicula(peliculaDto);
                if (!respuesta.getEstado()) {
                    if(usuarioDto.getIdioma().equals("E")){
                       new Mensaje().showModal(Alert.AlertType.ERROR, "Guardar pelicula", getStage(), respuesta.getMensaje());
                    }else if(usuarioDto.getIdioma().equals("I")){
                        new Mensaje().showModal(Alert.AlertType.ERROR, "Save movie", getStage(), respuesta.getMensaje());
                        
                    }
                } else {
                    unbindPelicula();
                    peliculaDto = (PeliculaDto) respuesta.getResultado("Pelicula");
                    if(usuarioDto.getIdioma().equals("E")){
                       new Mensaje().showModal(Alert.AlertType.INFORMATION, "Guardar pelicula", getStage(), "pelicula actualizada correctamente.");
                    }else if(usuarioDto.getIdioma().equals("I")){
                        new Mensaje().showModal(Alert.AlertType.INFORMATION, "Save movie", getStage(), "Movie updated successfully.");
                    }

                    nuevaPelicula();

                }
            }
        } catch (Exception ex) {
           if(usuarioDto.getIdioma().equals("E")){
                
                       Logger.getLogger(RegistroAlimentoViewController.class.getName()).log(Level.SEVERE, "Error guardando el Alimento.", ex);
            new Mensaje().showModal(Alert.AlertType.ERROR, "Guardar pelicula", getStage(), "Ocurrio un error guardando la pelicula.");
                    }else if(usuarioDto.getIdioma().equals("I")){
                        Logger.getLogger(RegistroAlimentoViewController.class.getName()).log(Level.SEVERE, "Error saving movie.", ex);
            new Mensaje().showModal(Alert.AlertType.ERROR, "Save food", getStage(), "Error saving movie");
                        
                    }
        }
    }

    private void nuevaPelicula() {
        unbindPelicula();
        peliculaDto = new PeliculaDto();
        bindPelicula(true);
        txtId.clear();
        txtId.requestFocus();
        imgPortadaEspannol.setImage(null);
        imgPortadaIngles.setImage(null);
    }

    public void indicarRequeridos() {
        requeridos.clear();
        requeridos.addAll(Arrays.asList(txtNombreEspannol, txtNombreIngles,
                txtResennaEspannol, txtResennaIngles,
                txtTrailerEspannol, txtTrailerIngles, dpFechaEstreno,dtpFechaFin, imgPortadaEspannol, imgPortadaIngles));
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
        
        lblTitulo.setText(prop.getProperty("txttituloRegistroPelicula"));
        txtId.setPromptText(prop.getProperty("txtIdRegistroPelicula"));
        txtNombreEspannol.setPromptText(prop.getProperty("txtNombreEspannolRegistroPelicula"));
        txtNombreIngles.setPromptText(prop.getProperty("txtNombreInglesRegistroPelicula"));
        chkEspannol.setText(prop.getProperty("chkEspannolRegistroPelicula"));
        chkIngles.setText(prop.getProperty("chkInglesRegistroPelicula"));
        chkAmbos.setText(prop.getProperty("chkAmbosRegistroPelicula"));
        txtTrailerEspannol.setPromptText(prop.getProperty("txtTrailerEspannolRegistroPelicula"));
        txtTrailerIngles.setPromptText(prop.getProperty("txtTrailerInglesRegistroPelicula"));
        txtPortadaEspannolRegistroPelicula.setText(prop.getProperty("txtPortadaEspannolRegistroPelicula"));
        txtPortadaInglesRegistroPelicula.setText(prop.getProperty("txtPortadaInglesRegistroPelicula"));
        dpFechaEstreno.setPromptText(prop.getProperty("dpFechaEstrenoRegistroPelicula"));
        dtpFechaFin.setPromptText(prop.getProperty("dtpFechaFinRegistroPelicula"));
        chkDisponiblePronto.setText(prop.getProperty("chkDisponibleProntoRegistroPelicula"));
        chkEnCartelera.setText(prop.getProperty("chkEnCarteleraRegistroPelicula"));
        chkInactiva.setText(prop.getProperty("chkInactivaRegistroPelicula"));
        txtResennaEspannol.setPromptText(prop.getProperty("txtResennaEspannolRegistroPelicula"));
        txtResennaIngles.setPromptText(prop.getProperty("txtResennaInglesRegistroPelicula"));
        

        btnNuevo.setText(prop.getProperty("btnNuevo"));
        btnBuscar.setText(prop.getProperty("btnBuscar"));
        btnEliminar.setText(prop.getProperty("btnEliminar"));
        btnGuardar.setText(prop.getProperty("btnGuardar"));
        
        
        btnBuscarTrailerEspanol.setText(prop.getProperty("btnBuscarTrailerEspannol"));
        btnBuscarTrailerIngles.setText(prop.getProperty("btnBuscarTrailerIngles"));
        btnBuscarPortadaEspanol.setText(prop.getProperty("btnBuscarPortadaEspannol"));
        btnBuscarPortadaIngles.setText(prop.getProperty("btnBuscarPortadaIngles"));
        
        
        
        

    }
}
