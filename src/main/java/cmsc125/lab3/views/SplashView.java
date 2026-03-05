package cmsc125.lab3.views;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.Timer;
import java.awt.*;

public class SplashView extends JPanel {
    private static final Font TITLE_FONT = new Font("Serif", Font.BOLD, 100);
    private final SpinningO oSpinner;

    public SplashView() {
        setLayout(new GridBagLayout()); // Center component on screen

        // Container to hold text and animation side-by-side
        JPanel textPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        textPanel.setOpaque(false);

        // "Chron" Label
        JLabel chronLabel = new JLabel("Chron");
        chronLabel.setFont(TITLE_FONT);
        chronLabel.setForeground(Color.black);

        // Custom "O" Loading Spinner
        oSpinner = new SpinningO();

        // "S" Label
        JLabel sLabel = new JLabel("S");
        sLabel.setFont(TITLE_FONT);
        sLabel.setForeground(Color.black);

        textPanel.add(chronLabel);
        textPanel.add(oSpinner);
        textPanel.add(sLabel);
        add(textPanel);
    }

    // Called by MainFrame when switching away from Splash Screen
    public void stopAnimation() {
        oSpinner.stopTimer();
    }

    // Custom inner class for spinning animation
    private class SpinningO extends JPanel {
        private int angle = 0;
        private final Timer animationTimer;

        public SpinningO() {
            setPreferredSize(new Dimension(80, 100));
            setOpaque(false);

            // Internal UI timer to update graphic "O" continuously
            animationTimer = new Timer(20, e -> {
                angle -= 5; // Spin clockwise

                if (angle <= -360) angle = 0; // Reset to prevent integer overflow over time

                repaint(); //  Trigger arc redraw
            });
            animationTimer.start();
        }

        public void stopTimer() {
            if (animationTimer != null && animationTimer.isRunning()) animationTimer.stop();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Cast to Graphics2D for better drawing features
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(Color.black);
            g2d.setStroke(new BasicStroke(12)); // Line thickness

            // Draw arc leaving 80-degree gap for spinner effect
            g2d.drawArc(7, 25, 60, 60, angle, 280);
        }
    }
}