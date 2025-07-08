import java.util.*;

public class LibretaDeNotas {
    private HashMap<String, ArrayList<Double>> calificaciones;
    private HashMap<String, Double> promedios;
    private HashMap<String, Double> notasMaximas;
    private HashMap<String, Double> notasMinimas;
    private double promedioGeneral;
    private final double NOTA_APROBACION = 4.0;
    private Scanner scanner;

    public LibretaDeNotas() {
        calificaciones = new HashMap<>();
        promedios = new HashMap<>();
        notasMaximas = new HashMap<>();
        notasMinimas = new HashMap<>();
        scanner = new Scanner(System.in);
    }

    public void ejecutar() {
        System.out.println("=== EVALUADOR DE CLASES ===");
        ingresarDatos();
        calcularEstadisticas();
        mostrarMenu();
    }

    private void ingresarDatos() {
        System.out.println("\n--- INGRESO DE DATOS ---");

        // Solicitar cantidad de alumnos
        int cantidadAlumnos = validarEnteroPositivo("Ingrese la cantidad de alumnos: ");

        // Solicitar cantidad de notas por alumno
        int cantidadNotas = validarEnteroPositivo("Ingrese la cantidad de notas por alumno: ");

        // Ingresar datos de cada alumno
        for (int i = 1; i <= cantidadAlumnos; i++) {
            System.out.println("\n--- ALUMNO " + i + " ---");
            System.out.print("Ingrese el nombre del estudiante: ");
            String nombre = scanner.nextLine().trim();

            // Validar que el nombre no esté vacío
            while (nombre.isEmpty()) {
                System.out.print("El nombre no puede estar vacío. Ingrese nuevamente: ");
                nombre = scanner.nextLine().trim();
            }

            ArrayList<Double> notas = new ArrayList<>();

            // Ingresar notas del estudiante
            for (int j = 1; j <= cantidadNotas; j++) {
                double nota = validarNota("Ingrese la nota " + j + " (1.0 - 7.0): ");
                notas.add(nota);
            }

            calificaciones.put(nombre, notas);
        }
    }

    private void calcularEstadisticas() {
        double sumaPromedios = 0.0;

        for (Map.Entry<String, ArrayList<Double>> entry : calificaciones.entrySet()) {
            String nombre = entry.getKey();
            ArrayList<Double> notas = entry.getValue();

            // Calcular promedio
            double suma = 0.0;
            for (double nota : notas) {
                suma += nota;
            }
            double promedio = suma / notas.size();
            promedios.put(nombre, promedio);
            sumaPromedios += promedio;

            // Calcular nota máxima
            double max = Collections.max(notas);
            notasMaximas.put(nombre, max);

            // Calcular nota mínima
            double min = Collections.min(notas);
            notasMinimas.put(nombre, min);
        }

        // Calcular promedio general del curso
        promedioGeneral = sumaPromedios / calificaciones.size();
    }

    private void mostrarMenu() {
        int opcion;

        do {
            System.out.println("\n=== MENÚ DE OPCIONES ===");
            System.out.println("1. Mostrar el Promedio de Notas por Estudiante");
            System.out.println("2. Mostrar si la Nota es Aprobatoria o Reprobatoria por Estudiante");
            System.out.println("3. Mostrar si la Nota está por Sobre o por Debajo del Promedio del Curso");
            System.out.println("0. Salir del Menú");
            System.out.print("Seleccione una opción: ");

            opcion = validarEntero();

            switch (opcion) {
                case 1:
                    mostrarPromedios();
                    break;
                case 2:
                    evaluarNotaAprobatoria();
                    break;
                case 3:
                    compararConPromedioCurso();
                    break;
                case 0:
                    System.out.println("¡Gracias por usar el Evaluador de Clases!");
                    break;
                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
            }
        } while (opcion != 0);
    }

    private void mostrarPromedios() {
        System.out.println("\n--- PROMEDIO DE NOTAS POR ESTUDIANTE ---");
        System.out.println("Promedio General del Curso: " + String.format("%.2f", promedioGeneral));
        System.out.println("Nota de Aprobación: " + NOTA_APROBACION);
        System.out.println();

        for (Map.Entry<String, Double> entry : promedios.entrySet()) {
            String nombre = entry.getKey();
            double promedio = entry.getValue();
            double notaMax = notasMaximas.get(nombre);
            double notaMin = notasMinimas.get(nombre);

            System.out.println("Estudiante: " + nombre);
            System.out.println("  Promedio: " + String.format("%.2f", promedio));
            System.out.println("  Nota Máxima: " + String.format("%.2f", notaMax));
            System.out.println("  Nota Mínima: " + String.format("%.2f", notaMin));
            System.out.println("  Estado: " + (promedio >= NOTA_APROBACION ? "APROBADO" : "REPROBADO"));
            System.out.println();
        }
    }

    private void evaluarNotaAprobatoria() {
        System.out.println("\n--- EVALUACIÓN DE NOTA APROBATORIA ---");
        String nombre = solicitarNombreEstudiante();

        if (nombre != null) {
            double nota = validarNota("Ingrese la nota a evaluar (1.0 - 7.0): ");

            System.out.println("\nResultado:");
            System.out.println("Estudiante: " + nombre);
            System.out.println("Nota: " + String.format("%.2f", nota));
            System.out.println("Estado: " + (nota >= NOTA_APROBACION ? "APROBATORIA" : "REPROBATORIA"));
            System.out.println("Nota de Aprobación: " + NOTA_APROBACION);
        }
    }

    private void compararConPromedioCurso() {
        System.out.println("\n--- COMPARACIÓN CON PROMEDIO DEL CURSO ---");
        String nombre = solicitarNombreEstudiante();

        if (nombre != null) {
            double nota = validarNota("Ingrese la nota a comparar (1.0 - 7.0): ");

            System.out.println("\nResultado:");
            System.out.println("Estudiante: " + nombre);
            System.out.println("Nota: " + String.format("%.2f", nota));
            System.out.println("Promedio del Curso: " + String.format("%.2f", promedioGeneral));

            if (nota > promedioGeneral) {
                System.out.println("La nota está POR SOBRE el promedio del curso");
            } else if (nota < promedioGeneral) {
                System.out.println("La nota está POR DEBAJO del promedio del curso");
            } else {
                System.out.println("La nota es IGUAL al promedio del curso");
            }
        }
    }

    private String solicitarNombreEstudiante() {
        System.out.print("Ingrese el nombre del estudiante: ");
        String nombre = scanner.nextLine().trim();

        if (!calificaciones.containsKey(nombre)) {
            System.out.println("Error: El estudiante '" + nombre + "' no se encuentra registrado.");
            System.out.println("Estudiantes registrados: " + calificaciones.keySet());
            return null;
        }

        return nombre;
    }

    private int validarEnteroPositivo(String mensaje) {
        int numero;
        do {
            System.out.print(mensaje);
            numero = validarEntero();
            if (numero <= 0) {
                System.out.println("Error: Debe ingresar un número entero positivo.");
            }
        } while (numero <= 0);
        return numero;
    }

    private int validarEntero() {
        while (!scanner.hasNextInt()) {
            System.out.print("Error: Debe ingresar un número entero válido. Intente nuevamente: ");
            scanner.next();
        }
        int numero = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer
        return numero;
    }

    private double validarNota(String mensaje) {
        double nota;
        do {
            System.out.print(mensaje);
            while (!scanner.hasNextDouble()) {
                System.out.print("Error: Debe ingresar un número decimal válido. Intente nuevamente: ");
                scanner.next();
            }
            nota = scanner.nextDouble();
            scanner.nextLine(); // Limpiar buffer

            if (nota < 1.0 || nota > 7.0) {
                System.out.println("Error: La nota debe estar entre 1.0 y 7.0");
            }
        } while (nota < 1.0 || nota > 7.0);
        return nota;
    }

    public static void main(String[] args) {
        LibretaDeNotas libreta = new LibretaDeNotas();
        libreta.ejecutar();
    }
}


