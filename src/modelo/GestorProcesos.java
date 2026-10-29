
package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import memoria.GestorMemoria;

/**
 * Administra el registro de procesos del simulador.
 *
 * Responsabilidades:
 * - Asignar PID únicos.
 * - Validar los datos de entrada.
 * - Solicitar memoria al gestor correspondiente.
 * - Registrar los procesos aceptados.
 * - Consultar los procesos existentes.
 */
public class GestorProcesos {

    // Almacena los procesos aceptados por el simulador.
    private final List<Proceso> procesos = new ArrayList<>();

    // Gestiona las reservas de RAM.
    private final GestorMemoria gestorMemoria;

    // El primer proceso tendrá PID 1.
    private int siguientePID = 1;

    /**
     * Recibe el gestor de memoria que compartirá
     * con los demás componentes del simulador.
     */
    public GestorProcesos(GestorMemoria gestorMemoria) {
        if (gestorMemoria == null) {
            throw new IllegalArgumentException(
                "El gestor de memoria es obligatorio."
            );
        }

        this.gestorMemoria = gestorMemoria;
    }

    /**
     * Crea un proceso si sus datos son válidos
     * y existe suficiente memoria disponible.
     *
     * @param memoriaMB memoria solicitada en MB.
     * @param rafagaCPU duración total de CPU.
     * @return el proceso creado, o null si se rechaza.
     */
    public Proceso crearProceso(int memoriaMB, int rafagaCPU) {

        // Un proceso debe solicitar cantidades positivas.
        if (memoriaMB <= 0 || rafagaCPU <= 0) {
            System.out.println(
                "Error: la memoria y la ráfaga de CPU "
                + "deben ser mayores que cero."
            );
            return null;
        }

        // Generamos un identificador para el nuevo proceso.
        int pid = siguientePID;

        // Primero reservamos la memoria necesaria.
        boolean memoriaReservada =
            gestorMemoria.reservarMemoria(pid, memoriaMB);

        // Si falla la reserva, no registramos el proceso.
        if (!memoriaReservada) {
            System.out.println(
                "Out of Memory: no se pudo crear el proceso "
                + "PID " + pid + "."
            );
            System.out.println(
                "Memoria solicitada: " + memoriaMB + " MB."
            );
            System.out.println(
                "Memoria disponible: "
                + gestorMemoria.getMemoriaDisponibleMB() + " MB."
            );

            return null;
        }

        // Construimos el PCB una vez confirmada la reserva.
        Proceso proceso = new Proceso(
            pid,
            memoriaMB,
            rafagaCPU
        );

        // El proceso queda listo para entrar a la planificación.
        proceso.setEstado(Proceso.Estado.LISTO);

        // Registramos el proceso en el sistema.
        procesos.add(proceso);

        // El PID solo avanza cuando la creación tiene éxito.
        siguientePID++;

        System.out.println(
            "Proceso creado correctamente: PID " + pid
        );

        return proceso;
    }

    /**
     * Devuelve una vista no modificable de la lista.
     * Así otras clases no pueden agregar o eliminar
     * procesos directamente.
     */
    public List<Proceso> getProcesos() {
        return Collections.unmodifiableList(procesos);
    }

    /**
     * Busca un proceso por su PID.
     *
     * @param pid identificador buscado.
     * @return proceso encontrado o null.
     */
    public Proceso buscarPorPID(int pid) {
        for (Proceso proceso : procesos) {
            if (proceso.getPid() == pid) {
                return proceso;
            }
        }

        return null;
    }

    /**
     * Muestra los procesos registrados.
     */
    public void mostrarProcesos() {

        System.out.println("\n--- LISTA DE PROCESOS ---");

        if (procesos.isEmpty()) {
            System.out.println("No hay procesos registrados.");
            return;
        }

        for (Proceso proceso : procesos) {
            System.out.println(proceso);
        }
    }

    public int getCantidadProcesos() {
        return procesos.size();
    }

    public GestorMemoria getGestorMemoria() {
        return gestorMemoria;
    }
}
