/* (c) https://github.com/MontiCore/monticore */

package de.monticore.odbasis._symboltable;

import de.monticore.od4development.OD4DevelopmentMill;
import de.monticore.od4development.OD4DevelopmentTestUtil;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.runtime.junit.MCAssertions;
import de.monticore.runtime.junit.TestWithMCLanguage;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link ODBasisSymbolTableCompleter}.
 */
@TestWithMCLanguage(OD4DevelopmentMill.class)
public class ODBasisSymbolTableCompleterTest {

  /** Several completers without type checks share a single {@code DefaultObject} type. */
  @Test
  public void testSingleDefaultObjectType() throws IOException {
    ASTODArtifact first = parse("objectdiagram A { a:T {}; }");
    ASTODArtifact second = parse("objectdiagram B { b:T {}; }");
    OD4DevelopmentTestUtil.createSymbolTableFromAST(first);
    OD4DevelopmentTestUtil.createSymbolTableFromAST(second);

    OD4DevelopmentTestUtil.completeSymbolTable(first, false);
    OD4DevelopmentTestUtil.completeSymbolTable(second, false);

    assertEquals(1, OD4DevelopmentMill.globalScope().getTypeSymbols()
        .get(ODBasisSymbolTableCompleter.DEFAULT_OBJECT).size());
  }

  /** No {@code DefaultObject} type is added if the types are checked. */
  @Test
  public void testNoDefaultObjectTypeIfTypesAreChecked() throws IOException {
    ASTODArtifact artifact = parse("objectdiagram A { a:T {}; }");
    OD4DevelopmentTestUtil.createSymbolTableFromAST(artifact);

    OD4DevelopmentTestUtil.completeSymbolTable(artifact, true);

    MCAssertions.assertHasFindingStartingWith(
        ODBasisSymbolTableCompleter.ERROR_TYPE_NOT_CALCULATED.formatted("T", "a"));
    MCAssertions.assertHasFindingStartingWith("0xA0324");
    assertTrue(OD4DevelopmentMill.globalScope().getTypeSymbols()
        .get(ODBasisSymbolTableCompleter.DEFAULT_OBJECT).isEmpty());
  }

  protected ASTODArtifact parse(String model) throws IOException {
    return OD4DevelopmentMill.parser().parse_StringODArtifact(model).orElseThrow();
  }
}
