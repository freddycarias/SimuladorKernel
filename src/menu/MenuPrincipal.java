
package menu;

import java.util.Scanner;

import memoria.GestorMemoria;
import modelo.GestorProcesos;
import modelo.Proceso;
import planificacion.PlanificadorRoundRobin;
import concurrencia.GestorConcurrencia;

/**
 * Menu principal del SimuladorKernel.
 */
public class MenuPrincipal {

    private final Scanner scanner;
    private final GestorProcesos gestorProcesos;
    private final GestorMemoria gestorMemoria;
    private final PlanificadorRoundRobin planificador;
    private final GestorConcurrencia gestorConcurrencia;

    /**
     * Constructor que recibe los componentes del sistema.
     */
    public MenuPrincipal(
            GestorProcesos gestorProcesos,
            GestorMemoria gestorMemoria,
            PlanificadorRoundRobin planificador,
            GestorConcurrencia gestorConcurrencia) {

        this.scanner = new Scanner(System.in);
        this.gestorProcesos = gestorProcesos;
        this.gestorMemoria = gestorMemoria;
        this.planificador = planificador;
        this.gestorConcurrencia = gestorConcurrencia;
    }

    /**
     * Inicia el menu principal.
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
                    planificador.ejecutarPlanificacion();
                    break;

                case 4:
                    menuConcurrencia();
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
     * Muestra las opciones del menu principal.
     */
    private void mostrarMenu() {
        System.out.println("\n====================================");
        System.out.println(" SIMULADOR DE FUNCIONES DEL KERNEL");
        System.out.println("====================================");
        System.out.println("1. Crear proceso");
        System.out.println("2. Consultar memoria RAM");
        System.out.println("3. Ejecutar planificacion Round Robin");
        System.out.println("4. Gestion de concurrencia");
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
     * Submenu para administrar la concurrencia.
     */
    private void menuConcurrencia() {
        int opcion;

        do {
            System.out.println("\n================================");
            System.out.println(" GESTION DE CONCURRENCIA");
            System.out.println("================================");
            System.out.println("1. Solicitar acceso al semaforo");
            System.out.println("2. Modificar variable compartida");
            System.out.println("3. Liberar semaforo");
            System.out.println("4. Consultar estado de concurrencia");
            System.out.println("0. Volver al menu principal");
            System.out.println("================================");

            opcion = leerEntero("Seleccione una opcion: ");

            switch (opcion) {
                case 1:
                    solicitarAcceso();
                    break;

                case 2:
                    modificarVariable();
                    break;

                case 3:
                    liberarSemaforo();
                    break;

                case 4:
                    gestorConcurrencia.mostrarEstado();
                    break;

                case 0:
                    System.out.println(
                            "Regresando al menu principal...");
                    break;

                default:
                    System.out.println("Opcion no valida.");
            }

        } while (opcion != 0);
    }

    /**
     * Permite que un proceso solicite el semaforo.
     */
    private void solicitarAcceso() {
        System.out.println("\n--- SOLICITAR ACCESO ---");

        gestorProcesos.mostrarProcesos();

        int pid = leerEntero("Ingrese el PID del proceso: ");

        Proceso proceso = gestorProcesos.buscarPorPID(pid);

        if (proceso == null) {
            System.out.println("No existe un proceso con ese PID.");
            return;
        }

        gestorConcurrencia.solicitarAcceso(proceso);
    }

    /**
     * Permite modificar la variable compartida.
     */
    private void modificarVariable() {
        System.out.println("\n--- MODIFICAR VARIABLE ---");

        int pid = leerEntero("Ingrese el PID del proceso: ");

        Proceso proceso = gestorProcesos.buscarPorPID(pid);

        if (proceso == null) {
            System.out.println("Proceso no encontrado.");
            return;
        }

        int cantidad = leerEntero(
                "Cantidad que desea sumar a la variable: ");

        gestorConcurrencia.modificarVariableCompartida(
                proceso, cantidad);
    }

    /**
     * Permite que el proceso propietario libere el semaforo.
     */
    private void liberarSemaforo() {
        System.out.println("\n--- LIBERAR SEMAFORO ---");

        int pid = leerEntero("Ingrese el PID del proceso: ");

        Proceso proceso = gestorProcesos.buscarPorPID(pid);

        if (proceso == null) {
            System.out.println("Proceso no encontrado.");
            return;
        }

        gestorConcurrencia.liberarAcceso(proceso);
    }

    /**
     * Consulta el estado de la memoria RAM.
     */
    private void consultarMemoria() {
        System.out.println("\n--- ESTADO DE MEMORIA RAM ---");
        gestorMemoria.mostrarEstado();
    }

    /**
     * Muestra el estado general del sistema.
     */
    private void consultarEstadoGeneral() {
        System.out.println("\n--- ESTADO GENERAL DEL SISTEMA ---");

        System.out.println(
                "Procesos registrados: "
                + gestorProcesos.getCantidadProcesos());

        gestorMemoria.mostrarEstado();
        gestorConcurrencia.mostrarEstado();
    }

    /**
     * Informa que una funcionalidad esta pendiente.
     */
    private void mostrarPendiente(String funcionalidad) {
        System.out.println("\n" + funcionalidad);
        System.out.println(
                "Esta funcionalidad se implementara en una fase posterior.");
    }

    /**
     * Lee un numero entero y valida la entrada.
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
