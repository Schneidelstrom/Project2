package cmsc125.lab3.views;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;

public class MainFrame extends JFrame {
    // Constants for CardLayout routing
    private static final String VIEW_SPLASH = "SPLASH";
    private static final String VIEW_DASHBOARD = "DASHBOARD";
    private static final String VIEW_SETTINGS = "SETTINGS";

    private final CardLayout cardLayout;
    private final JPanel cardPanel;

    // Views references so controller can access them
    private final SplashView splashView;
    private final DashboardView dashboardView;
    private final SettingsView settingsView;

    public MainFrame() {
        setTitle("ChronOS");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Open in full screen

        // Set up CardLayout for panel swaps
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Instantiate Panel Views
        splashView = new SplashView();
        dashboardView = new DashboardView();
        settingsView = new SettingsView();

        cardPanel.add(splashView, VIEW_SPLASH);
        cardPanel.add(dashboardView, VIEW_DASHBOARD);
        cardPanel.add(settingsView, VIEW_SETTINGS);

        add(cardPanel);
    }

    // Method called by Controller to switch to Dashboard
    public void showDashboard() {
        cardLayout.show(cardPanel, VIEW_DASHBOARD);
        splashView.stopAnimation();
    }

    public void showSettings() {
        cardLayout.show(cardPanel, VIEW_SETTINGS);
    }

    public DashboardView getDashboardView() { return dashboardView; }
    public SettingsView getSettingsView() { return settingsView; }
}