import java.awt.*;
import java.awt.geom.Arc2D;
import javax.swing.*;

/**
 * Panel visual.
 */
public class SimulationPanel extends JPanel {
    
    private VonNeumannSimulator simulator;

    public SimulationPanel(VonNeumannSimulator simulator) {
        this.simulator = simulator;
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int centerY = height / 2;

        // --- 1. DIBUJAR CPU ---
        int cpuX = 80;
        int compSize = 160;
        
        // Color dinámico de la CPU
        if (simulator.getCurrentState() == CPUState.MEMORY_ACCESS) {
            g2.setColor(new Color(255, 150, 150)); // Rojo suave
        } else if (simulator.getCurrentState() == CPUState.DECODE_EXECUTE) {
            g2.setColor(new Color(150, 255, 150)); // Verde suave
        } else {
            g2.setColor(new Color(240, 240, 240));
        }

        g2.fillRoundRect(cpuX, centerY - compSize/2, compSize, compSize, 20, 20);
        g2.setStroke(new BasicStroke(3));
        g2.setColor(Color.DARK_GRAY);
        g2.drawRoundRect(cpuX, centerY - compSize/2, compSize, compSize, 20, 20);
        
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString("CPU", cpuX + 60, centerY - 10);
        
        // INDICADOR DE FRECUENCIA (PULSO)
        if (simulator.getCurrentState() == CPUState.DECODE_EXECUTE) {
             g2.setColor(new Color(0, 200, 0));
             g2.fillOval(cpuX + 10, centerY - 70, 15, 15);
             g2.setFont(new Font("Arial", Font.PLAIN, 10));
             g2.drawString("CLK", cpuX + 30, centerY - 60);
        } else {
             g2.setColor(Color.GRAY);
             g2.drawOval(cpuX + 10, centerY - 70, 15, 15);
        }
        
        String statusText = (simulator.getCurrentState() == CPUState.MEMORY_ACCESS) ? "ESPERANDO..." : "PROCESANDO";
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.setColor(Color.BLACK);
        g2.drawString(statusText, cpuX + 40, centerY + 20);

        // --- 2. DIBUJAR MEMORIA RAM ---
        int ramX = width - 240;
        g2.setColor(simulator.getCurrentState() == CPUState.MEMORY_ACCESS ? new Color(200, 230, 255) : new Color(240, 240, 240));
        g2.fillRoundRect(ramX, centerY - compSize/2, compSize, compSize, 20, 20);
        g2.setColor(Color.DARK_GRAY);
        g2.drawRoundRect(ramX, centerY - compSize/2, compSize, compSize, 20, 20);
        
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString("MEMORIA RAM", ramX + 20, centerY - 10);

        // INDICADOR GRÁFICO DE LATENCIA (Reloj de Carga)
        if (simulator.getCurrentState() == CPUState.MEMORY_ACCESS) {
            int max = simulator.getMaxRamWait();
            int current = simulator.getCurrentRamWait();
            if (max > 0) {
                // Dibujar un "pie chart" que se vacía
                int clockSize = 40;
                int clockX = ramX + compSize - 50;
                int clockY = centerY - 70;
                
                g2.setColor(Color.LIGHT_GRAY);
                g2.fillOval(clockX, clockY, clockSize, clockSize);
                
                g2.setColor(new Color(255, 100, 100)); // Rojo de latencia
                double angle = 360.0 * ((double)current / max);
                g2.fill(new Arc2D.Double(clockX, clockY, clockSize, clockSize, 90, angle, Arc2D.PIE));
                
                g2.setColor(Color.BLACK);
                g2.setFont(new Font("Arial", Font.BOLD, 10));
                g2.drawString("LATENCY", clockX - 5, clockY + clockSize + 12);
            }
        }
        
        // --- 3. DIBUJAR EL BUS ---
        g2.setStroke(new BasicStroke(4));
        g2.setColor(Color.DARK_GRAY);
        
        int busStart = cpuX + compSize;
        int busEnd = ramX;
        
        g2.drawLine(busStart, centerY - 30, busEnd, centerY - 30); // Bus Direcciones
        g2.drawLine(busStart, centerY + 30, busEnd, centerY + 30); // Bus Datos

        // Etiquetas de los Buses
        g2.setFont(new Font("Consolas", Font.ITALIC, 11));
        g2.setColor(Color.GRAY);
        g2.drawString("Bus de Direcciones (Address)", busStart + 50, centerY - 40);
        g2.drawString("Bus de Datos (Data)", busStart + 50, centerY + 50);

        // --- 4. ANIMACIÓN DEL PAQUETE DE DATOS ---
        CPUState state = simulator.getCurrentState();
        if (state == CPUState.FETCH_REQUEST || state == CPUState.FETCH_RESPONSE) {
            float progress = simulator.getBusProgress();
            
            // Posición
            int currentPacketX = busStart + (int)((busEnd - busStart) * progress);
            
            // Dibujar Bola
            g2.setColor(new Color(255, 165, 0)); // Naranja
            g2.fillOval(currentPacketX - 15, centerY - 45, 30, 30);
            
            // Dibujar Texto del Dato (HEX)
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Consolas", Font.BOLD, 11));
            // Centrar texto
            String dataText = simulator.getCurrentDataPacket();
            FontMetrics fm = g2.getFontMetrics();
            int txtW = fm.stringWidth(dataText);
            g2.drawString(dataText, currentPacketX - (txtW/2), centerY - 26);
        }

        // --- 5. AVISO DE CUELLO DE BOTELLA ---
        if (state == CPUState.MEMORY_ACCESS && simulator.getMaxRamWait() > 20) {
            g2.setColor(new Color(200, 0, 0, 180));
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            String warning = "¡CUELLO DE BOTELLA!";
            g2.drawString(warning, width/2 - 70, centerY - 80);
        }
    }
}