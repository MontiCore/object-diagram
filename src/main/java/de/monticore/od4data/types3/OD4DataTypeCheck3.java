/*
 *  (c) https://github.com/MontiCore/monticore
 */

package de.monticore.od4data.types3;

import de.monticore.expressions.commonexpressions.types3.CommonExpressionsCTTIVisitor;
import de.monticore.expressions.expressionsbasis.types3.ExpressionBasisCTTIVisitor;
import de.monticore.literals.mccommonliterals.types3.MCCommonLiteralsTypeVisitor;
import de.monticore.od4data.OD4DataMill;
import de.monticore.od4data._visitor.OD4DataTraverser;
import de.monticore.types.mcbasictypes.types3.MCBasicTypesTypeVisitor;
import de.monticore.types3.SymTypeRelations;
import de.monticore.types3.Type4Ast;
import de.monticore.types3.generics.context.InferenceContext4Ast;
import de.monticore.types3.util.*;
import de.monticore.visitor.ITraverser;
import de.se_rwth.commons.logging.Log;

/**
 * TypeCheck3 implementation for the OD4Data language.
 * After calling {@link #init()},
 * this implementation will be available through the TypeCheck3 interface.
 */
public class OD4DataTypeCheck3 extends MapBasedTypeCheck3 {
  
  public static void init() {
    initTC3Delegate();
    SymTypeRelations.init();
    OOWithinTypeBasicSymbolsResolver.init();
    OOWithinScopeBasicSymbolsResolver.init();
    TypeContextCalculator.init();
    TypeVisitorOperatorCalculator.init();
    TypeVisitorLifting.init();
  }
  
  protected static void initTC3Delegate() {
    Log.trace("init OD4DataTypeCheck3", "TypeCheck setup");
    
    OD4DataTraverser traverser = OD4DataMill.inheritanceTraverser();
    Type4Ast type4Ast = new Type4Ast();
    InferenceContext4Ast ctx4Ast = new InferenceContext4Ast();
    
    // Expressions
    
    CommonExpressionsCTTIVisitor visCommonExpressions = new CommonExpressionsCTTIVisitor();
    visCommonExpressions.setType4Ast(type4Ast);
    visCommonExpressions.setContext4Ast(ctx4Ast);
    traverser.add4CommonExpressions(visCommonExpressions);
    traverser.setCommonExpressionsHandler(visCommonExpressions);
    
    ExpressionBasisCTTIVisitor visExpressionBasis = new ExpressionBasisCTTIVisitor();
    visExpressionBasis.setType4Ast(type4Ast);
    visExpressionBasis.setContext4Ast(ctx4Ast);
    traverser.add4ExpressionsBasis(visExpressionBasis);
    traverser.setExpressionsBasisHandler(visExpressionBasis);
    
    MCCommonLiteralsTypeVisitor visMCCommonLiterals = new MCCommonLiteralsTypeVisitor();
    visMCCommonLiterals.setType4Ast(type4Ast);
    traverser.add4MCCommonLiterals(visMCCommonLiterals);
    
    // MCTypes
    
    MCBasicTypesTypeVisitor visMCBasicTypes = new MCBasicTypesTypeVisitor();
    visMCBasicTypes.setType4Ast(type4Ast);
    traverser.add4MCBasicTypes(visMCBasicTypes);
    
    // create delegate
    OD4DataTypeCheck3 oclTC3 = new OD4DataTypeCheck3(traverser, type4Ast, ctx4Ast);
    oclTC3.setThisAsDelegate();
  }
  
  protected OD4DataTypeCheck3(ITraverser typeTraverser, Type4Ast type4Ast,
      InferenceContext4Ast ctx4Ast) {
    super(typeTraverser, type4Ast, ctx4Ast);
  }
}
