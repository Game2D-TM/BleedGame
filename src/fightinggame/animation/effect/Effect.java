package fightinggame.animation.effect;

import fightinggame.entity.Animation;
import fightinggame.entity.SpriteSheet;
import java.awt.Graphics;

public abstract class Effect extends Animation {

    protected boolean active;
    protected int effectCounter = 0;
    protected int effectLimit;

    public Effect(int id, SpriteSheet sheet, int effectLimit) {
        super(id, sheet, 0);
        if (sheet.getImages().isEmpty()) {
            tickToExecute = -1;
        } else {
            tickToExecute = effectLimit / sheet.getImages().size();
        }
        this.effectLimit = effectLimit;
    }

    @Override
    public void tick() {
        if (active) {
            super.tick();
            if (effectCounter <= effectLimit) {
                effectCounter++;
            } else {
                active = false;
                effectCounter = 0;
            }
        }
    }

    @Override
    public void render(Graphics g, int x, int y, int width, int height) {
        if (active) {
            super.render(g, x, y, width, height);
        }
    }

    public void resetEffectCounter() {
        effectCounter = 0;
        active = false;
    }
    
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getEffectLimit() {
        return effectLimit;
    }

    public void setEffectLimit(int effectLimit) {
        this.effectLimit = effectLimit;
    }

}
