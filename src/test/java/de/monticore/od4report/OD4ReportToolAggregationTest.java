// (c) https://github.com/MontiCore/monticore

package de.monticore.od4report;

import de.se_rwth.commons.logging.Log;
import de.se_rwth.commons.logging.LogStub;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class OD4ReportToolAggregationTest {

  //Todo: Does not work due to unavailability of DeSer to deserialize symbols of kind de.monticore.cdbasis._symboltable.CDPackageSymbol

  @Before
  public void setUp() {
    LogStub.init();
    Log.enableFailQuick(false);
  }

  @Test
  @Ignore
  public void testOD4ReportSymboltypesTOGS() {

    //Used in -symtypes option to map CD Symbols to Deserializers
    String symtypes_args = "de.monticore.cdbasis._symboltable.CDTypeSymbol TypeSymbolDeSer de.monticore.types.check.SymTypeOfObject TypeSymbolDeSer de.monticore.cdassociation._symboltable.CDRoleSymbol FieldSymbolDeSer de.monticore.cdbasis._symboltable.CDTypeSymbol TypeSymbolDeSer de.monticore.symbols.oosymbols._symboltable.FieldSymbol FieldSymbolDeSer";

    //Use Case Game
    //String[] input_1 = { "-i", "src/test/resources/symboltable/aggregation/BasicGameOD.od", "-symboltypes", symtypes_args, "-path", "src/test/resources/symboltable/aggregation/basicgame_cd"};
    //OD4ReportTool.main(input_1);


    // Minimized Test
    //Names of Diagram and file have to be equal, otherwise MontiCore is unable to load the symbol table
    String[] input_2 = {"-i", "src/test/resources/symboltable/aggregation/TestOD.od", "-symboltypes", symtypes_args, "-path", "src/test/resources/symboltable/aggregation/cd"};
    OD4ReportTool.main(input_2);
  }

}
