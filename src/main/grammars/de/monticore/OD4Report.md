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
TODO: Graphisches und entsprechendes textuelles OD.

## Main Class XXX
TODO: CLI Klasse beschreiben/verlinken.

## Grammars

This module contains the  seven grammars .... 

### ODBasis
The OD Basis `ODBasis` grammar is a language component for textual 
object diagrams. It provides the basic elements and simple
structures for textual object diagrams: Objects and attributes. 
Additionally, it provides a basic set of context conditions to 
ensure semantic correctness of these elements. 

* The grammar file is [`de.monticore.ODBasis`][ODBasicsGrammar].

* The component grammar uses the following MontiCore languages:
  * [`de.monticore.UMLModifier`][MCUMLModifierGrammar] and  
  * [`de.monticore.types.MCFullGenericTypes`][MCBasicTypes] and
  * [`de.monticore.types.MCFullGenericTypes`][OOSymbols] and
  * [`de.monticore.types.MCFullGenericTypes`][CommonExpressions].

### ODX
The grammar ODX defines ...

### OD4Report 
The OD4Report grammar extends the OD4Data language by adding an 
extended namespace for objects and attribute values. 
Additionally, it offers the possibility to use dates as  
expression in certain structures of a model, e.g. attribute values. 

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

### Symbol kinds defined by the SD language (exported):
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
### AST
- Handwritten AST nodes can be found in [`monticore.od4report._ast`][_ast]
### Symboltable
- Handwritten Symbols, language, and Creator can be found in [`monticore.od4report._symbol`][_symboltable]
### Pretty Printer
- Pretty Printer can be found in [`de.monticore.od4report.prettyprinter`][prettyprinter]

## Functionality
### CoCos
The CoCos can be found in [`de.monticore.od4report._cocos`][_cocos] and are combined
accessible in [`monticore.od4report._cocos.OD4ReportCocos`][OD4ReportCocos].

The context conditions check different parts of the models, to ensure the semantic correctness.

[OD4Report]: https://git.rwth-aachen.de/monticore/languages/od/-/blob/master/src/main/grammars/de/monticore/DateLiterals.mc4
[DateLiterals]: https://git.rwth-aachen.de/monticore/languages/od/-/blob/master/src/main/grammars/de/monticore/DateLiterals.mc4
[OD4Data]: https://git.rwth-aachen.de/monticore/monticore/-/blob/dev/monticore-grammar/src/main/grammars/de/monticore/types/MCFullGenericTypes.mc4
[_ast]: https://git.rwth-aachen.de/monticore/languages/od/-/tree/master/src/main/java/de/monticore/od4report/_ast
[_symboltable]: https://git.rwth-aachen.de/monticore/languages/od/-/tree/master/src/main/java/de/monticore/od4report/_symboltable
[prettyprinter]: https://git.rwth-aachen.de/monticore/languages/od/-/blob/master/src/main/java/de/monticore/od4report/prettyprinter/
[_cocos]: https://git.rwth-aachen.de/monticore/languages/od/-/tree/master/src/main/java/de/monticore/od4report/_cocos
[OD4ReportCocos]: https://git.rwth-aachen.de/monticore/languages/od/-/blob/master/src/main/java/de/monticore/od4report/_cocos/OD4ReportCocos.java

## Further Information

* [Project root: MontiCore @github](https://github.com/MontiCore/monticore)
* [MontiCore documentation](http://www.monticore.de/)

* [**List of languages**](https://git.rwth-aachen.de/monticore/monticore/-/blob/dev/docs/Languages.md)
* [**MontiCore Core Grammar Library**](https://git.rwth-aachen.de/monticore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/Grammars.md)
* [Best Practices](BestPractices.md)
* [Publications about MBSE and MontiCore](https://www.se-rwth.de/publications/)

* [Licence definition](https://github.com/MontiCore/monticore/blob/master/00.org/Licenses/LICENSE-MONTICORE-3-LEVEL.md)

