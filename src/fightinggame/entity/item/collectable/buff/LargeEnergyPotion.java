package fightinggame.entity.item.collectable.buff;

import fightinggame.Gameplay;
import fightinggame.animation.item.PotionAnimation;
import fightinggame.entity.Character;
import fightinggame.entity.SpriteSheet;
import fightinggame.entity.item.Item;

public class LargeEnergyPotion extends EnergyPotion {

    public LargeEnergyPotion(int id, Character character, Gameplay gameplay, int amount) {
        super(id, "L Energy Potion", null, character, gameplay, amount, 30);
        SpriteSheet energyPotionSheet = new SpriteSheet();
        energyPotionSheet.add("assets/res/item/l_energy_potion.png");
        animation = new PotionAnimation(0, energyPotionSheet, -1);
    }

    public LargeEnergyPotion(int id, Character character, Gameplay gameplay, int amount, int price) {
        super(id, "L Energy Potion", null, character, gameplay, amount, price, 30);
        SpriteSheet energyPotionSheet = new SpriteSheet();
        energyPotionSheet.add("assets/res/item/l_energy_potion.png");
        animation = new PotionAnimation(0, energyPotionSheet, -1);
    }

    @Override
    public Item clone() {
        return new LargeEnergyPotion(id, character, gameplay, amount, price);
    }

}
