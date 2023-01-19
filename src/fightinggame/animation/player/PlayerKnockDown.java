package fightinggame.animation.player;

import fightinggame.entity.Animation;
import fightinggame.entity.SpriteSheet;

public class PlayerKnockDown extends Animation{
    
    public PlayerKnockDown(int id, SpriteSheet sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public PlayerKnockDown(int id, SpriteSheet sheet) {
        super(id, sheet);
    }
    
}
