package fightinggame.entity.item.collectable.quest;

import fightinggame.Gameplay;
import fightinggame.animation.item.KeyAnimation;
import fightinggame.entity.Character;
import fightinggame.entity.SpriteSheet;
import fightinggame.entity.item.collectable.CollectableItem;

public class Key extends CollectableItem {

    public Key(int id, String name, Character character, Gameplay gameplay, int amount) {
        super(id, name, null, character, gameplay, amount);
        SpriteSheet keySheet = new SpriteSheet();
        keySheet.add("assets/res/item/key.png");
        animation = new KeyAnimation(1, keySheet, -1);
    }

    @Override
    public boolean use() {
        return true;
    }

    @Override
    public Key clone() {
        return new Key(id, name, character, gameplay, amount);
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
