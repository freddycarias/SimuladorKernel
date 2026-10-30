
package concurrencia;

import modelo.Proceso;
import modelo.Proceso.Estado;

/**
 * Administra la simulacion de concurrencia.
 *
 * Controla una variable compartida y permite que
 * solamente un proceso a la vez entre en la seccion critica.
 */
public class GestorConcurrencia {

    // Semaforo que controla el acceso exclusivo.
    private final SemaforoBinario semaforo;

    // Variable compartida por los procesos.
    private int variableCompartida;

    // Proceso que actualmente ocupa la seccion critica.
    private Proceso procesoActual;

    /**
     * Constructor del gestor de concurrencia.
     */
    public GestorConcurrencia() {
        this.semaforo = new SemaforoBinario();
        this.variableCompartida = 0;
        this.procesoActual = null;
    }

    /**
     * Solicita acceso a la seccion critica.
     *
     * Si el semaforo esta libre, el proceso obtiene el acceso.
     * Si esta ocupado, el proceso queda bloqueado.
     *
     * @param proceso proceso que solicita acceso.
     * @return true si obtiene el acceso; false si debe esperar.
     */
    public boolean solicitarAcceso(Proceso proceso) {

        if (proceso == null) {
            throw new IllegalArgumentException(
                "El proceso no puede ser nulo."
            );
        }

        // Los procesos terminados o abortados no pueden acceder.
        if (proceso.getEstado() == Estado.TERMINADO
                || proceso.getEstado() == Estado.ABORTADO) {
            System.out.println(
                "Acceso rechazado: el proceso ya no esta activo."
            );
            return false;
        }

        // Si el mismo proceso ya tiene el acceso,
        // no debe solicitarlo nuevamente.
        if (procesoActual == proceso) {
            System.out.println(
                "El PID " + proceso.getPid()
                + " ya esta dentro de la seccion critica."
            );
            return true;
        }

        boolean obtuvoAcceso = semaforo.waitSemaforo(proceso);

        if (obtuvoAcceso) {
            procesoActual = proceso;

            System.out.println(
                "PID " + proceso.getPid()
                + " entro en la seccion critica."
            );
        }

        return obtuvoAcceso;
    }

    /**
     * Modifica la variable compartida.
     *
     * Solamente el proceso que tiene el semaforo
     * puede realizar esta operacion.
     *
     * @param proceso proceso que intenta modificar la variable.
     * @param cantidad cantidad que se sumara a la variable.
     */
    public void modificarVariableCompartida(
            Proceso proceso, int cantidad) {

        // Verifica que el proceso sea el propietario del acceso.
        if (proceso == null || procesoActual != proceso) {
            System.out.println(
                "Acceso denegado: el proceso no tiene el semaforo."
            );
            return;
        }

        // Seccion critica: modificacion de la variable.
        variableCompartida += cantidad;

        System.out.println(
            "PID " + proceso.getPid()
            + " modifico la variable compartida."
        );

        System.out.println(
            "Nuevo valor: " + variableCompartida
        );
    }

    /**
     * Finaliza la operacion del proceso actual.
     *
     * Libera el semaforo y entrega el acceso al siguiente
     * proceso de la cola de espera, si existe.
     *
     * @param proceso proceso que libera el recurso.
     */
    public void liberarAcceso(Proceso proceso) {

        if (proceso == null || procesoActual != proceso) {
            System.out.println(
                "Operacion rechazada: el proceso no posee el semaforo."
            );
            return;
        }

        System.out.println(
            "PID " + proceso.getPid()
            + " sale de la seccion critica."
        );

        // El proceso deja de ocupar la seccion critica.
        if (proceso.getEstado() == Estado.EJECUTANDO) {
            proceso.setEstado(Estado.LISTO);
        }

        // Libera el recurso o lo entrega al siguiente proceso.
        Proceso siguiente = semaforo.signal();

        // Actualiza quien tiene ahora el acceso.
        procesoActual = siguiente;

        if (siguiente != null) {
            System.out.println(
                "PID " + siguiente.getPid()
                + " puede continuar su ejecucion."
            );
        }
    }

    /**
     * Devuelve el valor actual de la variable compartida.
     */
    public int getVariableCompartida() {
        return variableCompartida;
    }

    /**
     * Muestra el estado general de la concurrencia.
     */
    public void mostrarEstado() {

        System.out.println("\n================================");
        System.out.println(" ESTADO DE CONCURRENCIA");
        System.out.println("================================");

        System.out.println(
            "Variable compartida: " + variableCompartida
        );

        if (procesoActual == null) {
            System.out.println("Proceso en seccion critica: ninguno");
        } else {
            System.out.println(
                "Proceso en seccion critica: PID "
                + procesoActual.getPid()
            );
        }

        semaforo.mostrarEstado();

        System.out.println("================================");
    }
}
