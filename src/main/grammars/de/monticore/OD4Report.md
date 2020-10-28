<!-- (c) https://github.com/MontiCore/monticore -->

<!-- Beta-version: This is intended to become a MontiCore stable explanation. -->

# (UML/P) Object Diagrams

This module contains a language for textual (UML/P) object diagrams. 
The main application of the OD language application is 
reporting in certain MontiCore projects and toolchains (e.g. 
artifact toolchains). ...

The grammar file is [`de.monticore.OD4Report`][OD4Report].

The language uses the following MontiCore languages:  
 [`de.monticore.OD4Data`][OD4Data] and
 [`de.monticore.DateLiterals`][DateLiterals]


## Example Model
<img width="800" src="../../../../../doc/pics/OD_Example.png" alt="The graphical syntax of an example OD" style="float: left; margin-right: 10px;">
<br><b>Figure 1:</b> The graphical syntax of an example OD.

&nbsp;  

Figure 1 depicts the OD ```MyFamily``` in graphical syntax. In textual syntax, 
the OD is defined as follows:

``` 
objectdiagram MyFamily {
  alice:Person {
    age = 29;
    cars = [
      :BMW {
        color = BLUE;
      },
      tiger:Jaguar {
        color = RED;
        length = 5.3;
      }
    ];
  };
  bob:Person {
    nicknames = ["Bob", "Bobby", "Robert"];
    cars = [tiger];
  };
  link married alice <-> bob;
}
```

This was for us the most intuitive textual representation of ODs, also provding an easy way to define
inner objects and more complex attributes.

## Main Class ```OD4ReportCLI```
The class [```OD4ReportCLI```](../../../java/de/monticore/od4report/OD4ReportCLI.java) provides typical functionality used when
processing models. To this effect, the class provides methods
for parsing, pretty-printing, creating symbol tables, storing symbols, and 
loading symbols. Detailed information about the methods can be found in the Javadoc documentation
of the class [```OD4ReportCLI```](../../../java/de/monticore/od4report/OD4ReportCLI.java). 


## Grammars

This module contains the six grammars [ODBasis](../../../grammars/de/monticore/ODBasis.mc4), 
[ODAttribute](../../../grammars/de/monticore/ODAttribute.mc4), 
[ODLink](../../../grammars/de/monticore/ODLink.mc4), 
[OD4Data](../../../grammars/de/monticore/OD4Data.mc4), 
[OD4Report](../../../grammars/de/monticore/OD4Report.mc4), 
and [DateLiterals](../../../grammars/de/monticore/DateLiterals.mc4). 

### ODBasis
The grammar [ODBasis](../../../grammars/de/monticore/ODBasis.mc4) contains the basic constituents to define textual 
representations of UML/P ODs. A detailed documentation of the grammar can 
be found in the [artifact defining the grammar](../../../grammars/de/monticore/ODBasis.mc4). 

The grammar [ODBasis](../../../grammars/de/monticore/ODBasis.mc4) defines the syntax for 
* OD artifact, 
* objects (named and anonymous), and 
* simple object attributes.  
                            
The grammar [ODBasis](../../../grammars/de/monticore/ODBasis.mc4) extends the grammars
* [MCBasicTypes](https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/types/MCBasicTypes.mc4) for adding the possibility to define objects typed as 
  MCObjectTypes, to be able to use symbols of kind Variable, and to enable typechecking. 
  Objects and local variables are added as variable symbols to the symbol table.
* [OOSymbols](https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/symbols/OOSymbols.mc4) for using symbols of kind Diagram und Variable.
* [CommonExpressions](https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/expressions/CommonExpressions.mc4) to be able to reuse visitors for typechecking.
* [UMLStereotype](https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/UMLStereotype.mc4) for adding UMLStereotypes as possible extension points to the grammar. 

### ODAttribute
The grammar [ODAttribute](../../../grammars/de/monticore/ODAttribute.mc4) adds more complex types of 
attributes, e.g. lists, to UML/P ODs. A detailed documentation of the grammar can 
be found in the [artifact defining the grammar](../../../grammars/de/monticore/ODAttribute.mc4). 

The grammar [ODAttribute](../../../grammars/de/monticore/ODAttribute.mc4) defines the syntax for
* Lists of values,
* and Maps of key-value pairs.

The grammar [ODAttribute](../../../grammars/de/monticore/ODAttribute.mc4) extends the grammars
* [ODBasis](../../../grammars/de/monticore/ODBasis.mc4) for providing the basis.
* [MCFullGenericTypes](https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/types/MCFullGenericTypes.mc4) for using symbols of kind Diagram und Variable.

### ODLink
The grammar [ODLink](../../../grammars/de/monticore/ODLink.mc4) adds links to UML/P ODs. 
A detailed documentation of the grammar can be found in the [artifact defining the grammar](../../../grammars/de/monticore/ODLink.mc4). 

The grammar [ODLink](../../../grammars/de/monticore/ODLink.mc4) extends the grammars
* [ODBasis](../../../grammars/de/monticore/ODBasis.mc4) for providing the basis.

### OD4Data
The grammar [OD4Data](../../../grammars/de/monticore/OD4Data.mc4) combines the components ODAttribute 
and ODLink to form a variant of OD language mainly used as a data format. Additionally, it offers 
the possibility to use dates as expression in certain structures of a model, e.g. attribute values.
A detailed documentation of the grammar can be found in the [artifact defining the grammar](../../../grammars/de/monticore/OD4Data.mc4). 

The grammar [OD4Data](../../../grammars/de/monticore/OD4Data.mc4) extends the grammars
* [ODAttribute](../../../grammars/de/monticore/ODAttribute.mc4) for complex attributes.
* [ODLink](../../../grammars/de/monticore/ODLink.mc4) for link structures.

### OD4Development
To write

### OD4Report 
The [OD4Report](../../../grammars/de/monticore/OD4Report.mc4) grammar extends the OD4Data language 
by adding an extended namespace for objects and attribute values. Further, it supports time dates
as values for attributes. Its main appliciation are reporting, e.g. in MontiCore's generated reports, 
or data formats which need make use of the extended namespace, e.g. artifact-based analyses. 

The grammar [OD4Report](../../../grammars/de/monticore/OD4Report.mc4) extends the grammars
* [OD4Data](../../../grammars/de/monticore/OD4Data.mc4) for the object diagrams.
* [DateLiterals](../../../grammars/de/monticore/DateLiterals.mc4) for the support of dates.  

### DateLiterals
The grammar DateLiterals...

## Context Conditions

Context conditions analog definieren
- The context condition XX...
- The context condition YY ...

## Symbol Table

### Symbol Table Data Structure
- CD mit Symboltabellendatenstruktur
- Beispielmodell
- Beispiel Instanz Datenstruktur für das Modell 

### Symbol kinds used by the OD Language (importable or subclassed)

### Symbol kinds defined by the OD language (exported):
None.

### Symbols imported by OD models:
- VariableSymbol aus anderen Modellen
- TypeSymbols für Typen von Objekte

### Symbols exported by SD models:
- VariableSymbols (für jedes Objekt)
- DiagramSymbol (pro Artefakt)

### Serialization and De-serialization of Symbol Tables
- keine handwritten extensions
- Beispiel serialisierte Symboltabelle z.B. vom vorherigen Beispielmodell

## Handwritten Extensions

## Functionality

## Further Information

* [Project root: MontiCore @github](https://github.com/MontiCore/monticore)
* [MontiCore documentation](http://www.monticore.de/)

* [**List of languages**](https://github.com/MontiCore/monticore/blob/dev/docs/Languages.md)
* [**MontiCore Core Grammar Library**](https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/Grammars.md)
* [Best Practices](https://github.com/MontiCore/monticore/blob/dev/docs/BestPractices.md)
* [Publications about MBSE and MontiCore](https://www.se-rwth.de/publications/)

* [Licence definition](https://github.com/MontiCore/monticore/blob/master/00.org/Licenses/LICENSE-MONTICORE-3-LEVEL.md)



