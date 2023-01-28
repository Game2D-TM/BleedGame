package fightinggame.entity.ability.type.skill;

import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.entity.Character;
import fightinggame.entity.GamePosition;
import fightinggame.entity.SpriteSheet;
import fightinggame.entity.ability.Ability;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public abstract class ActiveSkill extends Ability {

    protected BufferedImage border;
    protected GamePosition position;

    public ActiveSkill(BufferedImage border, GamePosition position, int id, String name, long resetTime, int energyLost, SpriteSheet skillIcon, Animation animationLTR, Animation animationRTL, Gameplay gameplay, Character character) {
        super(id, name, resetTime, energyLost, skillIcon, animationLTR, animationRTL, gameplay, character);
        this.border = border;
        this.position = position;
    }

    public ActiveSkill(BufferedImage border, GamePosition position, int id, String name, long resetTime, int energyLost, SpriteSheet skillIcon, Animation currAnimation, Gameplay gameplay, Character character) {
        super(id, name, resetTime, energyLost, skillIcon, currAnimation, gameplay, character);
        this.border = border;
        this.position = position;
    }

    public ActiveSkill(int id, String name, long resetTime, int energyLost, SpriteSheet skillIcon, Animation animationLTR, Animation animationRTL, Gameplay gameplay, Character character) {
        super(id, name, resetTime, energyLost, skillIcon, animationLTR, animationRTL, gameplay, character);
    }

    public ActiveSkill(int id, String name, long resetTime, int energyLost, SpriteSheet skillIcon, Animation currAnimation, Gameplay gameplay, Character character) {
        super(id, name, resetTime, energyLost, skillIcon, currAnimation, gameplay, character);
    }
    
    @Override
    public void render(Graphics g) {
        super.render(g);
        // skill icon
        if (border != null) {
            g.drawImage(border, position.getXPosition() - 5, position.getYPosition() - 5,
                    position.getWidth() + 10, position.getHeight() + 10, null);
        }
        if (skillIcon != null && position != null) {
            skillIcon.render(g, position.getXPosition(), position.getYPosition(), position.getWidth(), position.getHeight());
//          rectangle
//            g.setColor(Color.red);
//            g.drawRect(position.getXPosition(), position.getYPosition(), position.getWidth(), position.getHeight());
            g.setColor(Color.white);
            g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
            g.drawString(id + "", position.getXPosition() + 5, position.getMaxY() - 10);
            if (resetTimeCounter > 0) {
                g.drawString(getCoolDownTime() + "s", position.getXPosition() + position.getWidth() / 2 - 10,
                        position.getYPosition() + position.getHeight() / 2 + 5);
            }
        }
    }

    public BufferedImage getBorder() {
        return border;
    }

    public void setBorder(BufferedImage border) {
        this.border = border;
    }

    public GamePosition getPosition() {
        return position;
    }

    public void setPosition(GamePosition position) {
        this.position = position;
    }
    
    

}
