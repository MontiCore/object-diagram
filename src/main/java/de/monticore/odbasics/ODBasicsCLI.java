// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore

package de.monticore.odbasics;

import de.monticore.odbasics._ast.ASTODArtifact;
import de.monticore.odbasics._cocos.ODBasicsCoCoChecker;
import de.monticore.odbasics._cocos.ODBasicsCoCos;
import de.monticore.odbasics._parser.ODBasicsParser;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by TGr on 29.04.2016.
 */
public class ODBasicsCLI {

  private static final String JAR_NAME = "od-<version>-odBasics-cli.jar";

  private static final String PARSING_SUCCESSFUL = "Parsing Successful!";

  public static void main(String[] args) throws IOException {
    ODBasicsCLI cli = new ODBasicsCLI();
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
    ODBasicsCoCoChecker odCoCoChecker = new ODBasicsCoCos().getCheckerForAllCoCos();

    ODBasicsParser parser = new ODBasicsParser();

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
