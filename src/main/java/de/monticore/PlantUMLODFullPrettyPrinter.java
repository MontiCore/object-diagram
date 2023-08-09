package de.monticore;

import de.monticore.dateliterals._prettyprint.DateLiteralsPrettyPrinter;
import de.monticore.literals.mccommonliterals._prettyprint.MCCommonLiteralsPrettyPrinter;
import de.monticore.od4report.OD4ReportMill;
import de.monticore.od4report._visitor.OD4ReportTraverser;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.mccollectiontypes._prettyprint.MCCollectionTypesPrettyPrinter;

/**
 * Base class initializing the individual pretty printers.
 * Must override the visitors, handlers for each individual pretty printer
 */
public class PlantUMLODFullPrettyPrinter {

    protected OD4ReportTraverser traverser;
    protected IndentPrinter printer;
    
    public PlantUMLODFullPrettyPrinter() {
        printer = new IndentPrinter();
        traverser = OD4ReportMill.traverser();
        MCCommonLiteralsPrettyPrinter literalsPrettyPrinter = new MCCommonLiteralsPrettyPrinter(printer, false);
        traverser.add4MCCommonLiterals(literalsPrettyPrinter);
        traverser.setMCCommonLiteralsHandler(literalsPrettyPrinter);
        DateLiteralsPrettyPrinter dateLiteralsPrettyPrinter = new DateLiteralsPrettyPrinter(printer, false);
        traverser.add4DateLiterals(dateLiteralsPrettyPrinter);
        traverser.setDateLiteralsHandler(dateLiteralsPrettyPrinter);
        PlantUMLODBasisPrettyPrinter plantUmlOdBasisPrettyPrinter = new PlantUMLODBasisPrettyPrinter(printer);
        traverser.add4ODBasis(plantUmlOdBasisPrettyPrinter);
        traverser.setODBasisHandler(plantUmlOdBasisPrettyPrinter);
        PlantUMLODLinkPrettyPrinter plantUmlOdLinkPrettyPrinter = new PlantUMLODLinkPrettyPrinter(printer);
        traverser.add4ODLink(plantUmlOdLinkPrettyPrinter);
        traverser.setODLinkHandler(plantUmlOdLinkPrettyPrinter);
        PlantUMLODReportPrettyPrinter plantUMLODReportPrettyPrinter = new PlantUMLODReportPrettyPrinter(printer);
        traverser.add4OD4Report(plantUMLODReportPrettyPrinter);
        traverser.setOD4ReportHandler(plantUMLODReportPrettyPrinter);
    }
    
    /**
     * Pretty print the parsed OD.
     * @param a the parsed OD artifact
     * @return indented pretty printed PlantUML syntax
     */
    public String prettyprint(ASTODArtifact a) {
        a.accept(traverser);
        return printer.getContent();
    }
}
