package de.monticore.odbasis._symboltable;

import de.monticore.odbasis.ODBasisMill;
import de.monticore.odbasis._ast.ASTODNamedObject;
import de.monticore.odbasis._visitor.ODBasisHandler;
import de.monticore.odbasis._visitor.ODBasisTraverser;
import de.monticore.odbasis._visitor.ODBasisVisitor2;
import de.monticore.symbols.basicsymbols._symboltable.TypeSymbol;
import de.monticore.symboltable.modifiers.AccessModifier;
import de.monticore.types.check.SymTypeExpression;
import de.monticore.types.check.SymTypeExpressionFactory;
import de.monticore.types.mcbasictypes._ast.ASTMCObjectType;
import de.monticore.types3.TypeCheck3;
import de.se_rwth.commons.logging.Log;

public class ODBasisSymbolTableCompleter implements ODBasisVisitor2, ODBasisHandler {
  
  protected ODBasisTraverser traverser;
  
  protected boolean checkTypes;
  
  private SymTypeExpression defaultObjectType;
  
  
  public ODBasisSymbolTableCompleter(boolean checkTypes) {
    this.traverser = null;
    this.checkTypes = checkTypes;
    
    IODBasisGlobalScope gs = ODBasisMill.globalScope();
    TypeSymbol defaultObjectTypeSymbol = ODBasisMill.typeSymbolBuilder()
        .setName("DefaultObject")
        .setFullName("DefaultObject")
        .setEnclosingScope(gs)
        .setSpannedScope(ODBasisMill.scope())
        .setAccessModifier(AccessModifier.ALL_INCLUSION)
        .build();
    gs.add(defaultObjectTypeSymbol);
    this.defaultObjectType = SymTypeExpressionFactory.createTypeObject(defaultObjectTypeSymbol);
  }
  
  @Override
  public void endVisit(ASTODNamedObject node) {
    if (checkTypes) {
      ASTMCObjectType objectType = node.getMCObjectType();
      final SymTypeExpression typeResult = TypeCheck3.symTypeFromAST(objectType);
      if (typeResult.isObscureType()) {
        Log.error(String.format("0x0D013: The type of the return type (%s) could not be calculated",
                node.getMCObjectType().getClass().getSimpleName()),
            node.getMCObjectType().get_SourcePositionStart());
      } else {
        node.getSymbol().setType(typeResult);
      }
    } else {
      node.getSymbol().setType(this.defaultObjectType);
    }
  }
  
  @Override
  public ODBasisTraverser getTraverser() {
    return this.traverser;
  }
  
  @Override
  public void setTraverser(ODBasisTraverser traverser) {
    this.traverser = traverser;
  }
}
