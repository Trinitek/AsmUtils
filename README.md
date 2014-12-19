AsmUtils
========

A compilation of data compressors and converters to aid in the development of 16-bit assembly programs.

To run these JAR files, make sure Java is in your system PATH variable, and insert `java -jar` before the JAR filename and its required arguments.

#### Current Programs

`16color256index.jar <filename>`

> Compresses an image with no more than 16 colors, where pixel data is stored in the high and low nibbles of each bit

> `<filename>` - image file to convert

> Outputs two files: a file containing an RGB palette index, and a file containing the compressed image


`256color256index.jar <filename>`

> Extracts the pixel data and palette of an image with no more than 256 colors

> `<filename>` - image file to convert

> Outputs two files: a file containing at the most 256 RGB palette indexes, and a file containing the image data

`monochrome.jar <filename>`

> Compresses a black and white image into a string of compressed bits running left to right, top to bottom, with the most significant bit as the leading bit.

> `<filename>` - image file to convert

> Outputs one file: a file containing the compressed image
