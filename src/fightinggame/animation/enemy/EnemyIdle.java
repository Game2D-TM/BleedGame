package fightinggame.animation.enemy;

import fightinggame.animation.character.CharacterIdle;
import fightinggame.entity.Entity;

public class EnemyIdle extends CharacterIdle{
    
    public EnemyIdle(int id, Entity sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public EnemyIdle(int id, Entity sheet) {
        super(id, sheet);
    }
    
}
