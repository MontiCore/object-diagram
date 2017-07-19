/*
 * ******************************************************************************
 * MontiCore Language Workbench
 * Copyright (c) 2017, MontiCore, All rights reserved.
 *
 * This project is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this project. If not, see <http://www.gnu.org/licenses/>.
 * ******************************************************************************
 */
package de.monticore.lang.od.report;

import de.monticore.generating.templateengine.reporting.commons.ReportingRepository;
import de.monticore.lang.od._od.OD2OD;
import de.monticore.lang.od._visitor.ODDelegatorVisitor;
import de.monticore.lang.od._visitor.ODVisitor;
import de.monticore.prettyprint.IndentPrinter;

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
