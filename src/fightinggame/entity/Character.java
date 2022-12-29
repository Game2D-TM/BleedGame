package fightinggame.entity;

import fightinggame.entity.inventory.Inventory;
import fightinggame.Gameplay;
import fightinggame.entity.ability.Ability;
import fightinggame.entity.platform.Platform;
import fightinggame.input.handler.Handler;
import fightinggame.resource.ImageManager;
import fightinggame.resource.SpriteSheet;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Character {

    protected int id;
    protected String name;
    protected boolean isLTR;
    protected GamePosition position;
    protected Animation currAnimation;
    protected Map<CharacterState, Animation> animations;
    protected List<Handler> controller = new ArrayList<>();
    protected Map<String, BufferedImage> characterAssets;
    protected final Inventory inventory;
    protected final List<Ability> abilities = new ArrayList<>();
    protected BufferedImage avatar;
    protected HealthBar healthBar;
    protected boolean isAttacked = false;
    protected boolean isAttack = false;
    protected boolean isDeath = false;
    protected int receiveDamage = 0;
    protected int receiveDamageRenderTick = 0;
    protected Gameplay gameplay;
    protected Platform insidePlatform = null;
    protected Platform standPlatform = null;
    protected int jumpSpeed = 100;
    protected float vely = 0;
    protected int jumpFlySpeed = 4;
    protected boolean inAir = false;
    protected boolean fallDown = false;
    protected Stats stats;

    public Character(int id, String name, int health, GamePosition position, Map<CharacterState, Animation> animations, Map<String, BufferedImage> characterAssets,
            Gameplay gameplay, boolean isLTR, SpriteSheet inventorySheet) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.animations = animations;
        this.characterAssets = characterAssets;
        this.gameplay = gameplay;
        this.isLTR = isLTR;
        stats = new Stats(this, 1, 0, 10, 5, 100, 30, 0, 0, 0);
        inventory = new Inventory(this, inventorySheet, gameplay);
        if (isLTR) {
            currAnimation = animations.get(CharacterState.IDLE_LTR);
            avatar = ImageManager.loadImage(new File("assets/res/gui/avatar/avatar.png"));
        } else {
            currAnimation = animations.get(CharacterState.IDLE_RTL);
            avatar = ImageManager.loadImage(new File("assets/res/gui/avatar/avatar.png"));
        }
    }

    protected abstract void healthBarInit(int maxHealth);

    public abstract void healthBarTick();

    public void tick() {
        if (currAnimation != null) {
            currAnimation.tick();
        }
        if (insidePlatform != null) {
            checkPlatForm(gameplay.getSurroundPlatform(insidePlatform.getRow(), insidePlatform.getColumn()));
        }
        if (controller.size() > 0) {
            for (int i = 0; i < controller.size(); i++) {
                Handler handler = controller.get(i);
                handler.tick();
            }
        }
        if (abilities.size() > 0) {
            for (int i = 0; i < abilities.size(); i++) {
                Ability ablity = abilities.get(i);
                ablity.tick();
            }
        }
        if (inventory != null) {
            inventory.tick();
        }
        checkStandPlatform();
    }

    public void checkPlatForm(List<List<Platform>> scene) {
        try {
            if (scene != null && scene.size() > 0) {
                boolean isSet = false;
                for (int i = 0; i < scene.size(); i++) {
                    List<Platform> platforms = scene.get(i);
                    if (platforms != null && platforms.size() > 0) {
                        for (int j = 0; j < platforms.size(); j++) {
                            Platform platform = platforms.get(j);
                            if (platform != null) {
//                            GamePosition playerPos = new GamePosition(
//                                    position.getXPosition(),
//                                    position.getYPosition() + position.getHeight() / 2,
//                                    position.getWidth(), position.getHeight() / 2);
                                GamePosition playerPos = new GamePosition(getXHitBox(), getYHitBox() + 50,
                                        getWidthHitBox(), getHeightHitBox() / 2);
                                if (platform.checkValidPosition(playerPos)) {
                                    insidePlatform = platform;
                                    isSet = true;
                                    break;
                                }
                            }
                        }
                        if (isSet) {
                            break;
                        }
                    }
                }
            }
        } catch (Exception ex) {
//            System.out.println(ex.toString() + " in " + this.getClass().getName());
            isDeath = true;
            stats.setHealth(0);
            if (isLTR) {
                currAnimation = animations.get(CharacterState.DEATH_LTR);
            } else {
                currAnimation = animations.get(CharacterState.DEATH_RTL);
            }
        }
    }

    public void checkStandPlatform() {
        try {
            if (insidePlatform != null) {
                Platform platform = gameplay.getPlatforms().get(insidePlatform.getRow() + 1).get(insidePlatform.getColumn());
                if (platform != null) {
                    standPlatform = platform;
                }
            }
        } catch (Exception ex) {
//            System.out.println(ex.toString() + " in " + this.getClass().getName());
            isDeath = true;
            stats.setHealth(0);
            if (isLTR) {
                currAnimation = animations.get(CharacterState.DEATH_LTR);
            } else {
                currAnimation = animations.get(CharacterState.DEATH_RTL);
            }
        }
    }

    public void render(Graphics g) {
        if (currAnimation != null) {
            currAnimation.render(g, position.getXPosition() - gameplay.getCamera().getPosition().getXPosition(),
                    position.getYPosition() - gameplay.getCamera().getPosition().getYPosition(),
                    position.getWidth(), position.getHeight());
        }
        if (receiveDamage > 0) {
            receiveDamageRenderTick++;
            if (receiveDamageRenderTick > 80) {
                receiveDamage = 0;
                receiveDamageRenderTick = 0;
            }
            g.setColor(Color.red);
            g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
            g.drawString(receiveDamage + "", position.getXPosition() + position.getWidth() / 2 - gameplay.getCamera().getPosition().getXPosition(),
                    position.getYPosition() - 10 - gameplay.getCamera().getPosition().getYPosition());
            g.setFont(null);
            g.setColor(null);
        }
        if (abilities.size() > 0) {
            for (int i = 0; i < abilities.size(); i++) {
                abilities.get(i).render(g);
            }
        }
        if (inventory != null) {
            inventory.render(g);
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

    public abstract boolean checkHit(int attackX, int attackY, int attackHeight, boolean isAttack, Stats attackerStats, int attackDamage);

    public boolean checkHit(int attackX, int attackY, int attackHeight, boolean isAttack, Stats attackerStats) {
        return checkHit(attackX, attackY, attackHeight, isAttack, attackerStats, -1);
    }

    public boolean moveRight() {
        return position.moveRight(stats.getSpeed());
    }

    public boolean moveLeft() {
        return position.moveLeft(stats.getSpeed());
    }

    public boolean moveUp() {
        if (position.isJump) {
            inAir = true;
            return true;
        }
        return false;
    }

    public abstract int getXHitBox();

    public abstract int getWidthHitBox();

    public abstract int getHeightHitBox();

    public abstract int getXMaxHitBox();

    public abstract int getYHitBox();

    public abstract int getYMaxHitBox();

    public boolean moveDown() {
        return position.moveDown(stats.getSpeed());
    }

    public int getJumpFlySpeed() {
        return jumpFlySpeed;
    }

    public void setJumpFlySpeed(int jumpFlySpeed) {
        this.jumpFlySpeed = jumpFlySpeed;
    }

    public int getId() {
        return id;
    }

    public boolean isFallDown() {
        return fallDown;
    }

    public void setFallDown(boolean fallDown) {
        this.fallDown = fallDown;
    }

    public int getReceiveDamage() {
        return receiveDamage;
    }

    public void setReceiveDamage(int receiveDamage) {
        this.receiveDamage = receiveDamage;
    }

    public int getReceiveDamageRenderTick() {
        return receiveDamageRenderTick;
    }

    public void setReceiveDamageRenderTick(int receiveDamageRenderTick) {
        this.receiveDamageRenderTick = receiveDamageRenderTick;
    }

    public Platform getInsidePlatform() {
        return insidePlatform;
    }

    public void setInsidePlatform(Platform insidePlatform) {
        this.insidePlatform = insidePlatform;
    }

    public Platform getStandPlatform() {
        return standPlatform;
    }

    public void setStandPlatform(Platform standPlatform) {
        this.standPlatform = standPlatform;
    }

    public int getJumpSpeed() {
        return jumpSpeed;
    }

    public void setJumpSpeed(int jumpSpeed) {
        this.jumpSpeed = jumpSpeed;
    }

    public float getVely() {
        return vely;
    }

    public void setVely(float vely) {
        this.vely = vely;
    }

    public boolean isInAir() {
        return inAir;
    }

    public void setInAir(boolean inAir) {
        this.inAir = inAir;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GamePosition getPosition() {
        return position;
    }

    public void setPosition(GamePosition position) {
        this.position = position;
    }

    public Map<CharacterState, Animation> getAnimations() {
        return animations;
    }

    public void setAnimations(Map<CharacterState, Animation> animations) {
        this.animations = animations;
    }

    public Map<String, BufferedImage> getCharacterAssets() {
        return characterAssets;
    }

    public void setCharacterAssets(Map<String, BufferedImage> characterAssets) {
        this.characterAssets = characterAssets;
    }

    public List<Handler> getController() {
        return controller;
    }

    public void setController(List<Handler> controller) {
        this.controller = controller;
    }

    public Animation getCurrAnimation() {
        return currAnimation;
    }

    public void setCurrAnimation(Animation currAnimation) {
        if (currAnimation != null) {
            currAnimation.getSheet().setSpriteCounter(0);
            currAnimation.getSheet().setSpriteIndex(0);
        }
        this.currAnimation = currAnimation;
    }

    public BufferedImage getAvatar() {
        return avatar;
    }

    public void setAvatar(BufferedImage avatar) {
        this.avatar = avatar;
    }

    public HealthBar getHealthBar() {
        return healthBar;
    }

    public void setHealthBar(HealthBar healthBar) {
        this.healthBar = healthBar;
    }

    public boolean isAttacked() {
        return isAttacked;
    }

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

    public void setIsAttacked(boolean isAttacked) {
        this.isAttacked = isAttacked;
    }

    public boolean isAttack() {
        return isAttack;
    }

    public void setIsAttack(boolean isAttack) {
        this.isAttack = isAttack;
    }

    public List<Ability> getAbilities() {
        return abilities;
    }

    public boolean isDeath() {
        if (stats.getHealth() > 0) {
            return false;
        } else {
            return isDeath;
        }
    }

    public void setIsDeath(boolean isDeath) {
        this.isDeath = isDeath;
    }

    public boolean isLTR() {
        return isLTR;
    }

    public void setIsLTR(boolean isLTR) {
        this.isLTR = isLTR;
    }

    public Gameplay getGameplay() {
        return gameplay;
    }

    public void setGameplay(Gameplay gameplay) {
        this.gameplay = gameplay;
    }

    public Platform getCurPlatform() {
        return insidePlatform;
    }

    public void setCurPlatform(Platform curPlatform) {
        this.insidePlatform = curPlatform;
    }

}
