package fightinggame;

import java.awt.Dimension;
import java.awt.HeadlessException;
import javax.swing.JFrame;

public class Game extends JFrame {

    public static final int FPS = 144;
    private Gameplay gameplay;
    private boolean isRunning = true;
    private Thread gameThread;

    public Game(int width, int height) throws HeadlessException {
        super("Fighting Game");
        setSize(width, height);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        init();
        setVisible(true);
    }

    public void init() {
        gameplay = new Gameplay(this, getWidth(), getHeight());
        gameplay.setPreferredSize(new Dimension(getWidth(), getHeight()));
        add(gameplay);
        pack();
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        isRunning = false;
        if (gameplay.getThread() != null && gameplay.getThread().isAlive()) {
            gameplay.getThread().suspend();
        }
        if (gameThread != null && gameThread.isAlive()) {
            gameThread.suspend();
        }
    }

    public void start() {
        gameThread = new Thread(gameplay);
        gameThread.start();
    }

    public void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public boolean isRunning() {
        return isRunning;
    }
}
