# Data backup
_V1.0-SNAPSHOT: 2022-03-02_

A small application to save a folder recursively into a zip file.
It reads the input folder's and the output file's path from command line argument.
You can add relative or absolute path.
It filters files in the folder:
- didn't save hidden files
- only saves files with specific file name content (.gpkg)

## TODO

- implementation of test cases
- read paths from a properties file
- logback implementation
- documentation
- dynamic filters