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
import java.util.Optional;

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
    private static final String ARCHIVO = "producciones.dat";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("=== INICIALIZANDO CONTROLADOR ===");
        
        cmbTipo.getItems().addAll("Película", "Serie");
        cmbTipo.setValue("Película");

        // Configurar columnas
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
        
        // CARGAR DATOS AL INICIAR
        cargarDelArchivo();
        
        cmbTipo.setOnAction(e -> ajustarCamposSegunTipo());
        ajustarCamposSegunTipo();
        
        System.out.println("Controlador inicializado. Producciones en memoria: " + crud.getContador());
    }

    // ==================== GUARDAR PRODUCCIÓN ====================
    @FXML
    private void guardarProduccion(ActionEvent e) {
        try {
            String codigo = safe(txtCodigo);
            String titulo = safe(txtTitulo);
            String fecha = safe(txtFecha);
            String duracionStr = safe(txtDuracion);

            if (codigo.isEmpty() || titulo.isEmpty() || duracionStr.isEmpty()) {
                mostrarAlerta("Error", "Código, Título y Duración son obligatorios");
                return;
            }

            int duracion;
            try {
                duracion = Integer.parseInt(duracionStr);
            } catch (NumberFormatException ex) {
                mostrarAlerta("Error", "Duración debe ser un número");
                return;
            }

            Produccion p;
            Director[] dir = { new Director(1, "Desconocido", "N/A") };

            if (cmbTipo.getValue().equals("Película")) {
                String genero = safe(txtGenero);
                if (genero.isEmpty()) {
                    mostrarAlerta("Error", "Género es obligatorio para películas");
                    return;
                }
                p = new Pelicula(codigo, titulo, fecha, duracion, dir, genero);
            } else {
                String tempStr = safe(txtTemporadas);
                if (tempStr.isEmpty()) {
                    mostrarAlerta("Error", "Temporadas es obligatorio para series");
                    return;
                }
                int temporadas = Integer.parseInt(tempStr);
                p = new Serie(codigo, titulo, fecha, duracion, dir, temporadas);
            }

            crud.crear(p);
            actualizarTabla();
            guardarEnArchivo();
            limpiarCampos();
            mostrarAlerta("Éxito", "Producción guardada correctamente");
            System.out.println("Producción guardada: " + codigo);

        } catch (NumberFormatException ex) {
            mostrarAlerta("Error", "Verifique los datos numéricos");
        } catch (Exception ex) {
            mostrarAlerta("Error", "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
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
            mostrarAlerta("Error", "Producción NO encontrada con código: " + codigo);
            listaProducciones.clear();
            System.out.println("No se encontró: " + codigo);
        } else {
            listaProducciones.clear();
            listaProducciones.add(p);
            mostrarSeleccionEnFormulario(p);
            mostrarAlerta("Éxito", "Producción encontrada: " + p.getTitulo());
            System.out.println("Encontrada: " + codigo);
        }
    }

    // ==================== MODIFICAR PRODUCCIÓN ====================
    @FXML
    private void modificarProduccion(ActionEvent e) {
        // Buscar el original por código (que está en txtCodigo)
        String codigoOriginal = safe(txtCodigo);
        if (codigoOriginal.isEmpty()) {
            mostrarAlerta("Error", "Ingrese el código de la producción a modificar");
            return;
        }

        Produccion produccionOriginal = crud.buscar(codigoOriginal);
        if (produccionOriginal == null) {
            mostrarAlerta("Error", "No se encontró producción con código: " + codigoOriginal);
            return;
        }

        try {
            String codigo = safe(txtCodigo);
            String titulo = safe(txtTitulo);
            String fecha = safe(txtFecha);
            String duracionStr = safe(txtDuracion);

            if (titulo.isEmpty() || duracionStr.isEmpty()) {
                mostrarAlerta("Error", "Título y Duración son obligatorios");
                return;
            }

            int duracion = Integer.parseInt(duracionStr);
            Director[] dir = { new Director(1, "Desconocido", "N/A") };

            Produccion nuevo;
            if (produccionOriginal instanceof Pelicula) {
                String genero = safe(txtGenero);
                if (genero.isEmpty()) {
                    mostrarAlerta("Error", "Género es obligatorio");
                    return;
                }
                nuevo = new Pelicula(codigo, titulo, fecha, duracion, dir, genero);
            } else {
                String tempStr = safe(txtTemporadas);
                if (tempStr.isEmpty()) {
                    mostrarAlerta("Error", "Temporadas es obligatorio");
                    return;
                }
                int temporadas = Integer.parseInt(tempStr);
                nuevo = new Serie(codigo, titulo, fecha, duracion, dir, temporadas);
            }

            crud.modificar(codigoOriginal, nuevo);
            actualizarTabla();
            guardarEnArchivo();
            limpiarCampos();
            mostrarAlerta("Éxito", "Producción modificada correctamente");
            System.out.println("Modificada: " + codigoOriginal);

        } catch (NumberFormatException ex) {
            mostrarAlerta("Error", "Verifique los datos numéricos");
        } catch (Exception ex) {
            mostrarAlerta("Error", "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // ==================== ELIMINAR PRODUCCIÓN ====================
    @FXML
    private void eliminarProduccion(ActionEvent e) {
        String codigo = safe(txtCodigo);
        if (codigo.isEmpty()) {
            mostrarAlerta("Error", "Seleccione o ingrese el código a eliminar");
            return;
        }

        Produccion sel = crud.buscar(codigo);
        if (sel == null) {
            mostrarAlerta("Error", "No se encontró producción con código: " + codigo);
            return;
        }

        // Confirmar eliminación
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Eliminación");
        confirmacion.setHeaderText("¿Desea eliminar esta producción?");
        confirmacion.setContentText("Código: " + sel.getCodigo() + "\nTítulo: " + sel.getTitulo());

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            crud.eliminar(codigo);
            actualizarTabla();
            guardarEnArchivo();
            limpiarCampos();
            mostrarAlerta("Éxito", "Producción eliminada correctamente");
            System.out.println("Eliminada: " + codigo);
        } else {
            System.out.println("Eliminación cancelada");
        }
    }

    // ==================== LISTAR PRODUCCIONES ====================
    @FXML
    private void listarProducciones(ActionEvent e) {
        actualizarTabla();
        int total = listaProducciones.size();
        mostrarAlerta("Información", "Total de producciones: " + total);
        System.out.println("Total en lista: " + total);
    }

    // ==================== SELECCIONAR DE TABLA ====================
    @FXML
    private void mostrarSeleccion(MouseEvent e) {
        Produccion p = tblProducciones.getSelectionModel().getSelectedItem();
        if (p == null) return;
        mostrarSeleccionEnFormulario(p);
    }

    private void mostrarSeleccionEnFormulario(Produccion p) {
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

    // ==================== MÉTODOS DE ARCHIVO ====================
    private void guardarEnArchivo() {
        try {
            Produccion[] arr = crud.getProducciones();
            crud.guardarArchivo(arr, arr.length, ARCHIVO);
            System.out.println("Archivo guardado: " + ARCHIVO);
        } catch (Exception e) {
            System.out.println("Error al guardar archivo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void cargarDelArchivo() {
        try {
            Produccion[] cargados = crud.leerArchivo(ARCHIVO);
            
            if (cargados != null && cargados.length > 0) {
                crud.setLista(java.util.Arrays.asList(cargados));
                actualizarTabla();
                System.out.println("Cargadas " + cargados.length + " producciones del archivo");
            } else {
                System.out.println("Archivo vacío o no existe. Iniciando con lista nueva.");
            }
        } catch (Exception e) {
            System.out.println("No hay archivo previo: " + e.getMessage());
        }
    }

    @FXML
    public void cargarArchivo(ActionEvent event) {
        cargarDelArchivo();
        mostrarAlerta("Éxito", "Datos cargados desde archivo");
    }

    @FXML
    public void guardarArchivo(ActionEvent event) {
        guardarEnArchivo();
        mostrarAlerta("Éxito", "Datos guardados en archivo");
    }

    @FXML
    public void salirApp(ActionEvent event) {
        guardarEnArchivo();
        System.out.println("Aplicación cerrada");
        System.exit(0);
    }

    // ==================== MÉTODOS AUXILIARES ====================
    private void actualizarTabla() {
        listaProducciones.setAll(crud.listarTodas());
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