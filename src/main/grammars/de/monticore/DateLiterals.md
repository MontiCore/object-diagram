<!-- (c) https://github.com/MontiCore/monticore -->

<!-- Beta-version: This is intended to become a MontiCore stable grammar. -->

# DateLiterals

TODO: Write an awesome but precise description!

The grammar file is [`de.monticore.DateLiterals`][DateLiterals].

The OD language the monticoreuages  
 [`de.monticore.literals.MCCommonLiterals`][MCCommonLiterals]

## Handwritten Extensions
## Pretty Printer
- Pretty Printer can be found in [`de.monticore.dateliterals.prettyprinter`][prettyprinter]

## Functionality
### CoCos
The CoCos can be found in [`de.monticore.dateliterals._cocos`][cocos] and are combined
accessible in [`monticore.odbasics._cocos.ODBasicsCoCos`][DateCoCos].

The context conditions check different parts of the models, to ensure the semantic correctness.

[DateLiterals]: https://git.rwth-aachen.de/monticoreuages/od/-/blob/master/src/main/grammars/de/monticore/DateLiterals.mc4
[MCCommonLiterals]: https://git.rwth-aachen.de/monticore/monticore/-/blob/dev/monticore-grammar/src/main/grammars/de/monticore/types/MCFullGenericTypes.mc4
[_ast]: https://git.rwth-aachen.de/monticoreuages/od/-/tree/master/src%2Fmain%2Fjava%2Fde%2Fmonticore%2Flang%2Fodbasics%2F_ast
[_symboltable]: https://git.rwth-aachen.de/monticoreuages/od/-/tree/master/src%2Fmain%2Fjava%2Fde%2Fmonticore%2Flang%2Fodbasics%2F_symboltable
[prettyprinter]: https://git.rwth-aachen.de/monticoreuages/od/-/blob/master/src/main/java/de/monticore/dateliterals/prettyprinter/DateLiteralsPrettyPrinter.java
[cocos]: https://git.rwth-aachen.de/monticoreuages/od/-/tree/master/src/main/java/de/monticore/dateliterals/_cocos
[DateCoCos]: https://git.rwth-aachen.de/monticoreuages/od/-/blob/master/src/main/java/de/monticore/dateliterals/_cocos/DateLiteralsCoCos.java