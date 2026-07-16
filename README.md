<!-- (c) https://github.com/MontiCore/monticore -->

This documentation is primarily intended for **modelers** who work with object diagrams
and want to use the **OD4Development** tool effectively. The goal is to help you become
productive quickly, perform common modeling tasks confidently, and generate direct value
from ODs (for example, deriving a class diagram).

For **language engineers** (language development, grammar work, tool extensions), detailed
technical documentation is available at
[`src/main/grammars/de/monticore/OD4Report.md`](src/main/grammars/de/monticore/OD4Report.md).

# What is OD4Development?

OD4Development provides a complete toolchain for working with object diagrams:

- **Model validation**: Constraint-on-Context (CoCo) checks verify model consistency and correctness
  beyond syntax—catching logical issues before downstream processing.
- **Model normalization**: Pretty printing reformats ODs into a canonical style, useful for version
  control and team collaboration.
- **Symbol management**: Export and import symbol tables to share type information across models
  and enable cross-model references.
- **Artifact derivation**: Generate class diagrams from ODs, creating a structured view of the
  instances and relationships described in your model.
- **Transparent feedback**: All operations report their results clearly via the command line,
  making it easy to diagnose issues and verify success.

# Example Model

<figure>
  <img width="700" src="doc/pics/OD_Example.png" alt="The graphical syntax of the example OD">
  <figcaption><b>Figure 1:</b> The graphical syntax of the example OD.</figcaption>
</figure>

Figure 1 shows the OD `MyFamily`. In textual form:

```txt
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

This notation is intentionally aligned with the readability of class diagram syntax.

# Setup and Build

## Prerequisites

- Java 21 JDK
- Git
- Gradle 8.5+

> Note: This project has **no Gradle wrapper**. Use your locally installed
> `gradle` command directly.

## Getting the Tools

You have two options:

### Option A: Download Pre-built JARs (Recommended for Quick Start)

Pre-built JARs are available for download:

- [**OD4Development.jar**][od4dev-link]
- [**OD4Report.jar**][od4report-link]

Download the JAR you need and place it in your working directory. You can start using the tool immediately
without any compilation step.

### Option B: Build from Source

Clone the repository and build locally:

```bash
git clone git@github.com:MontiCore/object-diagram.git
cd object-diagram
gradle build --refresh-dependencies
```

Afterwards, the JARs are available in `target/libs`, including:

- `MCOD4Development.jar`
- `MCOD4Report.jar`

# Focus: OD4Development

## Quick Start

Create an `Example.od` file:

```txt
objectdiagram Example {
  bob:Person {
    age = 42;
  };

  alice:Person {
    age = 40;
  };

  link married bob <-> alice;
}
```

This simple example defines two persons (Bob and Alice) with their respective
ages, and establishes a "married" link between them. It's a minimal but complete
object diagram and therefore a good introductory example to demonstrate the
basic syntax and testing the tool.

Minimal command (parse only):

```bash
java -jar target/libs/MCOD4Development.jar -i Example.od
```

Typical successful output:

```txt
Successfully parsed Example
```

This confirms the OD has been read and parsed without errors.

## CLI Reference: OD4Development

| Option | Meaning |
| --- | --- |
| `-h`, `--help` | Show help |
| `-i`, `--input <file>` | **Required**: input OD |
| `-path <dirlist>` | Model/symbol path (multiple entries supported) |
| `-pp`, `--prettyprint [file]` | Pretty print to stdout or file |
| `-s`, `--symboltable [file]` | Store symbol table (`.odsym`) |
| `-c`, `--coco [intra\|inter]` | Run CoCos (`no arg` = all CoCos) |
| `-o`, `--output <dir>` | Derive class diagram from OD |
| `-d` | Enable debug logging |

## Typical Workflows

### 1) Run CoCos

**Why?** CoCos validate your model for consistency, type correctness, and naming
conventions to catch issues before they propagate downstream.

Use the following command to run intra-model CoCos only:

```bash
java -jar target/libs/MCOD4Development.jar -i Example.od -c intra
```

Use the following command to run inter-model CoCos only:

```bash
java -jar target/libs/MCOD4Development.jar -i Example.od -c inter
```

Use the following command to run all CoCos (intra + inter):

```bash
java -jar target/libs/MCOD4Development.jar -i Example.od -c
```

If no issues are found, the tool reports:

```txt
Successfully checked the CoCos for object diagram Example
```

### 2) Pretty Printing

**Why?** Keep your ODs readable and consistent, especially in team environments.
Pretty printing normalizes whitespace, indentation, and layout without changing
the model's meaning.

Use the following command to pretty print to stdout:

```bash
java -jar target/libs/MCOD4Development.jar -i Example.od -pp
```

Use the following command to pretty print to a file:

```bash
java -jar target/libs/MCOD4Development.jar -i Example.od -pp Example.pp.od
```

### 3) Export Symbol Table

Use the following command to export a symbol table with the default name (next to the model, e.g. `Example.odsym`):

```bash
java -jar target/libs/MCOD4Development.jar -i Example.od -s
```

Use the following command to export the symbol table to an explicit target path:

```bash
java -jar target/libs/MCOD4Development.jar -i Example.od -s syms/Example.odsym
```

On success, the tool reports:

```txt
Creation of symbol file <absolute-path-to-odsym> successful
```

### 4) Resolve Symbols via `-path`

Object diagrams describe *instances* of types that must be defined elsewhere—typically in a class diagram.
For example, the `MyFamily` OD uses types like `Person`, `BMW`, and `Jaguar`. These types are
defined in a separate class diagram model, whose symbol information is stored in a symbol file
(`.cdsym`).

To validate that your OD uses only types that actually exist, you must:

1. Make the symbol file(s) discoverable by setting a model path with `-path`.
2. Import the symbol files in your OD using `import` statements.
3. Run CoCo validation to check type references.

**Example workflow:**

Suppose you have a class diagram that defines `Person`, `BMW`, and `Jaguar` in a file called `Types.cd`.
After processing that CD, you get a symbol file `Types.cdsym`. Now add to your OD:

```txt
import Types.*;

objectdiagram Example {
  ...
}
```

Then run validation:

```bash
java -jar target/libs/MCOD4Development.jar -i Example.od -path cd -c
```

Here, `cd` is the directory containing `Types.cdsym`. The tool will load the symbol file, resolve
the type references in your OD, and report any mismatches.

**Troubleshooting symbol resolution:**

If types cannot be resolved, check:

- whether matching `*sym` files exist on the model path (file extensions must end with `sym`),
- whether your OD `import` statements match the symbol file location and names,
- whether `-path` points to the correct root directory containing the symbol files.

### 5) Derive a Class Diagram from an OD (`-o`)

**Why?** An OD shows a concrete snapshot of object instances and their relationships at a specific point in time.
By analyzing these instances, you can reverse-engineer a class diagram that captures the underlying
structure—the types, attributes, and associations that make up the model. This is useful when:

- You have detailed instance data but need a class-level abstraction,
- You want to document the structural design implied by your instances,
- You need a CD as input for downstream code generation or further modeling,
- You want to validate that your instances follow a coherent type structure.

**Usage:**

```bash
java -jar target/libs/MCOD4Development.jar -i Example.od -o out
```

This analyzes the OD and generates a class diagram, written to the `out` directory.
The resulting CD will contain classes for all object types referenced in the OD, their attributes,
and the associations between them—essentially a schema that your instance data conforms to.

# Further Information

- [Project root: MontiCore @github](https://github.com/MontiCore/monticore)
- [MontiCore documentation](http://www.monticore.de/)
- [List of languages](https://github.com/MontiCore/monticore/blob/opendev/docs/Languages.md)
- [MontiCore Core Grammar Library](https://github.com/MontiCore/monticore/blob/opendev/monticore-grammar/src/main/grammars/de/monticore/Grammars.md)
- [Best Practices](https://github.com/MontiCore/monticore/blob/opendev/docs/BestPractices.md)
- [Publications about MBSE and MontiCore](https://www.se-rwth.de/publications/)
- [Licence definition](https://github.com/MontiCore/monticore/blob/master/00.org/Licenses/LICENSE-MONTICORE-3-LEVEL.md)

[od4report-link]: http://www.monticore.de/download/MCOD4Report.jar

[od4dev-link]: http://www.monticore.de/download/MCOD4Development.jar
