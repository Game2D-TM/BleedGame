package fightinggame.entity;

import fightinggame.entity.state.HealthBarState;
import fightinggame.resource.DataManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class HealthBar {

    private int maxHealth;
    private BufferedImage avatar;
    private SpriteSheet sheet;
    private BufferedImage curHealthImage;
    private Shape ovalImage;
    private Character character;
    private HealthBarState state;
    private final Map<String, GamePosition> positions = new HashMap<>();
    private int healthBarShowCounter = 0;
    private int appearTimeLimit = 0;
    private boolean canShow = true;
    private Font customFont;

    public HealthBar(BufferedImage avatar, SpriteSheet healthBar, Character character, GamePosition healthBarPos,
            GamePosition avatarPos, int maxHealth) {
        this.avatar = avatar;
        this.sheet = healthBar;
        this.character = character;
        this.maxHealth = maxHealth;
        positions.put("health_bar_pos", healthBarPos);
        positions.put("avatar_pos", avatarPos);
        positions.put("character_name", new GamePosition(healthBarPos.getXPosition(),
                healthBarPos.getMaxY() + 25, 0, 0));
        positions.put("character_health", new GamePosition(healthBarPos.getXPosition() + healthBarPos.getWidth() / 2 - 50,
                healthBarPos.getYPosition() + healthBarPos.getHeight() / 2 + 8, 0, 0));
        customFont = DataManager.getFont(30);
    }

    public void tick() {
        character.healthBarTick();
        if (appearTimeLimit > 0 && canShow) {
            healthBarShowCounter++;
            if (healthBarShowCounter > appearTimeLimit) {
                canShow = false;
                healthBarShowCounter = 0;
            }
        }
        int health = character.getStats().getHealth();
        if (maxHealth == health) {
            state = HealthBarState.MAX;
        } else if (health > maxHealth * 90 / 100 && health <= maxHealth) {
            state = HealthBarState.H_90;
        } else if (health > maxHealth * 80 / 100 && health <= maxHealth * 90 / 100) {
            state = HealthBarState.H_80;
        } else if (health > maxHealth * 70 / 100 && health <= maxHealth * 80 / 100) {
            state = HealthBarState.H_70;
        } else if (health > maxHealth * 60 / 100 && health <= maxHealth * 70 / 100) {
            state = HealthBarState.H_60;
        } else if (health > maxHealth * 50 / 100 && health <= maxHealth * 60 / 100) {
            state = HealthBarState.H_50;
        } else if (health > maxHealth * 40 / 100 && health <= maxHealth * 50 / 100) {
            state = HealthBarState.H_40;
        } else if (health > maxHealth * 30 / 100 && health <= maxHealth * 40 / 100) {
            state = HealthBarState.H_30;
        } else if (health > maxHealth * 20 / 100 && health <= maxHealth * 30 / 100) {
            state = HealthBarState.H_20;
        } else if (health > 0 && health <= maxHealth * 20 / 100) {
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

    public void render(Graphics g){
        g.setColor(Color.red);
        g.drawImage(curHealthImage, getHealthBarPos().getXPosition(), getHealthBarPos().getYPosition(),
                getHealthBarPos().getWidth(), getHealthBarPos().getHeight(), null);
//      rectangle
//        g.drawRect(getHealthBarPos().getXPosition(), getHealthBarPos().getYPosition(),
//                 getHealthBarPos().getWidth(), getHealthBarPos().getHeight());
        g.setClip(ovalImage);
        g.drawImage(avatar, getAvatarPos().getXPosition(), getAvatarPos().getYPosition(),
                getAvatarPos().getWidth(), getAvatarPos().getHeight(), null);
        g.setClip(null);
        g.setFont(customFont);
        g.setColor(Color.white);
        g.drawString(character.getStats().getHealth() + "/" + maxHealth,
                getHealthPointPos().getXPosition(), getHealthPointPos().getYPosition());
        g.setColor(new Color(149, 1, 1));
        g.drawString(character.getName(), getNamePos().getXPosition()+180, getNamePos().getYPosition()-100);
        g.drawString("Level: " + character.getStats().getLevel(), getNamePos().getXPosition(), getNamePos().getYPosition());
        if (getPlayerScore() != null) {
            g.drawString("EXP: " + character.getStats().getLevelExperience(), getPlayerScore().getXPosition() + 300, getPlayerScore().getYPosition()-30);
        }
        g.setFont(null);
        g.setColor(null);
    }

    public void resetShowCounter() {
        healthBarShowCounter = 0;
        canShow = false;
    }

    public GamePosition getPlayerScore() {
        return positions.get("player_score");
    }

    public GamePosition getHealthBarPos() {
        return positions.get("health_bar_pos");
    }

    public GamePosition getAvatarPos() {
        return positions.get("avatar_pos");
    }

    public GamePosition getNamePos() {
        return positions.get("character_name");
    }

    public GamePosition getHealthPointPos() {
        return positions.get("character_health");
    }

    public HealthBar() {
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

    public Map<String, GamePosition> getPositions() {
        return positions;
    }

    public int getAppearTimeLimit() {
        return appearTimeLimit;
    }

    public void setAppearTimeLimit(int appearTimeLimit) {
        this.appearTimeLimit = appearTimeLimit;
    }

    public boolean isCanShow() {
        return canShow;
    }

    public void setCanShow(boolean canShow) {
        this.canShow = canShow;
    }

}
