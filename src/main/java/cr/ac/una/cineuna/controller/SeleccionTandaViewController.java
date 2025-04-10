/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package cr.ac.una.cineuna.controller;

import cr.ac.una.cineuna.model.PeliculaDto;
import cr.ac.una.cineuna.model.TandaDto;
import cr.ac.una.cineuna.service.PeliculaService;
import cr.ac.una.cineuna.util.AppContext;
import cr.ac.una.cineuna.util.Mensaje;
import cr.ac.una.cineuna.util.Respuesta;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author Cristhofer
 */
public class SeleccionTandaViewController extends Controller implements Initializable {

    @FXML
    private TableView tbvTandas;
    @FXML
    private Label lblTitulo;
    PeliculaDto peliculaDto;
    TandaDto tandaDto;
    public Object resultado;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void OnMousePressedTbvResultados(MouseEvent event) {
        if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
            resultado = tbvTandas.getSelectionModel().getSelectedItem();
            getStage().close();
        }
    }

    @Override
    public void initialize() {
        peliculaDto = new PeliculaDto();
        peliculaDto = (PeliculaDto) AppContext.getInstance().get("PeliculaSeleccionada");
        peliculasCartelera();
    }

    public void peliculasCartelera() {
        try {

            lblTitulo.setText("Tandas Disponibles");

            tbvTandas.getColumns().clear();
            tbvTandas.getItems().clear();

            TableColumn<TandaDto, String> tbcCosto = new TableColumn<>(" Nombre ");
            tbcCosto.setPrefWidth(75);

            tbcCosto.setCellValueFactory(cd -> cd.getValue().costoEntrada);

            TableColumn<TandaDto, LocalDate> tbcFecha = new TableColumn<>(" Fecha Inicio ");
            tbcFecha.setPrefWidth(100);
            tbcFecha.setCellValueFactory(cd -> cd.getValue().fecha);

            TableColumn<TandaDto, String> tbcHoraInicio = new TableColumn<>(" Hora Inicio ");
            tbcHoraInicio.setPrefWidth(75);
            tbcHoraInicio.setCellValueFactory(cd -> cd.getValue().horaInicio);

            TableColumn<TandaDto, String> tbcHoraFin = new TableColumn<>(" Hora Fin ");
            tbcHoraFin.setPrefWidth(75);
            tbcHoraFin.setCellValueFactory(cd -> cd.getValue().horaFin);

            tbvTandas.getColumns().add(tbcCosto);
            tbvTandas.getColumns().add(tbcFecha);
            tbvTandas.getColumns().add(tbcHoraInicio);
            tbvTandas.getColumns().add(tbcHoraFin);

            tbvTandas.refresh();

            tbvTandas.getItems().clear();

            PeliculaService service = new PeliculaService();
            Respuesta respuesta = service.getPelicula(peliculaDto.getId());

            if (respuesta.getEstado()) {
                peliculaDto = (PeliculaDto) respuesta.getResultado("Pelicula");
                //ObservableList<PeliculaDto> peliculas = FXCollections.observableList((List<PeliculaDto>) respuesta.getResultado("Peliculas"));
                tbvTandas.setItems(peliculaDto.getTandas());
                tbvTandas.refresh();
            } else {
                new Mensaje().showModal(Alert.AlertType.ERROR, "Informacion peliculas", getStage(), respuesta.getMensaje());
            }

        } catch (Exception ex) {
            //Logger.getLogger(InformacionPeliculasViewController.class.getName()).log(Level.SEVERE, "Error informacion de peliculas cartelera.", ex);
            new Mensaje().showModal(Alert.AlertType.ERROR, "informacion peliculas cartelera", getStage(), "Ocurrio un error obteniendo la informacion de las peliculas en cartelera.");
        }
    }

    public Object getResultado() {
        return resultado;
    }

    public void setResultado(Object resultado) {
        this.resultado = resultado;
    }
}
