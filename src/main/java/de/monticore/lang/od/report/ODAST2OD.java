/*
 * ******************************************************************************
 * MontiCore Language Workbench
 * Copyright (c) 2015, MontiCore, All rights reserved.
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

import de.monticore.common.common._od.Common2OD;
import de.monticore.generating.templateengine.reporting.commons.ReportingRepository;
import de.monticore.lang.od._od.OD2OD;
import de.monticore.lang.od._visitor.CommonODDelegatorVisitor;
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
    visitor = new CommonODDelegatorVisitor();
    visitor.set_de_monticore_literals_literals__visitor_LiteralsVisitor(
        new Literals2OD(printer, reporting));
    visitor.set_de_monticore_types_types__visitor_TypesVisitor(new Types2OD(printer, reporting));
    visitor.set_de_monticore_common_common__visitor_CommonVisitor(new Common2OD(printer, reporting));
    visitor.set_de_monticore_lang_od__visitor_ODVisitor(this);
    setPrintEmptyList(true);
    setPrintEmptyOptional(true);
  }
  
  /**
   * @see de.monticore.java.javadsl._od.OD2OD#setRealThis(de.monticore.java.javadsl._visitor.ODVisitor)
   */
  @Override
  public void setRealThis(ODVisitor realThis) {
    if (this.realThis != realThis) {
      this.realThis = realThis;
      visitor.setRealThis(realThis);
    }
  }
  
  /**
   * @see de.monticore.java.javadsl._od.OD2OD#getRealThis()
   */
  @Override
  public ODVisitor getRealThis() {
    return realThis;
  }
  
}
