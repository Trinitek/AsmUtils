import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
        //System.out.println(args[0]);
        //System.out.println(baseFilename);

        ArrayList<Byte> outputData = new ArrayList<Byte>();
        ArrayList<Byte> uncompressedData = new ArrayList<Byte>();
        ArrayList<Byte> outputPalette = new ArrayList<Byte>();
        ArrayList<Color> uncompressedPalette = new ArrayList<Color>();
        Color pixel;

        for (int y = 0; y < sourceImage.getHeight(); y++)
            for (int x = 0; x < sourceImage.getWidth(); x++) {
                // Add color to the uncompressed palette
                // If the color is a duplicate to one that already exists, do not add it
                pixel = new Color(sourceImage.getRGB(x, y));
                if (!uncompressedPalette.contains(pixel)) {
                    uncompressedPalette.add(pixel);

                    // Add color to the output palette
                    outputPalette.add((byte) pixel.getRed());
                    outputPalette.add((byte) pixel.getGreen());
                    outputPalette.add((byte) pixel.getBlue());
                }

                // Add the index number of the palette entry to the compressed data array
                uncompressedData.add((byte) uncompressedPalette.indexOf(pixel));
            }

        /*for (Color color : uncompressedPalette) {
            System.out.println(color.toString());
        }*/

        /*System.out.println();

        int rgbCounter = 1;
        for (Byte rgbValue : outputPalette) {
            if (rgbCounter == 1) {
                System.out.print(" R=" + String.format("%x", rgbValue));
                rgbCounter++;
            } else if (rgbCounter == 2) {
                System.out.print(" G=" + String.format("%x", rgbValue));
                rgbCounter++;
            } else {
                System.out.println(" B=" + String.format("%x", rgbValue));
                rgbCounter = 1;
            }
        }

        for (int y = 0; y < sourceImage.getHeight(); y++) {
            for (int x = 0; x < sourceImage.getWidth(); x++) {
                System.out.print(String.format("%x", uncompressedData.get(y * sourceImage.getHeight() + x)));
            }
            System.out.println();
        }*/

        byte compressedByte;

        for (int i = 0; i < uncompressedData.size(); i += 2) {
            //noinspection RedundantCast
            compressedByte = (byte) (uncompressedData.get(i) << 4);
            compressedByte += uncompressedData.get(i + 1);
            outputData.add(compressedByte);
        }

        /*System.out.println();

        for (int y = 0; y < sourceImage.getHeight() / 2; y++) {
            for (int x = 0; x < sourceImage.getWidth() / 2; x++) {
                System.out.print(String.format("%x", outputData.get(y * (sourceImage.getHeight() / 2) + x)));
            }
            System.out.println();
        }*/
    }
}
