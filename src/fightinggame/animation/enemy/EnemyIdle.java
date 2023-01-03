package fightinggame.animation.enemy;

import fightinggame.animation.character.CharacterIdle;
import fightinggame.resource.SpriteSheet;

public class EnemyIdle extends CharacterIdle{
    
    public EnemyIdle(int id, SpriteSheet sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public EnemyIdle(int id, SpriteSheet sheet) {
        super(id, sheet);
    }
    
}
