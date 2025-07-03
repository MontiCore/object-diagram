// (c) https://github.com/MontiCore/monticore

package de.monticore.od4report;

import de.monticore.ODOutTestBasis;
import de.monticore.runtime.junit.MCAssertions;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OD4ReportToolAggregationTest extends ODOutTestBasis {
  
  private final String[] SYMTYPE_ARGS =
      { "-symboltypes", "de.monticore.cdbasis._symboltable.CDTypeSymbol", "TypeSymbolDeSer",
          "de.monticore.types.check.SymTypeOfObject", "TypeSymbolDeSer",
          "de.monticore.cdassociation._symboltable.CDRoleSymbol", "FieldSymbolDeSer",
          "de.monticore.cdbasis._symboltable.CDTypeSymbol", "TypeSymbolDeSer",
          "de.monticore.symbols.oosymbols._symboltable.FieldSymbol", "FieldSymbolDeSer" };
  
  @BeforeEach
  void setUp() {
    OD4ReportMill.reset();
  }
  
  @Test
  public void testOD4ReportSymboltypesTOGS() {
    String[] basisArgs = { "-i", "src/test/resources/symboltable/aggregation/TestOD.od", "-path",
        "src/test/resources/symboltable/aggregation/cd" };
    String[] testArgs = ArrayUtils.addAll(basisArgs, SYMTYPE_ARGS);
    OD4ReportTool.main(testArgs);
    
    assertEquals(String.format(OD4ReportTool.PARSE_SUCCESSFUL, "TestOD"), getOut());
    MCAssertions.assertNoFindings();
  }
  
  @Test
  public void testOD4ReportSymboltypesTOGS2() {
    String[] basisArgs =
        { "-i", "src/test/resources/symboltable/aggregation/BasicGameOD.od", "-path",
            "src/test/resources/symboltable/aggregation/basicgame_cd" };
    String[] testArgs = ArrayUtils.addAll(basisArgs, SYMTYPE_ARGS);
    OD4ReportTool.main(testArgs);
    
    assertEquals(String.format(OD4ReportTool.PARSE_SUCCESSFUL, "BasicGameOD"), getOut());
    MCAssertions.assertNoFindings();
  }
  
  @Test
  public void testOD4ReportSymboltypesOdd() {
    String[] basisArgs =
        { "-i", "src/test/resources/symboltable/aggregation/BasicGameOD.od", "-path",
            "src/test/resources/symboltable/aggregation/basicgame_cd" };
    String[] oddSymtypeArgs = Arrays.copyOf(SYMTYPE_ARGS, SYMTYPE_ARGS.length - 1);
    String[] testArgs = ArrayUtils.addAll(basisArgs, oddSymtypeArgs);
    OD4ReportTool.main(testArgs);
    
    assertEquals(String.format(OD4ReportTool.PARSE_SUCCESSFUL, "BasicGameOD"), getOut());
    MCAssertions.assertHasFindingStartingWith(
        "Odd number of arguments for parameter -symboltypes! Ignoring last argument.");
  }
  
}
