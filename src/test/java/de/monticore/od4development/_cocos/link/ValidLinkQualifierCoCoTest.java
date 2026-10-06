/* (c) https://github.com/MontiCore/monticore */
package de.monticore.od4development._cocos.link;

import de.monticore.od4development.OD4DevelopmentMill;
import de.monticore.od4development._cocos.OD4DevelopmentCoCoChecker;
import de.monticore.od4development._parser.OD4DevelopmentParser;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.runtime.junit.MCAssertions;
import de.monticore.runtime.junit.TestWithMCLanguage;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Tests for {@link ValidLinkQualifierCoCo}.
 */
@TestWithMCLanguage(OD4DevelopmentMill.class)
class ValidLinkQualifierCoCoTest {

  @ParameterizedTest
  @ValueSource(strings = { "[[name]]", "[1]", "[\"key\"]", "[name]" })
  void shouldAcceptValidQualifier(String qualifier) throws IOException {
    check(qualifier);

    MCAssertions.assertNoFindings();
  }

  @ParameterizedTest
  @ValueSource(strings = { "[ [1, 2] ]", "[[1, 2]]", "[ [name] ]", "[ [] ]" })
  void shouldReportListQualifier(String qualifier) throws IOException {
    check(qualifier);

    MCAssertions.assertHasFindingStartingWith(ValidLinkQualifierCoCo.ERROR_LIST_QUALIFIER);
  }

  @ParameterizedTest
  @ValueSource(strings = { "[ [1 -> 2] ]", "[[1 -> 2]]", "[ [\"a\" -> 1, \"b\" -> 2] ]" })
  void shouldReportMapQualifier(String qualifier) throws IOException {
    check(qualifier);

    MCAssertions.assertHasFindingStartingWith(ValidLinkQualifierCoCo.ERROR_MAP_QUALIFIER);
  }

  @ParameterizedTest
  @ValueSource(strings = { "[ :C {} ]", "[ c:C {} ]", "[ :C { x = 1; } ]" })
  void shouldReportObjectQualifier(String qualifier) throws IOException {
    check(qualifier);

    MCAssertions.assertHasFindingStartingWith(ValidLinkQualifierCoCo.ERROR_OBJECT_QUALIFIER);
  }

  private static void check(String qualifier) throws IOException {
    OD4DevelopmentParser parser = OD4DevelopmentMill.parser();
    ASTODArtifact artifact = parser.parse_StringODArtifact(
        "objectdiagram Q { a:A {}; b:B {}; link a " + qualifier + " -> b; }").orElseThrow();
    assertFalse(parser.hasErrors());

    OD4DevelopmentCoCoChecker checker = new OD4DevelopmentCoCoChecker();
    checker.addCoCo(new ValidLinkQualifierCoCo());
    checker.checkAll(artifact);
  }
}
