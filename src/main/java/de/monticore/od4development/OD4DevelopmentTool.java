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
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.odbasis._prettyprint.ODBasisFullPrettyPrinter;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.symbols.basicsymbols.BasicSymbolsMill;
import de.monticore.types.mcbasictypes._ast.ASTMCImportStatement;
import de.se_rwth.commons.Names;
import de.se_rwth.commons.logging.Log;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class OD4DevelopmentTool extends OD4DevelopmentToolTOP {

  public void run(String[] args) {
    Log.init();
    OD4DevelopmentMill.init();
    Options options = initOptions();

    try {

      // create CLI parser and parse input options from command line
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


      // -option developer logging
      if (cmd.hasOption("d")) {
        Log.initDEBUG();
      } else {
        Log.init();
      }

      // parse input file, which is now available
      // (only returns if successful)
      ASTODArtifact ast = parse(cmd.getOptionValue("i"));
      
      if(cmd.hasOption("path")) {
        MCPath mcPath = new MCPath(cmd.getOptionValue("path"));
        OD4DevelopmentMill.globalScope().setSymbolPath(mcPath);
        OD4DevelopmentMill.globalScope().putTypeSymbolDeSer("de.monticore.cdbasis._symboltable.CDTypeSymbol");
        OD4DevelopmentMill.globalScope().putSymbolDeSer("de.monticore.cdassociation._symboltable.CDRoleSymbol", new CDRoleSymbolDeSer());
        BasicSymbolsMill.initializePrimitives();
        
        for (ASTMCImportStatement i : ast.getMCImportStatementList()) {
          OD4DevelopmentMill.globalScope().loadDiagram(i.getQName());
        }
      }
      
      IOD4DevelopmentArtifactScope as = createSymbolTable(ast);
      
      if (cmd.hasOption("s")) {
        storeSymTab(as, cmd.getOptionValue("s"));
      }

      if (cmd.hasOption("c")) {
        runDefaultCoCos(ast);
      }

      // -option pretty print
      if (cmd.hasOption("pp")) {
        String path = cmd.getOptionValue("pp", StringUtils.EMPTY);
        prettyPrint(ast, path);
      }

      String outputDir = cmd.hasOption("o")
              ? cmd.getOptionValue("o")
              : "target/gen-test/";
      if(cmd.hasOption("o")) {
        generateCD(ast, outputDir);
      }

    } catch (ParseException e) {
      // an unexpected error from the apache CLI parser:
      Log.error("0xA7105 Could not process parameters: " + e.getMessage());
    }
  }

  @Override
  public void prettyPrint(ASTODArtifact ast, String file) {
    ODBasisFullPrettyPrinter printer = new ODBasisFullPrettyPrinter(new IndentPrinter());
    String result = printer.prettyprint(ast);
    print(result, file);
  }

  @Override
  public void runDefaultCoCos(ASTODArtifact ast) {
    OD4DevelopmentCoCoChecker checker = new OD4DevelopmentCoCos().getCheckerForAllCoCos();
    checker.checkAll(ast);
  }

  public void generateCD(ASTODArtifact ast, String outputDir) {
    GeneratorSetup setup = new GeneratorSetup();
    GlobalExtensionManagement glex = new GlobalExtensionManagement();
    setup.setGlex(glex);
    glex.setGlobalValue("cdPrinter", new CdUtilsPrinter());
    glex.setGlobalValue("cp", new CompositionPrinter());

    if (!outputDir.isEmpty()){
      File targetDir = new File(outputDir);
      setup.setOutputDirectory(targetDir);
    }

    String configTemplate = "od2cd.OD2CD";
    TemplateController tc = setup.getNewTemplateController(configTemplate);
    CDGenerator generator = new CDGenerator(setup);
    TemplateHookPoint hpp = new TemplateHookPoint(configTemplate);
    List<Object> configTemplateArgs;
    // select the conversion variant:
    OD2CDConverter converter = new OD2CDConverter();
    configTemplateArgs = Arrays.asList(glex, converter, setup.getHandcodedPath(), generator);

    hpp.processValue(tc, ast, configTemplateArgs);
  }

  @Override
  public Options addAdditionalOptions(Options options) {
    options.addOption(new Option("o","output",true,"Sets the output path"));
    return options;
  }
  
  /**
   * prints the symboltable of the given scope out to a file
   *
   * @param as symboltable to store
   * @param path location of the file or directory containing the printed table
   */
  public void storeSymTab(IOD4DevelopmentArtifactScope as, String path) {
    if (Path.of(path).toFile().isFile()) {
      this.storeSymbols(as, path);
    }
    else {
      this.storeSymbols(
          as,
          Paths.get(path, Names.getPathFromPackage(as.getFullName()) + ".odsym").toString());
    }
  }
}