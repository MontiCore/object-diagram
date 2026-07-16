/* (c) https://github.com/MontiCore/monticore */
package de.monticore.od4report;

import de.monticore.grammar.grammar._symboltable.AdditionalAttributeSymbolDeSer;
import de.monticore.grammar.grammar._symboltable.MCGrammarSymbolDeSer;
import de.monticore.grammar.grammar._symboltable.ProdSymbolDeSer;
import de.monticore.grammar.grammar._symboltable.RuleComponentSymbolDeSer;
import de.monticore.io.paths.MCPath;
import de.monticore.javalight._symboltable.JavaMethodSymbolDeSer;
import de.monticore.od4report._prettyprint.OD4ReportFullPrettyPrinter;
import de.monticore.od4report._symboltable.IOD4ReportArtifactScope;
import de.monticore.od4report._symboltable.IOD4ReportGlobalScope;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.statements.mclowlevelstatements._symboltable.LabelSymbolDeSer;
import de.monticore.symbols.basicsymbols.BasicSymbolsMill;
import de.monticore.symbols.basicsymbols._symboltable.*;
import de.monticore.symbols.oosymbols._symboltable.FieldSymbolDeSer;
import de.monticore.symbols.oosymbols._symboltable.MethodSymbolDeSer;
import de.monticore.symbols.oosymbols._symboltable.OOTypeSymbolDeSer;
import de.se_rwth.commons.logging.Log;
import org.apache.commons.cli.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * OD4Report tool for parsing, checking, pretty printing, and exporting OD4Report models.
 */
public class OD4ReportTool extends OD4ReportToolTOP {
  
  protected static final String PARSE_SUCCESSFUL = "Successfully parsed %s";
  
  protected static final String CHECK_SUCCESSFUL =
      "Successfully checked the CoCos for class diagram %s";
  
  protected static final String CHECK_ERROR = "0x0D023 Error while processing the object diagram";

  protected static final String COCO_OPTION_INVALID =
      "0x0D024 Invalid argument '%s' for option -c. Allowed values are: intra, inter.";

  protected static final String COCO_OPTION_TOO_MANY_ARGS =
      "0x0D025 Option -c accepts at most one argument: intra or inter.";
  
  protected static final String STEXPORT_SUCCESSFUL = "Creation of symbol file %s successful";
  
  protected static final String INPUT_FILE_NOT_EXISTENT = "0x0D022 Input file '%s' does not exist";
  
  /*=================================================================*/
  /* Part 1: Handling the arguments and options
  /*=================================================================*/
  
  /**
   * Processes CLI arguments and executes parsing, symbol table creation, CoCo checks, pretty
   * printing and symbol export.
   * <pre>
   * <ul>
   *     <li>{@code -h}/{@code --help}: prints the help dialog and exits.</li>
   *     <li>{@code -i}/{@code --input <file>}: sets the mandatory input OD file to parse.</li>
   *     <li>{@code -symtypes}/{@code --symboltypes <sym> <deser> ...}: registers custom symbol
   *         deserializers for foreign-language symbols.</li>
   *     <li>{@code -path <dirlist>}: sets one or more symbol path entries for imported symbols.</li>
    *     <li>{@code -c}/{@code --coco [intra|inter]}: runs all CoCos by default, or only
    *         intra/inter CoCos when specified.</li>
   *     <li>{@code -pp}/{@code --prettyprint [file]}: pretty prints the AST to stdout or the
   *         optional file.</li>
   *     <li>{@code -s}/{@code --symboltable [file]}: writes the symbol table to the optional file
   *         or to the default location.</li>
   * </ul>
   * </pre>
   *
   * @param args command line arguments
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
      
      // don't output to stdout when the prettyprint is output to stdout
      final boolean doPrintToStdOut = !(cmd.hasOption("pp") && cmd.getOptionValue("pp") == null);
      
      // if -symbols is set: Add symbol types from file to global scope
      if (cmd.hasOption("symtypes")) {
        String[] cmdVals = cmd.getOptionValues("symtypes");
        if (cmdVals == null || cmdVals.length == 0) {
          Log.warn(
              "No arguments provided for parameter -symboltypes. Skipping custom symbol deserializer mapping.");
        }
        else {
          if (cmdVals.length % 2 != 0) {
            Log.warn("Odd number of arguments for parameter -symboltypes! Ignoring last argument.");
          }
          OD4ReportMill.reset();
          OD4ReportMill.init();
          OD4ReportMill.globalScope().clear();
          BasicSymbolsMill.initializePrimitives();
          IOD4ReportGlobalScope gs = OD4ReportMill.globalScope();
          for (int i = 0; i < cmdVals.length - 1; i += 2) {
            switch (cmdVals[i + 1]) {
              case "TypeSymbolDeSer" -> gs.putSymbolDeSer(cmdVals[i], new TypeSymbolDeSer());
              case "DiagramSymbolDeSer" -> gs.putSymbolDeSer(cmdVals[i], new DiagramSymbolDeSer());
              case "FunctionSymbolDeSer" ->
                  gs.putSymbolDeSer(cmdVals[i], new FunctionSymbolDeSer());
              case "TypeVarSymbolDeSer" -> gs.putSymbolDeSer(cmdVals[i], new TypeVarSymbolDeSer());
              case "VariableSymbolDeSer" ->
                  gs.putSymbolDeSer(cmdVals[i], new VariableSymbolDeSer());
              case "FieldSymbolDeSer" -> gs.putSymbolDeSer(cmdVals[i], new FieldSymbolDeSer());
              case "MethodSymbolDeSer" -> gs.putSymbolDeSer(cmdVals[i], new MethodSymbolDeSer());
              case "OOTypeSymbolDeSer" -> gs.putSymbolDeSer(cmdVals[i], new OOTypeSymbolDeSer());
              case "MCGrammarSymbolDeSer" ->
                  gs.putSymbolDeSer(cmdVals[i], new MCGrammarSymbolDeSer());
              case "AdditionalAttributeSymbolDeSer" ->
                  gs.putSymbolDeSer(cmdVals[i], new AdditionalAttributeSymbolDeSer());
              case "ProdSymbolDeSer" -> gs.putSymbolDeSer(cmdVals[i], new ProdSymbolDeSer());
              case "RuleComponentSymbolDeSer" ->
                  gs.putSymbolDeSer(cmdVals[i], new RuleComponentSymbolDeSer());
              case "JavaMethodSymbolDeSer" ->
                  gs.putSymbolDeSer(cmdVals[i], new JavaMethodSymbolDeSer());
              case "LabelSymbolDeSer" -> gs.putSymbolDeSer(cmdVals[i], new LabelSymbolDeSer());
              default -> {
                Log.warn(cmdVals[i + 1]
                    + " is not a valid Symbol Deserializer and has been assumed as TypeSymbolDeSer");
                gs.putSymbolDeSer(cmdVals[i], new TypeSymbolDeSer());
              }
            }
          }
        }
      }
      
      // if -path is set: save the model paths
      MCPath symbolPath = new MCPath();
      if (cmd.hasOption("path")) {
        String[] paths = cmd.getOptionValues("path");
        Arrays.stream(paths).forEach(p -> symbolPath.addEntry(Paths.get(p)));
      }
      OD4ReportMill.globalScope().setSymbolPath(symbolPath);
      
      // parse input file
      String modelFile = cmd.getOptionValue("i");
      Path modelFilePath = Paths.get(modelFile);
      if (!modelFilePath.toFile().exists()) {
        Log.error(String.format(INPUT_FILE_NOT_EXISTENT, modelFile));
        return;
      }
      
      ASTODArtifact astodArtifact = parse(modelFile);
      
      if (doPrintToStdOut) {
        Log.info(String.format(PARSE_SUCCESSFUL, astodArtifact.getObjectDiagram().getName()),
            getClass().getName());
      }
      
      // create symbol table
      IOD4ReportArtifactScope oD4ReportArtifactScope =
          OD4ReportToolAPI.createSymbolTable(astodArtifact);
      
      OD4ReportToolAPI.completeSymbolTable(astodArtifact, true);
      
      // run cocos
      if (cmd.hasOption("c")) {
        String[] cocoArgs = cmd.getOptionValues("c");
        if (cocoArgs == null || cocoArgs.length == 0) {
          OD4ReportToolAPI.runAllCoCos(astodArtifact);
        }
        else if (cocoArgs.length == 1) {
          String cocoArg = cocoArgs[0];
          if ("intra".equals(cocoArg)) {
            OD4ReportToolAPI.runAllIntraCoCos(astodArtifact);
          }
          else if ("inter".equals(cocoArg)) {
            OD4ReportToolAPI.runAllCoCos(astodArtifact);
          }
          else {
            Log.error(String.format(COCO_OPTION_INVALID, cocoArg));
            return;
          }
        }
        else {
          Log.error(COCO_OPTION_TOO_MANY_ARGS);
          return;
        }

        if (doPrintToStdOut) {
          if (Log.getErrorCount() == 0) {
            Log.info(String.format(CHECK_SUCCESSFUL, astodArtifact.getObjectDiagram().getName()),
                getClass().getName());
          }
          else {
            Log.error(CHECK_ERROR);
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
        Path symTabPath =
            storeSymTab(oD4ReportArtifactScope, modelFilePath, cmd.getOptionValue("s"));
        if (doPrintToStdOut) {
          Log.info(String.format(STEXPORT_SUCCESSFUL, symTabPath.toAbsolutePath()),
              getClass().getName());
        }
      }
    }
    catch (ParseException e) {
      // an unexpected error from the apache CLI parser:
      Log.error("0xA7110 Could not process CLI parameters: " + e.getMessage());
    }
    
  }
  
  /*=================================================================*/
  /* Part 2: Executing arguments
  /*=================================================================*/
  
  /**
   * Pretty prints the OD artifact either to stdout or to the given target file.
   *
   * @param astodArtifact parsed OD artifact
   * @param file target file path; empty means stdout
   */
  @Override
  public void prettyPrint(ASTODArtifact astodArtifact, String file) {
    // pretty print AST
    OD4ReportFullPrettyPrinter pp = new OD4ReportFullPrettyPrinter(new IndentPrinter());
    String od = pp.prettyprint(astodArtifact);
    print(od, file);
  }
  
  /*=================================================================*/
  /* Part 3: Defining the options incl. help-texts
  /*=================================================================*/
  
  /**
   * Initializes the standard CLI options for the OD tool.
   *
   * @return configured {@link Options} instance
   */
  @Override
  public Options addStandardOptions(Options options) {
    // help dialog
    options.addOption(Option.builder("h").longOpt("help").desc("Prints this help dialog").get());
    
    // parse input file
    options.addOption(Option.builder("i").longOpt("input").argName("file").hasArg()
        .desc("Reads the source file (mandatory) and parses the contents as an object diagram")
        .get());
    
    // Read file to add symboltypes to global scope
    options.addOption(
        Option.builder("symtypes").longOpt("symboltypes").argName("string").optionalArg(true)
            .hasArgs().desc(
                "Symbol type followed by deser (repeat for multiple) to be able to resolve smbols from foreign languages.")
            .get());
    
    // model paths
    options.addOption(Option.builder("path").argName("dirlist").hasArgs()
        .desc("Sets the artifact path for imported symbols").get());
    
    // pretty print OD
    options.addOption(Option.builder("pp").longOpt("prettyprint").argName("file").optionalArg(true)
        .numberOfArgs(1).desc("Prints the OD-AST to stdout or the specified file (optional)")
        .get());
    
    // print OD symtab
    options.addOption(
        Option.builder("s").longOpt("symboltable").argName("file").optionalArg(true).numberOfArgs(1)
            .desc("Stores the symbol table of the OD. The default value is `{ODName}.odsym`.")
            .get());
    return options;
  }
  
  @Override
  public Options addAdditionalOptions(Options options) {
    // check cocos
    options.addOption(Option.builder("c").longOpt("coco").optionalArg(true).numberOfArgs(1).desc(
        """
            Checks the CoCos for the input. Optional arguments are:
            -c intra to check only the intra-model CoCos,
            -c inter checks also inter-model CoCos.
            Without an argument, all CoCos are checked.
            """.stripIndent()).get());
    return options;
  }
  
  /**
   * Writes the symbol table of the given scope to a file.
   *
   * @param as symbol table to store
   * @param modelPath location of the file containing the OD
   * @param symTabPath location of the file or directory containing the printed table
   * @return the resolved target path
   */
  public Path storeSymTab(IOD4ReportArtifactScope as, Path modelPath, String symTabPath) {
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
