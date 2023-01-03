package fightinggame.animation.player;

import fightinggame.animation.character.CharacterIdle;
import fightinggame.resource.SpriteSheet;

public class PlayerIdle extends CharacterIdle{
    
    public PlayerIdle(int id, SpriteSheet sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public PlayerIdle(int id, SpriteSheet sheet) {
        super(id, sheet);
    }
    
    
}
