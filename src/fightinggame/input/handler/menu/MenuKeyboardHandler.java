package fightinggame.input.handler.menu;

import fightinggame.Game;
import static fightinggame.Game.STATE;
import fightinggame.entity.state.GameState;
import fightinggame.resource.DataManager;
import fightinggame.resource.ImageManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MenuKeyboardHandler implements KeyListener {

    private final Game game;
    private String[] buttons = {"Start", "Load Game", "Quit"};
    private int currIndex = 0;
    private Image image;
    private int width, height;
    private int firstY;
    private int x, y;
    private Font customFont;

    public MenuKeyboardHandler(Game game, int width, int height) {
        this.width = width;
        this.height = height;
        this.game = game;
        firstY = height / 2 - 50;
        x = width / 3 + 190;
        y = firstY + 80;
        image = ImageManager.loadImage("assets/res/background/Menu/Background.png");
        customFont = DataManager.getFont(55);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (Game.STATE == GameState.MENU_STATE) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    if (currIndex > 0) {
                        currIndex--;
                    } else {
                        currIndex = buttons.length - 1;
                    }
                    game.repaint();
                    break;

                case KeyEvent.VK_DOWN:
                    if (currIndex < buttons.length - 1) {
                        currIndex++;
                    } else {
                        currIndex = 0;
                    }
                    game.repaint();
                    break;
                case KeyEvent.VK_ENTER:
                    String button = buttons[currIndex];
                    switch (button) {
                        case "Start":
                            game.start();
                            break;
                        case "Load Game":
                            break;
                        case "Quit":
                            game.dispose();
                            break;
                    }
                    break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public void render(Graphics g) {
        if (STATE == GameState.MENU_STATE) {
            g.setColor(Color.white);
            g.fillRect(0, 0, width, height);
            g.drawImage(image, 0, 0, width, height, null);
            g.setColor(new Color(133, 0, 0));
//            g.drawRect(width / 3 + 50, firstY, width / 4, height / 8);
//            g.drawRect(width / 3 + 50, firstY + height / 8 + 40, width / 4, height / 8);
//            g.drawRect(width / 3 + 50, firstY + (height / 8 + 40) * 2, width / 4, height / 8);
            g.setFont(customFont);
            g.drawString(buttons[0], width / 3 + 190, firstY + 80);
            g.drawString(buttons[1], width / 3 + 120, firstY + height / 8 + 40 + 80);
            g.drawString(buttons[2], width / 3 + 200, firstY + (height / 8 + 40) * 2 + 80);
            g.setColor(Color.WHITE);
            switch (currIndex) {
                case 1:
                    x = width / 3 + 120;
                    y = firstY + height / 8 + 40 + 80;
                    break;
                case 2:
                    x = width / 3 + 200;
                    y = firstY + (height / 8 + 40) * 2 + 80;
                    break;
                default:
                    x = width / 3 + 190;
                    y = firstY + 80;
                    break;
            }
            g.drawString(buttons[currIndex], x, y);
        }
    }
}
