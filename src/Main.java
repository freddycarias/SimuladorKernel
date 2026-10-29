
import menu.MenuPrincipal;
import memoria.GestorMemoria;
import modelo.GestorProcesos;
import planificacion.PlanificadorRoundRobin;

public class Main {

    public static void main(String[] args) {

        // Administra la memoria RAM simulada.
        GestorMemoria gestorMemoria = new GestorMemoria();

        // Administra los procesos y sus identificadores PID.
        GestorProcesos gestorProcesos =
                new GestorProcesos(gestorMemoria);

        // Planifica los procesos usando Round Robin.
        PlanificadorRoundRobin planificador =
                new PlanificadorRoundRobin(gestorProcesos);

        // El menu recibe los gestores y el planificador.
        MenuPrincipal menu = new MenuPrincipal(
                gestorProcesos,
                gestorMemoria,
                planificador
        );

        // Inicia la aplicacion.
        menu.iniciar();
    }
}
