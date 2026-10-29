
package planificacion;

import java.util.LinkedList;
import java.util.Queue;

import modelo.GestorProcesos;
import modelo.Proceso;
import modelo.Proceso.Estado;

/**
 * Simula el algoritmo de planificacion Round Robin.
 *
 * Cada proceso recibe como maximo 3 unidades de CPU por turno.
 * Si no termina, regresa al final de la cola de listos.
 */
public class PlanificadorRoundRobin {

    // Tiempo maximo de CPU permitido por turno.
    private static final int QUANTUM = 3;

    // Cola FIFO: el primero en entrar es el primero en ejecutarse.
    private final Queue<Proceso> colaListos = new LinkedList<>();

    // Permite consultar los procesos registrados en el sistema.
    private final GestorProcesos gestorProcesos;

    /**
     * Recibe el gestor que contiene los procesos creados.
     */
    public PlanificadorRoundRobin(GestorProcesos gestorProcesos) {
        if (gestorProcesos == null) {
            throw new IllegalArgumentException(
                "El gestor de procesos no puede ser nulo."
            );
        }

        this.gestorProcesos = gestorProcesos;
    }

    /**
     * Agrega a la cola los procesos que actualmente estan LISTO.
     */
    private void cargarProcesosListos() {
        for (Proceso proceso : gestorProcesos.getProcesos()) {

            // Solo agregamos procesos listos para evitar
            // ejecutar procesos terminados o abortados.
            if (proceso.getEstado() == Estado.LISTO
                    && !colaListos.contains(proceso)) {
                colaListos.offer(proceso);
            }
        }
    }

    /**
     * Ejecuta la simulacion hasta que la cola quede vacia.
     */
    public void ejecutarPlanificacion() {
        cargarProcesosListos();

        if (colaListos.isEmpty()) {
            System.out.println(
                "\nNo hay procesos listos para ejecutar."
            );
            return;
        }

        int turno = 1;

        System.out.println("\n======================================");
        System.out.println(" PLANIFICACION ROUND ROBIN");
        System.out.println(" Quantum: " + QUANTUM + " unidades");
        System.out.println("======================================");

        // Mientras haya procesos esperando, se ejecutan por turnos.
        while (!colaListos.isEmpty()) {
            Proceso proceso = colaListos.poll();

            // El proceso pasa a ocupar la CPU.
            proceso.setEstado(Estado.EJECUTANDO);

            // Nunca se consume mas CPU que el quantum ni que
            // la rafaga que le queda al proceso.
            int unidadesCPU = Math.min(
                QUANTUM,
                proceso.getRafagaRestante()
            );

            System.out.println("\nTurno " + turno);
            System.out.println("Cambio de contexto: CPU asignada al PID "
                    + proceso.getPid());
            System.out.println("Rafaga restante antes: "
                    + proceso.getRafagaRestante());
            System.out.println("Unidades de CPU utilizadas: "
                    + unidadesCPU);

            // Descuenta las unidades ejecutadas.
            proceso.consumirCPU(unidadesCPU);

            System.out.println("Rafaga restante despues: "
                    + proceso.getRafagaRestante());

            if (proceso.getEstado() == Estado.TERMINADO) {
                System.out.println(
                    "PID " + proceso.getPid() + " ha terminado."
                );
            } else {
                // No termino: vuelve a LISTO y al final de la cola.
                proceso.setEstado(Estado.LISTO);
                colaListos.offer(proceso);

                System.out.println(
                    "PID " + proceso.getPid()
                    + " vuelve al final de la cola de listos."
                );
            }

            turno++;
        }

        System.out.println("\n======================================");
        System.out.println(" PLANIFICACION FINALIZADA");
        System.out.println("Todos los procesos de esta cola terminaron.");
        System.out.println("======================================");
    }
}
