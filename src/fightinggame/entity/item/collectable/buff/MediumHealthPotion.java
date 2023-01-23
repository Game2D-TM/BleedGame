package fightinggame.entity.item.collectable.buff;

import fightinggame.Gameplay;
import fightinggame.animation.item.PotionAnimation;
import fightinggame.entity.Character;
import fightinggame.entity.SpriteSheet;
import fightinggame.entity.item.Item;

public class MediumHealthPotion extends HealthPotion{

    public MediumHealthPotion(int id, Character character, Gameplay gameplay, int amount) {
        super(id, "M Health Potion", null, character, gameplay, amount, 20);
        SpriteSheet healthPotionSheet = new SpriteSheet();
        healthPotionSheet.add("assets/res/item/m_health_potion.png");
        animation = new PotionAnimation(0, healthPotionSheet, -1);
    }

    public MediumHealthPotion(int id, Character character, Gameplay gameplay, int amount, int price) {
        super(id, "M Health Potion", null, character, gameplay, amount, price, 20);
        SpriteSheet healthPotionSheet = new SpriteSheet();
        healthPotionSheet.add("assets/res/item/m_health_potion.png");
        animation = new PotionAnimation(0, healthPotionSheet, -1);
    }

    @Override
    public Item clone() {
        return new MediumHealthPotion(id, character, gameplay, amount, price);
    }
    
}
