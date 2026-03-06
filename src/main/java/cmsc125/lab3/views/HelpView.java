package cmsc125.lab3.views;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.URL;

public class HelpView extends JPanel {
    private final JButton backBtn;

    public HelpView() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Help & Documentation");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        contentPanel.add(createSectionTitle("Description"));
        contentPanel.add(createBodyText(
                "<b>ChronOS</b> is a CPU Scheduling Algorithms Simulator created to visualize and analyze " +
                        "how an operating system manages process execution.<br><br>" +
                        "It supports First Come First Serve (FCFS), Round Robin, Shortest Job First (Preemptive & Non-preemptive), " +
                        "and Priority Scheduling (Preemptive & Non-preemptive).", false
        ));
        contentPanel.add(Box.createVerticalStrut(30));

        contentPanel.add(createSectionTitle("Step-by-Step Guide"));
        contentPanel.add(createBodyText("<b>Step 1:</b> On the Dashboard, click <b>Play</b> to start configuring your simulation.", false));
        contentPanel.add(Box.createVerticalStrut(15));

        contentPanel.add(createBodyText("<b>Step 2:</b> In the Setup Screen, select your <b>Data Source</b> (User Input, Text File, or Random) and choose your preferred <b>Algorithm</b>. If required, configure the Quantum or Priority Rules.", false));
        contentPanel.add(createImageLabel("/images/help_step2.png", "Place 'help_step2.png' in your resources/images folder"));
        contentPanel.add(Box.createVerticalStrut(15));

        contentPanel.add(createBodyText("<b>Step 3:</b> Define your processes. You must have between 3 to 20 processes. Valid constraints are: Burst Time (1-30), Arrival Time (0-30), and Priority Number (1-20 with NO duplicates).", false));
        contentPanel.add(createImageLabel("/images/help_step3.png", "Place 'help_step3.png' in your resources/images folder"));
        contentPanel.add(Box.createVerticalStrut(15));

        contentPanel.add(createBodyText("<b>Step 4:</b> Click <b>Proceed to Simulation</b> once your data is valid.", false));
        contentPanel.add(Box.createVerticalStrut(15));

        contentPanel.add(createBodyText("<b>Step 5:</b> Watch the Gantt Chart! Use the bottom controls to Play/Pause, change the simulation Speed (e.g., 2.0x, 5.0x), or Restart the batch to watch it again.", false));
        contentPanel.add(createImageLabel("/images/help_step5.png", "Place 'help_step5.png' in your resources/images folder"));
        contentPanel.add(Box.createVerticalStrut(40));

        contentPanel.add(createSectionTitle("About"));
        contentPanel.add(createBodyText(
                "<b>Developers:</b><br>" +
                        "• ali1x3<br>" +
                        "• ddrhckrzz<br>" +
                        "• Schneidelstrom<br>", true
        ));

        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(createHyperlink("View GitHub Source Code", "https://github.com/Schneidelstrom/Project2"));
        contentPanel.add(Box.createVerticalStrut(20));

        contentPanel.add(createBodyText(
                "<i>Copyright © 2026. Created solely for educational purposes for CMSC 125.</i>", true
        ));
        contentPanel.add(Box.createVerticalStrut(20));

        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        wrapperPanel.add(contentPanel, gbc);

        JScrollPane scrollPane = new JScrollPane(wrapperPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        backBtn = new JButton("← Back to Dashboard");
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 18));
        bottomPanel.add(backBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JLabel createSectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 26));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        return label;
    }

    private JLabel createBodyText(String htmlText, boolean isTextCentered) {
        String align = isTextCentered ? "center" : "justify";

        JLabel label = new JLabel("<html><div style='width: 700px; line-height: 1.5; text-align: " + align + ";'>" + htmlText + "</div></html>");
        label.setFont(new Font("SansSerif", Font.PLAIN, 18));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JLabel createImageLabel(String imagePath, String fallbackText) {
        JLabel label = new JLabel();
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        try {
            URL imgURL = getClass().getResource(imagePath);
            if (imgURL != null) {
                ImageIcon icon = new ImageIcon(imgURL);
                Image img = icon.getImage();
                int width = Math.min(icon.getIconWidth(), 700);
                int height = (width * icon.getIconHeight()) / icon.getIconWidth();
                Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                label.setIcon(new ImageIcon(scaledImg));
            } else label.setText("<html><div style='width: 700px; height: 100px; background-color: #555; color: white; text-align: center; line-height: 100px; border: 1px solid gray; font-family: SansSerif;'>" + fallbackText + "</div></html>");
        } catch (Exception e) {
            label.setText("Image missing: " + imagePath);
        }
        return label;
    }

    private JLabel createHyperlink(String text, String url) {
        JLabel label = new JLabel("<html><a href='" + url + "'>" + text + "</a></html>");
        label.setFont(new Font("SansSerif", Font.BOLD, 18));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));

        label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Could not open link: " + ex.getMessage());
                }
            }
        });
        return label;
    }

    public JButton getBackBtn() {
        return backBtn;
    }
}