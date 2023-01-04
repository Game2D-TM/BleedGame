package fightinggame.entity.item.collectable.quest;

import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.entity.Character;
import fightinggame.entity.item.collectable.CollectableItem;

public class Key extends CollectableItem {

    public Key(int id, String name, Animation animation, Character character, Gameplay gameplay, int amount) {
        super(id, name, animation, character, gameplay, amount);
    }

    @Override
    public boolean use() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Key clone() {
        return new Key(id, name, animation, character, gameplay, amount);
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
    public int getXMaxHitBox() {
        return position.getMaxX();
    }

    @Override
    public int getYHitBox() {
        return position.getYPosition();
    }

    @Override
    public int getYMaxHitBox() {
        return position.getMaxY();
    }
    
}
