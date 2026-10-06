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

  public static final String ERROR_TYPE_NOT_CALCULATED =
      "0x0D031: The type '%s' of object '%s' could not be calculated.";

  /** Name of the type used for all objects if the types are not checked. */
  public static final String DEFAULT_OBJECT = "de.monticore.internal._DefaultObject";

  protected ODBasisTraverser traverser;

  protected boolean checkTypes;

  /** Created when it is needed for the first time, as it is not used if types are checked. */
  private SymTypeExpression defaultObjectType;


  public ODBasisSymbolTableCompleter(boolean checkTypes) {
    this.traverser = null;
    this.checkTypes = checkTypes;
  }

  @Override
  public void endVisit(ASTODNamedObject node) {
    if (checkTypes) {
      ASTMCObjectType objectType = node.getMCObjectType();
      final SymTypeExpression typeResult = TypeCheck3.symTypeFromAST(objectType);
      if (typeResult.isObscureType()) {
        Log.error(String.format(ERROR_TYPE_NOT_CALCULATED, objectType.printType(), node.getName()),
            objectType.get_SourcePositionStart());
      } else {
        node.getSymbol().setType(typeResult);
      }
    } else {
      node.getSymbol().setType(getDefaultObjectType());
    }
  }

  /**
   * Returns the type for all objects if the types are not checked. The type symbol is added to
   * the global scope only once, also if several completers are used.
   */
  protected SymTypeExpression getDefaultObjectType() {
    if (defaultObjectType == null) {
      IODBasisGlobalScope gs = ODBasisMill.globalScope();
      TypeSymbol defaultObjectTypeSymbol = gs.getTypeSymbols().get(DEFAULT_OBJECT).stream()
          .findFirst()
          .orElseGet(() -> {
            TypeSymbol symbol = ODBasisMill.typeSymbolBuilder()
                .setName(DEFAULT_OBJECT)
                .setFullName(DEFAULT_OBJECT)
                .setEnclosingScope(gs)
                .setSpannedScope(ODBasisMill.scope())
                .setAccessModifier(AccessModifier.ALL_INCLUSION)
                .build();
            gs.add(symbol);
            return symbol;
          });
      defaultObjectType = SymTypeExpressionFactory.createTypeObject(defaultObjectTypeSymbol);
    }
    return defaultObjectType;
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
