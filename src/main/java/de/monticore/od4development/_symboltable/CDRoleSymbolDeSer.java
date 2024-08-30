/* (c) https://github.com/MontiCore/monticore */
package de.monticore.od4development._symboltable;

import de.monticore.cdassociation._symboltable.CDRoleSymbol;
import de.monticore.symbols.oosymbols._symboltable.FieldSymbol;
import de.monticore.symbols.oosymbols._symboltable.FieldSymbolDeSer;
import de.monticore.symbols.oosymbols._symboltable.IOOSymbolsScope;
import de.monticore.symboltable.serialization.json.JsonObject;
import de.monticore.od4development._symboltable.CDRoleAdapter.LinkCardinality;

public class CDRoleSymbolDeSer extends FieldSymbolDeSer {
  
  public FieldSymbol deserialize(IOOSymbolsScope scope, JsonObject symbolJson) {
    CDRoleAdapter fs = new CDRoleAdapter(super.deserialize(scope, symbolJson));
    fs.setCardinality(deserializeCardinality(scope, symbolJson));
    fs.setAssocRef(deserializeAssocRef(scope, symbolJson));
    return fs;
  }
  
  protected LinkCardinality deserializeCardinality(IOOSymbolsScope scope, JsonObject symbolJson) {
    String cardStr = symbolJson.getStringMemberOpt("cardinality").orElse("[1]");
    return LinkCardinality.fromValue(cardStr);
  }
  
  protected int deserializeAssocRef(IOOSymbolsScope scope, JsonObject symbolJson) {
    return symbolJson.getIntegerMemberOpt("association").orElse(-1);
  }
  
  
}
