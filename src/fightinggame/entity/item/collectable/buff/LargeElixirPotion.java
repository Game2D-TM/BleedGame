package fightinggame.entity.item.collectable.buff;

import fightinggame.Gameplay;
import fightinggame.animation.item.PotionAnimation;
import fightinggame.entity.Character;
import fightinggame.entity.SpriteSheet;
import fightinggame.entity.item.Item;

public class LargeElixirPotion extends ElixirPotion {

    public LargeElixirPotion(int id, Character character, Gameplay gameplay, int amount) {
        super(id, "L Elixir Potion", null, character, gameplay, amount, 15, 15);
        SpriteSheet elixirSheet = new SpriteSheet();
        elixirSheet.add("assets/res/item/Potions/l_elixir_potion.png");
        animation = new PotionAnimation(0, elixirSheet, -1);
    }

    public LargeElixirPotion(int id, Character character, Gameplay gameplay, int amount, int price) {
        super(id, "L Elixir Potion", null, character, gameplay, amount, price, 15, 15);
        SpriteSheet elixirSheet = new SpriteSheet();
        elixirSheet.add("assets/res/item/Potions/l_elixir_potion.png");
        animation = new PotionAnimation(0, elixirSheet, -1);
    }

    @Override
    public Item clone() {
        return new LargeElixirPotion(id, character, gameplay, amount, price);
    }

}
