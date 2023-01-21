package fightinggame.entity.npc;

import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.entity.Character;
import fightinggame.entity.GamePosition;
import fightinggame.entity.SpriteSheet;
import fightinggame.entity.platform.Platform;
import fightinggame.entity.state.CharacterState;
import fightinggame.resource.DataManager;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Map;

public abstract class NPC extends Character {

    public NPC(int id, String name, GamePosition position, 
            Map<CharacterState, Animation> animations, Gameplay gameplay,
            boolean isLTR, SpriteSheet inventorySheet) {
        super(id, name, 100000, position, animations, gameplay, isLTR, inventorySheet);
    }
    
    public NPC(int id, String name, GamePosition position, 
            Map<CharacterState, Animation> animations, Gameplay gameplay,
            boolean isLTR) {
        super(id, name, 100000, position, animations, gameplay, isLTR, null);
    }

    public NPC() {
    }
    
    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        g.setColor(Color.red);
        g.setFont(DataManager.getFont(30f));
        g.drawString(name, position.getXPosition() + 20 - gameplay.getCamera().getPosition().getXPosition()
                , position.getYPosition() - 20 - gameplay.getCamera().getPosition().getYPosition());
                //hitbox
        g.setColor(Color.red);
        g.drawRect(getXHitBox() - gameplay.getCamera().getPosition().getXPosition(),
                getYHitBox() - gameplay.getCamera().getPosition().getYPosition(),
                getWidthHitBox(), getHeightHitBox());
    }
    
    

    @Override
    protected void healthBarInit(int maxHealth) {
    }

    @Override
    public boolean checkHit(GamePosition position, boolean isAttack, Character character, int attackDamage) {
        return checkHit(position, character);
    }

    public boolean checkHit(GamePosition position, Character character) {
        if (((position.getXPosition() >= getXHitBox() && position.getXPosition() <= getXMaxHitBox())
                || (position.getXPosition() >= getXHitBox() && position.getXPosition() <= getXMaxHitBox()
                && position.getMaxX() > getXMaxHitBox())
                || (position.getMaxX() >= getXHitBox() && position.getMaxX() <= getXMaxHitBox()
                && position.getXPosition() < getXHitBox())
                || (position.getXPosition() < getXHitBox() && position.getMaxX() > getXMaxHitBox()))
                && ((position.getYPosition() <= getYHitBox() && position.getYPosition() >= getYMaxHitBox()
                || (position.getYPosition() >= getYHitBox() && position.getMaxY() <= getYMaxHitBox())
                || (position.getYPosition() > getYHitBox() && position.getYPosition() <= getYMaxHitBox()
                && position.getMaxY() > getYMaxHitBox())
                || (position.getMaxY() > getYHitBox() && position.getMaxY() <= getYMaxHitBox()
                && position.getYPosition() < getYHitBox())))) {
            return true;
        }
        return false;
    }

    public abstract NPC init(Platform firstPlatform, Gameplay gameplay);

}
