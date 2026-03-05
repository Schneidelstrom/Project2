package cmsc125.lab3.controllers;

import cmsc125.lab3.views.DashboardView;
import cmsc125.lab3.views.MainFrame;
import cmsc125.lab3.services.AudioService;
import javax.swing.Timer;

public class AppController {
    private final MainFrame mainFrame;
    private final AudioService audioService;

    public AppController(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.audioService = new AudioService();
        setupButtonListeners();
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

        // Attach listener to Settings button
        dashboard.getSettingsButton().addActionListener(e -> {
            System.out.println("Settings button clicked!");
        });
    }

    // Splash screen 3 seconds delay for loading animation
    public void startApplication() {
        audioService.playMusic("/audio/opening.wav");
        Timer transitionTimer = new Timer(3000, e -> {
            audioService.stopMusic();   // Stop music exactly when timer finishes
            mainFrame.showDashboard();  // Switch View to Dashboard
        });

        transitionTimer.setRepeats(false); // Run once
        transitionTimer.start();
    }
}