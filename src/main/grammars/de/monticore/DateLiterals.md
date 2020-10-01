<!-- (c) https://github.com/MontiCore/monticore -->

<!-- Beta-version: This is intended to become a MontiCore stable explanation. -->

# DateLiterals

Language for the representation of Dates. In its current state the language provides three
 different formats for Dates and one format for to present time information. Furthermore, the
  language provides context conditions to ensure the correctness of dates and timestamps. 

The grammar file is [`de.monticore.DateLiterals`][DateLiterals].

The OD language the monticoreuages  
 [`de.monticore.literals.MCCommonLiterals`][MCCommonLiterals]

## Handwritten Extensions
### AST
- Handwritten AST nodes can be found in [`monticore.dateliterals._ast`][_ast]
### Pretty Printer
- Pretty Printer can be found in [`de.monticore.dateliterals.prettyprinter`][prettyprinter]

## Functionality
### CoCos
The CoCos can be found in [`de.monticore.dateliterals._cocos`][_cocos] and are combined
accessible in [`monticore.dateliterals._cocos.DateLiteralsCoCos`][DateCoCos].

The context conditions check different parts of the models, to ensure the semantic correctness
. In this case the cocos ensure that language models only contain valid dates and timestamps.

[DateLiterals]: https://git.rwth-aachen.de/monticore/languages/od/-/blob/master/src/main/grammars/de/monticore/DateLiterals.mc4
[MCCommonLiterals]: https://git.rwth-aachen.de/monticore/monticore/-/blob/dev/monticore-grammar/src/main/grammars/de/monticore/types/MCFullGenericTypes.mc4
[_ast]: https://git.rwth-aachen.de/monticore/languages/od/-/tree/master/src/main/java/de/monticore/dateliterals/_ast
[_symboltable]: https://git.rwth-aachen.de/monticore/languages/od/-/tree/master/src/main/java/de/monticore/dateliterals/_symboltable
[prettyprinter]: https://git.rwth-aachen.de/monticore/languages/od/-/blob/master/src/main/java/de/monticore/dateliterals/prettyprinter/DateLiteralsPrettyPrinter.java
[_cocos]: https://git.rwth-aachen.de/monticore/languages/od/-/tree/master/src/main/java/de/monticore/dateliterals/_cocos
[DateCoCos]: https://git.rwth-aachen.de/monticore/languages/od/-/blob/master/src/main/java/de/monticore/dateliterals/_cocos/DateLiteralsCoCos.java

## Further Information

* [Project root: MontiCore @github](https://github.com/MontiCore/monticore)
* [MontiCore documentation](http://www.monticore.de/)

* [**List of languages**](https://git.rwth-aachen.de/monticore/monticore/-/blob/dev/docs/Languages.md)
* [**MontiCore Core Grammar Library**](https://git.rwth-aachen.de/monticore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/Grammars.md)
* [Best Practices](BestPractices.md)
* [Publications about MBSE and MontiCore](https://www.se-rwth.de/publications/)

* [Licence definition](https://github.com/MontiCore/monticore/blob/master/00.org/Licenses/LICENSE-MONTICORE-3-LEVEL.md)

