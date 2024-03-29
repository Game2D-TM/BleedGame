package fightinggame.entity.enemy;

import fightinggame.Gameplay;
import fightinggame.animation.effect.Effect;
import fightinggame.animation.effect.enemy.EnemyHitEffect;
import fightinggame.animation.enemy.EnemyHeavyAttack;
import fightinggame.animation.enemy.EnemyHit;
import fightinggame.animation.enemy.EnemyLightAttack;
import fightinggame.animation.enemy.EnemyRunBack;
import fightinggame.animation.enemy.EnemyRunForward;
import fightinggame.entity.Animation;
import java.awt.Graphics;
import java.util.Map;
import java.util.Random;
import fightinggame.entity.Character;
import fightinggame.entity.Dialogue;
import fightinggame.entity.state.CharacterState;
import fightinggame.entity.GamePosition;
import fightinggame.entity.StatusBar;
import fightinggame.entity.Player;
import fightinggame.entity.item.Item;
import fightinggame.entity.platform.Platform;
import fightinggame.resource.ImageManager;
import fightinggame.entity.SpriteSheet;
import fightinggame.entity.inventory.Inventory;
import fightinggame.entity.platform.tile.Tile;
import fightinggame.entity.platform.tile.WallTile;
import java.io.File;
import java.util.List;

public abstract class Enemy extends Character {

    public static Enemy ENEMY_HEALTHBAR_SHOW;
    public static Enemy ENEMY_IS_SPEAK;

    protected int deathCounter = 0;
    protected int deathExpireLimit = 1500;
    protected int isAttackedCounter = 0;
    protected int walkCounter = 0;
    protected int walkLimit = 100;
    protected boolean animateChange = false;
    protected int point = 10;
    protected double experience = 0;
    protected int stunTime = 50;
    protected int defStunTime = 50;
    protected boolean isSpeak;
    protected Dialogue dialogue;
    protected int speakCounter = 0;
    protected int speakTimeDialogueCounter = 0;
    protected File dialogueFile;
    protected int attackCounter = 0;
    protected int attackLimit = 50;
    protected Effect hitEffect;
    protected boolean canMove = true;

    public Enemy() {

    }

    public Enemy(int id, String name, int health, GamePosition position, Map<CharacterState, Animation> animations,
            Gameplay gameplay, int rangeRandomSpeed) {
        super(id, name, health, position, animations, gameplay, false, null);
        healthBarInit(health);
        Random rand = new Random();
        stats.setHealth(health);
        stats.setSpeed(rand.nextInt(rangeRandomSpeed) + 30);
        SpriteSheet enemyHitSheet
                = new SpriteSheet(ImageManager.loadImage("assets/res/effect/Mini_Effect_2D/Impact_1.png"),
                        0, 0, 32, 32,
                        0, 0, 32, 32, 2);
        hitEffect = new EnemyHitEffect(0, enemyHitSheet, 25);
    }

    public Enemy(int id, String name, int health, GamePosition position, Map<CharacterState, Animation> animations,
            Gameplay gameplay) {
        super(id, name, health, position, animations, gameplay, false, null);
        healthBarInit(health);
        healthBar.setOvalImage(new java.awt.geom.Ellipse2D.Float(gameplay.getGame().getWidth() - 160, 10f, 100, 100));
        healthBar.setAppearTimeLimit(1000);
        stats.setHealth(health);
        SpriteSheet enemyHitSheet
                = new SpriteSheet(ImageManager.loadImage("assets/res/effect/Mini_Effect_2D/Impact_1.png"),
                        0, 0, 32, 32,
                        0, 0, 32, 32, 2);
        hitEffect = new EnemyHitEffect(0, enemyHitSheet, 25);
    }

    @Override
    protected void healthBarInit(int maxHealth) {
        SpriteSheet healthBarSheet = new SpriteSheet();
//        healthBarSheet.setImages(ImageManager.loadImagesWithCutFromFolderToList("assets/res/healthbar",
//                1, 2, 126, 12));
        healthBarSheet.setImages(ImageManager.loadImagesFromFoldersToList("assets/res/status-bar/HealthBar"));
        int healthBarX = gameplay.getGame().getWidth() - (gameplay.getGame().getWidth() / 3 + 40 + gameplay.getGame().getHeight() / 3 - 180) - 40;
        int healthBarWidth = gameplay.getGame().getWidth() / 3 + 40;
        healthBar = new StatusBar(avatar, healthBarSheet, this,
                new GamePosition(healthBarX, 20, 
                        healthBarWidth, gameplay.getHeight() / 3 - 180), 
                new GamePosition(healthBarX + healthBarWidth + 10, 8, 160, 120),
                maxHealth, 100);
        healthBar.setOvalImage(new java.awt.geom.Ellipse2D.Float(healthBarX + healthBarWidth + 5, 10f, 100, 100));
        healthBar.setAppearTimeLimit(1000);
        //975, 20, 550, 80
        //1540, 8, 180, 120
    }

    @Override
    public void tick() {
        super.tick();
        healthBar.tick();
        if (isDeath) {
            if (animateChange) {
                if (!isLTR) {
                    position.setXPosition(position.getXPosition() + stats.getAttackRange());
                } else {
                    position.setXPosition(position.getXPosition() - stats.getAttackRange());
                }
                position.setWidth(position.getWidth() - stats.getAttackRange());
                animateChange = false;
                attackCounter = 0;
            }
            if (deathCounter <= deathExpireLimit) {
                deathCounter++;
            } else {
                List<Item> itemsInInventory = inventory.getAllItemsFromInventory();
                if (itemsInInventory != null && itemsInInventory.size() > 0) {
                    for (int i = 0; i < itemsInInventory.size(); i++) {
                        Item item = itemsInInventory.get(i);
                        if (item == null) {
                            continue;
                        }
                        GamePosition itemPos = item.getPosition();
                        if (itemPos == null) {
                            itemPos = new GamePosition(0, 0, Item.ITEM_WIDTH, Item.ITEM_HEIGHT);
                            itemPos.setXPosition(position.getXPosition() + position.getWidth() / 2 - 20);
                            itemPos.setYPosition(position.getMaxY() - itemPos.getHeight());
                            item.setPosition(itemPos);
                        } else {
                            itemPos.setXPosition(position.getXPosition() + position.getWidth() / 2 - 20);
                            itemPos.setYPosition(position.getMaxY() - itemPos.getHeight());
                        }
                        gameplay.getItemsOnGround().add(item);
                        item.setSpawnDrop(true);
                    }
                }
                gameplay.getPlayer().getStats().addExperience(experience);
                gameplay.getPlayer().addScore(point);
                gameplay.getEnemies().remove(this);
                gameplay.getRule().addSecondsTimeLimit(10);
                gameplay.getPlayer().getEnemiesKilled().add(this);
                deathCounter = 0;
            }
        }
        if (isAttacked && !isDeath) {
            if (isAttackedCounter <= stunTime) {
                if (hitEffect != null) {
                    hitEffect.tick();
                }
                isAttackedCounter++;
            } else {
                if (currAnimation != null) {
                    if (currAnimation instanceof EnemyHit) {
                        if (isLTR) {
                            currAnimation = animations.get(CharacterState.IDLE_LTR);
                        } else {
                            currAnimation = animations.get(CharacterState.IDLE_RTL);
                        }
                    }
                }
                if (stunTime != defStunTime) {
                    stunTime = defStunTime;
                }
                hitEffect.resetEffectCounter();
                isAttacked = false;
                isAttackedCounter = 0;
                if (isAttack) {
                    isAttack = false;
                }
            }
        }
        boolean playerOnSight = false;
        if (!isDeath && !inAir && !fallDown) {
            playerOnSight = checkPlayerOnSight();
        }
        move();
        if (dialogue != null) {
            if (playerOnSight && !isDeath && !gameplay.getPlayer().isSpeak()) {
                if (isSpeak && ENEMY_IS_SPEAK == this) {
                    if (speakTimeDialogueCounter <= 500) {
                        speakTimeDialogueCounter++;
                        dialogue.tick();
                    } else {
                        dialogue.next();
                        speakTimeDialogueCounter = 0;
                    }
                    if (dialogue.isEndDialogue()) {
                        isSpeak = false;
                        speakTimeDialogueCounter = 0;
                    }
                } else {
                    if (speakCounter <= 100) {
                        speakCounter++;
                    } else {
                        boolean result = dialogue.loadDialogue(dialogueFile);
                        if (result) {
                            isSpeak = true;
                            speakCounter = 0;
                            dialogue.setEndDialogue(false);
                            ENEMY_IS_SPEAK = this;
                        }
                    }
                }
            }
            if (ENEMY_IS_SPEAK != null) {
                if (ENEMY_IS_SPEAK != this) {
                    if (isSpeak) {
                        setIsSpeak(false);
                    }
                } else {
                    if (isDeath) {
                        setIsSpeak(false);
                    }
                }
            }
        }
        attackMove();
        if (!healthBar.isCanShow() && this.equals(ENEMY_HEALTHBAR_SHOW)) {
            gameplay.setRenderMap(true);
        }
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        if (!gameplay.isHideGUI()) {
            if (healthBar.isCanShow() && this.equals(ENEMY_HEALTHBAR_SHOW)) {
                healthBar.render(g);
            }
        }
        if (hitEffect != null) {
            if (hitEffect.isActive()) {
                int x;
                if (isLTR) {
                    x = getXMaxHitBox() - (getWidthHitBox() / 3 + 20);
                } else {
                    x = getXHitBox();
                }
                hitEffect.render(g, x - gameplay.getCamera().getPosition().getXPosition(),
                        getYHitBox() + getHeightHitBox() / 3 - gameplay.getCamera().getPosition().getYPosition(),
                        getWidthHitBox() / 3 + 20, getHeightHitBox() / 3 + 20);
            }
        }
        if (isSpeak && ENEMY_IS_SPEAK == this) {
            if (dialogue != null) {
                dialogue.render(g);
            }
        }
        // vision hitbox
//        g.setColor(Color.red);
//        g.drawRect(getVisionPosition().getXPosition() - gameplay.getCamera().getPosition().getXPosition(),
//                getVisionPosition().getYPosition() - gameplay.getCamera().getPosition().getYPosition(),
//                getVisionPosition().getWidth(),
//                getVisionPosition().getHeight());
    }

    protected void attackMove() {
        if (!isAttack && !isAttacked && !isDeath && !animateChange) {
            if (!gameplay.getPlayer().isAttacked()) {
                if (gameplay.getPlayer().checkHit(attackHitBox(), false, null)) {
                    if (isLTR) {
                        if (currAnimation != null && currAnimation instanceof EnemyHeavyAttack); else {
                            currAnimation = animations.get(CharacterState.ATTACK01_LTR);
                        }
                    } else {
                        if (currAnimation != null && currAnimation instanceof EnemyHeavyAttack); else {
                            currAnimation = animations.get(CharacterState.ATTACK01_RTL);
                        }
                    }
                    isAttack = true;
                    gameplay.getPlayer().checkHit(attackHitBox(), isAttack, this);
                }
            }
        }
        if (!isAttacked && !isDeath) {
            if (animateChange) {
                if (attackCounter <= attackLimit) {
                    attackCounter++;
                } else {
                    if (!isLTR) {
                        position.setXPosition(position.getXPosition() + stats.getAttackRange());
                    } else {
                        position.setXPosition(position.getXPosition() - stats.getAttackRange());
                    }
                    position.setWidth(position.getWidth() - stats.getAttackRange());
                    animateChange = false;
                    if (position.isMoveRight) {
                        currAnimation = animations.get(CharacterState.RUNBACK);
                    } else if (position.isMoveLeft) {
                        currAnimation = animations.get(CharacterState.RUNFORWARD);
                    } else {
                        if (isLTR) {
                            currAnimation = animations.get(CharacterState.IDLE_LTR);
                        } else {
                            currAnimation = animations.get(CharacterState.IDLE_RTL);
                        }
                    }
                    attackCounter = 0;
                }
            } else {
                if (isAttack) {
                    if (!isLTR) {
                        position.setXPosition(position.getXPosition() - stats.getAttackRange());
                    } else {
                        position.setXPosition(position.getXPosition() + stats.getAttackRange());
                    }
                    position.setWidth(position.getWidth() + stats.getAttackRange());
                    isAttack = false;
                    animateChange = true;
                }
            }
        }
    }

    protected void move() {
        if (canMove) {
            if (!isDeath && !isAttacked && !isAttack && !animateChange
                    && !inAir && !fallDown) {
                if (walkCounter <= walkLimit) {
                    walkCounter++;
                } else {
                    if (currAnimation == null) {
                        if (!isLTR) {
                            currAnimation = animations.get(CharacterState.RUNFORWARD);
                        } else {
                            currAnimation = animations.get(CharacterState.RUNBACK);
                        }
                    } else {
                        if (!isLTR) {
                            if (currAnimation instanceof EnemyRunForward); else {
                                currAnimation = animations.get(CharacterState.RUNFORWARD);
                            }
                        } else {
                            if (currAnimation instanceof EnemyRunBack); else {
                                currAnimation = animations.get(CharacterState.RUNBACK);
                            }
                        }
                    }
                    checkInvalidPlatform();
                    if (!isLTR) {
                        position.isMoveLeft = true;
                    } else {
                        position.isMoveRight = true;
                    }
                    walkCounter = 0;
                }
            }
        }
    }

    public abstract Enemy init(Platform firstPlatform, Gameplay gameplay);

    public abstract void dropItems(Inventory inventory, Gameplay gameplay);

    public void setDefAttackedCounter() {
        isAttackedCounter = 0;
    }

    public boolean checkPlayerOnSight() {
        return checkPlayerInRange(getVisionPosition());
    }

    public boolean checkPlayerInRange(GamePosition rangePosition) {
        Player player = gameplay.getPlayer();
        if (player == null || player.isDeath()) {
            return false;
        }
        GamePosition visionPos = rangePosition;
        GamePosition playerPos = gameplay.getPlayer().getHitBoxPosition();
        if (visionPos == null || playerPos == null) {
            return false;
        }
        if (((playerPos.getXPosition() >= visionPos.getXPosition() && playerPos.getMaxX() <= visionPos.getMaxX())
                || (playerPos.getXPosition() >= visionPos.getXPosition() && playerPos.getXPosition() <= visionPos.getMaxX()
                && playerPos.getMaxX() > visionPos.getMaxX())
                || (playerPos.getMaxX() >= visionPos.getXPosition() && playerPos.getMaxX() <= visionPos.getMaxX()
                && playerPos.getXPosition() < visionPos.getXPosition()))
                && (playerPos.getYPosition() >= visionPos.getYPosition() && playerPos.getMaxY() <= visionPos.getMaxY())) {
            if (playerPos.getMaxX() < getXHitBox()) {
                isLTR = false;
            } else if (playerPos.getXPosition() > getXMaxHitBox()) {
                isLTR = true;
            }
            return true;
        }
        return false;
    }

    public GamePosition getVisionPosition() {
        int visionX = -500, visionY = -100, visionWidth = 1000, visionHeight = 100;
        return new GamePosition(getXHitBox() + visionX,
                getYHitBox() + visionY,
                getWidthHitBox() + visionWidth, getHeightHitBox() + visionHeight);
    }

    public boolean checkInvalidPlatform() {
        if (insidePlatform != null) {
            try {
                int row = insidePlatform.getRow();
                int column = insidePlatform.getColumn();
                if (row >= 0) {
                    if (!isLTR) {
                        int nColumn = column - 1;
                        Platform platform = gameplay.getPlatforms().get(row).get(nColumn);
                        if (platform != null) {
                            if (platform instanceof WallTile || platform instanceof Tile) {
                                if (platform.checkValidPosition(
                                        new GamePosition(getXHitBox() - stats.getSpeed(), getYHitBox(), getWidthHitBox(), getHeightHitBox()))) {
                                    isLTR = true;
                                    return true;
                                }
                            }
                            platform = gameplay.getPlatforms().get(row + 1).get(nColumn);
                            if (platform != null) {
                                if (!platform.isCanStand()) {
                                    isLTR = true;
                                    return true;
                                }
                            }
                        }
                    } else {
                        int nColumn = column + 1;
                        Platform platform = gameplay.getPlatforms().get(row).get(nColumn);
                        if (platform != null) {
                            if (platform instanceof WallTile || platform instanceof Tile) {
                                if (platform.checkValidPosition(
                                        new GamePosition(getXHitBox() + stats.getSpeed(), getYHitBox(), getWidthHitBox(), getHeightHitBox()))) {
                                    isLTR = false;
                                    return true;
                                }
                            }
                            platform = gameplay.getPlatforms().get(row + 1).get(nColumn);
                            if (platform != null) {
                                if (!platform.isCanStand()) {
                                    isLTR = false;
                                    return true;
                                }
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                if (!isLTR) {
                    isLTR = true;
                } else {
                    isLTR = false;
                }
            }
        }
        return false;
    }

    @Override
    public boolean checkHit(GamePosition attackHitBox, boolean isAttack,
            Character character, int attackDamage) {
        if (isAttack && !isDeath) {
            if (((attackHitBox.getXPosition() >= getXHitBox() && attackHitBox.getXPosition() <= getXMaxHitBox())
                    || (attackHitBox.getXPosition() >= getXHitBox() && attackHitBox.getXPosition() <= getXMaxHitBox()
                    && attackHitBox.getMaxX() > getXMaxHitBox())
                    || (attackHitBox.getMaxX() >= getXHitBox() && attackHitBox.getMaxX() <= getXMaxHitBox()
                    && attackHitBox.getXPosition() < getXHitBox())
                    || (attackHitBox.getXPosition() < getXHitBox() && attackHitBox.getMaxX() > getXMaxHitBox()))
                    && ((attackHitBox.getYPosition() <= getYHitBox() && attackHitBox.getYPosition() >= getYMaxHitBox()
                    || (attackHitBox.getYPosition() >= getYHitBox() && attackHitBox.getMaxY() <= getYMaxHitBox())
                    || (attackHitBox.getYPosition() > getYHitBox() && attackHitBox.getYPosition() <= getYMaxHitBox()
                    && attackHitBox.getMaxY() > getYMaxHitBox())
                    || (attackHitBox.getMaxY() > getYHitBox() && attackHitBox.getMaxY() <= getYMaxHitBox()
                    && attackHitBox.getYPosition() < getYHitBox())))) {
                setDefAttackedCounter();
                if (ENEMY_HEALTHBAR_SHOW != null) {
                    ENEMY_HEALTHBAR_SHOW.getStatusBar().resetShowCounter();
                }
                hitEffect.setActive(true);
                ENEMY_HEALTHBAR_SHOW = this;
                healthBar.setCanShow(true);
                gameplay.setRenderMap(false);
                isAttacked = true;
                if (attackDamage == -1) {
                    addReceiveDamage(stats.getHit(character.getStats()));
                } else {
                    addReceiveDamage(stats.getHit(character.getStats(), attackDamage));
                }
                if (character.isLTR()) {
                    position.setXPosition(position.getXPosition() + character.getStats().getBounceRange());
                } else {
                    position.setXPosition(position.getXPosition() - character.getStats().getBounceRange());
                }
                if (stats.getHealth() <= 0) {
                    isDeath = true;
                    if (hitEffect != null) {
                        hitEffect.resetEffectCounter();
                    }
                    if (isLTR) {
                        currAnimation = animations.get(CharacterState.DEATH_LTR);
                    } else {
                        currAnimation = animations.get(CharacterState.DEATH_RTL);
                    }
                } else {
                    if (character.isLTR()) {
                        if (currAnimation != null
                                && (currAnimation instanceof EnemyHeavyAttack
                                || currAnimation instanceof EnemyLightAttack)); else {
                            currAnimation = animations.get(CharacterState.GET_HIT_RTL);
                        }
                        isLTR = false;
                    } else {
                        if (currAnimation != null
                                && (currAnimation instanceof EnemyHeavyAttack
                                || currAnimation instanceof EnemyLightAttack)); else {
                            currAnimation = animations.get(CharacterState.GET_HIT_LTR);
                        }
                        isLTR = true;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public double getExperience() {
        return experience;
    }

    public void addExperience(double experience) {
        this.experience += experience;
    }

    public int getPoint() {
        return point;
    }

    public void addPoint(int point) {
        this.point += point;
    }

    public int getStunTime() {
        return stunTime;
    }

    public void setStunTime(int stunTime) {
        this.stunTime = stunTime;
    }

    public boolean isSpeak() {
        return isSpeak;
    }

    public void setIsSpeak(boolean isSpeak) {
        if (!isSpeak) {
            dialogue.setEndDialogue(true);
            speakTimeDialogueCounter = 0;
        }
        this.isSpeak = isSpeak;
    }

    public int getSpeakCounter() {
        return speakCounter;
    }

    public void setSpeakCounter(int speakCounter) {
        this.speakCounter = speakCounter;
    }

    public int getSpeakTimeDialogueCounter() {
        return speakTimeDialogueCounter;
    }

    public void setSpeakTimeDialogueCounter(int speakTimeDialogueCounter) {
        this.speakTimeDialogueCounter = speakTimeDialogueCounter;
    }

    public Effect getHitEffect() {
        return hitEffect;
    }

}
