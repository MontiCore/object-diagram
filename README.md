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

# Command Line Tool 

This section describes the command line tool of the OD language. The tool provides typical functionality
used when processing models. To this effect, it provides funcionality for

* parsing,
* coco-checking,
* pretty-printing,
* creating symbol tables,
* storing symbols in symbol files,
* ,and loading symbols from symbol files.

The requirements for building and using the OD tool are that Java 11, Git, and Gradle are
installed and available for use in Bash.

The following subsection describes how to download the tool. Then, this document describes how
to build the tool from the source files. Afterward, this document contains a tutorial for using
the tool.

## Tool Set Up

This section explains how to set up the command line interface tools for the OD languages. Each tool
is contained in a separate jar file, which is produced as result of building the project with
gradle. The following explains this.

### Tool Download

* [**Download OD Language OD4Data**][od4data-link]
* [**Download OD Language OD4Report**][od4report-link]

Alternatively, the tools can be built from source code.

### Prerequisites

To build the project, it is required to install a Java 11 JDK and git.

#### Step 1: Clone Project with git

    git clone <link to this Git repository>
    cd od

#### Step 2: Build Project with gradle

    gradle build --refresh-dependencies

Afterward, the jars of the tools are available in `od/target/libs`.

## Tutorial: Getting Started Using the OD Tool

The previous sections describe how to obtain an executable JAR file
(OD command line tool). This section provides a tutorial for using the OD tool. The following examples
assume that you locally named the tool `MCOD4Report`. Note that after setting up the tool in the
previous step, you will also find a `MCOD4Data`. The following instruction also hold for this tool.

### Step 1: Laying the basis

Executing the Jar file without any options prints usage information of the tool to the console:

```
java -jar MCOD4Report.jar
usage: OD4ReportTool
 -c,--coco <arg>            Checks the CoCos for the input ODs. Possible
                            arguments are 'intra' and 'all'. When
                            given the argument 'intra', only the intra-model
                            CoCos are checked while when using 'all' all CoCos are
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

To work properly, the tool needs the mandatory argument `-i,--input <arg>`, which takes the file
paths of at least one input file containing OD models. If no other arguments are specified, the
tool solely parses the model(s).

For trying this out, copy the `MCOD4Report.jar` into a directory of your choice. Afterward, create
a text file containing the following simple OD:

```
objectdiagram Example {
    bob:Person {
      age = 42;
    };
    
    alice:Person {
      age =40;
    };
    
    link married bob <-> alice;
}
```

Save the text file as `Example.od` in the directory where `MCOD4Report.jar` is located.

Now execute the following command:

```
java -jar MCOD4Report.jar -i Example.od -c intra
```

You may notice that the tool prints no output to the console. This means that the tool has
parsed the file `Example.od` successfully. Don't mind die option `c` at this point as it
will be discussed later in step 3.

### Step 2: Pretty-Printing

The tool provides a pretty-printer for the OD language. A pretty-printer can be used, e.g., to
fix the formatting of files containing ODs. To execute the pretty-printer, the `-pp,--prettyprint`
option can be used. Using the option without any arguments pretty-prints the models contained in the
input files to the console.

Execute the following command for trying this out:

```
java -jar MCOD4Report.jar -i Example.od -pp -c intra
```

The command prints the pretty-printed model contained in the input file to the console:

```
objectdiagram Example {
    bob:Person {
      age = 42;
    };
    
    alice:Person {
      age =40;
    };
    
    link married bob <-> alice;
}
```

It is possible to pretty-print the models contained in the input files to output files. For this
task, it is possible to provide the names of output files as arguments to the `-pp,--prettyprint`
option. If arguments for output files are provided, then the number of output files must be equal to
the number of input files. The i-th input file is pretty-printed into the i-th output file.

Execute the following command for trying this out:

```
java -jar MCOD4Report.jar -i Example.od -pp PPExample.od -c intra
```

The command prints the pretty-printed model contained in the input file into the file `PPExample.od`

### Step 3: Checking Context Conditions

For checking context conditions, the `-c,--coco <arg>` option can be used. Using this option without
any arguments checks whether the model satisfies all context conditions.

If you are only interested in checking whether a model only satisfies a subset of the context
conditions or want to explicate that all context conditions should be checked, you can do this by
additionally providing one of the three arguments `intra` and `all`.

* Using the argument `intra` only executes context conditions concerning violations of intra-model
  context conditions. These context conditions, for example, check naming conventions.
* Using the argument `all` executes all context conditions. These context conditions include
  checking whether used types exist. The behavior when using the argument `all` is also the default
  behavior when using no arguments.

Execute the following command for trying out a simple example:

```
java -jar MCOD4Report.jar -i Example.od -c intra
```

You may notice that the tool prints nothing to the console when executing this command. This means
that the model satisfies all context conditions.

Let us now consider a more complex scenario. Note that these commands do not work with the
`MCOD4Data` Tool, since the `MyFamily.od` dates as attribute values.
You can check the different kinds of context
conditions, using the `-c,--coco <arg>` option:

```
java -jar MCOD4Report.jar -i MyFamily.od -c intra
```

```
java -jar MCOD4Report.jar -i MyFamily.od -c all
```

```
java -jar MCOD4Report.jar -i MyFamily.od -c
```

While the first call of the tool does not produce any errors, the others do. The error states
something about types, in this case `Person` not being defined while being used. So how do we solve
this problem?

The answer is that the symbol file of this model has to be imported by the OD model for accessing
the type. For the OD language, we have not fixed a language for defining types. Instead, the types
can be defined in arbitrary models of arbitrary languages, as long as the information about the
definitions of the types are stored in the symbol files of the models and the OD imports these
symbol files. This may sound complicated at this point, but conceptually it is actually quite
simple. This has even a huge advantage because it allows us to use the OD language with any other
language that defines types. You could even use languages that are not defined with MontiCore, as
long as suitable symbol files are generated from the models of these languages.

The following subsection describes how to fix the error in the example model `Example.od`
by importing a symbol file defining the (yet undefined) types. .

### Step 4: Using the Model Path to Resolve Symbols

In this section we make use of the model path and provide the tool with a symbol file (stored
symbol table) of another model, which contains the necessary type information.

Create a new directory `cd` in the directory where the tool `MCOD4Report.jar` is located. The
symbol file `MyFamily.cdsym` of a model, which provides all necessary type information, can be
found [here](doc/MyFamily.cdsym). Download the file, name it `MyFamily.cdsym`, and move it into the
directory `cd`.

The contents of the symbol file are of minor importance for you as a language user. In case you are
curious and had a look into the symbol file:
The symbol file contains a JSON representation of the symbols defined in a model. In this case, the
symbol file contains information about all sorts of symbols defined in a class diagram. Usually,
the tools of MontiCore languages automatically generate the contents of these files and you, as
a language user, must not be concerned with their contents.

The path containing the directory structure that contains the symbol file is called the "Model Path"
. If we provide the model path to the tool, it will search for symbols in symbol files, which are
stored in directories contained in the model path. So, if we want the tool to find our symbol file,
we have to provide the model path to the tool via the `-path <arg>` option:

```
java -jar MCOD4Report.jar -i Example.od -c -path <MODELPATH>
```

where `<MODELPATH>` is the path where you stored the downloaded symbol file. In our example, in case
you stored the model in the directory `cd`, execute the following command:

```
java -jar MCOD4Report.jar -i Example.od -c -path cd
```

Well, executing the above command still produces the same error message. This is because the symbol
file needs to be imported first, just like in Java. Therefore, we add the following import statement
to the beginning of the contents contained in the file `MyFamily.od` containing the OD `MyFamily`:

```
import cd.MyFamily.*;

objectdiagram Example {
  ...
}
```

The added import statement means that the file containing the OD imports all symbols that are stored
in the symbol file `MyFamily`. Note that you may have to change the name here, depending on how you
named the symbol file from above. The concrete file ending `.cdsym` is not important in this case.
However, the file ending of the symbol file must end with `sym`, i.e., the name of the symbol file
must be compatible to the pattern `*.*sym`. If you strictly followed the instructions of this
tutorial, then you are fine.

If we now execute the command again, the tool will print no output. This means that it processed
the model successfully without any context condition violations. Great!

### Step 5: Storing Symbols

The previous section describes how to load symbols from an existing symbol file. Now, we will use
the tool to store a symbol file for our `Example.od` model. The stored symbol file will contain
information about the objects defined in the OD. It can be imported by other models for using the
symbols introduced by these object definitions, similar to how we changed the file `Example.od` for
importing the symbols contained in the symbol file `MyFamily.cdsym`.

Using the `-s,-symboltable <arg>` option builds the symbol tables of the input models and stores
them in the file paths given as arguments. Either no file paths must be provided or exactly one file
path has to be provided for each input model. The symbol file for the i-th input model is stored in
the file defined by the i-th file path. If you do not provide any file paths, the tool stores
the symbol table of each input model in the symbol
file `target/symbols/{packageName}/{fileName}.odsym`
where `packageName` is the name of the package as specified in the file containing the model
and `fileName` is the name of the file containing the model. The file is stored relative to the
working directory, i.e., the directory in which you execute the command for storing the symbol
files. Furthermore, please notice that in order to store the symbols properly, the model has to be
well-formed in all regards, and therefore all context conditions are checked beforehand.

For storing the symbol file of `Example.od`, execute the following command
(the implicit context condition checks require using the model path option):

```
java -jar MCOD4Report.jar -i Example.od -path cd -s
```

The tool produces the file `target/symbols/MyFamily.odsym`, which can now be imported by other
models, e.g., by models that need to use some of the objects defined in the OD `MyFamily`.

For storing the symbol file of `Example.od` in the file `syms/Example.odsym`, for example, execute
the following command
(again, the implicit context condition checks require using the model path option):

```
java -jar MCOD4Report.jar -i Example.od -path cd -s syms/Example.odsym
```

Congratulations, you have just finished the tutorial about saving OD symbol files and are pretty
much done reading this README!

## Further Information

* [Project root: MontiCore @github](https://github.com/MontiCore/monticore)
* [MontiCore documentation](http://www.monticore.de/)
* [**List of languages**](https://github.com/MontiCore/monticore/blob/opendev/docs/Languages.md)
* [**MontiCore Core Grammar Library**](https://github.com/MontiCore/monticore/blob/opendev/monticore-grammar/src/main/grammars/de/monticore/Grammars.md)
* [Best Practices](https://github.com/MontiCore/monticore/blob/opendev/docs/BestPractices.md)
* [Publications about MBSE and MontiCore](https://www.se-rwth.de/publications/)
* [Licence definition](https://github.com/MontiCore/monticore/blob/master/00.org/Licenses/LICENSE-MONTICORE-3-LEVEL.md)

[od4report-link]: http://www.monticore.de/download/MCOD4Report.jar

[od4data-link]: http://www.monticore.de/download/MCOD4Data.jar
