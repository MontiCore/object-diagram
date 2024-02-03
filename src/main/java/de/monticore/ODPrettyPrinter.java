package de.monticore;

import de.monticore.od4report._parser.OD4ReportParser;
import de.monticore.odbasis._ast.ASTODArtifact;

import java.io.IOException;
import java.util.Optional;

public class ODPrettyPrinter {

    public  String prettyPrintModel(String model) throws IOException {
        OD4ReportParser parser = new OD4ReportParser();
        Optional<ASTODArtifact> optOD = parser.parse(model);

        PlantUMLODFullPrettyPrinter prettyPrinter = new PlantUMLODFullPrettyPrinter();
        String result = prettyPrinter.prettyprint(optOD.get());
        System.out.println(result);
        return result;
    }
}
