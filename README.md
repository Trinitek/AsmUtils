AsmUtils
========

A compilation of data compressors and converters to aid in the development of 16-bit assembly programs.

To run these JAR files, make sure Java is in your system PATH variable, and insert `java -jar` before the JAR filename and its required arguments.

**`16color256index.jar <filename>`**

> Compresses an image with no more than 16 colors, where pixel data is stored in the high and low nibbles of each bit

> `<filename>` - image file to convert

> Outputs two files: a file containing an RGB palette index, and a file containing the compressed image