/* (c) https://github.com/MontiCore/monticore */
package de.monticore.od4development._symboltable;

import de.monticore.symbols.oosymbols._symboltable.FieldSymbol;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

/**
 * Adapter for importing role symbols from CDs.
 */
public class CDRoleAdapter extends FieldSymbol {
  

  
  protected FieldSymbol original;
  
  protected LinkCardinality cardinality;
  
  protected int assocRef;
  
  public CDRoleAdapter(FieldSymbol original) {
    super(original.getName());
    this.original = original;
  }
  
  public boolean isBidirectional() {
    Optional<CDRoleAdapter> oppositeRole = this.enclosingScope.getVariableSymbols().values().stream()
        .filter(CDRoleAdapter.class::isInstance)
        .map(CDRoleAdapter.class::cast)
        .filter(s -> s.getAssocRef() != -1 && s.getAssocRef() == this.getAssocRef() && !s.equals(this))
        .findAny();
    return oppositeRole.isPresent();
  }
  
  public FieldSymbol getOriginal() {
    return original;
  }
  
  public void setOriginal(FieldSymbol original) {
    this.original = original;
  }
  
  public LinkCardinality getCardinality() {
    return cardinality;
  }
  
  public void setCardinality(LinkCardinality cardinality) {
    this.cardinality = cardinality;
  }
  
  public int getAssocRef() {
    return assocRef;
  }
  
  public void setAssocRef(int assocRef) {
    this.assocRef = assocRef;
  }
  
  public enum LinkCardinality {
    ONE("[1]"),
    OPTIONAL("[?]"),
    STAR("[*]"),
    PLUS("[+]");
    
    protected String val;
    
    LinkCardinality(String val) {
      this.val = val;
    }
    
    public static LinkCardinality fromValue(String value) {
      Optional<LinkCardinality> res = Arrays.stream(values())
          .filter(c -> c.val.equals(value))
          .findAny();
      return res.isPresent() ? res.get() : ONE;
    }
  }

}
