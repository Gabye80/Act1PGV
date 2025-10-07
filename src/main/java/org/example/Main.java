package org.example;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;


/**
 * Clase principal que actúa como proceso padre.
 * Se encarga de lanzar un proceso hijo por cada fichero de entrada
 * y coordina la lectura de resultados parciales para generar un informe final.
 */
public class Main {
    /**
     * Punto de entrada del programa.
     * Lanza procesos hijos para procesar archivos y luego genera un informe global.
     * @param args argumentos de línea de comandos (no se utilizan).
     */
    public static void main(String[] args) {

        File dirPadre = new File("C:\\Users\\Invitados s\\Desktop\\pgva01ut1");
        String[] contenidoPadre = dirPadre.list((dir, name) -> name.startsWith("datos") && name.endsWith(".txt"));

        if (contenidoPadre == null | contenidoPadre.length == 0) {
            System.err.println("No se encontraron ficheros de entrada.");
            return;
        }

        Map<Integer, String> procesosMap = new HashMap<>();
        for (String nombre : contenidoPadre) {
            int id = Integer.parseInt(nombre.replaceAll("\\D+", ""));
            String pathArchivo = dirPadre.getAbsolutePath() + File.separator + nombre;
            procesosMap.put(id, pathArchivo);
        }

        List<Process> procesos = new ArrayList<>();

        // Lanzar procesos hijos
        for (Map.Entry<Integer, String> entry : procesosMap.entrySet()) {
            int id = entry.getKey();
            String path = entry.getValue();

            try {
                // Classpath dinámico
                String classPath = System.getProperty("java.class.path");

                ProcessBuilder pb = new ProcessBuilder(
                        "java", "-cp", classPath, "org.example.ProcesoHijo",
                        String.valueOf(id), path
                );

                pb.directory(dirPadre);
                pb.redirectError(new File("error-" + id + ".log"));

                Process p = pb.start();
                procesos.add(p);
                System.out.println("Lanzado proceso hijo con id=" + id + " y archivo=" + path);

            } catch (IOException e) {
                System.err.println("Error al crear proceso hijo con id=" + id + ": " + e.getMessage());
            }
        }

        // Esperar a que todos terminen
        for (Process p : procesos) {
            try {
                p.waitFor();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Informe final
        System.out.println("\n=== INFORME FINAL ===");
        int totalVocalesGlobal = 0;
        int totalPalabrasGlobal = 0;

        for (int id : procesosMap.keySet()) {
            try {
                int vocales = leerEnteroDeFichero(new File(dirPadre, "vocales-" + id + ".res").getPath());
                int palabras = leerEnteroDeFichero(new File(dirPadre, "palabras-" + id + ".res").getPath());

                totalVocalesGlobal += vocales;
                totalPalabrasGlobal += palabras;

                System.out.println("Hijo " + id + ": " + palabras + " palabras, " + vocales + " vocales.");
            } catch (IOException e) {
                System.err.println("Error leyendo resultados del hijo " + id + ": " + e.getMessage());
            }
        }

        if (totalPalabrasGlobal > 0) {
            double promedio = (double) totalVocalesGlobal / totalPalabrasGlobal;
            System.out.printf(Locale.US,
                    "\nTotal de vocales: %d\nTotal de palabras: %d\nPromedio de vocales por palabra: %.2f\n",
                    totalVocalesGlobal, totalPalabrasGlobal, promedio);
        } else {
            System.out.println("No se procesaron palabras válidas.");
        }
    }

    /**
     * Lee un número entero desde un fichero de texto.
     * @param nombre ruta del fichero.
     * @return el número entero leído.
     * @throws IOException si ocurre un error de lectura.
     */
    public static int leerEnteroDeFichero(String nombre) throws IOException {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(nombre), StandardCharsets.UTF_8))) {
            return Integer.parseInt(br.readLine().trim());
        }
    }
}
