package fightinggame.animation.player;

import fightinggame.entity.Animation;
import fightinggame.entity.CharacterState;
import fightinggame.resource.SpriteSheet;


public class PlayerHit extends Animation{
    
    public PlayerHit(int id, CharacterState state, SpriteSheet sheet) {
        super(id, state, sheet);
        tickToExecute = 25;
    }
    
}
