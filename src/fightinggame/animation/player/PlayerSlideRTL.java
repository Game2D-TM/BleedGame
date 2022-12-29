package fightinggame.animation.player;

import fightinggame.entity.Animation;
import fightinggame.resource.SpriteSheet;

public class PlayerSlideRTL extends Animation{
    
    public PlayerSlideRTL(int id, SpriteSheet sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }
    
    public PlayerSlideRTL(int id, SpriteSheet sheet) {
        super(id, sheet);
    }
}
