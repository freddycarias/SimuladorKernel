
package modelo;

/**
 * Representa un proceso mediante su Bloque de Control
 * de Procesos (PCB).
 *
 * El PCB conserva la información necesaria para que
 * el simulador identifique y administre el proceso.
 */
public class Proceso {

    /**
     * Estados que puede tener un proceso en el simulador.
     *
     * NUEVO: acaba de ser creado.
     * LISTO: espera un turno de CPU.
     * EJECUTANDO: está utilizando la CPU simulada.
     * BLOQUEADO: espera un recurso o condición.
     * TERMINADO: finalizó su ejecución.
     * ABORTADO: fue cancelado forzosamente.
     */
    public enum Estado {
        NUEVO,
        LISTO,
        EJECUTANDO,
        BLOQUEADO,
        TERMINADO,
        ABORTADO
    }

    // Identificador único asignado por el gestor de procesos.
    private final int pid;

    // Estado actual del proceso.
    private Estado estado;

    // Cantidad de memoria RAM que ocupa, en MB.
    private final int memoriaMB;

    // Duración total del proceso, en unidades de CPU.
    private final int rafagaTotal;

    // Unidades de CPU que todavía necesita.
    private int rafagaRestante;

    /**
     * Construye el PCB de un nuevo proceso.
     *
     * @param pid identificador único del proceso.
     * @param memoriaMB memoria solicitada en MB.
     * @param rafagaTotal duración total de CPU.
     */
    public Proceso(int pid, int memoriaMB, int rafagaTotal) {
        this.pid = pid;
        this.memoriaMB = memoriaMB;
        this.rafagaTotal = rafagaTotal;
        this.rafagaRestante = rafagaTotal;
        this.estado = Estado.NUEVO;
    }

    public int getPid() {
        return pid;
    }

    public Estado getEstado() {
        return estado;
    }

    public int getMemoriaMB() {
        return memoriaMB;
    }

    public int getRafagaTotal() {
        return rafagaTotal;
    }

    public int getRafagaRestante() {
        return rafagaRestante;
    }

    /**
     * Actualiza el estado del proceso.
     *
     * Se utilizará desde los gestores del simulador
     * cuando cambie su situación.
     */
    public void setEstado(Estado estado) {
        if (estado == null) {
            throw new IllegalArgumentException(
                "El estado no puede ser nulo."
            );
        }

        this.estado = estado;
    }

    /**
     * Consume unidades de CPU sin permitir que la
     * ráfaga restante sea negativa.
     *
     * El planificador Round Robin utilizará este método.
     *
     * @param unidades cantidad de CPU consumida.
     */
    public void consumirCPU(int unidades) {
        if (unidades <= 0) {
            throw new IllegalArgumentException(
                "Las unidades de CPU deben ser positivas."
            );
        }

        rafagaRestante -= Math.min(unidades, rafagaRestante);

        if (rafagaRestante == 0) {
            estado = Estado.TERMINADO;
        }
    }

    /**
     * Devuelve una representación legible del PCB.
     */
    @Override
    public String toString() {
        return String.format(
            "PID: %d | Estado: %s | Memoria: %d MB"
            + " | CPU total: %d | CPU restante: %d",
            pid,
            estado,
            memoriaMB,
            rafagaTotal,
            rafagaRestante
        );
    }
}
