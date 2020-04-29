# (UML/P) OD
Language for textual object diagrams. In its current state the language is mostly used for (i) a 
data structure in certain projets (e.g. artifact toolchain)and (ii) as a report format for 
languages developed with MontiCore. For its lastest version the OD language was enhanced with
the possiblility of using expressions in its attributes extending its capabilities.

The grammar file is [`de.monticore.lang.ODBasics`][ODBasicsGrammar].

The OD language the MontiCore languages  
 [`de.monticore.types.MCFullGenericTypes`][MCFullGenericGrammar] and
 [`de.monticore.UMLModifier`][MCUMLModifierGrammar] and 
 [`de.monticore.expressions.ExpressionsBasis`][MCExpressionBasicsGrammar]

## Handwritten Extensions
## AST
- Handwritten AST nodes can be found in [`monticore.lang.odbasics._ast`][_ast]
## Symboltable
- Handwritten Symbols, language, and Creator can be found in [`monticore.lang.odbasics._symbol`][_symboltable]
## Pretty Printer
- Pretty Printer can be found in [`monticore.lang.odbasics._prettyprinter`][prettyprinter]

## Functionality
### CoCos
The CoCos can be found in [`monticore.lang.odbasics.cocos`][cocos] and are combined
accessible in [`monticore.lang.odbasics.cocos.ODBasicsCoCos`][ODCoCos].

The context conditions check different parts of the models, to ensure the semantic correctness.

### CLI Application
[`de.monticore.lang.odbasics.ODBasicsCLI`][ODCLI] contains a standalone cli application which 
loads a given OD model.

[ODBasicsGrammar]: https://git.rwth-aachen.de/monticore/languages/od/-/blob/master/src/main/grammars/de/monticore/lang/ODBasics.mc4
[MCFullGenericGrammar]: https://git.rwth-aachen.de/monticore/monticore/-/blob/dev/monticore-grammar/src/main/grammars/de/monticore/types/MCFullGenericTypes.mc4
[MCUMLModifierGrammar]: https://git.rwth-aachen.de/monticore/monticore/-/blob/dev/monticore-grammar/src/main/grammars/de/monticore/UMLModifier.mc4
[MCExpressionBasicsGrammar]: https://git.rwth-aachen.de/monticore/monticore/-/blob/dev/monticore-grammar/src/main/grammars/de/monticore/expressions/ExpressionsBasis.mc4
[_ast]: https://git.rwth-aachen.de/monticore/languages/od/-/tree/master/src%2Fmain%2Fjava%2Fde%2Fmonticore%2Flang%2Fodbasics%2F_ast
[_symboltable]: https://git.rwth-aachen.de/monticore/languages/od/-/tree/master/src%2Fmain%2Fjava%2Fde%2Fmonticore%2Flang%2Fodbasics%2F_symboltable
[prettyprinter]: https://git.rwth-aachen.de/monticore/languages/od/-/tree/master/src%2Fmain%2Fjava%2Fde%2Fmonticore%2Flang%2Fodbasics%2Fprettyprinter
[cocos]: https://git.rwth-aachen.de/monticore/languages/od/-/tree/master/src%2Fmain%2Fjava%2Fde%2Fmonticore%2Flang%2Fodbasics%2Fcocos
[ODCoCos]: https://git.rwth-aachen.de/monticore/languages/od/-/blob/master/src/main/java/de/monticore/lang/odbasics/cocos/ODBasicsCoCos.java
[ODCLI]: https://git.rwth-aachen.de/monticore/languages/od/-/blob/master/src/main/java/de/monticore/lang/odbasics/ODBasicsCLI.java
