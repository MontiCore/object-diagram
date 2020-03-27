/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.odbasics.report;

import de.monticore.ast.ASTNode;
import de.monticore.generating.templateengine.reporting.commons.AReporter;
import de.monticore.generating.templateengine.reporting.commons.ReportingConstants;
import de.monticore.generating.templateengine.reporting.commons.ReportingRepository;
import de.monticore.lang.odbasics.ODBasicsTool;
import de.monticore.lang.odbasics._ast.ASTODBasicsNode;
import de.se_rwth.commons.Names;

import java.io.File;

public class AST2ODReporter extends AReporter {

  private String modelName;

  private ReportingRepository reporting;

  public AST2ODReporter(String outputDir, String modelName, ReportingRepository reporting) {
    super(outputDir + File.separator + ReportingConstants.REPORTING_DIR + File.separator
            + modelName,
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
    if (ast instanceof ASTODBasicsNode) {
      writeContent((ASTODBasicsNode) ast);
      writeFooter();
      super.flush(ast);
    }
  }

  /**
   * @param ast {@link ASTNode}
   */
  private void writeContent(ASTODBasicsNode ast) {
      writeLine(ODBasicsTool.prettyPrintODNode(ast));
  }

}
