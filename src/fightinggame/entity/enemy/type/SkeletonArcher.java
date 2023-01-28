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
import fightinggame.entity.item.collectable.buff.SmallEnergyPotion;
import fightinggame.entity.item.collectable.buff.SmallHealthPotion;
import fightinggame.entity.platform.Platform;
import fightinggame.entity.state.CharacterState;
import fightinggame.input.handler.game.enemy.EnemyMovementHandler;
import fightinggame.resource.EnemyAnimationResources;
import fightinggame.resource.ImageManager;
import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SkeletonArcher extends Enemy {

    public SkeletonArcher() {
    }

    public SkeletonArcher(int id, String name, int health, GamePosition position, Map<CharacterState, Animation> animations, Gameplay gameplay) {
        super(id, name, health, position, animations, gameplay);
        deathExpireLimit = 250;
        stats.setSpeed(60);
        point = 5;
        experience = 50;
        attackLimit = 120;
        stats.setAttackRange(70);
        stats.addAttackDamage(10);
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        //hitbox
        g.setColor(Color.red);
        g.drawRect(getXHitBox() - gameplay.getCamera().getPosition().getXPosition(),
                getYHitBox() - gameplay.getCamera().getPosition().getYPosition(),
                getWidthHitBox(), getHeightHitBox());
//         attack hitbox
//        g.setColor(Color.red);
//        g.fillRect(attackHitBox().getXPosition() - gameplay.getCamera().getPosition().getXPosition(),
//                attackHitBox().getYPosition() - gameplay.getCamera().getPosition().getYPosition(),
//                attackHitBox().getWidth(), attackHitBox().getHeight());
    }

    @Override
    public Enemy init(Platform firstPlatform, Gameplay gameplay) {
        GamePosition defEnemyPosition = new GamePosition(firstPlatform.getPosition().getXPosition(),
                firstPlatform.getPosition().getYPosition(), 180, 249);
        String loc = EnemyAnimationResources.SKELETON_ARCHER_SHEET_LOC;
        SpriteSheet idleLTRSheet = new SpriteSheet(ImageManager.loadImage(loc + "/Idle.png"),
                0, 0, 128, 128,
                45, 60, 40, 68, 7);
        SpriteSheet hitLTRSheet = new SpriteSheet(ImageManager.loadImage(loc + "/Hit.png"),
                0, 0, 128, 128,
                45, 60, 40, 68, 2);
        SpriteSheet deathLTRSheet = new SpriteSheet(ImageManager.loadImage(loc + "/Death.png"),
                0, 0, 128, 128,
                25, 60, 73, 68, 5);
        SpriteSheet runLTRSheet = new SpriteSheet(ImageManager.loadImage(loc + "/Run.png"),
                0, 0, 128, 128,
                45, 60, 40, 68, 8);
        SpriteSheet attack01LTRSheet = new SpriteSheet(ImageManager.loadImage(loc + "/Attack01.png"),
                0, 0, 128, 128,
                25, 60, 80, 68, 5);
        SpriteSheet attack02LTRSheet = new SpriteSheet(ImageManager.loadImage(loc + "/Attack02.png"),
                0, 0, 128, 128,
                0, 0, 128, 128, 4);
        SpriteSheet attack03LTRSheet = new SpriteSheet(ImageManager.loadImage(loc + "/Attack03.png"),
                0, 0, 128, 128,
                0, 0, 128, 128, 3);
        SpriteSheet shot01LTRSheet = new SpriteSheet(ImageManager.loadImage(loc + "/Shot01.png"),
                0, 0, 128, 128,
                0, 0, 128, 128, 15);
        SpriteSheet shot02LTRSheet = new SpriteSheet(ImageManager.loadImage(loc + "/Shot02.png"),
                0, 0, 128, 128,
                0, 0, 128, 128, 15);

        SpriteSheet idleRTLSheet = idleLTRSheet.convertRTL();
        SpriteSheet hitRTLSheet = hitLTRSheet.convertRTL();
        SpriteSheet deathRTLSheet = deathLTRSheet.convertRTL();
        SpriteSheet runRTLSheet = runLTRSheet.convertRTL();
        SpriteSheet attack01RTLSheet = attack01LTRSheet.convertRTL();
        SpriteSheet attack02RTLSheet = attack02LTRSheet.convertRTL();
        SpriteSheet attack03RTLSheet = attack03LTRSheet.convertRTL();
        SpriteSheet shot01RTLSheet = shot01LTRSheet.convertRTL();
        SpriteSheet shot02RTLSheet = shot02LTRSheet.convertRTL();

        EnemyIdle idleRTL = new EnemyIdle(0, idleRTLSheet);
        EnemyIdle idleLTR = new EnemyIdle(1, idleLTRSheet);
        EnemyHit hitRTL = new EnemyHit(2, hitRTLSheet, 25);
        EnemyHit hitLTR = new EnemyHit(3, hitLTRSheet, 25);
        EnemyDeath deathRTL = new EnemyDeath(4, deathRTLSheet, 50);
        EnemyDeath deathLTR = new EnemyDeath(5, deathLTRSheet, 50);
        EnemyRunForward runForward = new EnemyRunForward(6, runRTLSheet, 12);
        EnemyRunBack runBack = new EnemyRunBack(7, runLTRSheet, 12);
        EnemyHeavyAttack attack01RTL = new EnemyHeavyAttack(8, attack01RTLSheet, 24);
        EnemyHeavyAttack attack01LTR = new EnemyHeavyAttack(9, attack01LTRSheet, 24);
        EnemyHeavyAttack attack02RTL = new EnemyHeavyAttack(8, attack02RTLSheet, 16);
        EnemyHeavyAttack attack02LTR = new EnemyHeavyAttack(9, attack02LTRSheet, 16);
        EnemyHeavyAttack attack03RTL = new EnemyHeavyAttack(8, attack03RTLSheet, 16);
        EnemyHeavyAttack attack03LTR = new EnemyHeavyAttack(9, attack03LTRSheet, 16);
        EnemyHeavyAttack shot01RTL = new EnemyHeavyAttack(8, shot01RTLSheet, 16);
        EnemyHeavyAttack shot01LTR = new EnemyHeavyAttack(9, shot01LTRSheet, 16);
        EnemyHeavyAttack shot02RTL = new EnemyHeavyAttack(8, shot02RTLSheet, 16);
        EnemyHeavyAttack shot02LTR = new EnemyHeavyAttack(9, shot02LTRSheet, 16);

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
        enemyAnimations.put(CharacterState.SHOT01_RTL, shot01RTL);
        enemyAnimations.put(CharacterState.SHOT01_LTR, shot01LTR);
        enemyAnimations.put(CharacterState.SHOT02_RTL, shot02RTL);
        enemyAnimations.put(CharacterState.SHOT02_LTR, shot02LTR);

        Enemy skeletonArcher = new SkeletonArcher(0, "Skeleton Archer", 400, defEnemyPosition, enemyAnimations, gameplay);
        skeletonArcher.setInsidePlatform(firstPlatform);
        EnemyMovementHandler movementHandler = new EnemyMovementHandler("enemy_movement", gameplay, skeletonArcher);
        skeletonArcher.getController().add(movementHandler);
        dropItems(skeletonArcher.getInventory(), gameplay);
        return skeletonArcher;
    }

    @Override
    public void dropItems(Inventory inventory, Gameplay gameplay) {
        Random random = new Random();
        int randNum = random.nextInt(3);
        Item item = null;
        if (randNum == 0) {
            item = new SmallHealthPotion(0, inventory.getCharacter(), gameplay, 1);
        } else if (randNum == 1) {
            item = new SmallEnergyPotion(0, inventory.getCharacter(), gameplay, 1);
        }
        if (item == null) {
            return;
        }
        inventory.addItemToInventory(item);
    }

    @Override
    public int getXHitBox() {
        return position.getXPosition();
    }

    @Override
    public int getWidthHitBox() {
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
            attackX = position.getXPosition();
        } else {
            attackX = position.getMaxX() - attackWidth;
        }
        return new GamePosition(attackX, attackY, attackWidth, attackHeight);
    }

}
