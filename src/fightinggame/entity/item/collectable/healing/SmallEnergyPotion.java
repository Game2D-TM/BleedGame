package fightinggame.entity.item.collectable.healing;

import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.entity.Character;
import fightinggame.entity.ability.type.EnergyRecovery;
import fightinggame.entity.inventory.Inventory;
import fightinggame.entity.item.Item;
import fightinggame.entity.item.collectable.CollectableItem;

public class SmallEnergyPotion extends CollectableItem {

    public SmallEnergyPotion(int id
            , Animation animation, Character character, Gameplay gameplay, int amount) {
        super(id, "S Energy Potion", animation, character, gameplay, amount);
    }
    
    @Override
    public boolean checkHit(Character character) {
        boolean result = super.checkHit(character);
        if(result) {
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
        return new SmallEnergyPotion(id, animation, character, gameplay, amount);
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
