/* (c) https://github.com/MontiCore/monticore */

package de.monticore.od4development;

import de.monticore.cd.codegen.CDGenerator;
import de.monticore.cd.codegen.CdUtilsPrinter;
import de.monticore.generating.GeneratorSetup;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.generating.templateengine.TemplateController;
import de.monticore.generating.templateengine.TemplateHookPoint;
import de.monticore.io.paths.MCPath;
import de.monticore.od2cd.CompositionPrinter;
import de.monticore.od2cd.OD2CDConverter;
import de.monticore.od4development._cocos.OD4DevelopmentCoCoChecker;
import de.monticore.od4development._cocos.OD4DevelopmentCoCos;
import de.monticore.od4development._symboltable.CDRoleSymbolDeSer;
import de.monticore.od4development._symboltable.IOD4DevelopmentArtifactScope;
import de.monticore.od4development._visitor.OD4DevelopmentTraverser;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.odbasis._prettyprint.ODBasisFullPrettyPrinter;
import de.monticore.odbasis._symboltable.ODBasisSymbolTableCompleter;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.symbols.basicsymbols.BasicSymbolsMill;
import de.monticore.types.mcbasictypes._ast.ASTMCImportStatement;
import de.se_rwth.commons.logging.Log;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class OD4DevelopmentTool extends OD4DevelopmentToolTOP {
  
  protected static final String PARSE_SUCCESSFUL = "Successfully parsed %s";
  
  protected static final String CHECK_SUCCESSFUL =
      "Successfully checked the CoCos for object diagram %s";
  
  protected static final String CHECK_ERROR = "0x0D020 Error while processing the object diagram";
  
  protected static final String STEXPORT_SUCCESSFUL = "Creation of symbol file %s successful";
  protected static final String PRETTYPRINT_SUCCESSFUL = "Pretty printed OD to file %s";
  
  protected static final String INPUT_FILE_NOT_EXISTENT = "0x0D015 Input file '%s' does not exist";
  protected static final String INPUT_OPTION_NOT_PRESENT = "0x0D026 No input file given. Use -i <FILE> to declare the tool input.";
  protected static final String OUTPUT_PATH_INVALID =
      "0x0D016 Output path '%s' is not a valid directory path";
  protected static final String OUTPUT_OPTION_MISSING_ARG =
      "0x0D017 Option -o requires an argument specifying the output directory.";
  protected static final String COCO_OPTION_INVALID =
      "0x0D018 Invalid argument '%s' for option -c. Allowed values are: intra, inter.";
  protected static final String COCO_OPTION_TOO_MANY_ARGS =
      "0x0D019 Option -c accepts at most one argument: intra or inter.";
  protected static final String PARSE_EXCEPTION_MSG = "0x0D021 Could not process parameters: %s";
  
  /**
   * Processes CLI arguments and executes parsing, symbol table creation, CoCo checks,
   * pretty printing, symbol export and optional OD-to-CD generation.
   * <pre>
   * <ul>
   *     <li>{@code -h}/{@code --help}: prints the help dialog and exits.</li>
   *     <li>{@code -i}/{@code --input <file>}: sets the mandatory input OD file to parse.</li>
   *     <li>{@code -d}: enables debug logging output.</li>
   *     <li>{@code -path <dirlist>}: sets one or more symbol path entries for imported symbols.</li>
   *     <li>{@code -pp}/{@code --prettyprint [file]}: pretty prints the AST to stdout or the optional file.</li>
   *     <li>{@code -s}/{@code --symboltable [file]}: writes the symbol table to the optional file or a default name.</li>
   *     <li>{@code -c}/{@code --coco [intra|inter]}: runs all CoCos by default, or only intra/inter CoCos when specified.</li>
   *     <li>{@code -o}/{@code --output <dir>}: generates a class diagram into the given output directory.</li>
   * </ul>
   * </pre>
   *
   * @param cmd command line
   */
  public void doRun(CommandLine cmd) {
    // if -i input is missing: also print help and stop
    if (!cmd.hasOption("i")) {
      Log.error(INPUT_OPTION_NOT_PRESENT);
      return;
    }
    
    // avoid duplicate console output when pretty print already writes to stdout
    final boolean doPrintToStdOut = !(cmd.hasOption("pp") && cmd.getOptionValue("pp") == null);
    
    // parse input file
    String modelFile = cmd.getOptionValue("i");
    Path modelFilePath = Paths.get(modelFile);
    if (!modelFilePath.toFile().exists()) {
      Log.error(String.format(INPUT_FILE_NOT_EXISTENT, modelFile));
      return;
    }
    
    ASTODArtifact ast = parse(modelFile);
    
    if (doPrintToStdOut) {
      Log.info(String.format(PARSE_SUCCESSFUL, ast.getObjectDiagram().getName()),
          getClass().getName());
    }
    
    // initialize primitives
    BasicSymbolsMill.initializePrimitives();
    
    // if -path is set: set symbol path and load imported diagrams
    if (cmd.hasOption("path")) {
      MCPath mcPath = new MCPath();
      String[] paths = cmd.getOptionValues("path");
      if (paths != null) {
        // support multiple -path entries passed via CLI
        Arrays.stream(paths).forEach(p -> mcPath.addEntry(Paths.get(p)));
      }
      OD4DevelopmentMill.globalScope().setSymbolPath(mcPath);
      OD4DevelopmentMill.globalScope()
          .putTypeSymbolDeSer("de.monticore.cdbasis._symboltable.CDTypeSymbol");
      OD4DevelopmentMill.globalScope()
          .putSymbolDeSer("de.monticore.cdassociation._symboltable.CDRoleSymbol",
              new CDRoleSymbolDeSer());
      
      for (ASTMCImportStatement i : ast.getMCImportStatementList()) {
        OD4DevelopmentMill.globalScope().loadDiagram(i.getQName());
      }
    }
    
    IOD4DevelopmentArtifactScope as = createSymbolTable(ast);
    
    boolean checkTypes = cmd.hasOption("s") || cmd.hasOption("o") || (cmd.hasOption("c") && (
        cmd.getOptionValue("c") == null || cmd.getOptionValue("c").equals("inter")));
    completeSymbolTable(ast, checkTypes);
    
    if (cmd.hasOption("s")) {
      Path symTabPath = storeSymTab(as, modelFilePath, cmd.getOptionValue("s"));
      if (doPrintToStdOut) {
        Log.info(String.format(STEXPORT_SUCCESSFUL, symTabPath.toAbsolutePath()),
            getClass().getName());
      }
    }
    
    // -option check cocos
    if (cmd.hasOption("c")) {
      String[] cocoArgs = cmd.getOptionValues("c");
      if (cocoArgs == null || cocoArgs.length == 0) {
        runAllCoCos(ast);
      }
      else if (cocoArgs.length == 1) {
        String cocoArg = cocoArgs[0];
        if ("intra".equals(cocoArg)) {
          runDefaultCoCos(ast);
        }
        else if ("inter".equals(cocoArg)) {
          runInterCoCos(ast);
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
          Log.info(String.format(CHECK_SUCCESSFUL, ast.getObjectDiagram().getName()),
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
      prettyPrint(ast, path);
      if (StringUtils.isNotBlank(path)) {
        Log.info(PRETTYPRINT_SUCCESSFUL.formatted(path), getClass().getName());
      }
    }
    
    if (cmd.hasOption("o")) {
      String outputDirArgument = cmd.getOptionValue("o");
      if (outputDirArgument == null || outputDirArgument.isBlank()) {
        Log.error(OUTPUT_OPTION_MISSING_ARG);
        return;
      }
      try {
        Path outputDirPath = Paths.get(outputDirArgument);
        // allow non-existing directory paths; reject only existing non-directory targets
        if (!Files.exists(outputDirPath) || Files.isDirectory(outputDirPath)) {
          generateCD(ast, outputDirPath);
        }
        else {
          Log.error(String.format(OUTPUT_PATH_INVALID, outputDirArgument));
        }
      }
      catch (InvalidPathException e) {
        Log.error(String.format(OUTPUT_PATH_INVALID, outputDirArgument));
      }
    }
  }
  
  /**
   * Pretty prints the OD artifact either to stdout or to the given target file.
   *
   * @param ast parsed OD artifact
   * @param file target file path; empty means stdout
   */
  @Override
  public void prettyPrint(ASTODArtifact ast, String file) {
    ODBasisFullPrettyPrinter printer = new ODBasisFullPrettyPrinter(new IndentPrinter());
    String result = printer.prettyprint(ast);
    print(result, file);
  }
  
  /**
   * Runs only intra-model CoCos.
   *
   * @param ast parsed OD artifact
   */
  @Override
  public void runDefaultCoCos(ASTODArtifact ast) {
    OD4DevelopmentCoCoChecker checker = new OD4DevelopmentCoCos().getCheckerForAllIntraCoCos();
    checker.checkAll(ast);
  }
  
  /**
   * Runs all available CoCos (intra- and inter-model checks).
   *
   * @param ast parsed OD artifact
   */
  public void runAllCoCos(ASTODArtifact ast) {
    OD4DevelopmentCoCoChecker checker = new OD4DevelopmentCoCos().getCheckerForAllCoCos();
    checker.checkAll(ast);
  }
  
  /**
   * Runs only inter-model CoCos.
   *
   * @param ast parsed OD artifact
   */
  public void runInterCoCos(ASTODArtifact ast) {
    OD4DevelopmentCoCoChecker checker = new OD4DevelopmentCoCos().getCheckerForAllInterCoCos();
    checker.checkAll(ast);
  }
  
  /**
   * Generates a class diagram from the given OD artifact into the provided output directory.
   *
   * @param ast parsed OD artifact
   * @param outputDir target directory for generated files
   */
  public void generateCD(ASTODArtifact ast, Path outputDir) {
    GeneratorSetup setup = new GeneratorSetup();
    GlobalExtensionManagement glex = new GlobalExtensionManagement();
    setup.setGlex(glex);
    glex.setGlobalValue("cdPrinter", new CdUtilsPrinter());
    glex.setGlobalValue("cp", new CompositionPrinter());
    
    File targetDir = outputDir.toFile();
    setup.setOutputDirectory(targetDir);
    
    String configTemplate = "od2cd.OD2CD";
    TemplateController tc = setup.getNewTemplateController(configTemplate);
    CDGenerator generator = new CDGenerator(setup);
    TemplateHookPoint hpp = new TemplateHookPoint(configTemplate);
    List<Object> configTemplateArgs;
    // template arguments define converter variant and generator context
    OD2CDConverter converter = new OD2CDConverter();
    configTemplateArgs = Arrays.asList(glex, converter, setup.getHandcodedPath(), generator);
    
    hpp.processValue(tc, ast, configTemplateArgs);
  }
  
  /**
   * prints the symboltable of the given scope out to a file
   *
   * @param as symboltable to store
   * @param modelPath location of the file containing the OD
   * @param symTabPath location of the file or directory containing the printed table
   */
  public Path storeSymTab(IOD4DevelopmentArtifactScope as, Path modelPath, String symTabPath) {
    Path targetPath;
    if (symTabPath == null || symTabPath.isBlank()) {
      String symTabName = FilenameUtils.getBaseName(modelPath.toString()) + ".odsym";
      Path modelParent = modelPath.getParent();
      // handle relative input files that do not have an explicit parent directory
      targetPath = modelParent != null ? modelParent.resolve(symTabName) : Paths.get(symTabName);
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
  
  public void completeSymbolTable(ASTODArtifact ast, boolean checkObjectTypes) {
    OD4DevelopmentTraverser traverser = OD4DevelopmentMill.inheritanceTraverser();
    
    ODBasisSymbolTableCompleter odBasisCompleter =
        new ODBasisSymbolTableCompleter(checkObjectTypes);
    traverser.add4ODBasis(odBasisCompleter);
    odBasisCompleter.setTraverser(traverser);
    ast.accept(traverser);
  }
  
  /*=================================================================*/
  /* Defining the options incl. help-texts
  /*=================================================================*/
  
  /**
   * Initializes the standard options for the OD tool.
   *
   * @return The CLI options with arguments.
   */
  @Override
  public Options addStandardOptions(Options options) {
    // help dialog
    options.addOption(Option.builder("h").longOpt("help").desc("Prints this help dialog").get());
    
    // parse input file
    options.addOption(Option.builder("i").longOpt("input").argName("file").hasArg()
        .desc("Reads the source file (mandatory) and parses the contents as an " + "object diagram")
        .get());
    
    // model paths
    options.addOption(
        Option.builder("path").argName("dirlist").numberOfArgs(Option.UNLIMITED_VALUES).hasArg()
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
  
  /**
   * Adds tool-specific CLI options beyond the standard options.
   *
   * @param options current options instance
   * @return options including additional OD4Development options
   */
  @Override
  public Options addAdditionalOptions(Options options) {
    // check cocos
    options.addOption(Option.builder("c").longOpt("coco").optionalArg(true).numberOfArgs(1).desc(
        "Checks the CoCos for the input. Optional arguments are:\n" + "-c intra to check only the"
            + " intra-model CoCos,\n" + "-c inter to check only inter-model CoCos."
            + " Without an argument, all CoCos are checked.").get());
    
    options.addOption(
        Option.builder("o").longOpt("output").optionalArg(true).hasArg().numberOfArgs(1)
            .desc("The output path for the generated/derivated classdiagram").get());
    return options;
  }
}