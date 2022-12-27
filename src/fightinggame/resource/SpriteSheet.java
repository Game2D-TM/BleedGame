package fightinggame.resource;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SpriteSheet {

    private BufferedImage sprite;
    private List<BufferedImage> images;
    private int spriteCounter = 0;
    private int spriteIndex = 0;

    public SpriteSheet() {
        images = new ArrayList<>();
    }

    public SpriteSheet(BufferedImage sprite, int cutX, int cutY, int cutWidth, int cutHeight,
            int x, int y, int width, int height, int imageNum) {
        this.sprite = sprite;
        getImages(cutX, cutY, cutWidth, cutHeight, x, y, width, height, imageNum);
    }

    public void tick(int tickToExecute) {
        if (tickToExecute < 0) {
            return;
        }
        spriteCounter++;
        if (spriteCounter > tickToExecute) {
            if (spriteIndex < images.size() - 1) {
                spriteIndex++;
            } else {
                spriteIndex = 0;
            }
            spriteCounter = 0;
        }
    }

    public void render(Graphics g, int x, int y, int width, int height) {
        Image image = images.get(spriteIndex);
        if (image != null) {
            g.drawImage(image, x, y, width, height, null);
            g.setColor(Color.red);
            //rectangle
            g.drawRect(x, y, width, height);
        }
    }

    public List<BufferedImage> getImages(int cutX, int cutY, int cutWidth, int cutHeight,
            int x, int y, int width, int height, int imageNum) {
        images = new ArrayList<>();
        int sheetXPos = cutX;
        for (int i = 0; i < imageNum; i++) {
            BufferedImage imageHaveBlank = sprite.getSubimage(sheetXPos, cutY, cutWidth, cutHeight);
            images.add(imageHaveBlank.getSubimage(x, y, width, height));
            sheetXPos += cutWidth;
        }
        return images;
    }

    public void reverseImages() {
        Collections.reverse(images);
    }

    public boolean add(String filePath) {
        try {
            if (images == null) {
                images = new ArrayList<>();
            }
            images.add(ImageManager.loadImage(filePath));
            return true;
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return false;
    }

    public List<BufferedImage> getImages() {
        return images;
    }

    public void setImages(List<BufferedImage> images) {
        this.images = images;
    }

    public BufferedImage getImage(int index) {
        if (images == null || images.size() == 0) {
            return null;
        }
        if (index > images.size() - 1) {
            return null;
        }
        return images.get(index);
    }

    public int getSpriteCounter() {
        return spriteCounter;
    }

    public void setSpriteCounter(int spriteCounter) {
        this.spriteCounter = spriteCounter;
    }

    public int getSpriteIndex() {
        return spriteIndex;
    }

    public void setSpriteIndex(int spriteIndex) {
        this.spriteIndex = spriteIndex;
    }

}
