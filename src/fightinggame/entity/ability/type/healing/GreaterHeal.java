package fightinggame.entity.ability.type.healing;

import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.entity.GamePosition;
import fightinggame.entity.ability.type.Heal;
import fightinggame.entity.SpriteSheet;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import fightinggame.entity.Character;
import java.util.List;
public class GreaterHeal extends Heal {

    public GreaterHeal(int healPoint, int id, long resetTime, SpriteSheet skillIcon, 
            GamePosition position, Animation animationLTR, Animation animationRTL
            , Gameplay gameplay, Character character) {
        super(healPoint, id, "Greater Heal", resetTime, skillIcon, position, animationLTR, animationRTL, gameplay, character);
    }

    public GreaterHeal(int healPoint, int id, long resetTime, SpriteSheet skillIcon, 
            GamePosition position, Animation animationLTR, Animation animationRTL
            , BufferedImage border, Gameplay gameplay, Character character) {
        super(healPoint, id, "Greater Heal", resetTime, skillIcon, position, animationLTR, animationRTL, border, gameplay, character);
    }
    
    @Override
    public boolean healing(Character character) {
        return super.healing(character);
    }
    
    @Override
    public boolean healing() {
        return super.healing();
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

    @Override
    public boolean execute() {
        return healing();
    }

    @Override
    public boolean execute(Character character) {
        return healing(character);
    }

    @Override
    public boolean execute(List<Character> characters) {
        return false;
    }

}
