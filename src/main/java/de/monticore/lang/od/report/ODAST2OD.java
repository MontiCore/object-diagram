/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.od.report;

import de.monticore.common.common._od.Common2OD;
import de.monticore.generating.templateengine.reporting.commons.ReportingRepository;
import de.monticore.lang.od._od.OD2OD;
import de.monticore.lang.od._visitor.ODDelegatorVisitor;
import de.monticore.lang.od._visitor.ODVisitor;
import de.monticore.literals.literals._od.Literals2OD;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.types._od.Types2OD;

public class ODAST2OD extends OD2OD {

  private ODVisitor realThis = this;

  private final ODDelegatorVisitor visitor;

  public ODAST2OD(IndentPrinter printer, ReportingRepository reporting) {
    super(printer, reporting);
    visitor = new ODDelegatorVisitor();
    Literals2OD literalsVisitor = new Literals2OD(printer, reporting);
    Types2OD typesVisitor = new Types2OD(printer, reporting);
    Common2OD commonVisitor = new Common2OD(printer, reporting);
    visitor.setLiteralsVisitor(literalsVisitor);
    visitor.setTypesVisitor(typesVisitor);
    visitor.setCommonVisitor(commonVisitor);
    visitor.setODVisitor(this);
    boolean emptyList = true;
    boolean emptyOptional = true;
    setPrintEmptyList(emptyList);
    setPrintEmptyOptional(emptyOptional);
    literalsVisitor.setPrintEmptyList(emptyList);
    literalsVisitor.setPrintEmptyOptional(emptyOptional);
    typesVisitor.setPrintEmptyList(emptyList);
    typesVisitor.setPrintEmptyOptional(emptyOptional);
    commonVisitor.setPrintEmptyList(emptyList);
    commonVisitor.setPrintEmptyOptional(emptyOptional);
  }

  @Override
  public void setRealThis(ODVisitor realThis) {
    if (this.realThis != realThis) {
      this.realThis = realThis;
      visitor.setRealThis(realThis);
    }
  }

  @Override
  public ODVisitor getRealThis() {
    return realThis;
  }

}
