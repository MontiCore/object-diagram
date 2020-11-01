<!-- (c) https://github.com/MontiCore/monticore -->

<!-- Beta-version: This is intended to become a MontiCore stable explanation. -->

# (UML/P) Object Diagrams

Object diagrams are a sublanguage provided by the UML. 
MontiCore language for 
UML/P ODs. 

UML/P ODs are an OD variant suited e.g. for the 
modeling of for desired and unwanted object structures,
or can be used as setup definitions for tests.
UML/P ODs are suited for combination e.g. with the 
object constraint language OCL
(details can be found in [Rum16], [Rum17]).

This MontiCore project contains 
* seven language components, defined by their own grammars, 
* appropriate context conditions for each language component, 
* which also includes a symbol table infrastructure 
  for managing and storing symbol tables, and
* pretty-printers. 

## Example Model
In textual syntax, an example OD is defined as follows:

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

A graphical version is shown in Figure 1. 

<img width="300" src="../../../../../doc/pics/OD_Example.png" alt="The graphical syntax of an example OD" style="float: left; margin-right: 10px;">
<br><b>Figure 1:</b> The graphical syntax of example OD `MyFamiliy`.

The conceptual elements of ODs are similar to JSOM or XML, but deliver (hopefully) a better readable syntax:

* Objects have a name, e.g. `alice`, which can be used for link structures.
* Objects also have a type, e.g. `:Person` 
* UML allows both to be optional, if not needed or reconstructable from context.
* Attributes are defined with their value, e.g. `color = RED`.
* Links can be explicitly defined, allowing arbitrary graph structures, e.g. `alice <-> bob`. 




This was for us the most intuitive textual representation of ODs, also provding an easy way to define
inner objects and more complex attributes.

## Main Class ```OD4ReportCLI```
The class [```OD4ReportCLI```](../../../java/de/monticore/od4report/OD4ReportCLI.java) provides typical functionality used when
processing models. To this effect, the class provides methods
for parsing, pretty-printing, creating symbol tables, storing symbols, and 
loading symbols. Detailed information about the methods can be found in the Javadoc documentation
of the class [```OD4ReportCLI```](../../../java/de/monticore/od4report/OD4ReportCLI.java).
Each CLI can be found in the ```target``` folder once the project build was successful using the
name pattern ``od-[version]-[name of the language]-cli.jar``. Each CLI provides a ``-h`` parameter
providing further information on how to use the CLIs.

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
The [OD4Development](../../../grammars/de/monticore/OD4Development.mc4) grammar extends the OD4Data language 
by adding an extended namespace for objects and attribute values. Further, it supports time dates
as values for attributes. It focusses on the modelling phase in typical object-oriented development
projects and is therefore mainly used for data modelling. Consequently, it omits method signatures
and complex generics. OD4Development fits very well to CD4Analysis.
A detailed documentation of the grammar can be found in the [artifact defining the grammar](../../../grammars/de/monticore/OD4Development.mc4).

The grammar [OD4Development](../../../grammars/de/monticore/OD4Development.mc4) extends the grammar
* [OD4Data](../../../grammars/de/monticore/OD4Data.mc4) for the object diagrams, attributes, and links.

### OD4Report 
The [OD4Report](../../../grammars/de/monticore/OD4Report.mc4) grammar extends the OD4Data language 
by adding an extended namespace for objects and attribute values. Further, it supports time dates
as values for attributes. Its main appliciation are reporting, e.g. in MontiCore's generated reports, 
or data formats which need make use of the extended namespace, e.g. artifact-based analyses. 

The grammar [OD4Report](../../../grammars/de/monticore/OD4Report.mc4) extends the grammars
* [OD4Data](../../../grammars/de/monticore/OD4Data.mc4) for the object diagrams, attributes, and links.
* [DateLiterals](../../../grammars/de/monticore/DateLiterals.mc4) for the support of dates.  

### DateLiterals
The grammar [DateLiterals](../../../grammars/de/monticore/DateLiterals.mc4) is a component grammar for modeling dates in simple fashion. Dates consist
of a date part modeling year, month, and day, as well as a time part, modeling hour, minute, and 
second. 
A detailed documentation of the grammar can be found in the [artifact defining the grammar](../../../grammars/de/monticore/DateLiterals.mc4).

The grammar [DateLiterals](../../../grammars/de/monticore/DateLiterals.mc4) extends the grammar
* [MCCommonLiterals](https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/literals/MCCommonLiterals.mc4) for describung the timestamps.

## Context Conditions
This section lists the context conditions for the [ODBasis](../../../grammars/de/monticore/ODBasis.mc4) grammar, 
context conditions for the [ODAttribute](../../../grammars/de/monticore/ODAttribute.mc4) grammar,
context conditions for the [ODLink](../../../grammars/de/monticore/ODLink.mc4) grammar,
context conditions for the [OD4Data](../../../grammars/de/monticore/OD4Data.mc4) grammar
context conditions for the [OD4Development](../../../grammars/de/monticore/OD4Development.mc4) grammar.
context conditions for the [OD4Report](../../../grammars/de/monticore/OD4Report.mc4) grammar.
context conditions for the [DateLiterals](../../../grammars/de/monticore/DateLiterals.mc4) grammar.

### ODBasis Context Conditions
The implementations of the context conditions for the [ODBasis](../../../grammars/de/monticore/ODBasis.mc4) grammar are located [here](../../../java/de/monticore/odbasis/_cocos).

* The context condition [```NoAbstractAttributesCoCo```](../../../java/de/monticore/odbasis/_cocos/attributes/NoAbstractAttributesCoCo.java) checks if an object contains an abstract attribute.

* The context condition [```PartialAndCompleteAttributesCoCo```](../../../java/de/monticore/odbasis/_cocos/attributes/PartialAndCompleteAttributesCoCo.java) ensures that attributes defined with a partial operator are not defined in a complete attribute definition and vice versa.

* The context condition [```UniqueAttributeNamesCoCo```](../../../java/de/monticore/odbasis/_cocos/attributes/UniqueAttributeNamesCoCo.java) checks whether the attribute names within an object are unique.

* The context condition [```UniqueObjectNamesCoCo```](../../../java/de/monticore/odbasis/_cocos/names/UniqueObjectNamesCoCo.java) checks if object names are not unique in a diagram.

* The context condition [```ValidObjectReferenceCoCo```](../../../java/de/monticore/odbasis/_cocos/object/ValidObjectReferenceCoCo.java) checks object references as an attribute value actually exist. 

### ODAttribute Context Conditions
None.

### ODLink Context Conditions
The implementations of the context conditions for the [ODLink](../../../grammars/de/monticore/ODLink.mc4) grammar are located [here](../../../java/de/monticore/odlink/_cocos).

* The context condition [```LinkEndConsistencyCoCo```](../../../java/de/monticore/odlink/_cocos/link/LinkEndConsistencyCoCo.java) checks whether named links consists of the same object type references on the left and right side of a link.
                                                                                                                              
* The context condition [```NoAbstractLinkCoCo```](../../../java/de/monticore/odlink/_cocos/link/NoAbstractLinkCoCo.java) checks if links have an abstract modifier. 

* The context condition [```ValidLinkReferenceCoCo```](../../../java/de/monticore/odlink/_cocos/link/ValidLinkReferenceCoCo.java) checks if the object referenced actually exist.

### OD4Data Context Conditions
None.

### OD4Development Context Conditions
None.

### OD4Report Context Conditions
None.

### DateLiterals Context Conditions
The implementations of the context conditions for the [DateLiterals](../../../grammars/de/monticore/DateLiterals.mc4) grammar are located [here](../../../java/de/monticore/dateliterals/_cocos).

* The context condition [```DateConcistencyCoCo```](../../../java/de/monticore/dateliterals/_cocos/date/DateConcistencyCoCo.java) checks if a date is consistent in termns of day of year as well as time of day.

## Symbol Table
The OD language uses the build-in symbol types [```VariableSymbol```][BasicSymbolsRef]
and [```DiagramSymbol```][BasicSymbolsRef] as well as the [type symbol types][TypeSymbolsRef] of MontiCore. 

Each OD may define objects. Therefore, ODs may export [```VariableSymbols```][BasicSymbolsRef] 
containing the information about the name and the type of the object. Possible types for objects are 
[```MCObjectTypes```][MCBasicTypesRef].
Furthermore, for checking whether the types of objects and variables
are defined, ODs may import [```TypeSymbols```][BasicSymbolsRef] 
and [```OOTypeSymbols```][OOSymbolsRef]. 

### Symbol Table Data Structure

<img width="800" src="../../../../../doc/pics/STDataStructure.png" alt="The data structure of the symbol table of the OD language" style="float: left; margin-right: 10px;">
<br><b>Figure 2:</b> The data structure of the symbol table of the OD language.

&nbsp;  

Figure 2 depicts the symbol table data structure of the [```OD4Report```](../../../grammars/de/monticore/OD4Report.mc4)
grammar. The ```OD4ReportGlobalScope``` is associated to an
```OD4ReportArtifactScope``` for each artifact defining an OD. In each
of these artifacts, at most one OD can be defined and each OD introduces 
a [```DiagramSymbol```][BasicSymbolsRef]. 
Therefore, each ```OD4DReportArtifactScope``` is associated to exactly one 
[```DiagramSymbol```][BasicSymbolsRef]. 
The ```OD4ReportArtifactScope```
contains a [```VariableSymbol```][BasicSymbolsRef] for each named object that is defined inside the 
OD. 

<img width="800" src="../../../../../doc/pics/STInstanceExample.png" alt="Symbol table instance of the OD depicted in Figure 1" style="float: left; margin-right: 10px;">
<br><b>Figure 3:</b> Symbol table instance of the OD depicted in Figure 1.

&nbsp;  

Figure 3 depicts the symbol table instance for the OD ```MyFamily```. 
The three objects ```alice:Person```, ```bob:Person``` and  ```tiger:Jaguar``` correspond to the 
 [```VariableSymbol```][BasicSymbolsRef]
 instances linked to the ```OD4ReportArtifactScope```. 
 The object ```:DiagramSymbol``` is linked to the ```OD4ReportArtifactScope``` and has the 
 same name as the OD defining the symbol. 
 
 The handwritten extensions of the symbol table creator can be found as follows:
 * The creator for the [```ODBasis```](../../../grammars/de/monticore/ODBasis.mc4) grammar can be found in the class  [```ODBasisSymbolTableCreator```](../../../java/de/monticore/odbasis/_symboltable/ODBasisSymbolTableCreator.java). 
 * The creator for the [```ODAttribute```](../../../grammars/de/monticore/ODAttribute.mc4) grammar can be found in the class  [```ODAttributeSymbolTableCreator```](../../../java/de/monticore/odattribute/_symboltable/ODAttributeSymbolTableCreator.java). 
 * The creator for the [```ODLink```](../../../grammars/de/monticore/ODLink.mc4) grammar can be found in the class  [```ODLinkSymbolTableCreator```](../../../java/de/monticore/odlink/_symboltable/ODLinkSymbolTableCreator.java). 
 * The creator for the [```OD4Report```](../../../grammars/de/monticore/OD4Report.mc4) grammar can be found in the class  [```OD4ReportSymbolTableCreator```](../../../java/de/monticore/od4report/_symboltable/OD4ReportSymbolTableCreator.java). 
  
### Symbol kinds used by the OD Language (importable or subclassed)
The OD language uses symbols of kind [```TypeSymbol```][BasicSymbolsRef],
[```VariableSymbol```][BasicSymbolsRef], and
[```DiagramSymbol```][BasicSymbolsRef].

### Symbol kinds defined by the OD language (exported):
None.

### Symbols imported by OD models:
* ODs import [```VariableSymbols```][BasicSymbolsRef]. 
The objects represented by imported [```VariableSymbols```][BasicSymbolsRef]
can be used as attribute values and link references.
* ODs import [```TypeSymbols```][BasicSymbolsRef]. The imported types can be used as types for objects.

### Symbols exported by OD models:
* OD models export [```VariableSymbols```][BasicSymbolsRef]. 
For each object defined in an OD, the OD exports a corresponding [```VariableSymbol```][BasicSymbolsRef]. 
* Each OD exports exactly one [```DiagramSymbol```][BasicSymbolsRef] corresponding to the ODArtifact.

### Serialization and De-serialization of Symbol Tables
The OD language uses the DeSer implementations as generated by MontiCore
without any handwritten extensions. The following 
depicts the symbol file obtained from serializing the symbol table instance 
depicted in Figure 3:

```json
{
  "generated-using": "www.MontiCore.de technology",
  "kindHierarchy": [
    [
      "de.monticore.od4data._symboltable.OOTypeSymbol",
      "de.monticore.symbols.basicsymbols._symboltable.TypeSymbol"
    ],
    [
      "de.monticore.od4data._symboltable.TypeVarSymbol",
      "de.monticore.symbols.basicsymbols._symboltable.TypeSymbol"
    ],
    [
      "de.monticore.od4data._symboltable.FieldSymbol",
      "de.monticore.symbols.basicsymbols._symboltable.VariableSymbol"
    ],
    [
      "de.monticore.od4data._symboltable.MethodSymbol",
      "de.monticore.symbols.basicsymbols._symboltable.FunctionSymbol"
    ]
  ],
  "symbols": [
    {
      "kind": "de.monticore.symbols.basicsymbols._symboltable.VariableSymbol",
      "name": "alice",
      "type": {
        "kind": "de.monticore.types.check.SymTypeOfObject",
        "objName": "Person"
      },
      "isReadOnly": false
    },
    {
      "kind": "de.monticore.symbols.basicsymbols._symboltable.VariableSymbol",
      "name": "tiger",
      "type": {
        "kind": "de.monticore.types.check.SymTypeOfObject",
        "objName": "Jaguar"
      },
      "isReadOnly": false
    },
    {
      "kind": "de.monticore.symbols.basicsymbols._symboltable.VariableSymbol",
      "name": "bob",
      "type": {
        "kind": "de.monticore.types.check.SymTypeOfObject",
        "objName": "Person"
      },
      "isReadOnly": false
    },
    {
      "kind": "de.monticore.symbols.basicsymbols._symboltable.DiagramSymbol",
      "name": "MyFamily"
    }
  ]
}
```

## Further Information

* [Project root: MontiCore @github](https://github.com/MontiCore/monticore)
* [MontiCore documentation](http://www.monticore.de/)

* [**List of languages**](https://github.com/MontiCore/monticore/blob/dev/docs/Languages.md)
* [**MontiCore Core Grammar Library**](https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/Grammars.md)
* [Best Practices](https://github.com/MontiCore/monticore/blob/dev/docs/BestPractices.md)
* [Publications about MBSE and MontiCore](https://www.se-rwth.de/publications/)

* [Licence definition](https://github.com/MontiCore/monticore/blob/master/00.org/Licenses/LICENSE-MONTICORE-3-LEVEL.md)

[BasicSymbolsRef]:https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/symbols/BasicSymbols.mc4
[TypeSymbolsRef]:https://github.com/MontiCore/monticore/tree/dev/monticore-grammar/src/main/grammars/de/monticore/types
[MCBasicTypesRef]:https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/types/MCBasicTypes.mc4
[OOSymbolsRef]:https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/symbols/OOSymbols.mc4
[ExpressionsBasisRef]:https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/expressions/ExpressionsBasis.mc4
[UMLStereotypeRef]:https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/UMLStereotype.mc4
[MCCommonLiteralsRef]:https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/literals/MCCommonLiterals.mc4
[CommonExpressionsRef]:https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/expressions/CommonExpressions.mc4
[OCLExpressionsRef]:https://github.com/MontiCore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/expressions/OCLExpressions.mc4




