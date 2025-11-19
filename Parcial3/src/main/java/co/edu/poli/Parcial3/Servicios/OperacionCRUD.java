package co.edu.poli.Parcial3.Servicios;

import co.edu.poli.Parcial3.Modelo.Produccion;

public interface OperacionCRUD {

    /**
     * Crea una nueva producción (Película o Serie)
     */
    void crear(Produccion p);

    /**
     * Lee una producción por código
     */
    Produccion leer(String codigo);

    /**
     * Actualiza una producción existente
     */
    void actualizar(String codigo, Produccion nueva);

    /**
     * Elimina una producción por código
     */
    void eliminar(String codigo);

    /**
     * Lista todas las producciones
     */
    void listar();
}
