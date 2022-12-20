package fightinggame.entity.ability.type;

import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.entity.GamePosition;
import fightinggame.entity.ability.Ability;
import fightinggame.resource.SpriteSheet;
import java.awt.image.BufferedImage;
import fightinggame.entity.Character;
public abstract class Throwable extends Ability {

    protected int attackDamage;
    protected GamePosition spawnPosition;
    protected GamePosition endPosition;
    protected boolean isThrow;
    protected int speed;
    protected long existTime;
    protected int throwCounter = 0;
    
    public Throwable(int attackDamage, int speed, int id, String name, long resetTime, 
            SpriteSheet skillIcon, GamePosition position, Animation animation, Gameplay gameplay, Character character) {
        super(id, name, resetTime, skillIcon, position, animation, gameplay, character);
        this.attackDamage = attackDamage;
        this.speed = speed;
    }

    public Throwable(int attackDamage, int speed, int id, String name, long resetTime, 
            SpriteSheet skillIcon, GamePosition position, Animation animation, BufferedImage border, 
            Gameplay gameplay, Character character) {
        super(id, name, resetTime, skillIcon, position, animation, border, gameplay, character);
        this.attackDamage = attackDamage;
        this.speed = speed;
    }
    
    public abstract boolean execute(GamePosition spawnPosition, GamePosition endPosition);
}
