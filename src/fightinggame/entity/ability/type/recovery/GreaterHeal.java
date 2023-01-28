package fightinggame.entity.ability.type.recovery;

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

    @Override
    public boolean healing(Character character) {
        if(super.healing(character)) {
            gameplay.getAudioPlayer().startThread("heal_sound", false, gameplay.getOptionHandler().getOptionMenu().getSfxVolume());
            return true;
        }
        return false;
    }
    
    
    
}
