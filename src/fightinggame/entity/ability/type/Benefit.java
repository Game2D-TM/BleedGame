package fightinggame.entity.ability.type;

import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.entity.Character;
import fightinggame.entity.GamePosition;
import fightinggame.entity.ability.Ability;
import fightinggame.resource.SpriteSheet;
import java.awt.image.BufferedImage;

public abstract class Benefit extends Ability {

    public Benefit(int id, String name, long resetTime, 
            SpriteSheet skillIcon, GamePosition position
            , Animation animationLTR, Animation animationRTL, Gameplay gameplay, Character character) {
        super(id, name, resetTime, skillIcon, position, animationLTR, animationRTL, gameplay, character);
    }

    public Benefit(int id, String name, long resetTime, SpriteSheet skillIcon, 
            GamePosition position, Animation animationLTR, Animation animationRTL
            , BufferedImage border, Gameplay gameplay, Character character) {
        super(id, name, resetTime, skillIcon, position, animationLTR, animationRTL, border, gameplay, character);
    }
    
    
    
}
