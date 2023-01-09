package fightinggame.entity;

import fightinggame.Gameplay;
import java.awt.Color;
import java.awt.Graphics;

public class TransitionScreen {

    private int transitionCounter = 0;
    private boolean transition;
    private boolean backward;
    private int timeCounter = 0;
    private int timeLimit = 5;
    private final Gameplay gameplay;

    public TransitionScreen(Gameplay gameplay) {
        this.gameplay = gameplay;
    }

    public void tick() {
        if (transition) {
            timeCounter++;
            if (timeCounter > timeLimit) {
                if (!backward) {
                    if (transitionCounter <= 50) {
                        transitionCounter++;
                    } else {
                        transition = false;
                    }
                } else {
                    if (transitionCounter > 0) {
                        transitionCounter--;
                    } else {
                        transition = false;
                    }
                }
                timeCounter = 0;
            }
        }
    }

    public void render(Graphics g) {
        if (transition) {
            g.setColor(new Color(0, 0, 0, transitionCounter * 5));
            g.fillRect(0, 0,
                    gameplay.getCamera().getPosition().getWidth(),
                    gameplay.getCamera().getPosition().getHeight());
        }
    }

    public boolean isTransition() {
        return transition;
    }

    public void startTransitionForward() {
        transition = true;
        transitionCounter = 0;
        backward = false;
    }
    
    public void startTransitionBackward() {
        transition = true;
        transitionCounter = 50;
        backward = true;
    }

    public boolean isBackward() {
        return backward;
    }

}
