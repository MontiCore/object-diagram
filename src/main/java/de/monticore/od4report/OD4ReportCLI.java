// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore

package de.monticore.od4report;

import de.monticore.od4report._cocos.OD4ReportCoCoChecker;
import de.monticore.od4report._cocos.OD4ReportCoCos;
import de.monticore.od4report._parser.OD4ReportParser;
import de.monticore.odbasics._ast.ASTODArtifact;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by TGr on 29.04.2016.
 */
public class OD4ReportCLI {

  private static final String JAR_NAME = "od-<version>-od4Report-cli.jar";

  private static final String PARSING_SUCCESSFUL = "Parsing Successful!";

  public static void main(String[] args) throws IOException {
    OD4ReportCLI cli = new OD4ReportCLI();
    cli.run(args);
  }

  private void run(String[] args) throws IOException {
    handleArgs(args);
  }

  private void handleArgs(String[] args) throws IOException {
    if (args.length != 1) {
      printWrongUsage();
    }
    else {
      if ("-h".equals(args[0])) {
        printUsage();
      }
      else {
        doParse(args[0]);
      }
    }
  }

  private void doParse(String file) throws IOException {
    OD4ReportCoCoChecker odCoCoChecker = new OD4ReportCoCos().getCheckerForAllCoCos();

    OD4ReportParser parser = new OD4ReportParser();

    Optional<ASTODArtifact> odDef = parser.parseODArtifact(file);

    odDef.ifPresent(odCoCoChecker::checkAll);

    System.out.println(PARSING_SUCCESSFUL);
  }

  private void printUsage() {
    System.out.println("Usage: " + JAR_NAME + " [OPTION] [ODFILE]");
    System.out.println(
        "Parses the ODFILE. Displays parsing error or \"" + PARSING_SUCCESSFUL + "\" message\n");
    System.out.println("  -h   display this help and exit");
  }

  private void printWrongUsage() {
    System.out.println("ODCLI: wrong usage");
    System.out.println("For more information type: ODCLI -h");
  }

}
