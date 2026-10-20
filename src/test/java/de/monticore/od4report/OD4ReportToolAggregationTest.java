// (c) https://github.com/MontiCore/monticore

package de.monticore.od4report;

import de.monticore.ODTestBasis;
import de.se_rwth.commons.logging.RichConsoleLogHook;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests that {@code -symboltypes} makes symbols of foreign languages, here of CDs, loadable, such
 * that the object types of an OD can be resolved. The CoCos are checked ({@code -c}), as the types
 * are only resolved when they are checked.
 */
public class OD4ReportToolAggregationTest extends ODTestBasis {

  private final Path AGGREGATION = PATH.resolve("symboltable").resolve("aggregation");

  private final String[] SYMTYPE_ARGS =
      { "-symboltypes", "de.monticore.cdbasis._symboltable.CDTypeSymbol", "TypeSymbolDeSer",
          "de.monticore.cdassociation._symboltable.CDRoleSymbol", "FieldSymbolDeSer" };

  @Test
  public void testOD4ReportSymboltypesTOGS() {
    List<String> out = runTool("TestOD.od", "cd", SYMTYPE_ARGS);

    assertEquals(2, out.size(), String.join("\n", out));
    assertEquals(getAsInfo(OD4ReportTool.PARSE_SUCCESSFUL, "TestOD"), out.getFirst());
    assertEquals(getAsInfo(OD4ReportTool.CHECK_SUCCESSFUL, "TestOD"), out.get(1));
  }

  @Test
  public void testOD4ReportSymboltypesTOGS2() {
    List<String> out = runTool("BasicGameOD.od", "basicgame_cd", SYMTYPE_ARGS);

    assertEquals(2, out.size(), String.join("\n", out));
    assertEquals(getAsInfo(OD4ReportTool.PARSE_SUCCESSFUL, "BasicGameOD"), out.getFirst());
    assertEquals(getAsInfo(OD4ReportTool.CHECK_SUCCESSFUL, "BasicGameOD"), out.get(1));
  }

  /** Without {@code -symboltypes}, the CD types cannot be loaded and resolved. */
  @Test
  public void testOD4ReportWithoutSymboltypes() {
    List<String> out = runTool("TestOD.od", "cd");

    assertEquals(getAsInfo(OD4ReportTool.PARSE_SUCCESSFUL, "TestOD"), out.getFirst());
    assertTrue(out.stream().anyMatch(line ->
            line.contains("0xA0324 Cannot find symbol BasicGameCD.basic.game.Game")),
        String.join("\n", out));
  }

  /** The last argument without a deserializer is ignored, the other pairs are still used. */
  @Test
  public void testOD4ReportSymboltypesOdd() {
    String[] oddSymtypeArgs = Arrays.copyOf(SYMTYPE_ARGS, SYMTYPE_ARGS.length - 1);
    List<String> out = runTool("BasicGameOD.od", "basicgame_cd", oddSymtypeArgs);

    assertEquals(3, out.size(), String.join("\n", out));
    assertEquals(getAsWarn(OD4ReportTool.WARN_ODD_SYMBOLTYPES_ARGS), out.getFirst());
    assertEquals(getAsInfo(OD4ReportTool.PARSE_SUCCESSFUL, "BasicGameOD"), out.get(1));
    assertEquals(getAsInfo(OD4ReportTool.CHECK_SUCCESSFUL, "BasicGameOD"), out.get(2));
  }

  /** Runs the tool on the model with the symbol path and checks all CoCos. */
  protected List<String> runTool(String model, String symbolPath, String... additionalArgs) {
    String[] args = { "-i", AGGREGATION.resolve(model).toString(), "-path",
        AGGREGATION.resolve(symbolPath).toString(), "-c" };
    return OD4ReportTestUtil.runToolInSeparateProcess(ArrayUtils.addAll(args, additionalArgs));
  }

  protected String getAsInfo(String base, String... data) {
    return RichConsoleLogHook.BLUE + "[INFO]" + RichConsoleLogHook.RESET
        + "  de.monticore.od4report.OD4ReportTool " + base.formatted(data).strip();
  }

  protected String getAsWarn(String base, String... data) {
    return RichConsoleLogHook.YELLOW + "[WARN]" + RichConsoleLogHook.RESET + "  "
        + base.formatted(data).strip();
  }
}
