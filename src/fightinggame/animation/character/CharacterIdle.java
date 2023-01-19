package fightinggame.animation.character;

import fightinggame.entity.Animation;
import fightinggame.entity.SpriteSheet;

public abstract class CharacterIdle extends Animation{
    
    public CharacterIdle(int id, SpriteSheet sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public CharacterIdle(int id, SpriteSheet sheet) {
        super(id, sheet);
    }
    
}
