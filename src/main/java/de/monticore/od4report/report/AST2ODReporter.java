// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore

package de.monticore.od4report.report;

import de.monticore.ast.ASTNode;
import de.monticore.generating.templateengine.reporting.commons.AReporter;
import de.monticore.generating.templateengine.reporting.commons.ReportingConstants;
import de.monticore.generating.templateengine.reporting.commons.ReportingRepository;
import de.monticore.od4data.prettyprinter.OD4DataFullPrettyPrinter;
import de.monticore.odbasis._ast.ASTODBasisNode;
import de.monticore.prettyprint.IndentPrinter;
import de.se_rwth.commons.Names;

import java.io.File;

public class AST2ODReporter extends AReporter {

  private String modelName;

  private ReportingRepository reporting;

  public AST2ODReporter(String outputDir, String modelName, ReportingRepository reporting) {
    super(
        outputDir + File.separator + ReportingConstants.REPORTING_DIR + File.separator + modelName,
        Names.getSimpleName(modelName) + "_AST", ReportingConstants.OD_FILE_EXTENSION);
    this.modelName = modelName;
    this.reporting = reporting;
  }

  @Override
  protected void writeHeader() {
    writeLine("/*");
    writeLine(
        " * ========================================================== AST for ObjectDiagram");
    writeLine(" */");
  }

  private void writeFooter() {
    writeLine("/*");
    writeLine(" * ========================================================== Explanation");
    writeLine(" * Shows the AST with all attributes as object diagram");
    writeLine(" */");
  }

  @Override
  public void flush(ASTNode ast) {
    if (ast instanceof ASTODBasisNode) {
      writeContent((ASTODBasisNode) ast);
      writeFooter();
      super.flush(ast);
    }
  }

  /**
   * @param ast {@link ASTNode}
   */
  private void writeContent(ASTODBasisNode ast) {
    writeLine(new OD4DataFullPrettyPrinter(new IndentPrinter()).prettyprint(ast));
  }

}
