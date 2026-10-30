
package menu;

import java.util.Scanner;

import memoria.GestorMemoria;
import modelo.GestorProcesos;
import modelo.Proceso;
import planificacion.PlanificadorRoundRobin;
import concurrencia.GestorConcurrencia;
import deadlock.GestorDeadlock;

/**
 * Menu principal del SimuladorKernel.
 * Permite acceder a los diferentes modulos del simulador.
 */
public class MenuPrincipal {
    private final Scanner scanner;
    private final GestorProcesos gestorProcesos;
    private final GestorMemoria gestorMemoria;
    private final PlanificadorRoundRobin planificador;
    private final GestorConcurrencia gestorConcurrencia;
    private final GestorDeadlock gestorDeadlock;
    /**
     * Constructor que recibe los componentes del sistema.
     */
    public MenuPrincipal(
            GestorProcesos gestorProcesos,
            GestorMemoria gestorMemoria,
            PlanificadorRoundRobin planificador,
            GestorConcurrencia gestorConcurrencia,
            GestorDeadlock gestorDeadlock) {

        this.scanner = new Scanner(System.in);
        this.gestorProcesos = gestorProcesos;
        this.gestorMemoria = gestorMemoria;
        this.planificador = planificador;
        this.gestorConcurrencia = gestorConcurrencia;
        this.gestorDeadlock = gestorDeadlock;
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
                    menuDeadlock();
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
        System.out.println("5. Deteccion y resolucion de Deadlock");
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
     * Submenu para detectar y resolver situaciones de deadlock.
     */
    private void menuDeadlock() {
        int opcion;
        do {
            System.out.println("\n================================");
            System.out.println(" DETECCION Y RESOLUCION DE DEADLOCK");
            System.out.println("================================");
            System.out.println("1. Simular deadlock");
            System.out.println("2. Detectar deadlock");
            System.out.println("3. Resolver deadlock (abortar proceso)");
            System.out.println("4. Consultar estado de recursos");
            System.out.println("0. Volver al menu principal");
            System.out.println("================================");

            opcion = leerEntero("Seleccione una opcion: ");

            switch (opcion) {

                case 1:
                    simularDeadlock();
                    break;

                case 2:
                    gestorDeadlock.detectarDeadlock();
                    break;

                case 3:
                    resolverDeadlock();
                    break;

                case 4:
                    gestorDeadlock.mostrarEstado();
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
     * Permite seleccionar dos procesos para provocar un deadlock.
     */
    private void simularDeadlock() {
        System.out.println("\n--- SIMULAR DEADLOCK ---");

        System.out.println("Procesos disponibles:");
        gestorProcesos.mostrarProcesos();

        int pidA = leerEntero(
                "Ingrese el PID del primer proceso (A): ");

        int pidB = leerEntero(
                "Ingrese el PID del segundo proceso (B): ");

        Proceso procesoA = gestorProcesos.buscarPorPID(pidA);
        Proceso procesoB = gestorProcesos.buscarPorPID(pidB);

        if (procesoA == null || procesoB == null) {
            System.out.println(
                    "Error: uno o ambos procesos no existen.");
            return;
        }

        try {
            gestorDeadlock.simularDeadlock(procesoA, procesoB);

        } catch (IllegalArgumentException e) {
            System.out.println(
                    "No se pudo simular el deadlock: "
                    + e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println(
                    "No se pudo simular el deadlock: "
                    + e.getMessage());
        }
    }

    /**
     * Permite abortar un proceso para resolver el deadlock.
     */
    private void resolverDeadlock() {
        System.out.println("\n--- RESOLVER DEADLOCK ---");

        gestorDeadlock.mostrarEstado();

        if (!gestorDeadlock.isDeadlockDetectado()) {
            System.out.println(
                    "No existe un deadlock activo para resolver.");
            return;
        }

        int pid = leerEntero(
                "Ingrese el PID del proceso que desea abortar: ");

        try {
            gestorDeadlock.resolverDeadlock(pid);

        } catch (IllegalArgumentException e) {
            System.out.println(
                    "No se pudo resolver el deadlock: "
                    + e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println(
                    "No se pudo resolver el deadlock: "
                    + e.getMessage());
        }
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
        gestorDeadlock.mostrarEstado();
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
