package fightinggame.entity.enemy.type;

import fightinggame.Gameplay;
import fightinggame.animation.ability.FireBallAnimation;
import fightinggame.animation.enemy.EnemyDeath;
import fightinggame.animation.enemy.EnemyHeavyAttack;
import fightinggame.animation.enemy.EnemyHit;
import fightinggame.animation.enemy.EnemyIdle;
import fightinggame.animation.enemy.EnemyRunBack;
import fightinggame.animation.enemy.EnemyRunForward;
import fightinggame.entity.Animation;
import fightinggame.entity.Dialogue;
import fightinggame.entity.state.CharacterState;
import fightinggame.entity.enemy.Enemy;
import fightinggame.entity.GamePosition;
import fightinggame.entity.SpriteSheet;
import fightinggame.entity.ability.type.throwable.Fireball;
import fightinggame.entity.inventory.Inventory;
import fightinggame.entity.item.Item;
import fightinggame.entity.item.collectable.buff.SmallEnergyPotion;
import fightinggame.entity.item.collectable.buff.SmallHealthPotion;
import fightinggame.entity.platform.Platform;
import fightinggame.input.handler.game.enemy.EnemyMovementHandler;
import fightinggame.resource.AssetsManager;
import fightinggame.resource.DataManager;
import fightinggame.resource.EnemyAnimationResources;
import fightinggame.resource.ImageManager;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class DiorEnemy extends Enemy {

    private DiorColor color;

    public DiorEnemy() {
    }

    public DiorEnemy(DiorColor color, int id, String name, int health,
            GamePosition position, Map<CharacterState, Animation> animations,
            Gameplay gameplay, int rangeRandomSpeed) {
        super(id, name, health, position, animations, gameplay, rangeRandomSpeed);
        this.color = color;
        attackLimit = 100;
        dialogueFile = DataManager.getFile("dior_dialogue");
        avatar = ImageManager.loadImage(new File("assets/res/gui/avatar/dior_" + color.toString().toLowerCase() + ".png"));
        switch (color) {
            case Red:
                experience = 100;
                stats.addAttackDamage(25);
                stats.setSpeed(stats.getSpeed() - 20);
                stats.setHealth(stats.getHealth() - 150);
                break;
            case Blue:
                experience = 90;
                stats.addAttackDamage(15);
                stats.setSpeed(stats.getSpeed() + 10);
                stats.setHealth(stats.getHealth() + 50);
                break;
            case Green:
                experience = 80;
                stats.addAttackDamage(20);
                stats.setSpeed(stats.getSpeed() - 10);
                stats.setHealth(stats.getHealth() - 50);
                break;
            case Orange:
                experience = 80;
                stats.addAttackDamage(5);
                stats.setSpeed(stats.getSpeed() + 20);
                stats.setHealth(stats.getHealth() + 100);
                break;
            case Purple:
                experience = 80;
                stats.addAttackDamage(5);
                stats.setSpeed(stats.getSpeed() - 5);
                stats.setHealth(stats.getHealth() + 200);
                break;
            case White:
                experience = 80;
                stats.addAttackDamage(2);
                stats.setSpeed(stats.getSpeed() - 5);
                stats.setHealth(stats.getHealth() + 400);
                break;
            case Yellow:
                experience = 80;
                stats.addAttackDamage(5);
                stats.setSpeed(stats.getSpeed() + 30);
                stats.setHealth(stats.getHealth() + 50);
                break;
        }
        healthBar.setMaxHealth(stats.getHealth());
        if (stats.getSpeed() <= 35) {
            stats.setSpeed(35);
        }
        dialogue = new Dialogue(this, gameplay);
    }

    @Override
    public void tick() {
        super.tick();
//        if (color == DiorColor.Red) {
        if (!isAttack && !isDeath) {
            if (checkPlayerOnSight()) {
                Fireball fireBallAbility = ((Fireball) abilities.get(0));
                if (fireBallAbility.isCanUse()) {
                    int spawnX;
                    int xChange;
                    int skillDistance = 1400;
                    GamePosition endPos;
                    if (!isLTR) {
                        xChange = 215;
                        spawnX = position.getXPosition() - xChange;
                        endPos = new GamePosition(
                                position.getXPosition() - skillDistance - xChange, 0,
                                position.getMaxX() + skillDistance, 0);
                    } else {
                        xChange = 15;
                        spawnX = position.getMaxX() + xChange;
                        endPos = new GamePosition(
                                position.getXPosition() - skillDistance,
                                0, position.getMaxX() + skillDistance + xChange, 0);
                    }
                    GamePosition fireBallPos = new GamePosition(spawnX,
                            position.getYPosition() + position.getHeight() / 2 - 10, 200, 100);
                    boolean result = fireBallAbility.execute(fireBallPos, endPos);
                    if (result) {
                        if (isLTR) {
                            currAnimation = animations.get(CharacterState.ATTACK01_LTR);
                        } else {
                            currAnimation = animations.get(CharacterState.ATTACK01_RTL);
                        }
                        isAttack = true;
                    }
                }
            }
        }
//        }
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        if (isSpeak) {
            if (dialogue != null) {
                dialogue.render(g);
            }
        }
        //hitbox
//        g.setColor(Color.red);
//        g.drawRect(getXHitBox() - gameplay.getCamera().getPosition().getXPosition(),
//                 getYHitBox() - gameplay.getCamera().getPosition().getYPosition(),
//                getWidthHitBox(), getHeightHitBox());
        // attack hitbox
//        g.fillRect(attackHitBox().getXPosition() - gameplay.getCamera().getPosition().getXPosition()
//                , attackHitBox().getYPosition() - gameplay.getCamera().getPosition().getYPosition()
//                , attackHitBox().getWidth(), attackHitBox().getHeight());
    }

    public boolean isSpeak() {
        return isSpeak;
    }

    public void setIsSpeak(boolean isSpeak) {
        speakTimeDialogueCounter = 0;
        this.isSpeak = isSpeak;
    }

    @Override
    public int getXHitBox() {
        if (currAnimation != null) {
            if (currAnimation instanceof EnemyRunBack) {
                return position.getXPosition() + 30;
            }
        }
        return position.getXPosition();
    }

    @Override
    public int getYHitBox() {
        return position.getYPosition();
    }

    @Override
    public int getWidthHitBox() {
        return position.getWidth() - 30;
    }

    @Override
    public int getHeightHitBox() {
        return position.getHeight();
    }

    public DiorColor getColor() {
        return color;
    }

    public void setColor(DiorColor color) {
        this.color = color;
    }

    @Override
    public GamePosition attackHitBox() {
        int attackX, attackY, attackWidth, attackHeight;
        attackY = position.getYPosition() + position.getHeight() / 3 - 10;
        attackHeight = position.getHeight() / 2 - 10;
        attackWidth = position.getWidth() / 3;
        if (!isLTR) {
            attackX = position.getXPosition() - 30;
        } else {
            attackX = (position.getMaxX() + 30) - attackWidth;
        }
        return new GamePosition(attackX, attackY, attackWidth, attackHeight);
    }

    @Override
    public Enemy init(Platform firstPlatform, Gameplay gameplay) {
        Random random = new Random();
        int colorChoose = random.nextInt(7);
        String diorColorSheet = "";
        DiorColor diorColor = null;
        switch (colorChoose) {
            case 0:
                diorColorSheet = EnemyAnimationResources.DIOR_RED_SHEET;
                diorColor = DiorColor.Red;
                break;
            case 1:
                diorColorSheet = EnemyAnimationResources.DIOR_BLUE_SHEET;
                diorColor = DiorColor.Blue;
                break;
            case 2:
                diorColorSheet = EnemyAnimationResources.DIOR_GREEN_SHEET;
                diorColor = DiorColor.Green;
                break;
            case 3:
                diorColorSheet = EnemyAnimationResources.DIOR_ORANGE_SHEET;
                diorColor = DiorColor.Orange;
                break;
            case 4:
                diorColorSheet = EnemyAnimationResources.DIOR_PURPLE_SHEET;
                diorColor = DiorColor.Purple;
                break;
            case 5:
                diorColorSheet = EnemyAnimationResources.DIOR_WHITE_SHEET;
                diorColor = DiorColor.White;
                break;
            case 6:
                diorColorSheet = EnemyAnimationResources.DIOR_YELLOW_SHEET;
                diorColor = DiorColor.Yellow;
                break;
        }
        Map<String, SpriteSheet> spriteSheetMap = SpriteSheet.loadSpriteSheetFromFolder("assets/res/enemy/Dior Firor/"
                + diorColor.toString());
        // Init enemy position
        GamePosition defEnemyPosition = new GamePosition(firstPlatform.getPosition().getXPosition(),
                firstPlatform.getPosition().getYPosition(), 300, 200);
//        SpriteSheet idleRTLSheet = new SpriteSheet(ImageManager.loadImage(diorColorSheet),
//                0, 0, 192, 160,
//                23, 55, 160, 90, 4);
//        SpriteSheet enemyHitRTLSheet = new SpriteSheet(ImageManager.loadImage(diorColorSheet),
//                0, 480, 192, 160,
//                0, 55, 177, 90, 4);
//        SpriteSheet enemyDeathRTLSheet = new SpriteSheet(ImageManager.loadImage(diorColorSheet),
//                0, 1920, 192, 160,
//                23, 55, 167, 90, 4);
//        SpriteSheet enemyRunForward = new SpriteSheet(ImageManager.loadImage(diorColorSheet),
//                0, 1280, 192, 160,
//                23, 55, 160, 90, 4);
//        SpriteSheet enemyRunBack = new SpriteSheet(ImageManager.loadImage(diorColorSheet),
//                0, 1440, 192, 160,
//                10, 55, 160, 90, 4);
//        SpriteSheet enemyAttackRTLSheet = new SpriteSheet(ImageManager.loadImage(diorColorSheet),
//                0, 640, 192, 160,
//                0, 55, 180, 90, 4);
//        SpriteSheet idleLTRSheet = idleRTLSheet.convertRTL();
//        SpriteSheet enemyHitLTRSheet = enemyHitRTLSheet.convertRTL();
//        SpriteSheet enemyDeathLTRSheet = enemyDeathRTLSheet.convertRTL();
//        SpriteSheet enemyAttackLTRSheet = enemyAttackRTLSheet.convertRTL();

//        Export bufferedimage to file
//        ImageManager.writeImages(idleRTLSheet.getImages(), "assets/res/enemy/Dior Firor/" + diorColor.toString() +"/Idle_RTL", "Idle_RTL", ImageManager.EXTENSION_PNG);
//        ImageManager.writeImages(idleLTRSheet.getImages(), "assets/res/enemy/Dior Firor/" + diorColor.toString() +"/Idle_LTR", "Idle_LTR", ImageManager.EXTENSION_PNG);
//        ImageManager.writeImages(enemyHitRTLSheet.getImages(), "assets/res/enemy/Dior Firor/" + diorColor.toString() +"/Hit_RTL", "Hit_RTL", ImageManager.EXTENSION_PNG);
//        ImageManager.writeImages(enemyHitLTRSheet.getImages(), "assets/res/enemy/Dior Firor/" + diorColor.toString() +"/Hit_LTR", "Hit_LTR", ImageManager.EXTENSION_PNG);
//        ImageManager.writeImages(enemyDeathRTLSheet.getImages(), "assets/res/enemy/Dior Firor/" + diorColor.toString() +"/Death_RTL", "Death_RTL", ImageManager.EXTENSION_PNG);
//        ImageManager.writeImages(enemyDeathLTRSheet.getImages(), "assets/res/enemy/Dior Firor/" + diorColor.toString() +"/Death_LTR", "Death_LTR", ImageManager.EXTENSION_PNG);
//        ImageManager.writeImages(enemyRunBack.getImages(), "assets/res/enemy/Dior Firor/" + diorColor.toString() +"/Run_LTR", "Run_LTR", ImageManager.EXTENSION_PNG);
//        ImageManager.writeImages(enemyRunForward.getImages(), "assets/res/enemy/Dior Firor/" + diorColor.toString() +"/Run_RTL", "Run_RTL", ImageManager.EXTENSION_PNG);
//        ImageManager.writeImages(enemyAttackRTLSheet.getImages(), "assets/res/enemy/Dior Firor/" + diorColor.toString() +"/Attack_RTL", "Attack_RTL", ImageManager.EXTENSION_PNG);
//        ImageManager.writeImages(enemyAttackLTRSheet.getImages(), "assets/res/enemy/Dior Firor/" + diorColor.toString() +"/Attack_LTR", "Attack_LTR", ImageManager.EXTENSION_PNG);
//        
        EnemyIdle idleRTL = new EnemyIdle(0, spriteSheetMap.get("Idle_RTL"));
        EnemyIdle idleLTR = new EnemyIdle(1, spriteSheetMap.get("Idle_LTR"));
        EnemyHit hitRTL = new EnemyHit(2, spriteSheetMap.get("Hit_RTL"));
        EnemyHit hitLTR = new EnemyHit(3, spriteSheetMap.get("Hit_LTR"));
        EnemyDeath deathRTL = new EnemyDeath(4, spriteSheetMap.get("Death_RTL"));
        EnemyDeath deathLTR = new EnemyDeath(5, spriteSheetMap.get("Death_LTR"));
        EnemyRunForward runForward = new EnemyRunForward(6, spriteSheetMap.get("Run_RTL"), 25);
        EnemyRunBack runBack = new EnemyRunBack(7, spriteSheetMap.get("Run_LTR"), 25);
        EnemyHeavyAttack attackRTL = new EnemyHeavyAttack(8, spriteSheetMap.get("Attack_RTL"), 25);
        EnemyHeavyAttack attackLTR = new EnemyHeavyAttack(9, spriteSheetMap.get("Attack_LTR"), 25);

        Map<CharacterState, Animation> enemyAnimations = new HashMap();

        enemyAnimations.put(CharacterState.IDLE_RTL, idleRTL);
        enemyAnimations.put(CharacterState.IDLE_LTR, idleLTR);
        enemyAnimations.put(CharacterState.GET_HIT_RTL, hitRTL);
        enemyAnimations.put(CharacterState.GET_HIT_LTR, hitLTR);
        enemyAnimations.put(CharacterState.DEATH_RTL, deathRTL);
        enemyAnimations.put(CharacterState.DEATH_LTR, deathLTR);
        enemyAnimations.put(CharacterState.RUNFORWARD, runForward);
        enemyAnimations.put(CharacterState.RUNBACK, runBack);
        enemyAnimations.put(CharacterState.ATTACK01_RTL, attackRTL);
        enemyAnimations.put(CharacterState.ATTACK01_LTR, attackLTR);

        Enemy enemy = new DiorEnemy(diorColor, 0, "Dior Firor " + diorColor,
                500, defEnemyPosition,
                enemyAnimations, gameplay, 60);
        enemy.setInsidePlatform(firstPlatform);
        EnemyMovementHandler movementHandler = new EnemyMovementHandler("enemy_movement", gameplay, enemy);
        enemy.getController().add(movementHandler);
        SpriteSheet sheetLTR = new SpriteSheet();
        SpriteSheet sheetRTL = new SpriteSheet();

        // Abilities Init
        List<BufferedImage> fireBallsLTR = AssetsManager.fireBallsLTR;
        List<BufferedImage> fireBallsRTL = AssetsManager.fireBallsRTL;
        if (fireBallsLTR == null || fireBallsRTL == null) {
            return enemy;
        }
        sheetLTR.setImages(fireBallsLTR);
        sheetRTL.setImages(fireBallsRTL);
        Animation fireBallAnimationLTR = new FireBallAnimation(0, sheetLTR, 0);
        Animation fireBallAnimationRTL = new FireBallAnimation(1, sheetRTL, 0);
        Fireball fireball = new Fireball(20, 15, 2, 2000, 0, null, null,
                fireBallAnimationLTR, fireBallAnimationRTL, gameplay, enemy);
        enemy.getAbilities().add(fireball);
        dropItems(enemy.getInventory(), gameplay);
        // level up to 8
//        enemy.getStats().addExperience(10000);
        return enemy;
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
