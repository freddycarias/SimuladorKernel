
# Simulador de Funciones del Kernel

Proyecto final del curso Sistemas Operativos I.

## Descripción

Aplicación de consola desarrollada en Java para simular
componentes fundamentales de un sistema operativo.

## Tecnologías

- Java
- Visual Studio Code
- Git y GitHub

## Funcionalidades implementadas

- Registro de procesos mediante PCB.
- Asignación automática de identificadores PID.
- Validación de memoria solicitada.
- Administración de memoria RAM simulada de 100 MB.
- Rechazo de procesos cuando la memoria es insuficiente.
- Consulta del estado de los procesos y de la memoria.

## Funcionalidades pendientes

- Planificación Round Robin con quantum de 3.
- Gestión de concurrencia mediante semáforo binario.
- Simulación, detección y resolución de deadlock.

## Requisitos

- JDK compatible con el código fuente.
- Terminal o Visual Studio Code.

## Compilación

Desde la carpeta raíz del proyecto, ejecutar:

```bash
javac -d out src/Main.java src/menu/*.java src/modelo/*.java src/memoria/*.java
```

## Ejecución

```bash
java -cp out Main
```

## Estado del proyecto

Desarrollo por fases. Las funcionalidades indicadas como
pendientes se incorporarán en futuras versiones.
