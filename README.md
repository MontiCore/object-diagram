<!-- (c) https://github.com/MontiCore/monticore -->

This documentation is intended for  **modelers** who use the object diagram (OD) languages. A
detailed documentation for **language engineers** using or extending the OD language is
located **[here](src/main/grammars/de/monticore/OD4Report.md)**. We recommend that **language
engineers** read this documentation before reading the detailed documentation.

# An Example Model

<img width="700" src="doc/pics/OD_Example.png" alt="The graphical syntax of an example OD" style="float: left; margin-right: 10px;">
<br><b>Figure 1:</b> The graphical syntax of an example OD.

&nbsp;

Figure 1 depicts the OD ```MyFamily``` in graphical syntax. In textual syntax, the OD is defined as
follows:

``` 
objectdiagram MyFamily {
  alice:Person {
    age = 29;
    cars = [
      :BMW {
        bought = 2020-01-05 15:30:00
        color = BLUE;
      },
      tiger:Jaguar {
        bought = 2000/01/05 15:00:00
        color = RED;
        length = 5.3;
      }
    ];
  };
  bob:Person {
    nicknames = ["Bob", "Bobby", "Robert"];
    cars -> tiger;
  };
  link married alice <-> bob;
}
```

This was for us the most intuitive textual representation of ODs, which follows the syntax of class
diagrams.

# Command Line Interface (CLI)

This section describes the CLI tool of the OD language. The CLI tool provides typical functionality
used when processing models. To this effect, it provides funcionality for

* parsing,
* coco-checking,
* pretty-printing,
* creating symbol tables,
* storing symbols in symbol files,
* ,and loading symbols from symbol files.

The requirements for building and using the OD CLI tool are that Java 8, Git, and Gradle are
installed and available for use in Bash.

The following subsection describes how to download the CLI tool. Then, this document describes how
to build the CLI tool from the source files. Afterwards, this document contains a tutorial for using
the CLI tool.

## Downloading the Latest Version of the CLI Tool

A ready to use version of the CLI tool can be downloaded in the form of an executable JAR file. You
can use [**this download link**](http://monticore.de/download/OD4ReportCLI.jar) // TODO for
downloading the CLI tool.

Alternatively, you can download the CLI tool using `wget`. The following command downloads the
latest version of the CLI tool and saves it under the name `OD4ReportCLI`
in your working directory:
// TODO

```
wget "http://monticore.de/download/OD4ReportCLI.jar" -O OD4ReportCLI.jar
``` 

## Building the CLI Tool from the Sources

It is possible to build an executable JAR of the CLI tool from the source files located in GitHub.
The following describes the process for building the CLI tool from the source files using Bash. For
building an executable Jar of the CLI with Bash from the source files available in GitHub, execute
the following commands.

First, clone the repository:

```
git clone https://github.com/MontiCore/object-diagram.git
```

Change the directory to the root directory of the cloned sources:

```
cd object-diagram
```

Afterwards, build the source files with gradle (if `./gradlew.bat` is not recognized as a command in
your shell, then use `./gradlew`):

```
./gradlew.bat build
```

Congratulations! You can now find the executable JAR file `OD4ReportCLI.jar` in the
directory `target/libs` (accessible via `cd target/libs`).

## Tutorial: Getting Started Using the OD CLI Tool

The previous sections describe how to obtain an executable JAR file
(OD CLI tool). This section provides a tutorial for using the OD CLI tool. The following examples
assume that you locally named the CLI tool `OD4ReportCLI`.

### First Steps

Executing the Jar file without any options prints usage information of the CLI tool to the console:

```
java -jar OD4ReportCLI.jar
usage: OD4ReportCLI
 -c,--coco <arg>            Checks the CoCos for the input ODs. Possible
                            arguments are 'intra',  'inter', and 'type'. When
                            given the argument 'intra', only the intra-model
                            CoCos are checked. When given the argument 'inter',
                            only the intra- and inter-model CoCos are checked.
                            When given the argument 'type', all CoCos are
                            checked. When no argument is specified, all CoCos
                            are checked by default.
 -h,--help                  Prints this help dialog.
 -i,--input <arg>           Processes the OD input artifacts specified as
                            arguments. At least one input OD is mandatory.
 -path <arg>                Sets the artifact path for imported symbols, space
                            separated.
 -pp,--prettyprint <file>   Prints the input ODs to stdout or to the specified
                            files (optional).
 -s,--symboltable <arg>     Stores the serialized symbol tables of the input ODs
                            in the specified files. The n-th input OD is stored
                            in the file as specified by the n-th argument. If no
                            arguments are given, the serialized symbol tables
                            are stored in
                            'target/symbols/{packageName}/{artifactName}.odsym'
                            by default.
```

To work properly, the CLI tool needs the mandatory argument `-i,--input <arg>`, which takes the file
paths of at least one input file containing OD models. If no other arguments are specified, the CLI
tool solely parses the model(s).

For trying this out, copy the `OD4ReportCLI.jar` into a directory of your choice. Afterwards, create
a text file containing the following simple OD:

```
objectdiagram Example {
}
```

Save the text file as `Example.od` in the directory where `OD4ReportCLI.jar` is located.

Now execute the following command:

```
java -jar OD4ReportCLI.jar -i Example.od
```

You may notice that the CLI tool prints no output to the console. This means that the tool has
parsed the file `Example.od` successfully.

### Step 2: Pretty-Printing

The CLI tool provides a pretty-printer for the OD language. A pretty-printer can be used, e.g., to
fix the formatting of files containing ODs. To execute the pretty-printer, the `-pp,--prettyprint`
option can be used. Using the option without any arguments pretty-prints the models contained in the
input files to the console.

Execute the following command for trying this out:

```
java -jar OD4ReportCLI.jar -i Example.od -pp
```

The command prints the pretty-printed model contained in the input file to the console:

```
objectdiagram Example {
}
```

It is possible to pretty-print the models contained in the input files to output files. For this
task, it is possible to provide the names of output files as arguments to the `-pp,--prettyprint`
option. If arguments for output files are provided, then the number of output files must be equal to
the number of input files. The i-th input file is pretty-printed into the i-th output file.

Execute the following command for trying this out:

```
java -jar OD4ReportCLI.jar -i Example.od -pp PPExample.od
```

The command prints the pretty-printed model contained in the input file into the file `PPExample.od`
.

### Step 3: Storing Symbols

The previous section describes how to load symbols from an existing symbol file. Now, we will use
the CLI tool to store a symbol file for our `MyFamily.od` model. The stored symbol file will contain
information about the objects defined in the OD. It can be imported by other models for using the
symbols introduced by these object definitions, similar to how we changed the file `MyFamily.od` for
importing the symbols contained in the symbol file `MyFamily.cdsym`.

Using the `-s,-symboltable <arg>` option builds the symbol tables of the input models and stores
them in the file paths given as arguments. Either no file paths must be provided or exactly one file
path has to be provided for each input model. The symbol file for the i-th input model is stored in
the file defined by the i-th file path. If you do not provide any file paths, the CLI tool stores
the symbol table of each input model in the symbol
file `target/symbols/{packageName}/{fileName}.odsym`
where `packageName` is the name of the package as specified in the file containing the model
and `fileName` is the name of the file containing the model. The file is stored relative to the
working directory, i.e., the directory in which you execute the command for storing the symbol
files. Furthermore, please notice that in order to store the symbols properly, the model has to be
well-formed in all regards, and therefore all context conditions are checked beforehand.

For storing the symbol file of `MyObject.od`, execute the following command
(the implicit context condition checks require using the model path option):

```
java -jar OD4ReportCLI.jar -i MyFamily.od -path cd -s
```

The CLI tool produces the file `target/symbols/MyFamily.odsym`, which can now be imported by other
models, e.g., by models that need to use some of the objects defined in the OD `MyFamily`.

For storing the symbol file of `MyFamily.od` in the file `syms/MyFamily.odsym`, for example, execute
the following command
(again, the implicit context condition checks require using the model path option):

```
java -jar OD4ReportCLI.jar -i MyFamily.od -path cd -s syms/MyFamily.odsym
```

Congratulations, you have just finished the tutorial about saving OD symbol files and are pretty
much down reading this README!

## Further Information

* [Project root: MontiCore @github](https://github.com/MontiCore/monticore)
* [MontiCore documentation](http://www.monticore.de/)
* [**List of languages**](https://github.com/MontiCore/monticore/blob/dev/docs/Languages.md)
* [**MontiCore Core Grammar
  Library**](https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/Grammars.md)
* [Best Practices](https://github.com/MontiCore/monticore/blob/dev/docs/BestPractices.md)
* [Publications about MBSE and MontiCore](https://www.se-rwth.de/publications/)
* [Licence definition](https://github.com/MontiCore/monticore/blob/master/00.org/Licenses/LICENSE-MONTICORE-3-LEVEL.md)
