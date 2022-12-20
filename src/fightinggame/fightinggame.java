package fightinggame;

import java.awt.Color;
import java.awt.Graphics;

public class fightinggame {

    public static void main(String[] args) {
        Game game = new Game(1650, 1000);
        Graphics g = game.getGraphics();
        game.init();
        long now;
        long updateTime;
        long wait;

        long lastFpsCheck = 0;
        int currentFps = 0;
        int totalFrames = 0;

        final int TARGET_FPS = Game.FPS;
        final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
        while (game.isRunning()) {
            now = System.nanoTime();
            totalFrames++;
            if (System.nanoTime() > lastFpsCheck + 1000000000) {
                lastFpsCheck = System.nanoTime();
                currentFps = totalFrames;
                totalFrames = 0;
//                System.out.println("Current Fps: " + currentFps);
            }
            game.revalidate();
            g.setColor(Color.white);
            g.fillRect(0, 0, game.getWidth(), game.getHeight());

            try {
                game.tick();
                game.render(g);
            } catch (Exception ex) {
                System.out.println(ex.toString());
            }
            updateTime = System.nanoTime() - now;
            wait = (OPTIMAL_TIME - updateTime) / 1000000;
            try {
                Thread.sleep(wait);
            } catch (Exception e) {
//                System.out.println(e.toString());
            }
        }

    }
}
