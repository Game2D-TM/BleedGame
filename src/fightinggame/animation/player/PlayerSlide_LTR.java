package fightinggame.animation.player;

import fightinggame.entity.Animation;
import fightinggame.resource.SpriteSheet;

public class PlayerSlide_LTR extends Animation{
    
    public PlayerSlide_LTR(int id, SpriteSheet sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public PlayerSlide_LTR(int id, SpriteSheet sheet) {
        super(id, sheet);
    }
    
}
