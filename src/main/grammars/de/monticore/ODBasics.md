<!-- (c) https://github.com/MontiCore/monticore -->

<!-- Beta-version: This is intended to become a MontiCore stable grammar. -->

# (UML/P) OD

Language for textual object diagrams. In its current state the language is mostly used for (i) a 
data structure in certain projets (e.g. artifact toolchain)and (ii) as a report format for 
languages developed with MontiCore. For its lastest version the OD language was enhanced with
the possiblility of using expressions in its attributes extending its capabilities.

The grammar file is [`de.monticore.ODBasics`][ODBasicsGrammar].

The OD language the monticoreuages  
 [`de.monticore.types.MCFullGenericTypes`][MCFullGenericGrammar] and
 [`de.monticore.UMLModifier`][MCUMLModifierGrammar] and 
 [`de.monticore.expressions.ExpressionsBasis`][MCExpressionBasicsGrammar]

## Handwritten Extensions
## AST
- Handwritten AST nodes can be found in [`monticore.odbasics._ast`][_ast]
## Symboltable
- Handwritten Symbols, language, and Creator can be found in [`monticore.odbasics._symbol`][_symboltable]
## Pretty Printer
- Pretty Printer can be found in [`monticore.odbasics.prettyprinter`][prettyprinter]

## Functionality
### CoCos
The CoCos can be found in [`monticore.odbasics._cocos`][cocos] and are combined
accessible in [`monticore.odbasics._cocos.ODBasicsCoCos`][ODCoCos].

The context conditions check different parts of the models, to ensure the semantic correctness.

### CLI Application
[`de.monticore.odbasics.ODBasicsCLI`][ODCLI] contains a standalone cli application which 
loads a given OD model.

[ODBasicsGrammar]: https://git.rwth-aachen.de/monticoreuages/od/-/blob/master/src/main/grammars/de/monticore/ODBasics.mc4
[MCFullGenericGrammar]: https://git.rwth-aachen.de/monticore/monticore/-/blob/dev/monticore-grammar/src/main/grammars/de/monticore/types/MCFullGenericTypes.mc4
[MCUMLModifierGrammar]: https://git.rwth-aachen.de/monticore/monticore/-/blob/dev/monticore-grammar/src/main/grammars/de/monticore/UMLModifier.mc4
[MCExpressionBasicsGrammar]: https://git.rwth-aachen.de/monticore/monticore/-/blob/dev/monticore-grammar/src/main/grammars/de/monticore/expressions/ExpressionsBasis.mc4
[_ast]: https://git.rwth-aachen.de/monticoreuages/od/-/tree/master/src%2Fmain%2Fjava%2Fde%2Fmonticore%2Flang%2Fodbasics%2F_ast
[_symboltable]: https://git.rwth-aachen.de/monticoreuages/od/-/tree/master/src%2Fmain%2Fjava%2Fde%2Fmonticore%2Flang%2Fodbasics%2F_symboltable
[prettyprinter]: https://git.rwth-aachen.de/monticoreuages/od/-/tree/master/src%2Fmain%2Fjava%2Fde%2Fmonticore%2Flang%2Fodbasics%2Fprettyprinter
[cocos]: https://git.rwth-aachen.de/monticoreuages/od/-/tree/master/src%2Fmain%2Fjava%2Fde%2Fmonticore%2Flang%2Fodbasics%2F_cocos
[ODCoCos]: https://git.rwth-aachen.de/monticoreuages/od/-/blob/master/src/main/java/de/monticore/odbasics/_cocos/ODBasicsCoCos.java
[ODCLI]: https://git.rwth-aachen.de/monticoreuages/od/-/blob/master/src/main/java/de/monticore/odbasics/ODBasicsCLI.java