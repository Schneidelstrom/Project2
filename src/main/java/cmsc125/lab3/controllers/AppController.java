package cmsc125.lab3.controllers;

import cmsc125.lab3.views.MainFrame;
import javax.swing.Timer;

public class AppController {
    private final MainFrame mainFrame;

    public AppController(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    // Splash screen 3 seconds delay for loading animation
    public void startApplication() {
        Timer transitionTimer = new Timer(1000, e -> mainFrame.showDashboard());

        transitionTimer.setRepeats(false);
        transitionTimer.start();
    }
}