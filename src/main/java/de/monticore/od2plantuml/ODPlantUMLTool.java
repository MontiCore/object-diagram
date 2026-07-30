package de.monticore.od2plantuml;/* (c) https://github.com/MontiCore/monticore */

import de.monticore.io.paths.MCPath;
import de.monticore.od2plantuml.prettyprinter.PlantUMLODFullPrettyPrinter;
import de.monticore.od4development.OD4DevelopmentMill;
import de.monticore.od4development._parser.OD4DevelopmentParser;
import de.monticore.od4development._symboltable.CDRoleSymbolDeSer;
import de.monticore.od4development._symboltable.IOD4DevelopmentArtifactScope;
import de.monticore.od4development._symboltable.OD4DevelopmentScopesGenitorDelegator;
import de.monticore.od4development.trafo.OD4DevelopmentAttributeValueCompositionTrafo;
import de.monticore.od4development.trafo.OD4DevelopmentDeAnonymizeObjectsTrafo;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.types.mcbasictypes._ast.ASTMCImportStatement;
import de.se_rwth.commons.logging.Log;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import org.apache.commons.cli.*;
import org.apache.commons.cli.help.HelpFormatter;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

/**
 * Tool for generating PlantUML diagrams from Object Diagram (OD) models.
 */
public class ODPlantUMLTool {
  
  public static final String PARSE_ERROR_CLI = "0xA7105 Could not process parameters: %s";
  public static final String PARSE_ERROR_MODEL = "0xA1050x51507 Model could not be parsed.";
  public static final String PARSE_ERROR_IO = "0xA1051x70629 Failed to parse %s";
  public static final String ERROR_MISSING_EXTENSION =
      "0x0D011: Missing output file extension for -pp. Supported examples: .png, .svg, .eps.";
  public static final String ERROR_UNSUPPORTED_FORMAT =
      "0x0D012: Unsupported output format '%s' for -pp.";
  public static final String ERROR_GENERATE_IMAGE = "0x0D010: Error generating diagram image: %s";
  public static final String SUCCESS_IMAGE_GENERATED = "Diagram image generated and saved as: %s";
  
  /**
   * Processes CLI arguments and executes parsing, symbol table creation and PlantUML image
   * generation.
   * <pre>
   * <ul>
   *     <li>{@code -h}/{@code --help}: prints the help dialog and exits.</li>
   *     <li>{@code -i}/{@code --input <file>}: sets the mandatory input OD file to parse.</li>
   *     <li>{@code -path <dirlist>}: sets one or more symbol path entries for imported symbols.</li>
   *     <li>{@code -pp}/{@code --prettyprint [file]}: generates a PlantUML image to the given file,
   *         or prints the PlantUML source to stdout when no file is given.</li>
   *     <li>{@code -s}/{@code --symboltable <file>}: loads the symbol table from the given file
   *         instead of building it from the AST.</li>
   * </ul>
   * </pre>
   *
   * @param args command line arguments
   */
  public void run(String[] args) {
    Log.init();
    OD4DevelopmentMill.init();
    Options options = initOptions();
    
    try {
      CommandLineParser cliParser = new DefaultParser();
      CommandLine cmd = cliParser.parse(options, args);
      
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
      
      // parse input file, which is now available
      // (only returns if successful)
      Optional<ASTODArtifact> ast = parse(cmd.getOptionValue("i"));
      
      if (ast.isEmpty()) {
        return;
      }
      
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
        
        for (ASTMCImportStatement i : ast.get().getMCImportStatementList()) {
          OD4DevelopmentMill.globalScope().loadDiagram(i.getQName());
        }
      }
      
      if (cmd.hasOption("s")) {
        MCPath mcPath = new MCPath(cmd.getOptionValue("s"));
        OD4DevelopmentMill.globalScope().setSymbolPath(mcPath);
      }
      else {
        createSymbolTable(ast.get());
      }
      
      new OD4DevelopmentDeAnonymizeObjectsTrafo().transform(ast.get());
      new OD4DevelopmentAttributeValueCompositionTrafo().transform(ast.get());
      
      // -option pretty print
      if (cmd.hasOption("pp")) {
        String path = cmd.getOptionValue("pp", StringUtils.EMPTY);
        prettyPrint(ast.get(), path);
      }
      
    }
    catch (ParseException e) {
      // an unexpected error from the apache CLI parser:
      Log.error(String.format(PARSE_ERROR_CLI, e.getMessage()));
    }
  }
  
  /**
   * Parses the given OD model file and returns the resulting AST.
   *
   * @param model path to the OD model file, not null
   * @return the parsed {@link ASTODArtifact}, or {@link java.util.Optional#empty()} on failure
   */
  private Optional<ASTODArtifact> parse(String model) {
    try {
      OD4DevelopmentParser parser = OD4DevelopmentMill.parser();
      Optional<ASTODArtifact> optAst = parser.parse(model);
      
      if (!parser.hasErrors() && optAst.isPresent()) {
        return optAst;
      }
      Log.error(PARSE_ERROR_MODEL);
    }
    catch (NullPointerException | IOException e) {
      Log.error(String.format(PARSE_ERROR_IO, model), e);
    }
    return Optional.empty();
  }
  
  /**
   * Initializes the CLI options for this tool.
   *
   * @return configured {@link Options} instance
   */
  
  private Options initOptions() {
    Options options = new Options();
    options.addOption(Option.builder("h").longOpt("help").desc("Prints this help dialog").get());
    
    options.addOption(Option.builder("i").longOpt("input").argName("file").hasArg()
        .desc("Reads the source file (mandatory) and parses the contents").get());
    
    options.addOption(Option.builder("pp").longOpt("prettyprint").argName("file").optionalArg(true)
        .numberOfArgs(1).desc("Prints the AST to stdout or the specified file (optional)").get());
    
    options.addOption(Option.builder("s").longOpt("symboltable").argName("file").hasArg()
        .desc("Serialized the Symbol table of the given artifact.").get());
    
    options.addOption(
        Option.builder("path").argName("dirlist").numberOfArgs(Option.UNLIMITED_VALUES).hasArg()
            .desc("Sets the artifact path for imported symbols").build());
    return options;
  }
  
  /**
   * Prints the help dialog to stdout.
   *
   * @param options the configured CLI options to display
   */
  public void printHelp(Options options) {
    org.apache.commons.cli.help.HelpFormatter formatter = HelpFormatter.builder().get();
    try {
      formatter.printHelp("ODPlantUMLTool", "", options, "", true);
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  /**
   * Generates a PlantUML image from the given AST and writes it to the given file using the
   * specified format.
   *
   * @param ast parsed OD artifact
   * @param file destination file path without extension
   * @param fileFormat target image format (e.g. PNG)
   */
  public void prettyPrint(ASTODArtifact ast, String file, FileFormat fileFormat) {
    PlantUMLODFullPrettyPrinter printer = new PlantUMLODFullPrettyPrinter();
    String result = printer.prettyprint(ast);
    generateImage(result, file, fileFormat);
  }
  
  /**
   * Pretty prints the OD artifact either to stdout (when no path is given) or renders an image
   * file. The image format is derived from the file extension of {@code path}.
   *
   * @param ast parsed OD artifact
   * @param path output file path including extension; blank means stdout
   */
  public void prettyPrint(ASTODArtifact ast, String path) {
    PlantUMLODFullPrettyPrinter printer = new PlantUMLODFullPrettyPrinter();
    String result = printer.prettyprint(ast);
    
    if (StringUtils.isBlank(path)) {
      System.out.println(result);
      return;
    }
    
    String extension = FilenameUtils.getExtension(path);
    if (StringUtils.isBlank(extension)) {
      Log.error(ERROR_MISSING_EXTENSION);
      return;
    }
    
    try {
      FileFormat fileFormat = FileFormat.valueOf(extension.toUpperCase(Locale.ROOT));
      String destinationPath = FilenameUtils.removeExtension(path);
      generateImage(result, destinationPath, fileFormat);
    }
    catch (IllegalArgumentException e) {
      Log.error(String.format(ERROR_UNSUPPORTED_FORMAT, extension));
    }
  }
  
  /**
   * Renders the given PlantUML source string into an image file.
   *
   * @param plantUMLSource PlantUML source string to render
   * @param destinationPath target file path without extension
   * @param fileFormat target image format (e.g. PNG)
   */
  public void generateImage(String plantUMLSource, String destinationPath, FileFormat fileFormat) {
    try {
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      SourceStringReader reader = new SourceStringReader(plantUMLSource);
      reader.outputImage(outputStream, new FileFormatOption(fileFormat));
      
      try (
          FileOutputStream fileOutputStream = new FileOutputStream(
              destinationPath + fileFormat.getFileSuffix())) {
        outputStream.writeTo(fileOutputStream);
      }
      System.out.println(
          String.format(SUCCESS_IMAGE_GENERATED, destinationPath + fileFormat.getFileSuffix()));
    }
    catch (IOException e) {
      Log.error(String.format(ERROR_GENERATE_IMAGE, e.getMessage()));
    }
  }
  
  /**
   * Creates the symbol table for the given OD artifact.
   *
   * @param node parsed OD artifact
   * @return the resulting artifact scope
   */
  public IOD4DevelopmentArtifactScope createSymbolTable(ASTODArtifact node) {
    OD4DevelopmentScopesGenitorDelegator genitor = OD4DevelopmentMill.scopesGenitorDelegator();
    return genitor.createFromAST(node);
  }
  
  /**
   * Entry point of the ODPlantUMLTool. Delegates to {@link #run(String[])} and exits with code
   * {@code 0} on success or {@code 1} when errors occurred.
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {
    try {
      ODPlantUMLTool tool = new ODPlantUMLTool();
      tool.run(args);
    }
    catch (Exception exception) {
      // ensure a sane exit
      Log.ensureInitialization();
      Log.error(
          "0xEEEEE an internal error occurred" + " during the execution of the ODPlantUMLTool."
              + System.lineSeparator() + "This error is unexpected"
              + " and does not indicate an issue with any provided models.", exception);
    }
    // properly exit with a code
    Log.ensureInitialization();
    System.exit(Log.getErrorCount() == 0 ? 0 : 1);
  }
}