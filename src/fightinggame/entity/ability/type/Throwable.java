package fightinggame.entity.ability.type;

import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.entity.GamePosition;
import fightinggame.entity.SpriteSheet;
import fightinggame.entity.Character;
import fightinggame.entity.ability.type.skill.ActiveSkill;
import java.awt.image.BufferedImage;
public abstract class Throwable extends ActiveSkill {

    protected int attackDamage;
    protected GamePosition spawnPosition;
    protected GamePosition endPosition;
    protected boolean isThrow;
    protected int speed;
    protected long existTime;
    protected int throwCounter = 0;
    
    public Throwable(int attackDamage, int speed, int id, String name, long resetTime, int energyLost, 
            SpriteSheet skillIcon, Animation animationLTR, Animation animationRTL
            , GamePosition position, BufferedImage border, Gameplay gameplay, Character character) {
        super(border, position, id, name, resetTime, energyLost, skillIcon, animationLTR, animationRTL, gameplay, character);
        this.attackDamage = attackDamage;
        this.speed = speed;
    }
    
    public Throwable(int attackDamage, int speed, int id, String name, long resetTime, int energyLost, 
            SpriteSheet skillIcon, Animation animationLTR, Animation animationRTL, Gameplay gameplay, Character character) {
        super(id, name, resetTime, energyLost, skillIcon, animationLTR, animationRTL, gameplay, character);
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
