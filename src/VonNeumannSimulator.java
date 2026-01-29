import java.awt.*;
import java.util.Random;
import javax.swing.*;

/**
 * CLASE PRINCIPAL
 * PROYECTO - ARQUITECTURA DE COMPUTADORES - GRUPO 4 B2
 * SIMULADOR DEL CUELLO DE BOTELLA DE VON NEUMANN
 */
public class VonNeumannSimulator extends JFrame {

    // Componentes UI
    private SimulationPanel simulationPanel;
    private ControlPanel controlPanel;
    private Timer systemTimer;
    
    // Estado de la Simulación
    private CPUState currentState = CPUState.IDLE;
    private boolean isSimulationRunning = false;
    
    // Parámetros
    private int cpuSpeedDelay = 50; 
    private int ramLatency = 100;
    private int currentRamWait = 0;
    private int maxRamWait = 100; // Para calcular porcentaje de carga
    private float busProgress = 0.0f; // 0.0 a 1.0

    // Datos simulados
    private String currentDataPacket = "0x00";
    private Random random = new Random();

    // Métricas
    private long totalCycles = 0;
    private long idleCycles = 0;
    private double efficiency = 100.0;

    public VonNeumannSimulator() {
        setTitle("Simulador Cuello de Botella Von Neumann - Proyecto B2");
        setSize(950, 650); // Un poco más grande para los nuevos detalles
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Inicializar paneles pasando 'this' para que puedan acceder a los datos
        simulationPanel = new SimulationPanel(this);
        controlPanel = new ControlPanel(this);

        add(simulationPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        // Timer principal (Game Loop)
        systemTimer = new Timer(16, e -> updateLoop());
    }

    private void updateLoop() {
        if (!isSimulationRunning) return;

        // Lógica de Estados
        switch (currentState) {
            case FETCH_REQUEST:
                busProgress += 0.01f; 
                if (busProgress >= 1.0f) {
                    currentState = CPUState.MEMORY_ACCESS;
                    currentRamWait = ramLatency; 
                    maxRamWait = ramLatency; // Guardamos el total para dibujar el progreso
                    busProgress = 1.0f;
                }
                break;

            case MEMORY_ACCESS:
                // SIMULACIÓN DEL CUELLO DE BOTELLA
                if (currentRamWait > 0) {
                    currentRamWait -= 2; 
                    idleCycles++; // Contar tiempo perdido
                } else {
                    // Al terminar la espera, preparamos un dato aleatorio para volver
                    currentState = CPUState.FETCH_RESPONSE;
                    generateRandomData();
                }
                break;

            case FETCH_RESPONSE:
                busProgress -= 0.01f;
                if (busProgress <= 0.0f) {
                    currentState = CPUState.DECODE_EXECUTE;
                    busProgress = 0.0f;
                }
                break;

            case DECODE_EXECUTE:
                // Simular velocidad de procesamiento de CPU
                if (Math.random() > (double)cpuSpeedDelay / 200.0) {
                     currentState = CPUState.FETCH_REQUEST;
                     generateRandomInstruction(); // Nueva instrucción para pedir
                }
                break;
                
            case IDLE:
                break;
        }

        // Calcular eficiencia
        totalCycles++;
        if (totalCycles > 0) {
            efficiency = 100.0 - (((double) idleCycles / totalCycles) * 100.0);
        }

        // Refrescar Paneles
        simulationPanel.repaint();
        controlPanel.updateStatsUI();
    }

    // --- DATOS SIMULADOS ---
    private void generateRandomData() {
        // Genera valores hex como 0x4F, 0x1A, etc.
        currentDataPacket = String.format("0x%02X", random.nextInt(256));
    }

    private void generateRandomInstruction() {
        // Simula instrucciones de ensamblador
        String[] inst = {"LDA", "ADD", "SUB", "MOV", "JMP"};
        currentDataPacket = inst[random.nextInt(inst.length)];
    }

    // --- MÉTODOS DE CONTROL ---
    public void startSimulation() {
        isSimulationRunning = true;
        currentState = CPUState.FETCH_REQUEST;
        systemTimer.start();
    }

    public void stopSimulation() {
        isSimulationRunning = false;
        systemTimer.stop();
    }

    // --- GETTERS and SETTERS ---
    public CPUState getCurrentState() { return currentState; }
    public float getBusProgress() { return busProgress; }
    public double getEfficiency() { return efficiency; }
    public long getIdleCycles() { return idleCycles; }
    public String getCurrentDataPacket() { return currentDataPacket; }
    public int getCurrentRamWait() { return currentRamWait; }
    public int getMaxRamWait() { return maxRamWait; }
    
    public void setCpuSpeedDelay(int delay) { this.cpuSpeedDelay = delay; }
    public void setRamLatency(int latency) { this.ramLatency = latency; }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VonNeumannSimulator app = new VonNeumannSimulator();
            app.setVisible(true);
            app.setLocationRelativeTo(null); 
        });
    }
}