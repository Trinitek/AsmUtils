import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Wrong number of arguments");
            return;
        }

        BufferedImage sourceImage;

        try {
            sourceImage = ImageIO.read(new File(args[0]));
        } catch (IOException e) {
            System.out.println("Cannot read '" + args[0] + "'");
            return;
        }

        String baseFilename = FilenameUtils.removeExtension(args[0]);

        ArrayList<Byte> outputData = new ArrayList<Byte>();
        ArrayList<Byte> outputPalette = new ArrayList<Byte>();
        ArrayList<Color> rawPalette = new ArrayList<Color>();
        Color pixel;

        for (int y = 0; y < sourceImage.getHeight(); y++)
            for (int x = 0; x < sourceImage.getWidth(); x++) {
                // Add color to the raw palette containing Color objects
                // If the color is a duplicate to one that already exists, do not add it
                pixel = new Color(sourceImage.getRGB(x, y));
                if (!rawPalette.contains(pixel)) {
                    rawPalette.add(pixel);

                    // Add color to the output palette
                    outputPalette.add((byte) pixel.getRed());
                    outputPalette.add((byte) pixel.getGreen());
                    outputPalette.add((byte) pixel.getBlue());
                }

                // Add the index number of the palette entry to the output image data array
                outputData.add((byte) rawPalette.indexOf(pixel));
            }

        // Create or open the file to which to write the palette data
        String paletteFilename = baseFilename + ".pal";
        DataOutputStream paletteFileStream;
        try {
            paletteFileStream = new DataOutputStream(new FileOutputStream(paletteFilename));
        } catch (FileNotFoundException fileNotFound) {
            System.out.println("Can't write to " + paletteFilename + "; can't open or create file");
            return;
        } catch (SecurityException security) {
            System.out.println("Can't write to " + paletteFilename + "; operating system security manager denies write permission");
            return;
        }

        // Create or open the file to which to write the compressed image data
        String imageFilename = baseFilename + ".pxl";
        DataOutputStream imageFileStream;
        try {
            imageFileStream = new DataOutputStream(new FileOutputStream(imageFilename));
        } catch (FileNotFoundException fileNotFound) {
            System.out.println("Can't write to " + imageFilename + "; can't open or create file");
            return;
        } catch (SecurityException security) {
            System.out.println("Can't write to " + imageFilename + "; operating system security manager denies write permission");
            return;
        }

        // Write palette data to file
        System.out.println("Writing palette data to " + paletteFilename + "...");
        try {
            for (Byte data : outputPalette) {
                paletteFileStream.writeByte(data);
            }
        } catch (IOException io) {
            System.out.println("An error occurred when writing to " + paletteFilename + " - " + io.getMessage());
            return;
        }

        // Write compressed image data to file
        System.out.println("Writing image data to " + imageFilename + "...");
        try {
            for (Byte data : outputData) {
                imageFileStream.writeByte(data);
            }
        } catch (IOException io) {
            System.out.println("An error occurred when writing to " + imageFilename + " - " + io.getMessage());
            return;
        }

        System.out.println("Operation completed successfully");
    }
}
