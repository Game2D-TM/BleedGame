package fightinggame.animation.player;

import fightinggame.entity.Animation;
import fightinggame.entity.SpriteSheet;

public class PlayerRun_RTL extends Animation{
    
    public PlayerRun_RTL(int id, SpriteSheet sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }
    
    public PlayerRun_RTL(int id, SpriteSheet sheet) {
        super(id, sheet);
    }
    
}
