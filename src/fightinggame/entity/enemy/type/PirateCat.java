package fightinggame.entity.enemy.type;

import fightinggame.Gameplay;
import fightinggame.animation.enemy.*;
import fightinggame.entity.Animation;
import fightinggame.entity.GamePosition;
import fightinggame.entity.Player;
import fightinggame.entity.SpriteSheet;
import fightinggame.entity.ability.type.healing.TimeHeal;
import fightinggame.entity.enemy.Enemy;
import fightinggame.entity.inventory.Inventory;
import fightinggame.entity.item.Item;
import fightinggame.entity.item.collectable.buff.SmallEnergyPotion;
import fightinggame.entity.item.collectable.buff.SmallHealthPotion;
import fightinggame.entity.platform.Platform;
import fightinggame.entity.state.CharacterState;
import fightinggame.input.handler.game.enemy.EnemyMovementHandler;
import fightinggame.resource.EnemyAnimationResources;
import fightinggame.resource.ImageManager;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PirateCat extends Enemy {

    private boolean pushBackAttack;
    private int pushBackAttackCounter = 0;
    private int pushBackUseCounter = 0;

    public PirateCat(int id, String name, int health, GamePosition position, Map<CharacterState, Animation> animations, Gameplay gameplay, int rangeRandomSpeed) {
        super(id, name, health, position, animations, gameplay, rangeRandomSpeed);
        avatar = ImageManager.loadImage("assets/res/gui/avatar/pirate_cat.png");
        avatar = ImageManager.flipImage(avatar);
        healthBar.setAvatar(avatar);
        healthBar.setAvatarPos(new GamePosition(1500, 8, 180, 120));
        stunTime = 100;
        stats.setBounceRange(180);
        stats.addAttackDamage(25);
        stats.setAttackRange(180);
        stats.setSpeed(60);
        point = 100;
        experience = 500;
    }

    public PirateCat() {
    }

    private boolean checkNextToPlayer() {
        Player player = gameplay.getPlayer();
        if (player != null) {
            GamePosition playerPos = player.getHitBoxPosition();
            if (playerPos != null) {
                if (((playerPos.getXPosition() >= getXHitBox() && playerPos.getMaxX() <= getXMaxHitBox())
                        || (playerPos.getXPosition() >= getXHitBox() && playerPos.getXPosition() <= getXMaxHitBox()
                        && playerPos.getMaxX() > getXMaxHitBox())
                        || (playerPos.getMaxX() >= getXHitBox() && playerPos.getMaxX() <= getXMaxHitBox()
                        && playerPos.getXPosition() < getXHitBox()))
                        && (playerPos.getYPosition() >= getYHitBox() && playerPos.getMaxY() <= getYMaxHitBox())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (!isAttack && !animateChange && !isAttacked && !isDeath) {
            if (pushBackUseCounter <= 1000) {
                pushBackUseCounter++;
            } else {
                if (!pushBackAttack) {
                    if (checkNextToPlayer()) {
                        pushBackAttack = true;
                        if (currAnimation instanceof EnemyLightAttack); else {
                            if (isLTR) {
                                currAnimation = animations.get(CharacterState.ATTACK02_LTR);
                            } else {
                                currAnimation = animations.get(CharacterState.ATTACK02_RTL);
                            }
                        }
                        Player player = gameplay.getPlayer();
                        if (player != null && !player.isDeath()) {
                            if (isLTR) {
                                player.getPosition().setXPosition(player.getPosition().getXPosition() + 50);
                            } else {
                                player.getPosition().setXPosition(player.getPosition().getXPosition() - 50);
                            }
                            player.setIsAttacked(true);
                        }
                    }
                } else {
                    if (pushBackAttackCounter <= 50) {
                        pushBackAttackCounter++;
                    } else {
                        pushBackAttack = false;
                        if (currAnimation instanceof EnemyLightAttack) {
                            if (isLTR) {
                                currAnimation = animations.get(CharacterState.IDLE_LTR);
                            } else {
                                currAnimation = animations.get(CharacterState.IDLE_RTL);
                            }
                        }
                        pushBackAttackCounter = 0;
                        pushBackUseCounter = 0;
                    }
                }
            }
        }
        if (!isDeath) {
            if (stats.getHealth() < healthBar.getMaxHealth()) {
                TimeHeal timeHeal = ((TimeHeal) abilities.get(0));
                if (timeHeal != null && timeHeal.isCanUse()) {
                    timeHeal.execute();
                }
            }
        }
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        //hitbox
//        g.setColor(Color.red);
//        g.drawRect(getXHitBox() - gameplay.getCamera().getPosition().getXPosition(),
//                getYHitBox() - gameplay.getCamera().getPosition().getYPosition(),
//                getWidthHitBox(), getHeightHitBox());
        // attack hitbox
//        g.setColor(Color.red);
//        g.fillRect(attackHitBox().getXPosition() - gameplay.getCamera().getPosition().getXPosition(),
//                attackHitBox().getYPosition() - gameplay.getCamera().getPosition().getYPosition(),
//                attackHitBox().getWidth(), attackHitBox().getHeight());
    }

    @Override
    public int getXHitBox() {
        if (currAnimation != null) {
            if (currAnimation instanceof EnemyRunForward) {
                return position.getXPosition() + 25;
            } else if (currAnimation instanceof EnemyRunBack) {
                return position.getXPosition() + 65;
            }
        }
        return position.getXPosition();
    }

    @Override
    public int getWidthHitBox() {
        if (currAnimation != null) {
            if (currAnimation instanceof EnemyRunForward) {
                return position.getWidth() - 90;
            } else if (currAnimation instanceof EnemyRunBack) {
                return position.getWidth() - 90;
            }
        }
        return position.getWidth();
    }

    @Override
    public int getHeightHitBox() {
        return position.getHeight();
    }

    @Override
    public int getYHitBox() {
        return position.getYPosition();
    }

    @Override
    public GamePosition attackHitBox() {
        int attackX, attackY, attackWidth, attackHeight;
        attackY = position.getYPosition();
        attackHeight = position.getHeight() / 2 - 10 + position.getHeight() / 3 - 10;
        attackWidth = stats.getAttackRange();
        if (!isLTR) {
            attackX = position.getXPosition() - stats.getAttackRange() - 10;
        } else {
            attackX = position.getMaxX() + 10;
        }
        return new GamePosition(attackX, attackY, attackWidth, attackHeight);
    }

    @Override
    public Enemy init(Platform firstPlatform, Gameplay gameplay) {
        GamePosition defEnemyPosition = new GamePosition(firstPlatform.getPosition().getXPosition(),
                firstPlatform.getPosition().getYPosition(), 300, 259);
        String loc = EnemyAnimationResources.CAT_BOSS_SHEET_LOC;
        SpriteSheet idleLTRSheet = new SpriteSheet(ImageManager.loadImage(loc + "/Idle.png"),
                0, 0, 96, 48,
                27, 11, 40, 37, 5);
        SpriteSheet hitLTRSheet = new SpriteSheet(ImageManager.loadImage(loc + "/Hit.png"),
                0, 0, 96, 48,
                27, 7, 40, 41, 4);
        SpriteSheet deathLTRSheet = new SpriteSheet(ImageManager.loadImage(loc + "/Death.png"),
                384, 0, 96, 48,
                30, 7, 40, 41, 11);
        SpriteSheet runLTRSheet = new SpriteSheet(ImageManager.loadImage(loc + "/Run.png"),
                0, 0, 96, 48,
                27, 11, 40, 37, 8);
        SpriteSheet attack01LTRSheet = new SpriteSheet(ImageManager.loadImage(loc + "/Attack01.png"),
                384, 0, 96, 48,
                29, 11, 65, 37, 2);
        SpriteSheet attack02LTRSheet = new SpriteSheet(ImageManager.loadImage(loc + "/Attack02.png"),
                0, 0, 96, 48,
                24, 11, 42, 37, 6);
        SpriteSheet attack03LTRSheet = new SpriteSheet(ImageManager.loadImage(loc + "/Attack03.png"),
                0, 0, 96, 48,
                27, 11, 40, 37, 7);

        SpriteSheet idleRTLSheet = idleLTRSheet.convertRTL();
        SpriteSheet hitRTLSheet = hitLTRSheet.convertRTL();
        SpriteSheet deathRTLSheet = deathLTRSheet.convertRTL();
        SpriteSheet runRTLSheet = runLTRSheet.convertRTL();
        SpriteSheet attack01RTLSheet = attack01LTRSheet.convertRTL();
        SpriteSheet attack02RTLSheet = attack02LTRSheet.convertRTL();
        SpriteSheet attack03RTLSheet = attack03LTRSheet.convertRTL();

        EnemyIdle idleRTL = new EnemyIdle(0, idleRTLSheet);
        EnemyIdle idleLTR = new EnemyIdle(1, idleLTRSheet);
        EnemyHit hitRTL = new EnemyHit(2, hitRTLSheet, 25);
        EnemyHit hitLTR = new EnemyHit(3, hitLTRSheet, 25);
        EnemyDeath deathRTL = new EnemyDeath(4, deathRTLSheet, 136);
        EnemyDeath deathLTR = new EnemyDeath(5, deathLTRSheet, 136);
        EnemyRunForward runForward = new EnemyRunForward(6, runRTLSheet, 40);
        EnemyRunBack runBack = new EnemyRunBack(7, runLTRSheet, 40);
        EnemyHeavyAttack attack01RTL = new EnemyHeavyAttack(8, attack01RTLSheet, 25);
        EnemyHeavyAttack attack01LTR = new EnemyHeavyAttack(9, attack01LTRSheet, 25);
        EnemyLightAttack attack02RTL = new EnemyLightAttack(10, attack02RTLSheet, 8);
        EnemyLightAttack attack02LTR = new EnemyLightAttack(11, attack02LTRSheet, 8);
        EnemyHeavyAttack attack03RTL = new EnemyHeavyAttack(12, attack03RTLSheet, 25);
        EnemyHeavyAttack attack03LTR = new EnemyHeavyAttack(13, attack03LTRSheet, 25);

        Map<CharacterState, Animation> enemyAnimations = new HashMap();

        enemyAnimations.put(CharacterState.IDLE_RTL, idleRTL);
        enemyAnimations.put(CharacterState.IDLE_LTR, idleLTR);
        enemyAnimations.put(CharacterState.GET_HIT_RTL, hitRTL);
        enemyAnimations.put(CharacterState.GET_HIT_LTR, hitLTR);
        enemyAnimations.put(CharacterState.DEATH_RTL, deathRTL);
        enemyAnimations.put(CharacterState.DEATH_LTR, deathLTR);
        enemyAnimations.put(CharacterState.RUNFORWARD, runForward);
        enemyAnimations.put(CharacterState.RUNBACK, runBack);
        enemyAnimations.put(CharacterState.ATTACK01_RTL, attack01RTL);
        enemyAnimations.put(CharacterState.ATTACK01_LTR, attack01LTR);
        enemyAnimations.put(CharacterState.ATTACK02_RTL, attack02RTL);
        enemyAnimations.put(CharacterState.ATTACK02_LTR, attack02LTR);
        enemyAnimations.put(CharacterState.ATTACK03_RTL, attack03RTL);
        enemyAnimations.put(CharacterState.ATTACK03_LTR, attack03LTR);

        PirateCat pirateCat = new PirateCat(0, "Zach Fowler", 5000, defEnemyPosition, enemyAnimations, gameplay,
                60);
        pirateCat.setInsidePlatform(firstPlatform);
        EnemyMovementHandler movementHandler = new EnemyMovementHandler("enemy_movement", gameplay, pirateCat);
        pirateCat.getController().add(movementHandler);
        dropItems(pirateCat.getInventory(), gameplay);
        //ablities Init
        TimeHeal timeHeal = new TimeHeal(5000, 500, 10, 0,
                6000, 0, null, null, null, null, gameplay, pirateCat);
        pirateCat.getAbilities().add(timeHeal);
        return pirateCat;
    }

    @Override
    public void dropItems(Inventory inventory, Gameplay gameplay) {
        Random random = new Random();
        int randNum = random.nextInt(2);
        Item item = null;
        if (randNum == 0) {
            item = new SmallHealthPotion(0, inventory.getCharacter(), gameplay, 1);
        } else {
            item = new SmallEnergyPotion(0, inventory.getCharacter(),
                    gameplay, 1);
        }
        inventory.addItemToInventory(item);
    }

}
