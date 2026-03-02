package cmsc125.lab3.views;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Component;
import java.awt.Color;
import java.awt.Font;

public class DashboardView extends JPanel {

    public DashboardView() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("ChronOS");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 75));
        titleLabel.setForeground(Color.black);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel taglineLabel = new JLabel("Master the Gears of Time");
        taglineLabel.setFont(new Font("Serif", Font.ITALIC, 25));
        taglineLabel.setForeground(Color.black);
        taglineLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel iconsPanel = new JPanel();
        iconsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 250, 0));
        iconsPanel.setOpaque(false);

        iconsPanel.add(createIconGroup("Play"));
        iconsPanel.add(createIconGroup("How to Play"));
        iconsPanel.add(createIconGroup("Settings"));

        add(Box.createVerticalStrut(100));
        add(titleLabel);
        add(Box.createVerticalStrut(10));
        add(taglineLabel);
        add(Box.createVerticalStrut(500));
        add(iconsPanel);
    }

    // Method to create placeholder icon with text beneath it
    private JPanel createIconGroup(String labelText) {
        // Icon in CENTER, text in SOUTH
        JPanel groupPanel = new JPanel();
        groupPanel.setLayout(new BorderLayout(0, 10));
        groupPanel.setOpaque(false);

        // Placeholder for Image Icon
        JLabel iconPlaceholder = new JLabel();
        iconPlaceholder.setPreferredSize(new Dimension(100, 100));
        iconPlaceholder.setOpaque(true);
        iconPlaceholder.setBackground(new Color(212, 175, 55));
        iconPlaceholder.setBorder(BorderFactory.createLineBorder(new Color(43, 29, 20), 4));

        // Text Label beneath icon
        JLabel textLabel = new JLabel(labelText, SwingConstants.CENTER);
        textLabel.setFont(new Font("Serif", Font.BOLD, 20));
        textLabel.setForeground(new Color(43, 29, 20));

        groupPanel.add(iconPlaceholder, BorderLayout.CENTER);
        groupPanel.add(textLabel, BorderLayout.SOUTH);

        return groupPanel;
    }
}