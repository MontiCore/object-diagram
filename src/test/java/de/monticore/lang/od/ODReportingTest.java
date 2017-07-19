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
package de.monticore.lang.od;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.junit.BeforeClass;
import org.junit.Test;

import de.monticore.generating.templateengine.reporting.commons.ReportingRepository;
import de.monticore.io.paths.ModelPath;
import de.monticore.lang.od._ast.ASTODArtifact;
import de.monticore.lang.od._cocos.*;
import de.monticore.lang.od._parser.ODParser;
import de.monticore.lang.od._symboltable.ODLanguage;
import de.monticore.lang.od._symboltable.ODSymbolTableCreator;
import de.monticore.lang.od._symboltable.ObjectDiagramSymbol;
import de.monticore.lang.od.report.AST2ODReporter;
import de.monticore.lang.od.report.ODNodeIdentHelper;
import de.monticore.lang.od.report.ST2ODReporter;
import de.monticore.symboltable.GlobalScope;
import de.monticore.symboltable.ResolvingConfiguration;

/**
 * Test for {@link ODParser}.
 *
 * @author Marita Breuer
 */
public class ODReportingTest {

  private static ODLanguage odLanguage;

  private static ResolvingConfiguration resolverConfiguration;

  private static ModelPath modelPath;

  @BeforeClass
  public static void setup() {
    odLanguage = new ODLanguage();

    resolverConfiguration = new ResolvingConfiguration();
    resolverConfiguration.addDefaultFilters(odLanguage.getResolvers());
  }

  private void createAST(String packageName, String modelName) throws IOException {
    Path model = Paths.get("src/test/resources/" + packageName + modelName + ".od");
    modelPath =
        new ModelPath(Paths.get("src/test/resources/" + packageName));
    ODParser parser = new ODParser();
    Optional<ASTODArtifact> ASTODArtifact = parser.parseODArtifact(model.toString());
    assertFalse(parser.hasErrors());
    assertTrue(ASTODArtifact.isPresent());

    GlobalScope globalScope = new GlobalScope(modelPath, odLanguage, resolverConfiguration);

    Optional<ODSymbolTableCreator> symbolTable = odLanguage.getSymbolTableCreator(
        resolverConfiguration, globalScope);

    if (symbolTable.isPresent()) {
      symbolTable.get().createFromAST(ASTODArtifact.get());
    }

    globalScope.<ObjectDiagramSymbol>resolve(modelName, ObjectDiagramSymbol.KIND).orElse(null);

    ReportingRepository reporting = new ReportingRepository(new ODNodeIdentHelper());

    // Report AST
    AST2ODReporter reporter = new AST2ODReporter("target", modelName, reporting);
    reporter.flush(ASTODArtifact.get().getObjectDiagram());

    // Report ST
    ST2ODReporter st2ODReporter = new ST2ODReporter("target", modelName, reporting);
    st2ODReporter.flush(ASTODArtifact.get().getObjectDiagram());

  }

  @Test
  public void reportInnerLinkVariants() throws IOException {
    createAST("examples/hierarchical/", "InnerLinkVariants");

    Path astModel = Paths.get("target/reports/InnerLinkVariants/InnerLinkVariants_AST.od");
    Path stModel = Paths.get("target/reports/InnerLinkVariants/InnerLinkVariants_ST.od");

    // Initialize the CoCoChecker
    // ODCoCoChecker odCoCoChecker = new ODCoCoChecker();
    // odCoCoChecker.addCoCo(new UniqueObjectNamesCoCo());
    // odCoCoChecker.addCoCo(new ValidObjectReferenceCoCo());
    // odCoCoChecker.addCoCo(new ValidLinkReferenceCoCo());
    // odCoCoChecker.addCoCo(new LinkConsistsOfReferenceNamesCoCo());

    // Get the Parser
    ODParser parser = new ODParser();

    // Parse and check AST-Report
    Optional<ASTODArtifact> ASTODArtifact = parser.parseODArtifact(astModel.toString());
    assertFalse(parser.hasErrors());
    assertTrue(ASTODArtifact.isPresent());
    // odCoCoChecker.checkAll(ASTODArtifact.get());

    // Parse and check ST-Report
    ASTODArtifact = parser.parseODArtifact(stModel.toString());
    assertFalse(parser.hasErrors());
    assertTrue(ASTODArtifact.isPresent());
    // odCoCoChecker.checkAll(ASTODArtifact.get());
  }

}
