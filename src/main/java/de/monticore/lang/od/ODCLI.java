package de.monticore.lang.od;

import java.io.IOException;
import java.util.Optional;

import de.monticore.lang.od._ast.ASTODCompilationUnit;
import de.monticore.lang.od._parser.ODParser;

/**
 * Created by TGr on 29.04.2016.
 */
public class ODCLI {

  private static String JAR_NAME = "od-<Version>-jar-with-dependencies.jar";
  private static String PARSING_SUCCESSFUL = "Parsing Successful!";

  public static void main(String[] args) throws IOException {
    ODCLI cli = new ODCLI();
    cli.run(args);
  }

  public void run(String[] args) throws IOException {
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
    ODParser parser = new ODParser();
    Optional<ASTODCompilationUnit> odDef = parser.parseODCompilationUnit(file);

    System.out.println(PARSING_SUCCESSFUL);
  }

  private void printUsage() {
    System.out.println("Usage: " + JAR_NAME + " [OPTION] [ODFILE]");
    System.out.println("Parses the ODFILE. Displays parsing error or \""+ PARSING_SUCCESSFUL +"\" message\n");
    System.out.println("  -h   display this help and exit");
  }

  private void printWrongUsage() {
    System.out.println("ODCLI: wrong usage");
    System.out.println("For more information type: ODCLI -h");
  }
}
