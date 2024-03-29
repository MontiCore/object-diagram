// (c) https://github.com/MontiCore/monticore

package de.monticore.od4data._parser;

import de.monticore.od4report.OD4ReportMill;
import de.monticore.odlink._ast.*;
import de.se_rwth.commons.logging.Log;
import de.se_rwth.commons.logging.LogStub;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;

import static org.junit.Assert.*;

public class ODBasicsParserTest {

  @BeforeClass
  public static void disableFailQuick() {
    LogStub.init();
    Log.enableFailQuick(false);
    OD4ReportMill.reset();
    OD4ReportMill.init();
  }

  @Test
  public void testODLinkLeftSide() throws IOException {
    OD4DataParser odBasicsParser = new OD4DataParser();
    Optional<ASTODLinkLeftSide> linkLeftSide = odBasicsParser
        .parse_StringODLinkLeftSide("public " + "A1,A2 [[test]] (eineRolle)");
    assertTrue(linkLeftSide.isPresent());
    assertEquals(2, linkLeftSide.get().sizeReferenceNames());
    assertEquals("test", linkLeftSide.get().getODLinkQualifier().getName());
    assertFalse(linkLeftSide.get().getODLinkQualifier().isPresentODValue());
  }

  @Test
  public void testODLinkRightSide() throws IOException {
    OD4DataParser odBasicsParser = new OD4DataParser();
    Optional<ASTODLinkRightSide> linkRightSide = odBasicsParser
        .parse_StringODLinkRightSide("(eineRolle) [[test]] A1,A2 private");
    assertTrue(linkRightSide.isPresent());
    assertEquals(2, linkRightSide.get().sizeReferenceNames());
    assertEquals("test", linkRightSide.get().getODLinkQualifier().getName());
    assertFalse(linkRightSide.get().getODLinkQualifier().isPresentODValue());
  }

  @Test
  public void testODLink() throws IOException {
    OD4DataParser odBasicsParser = new OD4DataParser();
    Optional<ASTODLink> link = odBasicsParser.parse_StringODLink("link A -- B");
    assertTrue(link.isPresent());
  }

  @Test
  public void testODLinkDirection() throws IOException {
    OD4DataParser odBasicsParser = new OD4DataParser();
    Optional<ASTODUnspecifiedDir> unspecifiedDir = odBasicsParser.parse_StringODUnspecifiedDir(
        "--");
    Optional<ASTODLinkDirection> linkDirection = odBasicsParser.parse_StringODLinkDirection("--");
    assertTrue(linkDirection.isPresent());
  }

}
