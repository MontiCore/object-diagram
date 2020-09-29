// (c) https://github.com/MontiCore/monticore

package de.monticore.od4report;

import de.monticore.io.paths.ModelPath;
import de.monticore.od4report._parser.OD4ReportParser;
import de.monticore.od4report._symboltable.IOD4ReportArtifactScope;
import de.monticore.od4report._symboltable.OD4ReportScopeDeSer;
import de.monticore.od4report.prettyprinter.OD4ReportPrettyPrinterDelegator;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.se_rwth.commons.logging.Log;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;

/**
 * Created by TGr on 29.04.2016.
 */
public class OD4ReportCLI {

  private final static String EXTENSION = "od";

  /*=================================================================*/
  /* Part 1: Handling the arguments and options
  /*=================================================================*/

  /**
   * Main method that is called from command line and runs the OD tool.
   *
   * @param args The input parameters for configuring the OD tool.
   */
  public static void main(String[] args) {
    OD4ReportCLI cli = new OD4ReportCLI();
    // initialize logging with standard logging
    Log.init();
    cli.handleArgs(args);
  }

  /**
   * Processes user input from command line and delegates to the corresponding tools.
   *
   * @param args The input parameters for configuring the OD tool.
   */
  public void handleArgs(String[] args) {

    Options options = initOptions();

    try {
      // create CLI parser and parse input options from command line
      CommandLineParser cliparser = new DefaultParser();
      CommandLine cmd = cliparser.parse(options, args);

      // help: when --help
      if (cmd.hasOption("h")) {
        printHelp(options);
        // do not continue, when help is printed
        return;
      }

      // if -i input is missing: also print help and stop
      if (!cmd.hasOption("i")) {
        printHelp(options);
        // do not continue, when help is printed
        return;
      }

      // if -path is set: save the model paths
      ModelPath modelPath = new ModelPath();
      if (cmd.hasOption("path")) {
        String[] paths = cmd.getOptionValues("path");
        Arrays.stream(paths).forEach(p -> modelPath.addEntry(Paths.get(p)));
      }

      // parse input file, which is now available
      // (only returns if successful)
      ASTODArtifact astodArtifact = parseFile(cmd.getOptionValue("i"));

      // create symbol table
      IOD4ReportArtifactScope oD4ReportArtifactScope = OD4ReportTool
          .createSymbolTable(astodArtifact,
              OD4ReportMill.oD4ReportGlobalScopeBuilder().setModelPath(modelPath)
                  .setModelFileExtension(EXTENSION).build());

      // -option pretty print
      if (cmd.hasOption("pp")) {
        String path = cmd.getOptionValue("pp", StringUtils.EMPTY);
        prettyPrint(astodArtifact, path);
      }

      // -otion pretty print symboltable
      if (cmd.hasOption("s")) {
        String path = cmd.getOptionValue("s", StringUtils.EMPTY);
        prettyPrintST(oD4ReportArtifactScope, path);
      }
    }
    catch (ParseException e) {
      // ann unexpected error from the apache CLI parser:
      Log.error("0xA7101 Could not process CLI parameters: " + e.getMessage());
    }

  }

  /**
   * Processes user input from command line and delegates to the corresponding tools.
   *
   * @param options The input parameters and options.
   */
  public void printHelp(Options options) {
    HelpFormatter formatter = new HelpFormatter();
    formatter.setWidth(80);
    formatter.printHelp("ODCLI", options);
  }

  /*=================================================================*/
  /* Part 2: Executing arguments
  /*=================================================================*/

  /**
   * Parses the contents of a given file as an object diagram.
   *
   * @param path The path to the OD-file as String
   */
  public ASTODArtifact parseFile(String path) {
    Optional<ASTODArtifact> astodArtifact = Optional.empty();
    try {
      Path model = Paths.get(path);
      OD4ReportParser parser = new OD4ReportParser();
      astodArtifact = parser.parse(model.toString());
    }
    catch (IOException | NullPointerException e) {
      Log.error("0xA7102 Input file " + path + " not found.");
    }
    return astodArtifact.get();
  }

  /**
   * Prints the contents of the OD-AST to stdout or a specified file.
   *
   * @param astodArtifact The OD-AST to be pretty printed
   * @param file          The target file name for printing the OD artifact. If empty, the content
   *                      is printed to stdout instead
   */
  public void prettyPrint(ASTODArtifact astodArtifact, String file) {
    // pretty print AST
    OD4ReportPrettyPrinterDelegator pp = new OD4ReportPrettyPrinterDelegator();
    String od = pp.prettyprint(astodArtifact);
    print(od, file);
  }

  /**
   * Stores the contents of the symboltable to stdout or a specific file.
   *
   * @param OD4ReportArtifactScope ArtiactScope of object diagram
   * @param file                   Output file to store symboltable in.
   */
  public void prettyPrintST(IOD4ReportArtifactScope OD4ReportArtifactScope, String file) {
    // serializes the symboltable
    OD4ReportScopeDeSer odBasicsScopeDeSer = new OD4ReportScopeDeSer();

    if (StringUtils.isEmpty(file)) {
      System.out.println(odBasicsScopeDeSer.serialize(OD4ReportArtifactScope));
    }
    else {
      odBasicsScopeDeSer.store(OD4ReportArtifactScope, Paths.get(file).toString());
    }
  }

  /**
   * Extracts the model name from a given file name. The model name corresponds to the unqualified
   * file name without file extension.
   *
   * @param file The path to the input file
   * @return The extracted model name
   */
  public String getModelNameFromFile(String file) {
    String modelName = new File(file).getName();
    // cut file extension if present
    if (modelName.length() > 0) {
      int lastIndex = modelName.lastIndexOf(".");
      if (lastIndex != -1) {
        modelName = modelName.substring(0, lastIndex);
      }
    }
    return modelName;
  }

  /**
   * Prints the given content to a target file (if specified) or to stdout (if the file is
   * Optional.empty()).
   *
   * @param content The String to be printed
   * @param path    The target path to the file for printing the content. If empty, the content is
   *                printed to stdout instead
   */
  public void print(String content, String path) {
    // print to stdout or file
    if (path.isEmpty()) {
      System.out.println(content);
    }
    else {
      File f = new File(path);
      // create directories (logs error otherwise)
      f.getAbsoluteFile().getParentFile().mkdirs();

      FileWriter writer;
      try {
        writer = new FileWriter(f);
        writer.write(content);
        writer.close();
      }
      catch (IOException e) {
        Log.error("0xA7105 Could not write to file " + f.getAbsolutePath());
      }
    }
  }

  /*=================================================================*/
  /* Part 3: Defining the options incl. help-texts
  /*=================================================================*/

  /**
   * Initializes the available CLI options for the OD tool.
   *
   * @return The CLI options with arguments.
   */
  protected Options initOptions() {
    Options options = new Options();

    // help dialog
    options.addOption(Option.builder("h").longOpt("help").desc("Prints this help dialog").build());

    // parse input file
    options.addOption(Option.builder("i").longOpt("input").argName("file").hasArg()
        .desc("Reads the source file (mandatory) and parses the contents as an object diagram")
        .build());

    // model paths
    options.addOption(
        Option.builder("path").argName("dirlist").numberOfArgs(Option.UNLIMITED_VALUES).hasArg()
            .desc("Sets the artifact path for imported symbols").build());

    // pretty print OD
    options.addOption(Option.builder("pp").longOpt("prettyprint").argName("file").optionalArg(true)
        .numberOfArgs(1).desc("Prints the OD-AST to stdout or the specified file (optional)")
        .build());

    // print OD symtab
    options.addOption(
        Option.builder("s").longOpt("symboltable").argName("file").optionalArg(true).numberOfArgs(1)
            .desc("Prints the symboltable of the object diagram to stdout or the specified file "
                + "(optional)").build());

    return options;
  }

}
