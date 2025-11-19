package co.edu.poli.Parcial3.Servicios;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import co.edu.poli.Parcial3.Modelo.Produccion;

public class ImplementacionOperacionCRUD implements OperacionCRUD {

    private List<Produccion> producciones;
    private static final String ARCHIVO_DEFECTO = "producciones.dat";

    public ImplementacionOperacionCRUD() {
        producciones = new ArrayList<>();
    }

    public ImplementacionOperacionCRUD(int tamano) {
        producciones = new ArrayList<>();
    }

    @Override
    public void crear(Produccion p) {
        if (p != null) {
            producciones.add(p);
            System.out.println("Producción creada con éxito.");
        }
    }

    @Override
    public Produccion leer(String codigo) {
        for (Produccion p : producciones) {
            if (p.getCodigo().equals(codigo)) {
                return p;
            }
        }
        System.out.println("Producción no encontrada.");
        return null;
    }

    @Override
    public void actualizar(String codigo, Produccion nueva) {
        for (int i = 0; i < producciones.size(); i++) {
            if (producciones.get(i).getCodigo().equals(codigo)) {
                producciones.set(i, nueva);
                System.out.println("Producción actualizada.");
                return;
            }
        }
        System.out.println("No se encontró la producción a actualizar.");
    }

    @Override
    public void eliminar(String codigo) {
        for (int i = 0; i < producciones.size(); i++) {
            if (producciones.get(i).getCodigo().equals(codigo)) {
                producciones.remove(i);
                System.out.println("Producción eliminada.");
                return;
            }
        }
        System.out.println("No se encontró la producción a eliminar.");
    }

    @Override
    public void listar() {
        if (producciones.isEmpty()) {
            System.out.println("No hay producciones registradas.");
            return;
        }

        for (int i = 0; i < producciones.size(); i++) {
            System.out.println((i + 1) + ". " + producciones.get(i));
        }
    }

    public Produccion[] getProducciones() {
        return producciones.toArray(new Produccion[0]);
    }

    public int getContador() {
        return producciones.size();
    }

    public List<Produccion> getLista() {
        return new ArrayList<>(producciones);
    }

    public void modificar(String codigo, Produccion nueva) {
        actualizar(codigo, nueva);
    }

    public List<Produccion> listarTodas() {
        return new ArrayList<>(producciones);
    }

    public Produccion buscar(String codigo) {
        return leer(codigo);
    }

    public void setLista(List<Produccion> lista) {
        producciones.clear();
        producciones.addAll(lista);
    }

    // ==================== SERIALIZACIÓN ====================

    public String guardarArchivo(Produccion[] arr, int cantidad, String nombreArchivo) {
        try {
            FileOutputStream fos = new FileOutputStream(nombreArchivo);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            // Guardar el número de producciones
            oos.writeInt(cantidad);

            // Guardar cada producción
            for (int i = 0; i < cantidad; i++) {
                oos.writeObject(arr[i]);
            }

            oos.close();
            fos.close();

            System.out.println("Guardando " + cantidad + " producciones en " + nombreArchivo);
            return "Archivo guardado correctamente en: " + nombreArchivo;
        } catch (IOException e) {
            System.out.println("Error al guardar: " + e.getMessage());
            return "Error al guardar: " + e.getMessage();
        }
    }

    public Produccion[] leerArchivo(String nombreArchivo) {
        try {
            File archivo = new File(nombreArchivo);
            if (!archivo.exists()) {
                System.out.println("El archivo no existe: " + nombreArchivo);
                return new Produccion[0];
            }

            FileInputStream fis = new FileInputStream(nombreArchivo);
            ObjectInputStream ois = new ObjectInputStream(fis);

            // Leer el número de producciones
            int cantidad = ois.readInt();

            // Leer cada producción
            Produccion[] producciones = new Produccion[cantidad];
            for (int i = 0; i < cantidad; i++) {
                producciones[i] = (Produccion) ois.readObject();
            }

            ois.close();
            fis.close();

            System.out.println("Leyendo archivo: " + nombreArchivo);
            return producciones;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al leer: " + e.getMessage());
            return null;
        }
    }

    public void mostrarArchivo(String nombreArchivo) {
        System.out.println("Contenido del archivo: " + nombreArchivo);
        try {
            Produccion[] arr = leerArchivo(nombreArchivo);
            if (arr != null && arr.length > 0) {
                for (int i = 0; i < arr.length; i++) {
                    if (arr[i] != null) {
                        System.out.println((i + 1) + ". " + arr[i]);
                    }
                }
            } else {
                System.out.println("El archivo está vacío.");
            }
        } catch (Exception e) {
            System.out.println("Error al mostrar archivo: " + e.getMessage());
        }
    }

    // Método conveniente para guardar con nombre de archivo por defecto
    public void guardarEnArchivoPorDefecto() {
        Produccion[] arr = getProducciones();
        guardarArchivo(arr, arr.length, ARCHIVO_DEFECTO);
    }

    // Método conveniente para cargar desde archivo por defecto
    public void cargarDesdeArchivoPorDefecto() {
        Produccion[] arr = leerArchivo(ARCHIVO_DEFECTO);
        if (arr != null) {
            producciones.clear();
            for (Produccion p : arr) {
                if (p != null) {
                    producciones.add(p);
                }
            }
        }
    }
}