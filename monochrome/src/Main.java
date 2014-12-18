import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

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

        //ArrayList<Byte> outputData = new ArrayList<Byte>();
        //ArrayList<Byte> uncompressedData = new ArrayList<Byte>();
        //ArrayList<Byte> outputPalette = new ArrayList<Byte>();
        //ArrayList<Color> uncompressedPalette = new ArrayList<Color>();
        ArrayList<Boolean> uncompressedData = new ArrayList<Boolean>();
        Color pixel;

        for (int y = 0; y < sourceImage.getHeight(); y++)
            for (int x = 0; x < sourceImage.getWidth(); x++) {
                // If an RGB value has an average value that is gray or brighter, make that TRUE
                // Otherwise, make that FALSE
                pixel = new Color(sourceImage.getRGB(x, y));
                int averageColor = (pixel.getRed() + pixel.getGreen() + pixel.getBlue()) / 3;
                if (averageColor < 0) {
                    uncompressedData.add(true);
                } else uncompressedData.add(false);

                // Add the index number of the palette entry to the compressed data array
                uncompressedData.add((byte) uncompressedPalette.indexOf(pixel));
            }

        byte compressedByte;

        // Squeeze two pixels into one byte: leftmost pixel in the high nibble, rightmost in the low nibble
        for (int i = 0; i < uncompressedData.size(); i += 2) {
            //noinspection RedundantCast
            compressedByte = (byte) (uncompressedData.get(i) << 4);
            compressedByte += uncompressedData.get(i + 1);
            outputData.add(compressedByte);
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
