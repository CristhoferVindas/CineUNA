/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package cr.ac.una.cineuna.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import cr.ac.una.cineuna.idioma.Idioma;
import cr.ac.una.cineuna.model.AlimentoDto;
import cr.ac.una.cineuna.model.AlimentoxfacturaDto;
import cr.ac.una.cineuna.model.AsientoDto;
import cr.ac.una.cineuna.model.AsientoxtandaDto;
import cr.ac.una.cineuna.model.FacturaDto;
import cr.ac.una.cineuna.model.TandaDto;
import cr.ac.una.cineuna.model.UsuarioDto;
import cr.ac.una.cineuna.service.AlimentoService;
import cr.ac.una.cineuna.service.AlimentoxfacturaService;
import cr.ac.una.cineuna.service.AsientoxtandaService;
import cr.ac.una.cineuna.service.FacturaService;
import cr.ac.una.cineuna.util.AppContext;
import cr.ac.una.cineuna.util.Formato;
import cr.ac.una.cineuna.util.Mensaje;
import cr.ac.una.cineuna.util.Respuesta;
import cr.ac.una.cineuna.util.TableViewFacturas;
import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.Format;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author Cristhofer
 */
public class FacturaViewController extends Controller implements Initializable {

    @FXML
    private TableView tbvEntradas;
    @FXML
    private TableView tbvAlimentos;
    @FXML
    private JFXDatePicker dtpFechaFactura;
    @FXML
    private JFXTimePicker tmpHoraFactura;

    FacturaDto facturaDto;
    TandaDto tandaDto;
    UsuarioDto usuarioDto;
    @FXML
    private TableColumn<TableViewFacturas, String> tbcPrecio;
    @FXML
    private TableColumn<TableViewFacturas, String> tbcSala;
    @FXML
    private TableColumn<TableViewFacturas, String> tbcPelicula;
    @FXML
    private TableColumn<TableViewFacturas, LocalDate> tbcFecha;
    @FXML
    private TableColumn<TableViewFacturas, String> tbcHora;
    @FXML
    private TableColumn<TableViewFacturas, String> tbcFila;
    @FXML
    private TableColumn<TableViewFacturas, String> tbcColumna;
    ObservableList<AsientoDto> asientosReservados;
    @FXML
    private JFXButton BtnConfirnarCompra;
    @FXML
    private Label lblprecioTotal;
    Long precioTotal;
    @FXML
    private ChoiceBox<AlimentoDto> chbAlimentos;
    @FXML
    private JFXButton BtnAgregarAlimento;
    ObservableList<AlimentoxfacturaDto> alimentosList = FXCollections.observableArrayList();
    @FXML
    private TableColumn<AlimentoxfacturaDto, String> tbcNombre;
    @FXML
    private TableColumn<AlimentoxfacturaDto, String> tbcCantidad;
    @FXML
    private TableColumn<AlimentoxfacturaDto, String> tbcPrecioAlimento;
    @FXML
    private JFXTextField txtCantidad;
    Boolean alimentoactualizado;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @Override
    public void initialize() {
        usuarioDto = new UsuarioDto();
        usuarioDto = (UsuarioDto) AppContext.getInstance().get("Usuario");
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
        tbvEntradas.getItems().clear();
        txtCantidad.setTextFormatter(Formato.getInstance().letrasFormat(100));
        tandaDto = new TandaDto();
        tandaDto = (TandaDto) AppContext.getInstance().get("TandaActual");
        facturaDto = new FacturaDto();
        facturaDto = (FacturaDto) AppContext.getInstance().get("FacturaActual");
        asientosReservados = (ObservableList<AsientoDto>) AppContext.getInstance().get("AsientosReservados");
        precioTotal = asientosReservados.size() * tandaDto.getCostoEntrada();
        lblprecioTotal.setText("Precio total: " + precioTotal);
        cargarAlimentos();
        FacturaService facturaService = new FacturaService();
        dtpFechaFactura.setValue(LocalDate.now());
        tmpHoraFactura.setValue(LocalTime.now());

        tbcPrecio.setCellValueFactory(c -> c.getValue().precio);
        tbcSala.setCellValueFactory(c -> c.getValue().nombreSala);
        tbcPelicula.setCellValueFactory(c -> c.getValue().peliculanombreEspañol);
        tbcFecha.setCellValueFactory(c -> c.getValue().fecha);
        tbcHora.setCellValueFactory(c -> c.getValue().horainicio);
        tbcFila.setCellValueFactory(c -> c.getValue().fila);
        tbcColumna.setCellValueFactory(c -> c.getValue().numero);

        tbcNombre.setCellValueFactory((TableColumn.CellDataFeatures<AlimentoxfacturaDto, String> p) -> p.getValue().id);
        tbcNombre.setCellFactory((TableColumn<AlimentoxfacturaDto, String> p) -> new ButtonCell());

        tbcPrecioAlimento.setCellValueFactory(c -> c.getValue().precio);
        tbcCantidad.setCellValueFactory(c -> c.getValue().cantidad);

        Respuesta respuesta = facturaService.getdDetalleFactura(tandaDto.getId());

        if (respuesta.getEstado()) {
            ObservableList<TableViewFacturas> resultadoFactura = FXCollections.observableArrayList((List<TableViewFacturas>) respuesta.getResultado("Facturas"));
            ObservableList<TableViewFacturas> entradas = FXCollections.observableArrayList();
            for (TableViewFacturas rf : resultadoFactura) {
                for (AsientoDto asientosReservado : asientosReservados) {
                    if (asientosReservado.getFila().equals(rf.getFila()) && asientosReservado.getNumero() == Long.valueOf(rf.getNumero())) {
                        entradas.add(rf);
                    }
                }
            }
            tbvEntradas.setItems(entradas);
        }
    }

    @FXML
    private void OnActionBtnConfirmarCompra(ActionEvent event) throws MessagingException, FileNotFoundException, IOException {

        FacturaService facturaService = new FacturaService();

        facturaDto.setFecha(dtpFechaFactura.getValue());
        facturaDto.setHora(tmpHoraFactura.getValue().toString());
        facturaDto.setPrecioTotal(precioTotal);
        facturaDto.setIdUsuario(usuarioDto.getId());
        facturaDto.getAlimentoxFactura().addAll(tbvAlimentos.getItems());

        Respuesta respuesta = facturaService.guardarFactura(facturaDto);
        if (respuesta.getEstado()) {
            File portadasDir = new File("..\\CineUNA\\Reportes"); //"C:\\Users\\Cristhofer\\Desktop\\UNA\\CineUNA(Cliente-Servidor)\\WsCineUNA\\Imagenes\\+"
            if (!portadasDir.exists()) {
                portadasDir.mkdir();
                portadasDir.setWritable(true);
            }
            facturaDto = (FacturaDto) respuesta.getResultado("Factura");
            respuesta = facturaService.getComprobante(facturaDto.getId());
            if (respuesta.getEstado()) {
                String pdf = (String) respuesta.getResultado("Facturas");
                byte[] pdfDecode = Base64.getDecoder().decode(pdf);
                File pdfFile = new File("..\\CineUNA\\Reportes\\Comprobante.pdf");
                FileOutputStream pdfOutputStream = new FileOutputStream(pdfFile);
                pdfOutputStream.write(pdfDecode);
                pdfOutputStream.close();
            }
            respuesta = facturaService.getComprobanteAlimento(facturaDto.getId());
            if (respuesta.getEstado()) {
                String pdf = (String) respuesta.getResultado("Facturas");
                byte[] pdfDecode = Base64.getDecoder().decode(pdf);
                File pdfFile = new File("..\\CineUNA\\Reportes\\ComprobanteAlimento.pdf");
                FileOutputStream pdfOutputStream = new FileOutputStream(pdfFile);
                pdfOutputStream.write(pdfDecode);
                pdfOutputStream.close();
            }
            enviarCorreo();
        }

    }

    @FXML
    private void OnActionBtnAgregarAlimento(ActionEvent event) {
        alimentoactualizado = false;
        AlimentoxfacturaDto alimentoxfacturaDto = new AlimentoxfacturaDto();
        alimentoxfacturaDto.setPrecio(chbAlimentos.getValue().getPrecio());
        alimentoxfacturaDto.setCantidad(Long.valueOf(txtCantidad.getText()));
        alimentoxfacturaDto.setIdAlimento(chbAlimentos.getValue().getId());

        AlimentoxfacturaService alimentoService = new AlimentoxfacturaService();
        Respuesta respuesta = alimentoService.guardarAlimentoxfactura(alimentoxfacturaDto);
        if (respuesta.getEstado()) {

            alimentoxfacturaDto = (AlimentoxfacturaDto) respuesta.getResultado("Alimentoxfactura");
            alimentoxfacturaDto.setIdAlimento(alimentoxfacturaDto.getId());
            alimentoxfacturaDto.setId(chbAlimentos.getValue().getId());
            precioTotal += chbAlimentos.getValue().getPrecio() * Long.valueOf(txtCantidad.getText());
            lblprecioTotal.setText("Precio total: " + precioTotal);
            for (AlimentoxfacturaDto alimentoxfacturaDto1 : alimentosList) {
                if (alimentoxfacturaDto1.getId().equals(chbAlimentos.getValue().getId())) {
                    alimentoxfacturaDto1.setCantidad(alimentoxfacturaDto1.getCantidad() + Long.valueOf(txtCantidad.getText()));
                    alimentoactualizado = true;
                }
            }
            if (!alimentoactualizado) {
                alimentosList.add(alimentoxfacturaDto);
            }
            tbvAlimentos.setItems(alimentosList);
            tbvAlimentos.refresh();
        }

    }

    void cargarAlimentos() {
        AlimentoService alimentoService = new AlimentoService();
        Respuesta respuesta = alimentoService.getAlimentos();
        if (respuesta.getEstado()) {

            ObservableList<AlimentoDto> alimentos = FXCollections.observableArrayList((List<AlimentoDto>) respuesta.getResultado("Alimentos"));
            chbAlimentos.setItems(alimentos);
            chbAlimentos.getValue();
        }

    }

    private class ButtonCell extends TableCell<AlimentoxfacturaDto, String> {

        Label Nombre = new Label();

        ButtonCell() {

        }

        protected void updateItem(String t, boolean empty) {
            super.updateItem(t, empty);
            if (!empty) {

                AlimentoService alimentoService = new AlimentoService();
                Respuesta respuesta = alimentoService.getAlimento(Long.valueOf(t));
                if (respuesta.getEstado()) {
                    AlimentoDto alimentoDto = (AlimentoDto) respuesta.getResultado("Alimento");
                    Nombre.setText(alimentoDto.getNombre());
                    setGraphic(Nombre);
                }
            }
        }
    }

    void enviarCorreo() throws MessagingException {
        try {
            Properties propiedades = new Properties();
            propiedades.setProperty("mail.smtp.host", "smtp.gmail.com");
            propiedades.setProperty("mail.smtp.starttls.enable", "true");
            propiedades.setProperty("mail.smtp.port", "587");
            propiedades.setProperty("mail.smtp.auth", "true");

            Session sesion = Session.getDefaultInstance(propiedades);
            String correo_emisor = "cineuna32@gmail.com";
            String contraseña_emisor = "wysyrjtzhpgxiuqp";

            String correo_receptor = usuarioDto.getCorreo();
            String asunto = "Comprobante de Compra";

            BodyPart Comprobante = new MimeBodyPart();
            Comprobante.setDataHandler(new DataHandler(new FileDataSource("..\\CineUNA\\Reportes\\Comprobante.pdf")));
            Comprobante.setFileName("Comprobante.pdf");
            BodyPart ComprobanteAlimento = new MimeBodyPart();
            ComprobanteAlimento.setDataHandler(new DataHandler(new FileDataSource("..\\CineUNA\\Reportes\\ComprobanteAlimento.pdf")));
            ComprobanteAlimento.setFileName("ComprobanteAlimento.pdf");

            MimeMultipart multiParte = new MimeMultipart();
            multiParte.addBodyPart(Comprobante);
            multiParte.addBodyPart(ComprobanteAlimento);

            MimeMessage message = new MimeMessage(sesion);
            message.setFrom(new InternetAddress(correo_emisor));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(correo_receptor));
            message.setSubject(asunto);
            message.setContent(multiParte);

            Transport transporte = sesion.getTransport("smtp");
            transporte.connect(correo_emisor, contraseña_emisor);
            transporte.sendMessage(message, message.getAllRecipients());
            transporte.close();

            new Mensaje().showModal(Alert.AlertType.INFORMATION, "Factura", getStage(), "Correo enviado correctamente.");
            File ComprobanteAlimentoPdf = new File("..\\CineUNA\\Reportes\\ComprobanteAlimento.pdf");
            if (ComprobanteAlimentoPdf.exists()) {
                ComprobanteAlimentoPdf.delete();
            }
            File ComprobantePdf = new File("..\\CineUNA\\Reportes\\Comprobante.pdf");
            if (ComprobantePdf.exists()) {
                ComprobantePdf.delete();
            }
        } catch (AddressException ex) {
            new Mensaje().showModal(Alert.AlertType.ERROR, "Factura", getStage(), "Error al enviar el correo");
        }

    }

    private void cambiarIdioma(String pIdioma) throws IOException {
        Idioma idiom = new Idioma(pIdioma);
        Properties prop = idiom.getProperties(pIdioma);
        tbcPelicula.setText(prop.getProperty("tbcPeliculaFactura"));
        tbcPrecio.setText(prop.getProperty("tbcPrecioFactura"));
        tbcSala.setText(prop.getProperty("tbcSalaFactura"));
        tbcFecha.setText(prop.getProperty("tbcFechaFactura"));
        tbcHora.setText(prop.getProperty("tbcHoraFactura"));
        tbcFila.setText(prop.getProperty("tbcFilaFactura"));
        tbcColumna.setText(prop.getProperty("tbcColumnaFactura"));

        txtCantidad.setPromptText(prop.getProperty("txtCantidadFactura"));
        BtnAgregarAlimento.setText(prop.getProperty("BtnAgregarAlimentoFactura"));

        tbcNombre.setText(prop.getProperty("tbcNombreFactura"));
        tbcPrecioAlimento.setText(prop.getProperty("tbcPrecioAlimentoFactura"));
        tbcCantidad.setText(prop.getProperty("tbcCantidadFactura"));

        lblprecioTotal.setText(prop.getProperty("FlblprecioTotalactura"));
        BtnConfirnarCompra.setText(prop.getProperty("BtnConfirnarCompraFactura"));
    }
    public void confirmarSalida(Event e) throws MessagingException, IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Confirmar Salida", new ButtonType("Continuar Comprando"), new ButtonType("Facturar"), new ButtonType("Salir"));
        alert.setTitle("Confirmar Salida");
        alert.setHeaderText(null);
        alert.initOwner(getStage());
        alert.setContentText("¿Esta seguro que desea salir?.");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == alert.getButtonTypes().get(0)) {
            e.consume();

        } else if (result.get() == alert.getButtonTypes().get(1)) {
            OnActionBtnConfirmarCompra(null);
        } else if (result.get() == alert.getButtonTypes().get(2)) {
            AsientoxtandaService asientoService = new AsientoxtandaService();
            Respuesta respuesta = asientoService.getAsientosxtandas();

            if (respuesta.getEstado()) {
                
                for (AsientoxtandaDto asientoxtandaDto1 : facturaDto.getAsientosxTandas()) {
                    asientoService.eliminarAsientoxtanda(asientoxtandaDto1.getId());
                }
            }
            AlimentoxfacturaService alimentoxfacturaService = new AlimentoxfacturaService();
           
            for (AlimentoxfacturaDto alimentoxfacturaDto : alimentosList) {
                alimentoxfacturaService.eliminarAlimentoxfactura(alimentoxfacturaDto.getId());
            }
        }
    }
}
