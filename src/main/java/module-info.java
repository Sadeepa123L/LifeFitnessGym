module lk.ijse.lifefitnessgym {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires static lombok;
    requires mysql.connector.j;
    requires net.sf.jasperreports.core;


    opens lk.ijse.lifefitnessgym to javafx.fxml;
    opens lk.ijse.lifefitnessgym.dto.tm to javafx.base;

    exports lk.ijse.lifefitnessgym;
    exports lk.ijse.lifefitnessgym.controller;
    opens lk.ijse.lifefitnessgym.controller to javafx.fxml;
}