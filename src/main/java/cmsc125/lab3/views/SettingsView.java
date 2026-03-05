package cmsc125.lab3.views;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SwingConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.ImageIcon;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Color;
import java.awt.Font;
import java.awt.Component;
import java.awt.Cursor;
import java.net.URL;

public class SettingsView extends JPanel {
    public static final Color ENABLED_GREEN = new Color(175, 255, 175);
    public static final Color DISABLED_RED = new Color(255, 175, 175);
    private final JButton bgmToggleBtn, sfxToggleBtn, backBtn;
    private final JSlider bgmSlider, sfxSlider;
    private final JSpinner bgmSpinner, sfxSpinner;

    public SettingsView() {
        setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Settings");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 50));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create rows for BGM and SFX
        JPanel bgmRow = createSettingsRow("Background Music (BGM)", true);
        bgmToggleBtn = (JButton) bgmRow.getClientProperty("toggleBtn");
        bgmSlider = (JSlider) bgmRow.getClientProperty("slider");
        bgmSpinner = (JSpinner) bgmRow.getClientProperty("spinner");

        JPanel sfxRow = createSettingsRow("Sound Effects (SFX)", false);
        sfxToggleBtn = (JButton) sfxRow.getClientProperty("toggleBtn");
        sfxSlider = (JSlider) sfxRow.getClientProperty("slider");
        sfxSpinner = (JSpinner) sfxRow.getClientProperty("spinner");

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(titleLabel);
        centerPanel.add(Box.createVerticalStrut(50));
        centerPanel.add(bgmRow);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(sfxRow);
        centerPanel.add(Box.createVerticalGlue());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 30));
        bottomPanel.setOpaque(false);

        backBtn = createIconButton("Back", "back.png");
        bottomPanel.add(backBtn);

        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Method to create aligned row for settings
    private JPanel createSettingsRow(String labelText, boolean isBgm) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Serif", Font.PLAIN, 20));
        label.setPreferredSize(new Dimension(250, 30));

        // Toggle Button
        JButton toggleBtn = new JButton("Enabled");
        toggleBtn.setBackground(ENABLED_GREEN);
        toggleBtn.setFocusPainted(false);
        toggleBtn.setPreferredSize(new Dimension(120, 40));

        // Slider (0 to 100)
        JSlider slider = new JSlider(0, 100, 100);
        slider.setPreferredSize(new Dimension(300, 40));

        // Spinner (Manual number input, restricted 0-100)
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(100, 0, 100, 1);
        JSpinner spinner = new JSpinner(spinnerModel);
        spinner.setPreferredSize(new Dimension(60, 30));

        row.add(label);
        row.add(toggleBtn);
        row.add(slider);
        row.add(spinner);

        // Store references in the panel so can extract easily
        row.putClientProperty("toggleBtn", toggleBtn);
        row.putClientProperty("slider", slider);
        row.putClientProperty("spinner", spinner);

        return row;
    }

    private JButton createIconButton(String labelText, String iconFileName) {
        JButton button = new JButton(labelText);

        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setFont(new Font("Serif", Font.BOLD, 18));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        URL imgURL = getClass().getResource("/icons/" + iconFileName);

        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL);
            Image img = icon.getImage();
            Image scaledImg = img.getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledImg));
        } else System.err.println("Warning: Could not find image " + iconFileName);

        return button;
    }

    // Getters for Controller to attach listeners
    public JButton getBgmToggleBtn() { return bgmToggleBtn; }
    public JButton getSfxToggleBtn() { return sfxToggleBtn; }
    public JButton getBackBtn() { return backBtn; }
    public JSlider getBgmSlider() { return bgmSlider; }
    public JSlider getSfxSlider() { return sfxSlider; }
    public JSpinner getBgmSpinner() { return bgmSpinner; }
    public JSpinner getSfxSpinner() { return sfxSpinner; }
}