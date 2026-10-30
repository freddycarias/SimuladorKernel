
import menu.MenuPrincipal;
import memoria.GestorMemoria;
import modelo.GestorProcesos;
import planificacion.PlanificadorRoundRobin;
import concurrencia.GestorConcurrencia;

public class Main {

        public static void main(String[] args) {
                // Administrador de la memoria RAM simulada.
                GestorMemoria gestorMemoria = new GestorMemoria();

                // Administrador de los procesos.
                GestorProcesos gestorProcesos =
                        new GestorProcesos(gestorMemoria);

                // Planificador Round Robin.
                PlanificadorRoundRobin planificador =
                        new PlanificadorRoundRobin(gestorProcesos);

                // Administrador de concurrencia.
                GestorConcurrencia gestorConcurrencia =
                        new GestorConcurrencia();

                // Menu principal con todos los componentes.
                MenuPrincipal menu = new MenuPrincipal(
                        gestorProcesos,
                        gestorMemoria,
                        planificador,
                        gestorConcurrencia
                );

                // Iniciar el simulador.
                menu.iniciar();
        }
}
