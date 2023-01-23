package fightinggame.entity.npc.merchant;

import fightinggame.Gameplay;
import fightinggame.animation.player.PlayerHeavyAttack;
import fightinggame.animation.player.PlayerIdle;
import fightinggame.animation.player.PlayerRun_LTR;
import fightinggame.animation.player.PlayerRun_RTL;
import fightinggame.entity.Animation;
import fightinggame.entity.GamePosition;
import fightinggame.entity.SpriteSheet;
import fightinggame.entity.item.Item;
import fightinggame.entity.item.collectable.buff.LargeEnergyPotion;
import fightinggame.entity.item.collectable.buff.LargeHealthPotion;
import fightinggame.entity.item.collectable.buff.MediumEnergyPotion;
import fightinggame.entity.item.collectable.buff.MediumHealthPotion;
import fightinggame.entity.item.collectable.buff.SmallEnergyPotion;
import fightinggame.entity.item.collectable.buff.SmallHealthPotion;
import fightinggame.entity.npc.MerchantNPC;
import fightinggame.entity.npc.NPC;
import fightinggame.entity.platform.Platform;
import fightinggame.entity.state.CharacterState;
import fightinggame.input.handler.game.npc.NPCMovementHandler;
import fightinggame.resource.EnemyAnimationResources;
import fightinggame.resource.ImageManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pharmacist extends MerchantNPC {

    public Pharmacist(int id, String name, GamePosition position, Map<CharacterState, Animation> animations, Gameplay gameplay, boolean isLTR) {
        super(id, name, position, animations, gameplay, isLTR);
        shopAvatar = ImageManager.loadImage("assets/res/gui/avatar/pharmacist_shop_avatar.png");
        List<String> texts = new ArrayList<>();
        texts.add("Hi, Advanturer!");
        texts.add("My name is " + name + ". Nice to see you!");
        texts.add("I produce some potions wanna check it out?");
        dialogue.setTexts(texts);
    }

    public Pharmacist() {
    }

    @Override
    public NPC init(Platform firstPlatform, Gameplay gameplay) {
        GamePosition npcPos = new GamePosition(firstPlatform.getPosition().getXPosition(),
                firstPlatform.getPosition().getYPosition(), 180, 220);
        String loc = EnemyAnimationResources.PHARMACIST_SHEET_LOC;
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

        PlayerIdle idleRTL = new PlayerIdle(0, idleRTLSheet);
        PlayerIdle idleLTR = new PlayerIdle(1, idleLTRSheet);
        PlayerRun_LTR run_LTR = new PlayerRun_LTR(6, runRTLSheet, 40);
        PlayerRun_RTL run_RTL = new PlayerRun_RTL(7, runLTRSheet, 40);
        PlayerHeavyAttack attack01RTL = new PlayerHeavyAttack(8, attack01RTLSheet, 3);
        PlayerHeavyAttack attack01LTR = new PlayerHeavyAttack(9, attack01LTRSheet, 3);

        Map<CharacterState, Animation> npcAnimations = new HashMap();

        npcAnimations.put(CharacterState.IDLE_RTL, idleRTL);
        npcAnimations.put(CharacterState.IDLE_LTR, idleLTR);
        npcAnimations.put(CharacterState.RUNFORWARD, run_LTR);
        npcAnimations.put(CharacterState.RUNBACK, run_RTL);
        npcAnimations.put(CharacterState.ATTACK01_RTL, attack01RTL);
        npcAnimations.put(CharacterState.ATTACK01_LTR, attack01LTR);

        NPC pharmacistNPC = new Pharmacist(0, "Olivia Holster", npcPos, npcAnimations, gameplay, false);
        pharmacistNPC.setInsidePlatform(firstPlatform);
        NPCMovementHandler movementHandler = new NPCMovementHandler(pharmacistNPC, gameplay, pharmacistNPC.getName() + " Movement Handler");
        pharmacistNPC.getController().add(movementHandler);
        return pharmacistNPC;
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

    @Override
    public void initItems() {
        Item item;
        item = new SmallHealthPotion(0, this,
                gameplay, 15, 10);
        inventory.addItemToInventory(item);
        item = new SmallEnergyPotion(0, this,
                gameplay, 15, 10);
        inventory.addItemToInventory(item);
        item = new MediumHealthPotion(0, this, gameplay, 15, 20);
        inventory.addItemToInventory(item);
        item = new MediumEnergyPotion(0, this, gameplay, 15, 20);
        inventory.addItemToInventory(item);
        item = new LargeEnergyPotion(0, this, gameplay, 15, 30);
        inventory.addItemToInventory(item);
        item = new LargeHealthPotion(0, this, gameplay, 15, 30);
        inventory.addItemToInventory(item);
    }

}
