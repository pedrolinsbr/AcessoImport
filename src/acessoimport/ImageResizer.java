package acessoimport;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;


// Resizes all images in a folder to another folder
// IMPORTANT: Cannot have same file name with different extensions in the source folder.
public class ImageResizer {

    /*public static void main(String[] args) throws Exception {
        ImageResizer resizer = new ImageResizer();
        ImageOutputConfig config = new ImageOutputConfig();

        // Quality hints to Graphics2D
        RenderingHints hints = new RenderingHints(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        config.hints = hints;
        config.formatName = "jpg"; // use jpg, bmp, gif etc.
        config.resizePercentage = 20; // Resize to 50%

        String sourceDir = "/Acesso-source-images/";
        String destDir = "/Acesso-dest-images/";

        resizer.resizeAllImages(sourceDir, destDir, config);
    }*/
    // Java program to resize all images in the specified folder
    // Ignores all hidden files.
    // IMPORTANT: If source folder contains identical file names with different extensions only one appear in output
    public void resizeAllImages(String sourceDirName, String destDirName, ImageOutputConfig config) throws Exception {
        File sourceDir = new File(sourceDirName);
        File destFolder = new File(destDirName);
        destFolder.mkdir(); // create destination if missing
        File[] sourceNames = sourceDir.listFiles();

        BufferedImage sourceImage;
        BufferedImage outputImage;
        File newFile;
        for (int i = 0; i < sourceNames.length; i++) {
            if (sourceNames[i].isFile() && !sourceNames[i].isHidden()) {
                sourceImage = ImageIO.read(new File(sourceDirName + sourceNames[i].getName()));
                outputImage = resizeImage(sourceImage, config);

                newFile = new File(destDirName + removeExtension(sourceNames[i]) + "." + config.formatName);
                ImageIO.write(outputImage, config.formatName, newFile);
            }
        }
    }

    // Resizes the image using the configuration passed in
    private BufferedImage resizeImage(BufferedImage source, ImageOutputConfig config) {
        int newHeight = (int) (source.getHeight() * config.resizePercentage / 100);
        int newWidth = (int) (source.getWidth() * config.resizePercentage / 100);
        
        // PNG supports transparency
        int type = config.formatName.equals("png")?BufferedImage.TYPE_INT_ARGB:BufferedImage.TYPE_INT_RGB;
        
        BufferedImage outputImage = new BufferedImage(newWidth, newHeight, type);
        Graphics2D graphics2D = outputImage.createGraphics();
        if (config.hints != null) {
            graphics2D.setRenderingHints(config.hints);
        }
        graphics2D.drawImage(source, 0, 0, newWidth, newHeight, null);
        graphics2D.dispose();
        return outputImage;
    }

    // Removes file extension. The new extension depends on format name
    private String removeExtension(File file) {
        String name = file.getName();
        int pos = name.lastIndexOf(".");
        if (pos > 0) {
            name = name.substring(0, pos);
        }
        return name;
    }

}

// Wrapper for image ouput parameters
class ImageOutputConfig {
    String formatName;
    double resizePercentage;
    RenderingHints hints;
}
