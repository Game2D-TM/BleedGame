package fightinggame.animation.trap;

import fightinggame.entity.Animation;
import fightinggame.entity.SpriteSheet;

public class TrapAnimation extends Animation{
    
    public TrapAnimation(int id, SpriteSheet sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public TrapAnimation(int id, SpriteSheet sheet) {
        super(id, sheet);
    }
    
}
