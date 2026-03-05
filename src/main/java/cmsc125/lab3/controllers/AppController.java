package cmsc125.lab3.controllers;

import cmsc125.lab3.models.SettingsModel;
import cmsc125.lab3.services.AudioService;
import cmsc125.lab3.views.DashboardView;
import cmsc125.lab3.views.MainFrame;
import cmsc125.lab3.views.SettingsView;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class AppController {
    private final MainFrame mainFrame;
    private final AudioService audioService;
    private final SettingsModel settingsModel;

    public AppController(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.audioService = new AudioService();
        this.settingsModel = new SettingsModel();

        syncSettingsViewWithModel();
        setupWindowExitListener();
        setupButtonListeners();
        setupSettingsListeners();
    }

    private void syncSettingsViewWithModel() {
        SettingsView settings = mainFrame.getSettingsView();

        // Sync SFX
        updateToggleButton(settings.getSfxToggleBtn(), settingsModel.isSfxEnabled());
        settings.getSfxSlider().setValue(settingsModel.getSfxVolume());
        settings.getSfxSpinner().setValue(settingsModel.getSfxVolume());

        // Sync BGM
        updateToggleButton(settings.getBgmToggleBtn(), settingsModel.isBgmEnabled());
        settings.getBgmSlider().setValue(settingsModel.getBgmVolume());
        settings.getBgmSpinner().setValue(settingsModel.getBgmVolume());
    }

    private void setupButtonListeners() {
        // Get dashboard view from main frame
        DashboardView dashboard = mainFrame.getDashboardView();

        // Attach listener to Play button
        dashboard.getPlayButton().addActionListener(e -> {
            System.out.println("Play button clicked! Transition to game screen here.");
        });

        // Attach listener to How To Play button
        dashboard.getHowToPlayButton().addActionListener(e -> {
            System.out.println("How to Play button clicked!");
        });

        // Open Settings
        dashboard.getSettingsButton().addActionListener(e -> {
            mainFrame.showSettings();
        });

        // Open confirmation when Exit button is clicked
        dashboard.getExitButton().addActionListener(e -> {
            confirmAndExit();
        });
    }

    private void setupSettingsListeners() {
        SettingsView settings = mainFrame.getSettingsView();

        // Back button
        settings.getBackBtn().addActionListener(e -> mainFrame.showDashboard());

        // SFX toggle button
        settings.getSfxToggleBtn().addActionListener(e -> {
            boolean newState = !settingsModel.isSfxEnabled();
            settingsModel.setSfxEnabled(newState);
            updateToggleButton(settings.getSfxToggleBtn(), newState);
            audioService.updateSfxVolume(settingsModel.getSfxVolume(), newState);
        });

        // SFX Slider (Real-time updates to Audio Service and Spinner)
        settings.getSfxSlider().addChangeListener(e -> {
            int val = settings.getSfxSlider().getValue();
            settingsModel.setSfxVolume(val);
            settings.getSfxSpinner().setValue(val); // Sync with spinner
            audioService.updateSfxVolume(val, settingsModel.isSfxEnabled());
        });

        // SFX Spinner (Real-time updates to Slider)
        settings.getSfxSpinner().addChangeListener(e -> {
            int val = (Integer) settings.getSfxSpinner().getValue();
            settingsModel.setSfxVolume(val);
            settings.getSfxSlider().setValue(val); // Sync with slider
            audioService.updateSfxVolume(val, settingsModel.isSfxEnabled());
        });

        // BGM logic
        settings.getBgmToggleBtn().addActionListener(e -> {
            boolean newState = !settingsModel.isBgmEnabled();
            settingsModel.setBgmEnabled(newState);
            updateToggleButton(settings.getBgmToggleBtn(), newState);
        });

        settings.getBgmSlider().addChangeListener(e -> {
            int val = settings.getBgmSlider().getValue();
            settingsModel.setBgmVolume(val);
            settings.getBgmSpinner().setValue(val);
        });

        settings.getBgmSpinner().addChangeListener(e -> {
            int val = (Integer) settings.getBgmSpinner().getValue();
            settingsModel.setBgmVolume(val);
            settings.getBgmSlider().setValue(val);
        });
    }

    // UI to switch colors and text for toggles
    private void updateToggleButton(JButton button, boolean isEnabled) {
        if (isEnabled) {
            button.setText("Enabled");
            button.setBackground(SettingsView.ENABLED_GREEN);
        } else {
            button.setText("Disabled");
            button.setBackground(SettingsView.DISABLED_RED);
        }
    }

    // Automatically save files when user exits
    private void setupWindowExitListener() {
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmAndExit();
                settingsModel.saveSettings();
            }
        });
    }

    private void confirmAndExit() {
        int choice = JOptionPane.showConfirmDialog(mainFrame, "Are you sure you want to exit ChronOS?", "Confirm Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);   // Show standard Yes/No confirmation dialog box

        // If the user clicked "Yes"
        if (choice == JOptionPane.YES_OPTION) {
            settingsModel.saveSettings(); // Save user preferences
            mainFrame.dispose();          // Destroy the window safely
            System.exit(0);         // Fully terminate the Java program
        }
    }

    // Splash screen 3 seconds delay for loading animation
    public void startApplication() {
        // Play opening sound using Model settings
        audioService.playSFX("/audio/opening.wav", settingsModel.getSfxVolume(), settingsModel.isSfxEnabled());

        Timer transitionTimer = new Timer(3000, e -> {
            audioService.stopSFX();     // Stop music exactly when timer finishes
            mainFrame.showDashboard();  // Switch View to Dashboard
        });

        transitionTimer.setRepeats(false); // Run once
        transitionTimer.start();
    }
}