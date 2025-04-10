package cr.ac.una.cineuna.controller;

import com.jfoenix.controls.JFXTextField;
import cr.ac.una.cineuna.idioma.Idioma;
import cr.ac.una.cineuna.model.PeliculaDto;
import cr.ac.una.cineuna.model.UsuarioDto;
import cr.ac.una.cineuna.service.PeliculaService;
import cr.ac.una.cineuna.util.AppContext;
import cr.ac.una.cineuna.util.FlowController;
import cr.ac.una.cineuna.util.Mensaje;
import cr.ac.una.cineuna.util.Respuesta;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * FXML Controller class
 *
 * @author Cristhofer
 */
public class PeliculasPorEstrenarseViewController extends Controller implements Initializable {

    private JFXTextField txtNombre;
    private JFXTextField txtFechaEstreno;
    private JFXTextField txtResenna;
    private WebView webView;
    private ImageView imgPortada;

    private boolean boton = false;

    @FXML
    private Label lblTitulo;
    @FXML
    private TableView tbvPeliculasPorEstrenarse;

    PeliculaDto peliculaDto;
    private WebEngine engine;
    public Object resultado;

    UsuarioDto usuarioDto;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        peliculaDto = new PeliculaDto();       
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
        peliculasEstreno();
    }

    @Override
    public void initialize() {
    }

    public void peliculasEstreno() {
        try {
            lblTitulo.setText("Peliculas en cartelera");
            tbvPeliculasPorEstrenarse.getColumns().clear();
            tbvPeliculasPorEstrenarse.getItems().clear();

            TableColumn<PeliculaDto, String> tbcNombre = new TableColumn<>("Nombre");
            tbcNombre.setPrefWidth(150);
            tbcNombre.setCellValueFactory(cd -> cd.getValue().nombreEspannol);

            TableColumn<PeliculaDto, LocalDate> tbcFechaEstreno = new TableColumn<>("Fecha estreno");
            tbcFechaEstreno.setPrefWidth(150);
            tbcFechaEstreno.setCellValueFactory(cd -> cd.getValue().fechaEstreno);

            TableColumn<PeliculaDto, String> tbcPortada = new TableColumn<>("Portada");
            tbcPortada.setPrefWidth(80);
            tbcPortada.setCellValueFactory((TableColumn.CellDataFeatures<PeliculaDto, String> p) -> new SimpleStringProperty(p.getValue().getPortadaEspannol()));
            tbcPortada.setCellFactory((TableColumn<PeliculaDto, String> p) -> new ImageViewCell());

            TableColumn<PeliculaDto, Boolean> tbcBotones = new TableColumn("Más información");
            tbcBotones.setPrefWidth(150);
            tbcBotones.setCellValueFactory((TableColumn.CellDataFeatures<PeliculaDto, Boolean> p) -> new SimpleBooleanProperty(p.getValue() != null));
            tbcBotones.setCellFactory((TableColumn<PeliculaDto, Boolean> p) -> new ButtonCell());
            if(usuarioDto.getIdioma().equals("E")){
                       tbcNombre.setText("Nombre");
                       tbcFechaEstreno.setText("Fecha estreno");
                       tbcPortada.setText("Portada");
                       tbcBotones.setText("Información");
                    }else if(usuarioDto.getIdioma().equals("I")){
                       tbcNombre.setText("Name");
                       tbcFechaEstreno.setText("Release date");
                       tbcPortada.setText("Cover");
                       tbcBotones.setText("Information");
                        
                    }
            
            tbvPeliculasPorEstrenarse.getColumns().add(tbcNombre);
            tbvPeliculasPorEstrenarse.getColumns().add(tbcFechaEstreno);
            tbvPeliculasPorEstrenarse.getColumns().add(tbcPortada);
            tbvPeliculasPorEstrenarse.getColumns().add(tbcBotones);

            tbvPeliculasPorEstrenarse.refresh();

            tbvPeliculasPorEstrenarse.getItems().clear();

            PeliculaService service = new PeliculaService();
            String estado = "D";

            Respuesta respuesta = service.getPeliculasPorEstrenarse(estado);

            if (respuesta.getEstado()) {
                ObservableList<PeliculaDto> peliculas = FXCollections.observableList((List<PeliculaDto>) respuesta.getResultado("Peliculas"));
                tbvPeliculasPorEstrenarse.setItems(peliculas);
                tbvPeliculasPorEstrenarse.refresh();
            } else {
            if(usuarioDto.getIdioma().equals("E")){
                       new Mensaje().showModal(Alert.AlertType.ERROR, "Información peliculas", getStage(), respuesta.getMensaje());
                    }else if(usuarioDto.getIdioma().equals("I")){
                        new Mensaje().showModal(Alert.AlertType.ERROR, "Movie information", getStage(), respuesta.getMensaje());     
                }    
            }

        } catch (Exception ex) {
        if (usuarioDto.getIdioma().equals("E")) {
                Logger.getLogger(PeliculasPorEstrenarseViewController.class.getName()).log(Level.SEVERE, "Error informacion de peliculas cartelera.", ex);
                new Mensaje().showModal(Alert.AlertType.ERROR, "Informacion peliculas cartelera", getStage(), "Ocurrio un error obteniendo la informacion de las peliculas en cartelera.");

            } else if (usuarioDto.getIdioma().equals("I")) {
                Logger.getLogger(PeliculasPorEstrenarseViewController.class.getName()).log(Level.SEVERE, "Movie information error.", ex);
                new Mensaje().showModal(Alert.AlertType.ERROR, "Information billboard", getStage(), "Movie information error.");
            }    
        }
    }


    private void cargarPelicula(Long id) {
        PeliculaService service = new PeliculaService();
        Respuesta respuesta = service.getPelicula(id);

        if (respuesta.getEstado()) {
            if (usuarioDto.getIdioma().equals("E")) {
                peliculaDto = (PeliculaDto) respuesta.getResultado("Pelicula");
                txtNombre.setText(peliculaDto.getNombreEspannol());
                txtFechaEstreno.setText(peliculaDto.getFechaEstreno().toString());
                txtResenna.setText(peliculaDto.getResennaEspannol());
                engine = webView.getEngine();
                engine.load(peliculaDto.getTrailerEspannol());

                byte[] PotadaEspanolByte = Base64.getDecoder().decode(peliculaDto.getPortadaEspannol());
                Image imagePotadaEspañol = new Image(new ByteArrayInputStream(PotadaEspanolByte));
                imgPortada.setImage(imagePotadaEspañol);

            } else if (usuarioDto.getIdioma().equals("I")) {
                peliculaDto = (PeliculaDto) respuesta.getResultado("Pelicula");
                txtNombre.setText(peliculaDto.getNombreIngles());
                txtFechaEstreno.setText(peliculaDto.getFechaEstreno().toString());
                txtResenna.setText(peliculaDto.getNombreIngles());
                engine = webView.getEngine();
                engine.load(peliculaDto.getPortadaIngles());

                byte[] PotadaEspanolByte = Base64.getDecoder().decode(peliculaDto.getPortadaEspannol());
                Image imagePotadaIngles = new Image(new ByteArrayInputStream(PotadaEspanolByte));
                imgPortada.setImage(imagePotadaIngles);

            }

        } else {
        if (usuarioDto.getIdioma().equals("E")) {
                new Mensaje().showModal(Alert.AlertType.ERROR, "Cargar pelicula", getStage(), respuesta.getMensaje());
            } else if (usuarioDto.getIdioma().equals("I")) {
                new Mensaje().showModal(Alert.AlertType.ERROR, "Load movie", getStage(), respuesta.getMensaje());
            }    
        }
    }

    @FXML
    private void OnMousePressedTbvResultados(MouseEvent event) {
    }

    public Object getResultado() {
        return resultado;
    }

    public void setResultado(Object resultado) {
        this.resultado = resultado;
    }

    private class ButtonCell extends TableCell<PeliculaDto, Boolean> {

        final Button cellButton = new Button();

        ButtonCell() {
            cellButton.setPrefWidth(500);
            if(usuarioDto.getIdioma().equals("E")){
                       cellButton.setText("Ver más");
                    }else if(usuarioDto.getIdioma().equals("I")){
                        cellButton.setText("See more");
                        
                    }
            
            
            cellButton.setOnAction((ActionEvent t) -> {
                PeliculaDto emp = (PeliculaDto) ButtonCell.this.getTableView().getItems().get(ButtonCell.this.getIndex());
                resultado = emp;
                AppContext.getInstance().set("PeliculaSeleccionada", resultado);
                FlowController.getInstance().goViewInWindowModal("DetallePeliculaPorEstrenarseView", getStage(), true);
            });
        }

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if (!empty) {
                setGraphic(cellButton);
            }
        }
    }

    private class ImageViewCell extends TableCell<PeliculaDto, String> {

        final ImageView imageView = new ImageView();

        ImageViewCell() {

        }

        @Override
        protected void updateItem(String t, boolean empty) {
            super.updateItem(t, empty);
            if (!empty) {
                byte[] PotadaEspanolByte = Base64.getDecoder().decode(t);
                Image imagePotadaEspañol = new Image(new ByteArrayInputStream(PotadaEspanolByte));
                imageView.setImage(imagePotadaEspañol);
                imageView.setFitHeight(150);
                imageView.setFitWidth(75);
                setGraphic(imageView);
            }
        }
    }

    private void cambiarIdioma(String pIdioma) throws IOException {
        Idioma idiom = new Idioma(pIdioma);
        Properties prop = idiom.getProperties(pIdioma);
        lblTitulo.setText(prop.getProperty("lblTituloPeliculasPorEstrenarse"));

    }

}
