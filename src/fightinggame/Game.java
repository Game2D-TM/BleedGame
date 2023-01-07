package fightinggame;

import fightinggame.entity.state.GameState;
import fightinggame.input.handler.menu.MenuKeyboardHandler;
import fightinggame.resource.DataManager;
import fightinggame.resource.ImageManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import javax.swing.JFrame;

public class Game extends JFrame {

    public enum ScreenState {
        fullscreen, borderless, windowed, none
    };

    public static ScreenState current;
    private static GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
    private static GraphicsDevice ev = env.getDefaultScreenDevice();
    public static final int FPS = 150;
    public static GameState STATE = GameState.MENU_STATE;

    private Gameplay gameplay;
    private boolean isRunning = true;
    private Thread gameThread;
    private Image image;
    private MenuKeyboardHandler menuHandler;

    public static void main(String[] args) {
        new Game(1650, 950);
    }

    public Game(int width, int height) throws HeadlessException {
        super("Bleed Game");
        setSize(width, height);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        DataManager.loadSceneData();
        image = ImageManager.loadImage("assets/res/background/Menu/Background.png");
//        changeWindowMode(ScreenState.fullscreen);
        menuHandler = new MenuKeyboardHandler(this);
        addKeyListener(menuHandler);
        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        if (STATE == GameState.MENU_STATE) {
            g.setColor(Color.white);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
            g.setColor(Color.red);
            int firstY = getHeight() / 2 - 50;
            g.drawRect(getWidth() / 3 + 50, firstY, getWidth() / 4, getHeight() / 8);
            g.drawRect(getWidth() / 3 + 50, firstY + getHeight() / 8 + 40, getWidth() / 4, getHeight() / 8);
            g.drawRect(getWidth() / 3 + 50, firstY + (getHeight() / 8 + 40) * 2, getWidth() / 4, getHeight() / 8);
            g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 80));
            g.drawString("Start", getWidth() / 3 + 140, firstY + 80);
            g.drawString("Options", getWidth() / 3 + 90, firstY + getHeight() / 8 + 40 + 80);
            g.drawString("Quit", getWidth() / 3 + 160, firstY + (getHeight() / 8 + 40) * 2 + 80);
        }
    }

    public void init() {
        gameplay = new Gameplay(this);
        gameplay.setSize(getWidth(), getHeight());
        gameplay.setPreferredSize(new Dimension(getWidth(), getHeight()));
        add(gameplay, BorderLayout.CENTER);
        pack();
        gameplay.initCamera();
        gameplay.initFirstScene();
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        exitGame();
    }

    private void exitGame() {
        isRunning = false;
        try {
            if (gameplay.getThread() != null && gameplay.getThread().isAlive()) {
                gameplay.getThread().interrupt();
                gameplay.getThread().join();
            }
            if (gameThread != null && gameThread.isAlive()) {
                gameThread.interrupt();
                gameThread.join();
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    public void goBackToMenu() {
        exitGame();
        remove(gameplay);
        removeKeyListener(menuHandler);
        gameplay = null;
        gameThread = null;
        STATE = GameState.MENU_STATE;
        menuHandler = new MenuKeyboardHandler(this);
        addKeyListener(menuHandler);
        paint(getGraphics());
    }

    public void start() {
        isRunning = true;
        init();
        STATE = GameState.GAME_STATE;
        if (gameplay != null) {
            if(gameThread != null && gameThread.isAlive()) {
                gameThread.interrupt();
            }
            gameThread = new Thread(gameplay);
            gameThread.start();
        }
    }

    public void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void changeWindowMode(ScreenState state) {
        if (state == ScreenState.fullscreen && current != ScreenState.fullscreen) {
            if (ev.isFullScreenSupported()) {
                ev.setFullScreenWindow(this);
            }
            current = ScreenState.fullscreen;
        }
        if (state == ScreenState.borderless && current != ScreenState.borderless) {
            setUndecorated(true);
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            current = ScreenState.borderless;
        }
        if (state == ScreenState.windowed && current != ScreenState.windowed) {
            // you can choose to make the screen fit or not
            if (ev.isFullScreenSupported()) {
                ev.setFullScreenWindow(null);
            }
//            setSize(1650, 950);
//            setLocationRelativeTo(null);
            current = ScreenState.windowed;
        }
//        if (gameplay != null) {
//            gameplay.setSize(getWidth(), getHeight());
//            gameplay.setPreferredSize(new Dimension(getWidth(), getHeight()));
//            gameplay.initCamera();
//        }
        System.out.println(getWidth() + " " + getHeight());
    }

    public Thread getGameThread() {
        return gameThread;
    }

}
