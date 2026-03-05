package cmsc125.lab3.services;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.net.URL;

public class AudioService {
    private Clip currentSfxClip, currentBgmClip;

    // Play file as SFX, applying current volume and mute settings
    public void playSFX(String filePath, int volumeLevel, boolean isEnabled) {
        if (!isEnabled) return; // Don't play if disabled

        try {
            // Look for audio file in resources folder
            URL audioURL = getClass().getResource(filePath);
            if (audioURL == null) {
                System.err.println("Warning: Could not find audio file at " + filePath);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioURL);
            currentSfxClip = AudioSystem.getClip();
            currentSfxClip.open(audioStream);

            setClipVolume(currentSfxClip, volumeLevel);
            currentSfxClip.start();

        } catch (Exception e) {
            System.err.println("Error playing SFX: " + e.getMessage());
        }
    }

    // Live update for SFX volume (if currently playing)
    public void updateSfxVolume(int volumeLevel, boolean isEnabled) {
        if (currentSfxClip != null && currentSfxClip.isOpen()) {
            if (!isEnabled || volumeLevel == 0) setClipVolume(currentSfxClip, 0); // Mute
            else setClipVolume(currentSfxClip, volumeLevel);
        }
    }

    public void stopSFX() {
        if (currentSfxClip != null && currentSfxClip.isRunning()) {
            currentSfxClip.stop();
            currentSfxClip.close();
        }
    }

    //  Method for audio volume adjustment
    private void setClipVolume(Clip clip, int volumeLevel) {
        if (clip == null || !clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) return;

        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

        if (volumeLevel <= 0) gainControl.setValue(gainControl.getMinimum()); // Full mute
        else {
            // Convert 0-100 to Decibels (Logarithmic scale)
            float decibels = 20f * (float) Math.log10(volumeLevel / 100f);
            gainControl.setValue(decibels);
        }
    }
}