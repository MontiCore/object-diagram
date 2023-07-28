package de.monticore;/* (c) https://github.com/MontiCore/monticore */

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
public class ODPlantUMLTool {

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

            if(cmd.hasOption("s")) {
                MCPath mcPath = new MCPath(cmd.getOptionValue("s"));
                OD4ReportMill.globalScope().setSymbolPath(mcPath);
            } else {
                createSymbolTable(ast);
            }

            // -option pretty print
            if (cmd.hasOption("pp")) {
                String path = cmd.getOptionValue("pp", StringUtils.EMPTY);
                FileFormat extension = FileFormat.valueOf(FilenameUtils.getExtension(path).toUpperCase());
                String base = FilenameUtils.getBaseName(path);
                prettyPrint(ast, base, extension);
            }

        } catch (ParseException e) {
            // an unexpected error from the apache CLI parser:
            Log.error("0xA7105 Could not process parameters: " + e.getMessage());
        }
    }

    private ASTODArtifact parse(String model) {
        try {
            OD4ReportParser parser = OD4ReportMill.parser() ;
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

    public void printHelp(Options options)
    {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(80);
        formatter.printHelp("ODPlantUMLTool", options);
    }

    public void prettyPrint(ASTODArtifact ast, String file, FileFormat fileFormat) {
        PlantUMLODFullPrettyPrinter printer = new PlantUMLODFullPrettyPrinter();
        String result = printer.prettyprint(ast);
        generateImage(result, file, fileFormat);
    }

    public void generateImage(String plantUMLSource, String destinationPath, FileFormat fileFormat) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            SourceStringReader reader = new SourceStringReader(plantUMLSource);
            reader.outputImage(outputStream, new FileFormatOption(fileFormat));

            try (FileOutputStream fileOutputStream = new FileOutputStream(destinationPath + fileFormat.getFileSuffix())) {
                outputStream.writeTo(fileOutputStream);
            }
            Log.info("Diagram image generated and saved as: " + destinationPath,"SUCCESS");
        } catch (IOException e) {
            Log.error("Error generating diagram image: " + e.getMessage());
        }
    }

    public IOD4ReportArtifactScope createSymbolTable(ASTODArtifact node) {
        OD4ReportScopesGenitorDelegator genitor = OD4ReportMill.scopesGenitorDelegator();
        IOD4ReportArtifactScope symTab = genitor.createFromAST(node);
        return symTab;
    }

    public static void main(String[] args) {
        ODPlantUMLTool tool = new ODPlantUMLTool();
        tool.run(args);
    }
}