module cr.ac.una.cineuna {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires com.jfoenix;
    requires jakarta.ws.rs;
    requires jakarta.xml.bind;
    requires java.sql;
    requires jakarta.json;
    requires java.base;
    requires javafx.media;
    requires javafx.web;
    requires jakarta.mail;
    requires javafx.swing;
    requires javafx.graphics;
    requires javafx.base;
   
    opens cr.ac.una.cineuna to javafx.fxml,javafx.graphics, javafx.web, javafx.media, javafx.controls, jakarta.mail, javafx.swing, javafx.base;
    opens cr.ac.una.cineuna.controller to javafx.fxml,javafx.controls,com.jfoenix, javafx.media, jakarta.mail, javafx.swing, javafx.base, javafx.web;
    opens cr.ac.una.cineuna.util;
    exports cr.ac.una.cineuna.util;
    exports cr.ac.una.cineuna.model;
}