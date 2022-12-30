package fightinggame.animation.player;

import fightinggame.entity.Animation;
import fightinggame.resource.SpriteSheet;

public class PlayerSlide_RTL extends Animation{
    
    public PlayerSlide_RTL(int id, SpriteSheet sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }
    
    public PlayerSlide_RTL(int id, SpriteSheet sheet) {
        super(id, sheet);
    }
}
