package fightinggame.entity.enemy.type;

import fightinggame.Gameplay;
import fightinggame.animation.enemy.EnemyDeath;
import fightinggame.animation.enemy.EnemyHeavyAttack;
import fightinggame.animation.enemy.EnemyHit;
import fightinggame.animation.enemy.EnemyIdle;
import fightinggame.animation.enemy.EnemyRunBack;
import fightinggame.animation.enemy.EnemyRunForward;
import fightinggame.entity.Animation;
import fightinggame.entity.GamePosition;
import fightinggame.entity.SpriteSheet;
import fightinggame.entity.enemy.Enemy;
import fightinggame.entity.inventory.Inventory;
import fightinggame.entity.item.Item;
import fightinggame.entity.item.collectable.healing.SmallEnergyPotion;
import fightinggame.entity.item.collectable.healing.SmallHealthPotion;
import fightinggame.entity.platform.Platform;
import fightinggame.entity.state.CharacterState;
import fightinggame.input.handler.game.enemy.EnemyMovementHandler;
import fightinggame.resource.EnemyAnimationResources;
import fightinggame.resource.ImageManager;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SoldierFox extends Enemy {

    public SoldierFox() {
    }

    public SoldierFox(int id, String name, int health, GamePosition position, Map<CharacterState, Animation> animations, Gameplay gameplay) {
        super(id, name, health, position, animations, gameplay);
//        avatar = ImageManager.loadImage("assets/res/gui/avatar/military_fox.png");
//        avatar = ImageManager.flipImage(avatar);
//        healthBar.setAvatar(avatar);
        healthBar.setAvatarPos(new GamePosition(1485, 8, 180, 120));
        stats.setSpeed(80);
        stats.setAttackRange(70);
        attackLimit = 30;
        experience = 120;
        point = 20;
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
    public void tick() {
        super.tick(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Enemy init(Platform firstPlatform, Gameplay gameplay) {

        GamePosition defEnemyPosition = new GamePosition(firstPlatform.getPosition().getXPosition(),
                firstPlatform.getPosition().getYPosition(), 180, 249);
        String loc = EnemyAnimationResources.SOLDIER_FOX_SHEET_LOC;
        SpriteSheet idleLTRSheet = new SpriteSheet(ImageManager.loadImage(loc + "/Idle.png"),
                0, 0, 64, 48,
                15, 12, 34, 36, 6);
        SpriteSheet hitLTRSheet = new SpriteSheet(ImageManager.loadImage(loc + "/Hit.png"),
                0, 0, 64, 48,
                15, 12, 34, 36, 4);
        SpriteSheet deathLTRSheet = new SpriteSheet(ImageManager.loadImage(loc + "/Death.png"),
                256, 0, 64, 48,
                15, 12, 34, 36, 11);
        SpriteSheet runLTRSheet = new SpriteSheet(ImageManager.loadImage(loc + "/Run.png"),
                0, 0, 64, 48,
                15, 12, 34, 36, 8);
        SpriteSheet attack01LTRSheet = new SpriteSheet(ImageManager.loadImage(loc + "/Attack01.png"),
                0, 0, 64, 48,
                18, 12, 43, 36, 8);

        SpriteSheet idleRTLSheet = idleLTRSheet.convertRTL();
        SpriteSheet hitRTLSheet = hitLTRSheet.convertRTL();
        SpriteSheet deathRTLSheet = deathLTRSheet.convertRTL();
        SpriteSheet runRTLSheet = runLTRSheet.convertRTL();
        SpriteSheet attack01RTLSheet = attack01LTRSheet.convertRTL();

        EnemyIdle idleRTL = new EnemyIdle(0, idleRTLSheet);
        EnemyIdle idleLTR = new EnemyIdle(1, idleLTRSheet);
        EnemyHit hitRTL = new EnemyHit(2, hitRTLSheet, 12);
        EnemyHit hitLTR = new EnemyHit(3, hitLTRSheet, 12);
        EnemyDeath deathRTL = new EnemyDeath(4, deathRTLSheet, 136);
        EnemyDeath deathLTR = new EnemyDeath(5, deathLTRSheet, 136);
        EnemyRunForward runForward = new EnemyRunForward(6, runRTLSheet, 40);
        EnemyRunBack runBack = new EnemyRunBack(7, runLTRSheet, 40);
        EnemyHeavyAttack attack01RTL = new EnemyHeavyAttack(8, attack01RTLSheet, 3);
        EnemyHeavyAttack attack01LTR = new EnemyHeavyAttack(9, attack01LTRSheet, 3);

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

        Enemy soldierDog = new SoldierFox(0, "Solider Fox", 600, defEnemyPosition, enemyAnimations, gameplay);
        soldierDog.setInsidePlatform(firstPlatform);
        EnemyMovementHandler movementHandler = new EnemyMovementHandler("enemy_movement", gameplay, soldierDog);
        soldierDog.getController().add(movementHandler);
        dropItems(soldierDog.getInventory(), gameplay);
        return soldierDog;
    }

    @Override
    public int getXHitBox() {
        if (currAnimation instanceof EnemyIdle) {
            return position.getXPosition() + 20;
        } else if (currAnimation instanceof EnemyRunForward) {
            return position.getXPosition() + 20;
        } else if (currAnimation instanceof EnemyRunBack) {
            return position.getXPosition() + 40;
        } else if (currAnimation instanceof EnemyHit) {
            if (isLTR) {
                return position.getXPosition() + 40;
            } else {
                return position.getXPosition() + 20;
            }
        }
        return position.getXPosition();
    }

    @Override
    public int getWidthHitBox() {
        if (currAnimation instanceof EnemyIdle) {
            return position.getWidth() - 80;
        } else if (currAnimation instanceof EnemyRunForward) {
            return position.getWidth() - 65;
        } else if (currAnimation instanceof EnemyRunBack) {
            return position.getWidth() - 60;
        } else if (currAnimation instanceof EnemyHit) {
            if (isLTR) {
                return position.getWidth() - 50;
            } else {
                return position.getWidth() - 60;
            }
        }
        return position.getWidth();
    }

    @Override
    public int getHeightHitBox() {
        return position.getHeight() - 20;
    }

    @Override
    public int getYHitBox() {
        return position.getYPosition() + 20;
    }

    @Override
    public GamePosition attackHitBox() {
        int attackX, attackY, attackWidth, attackHeight;
        attackY = position.getYPosition();
        attackHeight = position.getHeight() / 2 - 10 + position.getHeight() / 3 - 10;
        attackWidth = stats.getAttackRange();
        if (!isLTR) {
            attackX = position.getXPosition() - 10;
        } else {
            attackX = position.getMaxX() - attackWidth + 10;
        }
        return new GamePosition(attackX, attackY, attackWidth, attackHeight);
    }

    @Override
    public void dropItems(Inventory inventory, Gameplay gameplay) {
        Random random = new Random();
        int randNum = random.nextInt(2);
        Item item = null;
        if (randNum == 0) {
            item = new SmallHealthPotion(0, inventory.getCharacter(), gameplay, 1);
        } else {
            item = new SmallEnergyPotion(0, inventory.getCharacter(), gameplay, 1);
        }
        inventory.addItemToInventory(item);
    }

}
