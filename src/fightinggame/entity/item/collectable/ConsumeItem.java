package fightinggame.entity.item.collectable;

import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.entity.Character;

public abstract class ConsumeItem extends CollectableItem{
    
    public ConsumeItem(int id, String name, Animation animation, Character character, Gameplay gameplay, int amount) {
        super(id, name, animation, character, gameplay, amount);
    }

    public ConsumeItem(int id, String name, Animation animation, Character character, Gameplay gameplay, int amount, int price) {
        super(id, name, animation, character, gameplay, amount, price);
    }
    
}
