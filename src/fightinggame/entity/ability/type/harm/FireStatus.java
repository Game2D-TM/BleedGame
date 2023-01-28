package fightinggame.entity.ability.type.harm;

import fightinggame.Gameplay;
import fightinggame.entity.Character;

public class FireStatus extends BadStatusOvertime {

    public FireStatus(int timeLimit, int timeUseLimit, int damage, int id, Gameplay gameplay, Character character) {
        super(timeLimit, timeUseLimit, damage, id, "Fire Status", null, gameplay, character);
    }

    @Override
    public void badStatusEffect(Character character) {
        if (damage > 0) {
            int nHealth = character.getStats().getHealth() - damage;
            character.addReceiveDamage(damage);
            if (nHealth > 0) {
                character.getStats().setHealth(nHealth);
            } else {
                character.setIsDeath(true);
            }
        }
    }

}
