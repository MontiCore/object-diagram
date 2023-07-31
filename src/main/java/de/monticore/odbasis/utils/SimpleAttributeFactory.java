/*
 * (c) https://github.com/MontiCore/monticore
 */

package de.monticore.odbasis.utils;

import de.monticore.expressions.expressionsbasis._ast.ASTLiteralExpression;
import de.monticore.literals.mccommonliterals._ast.ASTConstantsMCCommonLiterals;
import de.monticore.odbasis.ODBasisMill;
import de.monticore.odbasis._ast.ASTODAttribute;
import de.monticore.odbasis._ast.ASTODAttributeBuilder;
import de.monticore.types.mcbasictypes._ast.ASTConstantsMCBasicTypes;
import de.monticore.umlmodifier._ast.ASTModifier;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;

public class SimpleAttributeFactory {
  
  /**
   * Builds s simple Attribute with an Integer value.
   *
   * @param modifier Modifier of the attribute
   * @param name     Name of the attribute
   * @param value    Value of the attribute
   * @return Attribute for the object
   */
  public static ASTODAttribute createSimpleIntegerAttribute(ASTModifier modifier, String name,
      int value) {
    // modifier and name
    ASTODAttributeBuilder attributeBuilder = init(modifier, name);
    
    // primitive type
    attributeBuilder.setMCType(
        ODBasisMill.mCPrimitiveTypeBuilder().setPrimitive(ASTConstantsMCBasicTypes.INT).build());
    
    // value
    ASTLiteralExpression expression = ODBasisMill.literalExpressionBuilder()
        .setLiteral(ODBasisMill.natLiteralBuilder().setDigits(Integer.toString(value)).build())
        .build();
    attributeBuilder.setODValue(
        ODBasisMill.oDSimpleAttributeValueBuilder().setExpression(expression).build());
    
    return attributeBuilder.build();
  }
  
  /**
   * Builds s simple Attribute with a Long value.
   *
   * @param modifier Modifier of the attribute
   * @param name     Name of the attribute
   * @param value    Value of the attribute
   * @return Attribute for the object
   */
  public static ASTODAttribute createSimpleLongAttribute(ASTModifier modifier, String name,
      long value) {
    // modifier and name
    ASTODAttributeBuilder attributeBuilder = init(modifier, name);
    
    // name
    attributeBuilder.setName(name);
    
    // primitive type
    attributeBuilder.setMCType(
        ODBasisMill.mCPrimitiveTypeBuilder().setPrimitive(ASTConstantsMCBasicTypes.LONG).build());
    
    // value
    ASTLiteralExpression expression = ODBasisMill.literalExpressionBuilder()
        .setLiteral(ODBasisMill.basicLongLiteralBuilder().setDigits(Long.toString(value)).build())
        .build();
    attributeBuilder.setODValue(
        ODBasisMill.oDSimpleAttributeValueBuilder().setExpression(expression).build());
    
    return attributeBuilder.build();
  }
  
  /**
   * Builds s simple Attribute with a Float value.
   *
   * @param modifier Modifier of the attribute
   * @param name     Name of the attribute
   * @param value    Value of the attribute
   * @return Attribute for the object
   */
  public static ASTODAttribute createSimpleFloatAttribute(ASTModifier modifier, String name,
      float value) {
    // modifier and name
    ASTODAttributeBuilder attributeBuilder = init(modifier, name);
    
    // name
    attributeBuilder.setName(name);
    
    // primitive type
    attributeBuilder.setMCType(
        ODBasisMill.mCPrimitiveTypeBuilder().setPrimitive(ASTConstantsMCBasicTypes.FLOAT).build());
    
    // value
    String pre = StringUtils.substringBefore(Float.toString(value), ".");
    String post = StringUtils.substringAfter(Float.toString(value), ".");
    
    ASTLiteralExpression expression = ODBasisMill.literalExpressionBuilder()
        .setLiteral(ODBasisMill.basicFloatLiteralBuilder().setPre(pre).setPost(post).build())
        .build();
    attributeBuilder.setODValue(
        ODBasisMill.oDSimpleAttributeValueBuilder().setExpression(expression).build());
    
    return attributeBuilder.build();
  }
  
  /**
   * Builds s simple Attribute with a Double value.
   *
   * @param modifier Modifier of the attribute
   * @param name     Name of the attribute
   * @param value    Value of the attribute
   * @return Attribute for the object
   */
  public static ASTODAttribute createSimpleDoubleAttribute(ASTModifier modifier, String name,
      double value) {
    // modifier and name
    ASTODAttributeBuilder attributeBuilder = init(modifier, name);
    
    // name
    attributeBuilder.setName(name);
    
    // primitive type
    attributeBuilder.setMCType(
        ODBasisMill.mCPrimitiveTypeBuilder().setPrimitive(ASTConstantsMCBasicTypes.DOUBLE).build());
    
    // value
    String pre = StringUtils.substringBefore(Double.toString(value), ".");
    String post = StringUtils.substringAfter(Double.toString(value), ".");
    
    ASTLiteralExpression expression = ODBasisMill.literalExpressionBuilder()
        .setLiteral(ODBasisMill.basicDoubleLiteralBuilder().setPre(pre).setPost(post).build())
        .build();
    attributeBuilder.setODValue(
        ODBasisMill.oDSimpleAttributeValueBuilder().setExpression(expression).build());
    
    return attributeBuilder.build();
  }
  
  /**
   * Builds s simple Attribute with a Boolean value.
   *
   * @param modifier Modifier of the attribute
   * @param name     Name of the attribute
   * @param value    Value of the attribute
   * @return Attribute for the object
   */
  public static ASTODAttribute createSimpleBooleanAttribute(ASTModifier modifier, String name,
      boolean value) {
    // modifier and name
    ASTODAttributeBuilder attributeBuilder = init(modifier, name);
    
    // name
    attributeBuilder.setName(name);
    
    // primitive type
    attributeBuilder.setMCType(ODBasisMill.mCPrimitiveTypeBuilder()
        .setPrimitive(ASTConstantsMCBasicTypes.BOOLEAN)
        .build());
    
    // value
    int booleanValue = value ?
        ASTConstantsMCCommonLiterals.TRUE :
        ASTConstantsMCCommonLiterals.FALSE;
    ASTLiteralExpression expression = ODBasisMill.literalExpressionBuilder()
        .setLiteral(ODBasisMill.booleanLiteralBuilder().setSource(booleanValue).build())
        .build();
    attributeBuilder.setODValue(
        ODBasisMill.oDSimpleAttributeValueBuilder().setExpression(expression).build());
    
    return attributeBuilder.build();
  }
  
  /**
   * Builds s simple Attribute with a String value.
   *
   * @param modifier Modifier of the attribute
   * @param name     Name of the attribute
   * @param value    Value of the attribute
   * @return Attribute for the object
   */
  public static ASTODAttribute createSimpleStringAttribute(ASTModifier modifier, String name,
      String value) {
    // modifier and name
    ASTODAttributeBuilder attributeBuilder = init(modifier, name);
    
    // name
    attributeBuilder.setName(name);
    
    // String type
    attributeBuilder.setMCType(ODBasisMill.mCQualifiedTypeBuilder()
        .setMCQualifiedName(ODBasisMill.mCQualifiedNameBuilder()
            .setPartsList(Collections.singletonList("String"))
            .build())
        .build());
    
    // value
    ASTLiteralExpression expression = ODBasisMill.literalExpressionBuilder()
        .setLiteral(ODBasisMill.stringLiteralBuilder().setSource(value).build())
        .build();
    attributeBuilder.setODValue(
        ODBasisMill.oDSimpleAttributeValueBuilder().setExpression(expression).build());
    
    return attributeBuilder.build();
  }
  
  private static ASTODAttributeBuilder init(ASTModifier modifier, String name) {
    return ODBasisMill.oDAttributeBuilder().setModifier(modifier).setName(name).setComplete("=");
  }
  
}
