package fightinggame.animation.player;

import fightinggame.entity.Animation;
import fightinggame.entity.SpriteSheet;

public class PlayerJumpRoll_RTL extends Animation{
    
    public PlayerJumpRoll_RTL(int id, SpriteSheet sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public PlayerJumpRoll_RTL(int id, SpriteSheet sheet) {
        super(id, sheet);
    }

}
