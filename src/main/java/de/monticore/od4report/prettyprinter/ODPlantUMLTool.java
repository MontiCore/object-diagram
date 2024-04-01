package de.monticore.od4report.prettyprinter;/* (c) https://github.com/MontiCore/monticore */

import de.monticore.io.paths.MCPath;
import de.monticore.od4report.OD4ReportMill;
import de.monticore.od4report._parser.OD4ReportParser;
import de.monticore.od4report._symboltable.IOD4ReportArtifactScope;
import de.monticore.od4report._symboltable.OD4ReportScopesGenitorDelegator;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.se_rwth.commons.logging.Log;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import org.apache.commons.cli.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

/**
 * Base class that is used to define the PlantUML based pretty printer.
 */
public class ODPlantUMLTool {
  
  /**
   * This is the driver method responsible for PlantUML tool creation.
   *
   * @param args Command line arguments enabling different features in the tool<br>
   *             <i>-h</i> description and help dialog of the tool<br>
   *             -<i>i</i> (mandatory) parse input OD file and return if successful<br> -<i>s</i>
   *             specify the symbol table<br> -<i>pp</i> pretty printing the OD in different
   *             formats(e.g. - PNG)
   */
  public void run(String[] args) {
    Log.init();
    OD4ReportMill.init();
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
      ASTODArtifact ast = parse(cmd.getOptionValue("i"));
      
      if (cmd.hasOption("s")) {
        MCPath mcPath = new MCPath(cmd.getOptionValue("s"));
        OD4ReportMill.globalScope().setSymbolPath(mcPath);
      }
      else {
        createSymbolTable(ast);
      }
      
      // -option pretty print
      if (cmd.hasOption("pp")) {
        String path = cmd.getOptionValue("pp", StringUtils.EMPTY);
        FileFormat extension = FileFormat.valueOf(FilenameUtils.getExtension(path).toUpperCase());
        String base = FilenameUtils.getBaseName(path);
        prettyPrint(ast, base, extension);
      }
      
    }
    catch (ParseException e) {
      // an unexpected error from the apache CLI parser:
      Log.error("0xA7105 Could not process parameters: " + e.getMessage());
    }
  }
  
  /**
   * This method parses the provided OD file.
   *
   * @param model filename for the OD model, not null
   * @return the ast for the OD artifact
   */
  private ASTODArtifact parse(String model) {
    try {
      OD4ReportParser parser = OD4ReportMill.parser();
      Optional<ASTODArtifact> optAst = parser.parse(model);
      
      if (!parser.hasErrors() && optAst.isPresent()) {
        return optAst.get();
      }
      Log.error("0xA1050x51507 Model could not be parsed.");
    }
    catch (NullPointerException | IOException e) {
      Log.error("0xA1051x70629 Failed to parse " + model, e);
    }
    return null;
  }
  
  /**
   * This method initializes the command line arguments
   *
   * @return {@link Options} object
   */
  
  private Options initOptions() {
    Options options = new Options();
    options.addOption(Option.builder("h")
        .longOpt("help")
        .desc("Prints this help dialog")
        .build());
    
    options.addOption(Option.builder("i")
        .longOpt("input")
        .argName("file")
        .hasArg()
        .desc("Reads the source file (mandatory) and parses the contents")
        .build());
    
    options.addOption(Option.builder("pp")
        .longOpt("prettyprint")
        .argName("file")
        .optionalArg(true)
        .numberOfArgs(1)
        .desc("Prints the AST to stdout or the specified file (optional)")
        .build());
    
    options.addOption(Option.builder("s")
        .longOpt("symboltable")
        .argName("file")
        .hasArg()
        .desc("Serialized the Symbol table of the given artifact.")
        .build());
    return options;
  }
  
  /**
   * This method prints the help dialog.
   *
   * @param options printing the initialized {@link Options}
   */
  public void printHelp(Options options) {
    HelpFormatter formatter = new HelpFormatter();
    formatter.setWidth(80);
    formatter.printHelp("ODPlantUMLTool", options);
  }
  
  /**
   * This method acts as a registry for injecting actual pretty printers into the framework.
   *
   * @param ast        parsed ast for the OD
   * @param file       the OD file
   * @param fileFormat pretty printed format of the OD(e.g. - PNG)
   */
  public void prettyPrint(ASTODArtifact ast, String file, FileFormat fileFormat) {
    PlantUMLODFullPrettyPrinter printer = new PlantUMLODFullPrettyPrinter();
    String result = printer.prettyprint(ast);
    generateImage(result, file, fileFormat);
  }
  
  /**
   * Rendering the pretty printed OD into an image.
   *
   * @param plantUMLSource  pretty printed source OD string
   * @param destinationPath path to store the generated image
   * @param fileFormat      format in which image must be generated(e.g. -PNG)
   */
  public void generateImage(String plantUMLSource, String destinationPath, FileFormat fileFormat) {
    try {
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      SourceStringReader reader = new SourceStringReader(plantUMLSource);
      reader.outputImage(outputStream, new FileFormatOption(fileFormat));
      
      try (FileOutputStream fileOutputStream = new FileOutputStream(
          destinationPath + fileFormat.getFileSuffix())) {
        outputStream.writeTo(fileOutputStream);
      }
      Log.info("Diagram image generated and saved as: " + destinationPath, "SUCCESS");
    }
    catch (IOException e) {
      Log.error("Error generating diagram image: " + e.getMessage());
    }
  }
  
  /**
   * Create symbol table from the ast.
   *
   * @param node parsed ast for the OD
   * @return the symbol table
   */
  public IOD4ReportArtifactScope createSymbolTable(ASTODArtifact node) {
    OD4ReportScopesGenitorDelegator genitor = OD4ReportMill.scopesGenitorDelegator();
    return genitor.createFromAST(node);
  }
  
  /**
   * The main method of the program.
   *
   * @param args The command-line arguments provided by the user.
   */
  public static void main(String[] args) {
    ODPlantUMLTool tool = new ODPlantUMLTool();
    tool.run(args);
  }
}
