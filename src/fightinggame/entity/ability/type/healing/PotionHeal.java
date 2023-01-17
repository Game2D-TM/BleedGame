package fightinggame.entity.ability.type.healing;

import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.entity.Character;
import fightinggame.entity.GamePosition;
import fightinggame.entity.SpriteSheet;
import fightinggame.entity.ability.type.Heal;
import java.awt.image.BufferedImage;

public class PotionHeal extends Heal{

    public PotionHeal(int healPoint, int id, long resetTime
            , SpriteSheet skillIcon, Animation currAnimation
            , GamePosition position, BufferedImage border, Gameplay gameplay, Character character) {
        super(healPoint, id, "Potion Heal", resetTime, skillIcon, currAnimation, position, border, gameplay, character);
    }
    
}
