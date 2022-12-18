package fightinggame.entity;

import fightinggame.input.handler.Handler;
import fightinggame.resource.ImageManager;
import fightinggame.resource.SpriteSheet;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Character {

    protected int id;
    protected String name;
    protected GamePosition position;
    protected Animation currAnimation;
    protected Map<CharacterState, Animation> animations;
    protected List<Handler> controller = new ArrayList<>();
    protected Map<String, BufferedImage> characterAssets;
    protected List<List<Item>> inventory;
    protected int speed = 30;
    protected int attackDamage = 10;
    protected BufferedImage avatar;
    protected HealthBar healthBar;
    protected boolean isAttacked = false;
    protected boolean isAttack = false;

    public Character(int id, String name, int health, GamePosition position, Map<CharacterState, Animation> animations, Map<String, BufferedImage> characterAssets, List<List<Item>> inventory) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.animations = animations;
        this.characterAssets = characterAssets;
        this.inventory = inventory;
        currAnimation = animations.get(CharacterState.NORMAL);
        avatar = animations.get(CharacterState.NORMAL).getSheet().getImage(0);
        healthBarInit();
        healthBar.setHealth(health);
        healthBar.setMaxHealth(health);
    }

    public Character() {
    }
    
    protected void healthBarInit() {
        SpriteSheet healthBar = new SpriteSheet();
        healthBar.setImages(ImageManager.loadImagesWithCutFromFolderToList("assets/res/healthbar",
                1, 2, 126, 12));
        this.healthBar = new HealthBar(avatar, healthBar, this);
    }
    
    public abstract void healthBarTick();
    
    public void tick() {
        if (currAnimation != null) {
            currAnimation.tick();
        }
    }

    public void render(Graphics g) {
        if (currAnimation != null) {
            currAnimation.render(g, position.getXPosition(), position.getYPosition(),
                    position.getWidth(), position.getHeight());
        }
    }
    
    public abstract boolean checkHit(int attackX, int attackY, boolean isAttack, int attackDmg);
    
    public int getId() {
        return id;
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

    public List<List<Item>> getInventory() {
        return inventory;
    }

    public void setInventory(List<List<Item>> inventory) {
        this.inventory = inventory;
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
        if(currAnimation != null) {
            currAnimation.setSpriteCounter(0);
            currAnimation.setSpriteIndex(0);
        }
        this.currAnimation = currAnimation;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
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

    public void setIsAttacked(boolean isAttacked) {
        this.isAttacked = isAttacked;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public boolean isAttack() {
        return isAttack;
    }

    public void setIsAttack(boolean isAttack) {
        this.isAttack = isAttack;
    }
}
