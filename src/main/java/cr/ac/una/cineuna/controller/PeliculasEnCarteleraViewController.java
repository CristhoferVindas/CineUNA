/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package cr.ac.una.cineuna.controller;

import com.jfoenix.controls.JFXTextField;
import cr.ac.una.cineuna.model.PeliculaDto;
import cr.ac.una.cineuna.model.TandaDto;
import cr.ac.una.cineuna.model.UsuarioDto;
import cr.ac.una.cineuna.service.PeliculaService;
import cr.ac.una.cineuna.service.TandaService;
import cr.ac.una.cineuna.util.AppContext;
import cr.ac.una.cineuna.util.FlowController;
import cr.ac.una.cineuna.util.Mensaje;
import cr.ac.una.cineuna.util.Respuesta;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Base64;
import java.util.List;
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
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * FXML Controller class
 *
 * @author Cristhofer
 */
public class PeliculasEnCarteleraViewController extends Controller implements Initializable {


    private Text txtTanda;

    PeliculaDto peliculaDto;
    TandaDto tandaDto;
    public Object resultado;
    private WebEngine engine;
    @FXML
    private TableView tbvPeliculasPorEnCartelera;
    @FXML
    private Label lbltitulo;
     UsuarioDto usuarioDto;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        peliculaDto = new PeliculaDto();
        usuarioDto = (UsuarioDto) AppContext.getInstance().get("Usuario");
        peliculasCartelera();
     
       
    }

    @Override
    public void initialize() {
    }

    public void peliculasCartelera() {
        try {

            lbltitulo.setText("Películas en cartelera");

            tbvPeliculasPorEnCartelera.getColumns().clear();
            tbvPeliculasPorEnCartelera.getItems().clear();

            TableColumn<PeliculaDto, String> tbcNombre = new TableColumn<>(" Nombre ");
            tbcNombre.setPrefWidth(200);
            tbcNombre.setCellValueFactory(cd -> cd.getValue().nombreEspannol);

            TableColumn<PeliculaDto, Boolean> tbcBotones = new TableColumn("Botones");
            tbcBotones.setPrefWidth(150);
            tbcBotones.setCellValueFactory((TableColumn.CellDataFeatures<PeliculaDto, Boolean> p) -> new SimpleBooleanProperty(p.getValue() != null));
            tbcBotones.setCellFactory((TableColumn<PeliculaDto, Boolean> p) -> new ButtonCell());

            TableColumn<PeliculaDto, String> tbcPortada = new TableColumn<>(" Portada ");
            tbcPortada.setPrefWidth(200);
            tbcPortada.setCellValueFactory((TableColumn.CellDataFeatures<PeliculaDto, String> p) -> new SimpleStringProperty(p.getValue().getPortadaIngles()));
            tbcPortada.setCellFactory((TableColumn<PeliculaDto, String> p) -> new ImageViewCell());

                        if(usuarioDto.getIdioma().equals("E")){
                       tbcNombre.setText("Nombre");
                      
                       tbcPortada.setText("Portada");
                       tbcBotones.setText("Información");
                    }else if(usuarioDto.getIdioma().equals("I")){
                       tbcNombre.setText("Name");
                    
                       tbcPortada.setText("Cover");
                       tbcBotones.setText("Information");
                        
                    }
            
            tbvPeliculasPorEnCartelera.getColumns().add(tbcNombre);
            tbvPeliculasPorEnCartelera.getColumns().add(tbcPortada);
            tbvPeliculasPorEnCartelera.getColumns().add(tbcBotones);

            tbvPeliculasPorEnCartelera.refresh();

            tbvPeliculasPorEnCartelera.getItems().clear();

            PeliculaService service = new PeliculaService();
            String estado = "C";

            Respuesta respuesta = service.getPeliculasPorEstrenarse(estado);

            if (respuesta.getEstado()) {
                ObservableList<PeliculaDto> peliculas = FXCollections.observableList((List<PeliculaDto>) respuesta.getResultado("Peliculas"));
                tbvPeliculasPorEnCartelera.setItems(peliculas);
                tbvPeliculasPorEnCartelera.refresh();
            } else {
                if (usuarioDto.getIdioma().equals("E")) {
                    new Mensaje().showModal(Alert.AlertType.ERROR, "Informacion peliculas", getStage(), respuesta.getMensaje());
                } else if (usuarioDto.getIdioma().equals("I")) {
                    new Mensaje().showModal(Alert.AlertType.ERROR, "Movie information", getStage(), respuesta.getMensaje());
                }
            }

        } catch (Exception ex) {
         if (usuarioDto.getIdioma().equals("E")) {
                new Mensaje().showModal(Alert.AlertType.ERROR, "Peliculas cartelera", getStage(), "Ocurrio un error obteniendo la informacion de las peliculas en cartelera.");

            } else if (usuarioDto.getIdioma().equals("I")) {
                new Mensaje().showModal(Alert.AlertType.ERROR, "Movies billboard", getStage(), "Error getting the information.");

            }   
        }
    }


    private void cargarTanda(Long id) {
        TandaService service = new TandaService();
        Respuesta respuesta = service.getTanda(id);
        if (respuesta.getEstado()) {
            txtTanda.setText(tandaDto.getIDPelicula().toString());
        } else {
            new Mensaje().showModal(Alert.AlertType.ERROR, "Cargar tanda", getStage(), respuesta.getMensaje());
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

    private class ButtonCell extends TableCell<PeliculaDto, Boolean>{

        final Button cellButton = new Button();

        ButtonCell() {
            cellButton.setPrefWidth(500);
            cellButton.setText("Ver más ");
            cellButton.setOnAction((ActionEvent t) -> {
                PeliculaDto emp = (PeliculaDto) ButtonCell.this.getTableView().getItems().get(ButtonCell.this.getIndex());
                resultado = emp;
                AppContext.getInstance().set("PeliculaSeleccionada", resultado);
                FlowController.getInstance().goViewInWindowModal("DetallePeliculaCarteleraView", getStage(), true);
               
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
                imageView.setFitHeight(200);
                imageView.setFitWidth(200);
                setGraphic(imageView);
            }
        }
    }

}
