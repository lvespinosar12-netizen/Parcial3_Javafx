package co.edu.poli.Parcial3.Servicios;

import java.util.List;

import co.edu.poli.Parcial3.Modelo.Produccion;

/**
 * Interfaz para manejar operaciones de archivo 
 * relacionadas con objetos de tipo Produccion (Películas y Series).
 * 
 * Esta interfaz define métodos para serializar, deserializar y leer
 * información desde archivos.
 */
public interface OperacionArchivo {

    /**
     * Serializa un arreglo de producciones y lo guarda en un archivo binario.
     *
     * @param producciones Arreglo de Produccion a guardar
     * @param cantidad     Número de producciones válidas dentro del arreglo
     * @param nombreArchivo Nombre del archivo donde se guardará
     * @return Mensaje indicando si la operación fue exitosa o no
     */
    String guardarArchivo(Produccion[] producciones, int cantidad, String nombreArchivo);

    /**
     * Deserializa un archivo binario y carga las producciones en un arreglo.
     *
     * @param nombreArchivo Nombre del archivo a leer
     * @return Arreglo de Produccion cargado desde archivo, o null si ocurre un error
     */
    Produccion[] leerArchivo(String nombreArchivo);

    /**
     * Método opcional para mostrar o inspeccionar los datos del archivo.
     *
     * @param nombreArchivo Nombre del archivo a mostrar
     */
    void mostrarArchivo(String nombreArchivo);

	void guardar(Produccion lista);

	List<Produccion> cargar();

}

