/* (c) https://github.com/MontiCore/monticore */
package de.monticore.od2plantuml;

import de.monticore.od4report.OD4ReportMill;
import de.monticore.runtime.junit.MCAssertions;
import de.monticore.runtime.junit.TestWithMCLanguage;
import net.sourceforge.plantuml.FileFormat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Tests {@link ODPlantUMLTool#generateImage} in-process.
 */
@TestWithMCLanguage(OD4ReportMill.class)
class ODPlantUMLToolImageTest {

  @TempDir
  Path tmp;

  /** PlantUML renders an image showing the error for invalid sources, which must not be saved. */
  @Test
  void shouldNotSaveImageOfInvalidPlantUML() {
    Path destination = tmp.resolve("invalid");

    new ODPlantUMLTool().generateImage("@startuml\nobject a\na ][ --> b\n@enduml\n",
        destination.toString(), FileFormat.SVG);

    MCAssertions.assertHasFindingStartingWith("0x0D033");
    assertFalse(Files.exists(tmp.resolve("invalid.svg")));
  }
}
