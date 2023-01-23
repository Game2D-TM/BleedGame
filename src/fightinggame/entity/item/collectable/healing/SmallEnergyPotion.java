package fightinggame.entity.item.collectable.healing;

import fightinggame.Gameplay;
import fightinggame.animation.item.PotionAnimation;
import fightinggame.entity.Character;
import fightinggame.entity.SpriteSheet;
import fightinggame.entity.ability.type.EnergyRecovery;
import fightinggame.entity.ability.type.healing.PotionEnergyRecovery;
import fightinggame.entity.inventory.Inventory;
import fightinggame.entity.item.Item;
import fightinggame.entity.item.collectable.CollectableItem;

public class SmallEnergyPotion extends CollectableItem {

    public SmallEnergyPotion(int id, Character character, Gameplay gameplay, int amount) {
        super(id, "S Energy Potion", null, character, gameplay, amount);
        SpriteSheet energyPotionSheet = new SpriteSheet();
        energyPotionSheet.add("assets/res/item/s_energy_potion.png");
        animation = new PotionAnimation(0, energyPotionSheet, -1);
        EnergyRecovery energyRecovery = new PotionEnergyRecovery(10, 0, 1000,
                null, null, null, null, gameplay, character);
        abilities.add(energyRecovery);
        description = "[S Energy Potion] Recover 10 MP.";
    }

    public SmallEnergyPotion(int id, Character character, Gameplay gameplay, int amount, int price) {
        super(id, "S Energy Potion", null, character, gameplay, amount, price);
        SpriteSheet energyPotionSheet = new SpriteSheet();
        energyPotionSheet.add("assets/res/item/s_energy_potion.png");
        animation = new PotionAnimation(0, energyPotionSheet, -1);
        EnergyRecovery energyRecovery = new PotionEnergyRecovery(10, 0, 1000,
                null, null, null, null, gameplay, character);
        abilities.add(energyRecovery);
        description = "[S Energy Potion] Recover 10 MP.";
    }

    @Override
    public boolean checkHit(Character character) {
        boolean result = super.checkHit(character);
        if (result) {
            gameplay.getAudioPlayer().startThread("health_pickup", false, gameplay.getOptionHandler().getOptionMenu().getSfxVolume());
            return true;
        }
        return false;
    }

    @Override
    public boolean use() {
        EnergyRecovery energyRecovery = (EnergyRecovery) abilities.get(0);
        if (energyRecovery == null) {
            return false;
        }
        boolean result = energyRecovery.execute();
        if (result) {
            if (amount > 0) {
                int nAmount = amount - 1;
                System.out.println("Used 1" + name
                        + " " + nAmount + " Left"
                        + " Wait for " + abilities.get(0).getCoolDownTime());
                if (nAmount == 0) {
                    Inventory inventory = character.getInventory();
                    inventory.removeItemFromInventory(this);
                } else {
                    amount = nAmount;
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public Item clone() {
        return new SmallEnergyPotion(id, character, gameplay, amount);
    }

    @Override
    public int getXHitBox() {
        return position.getXPosition() + 20;
    }

    @Override
    public int getWidthHitBox() {
        return position.getWidth() - 40;
    }

    @Override
    public int getHeightHitBox() {
        return position.getHeight();
    }

    @Override
    public int getXMaxHitBox() {
        return getXHitBox() + getWidthHitBox();
    }

    @Override
    public int getYHitBox() {
        return position.getYPosition();
    }

    @Override
    public int getYMaxHitBox() {
        return getYHitBox() + getHeightHitBox();
    }

}
