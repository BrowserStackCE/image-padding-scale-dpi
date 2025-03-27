import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ImageSuperimposer {

    public static void main(String[] args) {
        try {

            String inputDir= "src/main/resources/images/input";
            String outputDir= "src/main/resources/images/output";

            java.util.List<File> files = getAllFilesInDirectory(inputDir);

            if (!files.isEmpty()) {
                System.out.println("Files in directory '" + inputDir + "':");
                for (File file : files) {
                    System.out.println(file.getName());
                    String imageFilePath = inputDir+File.separator+file.getName(); // Replace with the actual path to your image
                    String outputFilePath = outputDir+File.separator+file.getName();

                    ImageSuperimposer imageProcessor = new ImageSuperimposer();
                    imageProcessor.processImage(imageFilePath,outputFilePath);

                }
            } else {
                System.out.println("No files found in directory '" + inputDir + "'.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static java.util.List<File> getAllFilesInDirectory(String directoryPath) {
        java.util.List<File> fileList = new ArrayList<>();
        File directory = new File(directoryPath);

        // Check if the provided path is actually a directory
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    // Check if the item in the directory is a file (not another directory)
                    if (file.isFile()) {
                        fileList.add(file);
                    }
                }
            } else {
                System.err.println("Error: Could not list files in directory: " + directoryPath);
            }
        } else {
            System.err.println("Error: Path is not a valid directory: " + directoryPath);
        }

        return fileList;
    }

    public void processImage(String imageFilePath, String outputFilePath)
    {
        try {

            // --- Configuration ---
            int backgroundWidthMM = 105;
            int backgroundHeightMM = 148;
            int imageWidthMM = 115;
            int imageHeightMM = 66;
            int xCoordinateMM = -12;
            int yCoordinateMM = 40;

            // --- Conversion from mm to pixels (assuming 72 DPI) ---
            double mmToPixels = 522.0 / 25.4;
            int backgroundWidthPixels = (int) Math.round(backgroundWidthMM * mmToPixels);
            int backgroundHeightPixels = (int) Math.round(backgroundHeightMM * mmToPixels);
            int imageWidthPixels = (int) Math.round(imageWidthMM * mmToPixels);
            int imageHeightPixels = (int) Math.round(imageHeightMM * mmToPixels);
            int xCoordinatePixels = (int) Math.round(xCoordinateMM * mmToPixels);
            int yCoordinatePixels = (int) Math.round(yCoordinateMM * mmToPixels);

            // --- Create White Background ---
            BufferedImage backgroundImage = new BufferedImage(backgroundWidthPixels, backgroundHeightPixels, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = backgroundImage.createGraphics();
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, backgroundWidthPixels, backgroundHeightPixels);
            g2d.dispose();


            // --- Load Image to Superimpose ---
            File imageFile = new File(imageFilePath);
            BufferedImage originalImage = ImageIO.read(imageFile);

            // --- Superimpose Image onto Background with Rendering Hints ---
            Graphics2D backgroundGraphics = backgroundImage.createGraphics();

            // Enable Anti-aliasing for smoother edges
            backgroundGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Enable higher quality interpolation for smoother scaling
            backgroundGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            // Alternatively, you can try:
            // backgroundGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            double scaleX = (double) imageWidthPixels / originalImage.getWidth();
            double scaleY = (double) imageHeightPixels / originalImage.getHeight();
            AffineTransform transform = new AffineTransform();
            transform.translate(xCoordinatePixels, yCoordinatePixels);
            transform.scale(scaleX, scaleY);
            backgroundGraphics.drawImage(originalImage, transform, null);
            //backgroundGraphics.drawImage(originalImage, xCoordinatePixels, yCoordinatePixels, imageWidthPixels, imageHeightPixels, null);
            backgroundGraphics.dispose();

            // --- Save the Resulting Image ---
            File outputFile = new File(outputFilePath);
            ImageIO.write(backgroundImage, "png", outputFile);

            System.out.println("Image superimposed successfully! Output saved to: " + outputFilePath);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}