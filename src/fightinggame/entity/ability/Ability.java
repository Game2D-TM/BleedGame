package fightinggame.entity.ability;

import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.input.handler.GameHandler;
import fightinggame.entity.SpriteSheet;
import fightinggame.entity.Character;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public abstract class Ability {

    protected int id;
    protected String name;
    protected int energyLost = 0;
    protected long resetTime = 0;
    protected long coolDown = 0;
    protected long resetTimeCounter = 0;
    protected boolean canUse = true;
    protected SpriteSheet skillIcon;
    protected Animation animationLTR;
    protected Animation animationRTL;
    protected Animation currAnimation;
    protected List<GameHandler> handlers = new ArrayList<>();
    protected Gameplay gameplay;
    protected Character character;
    protected boolean isLTR;

    public Ability(int id, String name, long resetTime, int energyLost, SpriteSheet skillIcon,
            Animation animationLTR, Animation animationRTL, Gameplay gameplay, Character character) {
        this.id = id;
        this.name = name;
        this.resetTime = resetTime;
        this.energyLost = energyLost;
        this.skillIcon = skillIcon;
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

    public Ability(int id, String name, long resetTime, int energyLost, SpriteSheet skillIcon,
            Animation currAnimation, Gameplay gameplay, Character character) {
        this.id = id;
        this.name = name;
        this.resetTime = resetTime;
        this.energyLost = energyLost;
        this.currAnimation = currAnimation;
        this.skillIcon = skillIcon;
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
            if (resetTimeCounter < resetTime) {
                resetTimeCounter++;
            } else {
                canUse = true;
                resetTimeCounter = 0;
                coolDown = resetTime;
            }
        }
        if (currAnimation != null) {
            currAnimation.tick();
        }
    }

    public void render(Graphics g) {
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

    public int getEnergyLost() {
        return energyLost;
    }

    public void setEnergyLost(int energyLost) {
        this.energyLost = energyLost;
    }

    public SpriteSheet getSkillIcon() {
        return skillIcon;
    }

    public void setSkillIcon(SpriteSheet skillIcon) {
        this.skillIcon = skillIcon;
    }
}
