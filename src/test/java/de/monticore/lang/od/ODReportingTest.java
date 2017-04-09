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
package de.monticore.lang.od;

import de.monticore.generating.templateengine.reporting.commons.ReportingRepository;
import de.monticore.lang.od._ast.ASTODArtefact;
import de.monticore.lang.od._parser.ODParser;
import de.monticore.lang.od.report.AST2ODReporter;
import de.monticore.lang.od.report.ODNodeIdentHelper;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link ODParser}.
 *
 * @author Marita Breuer
 */
public class ODReportingTest {

  private void createAST(String packageName, String modelName) throws IOException {
    Path model = Paths.get("src/test/resources/" + packageName + modelName + ".od");
    ODParser parser = new ODParser();
    Optional<ASTODArtefact> odDef = parser.parseODArtefact(model.toString());
    assertFalse(parser.hasErrors());
    assertTrue(odDef.isPresent());

    ReportingRepository reporting = new ReportingRepository(new ODNodeIdentHelper());

    // Report AST
    AST2ODReporter reporter = new AST2ODReporter("target", modelName, reporting);
    reporter.flush(odDef.get());

  }

  @Test
  public void reportInnerLinkVariants() throws IOException {
    createAST("examples/hierarchical/", "InnerLinkVariants");
  }

}
