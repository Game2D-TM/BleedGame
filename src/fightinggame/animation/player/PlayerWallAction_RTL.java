package fightinggame.animation.player;

import fightinggame.entity.Animation;
import fightinggame.entity.SpriteSheet;

public class PlayerWallAction_RTL extends Animation{
    
    public PlayerWallAction_RTL(int id, SpriteSheet sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public PlayerWallAction_RTL(int id, SpriteSheet sheet) {
        super(id, sheet);
    }
    
    
}
