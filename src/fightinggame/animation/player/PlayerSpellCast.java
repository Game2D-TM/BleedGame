package fightinggame.animation.player;

import fightinggame.entity.Animation;
import fightinggame.resource.SpriteSheet;


public class PlayerSpellCast extends Animation{
    
    public PlayerSpellCast(int id, SpriteSheet sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public PlayerSpellCast(int id, SpriteSheet sheet) {
        super(id, sheet);
    }
   
    
}
