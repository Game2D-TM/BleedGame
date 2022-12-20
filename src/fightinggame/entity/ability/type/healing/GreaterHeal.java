package fightinggame.entity.ability.type.healing;

import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.entity.GamePosition;
import fightinggame.entity.ability.type.Heal;
import fightinggame.resource.SpriteSheet;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import fightinggame.entity.Character;
public class GreaterHeal extends Heal {

    public GreaterHeal(int healPoint, int id, long resetTime, SpriteSheet skillIcon, 
            GamePosition position, Animation animation, Gameplay gameplay, Character character) {
        super(healPoint, id, "Greater Heal", resetTime, skillIcon, position, animation, gameplay, character);
    }

    public GreaterHeal(int healPoint, int id, long resetTime, SpriteSheet skillIcon, 
            GamePosition position, Animation animation, BufferedImage border, Gameplay gameplay,
            Character character) {
        super(healPoint, id, "Greater Heal", resetTime, skillIcon, position, animation, border, gameplay, character);
    }
    
    @Override
    public void healing(Character character) {
        if (canUse) {
            int maxHealth = character.getHealthBar().getMaxHealth();
            int health = character.getHealthBar().getHealth();
            if(health == maxHealth) return;
            int afterHeal = health + healPoint;
            if (afterHeal > maxHealth) {
                afterHeal = maxHealth;
            }
            character.getHealthBar().setHealth(afterHeal);
            canUse = false;
        }
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
    }

    public GamePosition getPosition() {
        return position;
    }

    public void setPosition(GamePosition position) {
        this.position = position;
    }

}
