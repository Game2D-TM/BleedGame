package fightinggame.entity.item.collectable.buff;

import fightinggame.Gameplay;
import fightinggame.animation.item.PotionAnimation;
import fightinggame.entity.Character;
import fightinggame.entity.SpriteSheet;
import fightinggame.entity.item.Item;

public class SmallElixirPotion extends ElixirPotion{

    public SmallElixirPotion(int id, Character character, Gameplay gameplay, int amount) {
        super(id, "S Elixir Potion", null, character, gameplay, amount, 5, 5);
        SpriteSheet elixirSheet = new SpriteSheet();
        elixirSheet.add("assets/res/item/s_elixir_potion.png");
        animation = new PotionAnimation(0, elixirSheet, -1);
    }

    public SmallElixirPotion(int id, Character character, Gameplay gameplay, int amount, int price) {
        super(id, "S Elixir Potion", null, character, gameplay, amount, price, 5, 5);
        SpriteSheet elixirSheet = new SpriteSheet();
        elixirSheet.add("assets/res/item/s_elixir_potion.png");
        animation = new PotionAnimation(0, elixirSheet, -1);
    }
    

    @Override
    public Item clone() {
        return new SmallElixirPotion(id, character, gameplay, amount, price);
    }
    
}
