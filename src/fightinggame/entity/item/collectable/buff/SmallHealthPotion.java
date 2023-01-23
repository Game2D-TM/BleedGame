package fightinggame.entity.item.collectable.buff;

import fightinggame.Gameplay;
import fightinggame.animation.item.PotionAnimation;
import fightinggame.entity.Character;
import fightinggame.entity.SpriteSheet;
import fightinggame.entity.item.Item;

public class SmallHealthPotion extends HealthPotion {

    public SmallHealthPotion(int id, Character character, Gameplay gameplay, int amount) {
        super(id, "S Health Potion", null, character, gameplay, amount, 10);
        SpriteSheet healthPotionSheet = new SpriteSheet();
        healthPotionSheet.add("assets/res/item/s_health_potion.png");
        animation = new PotionAnimation(0, healthPotionSheet, -1);
    }

    public SmallHealthPotion(int id, Character character, Gameplay gameplay, int amount, int price) {
        super(id, "S Health Potion", null, character, gameplay, amount, price, 10);
        SpriteSheet healthPotionSheet = new SpriteSheet();
        healthPotionSheet.add("assets/res/item/s_health_potion.png");
        animation = new PotionAnimation(0, healthPotionSheet, -1);
    }

    @Override
    public Item clone() {
        return new SmallHealthPotion(id, character, gameplay, amount);
    }

}
