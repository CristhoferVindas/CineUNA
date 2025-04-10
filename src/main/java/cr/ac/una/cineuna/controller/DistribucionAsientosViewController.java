package cr.ac.una.cineuna.controller;

import com.jfoenix.controls.JFXButton;
import cr.ac.una.cineuna.idioma.Idioma;
import cr.ac.una.cineuna.model.AsientoDto;
import cr.ac.una.cineuna.model.SalaDto;
import cr.ac.una.cineuna.model.UsuarioDto;
import cr.ac.una.cineuna.service.AsientoService;
import cr.ac.una.cineuna.service.SalaService;
import cr.ac.una.cineuna.util.AppContext;
import cr.ac.una.cineuna.util.Mensaje;
import cr.ac.una.cineuna.util.Respuesta;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
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
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
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
public class DistribucionAsientosViewController extends Controller implements Initializable {

    @FXML
    private JFXButton btnAgregarFila;
    @FXML
    private ScrollPane sclEspacioAsientos;
    @FXML
    private JFXButton btnAgregarColumna;
    @FXML
    private ImageView imgAsiento;
    UsuarioDto usuarioDto;

    GridPane grid = new GridPane();
    AsientoDto asientoDto;
    SalaDto salaDto;

    int ultima_fila;
    int ultima_columna;

    Boolean asiento_creado = false;

    @FXML
    private JFXButton btnGuardar;
    @FXML
    private BorderPane root;

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

    }

    void CrearEspacios() {
        asientoDto = new AsientoDto();
        salaDto = new SalaDto();
        salaDto = (SalaDto) AppContext.getInstance().get("SalaSeleccionada");
        ultima_columna = 0;
        ultima_fila = 0;
        grid = new GridPane();
        InputStream inputStream = null;
        byte[] imageFondoB64 = Base64.getDecoder().decode(salaDto.getImagenFondo());
        //  imgFondo.setImage(new Image(inputStream));
        Image imagenFondo = new Image(new ByteArrayInputStream(imageFondoB64));

        BackgroundImage image = new BackgroundImage(imagenFondo,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(grid.getWidth(), grid.getHeight(), true, true, true, true));

        byte[] imagenasientoB64 = Base64.getDecoder().decode(salaDto.getImagenAsiento());
        Image imagenAsiento = new Image(new ByteArrayInputStream(imagenasientoB64));
        imgAsiento.setImage(imagenAsiento);
        imgAsiento.maxHeight(100);
        imgAsiento.maxWidth(100);
        grid.setGridLinesVisible(true);
        grid.setBackground(new Background(image));//new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        if (!salaDto.getAsientos().isEmpty()) {
            for (AsientoDto asiento : salaDto.getAsientos()) {
                if (Int_Filas(asiento.getFila()) > ultima_fila) {
                    ultima_fila = Int_Filas(asiento.getFila());
                }
                if (asiento.getNumero() > ultima_columna) {
                    ultima_columna = asiento.getNumero().intValue();
                }

            }
        }
        for (int i = 0; i <= ultima_columna; i++) {
            for (int j = 0; j <= ultima_fila; j++) {
                asiento_creado = false;
                if (!salaDto.getAsientos().isEmpty()) {
                    for (AsientoDto asi : salaDto.getAsientos()) {
                        if (Int_Filas(asi.getFila()) == j && asi.getNumero() == i) {

                            grid.add(crearEspaciosConAsiento(i, j, imgAsiento.getImage(), asi.getId()), i, j);
                            asiento_creado = true;
                        }
                    }
                    if (asiento_creado == false) {
                        grid.add(crearEspaciosVacios(i, j), i, j);
                        asiento_creado = true;
                    }

                } else {
                    grid.add(crearEspaciosVacios(i, j), i, j);
                }
            }
        }

        sclEspacioAsientos.setContent(grid);

        grid.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent t) {
                if (t.getGestureSource() != grid
                        && t.getDragboard().hasImage()) {
                    t.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                t.consume();
            }
        });

        grid.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent t) {
                Dragboard db = t.getDragboard();

                boolean success = false;
                Node node = t.getPickResult().getIntersectedNode();
                if (!(node instanceof VBox)) {
                node = node.getParent();
                if (node instanceof Label) {
                  node = node.getParent();
                }
                if (node instanceof ImageView) {
                    node = node.getParent();
                }
                if (node instanceof HBox) {
                    node = node.getParent();
                }
            }
                if (!node.getId().equals("DEFAULT")) {
                    if (usuarioDto.getIdioma().equals("E")) {
                        new Mensaje().showModal(Alert.AlertType.ERROR, "Guardar asiento", getStage(), "Este asiento ya está creado");
                    } else if (usuarioDto.getIdioma().equals("I")) {
                        new Mensaje().showModal(Alert.AlertType.ERROR, "Save seat", getStage(), "This seat is already created");
                    }
                } else {
                    if (db.hasImage()) {

                        int columnas = GridPane.getColumnIndex(node);
                        int filas = GridPane.getRowIndex(node);

                        asientoDto.setFila(String_Filas(filas));
                        asientoDto.setNumero(Long.valueOf(columnas));
                        asientoDto.setIdsala(salaDto.getId());

                        AsientoService asientoService = new AsientoService();

                        Respuesta respuesta = asientoService.guardarAsiento(asientoDto);
                        if (!respuesta.getEstado()) {

                            if (usuarioDto.getIdioma().equals("E")) {
                                new Mensaje().showModal(Alert.AlertType.ERROR, "Guardar asiento", getStage(), respuesta.getMensaje());
                            } else if (usuarioDto.getIdioma().equals("I")) {
                                new Mensaje().showModal(Alert.AlertType.ERROR, "Save seat", getStage(), respuesta.getMensaje());
                            }
                        } else {
                            asientoDto = (AsientoDto) respuesta.getResultado("Asiento");
                            asientoDto.setModificado(true);
                            if (grid.getChildren().remove(node)) {
                            }

                            grid.add(crearEspaciosConAsiento(columnas, filas, imgAsiento.getImage(), asientoDto.getId()), columnas, filas);
                            salaDto.getAsientos().add(asientoDto);

                        }
                        asientoDto = new AsientoDto();
                        success = true;
                    }
                }

                t.setDropCompleted(success);
                t.consume();
            }
        });
        grid.setOnMouseClicked((t) -> {
            Node node = t.getPickResult().getIntersectedNode();
            int columnas = GridPane.getColumnIndex(node);
            int filas = GridPane.getRowIndex(node);
            if (!(node instanceof VBox)) {
                node = node.getParent();
                
                 if (node instanceof ImageView) {
                    node = node.getParent();
                }
                if (node instanceof Label) {
                    node = node.getParent();
                }
                if (node instanceof HBox) {
                    node = node.getParent();
                }
            }

            if (!node.getId().equals("DEFAULT")) {
                if (usuarioDto.getIdioma().equals("E")) {
                    if (new Mensaje().showConfirmation("Borrar Asiento", getStage(), "¿Esta seguro que desea borrar el asiento de la posision:" + String_Filas(filas) + columnas + " ?")) {
                        if (grid.getChildren().remove(node)) {
                            AsientoService asientoService = new AsientoService();
                            asientoService.eliminarAsiento(Long.valueOf(node.getId()));

                            grid.add(crearEspaciosVacios(columnas, filas), columnas, filas);
                        }
                    }
                } else if (usuarioDto.getIdioma().equals("I")) {
                    if (new Mensaje().showConfirmation("Delete seat", getStage(), "Are you sure you want to delete the seat? Position : " + String_Filas(filas) + columnas)) {
                        if (grid.getChildren().remove(node)) {
                            AsientoService asientoService = new AsientoService();
                            asientoService.eliminarAsiento(Long.valueOf(node.getId()));

                            grid.add(crearEspaciosVacios(columnas, filas), columnas, filas);
                        }
                    }
                }
            }
        });

        imgAsiento.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                Dragboard db = imgAsiento.startDragAndDrop(TransferMode.COPY_OR_MOVE);
                WritableImage wi = new WritableImage(50, 50);

                ClipboardContent content = new ClipboardContent();
                content.putImage(imgAsiento.snapshot(null, wi));
                db.setContent(content);

                t.consume();
            }
        });
        salaDto.getAsientos().clear();
    }

    @FXML
    private void OnActionBtnAgregarFila(ActionEvent event) {
        ultima_fila++;
        for (int i = 0; i <= ultima_columna; i++) {
            grid.add(crearEspaciosVacios(i, ultima_fila), i, ultima_fila);
        }
    }

    @FXML
    private void OnActionBtnAgregarColumna(ActionEvent event) {
        ultima_columna++;
        for (int i = 0; i <= ultima_fila; i++) {
            grid.add(crearEspaciosVacios(ultima_columna, i), ultima_columna, i);
        }
    }

    @FXML
    private void OnActionBtnGuardar(ActionEvent event) {
        try {
            SalaService service = new SalaService();
            Respuesta respuesta = service.guardarSala(salaDto);
            if (!respuesta.getEstado()) {
                if (usuarioDto.getIdioma().equals("E")) {
                    new Mensaje().showModal(Alert.AlertType.ERROR, "Guardar sala", getStage(), respuesta.getMensaje());
                } else if (usuarioDto.getIdioma().equals("I")) {
                    new Mensaje().showModal(Alert.AlertType.ERROR, "Save room", getStage(), respuesta.getMensaje());
                }
            } else {
                salaDto = (SalaDto) respuesta.getResultado("Sala");
                service = new SalaService();
                respuesta = service.getSala(salaDto.getId());
                if (respuesta.getEstado()) {
                    salaDto = (SalaDto) respuesta.getResultado("Sala");
                    salaDto.getAsientos().clear();
                } else {
                    if (usuarioDto.getIdioma().equals("E")) {
                        new Mensaje().showModal(Alert.AlertType.ERROR, "Cargar sala", getStage(), respuesta.getMensaje());
                    } else if (usuarioDto.getIdioma().equals("I")) {
                        new Mensaje().showModal(Alert.AlertType.ERROR, "Room", getStage(), respuesta.getMensaje());
                    }
                }
                if (usuarioDto.getIdioma().equals("E")) {
                    new Mensaje().showModal(Alert.AlertType.INFORMATION, "Guardar sala", getStage(), "Sala actualizada correctamente.");
                } else if (usuarioDto.getIdioma().equals("I")) {
                    new Mensaje().showModal(Alert.AlertType.INFORMATION, "Save", getStage(), "Successfully updated.");
                }

            }
        } catch (Exception ex) {
            if (usuarioDto.getIdioma().equals("E")) {
                Logger.getLogger(RegistroSalaViewController.class.getName()).log(Level.SEVERE, "Error guardando el sala.", ex);
                new Mensaje().showModal(Alert.AlertType.ERROR, "Guardar sala", getStage(), "Ocurrio un error guardando el sala.");
            } else if (usuarioDto.getIdioma().equals("I")) {
                Logger.getLogger(RegistroSalaViewController.class.getName()).log(Level.SEVERE, "Failed to save.", ex);
                new Mensaje().showModal(Alert.AlertType.ERROR, "Room", getStage(), "Failed to save.");
            }
        }
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

    VBox crearEspaciosVacios(int columnas, int filas) {
        VBox vBox = new VBox();
        vBox.setPrefSize(100, 100);
        vBox.setAlignment(Pos.CENTER);
        vBox.setId("DEFAULT");
        HBox box = new HBox();
        Label col = new Label(columnas + " ");
        col.setAlignment(Pos.CENTER);
        col.setPrefSize(20, 20);
        Label fil = new Label(String_Filas(filas));
        fil.setAlignment(Pos.CENTER);
        fil.setPrefSize(20, 20);
        box.setPrefSize(100, 100);
        box.setAlignment(Pos.CENTER);
        //box.setId("DEFAULT");
        box.getChildren().addAll(fil, col);
        vBox.getChildren().add(box);
        

        return vBox;
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
        HBox hbox = new HBox();
        hbox.setPrefSize(20, 20);
        hbox.setAlignment(Pos.CENTER);
        Background b = new Background(new BackgroundFill(Color.GREEN, new CornerRadii(10), new Insets(10)));
        Platform.runLater(() -> vBox.setBackground(b));
        hbox.setId("" + idAsiento);
        hbox.getChildren().addAll(fil, col);
        vBox.getChildren().addAll(imageView, hbox);
        return vBox;
    }

    public void confirmarSalida(Event e) {
        if (usuarioDto.getIdioma().equals("E")) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Confirmar Salida", new ButtonType("Continuar Editando"), new ButtonType("Guardar Cambios"), new ButtonType("Salir"));
            alert.setTitle("Confirmar Salida");
            alert.setHeaderText(null);
            alert.initOwner(getStage());
            alert.setContentText("¿Esta seguro que desea salir?.");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == alert.getButtonTypes().get(0)) {
                e.consume();

            } else if (result.get() == alert.getButtonTypes().get(1)) {
                OnActionBtnGuardar(null);
            } else if (result.get() == alert.getButtonTypes().get(2)) {
                AsientoService asientoService = new AsientoService();
                for (AsientoDto asientoDtos : salaDto.getAsientos()) {
                    asientoService.eliminarAsiento(asientoDtos.getId());
                }
            }
        } else if (usuarioDto.getIdioma().equals("I")) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "confirm departure", new ButtonType("Continue editing"), new ButtonType("Save"), new ButtonType("Exit"));
            alert.setTitle("Confirm departure");
            alert.setHeaderText(null);
            alert.initOwner(getStage());
            alert.setContentText("Do you want to go out?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == alert.getButtonTypes().get(0)) {
                e.consume();

            } else if (result.get() == alert.getButtonTypes().get(1)) {
                OnActionBtnGuardar(null);
            } else if (result.get() == alert.getButtonTypes().get(2)) {
                AsientoService asientoService = new AsientoService();
                for (AsientoDto asientoDtos : salaDto.getAsientos()) {
                    asientoService.eliminarAsiento(asientoDtos.getId());
                }
            }
        }
    }

    private void cambiarIdioma(String pIdioma) throws IOException {
        Idioma idiom = new Idioma(pIdioma);
        Properties prop = idiom.getProperties(pIdioma);
        btnGuardar.setText(prop.getProperty("btnGuardar"));

    }
}
