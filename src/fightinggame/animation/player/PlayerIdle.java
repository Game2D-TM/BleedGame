package fightinggame.animation.player;

import fightinggame.entity.Animation;
import fightinggame.entity.CharacterState;
import fightinggame.resource.SpriteSheet;

public class PlayerIdle extends Animation{
    
    public PlayerIdle(int id, CharacterState state, SpriteSheet sheet) {
        super(id, state, sheet);
    }
    
}
