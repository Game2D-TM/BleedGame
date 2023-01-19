package fightinggame.entity.ability.type.healing;

import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.entity.GamePosition;
import fightinggame.entity.ability.type.Heal;
import fightinggame.entity.Character;
import fightinggame.entity.SpriteSheet;
import java.awt.image.BufferedImage;
public class GreaterHeal extends Heal {

    public GreaterHeal(int healPoint, int id, long resetTime, int energyLost, SpriteSheet skillIcon,
            Animation currAnimation, GamePosition position, BufferedImage border, Gameplay gameplay, Character character) {
        super(healPoint, id, "Greater Heal", resetTime, energyLost, skillIcon, currAnimation, position, border, gameplay, character);
    }

}
