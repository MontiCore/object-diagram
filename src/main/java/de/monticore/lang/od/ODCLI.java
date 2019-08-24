/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.od;

import java.io.IOException;
import java.util.Optional;

import de.monticore.lang.od._ast.ASTODArtifact;
import de.monticore.lang.od._cocos.ODCoCoChecker;
import de.monticore.lang.od._cocos.ODCoCos;
import de.monticore.lang.od._parser.ODParser;
import de.monticore.lang.od._symboltable.ODLanguage;
import de.monticore.symboltable.ResolvingConfiguration;

/**
 * Created by TGr on 29.04.2016.
 */
public class ODCLI {

  private static String JAR_NAME = "od-<version>-tool.jar";

  private static String PARSING_SUCCESSFUL = "Parsing Successful!";

  private static ODLanguage odLanguage;

  private static ResolvingConfiguration resolverConfiguration;

  private static ODCoCoChecker odCoCoChecker;

  public static void main(String[] args) throws IOException {
    ODCLI cli = new ODCLI();
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

    odLanguage = new ODLanguage();

    resolverConfiguration = new ResolvingConfiguration();
    resolverConfiguration.addDefaultFilters(odLanguage.getResolvingFilters());

    odCoCoChecker = new ODCoCos().getChecker();

    ODParser parser = odLanguage.getParser();

    Optional<ASTODArtifact> odDef = parser.parseODArtifact(file);

    if (odDef.isPresent()) {
      odCoCoChecker.checkAll(odDef.get());
    }

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
