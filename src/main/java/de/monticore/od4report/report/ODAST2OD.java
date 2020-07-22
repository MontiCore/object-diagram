// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore

/* (c) https://github.com/MontiCore/monticore */
package de.monticore.od4report.report;

import de.monticore.generating.templateengine.reporting.commons.ReportingRepository;
import de.monticore.literals.mccommonliterals._od.MCCommonLiterals2OD;
import de.monticore.literals.mcjavaliterals._od.MCJavaLiterals2OD;
import de.monticore.odbasis._od.ODBasis2OD;
import de.monticore.odbasis._visitor.ODBasisDelegatorVisitor;
import de.monticore.odbasis._visitor.ODBasisVisitor;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.mcbasictypes._od.MCBasicTypes2OD;
import de.monticore.umlmodifier._od.UMLModifier2OD;
import de.monticore.umlstereotype._od.UMLStereotype2OD;

public class ODAST2OD extends ODBasis2OD {

  private ODBasisVisitor realThis = this;

  private final ODBasisDelegatorVisitor visitor;

  public ODAST2OD(IndentPrinter printer, ReportingRepository reporting) {
    super(printer, reporting);
    visitor = new ODBasisDelegatorVisitor();
    MCBasicTypes2OD mcBasicTypesVisitor = new MCBasicTypes2OD(printer, reporting);
    UMLStereotype2OD umlStereotypeVisitor = new UMLStereotype2OD(printer, reporting);
    UMLModifier2OD umlModifierVisitor = new UMLModifier2OD(printer, reporting);
    MCCommonLiterals2OD mcCommonLiteralsVisitor = new MCCommonLiterals2OD(printer, reporting);
    MCJavaLiterals2OD mcJavaLiteralsVisitor = new MCJavaLiterals2OD(printer, reporting);

    visitor.setMCBasicTypesVisitor(mcBasicTypesVisitor);
    visitor.setUMLStereotypeVisitor(umlStereotypeVisitor);
    visitor.setUMLModifierVisitor(umlModifierVisitor);
    visitor.setMCCommonLiteralsVisitor(mcCommonLiteralsVisitor);
    visitor.setODBasisVisitor(this);

    boolean emptyList = true;
    boolean emptyOptional = true;

    setPrintEmptyList(emptyList);
    setPrintEmptyOptional(emptyOptional);

    mcBasicTypesVisitor.setPrintEmptyList(emptyList);
    mcBasicTypesVisitor.setPrintEmptyOptional(emptyList);

    umlStereotypeVisitor.setPrintEmptyList(emptyList);
    umlStereotypeVisitor.setPrintEmptyOptional(emptyList);

    umlModifierVisitor.setPrintEmptyList(emptyList);
    umlModifierVisitor.setPrintEmptyOptional(emptyList);

    mcCommonLiteralsVisitor.setPrintEmptyList(emptyList);
    mcCommonLiteralsVisitor.setPrintEmptyOptional(emptyList);

    mcJavaLiteralsVisitor.setPrintEmptyList(emptyList);
    mcJavaLiteralsVisitor.setPrintEmptyOptional(emptyList);
  }

  @Override
  public void setRealThis(ODBasisVisitor realThis) {
    if (this.realThis != realThis) {
      this.realThis = realThis;
      visitor.setRealThis(realThis);
    }
  }

  @Override
  public ODBasisVisitor getRealThis() {
    return realThis;
  }

}
