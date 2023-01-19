package fightinggame.entity.ability.type.healing;

import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.entity.Character;
import fightinggame.entity.GamePosition;
import fightinggame.entity.Entity;
import fightinggame.entity.ability.type.Heal;
import java.awt.image.BufferedImage;
import java.util.List;

public class PotionHeal extends Heal{

    public PotionHeal(int healPoint, int id, long resetTime
            , Entity skillIcon, Animation currAnimation
            , GamePosition position, BufferedImage border, Gameplay gameplay, Character character) {
        super(healPoint, id, "Potion Heal", resetTime, 0, skillIcon, currAnimation, position, border, gameplay, character);
    }
    
    
    @Override
    public boolean execute(List<Character> characters) {
        return false;
    }

    @Override
    public boolean execute(Character character) {
        return healing(character);
    }

    @Override
    public boolean execute() {
        return healing();
    }
}
