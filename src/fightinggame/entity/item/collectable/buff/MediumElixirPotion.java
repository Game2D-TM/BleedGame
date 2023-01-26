package fightinggame.entity.item.collectable.buff;

import fightinggame.Gameplay;
import fightinggame.animation.item.PotionAnimation;
import fightinggame.entity.Character;
import fightinggame.entity.SpriteSheet;
import fightinggame.entity.item.Item;

public class MediumElixirPotion extends ElixirPotion{

    public MediumElixirPotion(int id, Character character, Gameplay gameplay, int amount) {
        super(id, "M Elixir Potion", null, character, gameplay, amount, 10, 10);
        SpriteSheet elixirSheet = new SpriteSheet();
        elixirSheet.add("assets/res/item/Potions/m_elixir_potion.png");
        animation = new PotionAnimation(0, elixirSheet, -1);
    }

    public MediumElixirPotion(int id, Character character, Gameplay gameplay, int amount, int price) {
        super(id, "M Elixir Potion", null, character, gameplay, amount, price, 10, 10);
        SpriteSheet elixirSheet = new SpriteSheet();
        elixirSheet.add("assets/res/item/Potions/m_elixir_potion.png");
        animation = new PotionAnimation(0, elixirSheet, -1);
    }
    
    @Override
    public Item clone() {
        return new MediumElixirPotion(id, character, gameplay, amount, price);
    }
    
}
