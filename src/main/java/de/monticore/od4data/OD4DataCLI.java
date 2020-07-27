// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore

package de.monticore.od4data;

import de.monticore.od4data._parser.OD4DataParser;
import de.monticore.od4data.prettyprinter.OD4DataPrettyPrinterDelegator;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.se_rwth.commons.logging.Log;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class OD4DataCLI {

  private BufferedReader reader;

  private Optional<ASTODArtifact> astodArtifact;

  private static final String separator = " ";

  private static final String[] help = { "-h" };

  private static final String[] parse = { "parse" };

  private static final String[] parseFile = { "parse", "-f" };

  private static final String[] print = { "print" };

  private static final String[] printToFile = { "print", "-f" };

  private static final String[] quit = { "-q" };

  public static void main(String[] args) throws IOException {
    OD4DataCLI cli = new OD4DataCLI();
    cli.run();
  }

  /**
   * Main run method of the CLI instance. Initializes the tool and passes arguments to main program
   * loop.
   *
   * @throws IOException
   */
  private void run() throws IOException {
    init();
    printHelp();
    handleArgs();
  }

  /**
   * Initializes the CLI tool. Sets up the logger as well as available tooling.
   */
  private void init() {
    System.out.println("##### OD4Data command line tool #####");
    Log.init();
    Log.enableFailQuick(false);
    reader = new BufferedReader(new InputStreamReader(System.in));
    astodArtifact = Optional.empty();
  }

  /**
   * Main program loop. Processes user input from command line and delegates to the corresponding
   * tools.
   *
   * @throws IOException
   */
  private void handleArgs() throws IOException {
    boolean exit = false;

    while (!exit) {
      String input = input();

      if (input.isEmpty()) {
        // do nothing
      }
      else if (hasFormat(input, help)) {
        printHelp();
      }
      else if (hasFormat(input, parseFile)) {
        parseFile(input.split(separator, 3)[2]);
      }
      else if (hasFormat(input, parse)) {
        parseString(input.split(separator, 2)[1]);
      }
      else if (hasFormat(input, printToFile)) {
        print(input.split(separator, 3)[2]);
      }
      else if (hasFormat(input, print) && partsMatch(input, 1)) {
        print("");
      }
      else if (hasFormat(input, quit) && partsMatch(input, 1)) {
        exit = true;
      }
      else {
        printHelp();
      }

    }
  }

  /**
   * Parses the contents of a given file as an object diagram.
   *
   * @param path The path to the OD4Data-file as String
   */
  private void parseFile(String path) {
    refresh();
    Path model = Paths.get(path);
    OD4DataParser parser = new OD4DataParser();
    try {
      astodArtifact = parser.parse(model.toString());
    }
    catch (IOException e) {
      System.out.println("Error: File not found.");
    }
  }

  /**
   * Parses an input String as an object diagram.
   *
   * @param objectDiagram The input String
   * @throws IOException
   */
  private void parseString(String objectDiagram) throws IOException {
    refresh();
    OD4DataParser parser = new OD4DataParser();
    astodArtifact = parser.parse_StringODArtifact(objectDiagram);
  }

  /**
   * Prints the contents of the ODArifact-AST to command line or a specified file.
   *
   * @param file The target file for printing the ODArtifact artifact. If empty, the artifact is
   *             printed to the command line instead
   */
  private void print(String file) {
    // check if AST is available
    if (!astodArtifact.isPresent()) {
      System.out
          .println("Error: No object diagram available. First parse a valid OD4Data artifact.");
      return;
    }

    // pretty print AST
    OD4DataPrettyPrinterDelegator pp = new OD4DataPrettyPrinterDelegator();
    String diagram = pp.prettyprint(astodArtifact.get());

    // print to console or file
    if (file.isEmpty()) {
      System.out.println(diagram);
    }
    else {
      FileWriter writer;
      try {
        writer = new FileWriter(file);
        writer.write(diagram);
        writer.close();
      }
      catch (IOException e) {
        System.out.println("Error: Could not write to file " + file);
      }
    }
  }

  /**
   * Refreshes the CLI tool on model changes. Resets the stored ODArtifact-AST and instantiates
   * available tooling to enable efficient computation.
   */
  private void refresh() {
    astodArtifact = Optional.empty();
  }

  /**
   * Requests command line input.
   *
   * @return The given input line from command line
   * @throws IOException
   */
  private String input() throws IOException {
    System.out.print("> ");
    String input = reader.readLine();
    return input;
  }

  /**
   * Checks the format of a command line input to derive the correct behavior.
   *
   * @param input  The input String from command line.
   * @param format A predefined format of input
   * @return true, ifthe input matches the format, false otherwise
   */
  private boolean hasFormat(String input, String[] format) {
    String[] tmp = input.split(separator);
    for (int i = 0; i < format.length; i++) {
      if (tmp.length <= i || !format[i].equals(tmp[i])) {
        return false;
      }
    }
    return true;
  }

  /**
   * Checks whether the number of parts matches expected number concerning a specific format.
   *
   * @param input The input String
   * @param parts The expected number of parts
   * @return true if parts matches the expected number, false otherwise
   */
  private boolean partsMatch(String input, int parts) {
    return input.split(separator).length == parts;
  }

  /**
   * Prints the usage of the CLI. Contains available commands with possible parameters an their
   * explanations.
   */
  private void printHelp() {
    System.out.println("-h                    Opens this help dialoge");
    System.out.println("parse *input*         Parses the given input as an object diagram");
    System.out.println(
        "parse -f *source*     Reads the given source file and parses the contents as am object "
            + "diagram");
    System.out.println("print                 Prints the OD4Data-AST");
    System.out.println("print -f *target*     Prints the OD4Data-AST to the specified file");
    System.out.println("-q                    Quit");
  }

}
