package fightinggame;

import fightinggame.animation.ability.FireBallAnimation;
import fightinggame.animation.enemy.EnemyAttack;
import fightinggame.animation.enemy.EnemyDeath;
import fightinggame.animation.enemy.EnemyHit;
import fightinggame.animation.enemy.EnemyIdle;
import fightinggame.animation.enemy.EnemyRunBack;
import fightinggame.animation.enemy.EnemyRunForward;
import fightinggame.animation.item.HealthPotionAnimation;
import fightinggame.animation.item.equipment.FireSwordAnimation;
import fightinggame.animation.player.*;
import fightinggame.entity.Animation;
import fightinggame.entity.Background;
import fightinggame.entity.Camera;
import fightinggame.entity.CharacterState;
import fightinggame.entity.enemy.Enemy;
import fightinggame.entity.GamePosition;
import fightinggame.entity.Player;
import fightinggame.entity.ability.Ability;
import fightinggame.entity.enemy.dior.DiorColor;
import fightinggame.entity.enemy.dior.DiorEnemy;
import fightinggame.resource.EnemyAnimationResources;
import fightinggame.resource.ImageManager;
import fightinggame.resource.SpriteSheet;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import fightinggame.entity.Character;
import fightinggame.entity.GameMap;
import fightinggame.entity.ability.type.healing.GreaterHeal;
import fightinggame.entity.ability.type.healing.PotionHeal;
import fightinggame.entity.ability.type.increase.AttackIncrease;
import fightinggame.entity.ability.type.throwable.Fireball;
import fightinggame.entity.background.touchable.Chest;
import fightinggame.entity.inventory.Inventory;
import fightinggame.entity.item.Item;
import fightinggame.entity.item.equipment.weapon.Sword;
import fightinggame.entity.item.healing.HealthPotion;
import fightinggame.entity.platform.Platform;
import fightinggame.entity.platform.tile.Tile;
import fightinggame.entity.platform.tile.WallTile;
import fightinggame.input.handler.EnemyMovementHandler;
import fightinggame.input.handler.MouseHandler;
import fightinggame.input.handler.PlayerAbilityHandler;
import fightinggame.input.handler.PlayerMovementHandler;
import fightinggame.resource.AudioPlayer;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class Gameplay extends JPanel implements Runnable {

    private Background background;
    private Background map;
    private boolean renderMap = true;
    private Player player;
    private final List<Enemy> enemies = new ArrayList<Enemy>();
    private final Map<String, GamePosition> positions = new HashMap<String, GamePosition>();
    private Game game;
    private int itemCount = 0;
    private int enemyCount = 0;
    private int enemyAnimationCount = 0;
    private Thread spawnEnemiesThread;
    private AudioPlayer audioPlayer;
    private final List<Item> itemsOnGround = new ArrayList<Item>();
    private Camera camera;
    public int gravity = 7;

    public Gameplay(Game game, int width, int height) {
        setSize(width, height);
        camera = new Camera(player, new GamePosition(0, 0, 0, 0), getWidth(), getHeight(), this);
        background = new Background(0, "Scene 1",
                ImageManager.loadImagesFromFolderToMap("assets/res/background/Wallpaper"), width, height,
                ImageManager.loadImagesFromFolderToMap("assets/res/background/Tiles"),
                ImageManager.loadImagesFromFolderToMap("assets/res/background/Objects"), this,
                "data/scene_1.txt", 250, 180);
        map = new GameMap(1, "Map", this,
                new GamePosition(0, 0, 0, 0),
                ImageManager.loadImagesFromFolderToMap("assets/res/background/Wallpaper"),
                ImageManager.loadImagesFromFolderToMap("assets/res/background/Tiles"),
                ImageManager.loadImagesFromFolderToMap("assets/res/background/Objects"),
                "data/scene_1.txt", 15, 15);
        this.game = game;
        audioPlayer = new AudioPlayer("assets/res/sound");
        Platform firstPlatform = getPlatforms().get(10).get(3);
        playerInit(firstPlatform);
        firstPlatform = background.getScene().get(9).get(8);
        diorInit(firstPlatform);
//        firstPlatform = background.getScene().get(9).get(9);
//        diorInit(firstPlatform);
//        spawnEnemiesThread = new Thread(spawnEnemies());
//        spawnEnemiesThread.start();
        audioPlayer.startThread("background_music", true, 0.75f);
    }

    @Override
    public void run() {
        Graphics g = getGraphics();
        long now;
        long updateTime;
        long wait;

        long lastFpsCheck = 0;
        int currentFps = 0;
        int totalFrames = 0;

        final int TARGET_FPS = Game.FPS;
        final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
        while (game.isRunning()) {
            now = System.nanoTime();
            totalFrames++;
            if (System.nanoTime() > lastFpsCheck + 1000000000) {
                lastFpsCheck = System.nanoTime();
                currentFps = totalFrames;
                totalFrames = 0;
//                System.out.println("Current Fps: " + currentFps);
            }
            revalidate();
            try {
                tick();
                render(g);
            } catch (Exception ex) {
//                System.out.println(ex.toString());
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

    public void diorInit(Platform firstPlatform) {
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
        // Init enemy position
        GamePosition defEnemyPosition = new GamePosition(firstPlatform.getPosition().getXPosition(),
                firstPlatform.getPosition().getYPosition(), 300, 200);
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
//        Export bufferedimage to file
//        ImageManager.writeImages(enemyIdleSheet.getImages(), "assets/res/enemy/dior_firor/idle", "Idle_RTL", ImageManager.EXTENSION_PNG);
//        ImageManager.writeImages(enemyHit.getImages(), "assets/res/enemy/dior_firor/hit", "Hit_RTL", ImageManager.EXTENSION_PNG);
//        ImageManager.writeImages(enemyDeath.getImages(), "assets/res/enemy/dior_firor/death", "Death_RTL", ImageManager.EXTENSION_PNG);
//        ImageManager.writeImages(enemyRunForward.getImages(), "assets/res/enemy/dior_firor/run_rtl", "Run_RTL", ImageManager.EXTENSION_PNG);
//        ImageManager.writeImages(enemyRunBack.getImages(), "assets/res/enemy/dior_firor/run_ltr", "Run_LTR", ImageManager.EXTENSION_PNG);
//        ImageManager.writeImages(enemyAttack.getImages(), "assets/res/enemy/dior_firor/attack", "Attack_RTL", ImageManager.EXTENSION_PNG);
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
        enemyAnimations.put(CharacterState.ATTACK01_RTL, attack);
        enemyAnimations.put(CharacterState.ATTACK01_LTR, attack);
        Enemy enemy = new DiorEnemy(diorColor, enemyCount, "Dior Firor " + diorColor + " " + enemyCount,
                500, defEnemyPosition,
                enemyAnimations, null, this, 200, null);
        enemy.setInsidePlatform(firstPlatform);
        EnemyMovementHandler movementHandler = new EnemyMovementHandler("enemy_movement", this, enemy);
        enemy.getController().add(movementHandler);
        abilitiesCharacterInit(enemy.getAbilities(), enemy);
        itemInit(enemy.getInventory(), enemy);
        enemyCount++;
        enemies.add(enemy);
        positions.put(enemy.getName(), enemy.getPosition());
        // level up to 8
//        enemy.getStats().addExperience(50000);
    }

    public void playerInit(Platform firstPlatform) {
        // init player position
        GamePosition defPlayerPosition = new GamePosition(firstPlatform.getPosition().getXPosition(),
                firstPlatform.getPosition().getYPosition()
                - 280 - 400, 350, 259);
        Map<String, SpriteSheet> spriteSheetMap = SpriteSheet.loadSpriteSheetFromFolder("assets/res/player");
        
        SpriteSheet attackSpecialLTR = spriteSheetMap.get("Attack01").clone();
        SpriteSheet attackSpecialRTL = spriteSheetMap.get("Attack01").convertRTL();
        // Add special attack new sheet
        attackSpecialLTR.getImages().addAll(spriteSheetMap.get("Attack02").getImages());
        attackSpecialLTR.getImages().addAll(spriteSheetMap.get("Attack03").getImages());
        attackSpecialRTL.getImages().addAll(spriteSheetMap.get("Attack02").convertRTL().getImages());
        attackSpecialRTL.getImages().addAll(spriteSheetMap.get("Attack03").convertRTL().getImages());
        
        //LTR
        PlayerHit hitLTR = new PlayerHit(3, spriteSheetMap.get("HurtAnim01"), 25);
        PlayerIdle idleLTR = new PlayerIdle(0, spriteSheetMap.get("Idle02"));
        PlayerIdle fireIdleLTR = new PlayerIdle(0, spriteSheetMap.get("FireIdle01"));
        PlayerRun_LTR runLTR = new PlayerRun_LTR(1, spriteSheetMap.get("Run01"), 0);
        PlayerAttack attack01LTR = new PlayerAttack(2, spriteSheetMap.get("Attack01"), 12);
        PlayerAttack attack02LTR = new PlayerAttack(2, spriteSheetMap.get("Attack02"), 12);
        PlayerAttackSpecial_LTR attack03LTR = new PlayerAttackSpecial_LTR(2, attackSpecialLTR, 15);
        PlayerAttack fireAttack01LTR = new PlayerAttack(2, spriteSheetMap.get("FireAttack01"), 12);
        PlayerDeath deathLTR = new PlayerDeath(4, spriteSheetMap.get("Death01"), 50);
        PlayerJump_LTR jumpLTR = new PlayerJump_LTR(5, spriteSheetMap.get("Jump02"), 30);
        PlayerFallDown_LTR fallDownLTR = new PlayerFallDown_LTR(6, spriteSheetMap.get("FallAnim01"), 50);
        PlayerSpellCast spellCastLTR = new PlayerSpellCast(7, spriteSheetMap.get("Spellcast01"), 40);
        PlayerCrouch crouchLTR = new PlayerCrouch(8, spriteSheetMap.get("Crouch01"), 10);
        PlayerSpellCastLoop spellCastLoopLTR = new PlayerSpellCastLoop(9, spriteSheetMap.get("SpellcastLoop"), 15);
        PlayerSlide_LTR slideLTR = new PlayerSlide_LTR(10, spriteSheetMap.get("Slide01"), 30);
        PlayerAirAttack_LTR airAttack01LTR = new PlayerAirAttack_LTR(11, spriteSheetMap.get("AirAttack01"), 20);
        PlayerAirAttack_LTR airAttack02LTR = new PlayerAirAttack_LTR(11, spriteSheetMap.get("AirAttack02"), 20);
        PlayerAirAttack_LTR airAttack03LTR = new PlayerAirAttack_LTR(11, spriteSheetMap.get("AirAttack03"), 20);
        PlayerAirAttack_LTR airAttackLoopLTR = new PlayerAirAttack_LTR(11, spriteSheetMap.get("AirAttack03Loop"), 20);
        PlayerJumpRoll_LTR jumpRollLTR = new PlayerJumpRoll_LTR(12, spriteSheetMap.get("JumpRoll01"), 15);
        PlayerGetUp_LTR getUpLTR = new PlayerGetUp_LTR(13, spriteSheetMap.get("GetUp01"), 15);
        PlayerFallDown_LTR knockDownLTR = new PlayerFallDown_LTR(14, spriteSheetMap.get("KnockDown01"), 25);
        PlayerLedgeAction_LTR ledgeClimbLTR = new PlayerLedgeAction_LTR(15, spriteSheetMap.get("LedgeClimb01"), 15);
        PlayerLedgeAction_LTR ledgeGrabLTR = new PlayerLedgeAction_LTR(16, spriteSheetMap.get("LedgeGrab01"), 15);
        PlayerWallAction_LTR wallRunLTR = new PlayerWallAction_LTR(17, spriteSheetMap.get("WallRun01"), 15);
        PlayerWallAction_LTR wallSlideLTR = new PlayerWallAction_LTR(18, spriteSheetMap.get("WallSlide01"), 15);
        PlayerRun_LTR sprintLTR = new PlayerRun_LTR(19, spriteSheetMap.get("Sprint01"), 0);

        //RTL
        PlayerHit hitRTL = new PlayerHit(3, spriteSheetMap.get("HurtAnim01").convertRTL(), 25);
        PlayerIdle idleRTL = new PlayerIdle(0, spriteSheetMap.get("Idle02").convertRTL());
        PlayerIdle fireIdleRTL = new PlayerIdle(0, spriteSheetMap.get("FireIdle01").convertRTL());
        PlayerRun_RTL runRTL = new PlayerRun_RTL(1, spriteSheetMap.get("Run01").convertRTL(), 0);
        PlayerAttack attack01RTL = new PlayerAttack(2, spriteSheetMap.get("Attack01").convertRTL(), 12);
        PlayerAttack attack02RTL = new PlayerAttack(2, spriteSheetMap.get("Attack02").convertRTL(), 12);
        PlayerAttackSpecial_RTL attack03RTL = new PlayerAttackSpecial_RTL(2, attackSpecialRTL, 15);
        PlayerAttack fireAttack01RTL = new PlayerAttack(2, spriteSheetMap.get("FireAttack01").convertRTL(), 12);
        PlayerCrouch crouchRTL = new PlayerCrouch(8, spriteSheetMap.get("Crouch01").convertRTL(), 10);
        PlayerDeath deathRTL = new PlayerDeath(4, spriteSheetMap.get("Death01").convertRTL(), 50);
        PlayerJump_RTL jumpRTL = new PlayerJump_RTL(5, spriteSheetMap.get("Jump02").convertRTL(), 30);
        PlayerFallDown_RTL fallDownRTL = new PlayerFallDown_RTL(6, spriteSheetMap.get("FallAnim01").convertRTL(), 50);
        PlayerSpellCast spellCastRTL = new PlayerSpellCast(7, spriteSheetMap.get("Spellcast01").convertRTL(), 40);
        PlayerSpellCastLoop spellCastLoopRTL = new PlayerSpellCastLoop(9, spriteSheetMap.get("SpellcastLoop").convertRTL(), 15);
        PlayerSlide_RTL slideRTL = new PlayerSlide_RTL(10, spriteSheetMap.get("Slide01").convertRTL(), 30);
        PlayerAirAttack_RTL airAttack01RTL = new PlayerAirAttack_RTL(11, spriteSheetMap.get("AirAttack01").convertRTL(), 20);
        PlayerAirAttack_RTL airAttack02RTL = new PlayerAirAttack_RTL(11, spriteSheetMap.get("AirAttack02").convertRTL(), 20);
        PlayerAirAttack_RTL airAttack03RTL = new PlayerAirAttack_RTL(11, spriteSheetMap.get("AirAttack03").convertRTL(), 20);
        PlayerAirAttack_RTL airAttackLoopRTL = new PlayerAirAttack_RTL(11, spriteSheetMap.get("AirAttack03Loop").convertRTL(), 20);
        PlayerJumpRoll_RTL jumpRollRTL = new PlayerJumpRoll_RTL(12, spriteSheetMap.get("JumpRoll01").convertRTL(), 15);
        PlayerGetUp_RTL getUpRTL = new PlayerGetUp_RTL(13, spriteSheetMap.get("GetUp01").convertRTL(), 15);
        PlayerFallDown_RTL knockDownRTL = new PlayerFallDown_RTL(14, spriteSheetMap.get("KnockDown01").convertRTL(), 25);
        PlayerLedgeAction_RTL ledgeClimbRTL = new PlayerLedgeAction_RTL(15, spriteSheetMap.get("LedgeClimb01").convertRTL(), 15);
        PlayerLedgeAction_RTL ledgeGrabRTL = new PlayerLedgeAction_RTL(16, spriteSheetMap.get("LedgeGrab01").convertRTL(), 15);
        PlayerWallAction_RTL wallRunRTL = new PlayerWallAction_RTL(17, spriteSheetMap.get("WallRun01").convertRTL(), 15);
        PlayerWallAction_RTL wallSlideRTL = new PlayerWallAction_RTL(18, spriteSheetMap.get("WallSlide01").convertRTL(), 15);
        PlayerRun_RTL sprintRTL = new PlayerRun_RTL(19, spriteSheetMap.get("Sprint01").convertRTL(), 0);
        // reverse arrays animations
        jumpLTR.getSheet().reverseImages();
        jumpRTL.getSheet().reverseImages();

        //Put Animations to HashMap
        Map<CharacterState, Animation> playerAnimations = new HashMap();

        //Run directions
        playerAnimations.put(CharacterState.RUNFORWARD, runLTR);
        playerAnimations.put(CharacterState.RUNBACK, runRTL);
        playerAnimations.put(CharacterState.SPRINT_LTR, sprintLTR);//New
        playerAnimations.put(CharacterState.SPRINT_RTL, sprintRTL);//New
        playerAnimations.put(CharacterState.WALLRUN_LTR, wallRunLTR);//New
        playerAnimations.put(CharacterState.WALLRUN_RTL, wallRunRTL);//New
        playerAnimations.put(CharacterState.WALLSLIDE_LTR, wallSlideLTR);
        playerAnimations.put(CharacterState.WALLSLIDE_RTL, wallSlideRTL);

        //Idle
        playerAnimations.put(CharacterState.IDLE_LTR, idleLTR);
        playerAnimations.put(CharacterState.IDLE_RTL, idleRTL);
        playerAnimations.put(CharacterState.FIREIDLE_LTR, fireIdleLTR);
        playerAnimations.put(CharacterState.FIREIDLE_RTL, fireIdleRTL);

        //Attacks
        playerAnimations.put(CharacterState.ATTACK01_LTR, attack01LTR);
        playerAnimations.put(CharacterState.ATTACK01_RTL, attack01RTL);
        playerAnimations.put(CharacterState.ATTACK02_LTR, attack02LTR);
        playerAnimations.put(CharacterState.ATTACK02_RTL, attack02RTL);
        playerAnimations.put(CharacterState.ATTACK03_LTR, attack03LTR);
        playerAnimations.put(CharacterState.ATTACK03_RTL, attack03RTL);
        playerAnimations.put(CharacterState.FIREATTACK01_LTR, fireAttack01LTR);
        playerAnimations.put(CharacterState.FIREATTACK01_RTL, fireAttack01RTL);
        playerAnimations.put(CharacterState.AIRATTACK01_LTR, airAttack01LTR); //New
        playerAnimations.put(CharacterState.AIRATTACK01_RTL, airAttack01RTL);//New
        playerAnimations.put(CharacterState.AIRATTACK02_LTR, airAttack02LTR);//New
        playerAnimations.put(CharacterState.AIRATTACK02_RTL, airAttack02RTL);//New
        playerAnimations.put(CharacterState.AIRATTACK03_LTR, airAttack03LTR);//New
        playerAnimations.put(CharacterState.AIRATTACK03_RTL, airAttack03RTL);//New
        playerAnimations.put(CharacterState.AIRATTACKLOOP_LTR, airAttackLoopLTR);//New
        playerAnimations.put(CharacterState.AIRATTACKLOOP_RTL, airAttackLoopRTL);//New

        //Get Hit
        playerAnimations.put(CharacterState.GET_HIT_LTR, hitLTR);
        playerAnimations.put(CharacterState.GET_HIT_RTL, hitRTL);

        //Death Animation
        playerAnimations.put(CharacterState.DEATH_LTR, deathLTR);
        playerAnimations.put(CharacterState.DEATH_RTL, deathRTL);

        //Jump Animation
        playerAnimations.put(CharacterState.JUMP_LTR, jumpLTR);
        playerAnimations.put(CharacterState.JUMP_RTL, jumpRTL);
        playerAnimations.put(CharacterState.JUMPROLL_LTR, jumpRollLTR);
        playerAnimations.put(CharacterState.JUMPROLL_RTL, jumpRollRTL);

        //Crouch Animation
        playerAnimations.put(CharacterState.CROUCH_LTR, crouchLTR);
        playerAnimations.put(CharacterState.CROUCH_RTL, crouchRTL);

        //Slide Animation
        playerAnimations.put(CharacterState.SLIDE_LTR, slideLTR);
        playerAnimations.put(CharacterState.SLIDE_RTL, slideRTL);

        //Falldown
        playerAnimations.put(CharacterState.FALLDOWN_LTR, fallDownLTR);
        playerAnimations.put(CharacterState.FALLDOWN_RTL, fallDownRTL);
        playerAnimations.put(CharacterState.KNOCKDOWN_LTR, knockDownLTR);//New
        playerAnimations.put(CharacterState.KNOCKDOWN_RTL, knockDownRTL);//New
        playerAnimations.put(CharacterState.GETUP_LTR, getUpLTR);//New
        playerAnimations.put(CharacterState.GETUP_RTL, getUpRTL);//New

        //SpellCast
        playerAnimations.put(CharacterState.SPELLCAST_LTR, spellCastLTR);
        playerAnimations.put(CharacterState.SPELLCAST_RTL, spellCastRTL);
        playerAnimations.put(CharacterState.SPELLCASTLOOP_LTR, spellCastLoopLTR);
        playerAnimations.put(CharacterState.SPELLCASTLOOP_RTL, spellCastLoopRTL);

        //Ledge Climb
        playerAnimations.put(CharacterState.LEDGECLIMB_LTR, ledgeClimbLTR);//New
        playerAnimations.put(CharacterState.LEDGECLIMB_RTL, ledgeClimbRTL);//New
        playerAnimations.put(CharacterState.LEDGEGRAB_LTR, ledgeGrabLTR);//New
        playerAnimations.put(CharacterState.LEDGEGRAB_RTL, ledgeGrabRTL);//New

        //Init Inventory
        SpriteSheet inventorySheet = new SpriteSheet();
        inventorySheet.setImages(ImageManager.loadImagesFromFolderToList("assets/res/inventory"));

        //Init Player
        player = new Player(0, "Shinobu Windsor", 100, defPlayerPosition,
                playerAnimations, null, this, inventorySheet);
        //Init platforms
        player.setInsidePlatform(firstPlatform);

        //Init Ability
        abilitiesCharacterInit(player.getAbilities(), player);

        //Init Items
        SpriteSheet fireSwordSheet = new SpriteSheet();
        fireSwordSheet.add("assets/res/item/icon_items/Swords/Fire_Sworld.png");
        FireSwordAnimation fireSwordAnimation = new FireSwordAnimation(1, fireSwordSheet, -1);
        Map<CharacterState, Animation> itemEquipAnimations = new HashMap<CharacterState, Animation>();
        itemEquipAnimations.put(CharacterState.IDLE_LTR, fireIdleLTR);
        itemEquipAnimations.put(CharacterState.IDLE_RTL, fireIdleRTL);
        itemEquipAnimations.put(CharacterState.ATTACK01_LTR, fireAttack01LTR);
        itemEquipAnimations.put(CharacterState.ATTACK01_RTL, fireAttack01RTL);
        Sword fireSword = new Sword(1, "Fire Sword", fireSwordAnimation, null, this, 1, itemEquipAnimations);
        AttackIncrease attackIncrease = new AttackIncrease(1, "Attack Increase", 30, null, null, this, null);
        fireSword.getAbilities().add(attackIncrease);
//        platform spawn
//        Platform spawnArea = getPlatforms().get(4).get(10);
//        fireSword.setPosition(spawnArea.middlePlatform(Item.ITEM_WIDTH + 80, Item.ITEM_HEIGHT + 80));
//        fireSword.getPosition().setYPosition(fireSword.getPosition().getYPosition() + 55);
//        fireSword.setSpawnForever(true);
//        itemsOnGround.add(fireSword);
        Platform platform = getPlatforms().get(4).get(10);
        if (platform != null) {
            fireSword.setPosition(platform.middlePlatform(Item.ITEM_WIDTH + 80, Item.ITEM_HEIGHT + 80));
            fireSword.getPosition().setYPosition(fireSword.getPosition().getYPosition() + 55);
            ((Chest) platform.getObjects().get(0)).getItems().add(fireSword);
        }

        itemInit(player.getInventory(), player);

        //Init Handler
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
        camera.setPlayer(player);
        // level up to 8
//        player.getStats().addExperience(50000);
    }

    public void itemInit(Inventory inventory, Character character) {
        SpriteSheet healthPotionSheet = new SpriteSheet();
        healthPotionSheet.add("assets/res/item/s_potion.png");
        HealthPotionAnimation healthPotionAnimation = new HealthPotionAnimation(0, healthPotionSheet, -1);
        HealthPotion healthPotion = new HealthPotion(itemCount, "S Health", healthPotionAnimation, character,
                this, 1);
        abilitiesItemInit(healthPotion.getAbilities(), character);
        inventory.addItemToInventory(healthPotion);
        itemCount++;
    }

    public void abilitiesItemInit(List<Ability> abilities, Character character) {
        Ability potionHeal = new PotionHeal(5, 0, "S Potion", 500, null, null, this, character);
        abilities.add(potionHeal);
    }

    public void abilitiesCharacterInit(List<Ability> abilities, Character character) {
        List<BufferedImage> fireBallsLTR = ImageManager.loadImagesWithCutFromFolderToList("assets/res/ability/Fire Ball/LTR", 200, 365, 580, 200);
        List<BufferedImage> fireBallsRTL = ImageManager.loadImagesWithCutFromFolderToList("assets/res/ability/Fire Ball/RTL", 30, 365, 580, 200);
        if (character instanceof Player) {
            BufferedImage redBorder = ImageManager.loadImage("assets/res/ability/border-red.png");
            SpriteSheet greaterHealSheet = new SpriteSheet();
            greaterHealSheet.getImages().add(ImageManager.loadImage("assets/res/ability/Greater-Heal.png"));
            GamePosition firstSkillPosition = new GamePosition(character.getHealthBar().getHealthBarPos().getXPosition(),
                    character.getHealthBar().getHealthBarPos().getMaxY() + 90, 80, 80);
            Ability greaterHeal = new GreaterHeal(20, 1, 2500, greaterHealSheet,
                    firstSkillPosition, null, null, redBorder, this, character);
            abilities.add(greaterHeal);
            SpriteSheet fireballIcon = new SpriteSheet();
            fireballIcon.getImages().add(ImageManager.loadImage("assets/res/ability/Fire-Ball.png"));
            SpriteSheet sheetLTR = new SpriteSheet();
            SpriteSheet sheetRTL = new SpriteSheet();
            if (fireBallsLTR == null || fireBallsRTL == null) {
                return;
            }
            sheetLTR.setImages(fireBallsLTR);
            sheetRTL.setImages(fireBallsRTL);
            Animation fireBallAnimationLTR = new FireBallAnimation(0, sheetLTR, 0);
            Animation fireBallAnimationRTL = new FireBallAnimation(1, sheetRTL, 0);
            Fireball fireball = new Fireball(150, 30, 2, 500, fireballIcon,
                    new GamePosition(firstSkillPosition.getMaxX() + 15,
                            firstSkillPosition.getYPosition(), firstSkillPosition.getWidth(), firstSkillPosition.getHeight()),
                    fireBallAnimationLTR, fireBallAnimationRTL, redBorder, this, character);
            abilities.add(fireball);
        } else {
            if (((DiorEnemy) character).getColor() == DiorColor.Red) {
                SpriteSheet sheetLTR = new SpriteSheet();
                SpriteSheet sheetRTL = new SpriteSheet();
                if (fireBallsLTR == null || fireBallsRTL == null) {
                    return;
                }
                sheetLTR.setImages(fireBallsLTR);
                sheetRTL.setImages(fireBallsRTL);
                Animation fireBallAnimationLTR = new FireBallAnimation(0, sheetLTR, 0);
                Animation fireBallAnimationRTL = new FireBallAnimation(1, sheetRTL, 0);
                Fireball fireball = new Fireball(20, 15, 2, 2000, null, null,
                        fireBallAnimationLTR, fireBallAnimationRTL, this, character);
                abilities.add(fireball);
            }
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
                            int platformStandSize = getPlatforms().get(9).size() - 1;
                            int platformColumn = platformStandSize;
                            for (int i = 0; i < numberOfEnemies; i++) {
                                Platform platform = getPlatforms().get(9).get(platformColumn);
                                if (platform == null) {
                                    continue;
                                }
                                if (platform instanceof Tile || platform instanceof WallTile) {
                                    if (i > 0) {
                                        i--;
                                    }
                                } else {
                                    diorInit(platform);
                                }
                                if (platformColumn > 0) {
                                    platformColumn--;
                                } else {
                                    platformColumn = platformStandSize;
                                }
                            }
                        } catch (Exception ex) {
                            System.out.println(ex.toString());
                        }
                        spawnCounter = 0;
                    }
                    updateTime = System.nanoTime() - now;
                    wait = (OPTIMAL_TIME - updateTime) / 1000000;
                    try {
                        Thread.sleep(wait);
                    } catch (Exception e) {

                    }
                }
            }
        };
    }

    public void tick() {
        if (background != null) {
            background.tick();
        }
        if (renderMap) {
            if (map != null) {
                map.tick();
            }
        }
        if (camera != null) {
            camera.tick();
        }
        if (enemies != null) {
            if (enemies.size() > 0) {
                for (int i = 0; i < enemies.size(); i++) {
                    Enemy enemy = enemies.get(i);
                    enemy.tick();
                }
            }
        }
        if (itemsOnGround.size() > 0) {
            for (int i = 0; i < itemsOnGround.size(); i++) {
                Item item = itemsOnGround.get(i);
                if (item != null) {
                    item.tick();
                }
            }
        }
        if (player != null) {
            player.tick();
        }
    }

    public void render(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        if (background != null) {
            background.render(g2);
        }
        if (renderMap) {
            if (map != null) {
                map.render(g2);
            }
        }
        if (camera != null) {
            camera.render(g2);
        }
        if (enemies != null) {
            if (enemies.size() > 0) {
                for (int i = 0; i < enemies.size(); i++) {
                    Enemy enemy = enemies.get(i);
                    if (camera.checkPositionRelateToCamera(enemy.getPosition())) {
                        enemy.render(g2);
                    }
                }
            }
        }
        if (itemsOnGround.size() > 0) {
            for (int i = 0; i < itemsOnGround.size(); i++) {
                Item item = itemsOnGround.get(i);
                if (item != null) {
                    if (camera.checkPositionRelateToCamera(item.getPosition())) {
                        item.render(g2);
                    }
                }
            }
        }
        if (player != null) {
            player.render(g2);
        }
    }

    public Camera getCamera() {
        return camera;
    }

    public List<List<Platform>> getPlatforms() {
        return background.getScene();
    }

    public GamePosition getPositionFromPlatform(Platform platform, int characterWidth, int characterHeight) {
        return new GamePosition(platform.getPosition().getXPosition() + 5,
                platform.getPosition().getYPosition(), characterWidth, characterHeight);
    }

    public List<List<Platform>> getSurroundPlatform(int i, int j) {
        return background.getSurroundPlatform(i, j, background.DEF_SURROUND_TILE,
                0, 0, 0, 0);
    }

    public GamePosition getScenePosition() {
        return background.getPosition();
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

    public boolean isRenderMap() {
        return renderMap;
    }

    public void setRenderMap(boolean renderMap) {
        this.renderMap = renderMap;
    }

    public Thread getThread() {
        return spawnEnemiesThread;
    }

    public void setThread(Thread thread) {
        this.spawnEnemiesThread = thread;
    }

    public AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }

    public void setAudioPlayer(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }

    public List<Item> getItemsOnGround() {
        return itemsOnGround;
    }

}
