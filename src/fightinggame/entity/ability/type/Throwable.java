package fightinggame.entity.ability.type;

import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.entity.GamePosition;
import fightinggame.entity.ability.Ability;
import fightinggame.entity.SpriteSheet;
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
    
    public Throwable(int attackDamage, int speed, int id, String name, long resetTime, int energyLost, 
            SpriteSheet skillIcon, GamePosition position, Animation animationLTR, Animation animationRTL
            , Gameplay gameplay, Character character) {
        super(id, name, resetTime, energyLost, skillIcon, position, animationLTR, animationRTL, gameplay, character);
        this.attackDamage = attackDamage;
        this.speed = speed;
    }

    public Throwable(int attackDamage, int speed, int id, String name, long resetTime, int energyLost, 
            SpriteSheet skillIcon, GamePosition position, Animation animationLTR, Animation animationRTL
            , BufferedImage border, Gameplay gameplay, Character character) {
        super(id, name, resetTime, energyLost, skillIcon, position, animationLTR, animationRTL, border, gameplay, character);
        this.attackDamage = attackDamage;
        this.speed = speed;
    }
    
    public boolean execute(GamePosition spawnPosition, GamePosition endPosition) {
        if(this.character == null) {
            return false;
        }
        if(character.getStats().haveEnergy()) {
            return true;
        }
        return false;
    }
}
