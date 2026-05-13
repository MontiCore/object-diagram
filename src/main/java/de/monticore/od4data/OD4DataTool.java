/* (c) https://github.com/MontiCore/monticore */
package de.monticore.od4data;

import de.monticore.io.paths.MCPath;
import de.monticore.od4data._symboltable.IOD4DataArtifactScope;
import de.monticore.od4data._prettyprint.OD4DataFullPrettyPrinter;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.symbols.basicsymbols.BasicSymbolsMill;
import de.se_rwth.commons.logging.Log;
import org.apache.commons.cli.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Command line interface for the OD language and corresponding tooling. Defines, handles, and
 * executes the corresponding command line options and arguments, such as --help
 */
public class OD4DataTool extends OD4DataToolTOP {
  
  protected static final String PARSE_SUCCESSFUL = "Successfully parsed %s\n";
  
  protected static final String CHECK_SUCCESSFUL =
      "Successfully checked the CoCos for class " + "diagram %s\n";
  
  protected static final String CHECK_ERROR = "Error while parsing or CoCo checking";
  
  protected static final String STEXPORT_SUCCESSFUL = "Creation of symbol file %s successful\n";
  
  protected static final String INPUT_FILE_NOT_EXISTENT = "Input file '%s' does not exist\n";

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
        BasicSymbolsMill.initializePrimitives();
        OD4DataMill.globalScope().putTypeSymbolDeSer("de.monticore.cdbasis._symboltable.CDTypeSymbol");
      }
      OD4DataMill.globalScope().setSymbolPath(symbolPath);

      // don't output to stdout when the prettyprint is output to stdout
      final boolean doPrintToStdOut = !(cmd.hasOption("pp") && cmd.getOptionValue("pp") == null);

      // parse input file
      String modelFile = cmd.getOptionValue("i");
      Path modelFilePath = Paths.get(modelFile);
      if (!modelFilePath.toFile().exists()) {
        System.out.printf(INPUT_FILE_NOT_EXISTENT, modelFile);
        return;
      }
      
      ASTODArtifact astodArtifact = parse(modelFile);
      
      if (doPrintToStdOut) {
        System.out.printf(PARSE_SUCCESSFUL, astodArtifact.getObjectDiagram().getName());
      }

      // -option check cocos
      Set<String> cocoOptionValue = new LinkedHashSet<>();
      if (cmd.hasOption("c") && cmd.getOptionValues("c") != null) {
        cocoOptionValue.addAll(Arrays.asList(cmd.getOptionValues("c")));
      }

      // create symbol table
      IOD4DataArtifactScope od4DataArtifactScope = OD4DataToolAPI.createSymbolTable(astodArtifact,
          !cocoOptionValue.contains("intra"));

      // run cocos
      if (cmd.hasOption("c")) {
        if (cocoOptionValue.contains("intra")) {
          OD4DataToolAPI.runAllIntraCoCos(astodArtifact);
        }
        else {
          OD4DataToolAPI.runAllCoCos(astodArtifact);
        }
        
        if (doPrintToStdOut) {
          if (Log.getErrorCount() == 0) {
            System.out.printf(CHECK_SUCCESSFUL, astodArtifact.getObjectDiagram().getName());
          }
          else {
            System.out.println(CHECK_ERROR);
            return;
          }
        }
      }

      // -option pretty print
      if (cmd.hasOption("pp")) {
        String path = cmd.getOptionValue("pp", StringUtils.EMPTY);
        prettyPrint(astodArtifact, path);
      }

      // -option pretty print symboltable
      if (cmd.hasOption("s")) {
        Path symTabPath = storeSymTab(od4DataArtifactScope, modelFilePath,cmd.getOptionValue("s"));
        if (doPrintToStdOut) {
          System.out.printf(STEXPORT_SUCCESSFUL, symTabPath.toAbsolutePath());
        }
      }
    }
    catch (ParseException e) {
      // an unexpected error from the apache CLI parser:
      Log.error("0xA7121 Could not process CLI parameters: " + e.getMessage());
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
    OD4DataFullPrettyPrinter pp = new OD4DataFullPrettyPrinter(new IndentPrinter());
    String od = pp.prettyprint(astodArtifact);
    print(od, file);
  }


  /*=================================================================*/
  /* Part 3: Defining the options incl. help-texts
  /*=================================================================*/

  /**
   * Initializes the standard options for the OD tool.
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
        .desc("Reads the source file (mandatory) and parses the contents as an " + "object diagram")
        .build());

    // model paths
    options.addOption(Option.builder("path")
        .argName("dirlist")
        .numberOfArgs(Option.UNLIMITED_VALUES)
        .hasArg()
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
        .desc("Stores the symbol table of the OD. The default value is `{ODName}.odsym`.")
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
  
  /**
   * prints the symboltable of the given scope out to a file
   *
   * @param as symboltable to store
   * @param modelPath location of the file containing the OD
   * @param symTabPath location of the file or directory containing the printed table
   */
  public Path storeSymTab(IOD4DataArtifactScope as, Path modelPath, String symTabPath) {
    Path targetPath;
    if (symTabPath == null || symTabPath.isBlank()) {
      String symTabName = FilenameUtils.getBaseName(modelPath.toString()) + ".odsym";
      targetPath = modelPath.getParent().resolve(symTabName);
    }
    else {
      targetPath = Paths.get(symTabPath);
      if (targetPath.toFile().exists() && targetPath.toFile().isDirectory()) {
        String symTabName = FilenameUtils.getBaseName(modelPath.toString()) + ".odsym";
        targetPath = targetPath.resolve(symTabName);
      }
    }
    this.storeSymbols(as, targetPath.toString());
    return targetPath;
  }

}
