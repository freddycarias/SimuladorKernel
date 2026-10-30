
package deadlock;

import modelo.Proceso;
import modelo.Proceso.Estado;

/**
 * Administra la simulacion de deadlock.
 *
 * Se utilizan dos recursos exclusivos y dos procesos.
 * Cada proceso obtiene inicialmente un recurso y solicita
 * el recurso que posee el otro proceso.
 */
public class GestorDeadlock {

    // Propietarios actuales de los recursos.
    private Proceso propietarioRecurso1;
    private Proceso propietarioRecurso2;

    // Procesos que esperan obtener cada recurso.
    private Proceso esperandoRecurso1;
    private Proceso esperandoRecurso2;

    // Indica si existe un deadlock detectado.
    private boolean deadlockDetectado;

    /**
     * Constructor: inicialmente los recursos estan libres.
     */
    public GestorDeadlock() {
        propietarioRecurso1 = null;
        propietarioRecurso2 = null;
        esperandoRecurso1 = null;
        esperandoRecurso2 = null;
        deadlockDetectado = false;
    }

    /**
     * Construye el escenario de interbloqueo.
     *
     * Proceso A obtiene el recurso 1.
     * Proceso B obtiene el recurso 2.
     * Proceso A solicita el recurso 2.
     * Proceso B solicita el recurso 1.
     */
    public void simularDeadlock(Proceso procesoA, Proceso procesoB) {

        if (procesoA == null || procesoB == null) {
            System.out.println("Error: los procesos no pueden ser nulos.");
            return;
        }

        if (procesoA == procesoB) {
            System.out.println("Error: se necesitan dos procesos diferentes.");
            return;
        }

        if (procesoA.getEstado() != Estado.LISTO
                || procesoB.getEstado() != Estado.LISTO) {
            System.out.println(
                "Error: ambos procesos deben estar en estado LISTO."
            );
            return;
        }

        if (propietarioRecurso1 != null
                || propietarioRecurso2 != null) {
            System.out.println(
                "Error: los recursos ya estan ocupados. "
                + "Resuelva la simulacion anterior primero."
            );
            return;
        }

        System.out.println("\n==================================");
        System.out.println(" INICIANDO SIMULACION DE DEADLOCK");
        System.out.println("==================================");

        // Primera asignacion: A obtiene el recurso 1.
        propietarioRecurso1 = procesoA;

        System.out.println(
            "PID " + procesoA.getPid()
            + " obtiene RECURSO_1."
        );

        // Segunda asignacion: B obtiene el recurso 2.
        propietarioRecurso2 = procesoB;

        System.out.println(
            "PID " + procesoB.getPid()
            + " obtiene RECURSO_2."
        );

        // A solicita el recurso que tiene B.
        esperandoRecurso2 = procesoA;

        System.out.println(
            "PID " + procesoA.getPid()
            + " solicita RECURSO_2, ocupado por PID "
            + procesoB.getPid() + "."
        );

        // B solicita el recurso que tiene A.
        esperandoRecurso1 = procesoB;

        System.out.println(
            "PID " + procesoB.getPid()
            + " solicita RECURSO_1, ocupado por PID "
            + procesoA.getPid() + "."
        );

        // Ambos procesos quedan bloqueados.
        procesoA.setEstado(Estado.BLOQUEADO);
        procesoB.setEstado(Estado.BLOQUEADO);

        System.out.println(
            "PID " + procesoA.getPid() + " queda BLOQUEADO."
        );

        System.out.println(
            "PID " + procesoB.getPid() + " queda BLOQUEADO."
        );

        // Comprobamos si existe un ciclo de espera.
        detectarDeadlock();
    }

    /**
     * Detecta el ciclo de espera entre los dos procesos.
     *
     * Existe deadlock cuando:
     * - A posee RECURSO_1 y espera RECURSO_2.
     * - B posee RECURSO_2 y espera RECURSO_1.
     */
    public boolean detectarDeadlock() {

        boolean cicloDetectado =
                propietarioRecurso1 != null
                && propietarioRecurso2 != null
                && esperandoRecurso2 == propietarioRecurso1
                && esperandoRecurso1 == propietarioRecurso2;

        deadlockDetectado = cicloDetectado;

        if (deadlockDetectado) {
            System.out.println("\n!!! ALERTA DE DEADLOCK !!!");
            System.out.println(
                "Se detecto una espera circular entre dos procesos."
            );

            System.out.println(
                "PID " + propietarioRecurso1.getPid()
                + " espera RECURSO_2."
            );

            System.out.println(
                "PID " + propietarioRecurso2.getPid()
                + " espera RECURSO_1."
            );
        } else {
            System.out.println("No se detecto deadlock.");
        }

        return deadlockDetectado;
    }

    /**
     * Resuelve el deadlock abortando uno de los procesos.
     *
     * Al abortar un proceso:
     * - Cambia su estado a ABORTADO.
     * - Libera los recursos que posee.
     * - Entrega el recurso liberado al proceso que lo esperaba.
     *
     * @param pidVictima PID del proceso que sera abortado.
     */
    public void resolverDeadlock(int pidVictima) {

        if (!deadlockDetectado) {
            System.out.println(
                "No existe un deadlock activo para resolver."
            );
            return;
        }

        Proceso victima = null;

        if (propietarioRecurso1 != null
                && propietarioRecurso1.getPid() == pidVictima) {
            victima = propietarioRecurso1;
        } else if (propietarioRecurso2 != null
                && propietarioRecurso2.getPid() == pidVictima) {
            victima = propietarioRecurso2;
        }

        if (victima == null) {
            System.out.println(
                "El PID indicado no es propietario de los recursos."
            );
            return;
        }

        System.out.println("\n--- RESOLVIENDO DEADLOCK ---");

        System.out.println(
            "Se abortara el proceso PID " + pidVictima + "."
        );

        // El proceso victima deja de ejecutarse.
        victima.setEstado(Estado.ABORTADO);

        // Si la victima posee RECURSO_1, lo liberamos.
        if (propietarioRecurso1 == victima) {

            propietarioRecurso1 = null;

            System.out.println(
                "PID " + pidVictima + " libero RECURSO_1."
            );

            // Se entrega el recurso al proceso que lo esperaba.
            if (esperandoRecurso1 != null) {
                propietarioRecurso1 = esperandoRecurso1;

                System.out.println(
                    "RECURSO_1 asignado al PID "
                    + propietarioRecurso1.getPid() + "."
                );
            }

            esperandoRecurso1 = null;
        }

        // Si la victima posee RECURSO_2, lo liberamos.
        if (propietarioRecurso2 == victima) {

            propietarioRecurso2 = null;

            System.out.println(
                "PID " + pidVictima + " libero RECURSO_2."
            );

            // Se entrega el recurso al proceso que lo esperaba.
            if (esperandoRecurso2 != null) {
                propietarioRecurso2 = esperandoRecurso2;

                System.out.println(
                    "RECURSO_2 asignado al PID "
                    + propietarioRecurso2.getPid() + "."
                );
            }

            esperandoRecurso2 = null;
        }

        // El proceso que sobrevive deja de estar bloqueado.
        if (propietarioRecurso1 != null
                && propietarioRecurso1.getEstado() == Estado.BLOQUEADO) {
            propietarioRecurso1.setEstado(Estado.LISTO);
        }

        if (propietarioRecurso2 != null
                && propietarioRecurso2.getEstado() == Estado.BLOQUEADO) {
            propietarioRecurso2.setEstado(Estado.LISTO);
        }

        // Ya no existe la espera circular.
        esperandoRecurso1 = null;
        esperandoRecurso2 = null;
        deadlockDetectado = false;

        System.out.println(
            "Deadlock resuelto. El proceso restante puede continuar."
        );
    }

    /**
     * Muestra los propietarios y el estado de los recursos.
     */
    public void mostrarEstado() {

        System.out.println("\n==================================");
        System.out.println(" ESTADO DE RECURSOS Y DEADLOCK");
        System.out.println("==================================");

        mostrarPropietario("RECURSO_1", propietarioRecurso1);
        mostrarPropietario("RECURSO_2", propietarioRecurso2);

        System.out.println(
            "PID esperando RECURSO_1: "
            + obtenerPID(esperandoRecurso1)
        );

        System.out.println(
            "PID esperando RECURSO_2: "
            + obtenerPID(esperandoRecurso2)
        );

        System.out.println(
            "Deadlock detectado: "
            + (deadlockDetectado ? "SI" : "NO")
        );

        System.out.println("==================================");
    }

    /**
     * Muestra quien posee un recurso.
     */
    private void mostrarPropietario(
            String recurso, Proceso propietario) {

        if (propietario == null) {
            System.out.println(recurso + ": LIBRE");
        } else {
            System.out.println(
                recurso + ": PID " + propietario.getPid()
            );
        }
    }

    /**
     * Devuelve el PID o la palabra NINGUNO.
     */
    private String obtenerPID(Proceso proceso) {
        if (proceso == null) {
            return "NINGUNO";
        }

        return String.valueOf(proceso.getPid());
    }

    /**
     * Permite consultar si hay un deadlock activo.
     */
    public boolean isDeadlockDetectado() {
        return deadlockDetectado;
    }
}
