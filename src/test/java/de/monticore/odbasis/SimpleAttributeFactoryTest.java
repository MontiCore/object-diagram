/*
 * (c) https://github.com/MontiCore/monticore
 */

package de.monticore.odbasis;

import de.monticore.odbasis._ast.ASTODAttribute;
import de.monticore.odbasis.utils.SimpleAttributeFactory;
import de.se_rwth.commons.logging.Log;
import de.se_rwth.commons.logging.LogStub;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.regex.Pattern;

public class SimpleAttributeFactoryTest {
  
  @Before
  public void setup() {
    LogStub.init();
    Log.enableFailQuick(false);
    
    ODBasisMill.reset();
    ODBasisMill.init();
  }
  
  @Test
  public void testCreateInteger() {
    ASTODAttribute integerAttribute = SimpleAttributeFactory.createSimpleIntegerAttribute(
        ODBasisMill.modifierBuilder().PROTECTED().build(), "test", -10);
    String result = ODBasisMill.prettyPrint(integerAttribute, false);
    // replace '#' due to generated prettyprinter
    // remove once it is possible to select between print and alt print for constants
    // remove ending newline "\n" generated by the prettyprinter
    Assert.assertEquals("protected int test=-10;", result.replaceFirst(Pattern.quote("#"), " ").replaceAll("\\n$", ""));
    
    integerAttribute = SimpleAttributeFactory.createSimpleIntegerAttribute(
        ODBasisMill.modifierBuilder().PROTECTED().build(), "test", 10);
    // replace '#' due to generated prettyprinter
    // remove once it is possible to select between print and alt print for constants
    // remove ending newline "\n" generated by the prettyprinter
    result = ODBasisMill.prettyPrint(integerAttribute, false);
    Assert.assertEquals("protected int test=10;", result.replaceFirst(Pattern.quote("#"), " ").replaceAll("\\n$", ""));
  }
  
  @Test
  public void testCreateLong() {
    ASTODAttribute longAttribute = SimpleAttributeFactory.createSimpleLongAttribute(
        ODBasisMill.modifierBuilder().PROTECTED().build(), "test", -1l);
    
    String result = ODBasisMill.prettyPrint(longAttribute, false);
    // replace '#' due to generated prettyprinter
    // remove once it is possible to select between print and alt print for constants
    // remove ending newline "\n" generated by the prettyprinter
    Assert.assertEquals("protected long test=-1l;", result.replaceFirst(Pattern.quote("#"), " ").replaceAll("\\n$", ""));
    
    longAttribute = SimpleAttributeFactory.createSimpleLongAttribute(
        ODBasisMill.modifierBuilder().PROTECTED().build(), "test", 1l);
    result = ODBasisMill.prettyPrint(longAttribute, false);
    // replace '#' due to generated prettyprinter
    // remove once it is possible to select between print and alt print for constants
    // remove ending newline "\n" generated by the prettyprinter
    Assert.assertEquals("protected long test=1l;", result.replaceFirst(Pattern.quote("#"), " ").replaceAll("\\n$", ""));
  }
  
  @Test
  public void testCreateFloat() {
    ASTODAttribute floatAttribute = SimpleAttributeFactory.createSimpleFloatAttribute(
        ODBasisMill.modifierBuilder().PROTECTED().build(), "test", -1.0f);
    
    String result = ODBasisMill.prettyPrint(floatAttribute, false);
    // replace '#' due to generated prettyprinter
    // remove once it is possible to select between print and alt print for constants
    // remove ending newline "\n" generated by the prettyprinter
    Assert.assertEquals("protected float test=-1.0f;",
        result.replaceFirst(Pattern.quote("#"), " ").replaceAll("\\n$", ""));
    
    floatAttribute = SimpleAttributeFactory.createSimpleFloatAttribute(
        ODBasisMill.modifierBuilder().PROTECTED().build(), "test", 1.0f);
    result = ODBasisMill.prettyPrint(floatAttribute, false);
    // replace '#' due to generated prettyprinter
    // remove once it is possible to select between print and alt print for constants
    // remove ending newline "\n" generated by the prettyprinter
    Assert.assertEquals("protected float test=1.0f;", result.replaceFirst(Pattern.quote("#"), " ").replaceAll("\\n$", ""));
  }
  
  @Test
  public void testCreateDouble() {
    ASTODAttribute doubleAttribute = SimpleAttributeFactory.createSimpleDoubleAttribute(
        ODBasisMill.modifierBuilder().PROTECTED().build(), "test", -1.0);
    
    String result = ODBasisMill.prettyPrint(doubleAttribute, false);
    // replace '#' due to generated prettyprinter
    // remove once it is possible to select between print and alt print for constants
    // remove ending newline "\n" generated by the prettyprinter
    Assert.assertEquals("protected double test=-1.0;",
        result.replaceFirst(Pattern.quote("#"), " ").replaceAll("\\n$", ""));
    
    doubleAttribute = SimpleAttributeFactory.createSimpleDoubleAttribute(
        ODBasisMill.modifierBuilder().PROTECTED().build(), "test", 1.0);
    result = ODBasisMill.prettyPrint(doubleAttribute, false);
    // replace '#' due to generated prettyprinter
    // remove once it is possible to select between print and alt print for constants
    // remove ending newline "\n" generated by the prettyprinter
    Assert.assertEquals("protected double test=1.0;", result.replaceFirst(Pattern.quote("#"), " ").replaceAll("\\n$", ""));
  }
  
  @Test
  public void testCreateBoolean() {
    ASTODAttribute booleanAttribute = SimpleAttributeFactory.createSimpleBooleanAttribute(
        ODBasisMill.modifierBuilder().PROTECTED().build(), "test", true);
    
    String result = ODBasisMill.prettyPrint(booleanAttribute, false);
    // replace '#' due to generated prettyprinter
    // remove once it is possible to select between print and alt print for constants
    // remove ending newline "\n" generated by the prettyprinter
    Assert.assertEquals("protected boolean test=true;",
        result.replaceFirst(Pattern.quote("#"), " ").replaceAll("\\n$", ""));
    
    booleanAttribute = SimpleAttributeFactory.createSimpleBooleanAttribute(
        ODBasisMill.modifierBuilder().PROTECTED().build(), "test", false);
    result = ODBasisMill.prettyPrint(booleanAttribute, false);
    // replace '#' due to generated prettyprinter
    // remove once it is possible to select between print and alt print for constants
    // remove ending newline "\n" generated by the prettyprinter
    Assert.assertEquals("protected boolean test=false;",
        result.replaceFirst(Pattern.quote("#"), " ").replaceAll("\\n$", ""));
  }
  
  @Test
  public void testCreateString() {
    ASTODAttribute stringAttribute = SimpleAttributeFactory.createSimpleStringAttribute(
        ODBasisMill.modifierBuilder().PROTECTED().build(), "test", "test");
    
    String result = ODBasisMill.prettyPrint(stringAttribute, false);
    // replace '#' due to generated prettyprinter
    // remove once it is possible to select between print and alt print for constants
    // remove ending newline "\n" generated by the prettyprinter
    Assert.assertEquals("protected String test=\"test\";",
        result.replaceFirst(Pattern.quote("#"), " ").replaceAll("\\n$", ""));
  }
  
}
