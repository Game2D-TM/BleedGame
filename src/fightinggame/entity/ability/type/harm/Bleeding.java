package fightinggame.entity.ability.type.harm;

import fightinggame.Gameplay;
import fightinggame.entity.Character;
import fightinggame.entity.SpriteSheet;

public class Bleeding extends BadStatusOvertime {

    public Bleeding(int timeLimit, int timeUseLimit, int damage, int id, Gameplay gameplay, Character character) {
        super(timeLimit, timeUseLimit, damage, id, "Bleeding Status", null, gameplay, character);
        skillIcon = new SpriteSheet();
        skillIcon.add("assets/res/ability/bleeding.png");
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
