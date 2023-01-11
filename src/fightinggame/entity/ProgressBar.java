package fightinggame.entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

public class ProgressBar {

    private List<BufferedImage> images;
    private int currIndex = 0;

    public ProgressBar(List<BufferedImage> images) {
        this.images = images;
    }

    public void render(Graphics g, int x, int y, int width, int height) {
        if (images != null && images.size() > 0) {
            g.drawImage(images.get(currIndex), x, y, width, height, null);
        }
    }

    public BufferedImage next() {
        if (images != null && images.size() > 1) {
            if (currIndex < images.size() - 1) {
                currIndex++;
            }
            return images.get(currIndex);
        }
        return null;
    }

    public BufferedImage back() {
        if (images != null && images.size() > 1) {
            if (currIndex > 0) {
                currIndex--;
            }
            return images.get(currIndex);
        }
        return null;
    }

    public BufferedImage getImage(int index) {
        if (images == null || images.isEmpty()) {
            return null;
        }
        if (index > images.size() - 1) {
            return null;
        }
        return images.get(index);
    }

    public int getCurrIndex() {
        return currIndex;
    }

    public void setDefaultIndex(int index) {
        if (images == null || images.isEmpty()) {
            return;
        }
        if (index > images.size() - 1) {
            return;
        }
        currIndex = index;
    }
    
    public boolean isEmpty() {
        if (images == null || images.isEmpty()) {
            return true;
        }
        return false;
    }

}
