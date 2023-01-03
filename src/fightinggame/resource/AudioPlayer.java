package fightinggame.resource;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioPlayer implements Runnable {

    // to store current position
    public Map<String, Clip> audios = new HashMap();
    private Long currentFrame;
    private Clip clip;

    // current status of clip
    private String status;
    private boolean isLoop;
    private float volume;
    private String folderPath;
    private String name;
    private Thread thread;

    // constructor to initialize streams and clip
    public AudioPlayer(String folder) {
        this.folderPath = folder;
        if (folder != null && !folder.isBlank()) {
            File file = new File(folder);
            if (file.listFiles() != null && file.listFiles().length > 0) {
                for (int i = 0; i < file.listFiles().length; i++) {
                    File sfile = file.listFiles()[i];
                    if (sfile.exists() && sfile.isFile()) {
                        AudioInputStream audioInputStream = null;
                        try {
                            // create AudioInputStream object
                            audioInputStream = AudioSystem.getAudioInputStream(sfile.getAbsoluteFile());
                        } catch (UnsupportedAudioFileException ex) {
                            Logger.getLogger(AudioPlayer.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(AudioPlayer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        if (audioInputStream == null) {
                            continue;
                        }
                        try {
                            // create clip reference
                            clip = AudioSystem.getClip();
                        } catch (LineUnavailableException ex) {
                            Logger.getLogger(AudioPlayer.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        try {
                            // open audioInputStream to the clip
                            clip.open(audioInputStream);
                        } catch (LineUnavailableException ex) {
                            Logger.getLogger(AudioPlayer.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(AudioPlayer.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        String name = "";
                        if (sfile.getName().contains(".wav")) {
                            name = sfile.getName().replace(".wav", "");
                        } else if (sfile.getName().contains(".mp3")) {
                            name = sfile.getName().replace(".mp3", "");
                        }
                        audios.put(name, clip);
                    }
                }
            }
        }
    }

    public void startThread(String name, boolean isLoop, float volume) {
        this.name = name;
        this.isLoop = isLoop;
        this.volume = volume;
        thread = new Thread(this);
        thread.start();
    }

    public boolean closeThread() {
        if (thread != null) {
            restart();
            thread.interrupt();
            return true;
        }
        return false;
    }

    public void play(String name) {
        //start the clip
        clip = audios.get(name);
        if (volume > 0) {
            setVolume(volume);
        }
        clip.setFramePosition(0);
        clip.start();

        status = "play";
        if (isLoop) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public float getVolume() {
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        return (float) Math.pow(10f, gainControl.getValue() / 20f);
    }

    public void setVolume(float volume) {
        if (volume < 0f || volume > 1f) {
            throw new IllegalArgumentException("Volume not valid: " + volume);
        }
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        float gain = (range * volume) + gainControl.getMinimum();
        gainControl.setValue(gain);
    }

    // Method to pause the audio
    public void pause() {
        if (status.equals("paused")) {
            return;
        }
        this.currentFrame
                = this.clip.getMicrosecondPosition();
        clip.stop();
        status = "paused";
    }

    // Method to resume the audio
    public void resumeAudio() throws UnsupportedAudioFileException,
            IOException, LineUnavailableException {
        if (status.equals("play")) {
            return;
        }
        clip.close();
        clip.setMicrosecondPosition(currentFrame);
    }

    // Method to restart the audio
    public void restart() {
        clip.stop();
        clip.close();
        currentFrame = 0L;
        clip.setMicrosecondPosition(0);
    }

    // Method to stop the audio
    public void stop() throws UnsupportedAudioFileException,
            IOException, LineUnavailableException {
        currentFrame = 0L;
        clip.stop();
        clip.close();
    }

    // Method to jump over a specific part
    public void jump(long c) throws UnsupportedAudioFileException, IOException,
            LineUnavailableException {
        if (c > 0 && c < clip.getMicrosecondLength()) {
            clip.stop();
            clip.close();
            currentFrame = c;
            clip.setMicrosecondPosition(c);
        }
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String filePath) {
        this.folderPath = filePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        if (audios.size() > 0 && name != null && !name.isBlank()) {
            play(name);
        }
    }

}
