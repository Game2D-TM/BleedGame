package fightinggame.entity.npc.merchant;

import fightinggame.Gameplay;
import fightinggame.animation.enemy.EnemyHeavyAttack;
import fightinggame.animation.enemy.EnemyIdle;
import fightinggame.animation.enemy.EnemyRunBack;
import fightinggame.animation.enemy.EnemyRunForward;
import fightinggame.entity.Animation;
import fightinggame.entity.GamePosition;
import fightinggame.entity.SpriteSheet;
import fightinggame.entity.npc.MerchantNPC;
import fightinggame.entity.npc.NPC;
import fightinggame.entity.platform.Platform;
import fightinggame.entity.state.CharacterState;
import fightinggame.input.handler.game.npc.NPCMovementHandler;
import fightinggame.resource.EnemyAnimationResources;
import fightinggame.resource.ImageManager;
import java.util.HashMap;
import java.util.Map;

public class Advanturer extends MerchantNPC {

    public Advanturer(int id, String name, GamePosition position, Map<CharacterState, Animation> animations, Gameplay gameplay, boolean isLTR) {
        super(id, name, position, animations, gameplay, isLTR);
    }

    public Advanturer() {
    }
    
    @Override
    public NPC init(Platform firstPlatform, Gameplay gameplay) {
        GamePosition npcPos = new GamePosition(firstPlatform.getPosition().getXPosition(),
                firstPlatform.getPosition().getYPosition(), 180, 220);
        String loc = EnemyAnimationResources.ADVANTURER_SHEET_LOC;
        SpriteSheet idleLTRSheet = new SpriteSheet(ImageManager.loadImage(loc + "/Idle.png"),
                0, 0, 64, 80,
                20, 15, 38, 50, 4);
        SpriteSheet runLTRSheet = new SpriteSheet(ImageManager.loadImage(loc + "/Run.png"),
                0, 0, 64, 80,
                20, 15, 38, 50, 8);
        SpriteSheet attack01LTRSheet = new SpriteSheet(ImageManager.loadImage(loc + "/Attack01.png"),
                0, 0, 64, 80,
                20, 15, 38, 50, 8);

        SpriteSheet idleRTLSheet = idleLTRSheet.convertRTL();
        SpriteSheet runRTLSheet = runLTRSheet.convertRTL();
        SpriteSheet attack01RTLSheet = attack01LTRSheet.convertRTL();

        EnemyIdle idleRTL = new EnemyIdle(0, idleRTLSheet);
        EnemyIdle idleLTR = new EnemyIdle(1, idleLTRSheet);
        EnemyRunForward runForward = new EnemyRunForward(6, runRTLSheet, 40);
        EnemyRunBack runBack = new EnemyRunBack(7, runLTRSheet, 40);
        EnemyHeavyAttack attack01RTL = new EnemyHeavyAttack(8, attack01RTLSheet, 3);
        EnemyHeavyAttack attack01LTR = new EnemyHeavyAttack(9, attack01LTRSheet, 3);

        Map<CharacterState, Animation> npcAnimations = new HashMap();

        npcAnimations.put(CharacterState.IDLE_RTL, idleRTL);
        npcAnimations.put(CharacterState.IDLE_LTR, idleLTR);
        npcAnimations.put(CharacterState.RUNFORWARD, runForward);
        npcAnimations.put(CharacterState.RUNBACK, runBack);
        npcAnimations.put(CharacterState.ATTACK01_RTL, attack01RTL);
        npcAnimations.put(CharacterState.ATTACK01_LTR, attack01LTR);

        NPC advanturerNpc = new Advanturer(0, "Olivia Holster", npcPos, npcAnimations, gameplay, false);
        advanturerNpc.setInsidePlatform(firstPlatform);
        NPCMovementHandler movementHandler = new NPCMovementHandler(advanturerNpc, gameplay, "Olivia Holster Movement Handler");
        advanturerNpc.getController().add(movementHandler);
        return advanturerNpc;
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
        return null;
    }

}
