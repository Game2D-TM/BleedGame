package fightinggame.animation.player;

import fightinggame.entity.Animation;
import fightinggame.entity.SpriteSheet;

public class PlayerCrouch extends Animation{
    
    public PlayerCrouch(int id, SpriteSheet sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public PlayerCrouch(int id, SpriteSheet sheet) {
        super(id, sheet);
    }
    
    
    
}
