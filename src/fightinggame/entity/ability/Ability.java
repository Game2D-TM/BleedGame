package fightinggame.entity.ability;

import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.entity.GamePosition;
import fightinggame.input.handler.GameHandler;
import fightinggame.entity.SpriteSheet;
import fightinggame.entity.Character;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public abstract class Ability {

    protected int id;
    protected String name;
    protected long resetTime = 0;
    protected long coolDown = 0;
    protected long resetTimeCounter = 0;
    protected boolean canUse = true;
    protected SpriteSheet skillIcon;
    protected Animation animationLTR;
    protected Animation animationRTL;
    protected Animation currAnimation;
    protected BufferedImage border;
    protected GamePosition position;
    protected List<GameHandler> handlers = new ArrayList<>();
    protected Gameplay gameplay;
    protected Character character;
    protected boolean isLTR;

    public Ability(int id, String name, long resetTime, SpriteSheet skillIcon, GamePosition position,
            Animation animationLTR, Animation animationRTL, Gameplay gameplay, Character character) {
        this.id = id;
        this.name = name;
        this.resetTime = resetTime;
        this.skillIcon = skillIcon;
        this.position = position;
        this.animationLTR = animationLTR;
        this.animationRTL = animationRTL;
        this.gameplay = gameplay;
        this.character = character;
        if (character != null) {
            if (character.isLTR()) {
                currAnimation = animationLTR;
            } else {
                currAnimation = animationRTL;
            }
        }
    }

    public Ability(int id, String name, long resetTime, SpriteSheet skillIcon, GamePosition position,
            Animation animationLTR, Animation animationRTL, BufferedImage border, Gameplay gameplay, Character character) {
        this.id = id;
        this.name = name;
        this.resetTime = resetTime;
        this.skillIcon = skillIcon;
        this.animationLTR = animationLTR;
        this.animationRTL = animationRTL;
        this.position = position;
        this.border = border;
        this.gameplay = gameplay;
        this.character = character;
        if (character != null) {
            if (character.isLTR()) {
                currAnimation = animationLTR;
            } else {
                currAnimation = animationRTL;
            }
        }
    }

    public Ability(int id, String name, long resetTime, Animation currAnimation, GamePosition position, Gameplay gameplay, Character character) {
        this.id = id;
        this.name = name;
        this.resetTime = resetTime;
        this.currAnimation = currAnimation;
        this.position = position;
        this.gameplay = gameplay;
        this.character = character;
    }

    public long getCoolDownTime() {
        return coolDown = (resetTime - resetTimeCounter) / 100;
    }

    public void tick() {
        if (handlers.size() > 0) {
            for (int i = 0; i < handlers.size(); i++) {
                handlers.get(i).tick();
            }
        }
        if (!canUse) {
            resetTimeCounter++;
        }
        if (resetTimeCounter > resetTime) {
            canUse = true;
            resetTimeCounter = 0;
            coolDown = resetTime;
        }
        if (currAnimation != null) {
            currAnimation.tick();
        }
    }

    public void render(Graphics g) {
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
        //

    }

    public abstract boolean execute();

    public abstract boolean execute(Character character);

    public abstract boolean execute(List<Character> characters);

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getResetTime() {
        return resetTime;
    }

    public void setResetTime(long resetTime) {
        this.resetTime = resetTime;
    }

    public SpriteSheet getSheet() {
        return skillIcon;
    }

    public void setSheet(SpriteSheet sheet) {
        this.skillIcon = sheet;
    }

    public long getSkillCounter() {
        return resetTimeCounter;
    }

    public void setSkillCounter(long skillCounter) {
        this.resetTimeCounter = skillCounter;
    }

    public boolean isCanUse() {
        return canUse;
    }

    public void setCanUse(boolean canUse) {
        this.canUse = canUse;
    }

    public GamePosition getPosition() {
        return position;
    }

    public void setPosition(GamePosition position) {
        this.position = position;
    }

    public List<GameHandler> getHandlers() {
        return handlers;
    }

    public void setHandlers(List<GameHandler> handlers) {
        this.handlers = handlers;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

}
