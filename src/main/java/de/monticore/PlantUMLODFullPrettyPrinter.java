package de.monticore;

import de.monticore.literals.mccommonliterals._prettyprint.MCCommonLiteralsPrettyPrinter;
import de.monticore.od4development.OD4DevelopmentMill;
import de.monticore.od4development._visitor.OD4DevelopmentTraverser;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.prettyprint.IndentPrinter;

public class PlantUMLODFullPrettyPrinter {
    protected OD4DevelopmentTraverser traverser;
    protected IndentPrinter printer;

    public PlantUMLODFullPrettyPrinter() {
        printer = new IndentPrinter();
        traverser = OD4DevelopmentMill.traverser();
        MCCommonLiteralsPrettyPrinter literalsPrettyPrinter = new MCCommonLiteralsPrettyPrinter(printer, false);
        traverser.add4MCCommonLiterals(literalsPrettyPrinter);
        traverser.setMCCommonLiteralsHandler(literalsPrettyPrinter);
        PlantUMLODBasisPrettyPrinter plantUmlOdBasisPrettyPrinter = new PlantUMLODBasisPrettyPrinter(printer);
        traverser.add4ODBasis(plantUmlOdBasisPrettyPrinter);
        traverser.setODBasisHandler(plantUmlOdBasisPrettyPrinter);
        PlantUMLODLinkPrettyPrinter plantUmlOdLinkPrettyPrinter = new PlantUMLODLinkPrettyPrinter(printer);
        traverser.add4ODLink(plantUmlOdLinkPrettyPrinter);
        traverser.setODLinkHandler(plantUmlOdLinkPrettyPrinter);
    }

    public String prettyprint(ASTODArtifact a) {
        a.accept(traverser);
        return printer.getContent();
    }
}
