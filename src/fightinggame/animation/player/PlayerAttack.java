package fightinggame.animation.player;

import fightinggame.entity.Animation;
import fightinggame.entity.CharacterState;
import fightinggame.resource.SpriteSheet;

public class PlayerAttack extends Animation{
    
    public PlayerAttack(int id, CharacterState state, SpriteSheet sheet) {
        super(id, state, sheet);
        tickToExecute = 0;
    }
    
}
