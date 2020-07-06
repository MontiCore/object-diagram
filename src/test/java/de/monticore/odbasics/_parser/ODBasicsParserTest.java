// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore

package de.monticore.odbasics._parser;

import de.monticore.odbasics._ast.*;
import de.se_rwth.commons.logging.Log;
import de.se_rwth.commons.logging.Slf4jLog;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;

import static org.junit.Assert.*;

public class ODBasicsParserTest {

  @BeforeClass
  public static void disableFailQuick() {
    Slf4jLog.init();
    Log.enableFailQuick(false);
  }

  @Test
  public void testODLinkLeftSide() throws IOException {
    ODBasicsParser odBasicsParser = new ODBasicsParser();
    Optional<ASTODLinkLeftSide> linkLeftSide = odBasicsParser
        .parse_StringODLinkLeftSide("public " + "A1,A2 [[test]] (eineRolle)");
    assertTrue(linkLeftSide.isPresent());
    assertEquals(2, linkLeftSide.get().sizeReferenceNames());
    assertEquals("test", linkLeftSide.get().getODLinkQualifier().getName());
    assertFalse(linkLeftSide.get().getODLinkQualifier().isPresentODValue());
  }

  @Test
  public void testODLinkRightSide() throws IOException {
    ODBasicsParser odBasicsParser = new ODBasicsParser();
    Optional<ASTODLinkRightSide> linkRightSide = odBasicsParser
        .parse_StringODLinkRightSide("(eineRolle) [[test]] A1,A2 private");
    assertTrue(linkRightSide.isPresent());
    assertEquals(2, linkRightSide.get().sizeReferenceNames());
    assertEquals("test", linkRightSide.get().getODLinkQualifier().getName());
    assertFalse(linkRightSide.get().getODLinkQualifier().isPresentODValue());
  }

  @Test
  public void testODLink() throws IOException {
    ODBasicsParser odBasicsParser = new ODBasicsParser();
    Optional<ASTODLink> link = odBasicsParser.parse_StringODLink("link A -- B");
    assertTrue(link.isPresent());
  }

  @Test
  public void testODLinkDirection() throws IOException {
    ODBasicsParser odBasicsParser = new ODBasicsParser();
    Optional<ASTODUnspecifiedDir> unspecifiedDir = odBasicsParser.parse_StringODUnspecifiedDir(
        "--");
    Optional<ASTODLinkDirection> linkDirection = odBasicsParser.parse_StringODLinkDirection("--");
    assertTrue(linkDirection.isPresent());
  }

}
