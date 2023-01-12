package fightinggame.entity;

import fightinggame.Gameplay;
import fightinggame.resource.DataManager;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import java.util.List;

public class Dialogue {

    private List<String> texts;
    private int index = 0;
    private Gameplay gameplay;
    private Character character;
    private GamePosition position;
    private boolean endDialogue;

    public Dialogue(Character character, Gameplay gameplay) {
        this.gameplay = gameplay;
        this.character = character;
        position = new GamePosition(150,
                gameplay.getHeight() / 2 + gameplay.getHeight() / 3 - 70,
                gameplay.getWidth() - 300,
                gameplay.getHeight() - (gameplay.getHeight() / 2 + gameplay.getHeight() / 3) + 30);
    }

    public void tick() {

    }

    public void render(Graphics g) {
        try {
            if (haveDialogue()) {
                if (!endDialogue) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setColor(new Color(0, 0, 0, 90));
                    g2.fillRoundRect(position.getXPosition(), position.getYPosition(),
                            position.getWidth(), position.getHeight(), 35, 35);
                    g2.setColor(Color.white);
                    g2.setStroke(new BasicStroke(5));
                    g2.drawRoundRect(position.getXPosition(), position.getYPosition(),
                            position.getWidth(), position.getHeight(), 25, 25);
                    g2.setColor(new Color(0, 0, 0, 100));
                    g2.fillRoundRect(position.getXPosition() + 50, position.getYPosition() - 50,
                            420, 45, 10, 10);
                    g2.drawImage(character.getAvatar(), position.getXPosition() + 25,
                            position.getYPosition() - 150, 150, 150, null);
                    g2.setColor(Color.white);
                    g2.setFont(DataManager.getFont(35f));
                    g2.drawString(character.getName(), position.getXPosition() + 185, position.getYPosition() - 15);
                    g2.setFont(DataManager.getFont(25f));
                    int yText = position.getYPosition() + position.getHeight() / 3 - 15;
                    for (String split : texts.get(index).split("\n")) {
                        g2.drawString(split, position.getXPosition() + 50, yText);
                        yText += 30;
                    }
                    if (character instanceof Player) {
                        g2.drawString("space to continue", position.getMaxX() - 250, position.getMaxY() - 30);
                    }
                }
            }
        } catch (Exception ex) {

        }
    }

    public boolean haveDialogue() {
        if (texts == null || texts.isEmpty()) {
            return false;
        }
        return true;
    }

    public void next() {
        if (haveDialogue()) {
            if (index < texts.size() - 1) {
                index++;
            } else {
                index = 0;
                endDialogue = true;
                texts = null;
            }
        }
    }

    public boolean loadDialogue(File file) {
        texts = DataManager.readFileToList(file);
        return haveDialogue();
    }

    public boolean isEndDialogue() {
        return endDialogue;
    }

    public void setEndDialogue(boolean endDialogue) {
        if (endDialogue) {
            index = 0;
            texts = null;
        }
        this.endDialogue = endDialogue;
    }

    public List<String> getTexts() {
        return texts;
    }

    public void setTexts(List<String> texts) {
        this.texts = texts;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Gameplay getGameplay() {
        return gameplay;
    }

    public void setGameplay(Gameplay gameplay) {
        this.gameplay = gameplay;
    }

}
