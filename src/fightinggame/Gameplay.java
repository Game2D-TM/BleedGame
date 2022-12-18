package fightinggame;

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
import fightinggame.entity.Enemy;
import fightinggame.entity.GamePosition;
import fightinggame.entity.Player;
import fightinggame.input.handler.KeyboardHandler;
import fightinggame.resource.EnemyAnimationResources;
import fightinggame.resource.ImageManager;
import fightinggame.resource.SpriteSheet;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
        playPosition = new GamePosition(10, game.getHeight() / 2 + 130, game.getWidth() - 20, game.getHeight() / 3);
        this.game = game;
        int xPosition = playPosition.getXPosition();
        enemySpawnXPosition = xPosition + 1700;
        playerInit(xPosition + 10);
        enemyInit(enemySpawnXPosition, playPosition.getYPosition() - 50);
        enemyInit(enemySpawnXPosition, playPosition.getYPosition() + 50);
        thread = new Thread(spawnEnemies());
        thread.start();
    }

    public void enemyInit(int xPosition, int yPosition) {
        GamePosition defEnemyPosition = new GamePosition(xPosition,
                yPosition, 300, 200);
        SpriteSheet enemyIdleSheet = new SpriteSheet(ImageManager.loadImage(EnemyAnimationResources.DIOR_RED_IDLE_SPRITES),
                0, 0, 192, 160,
                23, 55, 160, 90, 4);
        SpriteSheet enemyHit = new SpriteSheet(ImageManager.loadImage(EnemyAnimationResources.DIOR_RED_HIT_SPRITES),
                0, 480, 192, 160,
                0, 55, 177, 90, 4);
        SpriteSheet enemyDeath = new SpriteSheet(ImageManager.loadImage(EnemyAnimationResources.DIOR_RED_DEATH_SPRITES),
                0, 1920, 192, 160,
                23, 55, 167, 90, 4);
        SpriteSheet enemyRunForward = new SpriteSheet(ImageManager.loadImage(EnemyAnimationResources.DIOR_RED_RUN_RTL_SPRITES),
                0, 1280, 192, 160,
                23, 55, 160, 90, 4);
        SpriteSheet enemyRunBack = new SpriteSheet(ImageManager.loadImage(EnemyAnimationResources.DIOR_RED_RUN_LTR_SPRITES),
                0, 1440, 192, 160,
                10, 55, 160, 90, 4);
        SpriteSheet enemyAttack = new SpriteSheet(ImageManager.loadImage(EnemyAnimationResources.DIOR_RED_RUN_ATTACK_SPRITES),
                0, 640, 192, 160,
                0, 55, 180, 90, 4);
        EnemyIdle idle = new EnemyIdle(enemyAnimationCount, CharacterState.NORMAL, enemyIdleSheet);
        enemyAnimationCount++;
        EnemyHit hit = new EnemyHit(enemyAnimationCount, CharacterState.GET_HIT, enemyHit);
        enemyAnimationCount++;
        EnemyDeath death = new EnemyDeath(enemyAnimationCount, CharacterState.DEATH, enemyDeath);
        enemyAnimationCount++;
        EnemyRunForward runForward = new EnemyRunForward(enemyAnimationCount, CharacterState.RUNFORWARD, enemyRunForward);
        enemyAnimationCount++;
        EnemyRunBack runBack = new EnemyRunBack(enemyAnimationCount, CharacterState.RUNBACK, enemyRunBack);
        enemyAnimationCount++;
        EnemyAttack attack = new EnemyAttack(enemyAnimationCount, CharacterState.ATTACK, enemyAttack);
        enemyAnimationCount++;
        Map<CharacterState, Animation> enemyAnimations = new HashMap();
        enemyAnimations.put(CharacterState.NORMAL, idle);
        enemyAnimations.put(CharacterState.GET_HIT, hit);
        enemyAnimations.put(CharacterState.DEATH, death);
        enemyAnimations.put(CharacterState.RUNFORWARD, runForward);
        enemyAnimations.put(CharacterState.RUNBACK, runBack);
        enemyAnimations.put(CharacterState.ATTACK, attack);
        Enemy enemy = new Enemy(enemyCount, "Dior Firor Red " + enemyCount, 500, defEnemyPosition,
                enemyAnimations, null, null, this, 200);
        enemyCount++;
        enemies.add(enemy);
        positions.put(enemy.getName(), enemy.getPosition());
    }

    public void playerInit(int xPosition) {
        GamePosition defPlayerPosition = new GamePosition(xPosition,
                playPosition.getYPosition() - 50, 200, 300);
        SpriteSheet playerIdleSheet = new SpriteSheet(ImageManager.loadImage("assets/res/player/Idle.png"),
                0, 0, 200, 200,
                75, 70, 38, 55, 8);
        SpriteSheet playerRun = new SpriteSheet(ImageManager.loadImage("assets/res/player/Run.png"),
                0, 0, 200, 200,
                75, 70, 38, 55, 8);
        SpriteSheet playerAttack1 = new SpriteSheet(ImageManager.loadImage("assets/res/player/Attack1.png"),
                800, 0, 200, 200,
                75, 53, 115, 70, 2);
        SpriteSheet playerAttack2 = new SpriteSheet(ImageManager.loadImage("assets/res/player/Attack2.png"),
                800, 0, 200, 200,
                75, 53, 115, 70, 2);
        SpriteSheet playerHit = new SpriteSheet(ImageManager.loadImage("assets/res/player/TakeHit.png"),
                0, 0, 200, 200,
                75, 70, 38, 55, 4);
        SpriteSheet playerDeath = new SpriteSheet(ImageManager.loadImage("assets/res/player/Death.png"),
                0, 0, 200, 200,
                75, 70, 42, 55, 6);
        playerAttack1.getImages().addAll(playerAttack2.getImages());
        PlayerHit hit = new PlayerHit(3, CharacterState.GET_HIT, playerHit);
        PlayerIdle idle = new PlayerIdle(0, CharacterState.NORMAL, playerIdleSheet);
        PlayerRun run = new PlayerRun(1, CharacterState.RUNFORWARD, playerRun);
        PlayerAttack attack = new PlayerAttack(2, CharacterState.ATTACK, playerAttack1);
        PlayerDeath death = new PlayerDeath(4, CharacterState.DEATH, playerDeath);
        Map<CharacterState, Animation> playerAnimations = new HashMap();
        playerAnimations.put(CharacterState.NORMAL, idle);
        playerAnimations.put(CharacterState.RUNFORWARD, run);
        playerAnimations.put(CharacterState.ATTACK, attack);
        playerAnimations.put(CharacterState.GET_HIT, hit);
        playerAnimations.put(CharacterState.DEATH, death);
        player = new Player(0, "Shinobu Windsor", 100, defPlayerPosition,
                playerAnimations, null, null);
        KeyboardHandler handler = new KeyboardHandler(player, "keyboard", this);
        player.getController().add(handler);
        game.addKeyListener(handler);
        positions.put(player.getName(), player.getPosition());
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
                                enemySpawnYPosition = playPosition.getYPosition() + playPosition.getHeight() / random.nextInt(32);
                                while (true) {
                                    enemySpawnYPosition = playPosition.getYPosition() + playPosition.getHeight() / random.nextInt(32);
                                    if (enemySpawnYPosition <= getMaxYPlayArea() - 200) {
                                        break;
                                    }
                                }
                                enemyInit(enemySpawnXPosition, enemySpawnYPosition);
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
        return playPosition.getYPosition() + playPosition.getHeight();
    }

    public int getMaxXPlayArea() {
        return playPosition.getXPosition() + playPosition.getWidth();
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

}
