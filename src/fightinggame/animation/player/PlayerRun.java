package fightinggame.animation.player;

import fightinggame.entity.Animation;
import fightinggame.entity.CharacterState;
import fightinggame.resource.SpriteSheet;


public class PlayerRun extends Animation{
    
    public PlayerRun(int id, CharacterState state, SpriteSheet sheet) {
        super(id, state, sheet);
        tickToExecute = 0;
    }
    
    
    
}
