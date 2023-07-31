package de.monticore.od4report.prettyprinter;

import de.monticore.od4report.OD4ReportMill;
import de.monticore.od4report._ast.ASTOD4ReportNode;
import de.monticore.od4report._ast.ASTODReportObject;
import de.monticore.odattribute._ast.ASTODAttributeNode;
import de.monticore.odbasis._ast.ASTODBasisNode;
import de.monticore.odlink._ast.ASTODLink;
import de.monticore.odlink._ast.ASTODLinkNode;
import de.monticore.prettyprint.IndentPrinter;

/**
 * The entire reason for this class is to act as a wrapper to the newly generated PrettyPrinters
 * Note: Mills may not be initialized properly
 */
@Deprecated(forRemoval = true)
@SuppressWarnings("unused")
public class OD4ReportFullPrettyPrinter {
  public OD4ReportFullPrettyPrinter() {
  }
  
  public OD4ReportFullPrettyPrinter(IndentPrinter printer) {
  }
  
  public String prettyprint(ASTOD4ReportNode astod4ReportNode) {
    return OD4ReportMill.prettyPrint(astod4ReportNode, false);
  }
  
  public String prettyprint(ASTODReportObject astodReportObject) {
    return OD4ReportMill.prettyPrint(astodReportObject, false);
  }
  
  public String prettyprint(ASTODBasisNode astodBasisNode) {
    return OD4ReportMill.prettyPrint(astodBasisNode, false);
  }
  
  public String prettyprint(ASTODLinkNode astodLinkNode) {
    return OD4ReportMill.prettyPrint(astodLinkNode, false);
  }
  
  public String prettyprint(ASTODLink astodLink) {
    return OD4ReportMill.prettyPrint(astodLink, false);
  }
  
  public String prettyprint(ASTODAttributeNode astodAttributeNode) {
    return OD4ReportMill.prettyPrint(astodAttributeNode, false);
  }
}
