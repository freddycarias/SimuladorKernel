
package menu;

import java.util.Scanner;

import memoria.GestorMemoria;
import modelo.GestorProcesos;
import modelo.Proceso;
import planificacion.PlanificadorRoundRobin;

/**
 * Menu principal de la aplicacion.
 * Permite administrar procesos, memoria y planificacion.
 */
public class MenuPrincipal {

    private final Scanner scanner;
    private final GestorProcesos gestorProcesos;
    private final GestorMemoria gestorMemoria;
    private final PlanificadorRoundRobin planificador;

    /**
     * Recibe los componentes que utiliza el menu.
     */
    public MenuPrincipal(
            GestorProcesos gestorProcesos,
            GestorMemoria gestorMemoria,
            PlanificadorRoundRobin planificador) {

        this.scanner = new Scanner(System.in);
        this.gestorProcesos = gestorProcesos;
        this.gestorMemoria = gestorMemoria;
        this.planificador = planificador;
    }

    /**
     * Mantiene el menu activo hasta que el usuario elija salir.
     */
    public void iniciar() {
        int opcion;

        do {
            mostrarMenu();
            opcion = leerEntero("Seleccione una opcion: ");

            switch (opcion) {
                case 1:
                    crearProceso();
                    break;

                case 2:
                    consultarMemoria();
                    break;

                case 3:
                    ejecutarRoundRobin();
                    break;

                case 4:
                    mostrarPendiente("Gestion de concurrencia");
                    break;

                case 5:
                    mostrarPendiente("Deteccion y resolucion de deadlock");
                    break;

                case 6:
                    gestorProcesos.mostrarProcesos();
                    break;

                case 7:
                    consultarEstadoGeneral();
                    break;

                case 0:
                    System.out.println("Cerrando el simulador...");
                    break;

                default:
                    System.out.println("Opcion no valida.");
            }

        } while (opcion != 0);
    }

    /**
     * Muestra las opciones disponibles.
     */
    private void mostrarMenu() {
        System.out.println("\n====================================");
        System.out.println(" SIMULADOR DE FUNCIONES DEL KERNEL");
        System.out.println("====================================");
        System.out.println("1. Crear proceso");
        System.out.println("2. Consultar memoria RAM");
        System.out.println("3. Ejecutar planificacion Round Robin");
        System.out.println("4. Gestion de concurrencia (pendiente)");
        System.out.println("5. Deadlock (pendiente)");
        System.out.println("6. Listar procesos");
        System.out.println("7. Estado general del sistema");
        System.out.println("0. Salir");
        System.out.println("====================================");
    }

    /**
     * Solicita los datos y crea un proceso.
     */
    private void crearProceso() {
        System.out.println("\n--- CREAR PROCESO ---");

        int memoriaMB = leerEntero(
                "Memoria requerida en MB: ");

        int rafagaCPU = leerEntero(
                "Rafaga total de CPU en unidades: ");

        Proceso proceso =
                gestorProcesos.crearProceso(memoriaMB, rafagaCPU);

        if (proceso != null) {
            System.out.println(
                    "Proceso creado correctamente con PID "
                    + proceso.getPid());
        }

        gestorMemoria.mostrarEstado();
    }

    /**
     * Ejecuta la planificacion Round Robin.
     */
    private void ejecutarRoundRobin() {
        System.out.println("\nIniciando planificacion...");
        planificador.ejecutarPlanificacion();
    }

    /**
     * Muestra el estado de la memoria simulada.
     */
    private void consultarMemoria() {
        System.out.println("\n--- ESTADO DE MEMORIA RAM ---");
        gestorMemoria.mostrarEstado();
    }

    /**
     * Muestra informacion general del sistema.
     */
    private void consultarEstadoGeneral() {
        System.out.println("\n--- ESTADO GENERAL DEL SISTEMA ---");
        System.out.println(
                "Procesos registrados: "
                + gestorProcesos.getCantidadProcesos());

        gestorMemoria.mostrarEstado();
    }

    /**
     * Indica que una funcion se implementara en una fase posterior.
     */
    private void mostrarPendiente(String funcionalidad) {
        System.out.println("\n" + funcionalidad);
        System.out.println(
                "Esta funcionalidad se implementara en una fase posterior.");
    }

    /**
     * Lee un entero y vuelve a solicitarlo si la entrada no es valida.
     */
    private int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine().trim();

            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println(
                        "Entrada no valida. Introduzca un numero entero.");
            }
        }
    }
}
