package fightinggame.entity.ability.type.harm;

import fightinggame.Gameplay;
import fightinggame.entity.Character;
import fightinggame.entity.GamePosition;
import fightinggame.entity.SpriteSheet;
import fightinggame.entity.StatusBar;
import fightinggame.entity.ability.type.Harm;
import java.awt.Graphics;

public abstract class BadStatusOvertime extends Harm {

    protected Character effectCharacter;
    protected long timeLimit;
    protected long timeCounter = 0;
    protected int timeUseCounter = 0;
    protected int timeUseLimit;
    protected boolean isUse;

    public BadStatusOvertime(int timeLimit, int timeUseLimit, int damage, int id, String name, SpriteSheet skillIcon, Gameplay gameplay, Character character) {
        super(damage, id, name, skillIcon, gameplay, character);
        this.timeLimit = timeLimit;
        this.timeUseLimit = timeUseLimit;
        timeCounter = timeLimit;
    }

    @Override
    public void tick() {
        super.tick();
        if (effectCharacter != null) {
            if (isUse && !effectCharacter.isDeath()) {
                if (timeCounter > 0) {
                    timeCounter--;
                    if (timeUseCounter <= timeUseLimit) {
                        timeUseCounter++;
                    } else {
                        badStatusEffect(effectCharacter);
                        timeUseCounter = 0;
                    }
                } else {
                    isUse = false;
                    timeCounter = timeLimit;
                    timeUseCounter = 0;
                    effectCharacter.getAbilities().remove(this);
                }
            }
        }
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        if (effectCharacter != null) {
            if (isUse && !effectCharacter.isDeath()) {
                StatusBar statusBar = effectCharacter.getStatusBar();
                if (statusBar != null) {
                    GamePosition position = new GamePosition(statusBar.getStatusBarPos().getMaxX() - 120,
                            statusBar.getStatusBarPos().getMaxY() + 90, 30, 30);
                    if (skillIcon != null) {
                        skillIcon.render(g, position.getXPosition(), position.getYPosition(),
                                position.getWidth(), position.getHeight());
                    }
                }
            }
        }
    }

    public abstract void badStatusEffect(Character character);

    @Override
    public boolean execute(Character character) {
        if (character == null) {
            return false;
        }
        this.effectCharacter = character;
        character.getAbilities().add(this);
        isUse = true;
        return true;
    }

    @Override
    public boolean execute() {
        return execute(effectCharacter);
    }

    public Character getEffectCharacter() {
        return effectCharacter;
    }

    public void setEffectCharacter(Character effectCharacter) {
        this.effectCharacter = effectCharacter;
    }

}
