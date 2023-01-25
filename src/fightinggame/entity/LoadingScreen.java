package fightinggame.entity;

import fightinggame.Game;
import fightinggame.animation.BackgroundAnimation;
import fightinggame.entity.state.GameState;
import fightinggame.resource.DataManager;
import java.awt.Color;
import java.awt.Graphics;

public class LoadingScreen {

    private BackgroundAnimation backgroundAnimation;
    private ProgressBar progressBar;
    private GamePosition position;
    private int loadingCounter = 0;
    private int loadingLimit = 180;
    private int percent = 0;
    private boolean finish;

    public LoadingScreen(BackgroundAnimation backgroundAnimation, ProgressBar progressBar,
            GamePosition position) {
        this.backgroundAnimation = backgroundAnimation;
        this.progressBar = progressBar;
        this.position = position;
    }

    public void tick() {
        if (Game.STATE == GameState.LOADING_STATE) {
            if (loadingCounter <= loadingLimit) {
                loadingCounter++;
            } else {
                if (!progressBar.isEmpty()) {
                    progressBar.next();
                    loadingCounter = 0;
                    if(!finish) {
                        if(percent < 100) {
                            percent += 5;
                        }
                    }
                }
            }
        }
    }

    public void render(Graphics g) {
        if (Game.STATE == GameState.LOADING_STATE) {
            if (backgroundAnimation != null) {
                backgroundAnimation.render(g, position.getXPosition(), position.getYPosition(), position.getWidth(), position.getHeight());
            }
            if (progressBar != null) {
                progressBar.render(g, position.getXPosition() + 30,
                        position.getMaxY() - 90,
                        position.getWidth() - 60, 50);
            }
            g.setFont(DataManager.getFont(30f));
            g.setColor(Color.white);
            g.drawString(percent + "%", position.getMaxX() - 100,
                    position.getMaxY() - 10);
        }
    }

    public BackgroundAnimation getBackgroundAnimation() {
        return backgroundAnimation;
    }

    public void resetLoading() {
        percent = 0;
        loadingCounter = 0;
        finish = false;
        progressBar.setDefaultIndex(0);
    }

    public void setBackgroundAnimation(BackgroundAnimation backgroundAnimation) {
        this.backgroundAnimation = backgroundAnimation;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public GamePosition getPosition() {
        return position;
    }

    public void setPosition(GamePosition position) {
        this.position = position;
    }

    public int getLoadingCounter() {
        return loadingCounter;
    }

    public void setLoadingCounter(int loadingCounter) {
        this.loadingCounter = loadingCounter;
    }

    public int getLoadingLimit() {
        return loadingLimit;
    }

    public void setLoadingLimit(int loadingLimit) {
        this.loadingLimit = loadingLimit;
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        percent = 100;
        this.finish = finish;
    }
    
}
