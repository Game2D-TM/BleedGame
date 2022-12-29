package fightinggame.animation.player;

import fightinggame.entity.Animation;
import fightinggame.resource.SpriteSheet;

public class PlayerSlideLTR extends Animation{
    
    public PlayerSlideLTR(int id, SpriteSheet sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public PlayerSlideLTR(int id, SpriteSheet sheet) {
        super(id, sheet);
    }
    
}
