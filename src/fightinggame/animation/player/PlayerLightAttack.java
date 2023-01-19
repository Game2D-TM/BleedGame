package fightinggame.animation.player;

import fightinggame.entity.Animation;
import fightinggame.entity.SpriteSheet;

public class PlayerLightAttack extends Animation{
    
    public PlayerLightAttack(int id, SpriteSheet sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public PlayerLightAttack(int id, SpriteSheet sheet) {
        super(id, sheet);
    }
    
    
}
