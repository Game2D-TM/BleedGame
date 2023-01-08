package fightinggame.input.handler.menu;

import fightinggame.Game;
import static fightinggame.Game.STATE;
import fightinggame.entity.state.GameState;
import fightinggame.resource.ImageManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

public class MenuKeyboardHandler implements KeyListener {

    private final Game game;
    private String[] buttons = {"Start", "Options", "Quit"};
    private int currIndex = 0;
    private Image image;
    private Font customFont;
    private int width, height;
    private int x = width / 3 + 190 , firstY = height / 2 - 50 , y = firstY + 80;

    public MenuKeyboardHandler(Game game, int width, int height) {
        this.width = width;
        this.height = height;
        this.game = game;
        image = ImageManager.loadImage("assets/res/background/Menu/Background.png");
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(currIndex == 1) {
            y = firstY + height / 8 + 40 + 80;
        }
        else if(currIndex == 2) {
            y = (firstY + (height / 8 + 40) * 2 + 80);
        }
        else y = firstY + 80;
        
        if (Game.STATE == GameState.MENU_STATE) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    if (currIndex > 0) {
                        currIndex--;
                    } else {
                        currIndex = 2;
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (currIndex < 2) {
                        currIndex++;
                    } else {
                        currIndex = 0;
                    }
                    break;
                case KeyEvent.VK_ENTER:
                    String button = buttons[currIndex];
                    switch (button) {
                        case "Start":
                            game.start();
                            break;
                        case "Options":
                            break;
                        case "Quit":
                            game.dispose();
                            break;
                    }
                    break;
            }
            String button = buttons[currIndex];
            System.out.println(button);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public void render(Graphics g) {
        try {
            //create the font to use. Specify the size!
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/res/gui/font/AbaddonBold.ttf")).deriveFont(50f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //register the font
            ge.registerFont(customFont);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FontFormatException e) {
            e.printStackTrace();
        }
        if (STATE == GameState.MENU_STATE) {
            g.setColor(Color.white);
            g.fillRect(0, 0, width, height);
            g.drawImage(image, 0, 0, width, height, null);
            g.setColor(Color.red);
            g.drawRect(width / 3 + 50, firstY, width / 4, height / 8);
            g.drawRect(width / 3 + 50, firstY + height / 8 + 40, width / 4, height / 8);
            g.drawRect(width / 3 + 50, firstY + (height / 8 + 40) * 2, width / 4, height / 8);
            g.setFont(customFont);
            g.drawString("Start", width / 3 + 190, firstY + 80);
            g.drawString("Options", width / 3 + 190, firstY + height / 8 + 40 + 80);
            g.drawString("Quit", width / 3 + 190, firstY + (height / 8 + 40) * 2 + 80);
            g.setColor(Color.WHITE);
            g.drawString(buttons[currIndex], x, y);
        }

    }
}

