// (c) https://github.com/MontiCore/monticore

package de.monticore.od4development._parser;

import de.monticore.ODTestBasis;
import de.monticore.od4development.OD4DevelopmentMill;
import de.monticore.odattribute._ast.ASTODList;
import de.monticore.odbasis._ast.ASTODValue;
import de.monticore.odlink._ast.*;

import de.monticore.runtime.junit.TestWithMCLanguage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestWithMCLanguage(OD4DevelopmentMill.class)
public class ODBasicsParserTest extends ODTestBasis {
  
  @Test
  public void testODLinkLeftSide() throws IOException {
    OD4DevelopmentParser odBasicsParser = OD4DevelopmentMill.parser();
    Optional<ASTODLinkLeftSide> linkLeftSide =
        odBasicsParser.parse_StringODLinkLeftSide("public " + "A1,A2 [[test]] (eineRolle)");
    assertTrue(linkLeftSide.isPresent());
    assertEquals(2, linkLeftSide.get().sizeReferenceNames());
    assertEquals("test", linkLeftSide.get().getODLinkQualifier().getName());
    assertFalse(linkLeftSide.get().getODLinkQualifier().isPresentODValue());
  }
  
  @Test
  public void testODLinkRightSide() throws IOException {
    OD4DevelopmentParser odBasicsParser = OD4DevelopmentMill.parser();
    Optional<ASTODLinkRightSide> linkRightSide =
        odBasicsParser.parse_StringODLinkRightSide("(eineRolle) [[test]] A1,A2 private");
    assertTrue(linkRightSide.isPresent());
    assertEquals(2, linkRightSide.get().sizeReferenceNames());
    assertEquals("test", linkRightSide.get().getODLinkQualifier().getName());
    assertFalse(linkRightSide.get().getODLinkQualifier().isPresentODValue());
  }
  
  @Test
  public void testODLink() throws IOException {
    OD4DevelopmentParser odBasicsParser = OD4DevelopmentMill.parser();
    Optional<ASTODLink> link = odBasicsParser.parse_StringODLink("link A -- B");
    assertTrue(link.isPresent());
  }
  
  /** "[[" and "]]" are split tokens, so they may appear in values. */
  @ParameterizedTest
  @ValueSource(strings = { "[[1, 2], [3]]", "[ [1, 2], [3]]", "a[b[1]]" })
  public void testODValueWithDoubleBrackets(String input) throws IOException {
    OD4DevelopmentParser parser = OD4DevelopmentMill.parser();
    Optional<ASTODValue> value = parser.parse_StringODValue(input);
    assertFalse(parser.hasErrors());
    assertTrue(value.isPresent());
  }

  @ParameterizedTest
  @ValueSource(strings = { "[[x]]", "[[ x ]]" })
  public void testODLinkQualifierName(String input) throws IOException {
    OD4DevelopmentParser parser = OD4DevelopmentMill.parser();
    Optional<ASTODLinkQualifier> qualifier = parser.parse_StringODLinkQualifier(input);
    assertFalse(parser.hasErrors());
    assertTrue(qualifier.isPresent());
    assertEquals("x", qualifier.get().getName());
  }

  /** Brackets separated by whitespace denote a list value, not a name qualifier. */
  @ParameterizedTest
  @ValueSource(strings = { "[ [x] ]", "[ [x]]", "[[1, 2]]" })
  public void testODLinkQualifierListValue(String input) throws IOException {
    OD4DevelopmentParser parser = OD4DevelopmentMill.parser();
    Optional<ASTODLinkQualifier> qualifier = parser.parse_StringODLinkQualifier(input);
    assertFalse(parser.hasErrors());
    assertTrue(qualifier.isPresent());
    assertFalse(qualifier.get().isPresentName());
    assertInstanceOf(ASTODList.class, qualifier.get().getODValue());
  }

  @Test
  public void testODLinkDirection() throws IOException {
    OD4DevelopmentParser odBasicsParser = OD4DevelopmentMill.parser();
    Optional<ASTODUnspecifiedDir> unspecifiedDir =
        odBasicsParser.parse_StringODUnspecifiedDir("--");
    Optional<ASTODLinkDirection> linkDirection = odBasicsParser.parse_StringODLinkDirection("--");
    assertTrue(unspecifiedDir.isPresent());
    assertTrue(linkDirection.isPresent());
  }
  
}
