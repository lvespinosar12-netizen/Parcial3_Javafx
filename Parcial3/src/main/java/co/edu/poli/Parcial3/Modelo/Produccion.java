package co.edu.poli.Parcial3.Modelo;

import java.io.Serializable;

public abstract class Produccion implements Serializable {

    private static final long serialVersionUID = 2024L;
    
    private String codigo;
    private String titulo;
    private String fechaEstreno;
    private int duracion;
    private Director[] directores;

    public Produccion(String codigo, String titulo, String fechaEstreno, int duracion, Director[] directores) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.fechaEstreno = fechaEstreno;
        this.duracion = duracion;
        this.directores = directores;
    }

    public String getCodigo() { 
        return codigo; 
    }
    
    public String getTitulo() { 
        return titulo; 
    }
    
    public String getFechaEstreno() { 
        return fechaEstreno; 
    }
    
    public String getFechaDeEstreno() {
        return fechaEstreno;
    }
    
    public int getDuracion() { 
        return duracion; 
    }
    
    public Director[] getDirectores() { 
        return directores; 
    }

    public void setCodigo(String codigo) { 
        this.codigo = codigo; 
    }
    
    public void setTitulo(String titulo) { 
        this.titulo = titulo; 
    }
    
    public void setFechaEstreno(String fechaEstreno) { 
        this.fechaEstreno = fechaEstreno; 
    }
    
    public void setDuracion(int duracion) { 
        this.duracion = duracion; 
    }
    
    public void setDirectores(Director[] directores) { 
        this.directores = directores; 
    }

    @Override
    public String toString() {
        return "Código: " + codigo +
               ", Título: " + titulo +
               ", Fecha: " + fechaEstreno +
               ", Duración: " + duracion + " min";
    }

    public int size() {
        return 1;
    }

    public Produccion[] toArray(Produccion[] producciones) {
        Produccion[] result = new Produccion[1];
        result[0] = this;
        return result;
    }
}