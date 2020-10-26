<!-- (c) https://github.com/MontiCore/monticore -->

<!-- Beta-version: This is intended to become a MontiCore stable explanation. -->

# DateLiterals

Language component for the representation of Dates:
 * `DatePart`s forms are `yyyy-MM-dd`, `yyyy.MM.dd`, `yyyy/MM/dd`
 * `TimePart`s format is `hh:mm:ss`
 * the combined `Date` format is a combinatiion of both, e.g.
 	`yyyy-MM-dd hh:mm:ss`.

 * All numbers are `NatLiterals`: 
   it is allowed to omit or add initial `0`s as desired.

The language provides three
different formats for Dates and the typical format to present
time information. Furthermore, the
language provides context conditions to ensure the correctness
of dates and timestamps. 
The respective interface nonerminals are designed to be extensible 
by further variants of dates.

 * The *grammar file* is [`de.monticore.DateLiterals`][DateLiterals].

 * The Date Langauge component relies on the MontiCore literals
   in [`de.monticore.literals.MCCommonLiterals`][MCCommonLiterals]

## Handwritten Extensions

### AST
- One handwritten class `ASTDate` can be found in
  [`monticore.dateliterals._ast`][_ast]. It adds a conversion
  of the parsed characterstring to a `LocalDateTime` via 
  `toLocalDateTime()` and vice versa via `setDate()`.

### Pretty Printer
- A Pretty Printer can be found in [`de.monticore.dateliterals.prettyprinter`][prettyprinter]

## Functionality
### CoCos
The CoCos can be found in [`de.monticore.dateliterals._cocos`][_cocos]
and are combined
in [`monticore.dateliterals._cocos.DateLiteralsCoCos`][DateCoCos].

[DateLiterals]: https://git.rwth-aachen.de/monticore/languages/od/-/blob/master/src/main/grammars/de/monticore/DateLiterals.mc4
[MCCommonLiterals]: https://git.rwth-aachen.de/monticore/monticore/-/blob/dev/monticore-grammar/src/main/grammars/de/monticore/types/MCFullGenericTypes.mc4
[_ast]: https://git.rwth-aachen.de/monticore/languages/od/-/tree/master/src/main/java/de/monticore/dateliterals/_ast
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

