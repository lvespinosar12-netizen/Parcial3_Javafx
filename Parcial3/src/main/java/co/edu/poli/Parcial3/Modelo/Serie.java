package co.edu.poli.Parcial3.Modelo;

import java.io.Serializable;

public class Serie extends Produccion implements Serializable {

    private static final long serialVersionUID = 2024L;
    
    private int numeroDeTemporadas;

    public Serie(String codigo, String titulo, String fechaEstreno, int duracion, 
                 Director[] directores, int numeroDeTemporadas) {
        super(codigo, titulo, fechaEstreno, duracion, directores);
        this.numeroDeTemporadas = numeroDeTemporadas;
    }

    public int getNumeroDeTemporadas() {
        return numeroDeTemporadas;
    }

    public void setNumeroDeTemporadas(int numeroDeTemporadas) {
        this.numeroDeTemporadas = numeroDeTemporadas;
    }

    @Override
    public String toString() {
        return super.toString() + ", Temporadas: " + numeroDeTemporadas;
    }
}