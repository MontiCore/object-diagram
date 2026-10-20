/* (c) https://github.com/MontiCore/monticore */

package de.monticore.od4report._symboltable;

import de.monticore.od4report.OD4ReportMill;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.runtime.junit.TestWithMCLanguage;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

@TestWithMCLanguage(OD4ReportMill.class)
public class OD4ReportScopesGenitorTest {

  /** The genitor can be reused, each artifact scope is a sub scope of the global scope. */
  @Test
  public void testReusedGenitor() throws IOException {
    ASTODArtifact first = parse("package p; objectdiagram A { a:T {}; }");
    ASTODArtifact second = parse("objectdiagram B { b:T {}; }");
    OD4ReportScopesGenitorDelegator genitor = OD4ReportMill.scopesGenitorDelegator();

    IOD4ReportArtifactScope firstScope = genitor.createFromAST(first);
    IOD4ReportArtifactScope secondScope = genitor.createFromAST(second);

    assertSame(OD4ReportMill.globalScope(), firstScope.getEnclosingScope());
    assertSame(OD4ReportMill.globalScope(), secondScope.getEnclosingScope());
    assertSame(first, firstScope.getAstNode());
    assertSame(second, secondScope.getAstNode());
    assertEquals("p", firstScope.getPackageName());
    assertEquals("", secondScope.getPackageName());
    assertEquals("A", firstScope.getName());
    assertEquals("B", secondScope.getName());
  }

  protected ASTODArtifact parse(String model) throws IOException {
    return OD4ReportMill.parser().parse_StringODArtifact(model).orElseThrow();
  }
}
