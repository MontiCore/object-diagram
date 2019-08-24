/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.od.report;

import de.monticore.ast.ASTNode;
import de.monticore.generating.templateengine.reporting.commons.AReporter;
import de.monticore.generating.templateengine.reporting.commons.ReportingConstants;
import de.monticore.generating.templateengine.reporting.commons.ReportingRepository;
import de.monticore.lang.od._ast.ASTODArtifact;
import de.monticore.lang.od._ast.ASTODNode;
import de.monticore.lang.od._ast.ASTObjectDiagram;
import de.monticore.prettyprint.IndentPrinter;
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
    writeContent(ast);
    writeFooter();
    super.flush(ast);
  }

  /**
   * @param ast {@link ASTNode}
   */
  private void writeContent(ASTNode ast) {
    if (ast instanceof ASTODArtifact || ast instanceof ASTObjectDiagram) {
      ASTODNode odNode = (ASTODNode) ast;
      IndentPrinter pp = new IndentPrinter();
      ODAST2OD odPrinter = new ODAST2OD(pp, reporting);
      odPrinter.printObjectDiagram(Names.getSimpleName(modelName) + "_AST", odNode);
      writeLine(pp.getContent());
    }
  }

}
