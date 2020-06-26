package acessoimport;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.File;
import javax.imageio.ImageIO;


public class cutImage {

    public static void main(String[] args, String sourceDir) throws Exception {
        cutImage resizer = new cutImage();
        ImageOutputConfig config = new ImageOutputConfig();

        // Quality hints to Graphics2D
        RenderingHints hints;
        hints = new RenderingHints(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        config.hints = hints;
        config.formatName = "jpg";              // use jpg, bmp, gif etc.
        config.resizePercentage = 100;          // Resize to 50%
        String destDir = "/Acesso-dest-images/";
        resizer.cutAllImages(sourceDir, destDir, config);
    }
    
    public void cutAllImages(String sourceDirName, String destDirName, ImageOutputConfig config) throws Exception {
        File sourceDir = new File(sourceDirName);
        File destFolder = new File(destDirName);
        //destFolder.mkdir();                                     // create destination if missing
        File[] sourceNames = sourceDir.listFiles();

        BufferedImage sourceImage;
        BufferedImage outputImage;
        File newFile;
        
        for (int i = 0; i < sourceNames.length; i++) {
            if (sourceNames[i].isFile() && !sourceNames[i].isHidden()) {
                sourceImage = ImageIO.read(new File(sourceDirName + '\\' +sourceNames[i].getName()));
                outputImage = resizeImage(sourceImage, config);

                newFile = new File(destDirName + removeExtension(sourceNames[i]) + "." + config.formatName);
                try{
                    ImageIO.write(outputImage, config.formatName, newFile);
                }catch (IllegalArgumentException ex){
                    System.out.println("Erro na imagem "+ex.toString());
                }
            }
        }
    }
    private String removeExtension(File file) {
        String name = file.getName();
        int pos = name.lastIndexOf(".");
        if (pos > 0) {
            name = name.substring(0, pos);
        }
        return name;
    }
    private BufferedImage resizeImage(BufferedImage source, ImageOutputConfig config) {
              
        int newHeight = (int) (source.getHeight() * config.resizePercentage / 100);
        int newWidth = (int)  (source.getWidth() * config.resizePercentage / 100);
        
        // PNG supports transparency
        int type = config.formatName.equals("png")?BufferedImage.TYPE_INT_ARGB:BufferedImage.TYPE_INT_RGB;
        
        BufferedImage outputImage = new BufferedImage(newWidth, newHeight, type);
        
        Graphics2D graphics2D = outputImage.createGraphics();
        if (config.hints != null) {
            graphics2D.setRenderingHints(config.hints);
        }
        graphics2D.drawImage(source, 0, 0, null);
        try{
            BufferedImage imgl  = outputImage.getSubimage(120, 20, 350, 400);
            return imgl;
        }catch (RasterFormatException ex){
            System.out.println(("Erro na imagem "+ex.toString()));
            return source;
        }
    }
    
}