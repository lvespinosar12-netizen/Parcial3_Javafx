module co.edu.poli.Parcial3 {
    requires javafx.controls;
    requires javafx.fxml;

    opens co.edu.poli.Parcial3 to javafx.fxml;
    opens co.edu.poli.Parcial3.Controller to javafx.fxml;
    opens co.edu.poli.Parcial3.Modelo to javafx.base, javafx.fxml;
    opens co.edu.poli.Parcial3.Servicios to javafx.fxml;

    exports co.edu.poli.Parcial3;
    exports co.edu.poli.Parcial3.Controller;
    exports co.edu.poli.Parcial3.Modelo;
    exports co.edu.poli.Parcial3.Servicios;
}