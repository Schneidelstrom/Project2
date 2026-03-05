package cmsc125.lab3.services;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class AudioService {
    private Clip currentClip; // Holds audio in memory to play/stop it

    // Method to load and play .wav file
    public void playMusic(String filePath) {
        try {
            // Look for audio file in resources folder
            URL audioURL = getClass().getResource(filePath);
            if (audioURL == null) {
                System.err.println("Warning: Could not find audio file at " + filePath);
                return; // Exit to avoid crash
            }

            // Load audio and open clip
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioURL);
            currentClip = AudioSystem.getClip();
            currentClip.open(audioStream);

            // Start playing
            currentClip.start();

        } catch (Exception e) {
            System.err.println("Error playing audio: " + e.getMessage());
        }
    }

    // Method to safely stop music and free up computer memory
    public void stopMusic() {
        if (currentClip != null && currentClip.isRunning()) {
            currentClip.stop();
            currentClip.close();
        }
    }
}
