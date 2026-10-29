
import menu.MenuPrincipal;
import memoria.GestorMemoria;
import modelo.GestorProcesos;

/**
 * Punto de entrada del Simulador de Funciones del Kernel.
 *
 * Inicializa los componentes principales y los conecta.
 */
public class Main {

    public static void main(String[] args) {

        // Creamos una sola instancia del gestor de memoria.
        GestorMemoria gestorMemoria = new GestorMemoria();

        // El gestor de procesos utilizará esa misma memoria.
        GestorProcesos gestorProcesos =
            new GestorProcesos(gestorMemoria);

        // Entregamos los gestores al menú.
        MenuPrincipal menu = new MenuPrincipal(
            gestorProcesos,
            gestorMemoria
        );

        // Iniciamos la interfaz de consola.
        menu.iniciar();
    }
}
