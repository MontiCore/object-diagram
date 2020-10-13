<!-- (c) https://github.com/MontiCore/monticore -->

<!-- Beta-version: This is intended to become a MontiCore stable explanation. -->

# ODBasis -- the foundation for UML/P's Object Diagrams
 
## Handwritten Extensions
### AST
- There is a handwritten AST node extension for `ASTODObject`
  in [`monticore.odbasics._ast`][_ast].

### Symboltable
- TODO: Symbole beschreiben, die importiert exportiert werden, etc.
- Handwritten Symbols, language, and Creator can be found in [`monticore.odbasics._symbol`][_symboltable]

### Pretty Printer
- A pretty Printer can be found in [`monticore.odbasics.prettyprinter`][prettyprinter].

## Functionality
### CoCos
The CoCos can be found in [`monticore.odbasics._cocos`][_cocos] and are combined
accessible in [`monticore.odbasics._cocos.OD4ReportCoCos`][OD4ReportCoCos].

The context conditions check different parts of the models, to ensure the semantic correctness
. See each coco for more details.

[ODBasicsGrammar]: https://git.rwth-aachen.de/monticore/languages/od/-/blob/master/src/main/grammars/de/monticore/ODBasis.mc4
[UMLModifier]: https://git.rwth-aachen.de/monticore/monticore/-/blob/dev/monticore-grammar/src/main/grammars/de/monticore/types/MCFullGenericTypes.mc4
[MCBasicTypes]: https://git.rwth-aachen.de/monticore/monticore/-/blob/dev/monticore-grammar/src/main/grammars/de/monticore/types/MCBasicTypes.mc4
[OOSymbols]: https://git.rwth-aachen.de/monticore/monticore/-/blob/dev/monticore-grammar/src/main/grammars/de/monticore/symbols/OOSymbols.mc4
[CommonExpressions]: https://git.rwth-aachen.de/monticore/monticore/-/blob/dev/monticore-grammar/src/main/grammars/de/monticore/expressions/CommonExpressions.mc4
[_ast]: https://git.rwth-aachen.de/monticore/languages/od/-/tree/master/src/main/java/de/monticore/odbasis/_ast
[_symboltable]: https://git.rwth-aachen.de/monticore/languages/od/-/tree/master/src/main/java/de/monticore/odbasis/_symboltable
[prettyprinter]: https://git.rwth-aachen.de/monticore/languages/od/-/tree/master/src/main/java/de/monticore/odbasis/prettyprinter
[_cocos]: https://git.rwth-aachen.de/monticore/languages/od/-/tree/master/src/main/java/de/monticore/odbasis/_cocos
[ODCoCos]: https://git.rwth-aachen.de/monticore/languages/od/-/blob/master/src/main/java/de/monticore/odbasis/_cocos/ODBasicsCoCos.java

## Further Information

* [Project root: MontiCore @github](https://github.com/MontiCore/monticore)
* [MontiCore documentation](http://www.monticore.de/)

* [**List of languages**](https://git.rwth-aachen.de/monticore/monticore/-/blob/dev/docs/Languages.md)
* [**MontiCore Core Grammar Library**](https://git.rwth-aachen.de/monticore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/Grammars.md)
* [Best Practices](BestPractices.md)
* [Publications about MBSE and MontiCore](https://www.se-rwth.de/publications/)

* [Licence definition](https://github.com/MontiCore/monticore/blob/master/00.org/Licenses/LICENSE-MONTICORE-3-LEVEL.md)
