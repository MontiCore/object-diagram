<!-- (c) https://github.com/MontiCore/monticore -->

<!-- Beta-version: This is intended to become a MontiCore stable explanation. -->

# (UML/P) OD4Data

Language for textual object diagrams. In its current state the language is mostly used for a 
data structure in certain projets. It combines the component grammars ODLink and ODAttribute to 
provide an accurate textual representation of UML Object Diagrams. In addition, it offers an 
extended set of types for objects as well as attributes as well as the possibility to use 
expression in certain structures of a model, e.g. attribute values. 
   
The grammar file is [`de.monticore.OD4Data`][OD4Data].

The language uses the following MontiCore languages:  
 [`de.monticore.ODAttribute`][ODAttribute] and 
 [`de.monticore.ODLink`][ODLink]

## Handwritten Extensions
### Symboltable
- Handwritten Symbols, language, and Creator can be found in [`monticore.od4data._symbol`][_symboltable]
### Pretty Printer
- Pretty Printer can be found in [`monticore.od4data.prettyprinter`][prettyprinter]

## Functionality
### CoCos
The CoCos can be found in [`monticore.odbasics._cocos`][_cocos] and are combined
accessible in [`monticore.odbasics._cocos.ODBasicsCoCos`][OD4DataCocos].

The context conditions check different parts of the models, to ensure the semantic correctness
. See each coco for more details.

[OD4Data]: https://git.rwth-aachen.de/monticore/languages/od/-/blob/master/src/main/grammars/de/monticore/OD4Data.mc4
[ODAttribute]: https://git.rwth-aachen.de/monticore/monticore/-/blob/dev/monticore-grammar/src/main/grammars/de/monticore/ODAttribute.mc4
[ODLink]: https://git.rwth-aachen.de/monticore/monticore/-/blob/dev/monticore-grammar/src/main/grammars/de/monticore/ODLink.mc4
[_symboltable]: https://git.rwth-aachen.de/monticore/languages/od/-/tree/master/src/main/java/de/monticore/od4data/_symboltable
[_cocos]: https://git.rwth-aachen.de/monticore/languages/od/-/tree/master/src/main/java/de/monticore/od4data/_cocos
[prettyprinter]: https://git.rwth-aachen.de/monticore/languages/od/-/tree/master/src/main/java/de/monticore/od4data/prettyprinter
[OD4DataCocos]: https://git.rwth-aachen.de/monticore/languages/od/-/tree/master/src/main/java/de/monticore/od4data/_cocos/OD4DataCoCos.java

## Further Information

* [Project root: MontiCore @github](https://github.com/MontiCore/monticore)
* [MontiCore documentation](http://www.monticore.de/)

* [**List of languages**](https://git.rwth-aachen.de/monticore/monticore/-/blob/dev/docs/Languages.md)
* [**MontiCore Core Grammar Library**](https://git.rwth-aachen.de/monticore/monticore/blob/dev/monticore-grammar/src/main/grammars/de/monticore/Grammars.md)
* [Best Practices](BestPractices.md)
* [Publications about MBSE and MontiCore](https://www.se-rwth.de/publications/)

* [Licence definition](https://github.com/MontiCore/monticore/blob/master/00.org/Licenses/LICENSE-MONTICORE-3-LEVEL.md)
