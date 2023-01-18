package fightinggame.resource;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
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

    public static String EXTENSION_PNG = ".png";
    public static String EXTENSION_JPG = ".jpg";

    public static BufferedImage flipImage(BufferedImage imageFlip) {
        if (imageFlip == null) {
            return null;
        }
        BufferedImage sprite = imageFlip;
        // Flip the image horizontally
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
//            tx.translate(-sprite.getWidth(null), 0);
        tx.translate(-sprite.getWidth(null), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        sprite = op.filter(sprite, null);
        return sprite;
    }

    public static BufferedImage rotateImage(BufferedImage rotateImage, int radian) {
        if(rotateImage == null || radian <= 0) {
            return null;
        }
        BufferedImage sprite = rotateImage;
        // Rotate Image
        final double rads = Math.toRadians(radian);
        final double sin = Math.abs(Math.sin(rads));
        final double cos = Math.abs(Math.cos(rads));
        final int w = (int) Math.floor(sprite.getWidth() * cos + sprite.getHeight() * sin);
        final int h = (int) Math.floor(sprite.getHeight() * cos + sprite.getWidth() * sin);
        final BufferedImage rotatedImage = new BufferedImage(w, h, sprite.getType());
        final AffineTransform at = new AffineTransform();
        at.translate(w / 2, h / 2);
        at.rotate(rads, 0, 0);
        at.translate(-sprite.getWidth() / 2, -sprite.getHeight() / 2);
        final AffineTransformOp rotateOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        rotateOp.filter(sprite, rotatedImage);
        return rotatedImage;
    }

    public static boolean writeImage(BufferedImage image, String fileName) {
        if (!fileName.contains(".png") && !fileName.contains(".jpg")) {
            return false;
        }
        String extension = Utils.getFileExtension(fileName);
        File outputfile = new File(fileName);
        if (outputfile.exists()) {
            return false;
        }
        try {
            boolean result = outputfile.createNewFile();
            if (result) {
                return ImageIO.write(image, extension, outputfile);
            }
        } catch (IOException ex) {
            Logger.getLogger(ImageManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static boolean writeImages(List<BufferedImage> images, String folderPath,
            String defFileName, String extension) {
        if (images == null) {
            return false;
        }
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdir();
        }
        if (images.size() > 0) {
            int count = 0;
            for (int i = 0; i < images.size(); i++) {
                BufferedImage image = images.get(i);
                if (image == null) {
                    continue;
                }
                String fileName = folder.getAbsolutePath() + "/" + defFileName + "_" + count + extension;
                writeImage(image, fileName);
                count++;
            }
        }
        return false;
    }

    public static BufferedImage loadImage(String fileName) {
        try {
            if (!fileName.contains(".png") && !fileName.contains(".jpg")) {
                return null;
            }
            return ImageIO.read(new File(fileName));
        } catch (IOException ex) {
            Logger.getLogger(ImageManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static BufferedImage loadImage(File file) {
        try {
            if (!file.getName().contains(".png") && !file.getName().contains(".jpg")) {
                return null;
            }
            return ImageIO.read(file);
        } catch (IOException ex) {
            Logger.getLogger(ImageManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static Map<String, BufferedImage> loadImagesFromFolderToMap(String folderName) {
        try {
            File file = new File(folderName);
            if (file.exists()) {
                Map<String, BufferedImage> images = new HashMap<>();
                File[] imageFiles = file.listFiles();
                if (imageFiles != null && imageFiles.length > 0) {
                    for (File imageFile : imageFiles) {
                        if (imageFile == null) {
                            continue;
                        }
                        if (!imageFile.exists()) {
                            continue;
                        }
                        if (imageFile.isDirectory()) {
                            if (imageFile.getName().equals("Raw")) {
                                continue;
                            }
                            images.putAll(loadImagesFromFolderToMap(imageFile.getAbsolutePath()));
                        } else {
                            String fileName = imageFile.getName();
                            if (fileName.contains(".png")) {
                                fileName = fileName.replace(".png", "");
                            }
                            if (fileName.contains(".jpg")) {
                                fileName = fileName.replace(".jpg", "");
                            }
                            BufferedImage image = loadImage(imageFile);
                            if (image != null) {
                                images.put(fileName, image);
                            }
                        }
                    }
                }
                return images;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return null;
    }

    public static List<BufferedImage> loadImagesFromFoldersToList(String folderName) {
        try {
            File file = new File(folderName);
            if (file.exists()) {
                List<BufferedImage> images = new ArrayList<>();
                File[] imageFiles = file.listFiles();
                if (imageFiles != null && imageFiles.length > 0) {
                    for (File imageFile : imageFiles) {
                        if (imageFile == null) {
                            continue;
                        }
                        if (!imageFile.exists()) {
                            continue;
                        }
                        if (imageFile.isDirectory()) {
                            if (imageFile.getName().equals("Raw")) {
                                continue;
                            }
                            images.addAll(loadImagesFromFoldersToList(imageFile.getAbsolutePath()));
                        } else {
                            String fileName = imageFile.getName();
                            if (fileName.contains(".png")) {
                                fileName = fileName.replace(".png", "");
                            }
                            if (fileName.contains(".jpg")) {
                                fileName = fileName.replace(".jpg", "");
                            }
                            BufferedImage image = loadImage(imageFile);
                            if (image != null) {
                                images.add(image);
                            }
                        }
                    }
                }
                return images;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return null;
    }

    public static List<BufferedImage> loadImagesWithCutFromFolderToList(String folderName,
            int x, int y, int width, int height) {
        try {
            File file = new File(folderName);
            if (file.exists()) {
                List<BufferedImage> images = new ArrayList<>();
                File[] imageFiles = file.listFiles();
                if (imageFiles != null && imageFiles.length > 0) {
                    for (File imageFile : imageFiles) {
                        if (imageFile == null) {
                            continue;
                        }
                        if (!imageFile.exists()) {
                            continue;
                        }
                        if (imageFile.isDirectory()) {
                            if (imageFile.getName().equals("Raw")) {
                                continue;
                            }
                            images.addAll(loadImagesWithCutFromFolderToList(imageFile.getAbsolutePath(),
                                    x, y, width, height));
                        } else {
                            String fileName = imageFile.getName();
                            if (fileName.contains(".png")) {
                                fileName = fileName.replace(".png", "");
                            }
                            if (fileName.contains(".jpg")) {
                                fileName = fileName.replace(".jpg", "");
                            }
                            BufferedImage image = loadImage(imageFile);
                            if (image != null) {
                                images.add(image.getSubimage(x, y, width, height));
                            }
                        }
                    }
                }
                return images;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return null;
    }

}
