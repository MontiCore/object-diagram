/* (c) https://github.com/MontiCore/monticore */

package de.monticore.od4development._symboltable;

import de.monticore.od4development.OD4DevelopmentMill;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.runtime.junit.TestWithMCLanguage;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

@TestWithMCLanguage(OD4DevelopmentMill.class)
public class OD4DevelopmentScopesGenitorTest {

  /** The genitor can be reused, each artifact scope is a sub scope of the global scope. */
  @Test
  public void testReusedGenitor() throws IOException {
    ASTODArtifact first = parse("package p; objectdiagram A { a:T {}; }");
    ASTODArtifact second = parse("objectdiagram B { b:T {}; }");
    OD4DevelopmentScopesGenitorDelegator genitor = OD4DevelopmentMill.scopesGenitorDelegator();

    IOD4DevelopmentArtifactScope firstScope = genitor.createFromAST(first);
    IOD4DevelopmentArtifactScope secondScope = genitor.createFromAST(second);

    assertSame(OD4DevelopmentMill.globalScope(), firstScope.getEnclosingScope());
    assertSame(OD4DevelopmentMill.globalScope(), secondScope.getEnclosingScope());
    assertSame(first, firstScope.getAstNode());
    assertSame(second, secondScope.getAstNode());
    assertEquals("p", firstScope.getPackageName());
    assertEquals("", secondScope.getPackageName());
    assertEquals("A", firstScope.getName());
    assertEquals("B", secondScope.getName());
  }

  protected ASTODArtifact parse(String model) throws IOException {
    return OD4DevelopmentMill.parser().parse_StringODArtifact(model).orElseThrow();
  }
}
