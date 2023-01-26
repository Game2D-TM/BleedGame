package fightinggame.entity.item.collectable.buff;

import fightinggame.Gameplay;
import fightinggame.animation.item.PotionAnimation;
import fightinggame.entity.Character;
import fightinggame.entity.SpriteSheet;
import fightinggame.entity.item.Item;

public class SmallEnergyPotion extends EnergyPotion {

    public SmallEnergyPotion(int id, Character character, Gameplay gameplay, int amount) {
        super(id, "S Energy Potion", null, character, gameplay, amount, 10);
        SpriteSheet energyPotionSheet = new SpriteSheet();
        energyPotionSheet.add("assets/res/item/Potions/s_energy_potion.png");
        animation = new PotionAnimation(0, energyPotionSheet, -1);
    }

    public SmallEnergyPotion(int id, Character character, Gameplay gameplay, int amount, int price) {
        super(id, "S Energy Potion", null, character, gameplay, amount, price, 10);
        SpriteSheet energyPotionSheet = new SpriteSheet();
        energyPotionSheet.add("assets/res/item/Potions/s_energy_potion.png");
        animation = new PotionAnimation(0, energyPotionSheet, -1);
    }

    @Override
    public Item clone() {
        return new SmallEnergyPotion(id, character, gameplay, amount);
    }

}
