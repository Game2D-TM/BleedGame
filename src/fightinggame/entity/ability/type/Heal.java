package fightinggame.entity.ability.type;

import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.entity.ability.Ability;
import fightinggame.entity.Character;
import fightinggame.entity.GamePosition;
import fightinggame.resource.SpriteSheet;
import java.awt.image.BufferedImage;

public abstract class Heal extends Ability {

    protected int healPoint;

    public Heal(int healPoint, int id, String name, long resetTime, 
            SpriteSheet skillIcon, GamePosition position, Animation animation, Gameplay gameplay, Character character) {
        super(id, name, resetTime, skillIcon, position, animation, gameplay, character);
        this.healPoint = healPoint;
    }

    public Heal(int healPoint, int id, String name, long resetTime, SpriteSheet skillIcon, 
            GamePosition position, Animation animation, BufferedImage border, Gameplay gameplay, Character character) {
        super(id, name, resetTime, skillIcon, position, animation, border, gameplay, character);
        this.healPoint = healPoint;
    }

    public abstract void healing(Character character);

    public int getHealPoint() {
        return healPoint;
    }

    public void setHealPoint(int healPoint) {
        this.healPoint = healPoint;
    }
}
