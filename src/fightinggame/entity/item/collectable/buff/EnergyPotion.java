package fightinggame.entity.item.collectable.buff;

import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.entity.Character;
import fightinggame.entity.ability.Ability;
import fightinggame.entity.ability.type.EnergyRecovery;
import fightinggame.entity.ability.type.healing.PotionEnergyRecovery;
import fightinggame.entity.inventory.Inventory;
import fightinggame.entity.item.collectable.CollectableItem;

public abstract class EnergyPotion extends CollectableItem {

    protected int recoverPoint;

    public EnergyPotion(int id, String name,
            Animation animation,
            Character character, Gameplay gameplay, int amount, int recoverPoint) {
        super(id, name, animation, character, gameplay, amount);
        this.recoverPoint = recoverPoint;
        EnergyRecovery energyRecovery = new PotionEnergyRecovery(recoverPoint, 0, 1000,
                null, null, null, null, gameplay, character);
        abilities.add(energyRecovery);
        description = "[" + name + "] Recover " + recoverPoint + " MP.";
    }

    public EnergyPotion(int id, String name, Animation animation, Character character,
            Gameplay gameplay, int amount, int price, int recoverPoint) {
        super(id, name, animation, character, gameplay, amount, price);
        this.recoverPoint = recoverPoint;
        EnergyRecovery energyRecovery = new PotionEnergyRecovery(recoverPoint, 0, 1000,
                null, null, null, null, gameplay, character);
        abilities.add(energyRecovery);
        description = "[" + name + "] Recover " + recoverPoint + " MP.";
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
        if(abilities.size() <= 0) {
            return false;
        }
        boolean result = false;
        for(int i = 0 ; i < abilities.size() ; i++) {
            Ability ability = abilities.get(i);
            if(ability != null) {
                boolean temp = ability.execute();
                if(temp) {
                    result = true;
                }
            }
        }
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
    public int getYHitBox() {
        return position.getYPosition();
    }

    public int getRecoverPoint() {
        return recoverPoint;
    }

    public void setRecoverPoint(int recoverPoint) {
        this.recoverPoint = recoverPoint;
    }

}
