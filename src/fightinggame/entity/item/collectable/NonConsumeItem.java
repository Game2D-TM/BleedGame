package fightinggame.entity.item.collectable;

import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.entity.Character;

public abstract class NonConsumeItem extends CollectableItem{
    
    public NonConsumeItem(int id, String name, Animation animation, Character character, Gameplay gameplay, int amount) {
        super(id, name, animation, character, gameplay, amount);
    }

    public NonConsumeItem(int id, String name, Animation animation, Character character, Gameplay gameplay, int amount, int price) {
        super(id, name, animation, character, gameplay, amount, price);
    }

}
