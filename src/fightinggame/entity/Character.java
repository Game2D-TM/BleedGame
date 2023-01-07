package fightinggame.entity;

import fightinggame.entity.state.CharacterState;
import fightinggame.entity.inventory.Inventory;
import fightinggame.Gameplay;
import fightinggame.animation.player.PlayerCrouch;
import fightinggame.entity.ability.Ability;
import fightinggame.entity.platform.Platform;
import fightinggame.entity.platform.tile.BlankTile;
import fightinggame.entity.platform.tile.Tile;
import fightinggame.entity.platform.tile.WallTile;
import fightinggame.entity.platform.tile.WaterTile;
import fightinggame.input.handler.GameHandler;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
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
    protected List<GameHandler> controller = new ArrayList<>();
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
    protected boolean inAir = false;
    protected boolean fallDown = false;
    protected boolean wallSlide = false;
    protected boolean grapEdge = false;
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
            avatar = animations.get(CharacterState.IDLE_LTR).getSheet().getImage(0);
        } else {
            currAnimation = animations.get(CharacterState.IDLE_RTL);
            avatar = animations.get(CharacterState.IDLE_RTL).getSheet().getImage(0);
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
                GameHandler handler = controller.get(i);
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
        if (standPlatform != null) {
            if (standPlatform instanceof WaterTile) {
                isDeath = true;
                stats.setHealth(0);
                if (isLTR) {
                    currAnimation = animations.get(CharacterState.DEATH_LTR);
                } else {
                    currAnimation = animations.get(CharacterState.DEATH_RTL);
                }
            }
        }
    }

    public void checkPlatForm(List<List<Platform>> scene) {
        try {
            List<Platform> insidePlatforms = new ArrayList<>();
            List<Platform> insideInvalidTiles = new ArrayList();
            if (scene != null && scene.size() > 0) {
                boolean isSet = false;
                for (int i = 0; i < scene.size(); i++) {
                    List<Platform> platforms = scene.get(i);
                    if (platforms != null && platforms.size() > 0) {
                        for (int j = 0; j < platforms.size(); j++) {
                            Platform platform = platforms.get(j);
                            if (platform != null) {
                                if (platform instanceof BlankTile) {
                                    GamePosition charPos = new GamePosition(getXHitBox(), getYHitBox() + getHeightHitBox() / 3,
                                            getWidthHitBox(), getHeightHitBox() - getHeightHitBox() / 3);
                                    if (platform.checkValidPosition(charPos)) {
                                        insidePlatforms.add(platform);
                                        isSet = true;
                                    }
                                } else if (platform instanceof Tile
                                        || platform instanceof WallTile) {
                                    if (platform.checkValidPosition(getHitBoxPosition())) {
                                        insideInvalidTiles.add(platform);
                                    }
                                }
                            }
                        }
                        if (isSet) {
                            break;
                        }
                    }
                }
            }
            // check inside platform from platforms contain character
            switch (insidePlatforms.size()) {
                case 0:
                    break;
                case 1:
                    insidePlatform = insidePlatforms.get(0);
                    break;
                case 2:
                    Platform firstPlatform = insidePlatforms.get(0);
                    Platform secondPlatform = insidePlatforms.get(1);
                    int amountXFirstPlatform = firstPlatform.getPosition().getMaxX() - getXHitBox();
                    int amountXSecondPlatform = getXMaxHitBox() - secondPlatform.getPosition().getXPosition();
                    if (amountXFirstPlatform > amountXSecondPlatform) {
                        insidePlatform = firstPlatform;
                    } else {
                        insidePlatform = secondPlatform;
                    }
                    break;
                default:
                    int middle = insidePlatforms.size() / 2;
                    insidePlatform = insidePlatforms.get(middle);
                    break;
            }
            // check invalid tile 
            if (insideInvalidTiles.size() > 0) {
                for (int i = 0; i < insideInvalidTiles.size(); i++) {
                    Platform invalidTile = insideInvalidTiles.get(i);
                    if (invalidTile.getPosition() != null) {
                        // invalid tile is right or left 
                        // if player is inside invalid platform push character out
                        if (invalidTile.getColumn() > insidePlatform.getColumn()) {
                            pushToLeftTile(invalidTile);
                        } else {
                            if (invalidTile.getColumn() < insidePlatform.getColumn()) {
                                pushToRightTile(invalidTile);
                            }
                        }
                        // check invalid tile above character
                        // crouch when below invalid platform
                        if (invalidTile.getRow() < insidePlatform.getRow()
                                && invalidTile.getColumn() == insidePlatform.getColumn()) {
                            boolean isLeftPlatform = false;
                            if (checkIsLeftPlatform(invalidTile)) {
                                isLeftPlatform = true;
                            }
                            boolean isStuck = false;
                            if (isLeftPlatform && !position.isMoving()) {
                                position.isCrouch = true;
                                isStuck = true;
                            }
                            if (!isLeftPlatform && !position.isMoving()) {
                                position.isCrouch = true;
                                isStuck = true;
                            }
                            if (!isStuck) {
                                if (currAnimation instanceof PlayerCrouch) {
                                    if (isLTR) {
                                        currAnimation = animations.get(CharacterState.IDLE_LTR);
                                    } else {
                                        currAnimation = animations.get(CharacterState.IDLE_RTL);
                                    }
                                }
                                position.isCrouch = false;
                            }
                        }
                    }
                }
            }
            checkStandPlatform();
        } catch (Exception ex) {
//            System.out.println(ex.toString() + " in " + this.getClass().getName());
//            isDeath = true;
//            stats.setHealth(0);
//            if (isLTR) {
//                currAnimation = animations.get(CharacterState.DEATH_LTR);
//            } else {
//                currAnimation = animations.get(CharacterState.DEATH_RTL);
//            }
        }
    }

    public boolean pushToLeftTile(Platform invalidTile) {
        if (invalidTile == null) {
            return false;
        }
        if (invalidTile.getPosition() == null) {
            return false;
        }
        int distance = getXMaxHitBox() - invalidTile.getPosition().getXPosition();
        distance = Math.abs(distance);
        if (distance > 10) {
            position.setXPosition(position.getXPosition() - distance);
            return true;
        }
        return false;
    }

    public boolean pushToRightTile(Platform invalidTile) {
        if (invalidTile == null) {
            return false;
        }
        if (invalidTile.getPosition() == null) {
            return false;
        }
        int distance = getXHitBox() - invalidTile.getPosition().getMaxX();
        distance = Math.abs(distance);
        if (distance > 10) {
            position.setXPosition(position.getXPosition() + distance);
            return true;
        }
        return false;
    }

    public boolean checkIsLeftPlatform(Platform centerPlatform) {
        try {
            Platform leftPlatform = gameplay.getPlatforms().get(centerPlatform.getRow()).get(centerPlatform.getColumn() - 1);
            if (leftPlatform == null) {
                return false;
            }
            if (leftPlatform.getPosition() == null) {
                return false;
            }
            if (leftPlatform.checkValidPosition(getHitBoxPosition())) {
                return true;
            }
        } catch (Exception ex) {

        }
        return false;
    }

    public void checkStandPlatform() {
        try {
            if (standPlatform == null) {
                if (insidePlatform != null) {
                    Platform platform = gameplay.getPlatforms().get(insidePlatform.getRow() + 1).get(insidePlatform.getColumn());
                    if (platform != null) {
                        standPlatform = platform;
                    }
                }
            } else {
                if (standPlatform.getPosition() != null) {
                    if (((getXHitBox() >= standPlatform.getPosition().getXPosition() && getXMaxHitBox() <= standPlatform.getPosition().getMaxX())
                            || (getXHitBox() < standPlatform.getPosition().getXPosition() && getXMaxHitBox() > standPlatform.getPosition().getMaxX())
                            || (getXHitBox() >= standPlatform.getPosition().getXPosition() && getXHitBox() <= standPlatform.getPosition().getMaxX()
                            && getXMaxHitBox() > standPlatform.getPosition().getMaxX())
                            || (getXMaxHitBox() >= standPlatform.getPosition().getXPosition() && getXMaxHitBox() <= standPlatform.getPosition().getMaxX()
                            && getXHitBox() < standPlatform.getPosition().getXPosition()))
                            && (getYHitBox() < standPlatform.getPosition().getYPosition() && getYMaxHitBox() <= standPlatform.getPosition().getMaxY())) {

                    } else {
                        standPlatform = null;
                    }
                } else {
                    standPlatform = null;
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
        // find inside platform hitbox
//        g.setColor(Color.red);
//        g.drawRect(getXHitBox() - gameplay.getCamera().getPosition().getXPosition(),
//                getYHitBox() + getHeightHitBox() / 3 - gameplay.getCamera().getPosition().getYPosition(),
//                getWidthHitBox(), getHeightHitBox() - getHeightHitBox() / 3);
        // Draw damage taken
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

    public abstract boolean checkHit(int attackX, int attackY, int attackHeight,
            boolean isAttack, Character character, int attackDamage);

    public boolean checkHit(int attackX, int attackY, int attackHeight, boolean isAttack, Character character) {
        return checkHit(attackX, attackY, attackHeight, isAttack, character, -1);
    }

    public boolean moveRight() {
        return position.moveRight(stats.getSpeed());
    }

    public boolean moveLeft() {
        return position.moveLeft(stats.getSpeed());
    }

    public boolean jump() {
        if (position.isJump) {
            inAir = true;
            return true;
        }
        return false;
    }

    public boolean slideRight() {
        return position.slideRight(stats.getSpeed());
    }

    public boolean slideLeft() {
        return position.slideLeft(stats.getSpeed());
    }

    public abstract int getXHitBox();

    public abstract int getWidthHitBox();

    public abstract int getHeightHitBox();

    public abstract int getXMaxHitBox();

    public abstract int getYHitBox();

    public abstract int getYMaxHitBox();

    public GamePosition getHitBoxPosition() {
        return new GamePosition(getXHitBox(), getYHitBox(), getWidthHitBox(), getHeightHitBox());
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

    public Platform getStandPlatform() {
        return standPlatform;
    }

    public void setStandPlatform(Platform standPlatform) {
        this.standPlatform = standPlatform;
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

    public List<GameHandler> getController() {
        return controller;
    }

    public void setController(List<GameHandler> controller) {
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

    public void setInsidePlatform(Platform curPlatform) {
        this.insidePlatform = curPlatform;
    }

    public boolean isWallSlide() {
        return wallSlide;
    }

    public void setWallSlide(boolean wallSlide) {
        this.wallSlide = wallSlide;
    }

    public boolean isGrapEdge() {
        return grapEdge;
    }

    public void setGrapEdge(boolean grapEdge) {
        this.grapEdge = grapEdge;
    }

}
