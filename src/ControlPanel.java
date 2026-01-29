import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * Panel con los controles, estadísticas.
 */
public class ControlPanel extends JPanel {
    
    private VonNeumannSimulator simulator;
    private JSlider sldCpuFreq, sldRamLatency;
    private JLabel lblEfficiency, lblIdleTime;
    private JButton btnStart, btnStop;
    
    // Bordes con títulos dinámicos
    private TitledBorder borderCpu, borderRam;

    public ControlPanel(VonNeumannSimulator simulator) {
        this.simulator = simulator;
        
        setLayout(new GridLayout(3, 1));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initControls();
        initStats();
        initGraph();
    }

    private void initControls() {
        JPanel pnlControls = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        
        // Slider Frecuencia CPU
        sldCpuFreq = new JSlider(10, 100, 50);
        borderCpu = BorderFactory.createTitledBorder("Frecuencia CPU: 50 Hz");
        sldCpuFreq.setBorder(borderCpu);
        sldCpuFreq.setPreferredSize(new Dimension(250, 50));
        sldCpuFreq.addChangeListener(e -> {
            int val = sldCpuFreq.getValue();
            simulator.setCpuSpeedDelay(110 - val);
            borderCpu.setTitle("Frecuencia CPU: " + val + " Hz"); // Actualiza texto
            sldCpuFreq.repaint();
        });
        
        // Slider Latencia RAM
        sldRamLatency = new JSlider(0, 200, 100);
        borderRam = BorderFactory.createTitledBorder("Latencia RAM: 100 ms");
        sldRamLatency.setBorder(borderRam);
        sldRamLatency.setPreferredSize(new Dimension(250, 50));
        sldRamLatency.addChangeListener(e -> {
            int val = sldRamLatency.getValue();
            simulator.setRamLatency(val);
            borderRam.setTitle("Latencia RAM: " + val + " ms"); // Actualiza texto
            sldRamLatency.repaint();
        });

        btnStart = new JButton("Iniciar Simulación");
        btnStart.setBackground(new Color(100, 200, 100));
        btnStart.setForeground(Color.WHITE);
        btnStart.setFocusPainted(false);
        btnStart.addActionListener(e -> {
            simulator.startSimulation();
            btnStart.setEnabled(false);
            btnStop.setEnabled(true);
        });

        btnStop = new JButton("Detener");
        btnStop.setBackground(new Color(200, 80, 80));
        btnStop.setForeground(Color.WHITE);
        btnStop.setFocusPainted(false);
        btnStop.setEnabled(false);
        btnStop.addActionListener(e -> {
            simulator.stopSimulation();
            btnStart.setEnabled(true);
            btnStop.setEnabled(false);
        });

        pnlControls.add(sldCpuFreq);
        pnlControls.add(sldRamLatency);
        pnlControls.add(btnStart);
        pnlControls.add(btnStop);
        
        add(pnlControls);
    }

    private void initStats() {
        JPanel pnlStats = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 5));
        lblEfficiency = new JLabel("Eficiencia de CPU: 100.0%");
        lblEfficiency.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblEfficiency.setForeground(new Color(0, 100, 0));
        
        lblIdleTime = new JLabel("Ciclos Desperdiciados (Idle): 0");
        lblIdleTime.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        pnlStats.add(lblEfficiency);
        pnlStats.add(lblIdleTime);
        add(pnlStats);
    }

    private void initGraph() {
        JPanel pnlGraph = new JPanel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fondo
                g2.setColor(new Color(230, 230, 230));
                g2.fillRoundRect(10, 10, getWidth()-20, 25, 10, 10);
                
                double eff = simulator.getEfficiency();
                
                // Color degradado según eficiencia
                if (eff > 80) g2.setColor(new Color(50, 200, 50));
                else if (eff > 50) g2.setColor(new Color(230, 180, 50));
                else g2.setColor(new Color(220, 50, 50));
                
                int barWidth = (int)((getWidth()-20) * (eff / 100.0));
                g2.fillRoundRect(10, 10, barWidth, 25, 10, 10);
                
                // Borde
                g2.setColor(Color.GRAY);
                g2.drawRoundRect(10, 10, getWidth()-20, 25, 10, 10);
            }
        };
        add(pnlGraph);
    }

    public void updateStatsUI() {
        double eff = simulator.getEfficiency();
        lblEfficiency.setText(String.format("Eficiencia de CPU: %.1f%%", eff));
        
        // Cambiar color del texto si baja mucho
        if (eff < 50) lblEfficiency.setForeground(Color.RED);
        else lblEfficiency.setForeground(new Color(0, 100, 0));
        
        lblIdleTime.setText("Ciclos Desperdiciados (Idle): " + simulator.getIdleCycles());
        repaint();
    }
}