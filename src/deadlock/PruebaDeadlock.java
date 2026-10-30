
package deadlock;
import memoria.GestorMemoria;
import modelo.GestorProcesos;
import modelo.Proceso;

public class PruebaDeadlock {

    public static void main(String[] args) {

        // Crear los componentes necesarios para la prueba.
        GestorMemoria memoria = new GestorMemoria();
        GestorProcesos gestorProcesos = new GestorProcesos(memoria);
        GestorDeadlock gestorDeadlock = new GestorDeadlock();

        // Crear dos procesos con memoria y ráfagas de CPU.
        Proceso procesoA = gestorProcesos.crearProceso(10, 5);
        Proceso procesoB = gestorProcesos.crearProceso(10, 4);

        System.out.println("======================================");
        System.out.println(" PRUEBA DE DETECCION DE DEADLOCK");
        System.out.println("======================================");

        // Mostrar los procesos antes de provocar el deadlock.
        System.out.println("\nProcesos iniciales:");
        System.out.println(procesoA);
        System.out.println(procesoB);

        // Simular la espera circular entre los dos procesos.
        System.out.println("\n1. Simulando deadlock...");
        gestorDeadlock.simularDeadlock(procesoA, procesoB);

        // Mostrar los recursos y procesos involucrados.
        System.out.println("\n2. Estado despues de detectar el deadlock:");
        gestorDeadlock.mostrarEstado();

        // Comprobar que el deadlock fue detectado.
        System.out.println("\n3. Verificando deteccion:");
        System.out.println(
            "Deadlock detectado: " + gestorDeadlock.isDeadlockDetectado()
        );

        // Abortar el proceso B para resolver el deadlock.
        System.out.println("\n4. Resolviendo deadlock...");
        gestorDeadlock.resolverDeadlock(procesoB.getPid());

        // Mostrar el estado después de la recuperación.
        System.out.println("\n5. Estado despues de la recuperacion:");
        gestorDeadlock.mostrarEstado();

        System.out.println("\nProcesos finales:");
        System.out.println(procesoA);
        System.out.println(procesoB);

        System.out.println("\nDeadlock detectado: "
            + gestorDeadlock.isDeadlockDetectado());

        System.out.println("\n======================================");
        System.out.println(" FIN DE LA PRUEBA");
        System.out.println("======================================");
    }
}
