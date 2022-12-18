package fightinggame.resource;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class ImageManager {
    public static BufferedImage loadImage(String fileName) {
        try {
            if(!fileName.contains(".png") && !fileName.contains(".jpg")) return null;
            return ImageIO.read(new File(fileName));
        } catch (IOException ex) {
            Logger.getLogger(ImageManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    public static BufferedImage loadImage(File file) {
        try {
            if(!file.getName().contains(".png") && !file.getName().contains(".jpg")) return null;
            return ImageIO.read(file);
        } catch (IOException ex) {
            Logger.getLogger(ImageManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static Map<String, BufferedImage> loadImagesFromFolderToMap(String folderName) {
        File file = new File(folderName);
        if(file.exists()) {
            Map<String, BufferedImage> images = new HashMap<>();
            File[] imageFiles = file.listFiles();
            if(imageFiles != null && imageFiles.length > 0) {
                for(File imageFile : imageFiles) {
                    String fileName = imageFile.getName();
                    if(fileName.contains(".png")) fileName = fileName.replace(".png", "");
                    if(fileName.contains(".jpg")) fileName = fileName.replace(".jpg", "");
                    BufferedImage image = loadImage(imageFile);
                    if(image != null) images.put(fileName, image);
                }
            }
            return images;
        }
        return null;
    }
    
    public static List<BufferedImage> loadImagesFromFolderToList(String folderName) {
        File file = new File(folderName);
        if(file.exists()) {
            List<BufferedImage> images = new ArrayList<>();
            File[] imageFiles = file.listFiles();
            if(imageFiles != null && imageFiles.length > 0) {
                for(File imageFile : imageFiles) {
                    String fileName = imageFile.getName();
                    if(fileName.contains(".png")) fileName = fileName.replace(".png", "");
                    if(fileName.contains(".jpg")) fileName = fileName.replace(".jpg", "");
                    BufferedImage image = loadImage(imageFile);
                    if(image != null) images.add(image);
                }
            }
            return images;
        }
        return null;
    }
    public static List<BufferedImage> loadImagesWithCutFromFolderToList(String folderName
            , int x, int y, int width, int height) {
        File file = new File(folderName);
        if(file.exists()) {
            List<BufferedImage> images = new ArrayList<>();
            File[] imageFiles = file.listFiles();
            if(imageFiles != null && imageFiles.length > 0) {
                for(File imageFile : imageFiles) {
                    String fileName = imageFile.getName();
                    if(fileName.contains(".png")) fileName = fileName.replace(".png", "");
                    if(fileName.contains(".jpg")) fileName = fileName.replace(".jpg", "");
                    BufferedImage image = loadImage(imageFile);
                    if(image != null) images.add(image.getSubimage(x, y, width, height));
                }
            }
            return images;
        }
        return null;
    }
}
