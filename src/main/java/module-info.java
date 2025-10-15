module pl.ejdev.medic {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires kotlin.stdlib;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires ktfx.layouts;
    requires kotlinx.coroutines.core;
    requires ktfx.jfoenix.layouts;
    requires org.apache.poi.ooxml;

    opens pl.ejdev.medic to javafx.fxml;
    opens pl.ejdev.medic.view to javafx.fxml;
    opens pl.ejdev.medic.utils to javafx.fxml;
    opens pl.ejdev.medic.components to javafx.fxml;
    opens pl.ejdev.medic.controller to javafx.fxml;
    opens pl.ejdev.medic.model to javafx.fxml;
    opens pl.ejdev.medic.service to javafx.fxml;

    exports pl.ejdev.medic;
    exports pl.ejdev.medic.view;
    exports pl.ejdev.medic.components;
    exports pl.ejdev.medic.controller;
    exports pl.ejdev.medic.model;
    exports pl.ejdev.medic.service;
    exports pl.ejdev.medic.utils;
}

