package fightinggame.entity.ability.type;

import fightinggame.Gameplay;
import fightinggame.entity.Character;
import fightinggame.entity.GamePosition;
import fightinggame.entity.SpriteSheet;
import fightinggame.entity.ability.type.skill.PassiveSkill;

public abstract class IncreaseStat extends PassiveSkill {
    
    protected boolean isIncrease;
    protected double amountIncrease;
    
    public IncreaseStat(int id, String name, int amountIncrease,
            SpriteSheet skillIcon, GamePosition position, 
            Gameplay gameplay, Character character) {
        super(id, name, -1, 0, skillIcon, null, gameplay, character);
        this.amountIncrease = amountIncrease;
    }

    public double getAmountIncrease() {
        return amountIncrease;
    }

    public void setAmountIncrease(double amountIncrease) {
        this.amountIncrease = amountIncrease;
    }
    
    public boolean isIncrease() {
        return isIncrease;
    }

    public void setIsIncrease(boolean isIncrease) {
        this.isIncrease = isIncrease;
    }
    
}
