
import concurrencia.GestorConcurrencia;
import memoria.GestorMemoria;
import modelo.GestorProcesos;
import modelo.Proceso;

/**
 * Prueba independiente del funcionamiento del semaforo binario.
 *
 * Simula dos procesos que intentan modificar una variable compartida.
 */
public class PruebaConcurrencia {

    public static void main(String[] args) {

        // Creamos los componentes necesarios.
        GestorMemoria memoria = new GestorMemoria();
        GestorProcesos gestorProcesos =
                new GestorProcesos(memoria);

        GestorConcurrencia concurrencia =
                new GestorConcurrencia();

        // Creamos dos procesos para la prueba.
        Proceso proceso1 = gestorProcesos.crearProceso(10, 5);
        Proceso proceso2 = gestorProcesos.crearProceso(10, 4);

        System.out.println("\n==================================");
        System.out.println(" PRUEBA DE CONCURRENCIA");
        System.out.println("==================================");

        // PASO 1: El proceso 1 solicita el semaforo.
        System.out.println("\n1. PID 1 solicita acceso:");
        concurrencia.solicitarAcceso(proceso1);

        // PASO 2: El proceso 2 intenta entrar mientras
        // el proceso 1 todavia tiene ocupado el semaforo.
        System.out.println("\n2. PID 2 solicita acceso:");
        concurrencia.solicitarAcceso(proceso2);

        // PASO 3: El proceso 1 modifica la variable.
        System.out.println("\n3. PID 1 modifica la variable:");
        concurrencia.modificarVariableCompartida(
                proceso1, 10);

        // PASO 4: El proceso 1 libera el semaforo.
        // El proceso 2 recibe el acceso automaticamente.
        System.out.println("\n4. PID 1 libera el semaforo:");
        concurrencia.liberarAcceso(proceso1);

        // PASO 5: Ahora el proceso 2 puede modificar
        // la variable compartida.
        System.out.println("\n5. PID 2 modifica la variable:");
        concurrencia.modificarVariableCompartida(
                proceso2, 20);

        // PASO 6: El proceso 2 libera el semaforo.
        System.out.println("\n6. PID 2 libera el semaforo:");
        concurrencia.liberarAcceso(proceso2);

        // PASO 7: Mostramos el resultado final.
        System.out.println("\n7. Resultado final:");
        concurrencia.mostrarEstado();

        System.out.println(
                "\nValor final esperado de la variable: 30");

        System.out.println("Prueba de concurrencia finalizada.");
    }
}
