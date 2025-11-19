package co.edu.poli.Parcial3.Modelo;

import java.io.Serializable;

public class Pelicula extends Produccion implements Serializable {

    private static final long serialVersionUID = 2024L;
    
    private String genero;

    public Pelicula(String codigo, String titulo, String fechaEstreno, int duracion, 
                    Director[] directores, String genero) {
        super(codigo, titulo, fechaEstreno, duracion, directores);
        this.genero = genero;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    @Override
    public String toString() {
        return super.toString() + ", Género: " + genero;
    }
}