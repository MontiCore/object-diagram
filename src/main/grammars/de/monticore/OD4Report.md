<!-- (c) https://github.com/MontiCore/monticore -->

<!-- Beta-version: This is intended to become a MontiCore stable explanation. -->

# (UML/P) OD4Report

Language for textual object diagrams. In its current state the language extends the capabilities of 
the OD4Data language and its application is reporting in certain MontiCore projects and toolchain (e.g. 
artifact toolchains). It enhances the OD4Data language by adding an extended namespace for objects
and attribute values. Additionally, it offers the possibility to use dates as  
expression in certain structures of a model, e.g. attribute values. 

The grammar file is [`de.monticore.OD4Report`][OD4Report].

The language uses the following MontiCore languages:  
 [`de.monticore.OD4Data`][OD4Data] and
 [`de.monticore.DateLiterals`][DateLiterals]

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

