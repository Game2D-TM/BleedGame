package fightinggame;

import fightinggame.entity.Background;
import fightinggame.resource.ImageManager;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.HeadlessException;
import javax.swing.JFrame;

public class Game extends JFrame implements Runnable {
    
    public static final int FPS = 144;
    
    private Background background;
    private Gameplay gameplay;
    private boolean isRunning = true;

    public Game(int width, int height) throws HeadlessException {
        super("Fighting Game");
        setVisible(true);
        setSize(width, height);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        isRunning = false;
        if (gameplay.getThread() != null && gameplay.getThread().isAlive()) {
            gameplay.getThread().suspend();
        }
    }

    public void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void init() {
        background = new Background(0, "Street",
                ImageManager.loadImagesFromFolderToMap("assets/res/background/Street"), getWidth(), getHeight());
        gameplay = new Gameplay(this);
    }

    public void tick() {
        gameplay.tick();
    }

    public void render(Graphics g) throws InterruptedException {
        if (background != null) {
            background.render(g);
        }
        g.setColor(Color.red);
        g.drawRect(gameplay.getPlayPosition().getXPosition(), gameplay.getPlayPosition().getYPosition(),
                gameplay.getPlayPosition().getWidth(), gameplay.getPlayPosition().getHeight());
        gameplay.render(g);
    }

    @Override
    public void run() {

    }

}
