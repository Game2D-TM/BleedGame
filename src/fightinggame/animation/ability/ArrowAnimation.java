package fightinggame.animation.ability;

import fightinggame.entity.Animation;
import fightinggame.entity.SpriteSheet;

public class ArrowAnimation extends Animation{
    
    public ArrowAnimation(int id, SpriteSheet sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public ArrowAnimation(int id, SpriteSheet sheet) {
        super(id, sheet);
    }
    
}
