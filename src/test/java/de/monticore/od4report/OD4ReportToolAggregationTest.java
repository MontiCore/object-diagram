// (c) https://github.com/MontiCore/monticore

package de.monticore.od4report;

import de.monticore.ODTestBasis;
import de.se_rwth.commons.logging.RichConsoleLogHook;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OD4ReportToolAggregationTest extends ODTestBasis {
  
  private final String[] SYMTYPE_ARGS =
      { "-symboltypes", "de.monticore.cdbasis._symboltable.CDTypeSymbol", "TypeSymbolDeSer",
          "de.monticore.types.check.SymTypeOfObject", "TypeSymbolDeSer",
          "de.monticore.cdassociation._symboltable.CDRoleSymbol", "FieldSymbolDeSer",
          "de.monticore.cdbasis._symboltable.CDTypeSymbol", "TypeSymbolDeSer",
          "de.monticore.symbols.oosymbols._symboltable.FieldSymbol", "FieldSymbolDeSer" };
  
  @Test
  public void testOD4ReportSymboltypesTOGS() {
    String[] basisArgs = { "-i", "src/test/resources/symboltable/aggregation/TestOD.od", "-path",
        "src/test/resources/symboltable/aggregation/cd" };
    String[] testArgs = ArrayUtils.addAll(basisArgs, SYMTYPE_ARGS);
    List<String> out = OD4ReportTestUtil.runToolInSeparateProcess(testArgs);
    
    assertEquals(1, out.size());
    assertEquals(getAsInfo(OD4ReportTool.PARSE_SUCCESSFUL,"TestOD"), out.getFirst());
  }
  
  @Test
  public void testOD4ReportSymboltypesTOGS2() {
    String[] basisArgs =
        { "-i", "src/test/resources/symboltable/aggregation/BasicGameOD.od", "-path",
            "src/test/resources/symboltable/aggregation/basicgame_cd" };
    String[] testArgs = ArrayUtils.addAll(basisArgs, SYMTYPE_ARGS);
    List<String> out = OD4ReportTestUtil.runToolInSeparateProcess(testArgs);
    
    assertEquals(1, out.size());
    assertEquals(getAsInfo(OD4ReportTool.PARSE_SUCCESSFUL,"BasicGameOD"), out.getFirst());
  }
  
  @Test
  public void testOD4ReportSymboltypesOdd() {
    String[] basisArgs =
        { "-i", "src/test/resources/symboltable/aggregation/BasicGameOD.od", "-path",
            "src/test/resources/symboltable/aggregation/basicgame_cd" };
    String[] oddSymtypeArgs = Arrays.copyOf(SYMTYPE_ARGS, SYMTYPE_ARGS.length - 1);
    String[] testArgs = ArrayUtils.addAll(basisArgs, oddSymtypeArgs);
    List<String> out = OD4ReportTestUtil.runToolInSeparateProcess(testArgs);
    
    assertEquals(2, out.size());
    assertEquals(getAsWarn(OD4ReportTool.WARN_ODD_SYMBOLTYPES_ARGS), out.get(0));
    assertEquals(getAsInfo(OD4ReportTool.PARSE_SUCCESSFUL,"BasicGameOD"), out.get(1));
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
