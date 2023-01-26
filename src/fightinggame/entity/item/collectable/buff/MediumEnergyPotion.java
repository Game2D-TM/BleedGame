package fightinggame.entity.item.collectable.buff;

import fightinggame.Gameplay;
import fightinggame.animation.item.PotionAnimation;
import fightinggame.entity.Character;
import fightinggame.entity.SpriteSheet;
import fightinggame.entity.item.Item;

public class MediumEnergyPotion extends EnergyPotion{

    public MediumEnergyPotion(int id, Character character, Gameplay gameplay, int amount) {
        super(id, "M Energy Potion", null, character, gameplay, amount, 20);
        SpriteSheet energyPotionSheet = new SpriteSheet();
        energyPotionSheet.add("assets/res/item/Potions/m_energy_potion.png");
        animation = new PotionAnimation(0, energyPotionSheet, -1);
    }

    public MediumEnergyPotion(int id, Character character, Gameplay gameplay, int amount, int price) {
        super(id, "M Energy Potion", null, character, gameplay, amount, price, 20);
        SpriteSheet energyPotionSheet = new SpriteSheet();
        energyPotionSheet.add("assets/res/item/Potions/m_energy_potion.png");
        animation = new PotionAnimation(0, energyPotionSheet, -1);
    }


    @Override
    public Item clone() {
        return new MediumEnergyPotion(id, character, gameplay, amount, price);
    }
    
}
