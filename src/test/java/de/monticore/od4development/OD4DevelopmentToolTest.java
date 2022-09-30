/* (c) https://github.com/MontiCore/monticore */
package de.monticore.od4development;

import de.monticore.od4data.OD4DataMill;
import de.monticore.od4development._symboltable.IOD4DevelopmentGlobalScope;
import de.monticore.symbols.basicsymbols._symboltable.TypeSymbol;
import de.monticore.symbols.basicsymbols._symboltable.TypeVarSymbol;
import de.monticore.symbols.oosymbols._symboltable.OOTypeSymbol;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class OD4DevelopmentToolTest {

  @Before
  public void setup() {
    OD4DevelopmentMill.init();
    IOD4DevelopmentGlobalScope scope = OD4DevelopmentMill.globalScope();

    OOTypeSymbol objectType = OD4DevelopmentMill.oOTypeSymbolBuilder().setName("A").setEnclosingScope(scope).setSpannedScope(OD4DevelopmentMill.scope()).build();
    OOTypeSymbol objectType2 = OD4DevelopmentMill.oOTypeSymbolBuilder().setName("B").setEnclosingScope(scope).setSpannedScope(OD4DevelopmentMill.scope()).build();

    scope.add(objectType);
    scope.add(objectType2);
  }

  @Test
  public void testTool() {
    OD4DevelopmentToolTOP.main(new String[] {"-i","src/test/resources/examples/od2cd/Example.od"});
    assertTrue(!false);
  }

}
