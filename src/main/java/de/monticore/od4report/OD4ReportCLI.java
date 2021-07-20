/* (c) https://github.com/MontiCore/monticore */
package de.monticore.od4report;

import de.monticore.io.paths.MCPath;
import de.monticore.od4report._symboltable.IOD4ReportArtifactScope;
import de.monticore.od4report.prettyprinter.OD4ReportFullPrettyPrinter;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.se_rwth.commons.logging.Log;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class OD4ReportCLI extends OD4ReportCLITOP {

  /*=================================================================*/
  /* Part 1: Handling the arguments and options
  /*=================================================================*/

  /**
   * Processes user input from command line and delegates to the corresponding tools.
   *
   * @param args The input parameters for configuring the OD tool.
   */
  @Override
  public void run(String[] args) {
    init();
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
      MCPath symbolPath = new MCPath();
      if (cmd.hasOption("path")) {
        String[] paths = cmd.getOptionValues("path");
        Arrays.stream(paths).forEach(p -> symbolPath.addEntry(Paths.get(p)));
      }

      // parse input file, which is now available
      // (only returns if successful)
      ASTODArtifact astodArtifact = parse(cmd.getOptionValue("i"));

      // create symbol table
      IOD4ReportArtifactScope oD4ReportArtifactScope = OD4ReportTool.createSymbolTable(
        astodArtifact, true);

      // -option check cocos
      Set<String> cocoOptionValue = new HashSet<>();
      if (cmd.hasOption("c") && cmd.getOptionValues("c") != null) {
        cocoOptionValue.addAll(Arrays.asList(cmd.getOptionValues("c")));
      }

      if (cmd.hasOption("c")) {
        if (cocoOptionValue.contains("intra")) {
          OD4ReportTool.runAllIntraCoCos(astodArtifact);
        }
        else {
          OD4ReportTool.runAllCoCos(astodArtifact);
        }
      }

      // -option pretty print
      if (cmd.hasOption("pp")) {
        String path = cmd.getOptionValue("pp", StringUtils.EMPTY);
        prettyPrint(astodArtifact, path);
      }

      // -option pretty print symboltable
      if (cmd.hasOption("s")) {
        String path = cmd.getOptionValue("s", StringUtils.EMPTY);
        storeSymbols(oD4ReportArtifactScope, path);
      }
    }
    catch (ParseException e) {
      // ann unexpected error from the apache CLI parser:
      Log.error("0xA7101 Could not process CLI parameters: " + e.getMessage());
    }

  }

  /*=================================================================*/
  /* Part 2: Executing arguments
  /*=================================================================*/

  /**
   * Prints the contents of the OD-AST to stdout or a specified file.
   *
   * @param astodArtifact The OD-AST to be pretty printed
   * @param file          The target file name for printing the OD artifact. If empty, the content
   *                      is printed to stdout instead
   */
  @Override
  public void prettyPrint(ASTODArtifact astodArtifact, String file) {
    // pretty print AST
    OD4ReportFullPrettyPrinter pp = new OD4ReportFullPrettyPrinter();
    String od = pp.prettyprint(astodArtifact);
    print(od, file);
  }

  /*=================================================================*/
  /* Part 3: Defining the options incl. help-texts
  /*=================================================================*/

  /**
   * Initializes the standard CLI options for the OD tool.
   *
   * @return The CLI options with arguments.
   */
  @Override
  public Options addStandardOptions(Options options) {
    // help dialog
    options.addOption(Option.builder("h").longOpt("help").desc("Prints this help dialog").build());

    // parse input file
    options.addOption(Option.builder("i")
      .longOpt("input")
      .argName("file")
      .hasArg()
      .desc("Reads the source file (mandatory) and parses the contents as an object diagram")
      .build());

    // model paths
    options.addOption(Option.builder("path")
      .argName("dirlist")
      .hasArgs()
      .desc("Sets the artifact path for imported symbols")
      .build());

    // pretty print OD
    options.addOption(Option.builder("pp")
      .longOpt("prettyprint")
      .argName("file")
      .optionalArg(true)
      .numberOfArgs(1)
      .desc("Prints the OD-AST to stdout or the specified file (optional)")
      .build());

    // print OD symtab
    options.addOption(Option.builder("s")
      .longOpt("symboltable")
      .argName("file")
      .optionalArg(true)
      .numberOfArgs(1)
      .desc("Prints the symboltable of the object diagram to stdout or the specified file "
        + "(optional)")
      .build());
    return options;
  }

  @Override
  public Options addAdditionalOptions(Options options) {
    // check cocos
    options.addOption(Option.builder("c")
      .longOpt("coco")
      .optionalArg(true)
      .numberOfArgs(3)
      .desc("Checks the CoCos for the input. Optional arguments are:\n"
        + "-c intra to check only the" + " intra-model CoCos,\n"
        + "-c inter checks also inter-model CoCos,\n" + "-c type "
        + "(default) checks all CoCos.")
      .build());
    return options;
  }
}
