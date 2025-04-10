/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package cr.ac.una.cineuna.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import cr.ac.una.cineuna.idioma.Idioma;
import cr.ac.una.cineuna.model.PeliculaDto;
import cr.ac.una.cineuna.model.SalaDto;
import cr.ac.una.cineuna.model.UsuarioDto;
import cr.ac.una.cineuna.service.PeliculaService;
import cr.ac.una.cineuna.service.SalaService;
import cr.ac.una.cineuna.util.AppContext;
import cr.ac.una.cineuna.util.Formato;
import cr.ac.una.cineuna.util.Mensaje;
import cr.ac.una.cineuna.util.Respuesta;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Cristhofer
 */
public class BusquedaViewController extends Controller implements Initializable {

    @FXML
    private VBox vbxParametros;
    @FXML
    private JFXButton btnFiltrar;

    @FXML
    private TableView tbvResultados;
    @FXML
    private JFXButton btnAceptar;

    private EventHandler<KeyEvent> keyEnter;

    private Object resultado;
    @FXML
    private Label lblParametros;
    @FXML
    private Label lblBusqueda;
    
    UsuarioDto usuarioDto;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

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

        keyEnter = (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                btnFiltrar.fire();
            }
        };
    }

    @Override
    public void initialize() {

    }

    @FXML
    private void OnMousePressedTbvResultados(MouseEvent event) {
        if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
            onActionBtnAceptar(null);
        }
    }

    @FXML
    private void onActionBtnAceptar(ActionEvent event) {
        resultado = tbvResultados.getSelectionModel().getSelectedItem();
        getStage().close();

    }

    public void busquedaPeliculas() {
        try {
           

            JFXTextField txtNombreEspannol = new JFXTextField();
            txtNombreEspannol.setLabelFloat(true);
            txtNombreEspannol.setOnKeyPressed(keyEnter);
            txtNombreEspannol.setTextFormatter(Formato.getInstance().letrasFormat(30));

            JFXTextField txtNombreIngles = new JFXTextField();
            txtNombreIngles.setLabelFloat(true);
            txtNombreIngles.setTextFormatter(Formato.getInstance().letrasFormat(30));
            txtNombreIngles.setOnKeyPressed(keyEnter);

            JFXDatePicker dpFechaEstreno = new JFXDatePicker();
            dpFechaEstreno.setOnKeyPressed(keyEnter);

            vbxParametros.getChildren().clear();
            vbxParametros.getChildren().add(txtNombreEspannol);
            vbxParametros.getChildren().add(txtNombreIngles);
            vbxParametros.getChildren().add(dpFechaEstreno);

            tbvResultados.getColumns().clear();
            tbvResultados.getItems().clear();

            TableColumn<PeliculaDto, String> tbcNombreEspannol = new TableColumn<>("");
            tbcNombreEspannol.setPrefWidth(250);
            tbcNombreEspannol.setCellValueFactory(cd -> cd.getValue().nombreEspannol);

            TableColumn<PeliculaDto, String> tbcNombreIngles = new TableColumn<>("");
            tbcNombreIngles.setPrefWidth(250);
            tbcNombreIngles.setCellValueFactory(cd -> cd.getValue().nombreIngles);

            TableColumn<PeliculaDto, String> tbcFechaEstreno = new TableColumn<>("");
            tbcFechaEstreno.setPrefWidth(150);
            tbcFechaEstreno.setCellValueFactory(cd -> cd.getValue().fechaEstreno.asString());

            if (usuarioDto.getIdioma().equals("I")) {
                txtNombreEspannol.setPromptText("Spanish name");
                txtNombreIngles.setPromptText("English name");
                dpFechaEstreno.setPromptText("Premiere");
                tbcNombreEspannol.setText("Spanish name");
                tbcNombreIngles.setText("English name");
                tbcFechaEstreno.setText("Premiere");
                lblBusqueda.setText("Movie search");
            } else if (usuarioDto.getIdioma().equals("E")) {
                lblBusqueda.setText("Búsqueda de Peliculas");
                txtNombreEspannol.setPromptText("Nombre en español");
                txtNombreIngles.setPromptText("Nombre en ingles");
                dpFechaEstreno.setPromptText("Fecha de estreno");
                tbcNombreEspannol.setText("Nombre en español");
                tbcNombreIngles.setText("Nombre en ingles");
                tbcFechaEstreno.setText("Fecha de estreno");
            }
            tbvResultados.getColumns().add(tbcNombreEspannol);
            tbvResultados.getColumns().add(tbcNombreIngles);
            tbvResultados.getColumns().add(tbcFechaEstreno);

            tbvResultados.refresh();

            btnFiltrar.setOnAction((ActionEvent event) -> {

                tbvResultados.getItems().clear();

                PeliculaService service = new PeliculaService();
                String nombreEspannol = "%" + txtNombreEspannol.getText() + "%";

                String nombreIngles = "%" + txtNombreIngles.getText() + "%";

                Respuesta respuesta = service.getPeliculas(nombreEspannol.toUpperCase(), nombreIngles.toUpperCase());//fechaEstreno
                ObservableList<PeliculaDto> peliculaDtos = FXCollections.observableList((List<PeliculaDto>) respuesta.getResultado("Peliculas"));
                List lis = peliculaDtos.stream().filter(s -> s.getNombreEspannol().contains(txtNombreEspannol.getText()) && s.getNombreIngles().contains(txtNombreIngles.getText())).collect(Collectors.toList());

                if (respuesta.getEstado()) {
                    ObservableList<PeliculaDto> peliculasFiltradas = FXCollections.observableList(lis);
                    if (dpFechaEstreno.getValue() != null) {
                        ObservableList<PeliculaDto> peliculasxFecha = FXCollections.observableList(lis);
                        List lista = peliculasxFecha.stream().filter(s -> s.getFechaEstreno().equals(dpFechaEstreno.getValue())).collect(Collectors.toList()); //.collect(Collectors.toList()
                        peliculasFiltradas = FXCollections.observableList(lista);
                    }

                    tbvResultados.setItems(peliculasFiltradas);
                    tbvResultados.refresh();
                } else {
                     if (usuarioDto.getIdioma().equals("E")) {
                        new Mensaje().showModal(Alert.AlertType.ERROR, "Consultar peliculas", getStage(), respuesta.getMensaje());
                    } else {
                        new Mensaje().showModal(Alert.AlertType.ERROR, "Consult movies", getStage(), respuesta.getMensaje());
                    }
                }
            });
        } catch (Exception ex) {
            if (usuarioDto.getIdioma().equals("E")) {
                Logger.getLogger(BusquedaViewController.class.getName()).log(Level.SEVERE, "Error consultando los peliculas.", ex);
                new Mensaje().showModal(Alert.AlertType.ERROR, "Consultar peliculas", getStage(), "Ocurrio un error consultado los peliculas.");
            } else {
                Logger.getLogger(BusquedaViewController.class.getName()).log(Level.SEVERE, "Error consultando los peliculas.", ex);
                new Mensaje().showModal(Alert.AlertType.ERROR, "Consult movies", getStage(), "An error occurred while viewing movies.");
            }}
    }

    public void busquedaSalas() {
        try {
            
            tbvResultados.getColumns().clear();
            tbvResultados.getItems().clear();

            TableColumn<SalaDto, String> tbcNombre = new TableColumn<>("");
            tbcNombre.setPrefWidth(250);
            tbcNombre.setCellValueFactory(cd -> cd.getValue().nombre);

            TableColumn<SalaDto, String> tbcDetalle = new TableColumn<>("");
            tbcDetalle.setPrefWidth(300);
            tbcDetalle.setCellValueFactory(cd -> cd.getValue().detalle);
            
            if (usuarioDto.getIdioma().equals("E")) {
                lblBusqueda.setText("Búsqueda de salas");
                tbcNombre.setText("Nombre");
                tbcDetalle.setText("Detalle");
            } else {
                lblBusqueda.setText("Room search");
                tbcNombre.setText("Name");
                tbcDetalle.setText("Detail");
            }

            tbvResultados.getColumns().add(tbcNombre);
            tbvResultados.getColumns().add(tbcDetalle);


            tbvResultados.refresh();

            tbvResultados.getItems().clear();

            SalaService service = new SalaService();
            String nombre = "%" /*+ txtNombre.getText()*/ + "%";

            String detalle = "%" /*+ txtDetalle.getText() */ + "%";

            Respuesta respuesta = service.getSalas(nombre.toUpperCase(), detalle.toUpperCase());

            if (respuesta.getEstado()) {
                ObservableList<SalaDto> salas = FXCollections.observableList((List<SalaDto>) respuesta.getResultado("Salas"));
                tbvResultados.setItems(salas);
                tbvResultados.refresh();
            } else {
                if (usuarioDto.getIdioma().equals("E")) {
                    new Mensaje().showModal(Alert.AlertType.ERROR, "Consultar salas", getStage(), respuesta.getMensaje());
                } else {
                    new Mensaje().showModal(Alert.AlertType.ERROR, "Consult rooms", getStage(), respuesta.getMensaje());
                }
            }

        } catch (Exception ex) {
            if (usuarioDto.getIdioma().equals("E")) {
                Logger.getLogger(BusquedaViewController.class.getName()).log(Level.SEVERE, "Error consultando las salas.", ex);
                new Mensaje().showModal(Alert.AlertType.ERROR, "Consultar salas", getStage(), "Ocurrio un error consultado las salas.");
            } else {
                Logger.getLogger(BusquedaViewController.class.getName()).log(Level.SEVERE, "Error consulted rooms.", ex);
                new Mensaje().showModal(Alert.AlertType.ERROR, "Consult rooms", getStage(), ".");
            }
        }
    }
    
    public void busquedaPeliculasReporte() {
        try {

            tbvResultados.getColumns().clear();
            tbvResultados.getItems().clear();

            TableColumn<PeliculaDto, String> tbcNombreEspannol = new TableColumn<>("");
            tbcNombreEspannol.setPrefWidth(250);
            tbcNombreEspannol.setCellValueFactory(cd -> cd.getValue().nombreEspannol);

            TableColumn<PeliculaDto, String> tbcNombreIngles = new TableColumn<>("");
            tbcNombreIngles.setPrefWidth(250);
            tbcNombreIngles.setCellValueFactory(cd -> cd.getValue().nombreIngles);
            
            if (usuarioDto.getIdioma().equals("I")) {
                tbcNombreEspannol.setText("Spanish name");
                tbcNombreIngles.setText("English name");
                lblBusqueda.setText("Movie search");
            } else if (usuarioDto.getIdioma().equals("E")) {
                lblBusqueda.setText("Búsqueda de Peliculas");
                tbcNombreEspannol.setText("Nombre en español");
                tbcNombreIngles.setText("Nombre en ingles");
            }
            tbvResultados.getColumns().add(tbcNombreEspannol);
            tbvResultados.getColumns().add(tbcNombreIngles);

            tbvResultados.refresh();

            btnFiltrar.setOnAction((ActionEvent event) -> {

                tbvResultados.getItems().clear();

                PeliculaService service = new PeliculaService();
             
                Respuesta respuesta = service.getAllPeliculas();
                ObservableList<PeliculaDto> peliculaDtos = FXCollections.observableList((List<PeliculaDto>) respuesta.getResultado("Peliculas"));
                
                if (respuesta.getEstado()) {
                    ObservableList<PeliculaDto> peliculas = FXCollections.observableList(peliculaDtos);

                    tbvResultados.setItems(peliculas);
                    tbvResultados.refresh();
                } else {
                     if (usuarioDto.getIdioma().equals("E")) {
                        new Mensaje().showModal(Alert.AlertType.ERROR, "Consultar peliculas", getStage(), respuesta.getMensaje());
                    } else {
                        new Mensaje().showModal(Alert.AlertType.ERROR, "Consult movies", getStage(), respuesta.getMensaje());
                    }
                }
            });
        } catch (Exception ex) {
            if (usuarioDto.getIdioma().equals("E")) {
                Logger.getLogger(BusquedaViewController.class.getName()).log(Level.SEVERE, "Error consultando los peliculas.", ex);
                new Mensaje().showModal(Alert.AlertType.ERROR, "Consultar peliculas", getStage(), "Ocurrio un error consultado los peliculas.");
            } else {
                Logger.getLogger(BusquedaViewController.class.getName()).log(Level.SEVERE, "Error consultando los peliculas.", ex);
                new Mensaje().showModal(Alert.AlertType.ERROR, "Consult movies", getStage(), "An error occurred while viewing movies.");
            }}
    }
    

    public Object getResultado() {
        return resultado;
    }

    public void setResultado(Object resultado) {
        this.resultado = resultado;
    }
    private void cambiarIdioma(String pIdioma) throws IOException {
        Idioma idiom = new Idioma(pIdioma);
        Properties prop = idiom.getProperties(pIdioma);
        lblBusqueda.setText(prop.getProperty("lblTituloBusquedaView"));
        lblParametros.setText(prop.getProperty("lblParametrosBusquedaView"));
        btnAceptar.setText(prop.getProperty("btnAceptarBusquedaView"));
        btnFiltrar.setText(prop.getProperty("btnFiltrarBusquedaView"));
    }
}
