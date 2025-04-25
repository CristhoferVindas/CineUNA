/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package cr.ac.una.cineuna.controller;

import com.jfoenix.controls.JFXButton;
import com.sun.mail.imap.ACL;
import static cr.ac.una.cineuna.controller.DistribucionAsientosViewController.String_Filas;
import cr.ac.una.cineuna.idioma.Idioma;
import cr.ac.una.cineuna.model.AsientoDto;
import cr.ac.una.cineuna.model.AsientoxtandaDto;
import cr.ac.una.cineuna.model.FacturaDto;
import cr.ac.una.cineuna.model.SalaDto;
import cr.ac.una.cineuna.model.TandaDto;
import cr.ac.una.cineuna.model.UsuarioDto;
import cr.ac.una.cineuna.service.AsientoxtandaService;
import cr.ac.una.cineuna.util.AppContext;
import cr.ac.una.cineuna.util.FlowController;
import cr.ac.una.cineuna.util.Mensaje;
import cr.ac.una.cineuna.util.Respuesta;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author Cristhofer
 */
public class SeleccionAsientoViewController extends Controller implements Initializable {

    @FXML
    private BorderPane root;
    @FXML
    private ScrollPane sclEspacioAsientos;
    @FXML
    private JFXButton btnAgregarFactura;

    GridPane grid = new GridPane();
    AsientoDto asientoDto;
    SalaDto salaDto;
    AsientoxtandaDto asientoxtandaDto;
    TandaDto tandaDto;
    FacturaDto facturaDto;

    int ultima_fila;
    int ultima_columna;

    Boolean asiento_creado = false;
    ObservableList<AsientoxtandaDto> asientoxtandaDtos;
    ObservableList<AsientoDto> asientosReservados = FXCollections.observableArrayList();
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
    }

    @Override
    public void initialize() {
        ultima_columna = 0;
        ultima_fila = 0;
        asientoxtandaDto = new AsientoxtandaDto();
        asientoDto = new AsientoDto();
        tandaDto = new TandaDto();
        tandaDto = (TandaDto) AppContext.getInstance().get("TandaSeleccionada");
        salaDto = new SalaDto();
        salaDto = (SalaDto) AppContext.getInstance().get("SalaSeleccionada");
        facturaDto = new FacturaDto();
        cargarAsientos();

    }

    void cargarAsientos() {
        
        grid = new GridPane();
        // asientoxtandaDtos.clear();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("..\\CineUNA\\420125.jpg");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DistribucionAsientosViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

        BackgroundImage image = new BackgroundImage(new Image(inputStream),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(grid.getWidth(), grid.getHeight(), true, true, true, true));

        byte[] imageB64 = Base64.getDecoder().decode(salaDto.getImagenAsiento());
        Image ia = new Image(new ByteArrayInputStream(imageB64));
        grid.setGridLinesVisible(true);
        grid.setBackground(new Background(image));
        if (!salaDto.getAsientos().isEmpty()) {
            for (AsientoDto asiento : salaDto.getAsientos()) {
                if (Int_Filas(asiento.getFila()) > ultima_fila) {
                    ultima_fila = Int_Filas(asiento.getFila());
                }
                if (asiento.getNumero() > ultima_columna) {
                    ultima_columna = asiento.getNumero().intValue();
                }
            }
            AsientoxtandaService service = new AsientoxtandaService();
            Respuesta respuesta = service.getAsientosxtandas(tandaDto.getId());

            if (respuesta.getEstado()) {
                asientoxtandaDtos = FXCollections.observableList((List<AsientoxtandaDto>) respuesta.getResultado("Asientoxtandas"));
            } else {
                new Mensaje().showModal(Alert.AlertType.ERROR, "Cargar Asientoxtanda", getStage(), respuesta.getMensaje());
            }
        } else {
            ultima_columna = 0;
            ultima_fila = 0;

            grid.add(CrearEspacio(ultima_columna, ultima_fila), ultima_columna, ultima_fila);
        }
        for (int i = 0; i <= ultima_columna; i++) {
            for (int j = 0; j <= ultima_fila; j++) {
                asiento_creado = false;
                if (!salaDto.getAsientos().isEmpty()) {
                    for (AsientoDto asi : salaDto.getAsientos()) {
                        if (Int_Filas(asi.getFila()) == j && asi.getNumero() == i) {

                            VBox box = crearEspaciosConAsiento(i, j, ia, asi.getId());
                            if (!asientoxtandaDtos.isEmpty()) {
                                for (AsientoxtandaDto asientoxtandaDto1 : asientoxtandaDtos) {
                                    if (asientoxtandaDto1.getIDAsiento().equals(asi.getId()) && asientoxtandaDto1.getIDTanda().equals(tandaDto.getId()) && asientoxtandaDto1.getIDFactura() != 0) {

                                        Background b = new Background(new BackgroundFill(Color.RED, new CornerRadii(10), new Insets(10)));
                                        Platform.runLater(() -> box.setBackground(b));
                                        box.setId("No Disponible");
                                        break;
                                    } else if (asientoxtandaDto1.getIDAsiento().equals(asi.getId()) && asientoxtandaDto1.getIDTanda().equals(tandaDto.getId()) && asientoxtandaDto1.getIDFactura() == 0) {
                                        Background b = new Background(new BackgroundFill(Color.ORANGE, new CornerRadii(10), new Insets(10)));
                                        Platform.runLater(() -> box.setBackground(b));
                                        box.setId("Reservado temporalmente");
                                        break;
                                    } else {
                                        Background b = new Background(new BackgroundFill(Color.GREEN, new CornerRadii(10), new Insets(10)));
                                        Platform.runLater(() -> box.setBackground(b));
                                        box.setId(asi.getId() + "");

                                    }
                                }
                            } else {
                                Background b = new Background(new BackgroundFill(Color.GREEN, new CornerRadii(10), new Insets(10)));
                                box.setBackground(b);
                                box.setId(asi.getId() + "");
                            }
                            grid.add(box, i, j);
                            asiento_creado = true;
                        }
                    }
                    if (asiento_creado == false) {
                        grid.add(CrearEspacio(i, j), i, j);
                        asiento_creado = true;
                    }

                } else {

                    grid.add(CrearEspacio(i, j), i, j);
                }
            }
        }

        sclEspacioAsientos.setContent(grid);

        grid.setOnMouseClicked((t) -> {
            Node node = t.getPickResult().getIntersectedNode();
            if (!(node instanceof VBox)) {
                node = node.getParent();
                if (node instanceof Label) {
                    node = node.getParent();
                }
                if (node instanceof HBox) {
                    node = node.getParent();
                }
            }
            if (!node.getId().equals("DEFAULT")) {
                if (node.getId().equals("No Disponible")) {
                    new Mensaje().showModal(Alert.AlertType.INFORMATION, "Seleccion Asiento", getStage(), "Este asiento ya ha sido Reservado");
                } else if (node.getId().equals("Reservado temporalmente")) {
                    cargarAsientos();
                    new Mensaje().showModal(Alert.AlertType.INFORMATION, "Seleccion Asiento", getStage(), "Este asiento está reservado temporalmente");
                } else {
                    int columnas = GridPane.getColumnIndex(node);
                    int filas = GridPane.getRowIndex(node);
                    AsientoxtandaService asientoService = new AsientoxtandaService();
                    asientoxtandaDto.setIDAsiento(Long.valueOf(node.getId()));
                    asientoxtandaDto.setIDTanda(tandaDto.getId());
                    Respuesta respuesta = asientoService.guardarAsientoxtanda(asientoxtandaDto);

                    if (respuesta.getEstado()) {
                        asientoxtandaDto = (AsientoxtandaDto) respuesta.getResultado("Asientoxtandas");
                        facturaDto.getAsientosxTandas().add(asientoxtandaDto);

                        for (AsientoDto asiento : salaDto.getAsientos()) {
                            if (asiento.getId().equals(asientoxtandaDto.getIDAsiento())) {
                                asientosReservados.add(asiento);
                            }
                        }
                        grid.getChildren().remove(node);

                        VBox box = crearEspaciosConAsiento(columnas, filas, ia, asientoDto.getId());
                        Background b = new Background(new BackgroundFill(Color.ORANGE, new CornerRadii(10), new Insets(10)));
                        box.setBackground(b);
                        box.setId("Reservado temporalmente");
                        grid.add(box, columnas, filas);
                        asientoxtandaDto = new AsientoxtandaDto();
                    } else {
                        new Mensaje().showModal(Alert.AlertType.ERROR, "Reservar Asiento", getStage(), "Asiento reservado");
                        
                    }
                }

            } else {
                new Mensaje().showModal(Alert.AlertType.INFORMATION, "Seleccion Asiento", getStage(), "Este espacio esta vacío");
            }
            cargarAsientos();
        });
    }

    public static String String_Filas(int i) {
        if (i < 0) {
            return "-" + String_Filas(-i - 1);
        }
        int quot = i / 26;
        int rem = i % 26;
        char letter = (char) ((int) 'A' + rem);
        if (quot == 0) {
            return "" + letter;
        } else {
            return String_Filas(quot - 1) + letter;
        }
    }

    public static int Int_Filas(String text) {
        int sum = 0;
        for (int i = 0; i < text.length(); i++) {
            sum = sum * 26 + text.charAt(i) - 'A';
        }
        return sum;
    }

    @FXML
    private void OnActionBtnAgregarFactura(ActionEvent event) {
        if (!facturaDto.getAsientosxTandas().isEmpty()) {
            AppContext.getInstance().set("FacturaActual", facturaDto);
            AppContext.getInstance().set("TandaActual", tandaDto);
            AppContext.getInstance().set("AsientosReservados", asientosReservados);
            FacturaViewController facturaViewController = (FacturaViewController) FlowController.getInstance().getController("FacturaView");
            facturaViewController.initialize();
            FlowController.getInstance().goViewInWindow("FacturaView");//, getStage(), true);
            stage.close();
            asientosReservados.clear();
        } else {
            new Mensaje().showModal(Alert.AlertType.INFORMATION, "Agregar Factura", getStage(), "No ha seleccionado ningun asiento.");
        }
    }

    public void confirmarSalida(Event e) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Confirmar Salida", new ButtonType("Continuar Comprando"), new ButtonType("Facturar"), new ButtonType("Salir"));
        alert.setTitle("Confirmar Salida");
        alert.setHeaderText(null);
        alert.initOwner(getStage());
        alert.setContentText("¿Esta seguro que desea salir?.");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == alert.getButtonTypes().get(0)) {
            e.consume();

        } else if (result.get() == alert.getButtonTypes().get(1)) {
            OnActionBtnAgregarFactura(null);
        } else if (result.get() == alert.getButtonTypes().get(2)) {
            AsientoxtandaService asientoService = new AsientoxtandaService();
            Respuesta respuesta = asientoService.getAsientosxtandas();

            if (respuesta.getEstado()) {
                
                for (AsientoxtandaDto asientoxtandaDto1 : facturaDto.getAsientosxTandas()) {
                    asientoService.eliminarAsientoxtanda(asientoxtandaDto1.getId());
                }
            }
        }

    }

    HBox CrearEspacio(int columna, int fila) {
        VBox vBox = new VBox();
        vBox.setPrefSize(100, 100);
        vBox.setAlignment(Pos.CENTER);
        vBox.setId("DEFAULT");
        HBox box = new HBox();
        Label col = new Label(columna + " ");
        col.setAlignment(Pos.CENTER);
        col.setPrefSize(20, 20);
        Label fil = new Label(String_Filas(fila));
        fil.setAlignment(Pos.CENTER);
        fil.setPrefSize(20, 20);
        box.getChildren().addAll(fil, col);
        box.setAlignment(Pos.CENTER);
        box.setPrefSize(100, 100);
        //box.setId("DEFAULT");
        return box;
    }

    VBox crearEspaciosConAsiento(int columnas, int filas, Image imagenAsiento, Long idAsiento) {
        VBox vBox = new VBox();
        vBox.setPrefSize(100, 100);
        vBox.setAlignment(Pos.CENTER);
        vBox.setId("" + idAsiento);
        ImageView imageView = new ImageView(imagenAsiento);
        imageView.maxHeight(100);
        imageView.maxWidth(100);
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);
        Label col = new Label(columnas + " ");
        col.setAlignment(Pos.CENTER);
        col.setPrefSize(20, 20);
        Label fil = new Label(String_Filas(filas));
        fil.setAlignment(Pos.CENTER);
        fil.setPrefSize(20, 20);
        HBox box = new HBox();
        box.setPrefSize(20, 20);
        box.setAlignment(Pos.CENTER);
        box.setId("" + idAsiento);
        box.getChildren().addAll(fil, col);
        vBox.getChildren().addAll(imageView,box);

        return vBox;
    }
    
        private void cambiarIdioma(String pIdioma) throws IOException {
        Idioma idiom = new Idioma(pIdioma);
        Properties prop = idiom.getProperties(pIdioma);
        btnAgregarFactura.setText(prop.getProperty("btnAgregarFactura"));

    }
}
