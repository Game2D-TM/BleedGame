package fightinggame.entity;

import fightinggame.entity.state.StatusBarState;
import fightinggame.resource.DataManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class StatusBar {

    private int maxHealth;
    private int maxEnergy;
    private BufferedImage avatar;
    private SpriteSheet healthBarSheet;
    private SpriteSheet energyBarSheet;
    private BufferedImage healthStateImage;
    private BufferedImage energyStateImage;
    private Shape ovalImage;
    private Character character;
    private StatusBarState healthState;
    private StatusBarState energyState;
    private final Map<String, GamePosition> positions = new HashMap<>();
    private int healthBarShowCounter = 0;
    private int appearTimeLimit = 0;
    private boolean canShow = true;
    private Font customFont;

    public StatusBar(BufferedImage avatar, SpriteSheet healthBarSheet, SpriteSheet energyBarSheet, Character character, GamePosition statusBarPos,
            GamePosition avatarPos, int maxHealth, int maxEnergy) {
        this.avatar = avatar;
        this.healthBarSheet = healthBarSheet;
        this.energyBarSheet = energyBarSheet;
        this.character = character;
        this.maxHealth = maxHealth;
        this.maxEnergy = maxEnergy;
        positions.put("status_bar_pos", statusBarPos);
        positions.put("avatar_pos", avatarPos);
        positions.put("character_name", new GamePosition(statusBarPos.getXPosition(),
                statusBarPos.getMaxY() + 25, 0, 0));
        positions.put("character_health", new GamePosition(statusBarPos.getXPosition() + statusBarPos.getWidth() / 2 - 50,
                statusBarPos.getYPosition() + statusBarPos.getHeight() / 2 + 8, 0, 0));
        customFont = DataManager.getFont(20);
    }

    public StatusBar(BufferedImage avatar, SpriteSheet healthBarSheet, Character character, GamePosition statusBarPos,
            GamePosition avatarPos, int maxHealth, int maxEnergy) {
        this.maxHealth = maxHealth;
        this.maxEnergy = maxEnergy;
        this.avatar = avatar;
        this.healthBarSheet = healthBarSheet;
        this.character = character;
        positions.put("status_bar_pos", statusBarPos);
        positions.put("avatar_pos", avatarPos);
        positions.put("character_name", new GamePosition(statusBarPos.getXPosition(),
                statusBarPos.getMaxY() + 25, 0, 0));
        positions.put("character_health", new GamePosition(statusBarPos.getXPosition() + statusBarPos.getWidth() / 2 - 50,
                statusBarPos.getYPosition() + statusBarPos.getHeight() / 2 + 8, 0, 0));
        customFont = DataManager.getFont(20);
    }

    public void tick() {
        if (appearTimeLimit > 0 && canShow) {
            healthBarShowCounter++;
            if (healthBarShowCounter > appearTimeLimit) {
                canShow = false;
                healthBarShowCounter = 0;
            }
        }
        if (healthBarSheet != null && healthBarSheet.getImages().size() > 0) {
            healthState = getStatusBarState(maxHealth, character.getStats().getHealth(), healthState);
            healthStateImage = getCurStateImage(healthState, healthStateImage, healthBarSheet);
        }
        if (energyBarSheet != null && energyBarSheet.getImages().size() > 0) {
            energyState = getStatusBarState(maxEnergy, character.getStats().getEnergy(), energyState);
            energyStateImage = getCurStateImage(energyState, energyStateImage, energyBarSheet);
        }
    }

    public void render(Graphics g) {
        g.setColor(Color.red);
        if (healthStateImage != null) {
            g.drawImage(healthStateImage, getHealthBarPos().getXPosition(), getHealthBarPos().getYPosition(),
                    getHealthBarPos().getWidth(), getHealthBarPos().getHeight(), null);
            if (energyStateImage != null) {
                g.drawImage(energyStateImage, getHealthBarPos().getXPosition(), getHealthBarPos().getYPosition() + getHealthBarPos().getHeight(),
                        getHealthBarPos().getWidth(), getHealthBarPos().getHeight(), null);
            }
        }
//      rectangle
//        g.drawRect(getHealthBarPos().getXPosition(), getHealthBarPos().getYPosition(),
//                 getHealthBarPos().getWidth(), getHealthBarPos().getHeight());
        g.setClip(ovalImage);
        g.drawImage(avatar, getAvatarPos().getXPosition(), getAvatarPos().getYPosition(),
                getAvatarPos().getWidth(), getAvatarPos().getHeight(), null);
        g.setClip(null);
        g.setFont(customFont);
        g.setColor(Color.white);
        if (healthStateImage != null) {
            g.drawString(character.getStats().getHealth() + "/" + maxHealth,
                    getHealthPointPos().getXPosition(), getHealthPointPos().getYPosition());
        }
        if (energyStateImage != null) {
            g.drawString(character.getStats().getEnergy() + "/" + maxEnergy,
                    getHealthPointPos().getXPosition(), getHealthPointPos().getYPosition() + 80);
        }
        g.setColor(new Color(149, 1, 1));
        g.drawString(character.getName(), getNamePos().getXPosition() + 180, getNamePos().getYPosition() - 100);
        g.drawString("Level: " + character.getStats().getLevel(), getAvatarPos().getXPosition() + 20, getAvatarPos().getYPosition() + 135);
        if (character instanceof Player) {
            Player player = (Player) character;
            g.drawString("Score: " + player.getScore(), getAvatarPos().getXPosition() + 20, getAvatarPos().getYPosition() + 165);
        }
//        if (getPlayerScore() != null) {
//            g.drawString("EXP: " + character.getStats().getLevelExperience(), getPlayerScore().getXPosition() + 300, getPlayerScore().getYPosition() - 30);
//        }
        g.setFont(null);
        g.setColor(null);
    }

    public BufferedImage getCurStateImage(StatusBarState state, BufferedImage curStateImage, SpriteSheet sheet) {
        switch (state) {
            case MAX:
                curStateImage = sheet.getImage(2);
                break;
            case H_90:
                curStateImage = sheet.getImage(10);
                break;
            case H_80:
                curStateImage = sheet.getImage(9);
                break;
            case H_70:
                curStateImage = sheet.getImage(8);
                break;
            case H_60:
                curStateImage = sheet.getImage(7);
                break;
            case H_50:
                curStateImage = sheet.getImage(6);
                break;
            case H_40:
                curStateImage = sheet.getImage(5);
                break;
            case H_30:
                curStateImage = sheet.getImage(4);
                break;
            case H_20:
                curStateImage = sheet.getImage(3);
                break;
            case H_10:
                curStateImage = sheet.getImage(1);
                break;
            case H_0:
                curStateImage = sheet.getImage(0);
                break;
        }
        return curStateImage;
    }

    public StatusBarState getStatusBarState(int maxStatus, int status, StatusBarState state) {
        if (maxStatus <= status) {
            state = StatusBarState.MAX;
        } else if (status >= maxStatus * 90 / 100 && status < maxStatus) {
            state = StatusBarState.H_90;
        } else if (status >= maxStatus * 80 / 100 && status < maxStatus * 90 / 100) {
            state = StatusBarState.H_80;
        } else if (status >= maxStatus * 70 / 100 && status < maxStatus * 80 / 100) {
            state = StatusBarState.H_70;
        } else if (status >= maxStatus * 60 / 100 && status < maxStatus * 70 / 100) {
            state = StatusBarState.H_60;
        } else if (status >= maxStatus * 50 / 100 && status < maxStatus * 60 / 100) {
            state = StatusBarState.H_50;
        } else if (status >= maxStatus * 40 / 100 && status < maxStatus * 50 / 100) {
            state = StatusBarState.H_40;
        } else if (status >= maxStatus * 30 / 100 && status < maxStatus * 40 / 100) {
            state = StatusBarState.H_30;
        } else if (status >= maxStatus * 20 / 100 && status < maxStatus * 30 / 100) {
            state = StatusBarState.H_20;
        } else if (status > 0 && status < maxStatus * 20 / 100) {
            state = StatusBarState.H_10;
        } else {
            state = StatusBarState.H_0;
        }
        return state;
    }

    public void resetShowCounter() {
        healthBarShowCounter = 0;
        canShow = false;
    }

    public GamePosition getPlayerScore() {
        return positions.get("player_score");
    }

    public GamePosition getHealthBarPos() {
        return positions.get("status_bar_pos");
    }

    public GamePosition getAvatarPos() {
        return positions.get("avatar_pos");
    }

    public GamePosition setAvatarPos(GamePosition nPos) {
        return positions.put("avatar_pos", nPos);
    }

    public GamePosition getNamePos() {
        return positions.get("character_name");
    }

    public GamePosition getHealthPointPos() {
        return positions.get("character_health");
    }

    public StatusBar() {
    }

    public BufferedImage getAvatar() {
        return avatar;
    }

    public void setAvatar(BufferedImage avatar) {
        this.avatar = avatar;
    }

    public SpriteSheet getSheet() {
        return healthBarSheet;
    }

    public void setSheet(SpriteSheet sheet) {
        this.healthBarSheet = sheet;
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

    public StatusBarState getState() {
        return healthState;
    }

    public void setState(StatusBarState state) {
        this.healthState = state;
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

    public int getMaxEnergy() {
        return maxEnergy;
    }

    public void setMaxEnergy(int maxEnergy) {
        this.maxEnergy = maxEnergy;
    }

    public BufferedImage getHealthStateImage() {
        return healthStateImage;
    }

    public StatusBarState getHealthState() {
        return healthState;
    }

    public int getHealthBarShowCounter() {
        return healthBarShowCounter;
    }

    public SpriteSheet getHealthBarSheet() {
        return healthBarSheet;
    }

    public SpriteSheet getEnergyBarSheet() {
        return energyBarSheet;
    }

}
