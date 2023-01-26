package fightinggame.entity.item.collectable.buff;

import fightinggame.Gameplay;
import fightinggame.animation.item.PotionAnimation;
import fightinggame.entity.Character;
import fightinggame.entity.SpriteSheet;
import fightinggame.entity.item.Item;

public class LargeHealthPotion extends HealthPotion {

    public LargeHealthPotion(int id, Character character, Gameplay gameplay, int amount) {
        super(id, "L Health Potion", null, character, gameplay, amount, 30);
        SpriteSheet healthPotionSheet = new SpriteSheet();
        healthPotionSheet.add("assets/res/item/Potions/l_health_potion.png");
        animation = new PotionAnimation(0, healthPotionSheet, -1);
    }

    public LargeHealthPotion(int id, Character character, Gameplay gameplay, int amount, int price) {
        super(id, "L Health Potion", null, character, gameplay, amount, price, 30);
        SpriteSheet healthPotionSheet = new SpriteSheet();
        healthPotionSheet.add("assets/res/item/Potions/l_health_potion.png");
        animation = new PotionAnimation(0, healthPotionSheet, -1);
    }

    @Override
    public Item clone() {
        return new LargeHealthPotion(id, character, gameplay, amount, price);
    }

}
