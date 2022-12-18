package fightinggame.entity;

import fightinggame.resource.SpriteSheet;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.image.BufferedImage;

public class HealthBar {
    
    private int maxHealth;
    private int health;
    private boolean isDeath = false;
    private BufferedImage avatar;
    private SpriteSheet sheet;
    private BufferedImage curHealthImage;
    private Shape ovalImage;
    private Character character;
    private HealthBarState state;

    public HealthBar(BufferedImage avatar, SpriteSheet healthBar, Character character) {
        this.avatar = avatar;
        this.sheet = healthBar;
        this.character = character;
    }

    public void tick() {
        character.healthBarTick();
        if(maxHealth == health) {
            state = HealthBarState.MAX;
        } else if(maxHealth * 90/100 == health) {
            state = HealthBarState.H_90;
        } else if(maxHealth * 80/100 == health) {
            state = HealthBarState.H_80;
        } else if(maxHealth * 70/100 == health) {
            state = HealthBarState.H_70;
        } else if(maxHealth * 60/100 == health) {
            state = HealthBarState.H_60;
        } else if(maxHealth * 50/100 == health) {
            state = HealthBarState.H_50;
        } else if(maxHealth * 40/100 == health) {
            state = HealthBarState.H_40;
        } else if(maxHealth * 30/100 == health) {
            state = HealthBarState.H_30;
        } else if(maxHealth * 20/100 == health) {
            state = HealthBarState.H_20;
        } else if(maxHealth * 10/100 == health) {
            state = HealthBarState.H_10;
        } else {
            state = HealthBarState.H_0;
        }
        switch (state) {
            case MAX:
                curHealthImage = sheet.getImage(2);
                break;
            case H_90:
                curHealthImage = sheet.getImage(10);
                break;
            case H_80:
                curHealthImage = sheet.getImage(9);
                break;
            case H_70:
                curHealthImage = sheet.getImage(8);
                break;
            case H_60:
                curHealthImage = sheet.getImage(7);
                break;
            case H_50:
                curHealthImage = sheet.getImage(6);
                break;
            case H_40:
                curHealthImage = sheet.getImage(5);
                break;
            case H_30:
                curHealthImage = sheet.getImage(4);
                break;
            case H_20:
                curHealthImage = sheet.getImage(3);
                break;
            case H_10:
                curHealthImage = sheet.getImage(1);
                break;
            case H_0:
                curHealthImage = sheet.getImage(0);
                break;
        }
    }

    public void render(Graphics g, int xHBar, int yHBar, int widthHBar, int heightHBar,
        int xAvatar, int yAvatar, int widthAvatar, int heightAvatar) {
        g.setColor(Color.red);
        g.drawImage(curHealthImage, xHBar, yHBar, widthHBar, heightHBar, null);
        g.drawRect(xHBar, yHBar, widthHBar, heightHBar);
        g.setClip(ovalImage);
        g.drawImage(avatar, xAvatar, yAvatar, widthAvatar, heightAvatar, null);
        g.setClip(null);
        g.setColor(Color.white);
        g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 25));
        g.drawString(character.getName(), xHBar, yHBar + heightHBar + 25);
    }

    public HealthBar() {
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean isDeath() {
        return isDeath;
    }

    public void setIsDeath(boolean isDeath) {
        this.isDeath = isDeath;
    }

    public BufferedImage getAvatar() {
        return avatar;
    }

    public void setAvatar(BufferedImage avatar) {
        this.avatar = avatar;
    }

    public SpriteSheet getSheet() {
        return sheet;
    }

    public void setSheet(SpriteSheet sheet) {
        this.sheet = sheet;
    }

    public BufferedImage getCurHealthImage() {
        return curHealthImage;
    }

    public void setCurHealthImage(BufferedImage curHealthImage) {
        this.curHealthImage = curHealthImage;
    }

    public Shape getOvalImage() {
        return ovalImage;
    }

    public void setOvalImage(Shape ovalImage) {
        this.ovalImage = ovalImage;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public HealthBarState getState() {
        return state;
    }

    public void setState(HealthBarState state) {
        this.state = state;
    }
    
}
