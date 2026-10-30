
package concurrencia;

import java.util.LinkedList;
import java.util.Queue;

import modelo.Proceso;
import modelo.Proceso.Estado;

/**
 * Simula un semaforo binario para controlar
 * el acceso exclusivo a una seccion critica.
 *
 * El semaforo solamente puede estar:
 * - Disponible: ningun proceso ocupa el recurso.
 * - Ocupado: un proceso tiene acceso al recurso.
 */
public class SemaforoBinario {

    // Indica si el recurso compartido esta disponible.
    private boolean disponible;

    // Procesos que esperan para utilizar el recurso.
    private final Queue<Proceso> colaEspera;

    /**
     * Constructor.
     * Inicialmente el semaforo esta disponible.
     */
    public SemaforoBinario() {
        this.disponible = true;
        this.colaEspera = new LinkedList<>();
    }

    /**
     * Simula la operacion wait() de un semaforo.
     *
     * Si esta disponible, el proceso obtiene el recurso.
     * Si esta ocupado, el proceso queda bloqueado y espera.
     *
     * Nota: Java tiene un metodo Object.wait() final.
     * Por eso utilizamos el nombre waitSemaforo().
     *
     * @param proceso proceso que solicita el recurso.
     * @return true si obtiene el recurso; false si debe esperar.
     */
    public boolean waitSemaforo(Proceso proceso) {

        if (proceso == null) {
            throw new IllegalArgumentException(
                "El proceso no puede ser nulo."
            );
        }

        // Si el recurso esta libre, se concede el acceso.
        if (disponible) {
            disponible = false;
            proceso.setEstado(Estado.EJECUTANDO);

            System.out.println(
                "PID " + proceso.getPid()
                + " obtuvo el semaforo."
            );

            return true;
        }

        // Evita agregar dos veces el mismo proceso.
        if (!colaEspera.contains(proceso)) {
            colaEspera.offer(proceso);
            proceso.setEstado(Estado.BLOQUEADO);

            System.out.println(
                "PID " + proceso.getPid()
                + " bloqueado. El semaforo esta ocupado."
            );
        }

        return false;
    }

    /**
     * Simula la operacion signal() de un semaforo.
     *
     * Libera el recurso. Si existen procesos esperando,
     * entrega el acceso al primero de la cola.
     *
     * @return proceso que recibe el recurso o null si no hay espera.
     */
    public Proceso signal() {

        // Si hay procesos esperando, se entrega el recurso
        // al primero de la cola sin dejarlo disponible.
        if (!colaEspera.isEmpty()) {

            Proceso siguiente = colaEspera.poll();

            siguiente.setEstado(Estado.EJECUTANDO);

            System.out.println(
                "El semaforo se entrega al PID "
                + siguiente.getPid()
            );

            return siguiente;
        }

        // Si nadie espera, el recurso queda libre.
        disponible = true;

        System.out.println("Semaforo liberado. Recurso disponible.");

        return null;
    }

    /**
     * Indica si el semaforo esta disponible.
     */
    public boolean isDisponible() {
        return disponible;
    }

    /**
     * Devuelve la cantidad de procesos en espera.
     */
    public int getCantidadEnEspera() {
        return colaEspera.size();
    }

    /**
     * Muestra el estado actual del semaforo.
     */
    public void mostrarEstado() {

        System.out.println("\n--- ESTADO DEL SEMAFORO ---");

        System.out.println(
            "Recurso: "
            + (disponible ? "DISPONIBLE" : "OCUPADO")
        );

        System.out.println(
            "Procesos en espera: " + colaEspera.size()
        );

        if (!colaEspera.isEmpty()) {
            System.out.println("Cola de espera:");

            for (Proceso proceso : colaEspera) {
                System.out.println(
                    "PID " + proceso.getPid()
                );
            }
        }
    }
}
