/*
 * ******************************************************************************
 * MontiCore Language Workbench, www.monticore.de
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

package de.monticore.lang.odbasics.report;

import de.monticore.generating.templateengine.reporting.commons.ReportingConstants;
import de.monticore.generating.templateengine.reporting.commons.ReportingRepository;
import de.monticore.generating.templateengine.reporting.reporter.SymbolTableReporter2;
import de.monticore.lang.odbasics._symboltable.ODObjectSymbol;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.symboltable.ISymbol;

import java.io.File;

public class ST2ODReporter extends SymbolTableReporter2 {

  public ST2ODReporter(String outputDir, String modelName, ReportingRepository repository) {
    super(outputDir + File.separator + ReportingConstants.REPORTING_DIR + File.separator, modelName,
        repository);
    setPrintAllFieldsCommented(true);
    setPrintEmptyList(true);
    setPrintEmptyOptional(true);
  }

  /**
   * @see de.monticore.generating.templateengine.reporting.reporter.SymbolTableReporter2#reportAttributes(de.monticore.symboltable.ISymbol,
   * IndentPrinter)
   */
  @Override
  protected void reportAttributes(ISymbol sym, IndentPrinter printer) {
    super.reportAttributes(sym, printer);

    if (sym instanceof ODObjectSymbol) {
      // print object type
      printer.print("type = ");
      printer.print(repository.getASTNodeNameFormatted(((ODObjectSymbol) sym).getAstNode()));
      printer.println(";");
    }
  }

}
