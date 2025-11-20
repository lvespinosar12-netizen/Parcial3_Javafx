package co.edu.poli.Parcial3;

//Laura

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = loadFXML("formulario");
        Scene scene = new Scene(root, 800, 600);

        stage.setTitle("Gestión de Producciones");
        stage.setScene(scene);
        stage.show();
    }

    private Parent loadFXML(String fxml) throws Exception {
        System.out.println("Buscando FXML en: " + App.class.getResource(fxml + ".fxml"));

        FXMLLoader loader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return loader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}

