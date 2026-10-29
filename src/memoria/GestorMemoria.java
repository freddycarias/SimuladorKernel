
package memoria;

import java.util.HashMap;
import java.util.Map;

/**
 * Simula la administración de memoria RAM del sistema.
 *
 * Capacidad máxima: 100 MB.
 *
 * Cada reserva queda asociada a un PID, lo que permite
 * liberar la memoria del proceso correspondiente.
 */
public class GestorMemoria {

    // Capacidad total de RAM simulada.
    private static final int CAPACIDAD_TOTAL_MB = 100;

    // PID -> cantidad de MB reservados.
    private final Map<Integer, Integer> reservas =
        new HashMap<>();

    // Total de memoria actualmente ocupada.
    private int memoriaUtilizadaMB = 0;

    /**
     * Intenta reservar memoria para un proceso.
     *
     * @param pid identificador del proceso.
     * @param cantidadMB memoria solicitada.
     * @return true si se reservó; false si no fue posible.
     */
    public boolean reservarMemoria(int pid, int cantidadMB) {

        // No aceptamos identificadores ni cantidades inválidas.
        if (pid <= 0 || cantidadMB <= 0) {
            return false;
        }

        // Evita reservar dos veces para el mismo proceso.
        if (reservas.containsKey(pid)) {
            return false;
        }

        // Comprobamos la capacidad antes de modificar el uso.
        if (cantidadMB > getMemoriaDisponibleMB()) {
            return false;
        }

        // Registramos la reserva y actualizamos el contador.
        reservas.put(pid, cantidadMB);
        memoriaUtilizadaMB += cantidadMB;

        return true;
    }

    /**
     * Libera la memoria reservada por un proceso.
     *
     * Si el PID no tiene una reserva, no se modifica
     * el estado del gestor.
     *
     * @param pid identificador del proceso.
     * @return true si había memoria que liberar.
     */
    public boolean liberarMemoria(int pid) {

        Integer memoriaLiberada = reservas.remove(pid);

        if (memoriaLiberada == null) {
            return false;
        }

        memoriaUtilizadaMB -= memoriaLiberada;
        return true;
    }

    public int getCapacidadTotalMB() {
        return CAPACIDAD_TOTAL_MB;
    }

    public int getMemoriaUtilizadaMB() {
        return memoriaUtilizadaMB;
    }

    public int getMemoriaDisponibleMB() {
        return CAPACIDAD_TOTAL_MB - memoriaUtilizadaMB;
    }

    /**
     * Comprueba si un PID tiene memoria reservada.
     */
    public boolean tieneReserva(int pid) {
        return reservas.containsKey(pid);
    }

    /**
     * Muestra el resumen de uso de la RAM.
     */
    public void mostrarEstado() {
        System.out.println("\n--- ESTADO DE LA MEMORIA RAM ---");
        System.out.println(
            "Capacidad total: " + getCapacidadTotalMB() + " MB"
        );
        System.out.println(
            "Memoria utilizada: " + getMemoriaUtilizadaMB() + " MB"
        );
        System.out.println(
            "Memoria disponible: " + getMemoriaDisponibleMB() + " MB"
        );
    }
}
