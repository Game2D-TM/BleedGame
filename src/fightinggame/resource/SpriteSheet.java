package fightinggame.resource;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class SpriteSheet {
    private BufferedImage sprite;
    private List<BufferedImage> images;
    
    public SpriteSheet() {
        images = new ArrayList<>();
    }
    
    public SpriteSheet(BufferedImage sprite, int cutX, int cutY, int cutWidth, int cutHeight
            , int x, int y, int width, int height, int imageNum) {
        this.sprite = sprite;
        getImages(cutX, cutY, cutWidth, cutHeight, x, y, width, height, imageNum);
    }
    
    public List<BufferedImage> getImages(int cutX, int cutY, int cutWidth, int cutHeight,
            int x, int y, int width, int height, int imageNum) {
        images = new ArrayList<>();
        int sheetXPos = cutX;
        for(int i = 0 ; i < imageNum; i++) {
            BufferedImage imageHaveBlank = sprite.getSubimage(sheetXPos, cutY, cutWidth, cutHeight);
            images.add(imageHaveBlank.getSubimage(x, y, width, height));
            sheetXPos += cutWidth;
        }
        return images;
    }

    public List<BufferedImage> getImages() {
        return images;
    }

    public void setImages(List<BufferedImage> images) {
        this.images = images;
    }
    
    public BufferedImage getImage(int index) {
        if (images == null || images.size() == 0) return null;
        if (index > images.size() - 1) return null;
        return images.get(index);
    }
}
