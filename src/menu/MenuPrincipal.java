
package menu;

import java.util.Scanner;

import memoria.GestorMemoria;
import modelo.GestorProcesos;

/**
 * Interfaz de línea de comandos del simulador.
 *
 * El menú recibe los gestores desde Main y los utiliza
 * para solicitar operaciones y mostrar resultados.
 */
public class MenuPrincipal {

    private final Scanner scanner = new Scanner(System.in);

    private final GestorProcesos gestorProcesos;
    private final GestorMemoria gestorMemoria;

    private boolean ejecutando = true;

    /**
     * Recibe los gestores compartidos del simulador.
     */
    public MenuPrincipal(
        GestorProcesos gestorProcesos,
        GestorMemoria gestorMemoria
    ) {
        this.gestorProcesos = gestorProcesos;
        this.gestorMemoria = gestorMemoria;
    }

    /**
     * Mantiene activo el menú hasta que el usuario
     * seleccione la opción de salida.
     */
    public void iniciar() {

        while (ejecutando) {
            mostrarMenu();

            int opcion = leerEntero("Seleccione una opción: ");

            procesarOpcion(opcion);
        }

        scanner.close();
        System.out.println("\nSimulador finalizado.");
    }

    /**
     * Imprime las opciones disponibles.
     */
    private void mostrarMenu() {

        System.out.println("\n========================================");
        System.out.println("   SIMULADOR DE FUNCIONES DEL KERNEL");
        System.out.println("========================================");
        System.out.println("1. Crear proceso");
        System.out.println("2. Consultar memoria RAM");
        System.out.println("3. Planificación Round Robin [pendiente]");
        System.out.println("4. Gestión de concurrencia [pendiente]");
        System.out.println("5. Simulación de Deadlock [pendiente]");
        System.out.println("6. Listar procesos (PCB)");
        System.out.println("7. Consultar estado general");
        System.out.println("0. Salir");
        System.out.println("========================================");
    }

    /**
     * Interpreta la opción seleccionada.
     */
    private void procesarOpcion(int opcion) {

        switch (opcion) {
            case 1:
                crearProceso();
                break;

            case 2:
                gestorMemoria.mostrarEstado();
                break;

            case 3:
                mostrarPendiente("Planificación Round Robin");
                break;

            case 4:
                mostrarPendiente("Gestión de concurrencia");
                break;

            case 5:
                mostrarPendiente("Simulación de Deadlock");
                break;

            case 6:
                gestorProcesos.mostrarProcesos();
                break;

            case 7:
                consultarEstadoGeneral();
                break;

            case 0:
                ejecutando = false;
                break;

            default:
                System.out.println(
                    "Opción inválida. Seleccione una opción del menú."
                );
        }
    }

    /**
     * Solicita los datos y pide al gestor que cree
     * un proceso con su correspondiente PCB.
     */
    private void crearProceso() {

        System.out.println("\n--- CREACIÓN DE PROCESO ---");

        int memoriaMB = leerEntero(
            "Memoria que necesita el proceso (MB): "
        );

        int rafagaCPU = leerEntero(
            "Ráfaga total de CPU (unidades): "
        );

        // El gestor valida los valores y reserva la memoria.
        gestorProcesos.crearProceso(memoriaMB, rafagaCPU);

        // Mostramos el resultado actualizado de la RAM.
        gestorMemoria.mostrarEstado();
    }

    /**
     * Muestra los contadores actuales del simulador.
     */
    private void consultarEstadoGeneral() {

        System.out.println("\n--- ESTADO GENERAL ---");
        System.out.println(
            "Procesos registrados: "
            + gestorProcesos.getCantidadProcesos()
        );
        System.out.println(
            "RAM utilizada: "
            + gestorMemoria.getMemoriaUtilizadaMB() + " MB"
        );
        System.out.println(
            "RAM disponible: "
            + gestorMemoria.getMemoriaDisponibleMB() + " MB"
        );
    }

    /**
     * Lee un número entero y vuelve a preguntar si
     * el usuario escribe texto u otro formato inválido.
     *
     * @param mensaje texto que se muestra antes de leer.
     * @return el entero introducido.
     */
    private int leerEntero(String mensaje) {

        while (true) {
            System.out.print(mensaje);

            String entrada = scanner.nextLine().trim();

            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println(
                    "Entrada inválida. Ingrese un número entero."
                );
            }
        }
    }

    /**
     * Identifica las funciones que todavía no se han
     * implementado para no presentar resultados ficticios.
     */
    private void mostrarPendiente(String modulo) {

        System.out.println(
            "\nEl módulo \"" + modulo
            + "\" se implementará en una fase posterior."
        );
    }
}
