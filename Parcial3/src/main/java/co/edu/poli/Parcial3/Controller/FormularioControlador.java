package co.edu.poli.Parcial3.Controller;

import co.edu.poli.Parcial3.Modelo.Pelicula;
import co.edu.poli.Parcial3.Modelo.Produccion;
import co.edu.poli.Parcial3.Modelo.Serie;
import co.edu.poli.Parcial3.Modelo.Director;
import co.edu.poli.Parcial3.Servicios.ImplementacionOperacionCRUD;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class FormularioControlador implements Initializable {

    @FXML private TextField txtCodigo;
    @FXML private TextField txtTitulo;
    @FXML private TextField txtFecha;
    @FXML private TextField txtDuracion;
    @FXML private TextField txtGenero;
    @FXML private TextField txtTemporadas;

    @FXML private ComboBox<String> cmbTipo;

    @FXML private TableView<Produccion> tblProducciones;
    @FXML private TableColumn<Produccion, String> colCodigo;
    @FXML private TableColumn<Produccion, String> colTitulo;
    @FXML private TableColumn<Produccion, String> colFecha;
    @FXML private TableColumn<Produccion, Integer> colDuracion;
    @FXML private TableColumn<Produccion, String> colTipo;
    @FXML private TableColumn<Produccion, String> colExtra;

    private final ObservableList<Produccion> listaProducciones = FXCollections.observableArrayList();
    private final ImplementacionOperacionCRUD crud = new ImplementacionOperacionCRUD();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmbTipo.getItems().addAll("Película", "Serie");
        cmbTipo.setValue("Película");

        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaDeEstreno"));
        colDuracion.setCellValueFactory(new PropertyValueFactory<>("duracion"));

        colTipo.setCellValueFactory(cell -> {
            Produccion p = cell.getValue();
            String tipo = (p instanceof Pelicula) ? "Película" :
                           (p instanceof Serie) ? "Serie" : "Producción";
            return new SimpleStringProperty(tipo);
        });

        colExtra.setCellValueFactory(cell -> {
            Produccion p = cell.getValue();
            if (p instanceof Pelicula) {
                return new SimpleStringProperty(((Pelicula)p).getGenero());
            }
            if (p instanceof Serie) {
                return new SimpleStringProperty("T: " + ((Serie)p).getNumeroDeTemporadas());
            }
            return new SimpleStringProperty("");
        });

        tblProducciones.setItems(listaProducciones);
        listaProducciones.setAll(crud.getLista());

        cmbTipo.setOnAction(e -> ajustarCamposSegunTipo());
    }

    @FXML
    private void guardarProduccion(ActionEvent e) {
        try {
            String codigo = safe(txtCodigo);
            String titulo = safe(txtTitulo);
            String fecha = safe(txtFecha);
            int duracion = Integer.parseInt(safe(txtDuracion));

            if (codigo.isEmpty() || titulo.isEmpty()) {
                mostrarAlerta("Error", "Código y Título son obligatorios");
                return;
            }

            Produccion p;

            if (cmbTipo.getValue().equals("Película")) {
                String genero = safe(txtGenero);
                if (genero.isEmpty()) {
                    mostrarAlerta("Error", "Género es obligatorio para películas");
                    return;
                }
                Director[] dir = { new Director(1, "Desconocido", "N/A") };
                p = new Pelicula(codigo, titulo, fecha, duracion, dir, genero);
            } else {
                int temporadas = Integer.parseInt(safe(txtTemporadas));
                Director[] dir = { new Director(1, "Desconocido", "N/A") };
                p = new Serie(codigo, titulo, fecha, duracion, dir, temporadas);
            }

            crud.crear(p);
            listaProducciones.setAll(crud.getLista());
            limpiarCampos();
            mostrarAlerta("Éxito", "Guardado correctamente.");

        } catch (NumberFormatException ex) {
            mostrarAlerta("Error", "Duración y Temporadas deben ser números");
        } catch (Exception ex) {
            mostrarAlerta("Error", ex.getMessage());
        }
    }

    @FXML
    private void modificarProduccion(ActionEvent e) {
        Produccion seleccionado = tblProducciones.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Error", "Seleccione un elemento");
            return;
        }
        try {
            String codigo = safe(txtCodigo);
            String titulo = safe(txtTitulo);
            String fecha = safe(txtFecha);
            int duracion = Integer.parseInt(safe(txtDuracion));

            Produccion nuevo;
            if (seleccionado instanceof Pelicula) {
                nuevo = new Pelicula(codigo, titulo, fecha, duracion,
                        new Director[]{ new Director(1,"Desconocido","N/A") },
                        safe(txtGenero));
            } else {
                nuevo = new Serie(codigo, titulo, fecha, duracion,
                        new Director[]{ new Director(1,"Desconocido","N/A") },
                        Integer.parseInt(safe(txtTemporadas)));
            }

            crud.modificar(seleccionado.getCodigo(), nuevo);
            listaProducciones.setAll(crud.getLista());
            limpiarCampos();
            mostrarAlerta("Éxito", "Modificado correctamente.");

        } catch (Exception x) {
            mostrarAlerta("Error", x.getMessage());
        }
    }

    @FXML
    private void eliminarProduccion(ActionEvent e) {
        Produccion sel = tblProducciones.getSelectionModel().getSelectedItem();
        if (sel == null) {
            mostrarAlerta("Error", "Seleccione un elemento");
            return;
        }
        crud.eliminar(sel.getCodigo());
        listaProducciones.setAll(crud.getLista());
        mostrarAlerta("Éxito", "Eliminado correctamente.");
    }

    @FXML
    private void listarProducciones(ActionEvent e) {
        listaProducciones.setAll(crud.listarTodas());
    }

    @FXML
    private void buscarPorCodigo(ActionEvent e) {
        String codigo = safe(txtCodigo);
        if (codigo.isEmpty()) {
            mostrarAlerta("Error", "Ingrese un código para buscar");
            return;
        }
        Produccion p = crud.buscar(codigo);
        if (p == null) {
            mostrarAlerta("Error", "No encontrado");
            listaProducciones.clear();
        } else {
            listaProducciones.clear();
            listaProducciones.add(p);
        }
    }

    @FXML
    private void mostrarSeleccion(MouseEvent e) {
        Produccion p = tblProducciones.getSelectionModel().getSelectedItem();
        if (p == null) return;

        txtCodigo.setText(p.getCodigo());
        txtTitulo.setText(p.getTitulo());
        txtFecha.setText(p.getFechaDeEstreno());
        txtDuracion.setText(String.valueOf(p.getDuracion()));

        if (p instanceof Pelicula) {
            cmbTipo.setValue("Película");
            txtGenero.setText(((Pelicula)p).getGenero());
            txtTemporadas.clear();
        } else {
            cmbTipo.setValue("Serie");
            txtTemporadas.setText(String.valueOf(((Serie)p).getNumeroDeTemporadas()));
            txtGenero.clear();
        }
        ajustarCamposSegunTipo();
    }

    @FXML
    public void guardarArchivo(ActionEvent event) {
        try {
            if (listaProducciones.isEmpty()) {
                mostrarAlerta("Advertencia", "No hay elementos para guardar");
                return;
            }
            Produccion[] arr = new Produccion[listaProducciones.size()];
            arr = listaProducciones.toArray(arr);
            
            String resultado = crud.guardarArchivo(arr, arr.length, "producciones.dat");
            mostrarAlerta("Éxito", resultado);
            System.out.println("Archivo guardado exitosamente");
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al guardar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void cargarArchivo(ActionEvent event) {
        try {
            Produccion[] cargados = crud.leerArchivo("producciones.dat");
            
            if (cargados != null && cargados.length > 0) {
                listaProducciones.clear();
                for (Produccion p : cargados) {
                    if (p != null) {
                        listaProducciones.add(p);
                    }
                }
                mostrarAlerta("Éxito", "Se cargaron " + cargados.length + " producciones correctamente.");
                System.out.println("Archivo cargado exitosamente");
            } else {
                mostrarAlerta("Advertencia", "No hay datos en el archivo o no existe");
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al cargar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void salirApp(ActionEvent event) {
        System.exit(0);
    }

    private void ajustarCamposSegunTipo() {
        boolean esP = cmbTipo.getValue().equals("Película");
        txtGenero.setDisable(!esP);
        txtGenero.setVisible(esP);
        txtTemporadas.setDisable(esP);
        txtTemporadas.setVisible(!esP);
    }

    private void limpiarCampos() {
        txtCodigo.clear();
        txtTitulo.clear();
        txtFecha.clear();
        txtDuracion.clear();
        txtGenero.clear();
        txtTemporadas.clear();
    }

    private String safe(TextField t) {
        return t.getText() == null ? "" : t.getText().trim();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(titulo);
        a.setHeaderText(null);
        a.setContentText(mensaje);
        a.showAndWait();
    }
}