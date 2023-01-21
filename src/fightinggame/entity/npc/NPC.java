package fightinggame.entity.npc;

import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.entity.Character;
import fightinggame.entity.GamePosition;
import fightinggame.entity.SpriteSheet;
import fightinggame.entity.state.CharacterState;
import java.util.Map;

public class NPC extends Character{

    public NPC(int id, String name, int health
            , GamePosition position, Map<CharacterState
            , Animation> animations, Gameplay gameplay
            , boolean isLTR, SpriteSheet inventorySheet) {
        super(id, name, health, position, animations, gameplay, isLTR, inventorySheet);
    }

    @Override
    protected void healthBarInit(int maxHealth) {
    }

    @Override
    public void healthBarTick() {
    }

    @Override
    public boolean checkHit(GamePosition attackHitBox, boolean isAttack, Character character, int attackDamage) {
        return false;
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
