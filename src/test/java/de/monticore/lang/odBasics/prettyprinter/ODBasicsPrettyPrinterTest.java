// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore

package de.monticore.lang.odbasics.prettyprinter;

import de.monticore.lang.od4report.OD4ReportTool;
import de.monticore.lang.od4report._parser.OD4ReportParser;
import de.monticore.lang.od4report._symboltable.OD4ReportLanguage;
import de.monticore.lang.odbasics._ast.ASTODArtifact;
import de.monticore.lang.odbasics._ast.ASTObjectDiagram;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link ODBasicsPrettyPrinter}
 *
 * @author hillemacher
 */
public class ODBasicsPrettyPrinterTest {

  private final Path INPUT_INNNERLINK_PATH = Paths
      .get("src", "test", "resources", "examples", "hierarchical", "InnerLinkVariants.od");

  private final Path INPUT_SIMPLEOD_PATH = Paths
      .get("src", "test", "resources", "examples", "od", "SimpleOD.od");

  private OD4ReportParser odBasicsParser = new OD4ReportParser();

  @Test
  public void testPrettyPrinter() throws IOException {
    Optional<ASTODArtifact> astodArtifact = this.odBasicsParser
        .parseODArtifact(INPUT_INNNERLINK_PATH.toString());

    assertTrue(astodArtifact.isPresent());

    final OD4ReportLanguage odBasicsLanguage = new OD4ReportLanguage();
    OD4ReportTool.createSymbolTable(odBasicsLanguage, astodArtifact.get());

    ASTObjectDiagram objectDiagram = astodArtifact.get().getObjectDiagram();
    ASTObjectDiagram objectDiagram2 = objectDiagram.deepClone();

    assertTrue(objectDiagram.deepEquals(objectDiagram2));

    ASTODArtifact astodArtifact1 = OD4ReportTool.parse(INPUT_SIMPLEOD_PATH.toString());
    OD4ReportTool.createSymbolTable(odBasicsLanguage, astodArtifact1);

    assertNotNull(astodArtifact1);

    objectDiagram2 = astodArtifact1.getObjectDiagram().deepClone();
    assertTrue(astodArtifact1.getObjectDiagram().deepEquals(objectDiagram2));
  }

}
