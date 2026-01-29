# MANUAL DE USUARIO
## Simulador Visual del "Cuello de Botella" de Von Neumann

**Asignatura:** Arquitectura de Computadores  
**Proyecto:** Categoría B2 - Desarrollo de Software y Simulación  
**Versión:** 1.0  

---

### 1. INTRODUCCIÓN

Este software es una herramienta educativa desarrollada como parte del Proyecto Final de la asignatura. Su objetivo principal es evidenciar visualmente la limitación de velocidad impuesta por el bus compartido entre datos e instrucciones, fenómeno conocido en la teoría de computación como el **"Cuello de Botella de Von Neumann"**.

El simulador permite al usuario modificar parámetros críticos de hardware —como la Frecuencia de la CPU y la Latencia de la Memoria RAM— para observar en tiempo real cómo la CPU desperdicia ciclos de reloj esperando datos.

---

### 2. REQUISITOS DEL SISTEMA

Para la correcta ejecución del simulador, el equipo debe cumplir con los siguientes requisitos mínimos:

* **Sistema Operativo:** Windows 10/11, macOS o Linux.
* **Entorno de Ejecución:** Java Runtime Environment (JRE) 8 o superior.
* **Resolución de Pantalla:** Mínima de 1024x768 píxeles.
* **Periféricos:** Ratón y teclado estándar.

---

### 3. INTERFAZ GRÁFICA

La interfaz de usuario (GUI) está diseñada para ser intuitiva y se divide en dos secciones principales: el **Panel de Visualización** y el **Panel de Control**.

> *Nota: Se recomienda insertar aquí una captura general de la interfaz del programa.*
> `![Vista General de la Interfaz](ruta/a/tu/captura_pantalla.png)`

#### 3.1. Panel de Visualización (Zona Superior)
Este panel representa gráficamente los componentes físicos de la arquitectura y su interacción:

1.  **CPU (Unidad Central de Procesamiento):**
    * **Estado VERDE (Processing):** Indica que la CPU está decodificando y ejecutando una instrucción. Es el estado "productivo".
    * **Estado ROJO (Waiting):** Indica que la CPU está detenida ("Idle") esperando a que llegue un dato desde la memoria. Representa el desperdicio de recursos por el cuello de botella.
    * **Indicador CLK:** Una luz verde parpadeante que simula el pulso del reloj del procesador.

2.  **Memoria RAM:**
    * Incluye un **Gráfico de Carga (Pie Chart)** que se llena visualmente para representar el tiempo de latencia física (retraso) al buscar un dato solicitado.

3.  **Buses del Sistema:**
    * Representados por dos líneas paralelas que conectan la CPU y la RAM:
        * **Bus de Direcciones (Superior):** Por donde la CPU solicita la ubicación de memoria.
        * **Bus de Datos (Inferior):** Por donde viaja la información real.
    * **Paquetes de Datos:** Visualizados como círculos naranjas que viajan por el bus, conteniendo códigos hexadecimales (ej. `0xA1`) o instrucciones mnemónicas (ej. `MOV`, `ADD`).

#### 3.2. Panel de Control (Zona Inferior)
Permite la configuración de los parámetros de simulación en tiempo caliente:

* **Slider "Frecuencia CPU":** Ajusta la velocidad de procesamiento simulada en Hertz (Hz).
* **Slider "Latencia RAM":** Ajusta el tiempo de respuesta de la memoria en milisegundos (ms).
* **Botones de Control:** `Iniciar Simulación` y `Detener`.
* **Métricas en Tiempo Real:** Visualización numérica y gráfica (barra de progreso) de la **Eficiencia de la CPU**.

---

### 4. GUÍA DE USO

#### 4.1. Configuración Inicial
Al ejecutar el programa `VonNeumannSimulator`, los valores por defecto se establecen en un punto medio (Frecuencia: 50Hz, Latencia: