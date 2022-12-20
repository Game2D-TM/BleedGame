package fightinggame;

import fightinggame.animation.ability.FireBallAnimation;
import fightinggame.animation.enemy.EnemyAttack;
import fightinggame.animation.enemy.EnemyDeath;
import fightinggame.animation.enemy.EnemyHit;
import fightinggame.animation.enemy.EnemyIdle;
import fightinggame.animation.enemy.EnemyRunBack;
import fightinggame.animation.enemy.EnemyRunForward;
import fightinggame.animation.player.PlayerAttack;
import fightinggame.animation.player.PlayerDeath;
import fightinggame.animation.player.PlayerHit;
import fightinggame.animation.player.PlayerIdle;
import fightinggame.animation.player.PlayerRun;
import fightinggame.entity.Animation;
import fightinggame.entity.CharacterState;
import fightinggame.entity.enemy.Enemy;
import fightinggame.entity.GamePosition;
import fightinggame.entity.Player;
import fightinggame.entity.ability.Ability;
import fightinggame.entity.ability.type.healing.GreaterHeal;
import fightinggame.entity.ability.type.throwable.Fireball;
import fightinggame.entity.enemy.dior.DiorColor;
import fightinggame.entity.enemy.dior.DiorEnemy;
import fightinggame.input.handler.PlayerMovementHandler;
import fightinggame.input.handler.MouseHandler;
import fightinggame.input.handler.PlayerAbilityHandler;
import fightinggame.resource.EnemyAnimationResources;
import fightinggame.resource.ImageManager;
import fightinggame.resource.SpriteSheet;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import fightinggame.entity.Character;

public class Gameplay {

    private GamePosition playPosition;
    private Player player;
    private final List<Enemy> enemies = new ArrayList<Enemy>();
    private final Map<String, GamePosition> positions = new HashMap<String, GamePosition>();
    private Game game;
    private int enemyCount = 0;
    private int enemyAnimationCount = 0;
    private int enemySpawnXPosition;
    private Thread thread;

    public Gameplay(Game game) {
        playPosition = new GamePosition(10, game.getHeight() / 2 + 130, game.getWidth() - 20, game.getHeight() / 3 + 20);
        this.game = game;
        int xPosition = playPosition.getXPosition();
        enemySpawnXPosition = xPosition + 1700;
        playerInit(xPosition + 10); // playPosition.getYPosition() - 50
        diorInit(enemySpawnXPosition, playPosition.getYPosition() + playPosition.getHeight() - 520);
        diorInit(enemySpawnXPosition, playPosition.getYPosition() + 50);
        thread = new Thread(spawnEnemies());
        thread.start();
    }

    public void diorInit(int xPosition, int yPosition) {
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
        GamePosition defEnemyPosition = new GamePosition(xPosition,
                yPosition, 300, 200);
        SpriteSheet enemyIdleSheet = new SpriteSheet(ImageManager.loadImage(diorColorSheet),
                0, 0, 192, 160,
                23, 55, 160, 90, 4);
        SpriteSheet enemyHit = new SpriteSheet(ImageManager.loadImage(diorColorSheet),
                0, 480, 192, 160,
                0, 55, 177, 90, 4);
        SpriteSheet enemyDeath = new SpriteSheet(ImageManager.loadImage(diorColorSheet),
                0, 1920, 192, 160,
                23, 55, 167, 90, 4);
        SpriteSheet enemyRunForward = new SpriteSheet(ImageManager.loadImage(diorColorSheet),
                0, 1280, 192, 160,
                23, 55, 160, 90, 4);
        SpriteSheet enemyRunBack = new SpriteSheet(ImageManager.loadImage(diorColorSheet),
                0, 1440, 192, 160,
                10, 55, 160, 90, 4);
        SpriteSheet enemyAttack = new SpriteSheet(ImageManager.loadImage(diorColorSheet),
                0, 640, 192, 160,
                0, 55, 180, 90, 4);
        EnemyIdle idle = new EnemyIdle(enemyAnimationCount, enemyIdleSheet);
        enemyAnimationCount++;
        EnemyHit hit = new EnemyHit(enemyAnimationCount, enemyHit);
        enemyAnimationCount++;
        EnemyDeath death = new EnemyDeath(enemyAnimationCount, enemyDeath);
        enemyAnimationCount++;
        EnemyRunForward runForward = new EnemyRunForward(enemyAnimationCount, enemyRunForward, 40);
        enemyAnimationCount++;
        EnemyRunBack runBack = new EnemyRunBack(enemyAnimationCount, enemyRunBack, 40);
        enemyAnimationCount++;
        EnemyAttack attack = new EnemyAttack(enemyAnimationCount, enemyAttack, 25);
        enemyAnimationCount++;
        Map<CharacterState, Animation> enemyAnimations = new HashMap();
        enemyAnimations.put(CharacterState.IDLE_RTL, idle);
        enemyAnimations.put(CharacterState.IDLE_LTR, idle);
        enemyAnimations.put(CharacterState.GET_HIT_RTL, hit);
        enemyAnimations.put(CharacterState.GET_HIT_LTR, hit);
        enemyAnimations.put(CharacterState.DEATH_RTL, death);
        enemyAnimations.put(CharacterState.DEATH_LTR, death);
        enemyAnimations.put(CharacterState.RUNFORWARD, runForward);
        enemyAnimations.put(CharacterState.RUNBACK, runBack);
        enemyAnimations.put(CharacterState.ATTACK_RTL, attack);
        enemyAnimations.put(CharacterState.ATTACK_LTR, attack);
        Enemy enemy = new DiorEnemy(diorColor, enemyCount, "Dior Firor " + diorColor + " " + enemyCount, 500, defEnemyPosition,
                enemyAnimations, null, null, this, 200);
        if(diorColor == DiorColor.Red) {
            abilitiesInit(enemy.getAbilities(), enemy);
        }
        enemyCount++;
        enemies.add(enemy);
        positions.put(enemy.getName(), enemy.getPosition());
    }

    public void playerInit(int xPosition) {
        GamePosition defPlayerPosition = new GamePosition(xPosition,
                playPosition.getYPosition() - 50, 200, 290);
        SpriteSheet playerIdleSheetLTR = new SpriteSheet(ImageManager.loadImage("assets/res/player/LTR/Idle.png"),
                0, 0, 200, 200,
                75, 70, 38, 53, 8);
        SpriteSheet playerRunLTR = new SpriteSheet(ImageManager.loadImage("assets/res/player/LTR/Run.png"),
                0, 0, 200, 200,
                75, 75, 43, 48, 8);
        SpriteSheet playerAttack1LTR = new SpriteSheet(ImageManager.loadImage("assets/res/player/LTR/Attack1.png"),
                800, 0, 200, 200,
                75, 53, 115, 70, 2);
        SpriteSheet playerAttack2LTR = new SpriteSheet(ImageManager.loadImage("assets/res/player/LTR/Attack2.png"),
                800, 0, 200, 200,
                75, 53, 115, 70, 2);
        SpriteSheet playerHitLTR = new SpriteSheet(ImageManager.loadImage("assets/res/player/LTR/TakeHit.png"),
                0, 0, 200, 200,
                75, 68, 38, 55, 4);
        SpriteSheet playerDeathLTR = new SpriteSheet(ImageManager.loadImage("assets/res/player/LTR/Death.png"),
                0, 0, 200, 200,
                75, 70, 46, 55, 6);
        
        SpriteSheet playerIdleSheetRTL = new SpriteSheet(ImageManager.loadImage("assets/res/player/RTL/Idle.png"),
                0, 0, 200, 200,
                87, 70, 38, 53, 8);
        SpriteSheet playerRunRTL = new SpriteSheet(ImageManager.loadImage("assets/res/player/RTL/Run.png"),
                0, 0, 200, 200,
                83, 75, 43, 48, 8);
        SpriteSheet playerAttack1RTL = new SpriteSheet(ImageManager.loadImage("assets/res/player/RTL/Attack1.png"),
                0, 0, 200, 200,
                5, 53, 113, 70, 2);
        SpriteSheet playerAttack2RTL = new SpriteSheet(ImageManager.loadImage("assets/res/player/RTL/Attack2.png"),
                0, 0, 200, 200,
                5, 53, 113, 70, 2);
        SpriteSheet playerHitRTL = new SpriteSheet(ImageManager.loadImage("assets/res/player/RTL/TakeHit.png"),
                0, 0, 200, 200,
                85, 68, 38, 55, 4);
        SpriteSheet playerDeathRTL = new SpriteSheet(ImageManager.loadImage("assets/res/player/RTL/Death.png"),
                0, 0, 200, 200,
                80, 70, 42, 55, 6);
        playerIdleSheetRTL.reverseImages();
        playerRunRTL.reverseImages();
        playerAttack1RTL.reverseImages();
        playerAttack2RTL.reverseImages();
        playerHitRTL.reverseImages();
        playerDeathRTL.reverseImages();
        playerAttack1LTR.getImages().addAll(playerAttack2LTR.getImages());
        playerAttack1RTL.getImages().addAll(playerAttack2RTL.getImages());
        PlayerHit hitLTR = new PlayerHit(3, playerHitLTR, 25);
        PlayerIdle idleLTR = new PlayerIdle(0, playerIdleSheetLTR);
        PlayerRun runLTR = new PlayerRun(1, playerRunLTR, 0);
        PlayerAttack attackLTR = new PlayerAttack(2, playerAttack1LTR, 15);
        PlayerDeath deathLTR = new PlayerDeath(4, playerDeathLTR, 35);
        PlayerHit hitRTL = new PlayerHit(3, playerHitRTL, 25);
        PlayerIdle idleRTL = new PlayerIdle(0, playerIdleSheetRTL);
        PlayerRun runRTL = new PlayerRun(1, playerRunRTL, 0);
        PlayerAttack attackRTL = new PlayerAttack(2, playerAttack1RTL, 10);
        PlayerDeath deathRTL = new PlayerDeath(4, playerDeathRTL, 35);
        Map<CharacterState, Animation> playerAnimations = new HashMap();
        playerAnimations.put(CharacterState.IDLE_LTR, idleLTR);
        playerAnimations.put(CharacterState.IDLE_RTL, idleRTL);
        playerAnimations.put(CharacterState.RUNFORWARD, runLTR);
        playerAnimations.put(CharacterState.RUNBACK, runRTL);
        playerAnimations.put(CharacterState.ATTACK_LTR, attackLTR);
        playerAnimations.put(CharacterState.ATTACK_RTL, attackRTL);
        playerAnimations.put(CharacterState.GET_HIT_LTR, hitLTR);
        playerAnimations.put(CharacterState.GET_HIT_RTL, hitRTL);
        playerAnimations.put(CharacterState.DEATH_LTR, deathLTR);
        playerAnimations.put(CharacterState.DEATH_RTL, deathRTL);
        player = new Player(0, "Shinobu Windsor", 100, defPlayerPosition,
                playerAnimations, null, null);
        abilitiesInit(player.getAbilities(), player);
        PlayerAbilityHandler abilityHandler = new PlayerAbilityHandler(player, "player_ability_handler", this);
        player.getAbility(0).getHandlers().add(abilityHandler);
        player.getAbility(1).getHandlers().add(abilityHandler);
        game.addKeyListener(abilityHandler);
        PlayerMovementHandler keyBoardHandler = new PlayerMovementHandler(player, "player_movement", this);
        MouseHandler mouseHandler = new MouseHandler(player, "player_mouse", this);
        player.getController().add(keyBoardHandler);
        player.getController().add(mouseHandler);
        game.addKeyListener(keyBoardHandler);
        game.addMouseListener(mouseHandler);
        positions.put(player.getName(), player.getPosition());
    }

    public void abilitiesInit(List<Ability> abilities, Character character) {
        
        if (character instanceof Player) {
            BufferedImage redBorder = ImageManager.loadImage("assets/res/ability/border-red.png");
            SpriteSheet greaterHealSheet = new SpriteSheet();
            greaterHealSheet.getImages().add(ImageManager.loadImage("assets/res/ability/Greater-Heal.png"));
            GamePosition firstSkillPosition = new GamePosition(character.getHealthBar().getHealthBarPos().getXPosition(),
                    character.getHealthBar().getHealthBarPos().getMaxY() + 90, 80, 80);
            Ability greaterHeal = new GreaterHeal(20, 1, 2500, greaterHealSheet,
                    firstSkillPosition, null, redBorder, this, character);
            abilities.add(greaterHeal);
            SpriteSheet fireballIcon = new SpriteSheet();
            fireballIcon.getImages().add(ImageManager.loadImage("assets/res/ability/Fire-Ball.png"));
            SpriteSheet fireBallAniSheet = new SpriteSheet();
            List<BufferedImage> fireBalls = ImageManager.loadImagesWithCutFromFolderToList("assets/res/ability/Fire Ball/LTR", 200, 365, 580, 200);
            if(fireBalls == null) return;
            fireBallAniSheet.setImages(fireBalls);
            Animation fireBallAnimation = new FireBallAnimation(0, fireBallAniSheet, 0);
            Fireball fireball = new Fireball(150, 30, 2, 5000, fireballIcon,
                    new GamePosition(firstSkillPosition.getMaxX() + 15,
                            firstSkillPosition.getYPosition(), firstSkillPosition.getWidth(), firstSkillPosition.getHeight()),
                    fireBallAnimation, redBorder, this, character);
            abilities.add(fireball);
        } else {
            SpriteSheet fireBallAniSheet = new SpriteSheet();
            List<BufferedImage> fireBalls = ImageManager.loadImagesWithCutFromFolderToList("assets/res/ability/Fire Ball/RTL", 30, 365, 580, 200);
            if(fireBalls == null) return;
            fireBallAniSheet.setImages(fireBalls);
            Animation fireBallAnimation = new FireBallAnimation(0, fireBallAniSheet, 0);
            Fireball fireball = new Fireball(20, 15, 2, 2000, null, null,
                    fireBallAnimation, this, character);
            abilities.add(fireball);
        }
    }

    public Runnable spawnEnemies() {
        return new Runnable() {
            int spawnCounter = 0;

            @Override
            public void run() {
                long now;
                long updateTime;
                long wait;

                final int TARGET_FPS = Game.FPS;
                final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
                while (game.isRunning()) {
                    now = System.nanoTime();
                    spawnCounter++;
                    if (spawnCounter > 2000) {
                        try {
                            Random random = new Random();
                            int numberOfEnemies = random.nextInt(5);
                            int enemySpawnYPosition = -1;
                            for (int i = 0; i < numberOfEnemies; i++) {
                                enemySpawnYPosition = playPosition.getYPosition() + playPosition.getHeight() - random.nextInt(521);
                                while (true) {
                                    enemySpawnYPosition = playPosition.getYPosition() + playPosition.getHeight() - random.nextInt(521);
                                    if (enemySpawnYPosition <= getMaxYPlayArea() - 180) {
                                        break;
                                    }
                                }
                                diorInit(enemySpawnXPosition, enemySpawnYPosition);
                            }
                        } catch (Exception ex) {

                        }
                        spawnCounter = 0;
                    }
                    updateTime = System.nanoTime() - now;
                    wait = (OPTIMAL_TIME - updateTime) / 1000000;
                    try {
                        Thread.sleep(wait);
                    } catch (Exception e) {
//                System.out.println(e.toString());
                    }
                }
            }
        };
    }

    public void tick() {
        if (enemies != null) {
            if (enemies.size() > 0) {
                for (int i = 0; i < enemies.size(); i++) {
                    Enemy enemy = enemies.get(i);
                    enemy.tick();
                }
            }
        }
        if (player != null) {
            player.tick();
        }
    }

    public void render(Graphics g) {
        if (enemies != null) {
            if (enemies.size() > 0) {
                for (int i = 0; i < enemies.size(); i++) {
                    Enemy enemy = enemies.get(i);
                    enemy.render(g);
                }
            }
        }
        if (player != null) {
            player.render(g);
        }
    }

    public GamePosition getPlayPosition() {
        return playPosition;
    }

    public void setPlayPosition(GamePosition playPosition) {
        this.playPosition = playPosition;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public Map<String, GamePosition> getPositions() {
        return positions;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public int getMaxYPlayArea() {
        return playPosition.getMaxY();
    }

    public int getMaxXPlayArea() {
        return playPosition.getMaxX();
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

}
