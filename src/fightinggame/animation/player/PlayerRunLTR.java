package fightinggame.animation.player;

import fightinggame.entity.Animation;
import fightinggame.resource.SpriteSheet;


public class PlayerRunLTR extends Animation{
    
    public PlayerRunLTR(int id, SpriteSheet sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public PlayerRunLTR(int id, SpriteSheet sheet) {
        super(id, sheet);
    }

    
}
