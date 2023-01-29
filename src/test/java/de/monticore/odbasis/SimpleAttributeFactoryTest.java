/*
 * (c) https://github.com/MontiCore/monticore
 */

package de.monticore.odbasis;

import de.monticore.odbasis._ast.ASTODAttribute;
import de.monticore.odbasis.utils.SimpleAttributeFactory;
import de.se_rwth.commons.logging.Log;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.regex.Pattern;

public class SimpleAttributeFactoryTest {

  @Before
  public void init() {
    Log.init();
    ODBasisMill.reset();
    ODBasisMill.init();
  }

  @Test
  public void testCreateInteger() {
    ASTODAttribute integerAttribute = SimpleAttributeFactory.createSimpleIntegerAttribute(
        ODBasisMill.modifierBuilder().PRIVATE().build(), "test", -10);
    String result = ODBasisMill.prettyPrint(integerAttribute, false);
    // replace first '-' due to generated prettyprinter
    // remove once it is possible to select between print and alt print for constants
    Assert.assertEquals("private int test=-10;", result.replaceFirst(Pattern.quote("-"), " "));

    integerAttribute = SimpleAttributeFactory.createSimpleIntegerAttribute(
        ODBasisMill.modifierBuilder().PRIVATE().build(), "test", 10);
    // replace first '-' due to generated prettyprinter
    // remove once it is possible to select between print and alt print for constants
    result = ODBasisMill.prettyPrint(integerAttribute, false);
    Assert.assertEquals("private int test=10;", result.replaceFirst(Pattern.quote("-"), " "));
  }

  @Test
  public void testCreateLong() {
    ASTODAttribute longAttribute = SimpleAttributeFactory.createSimpleLongAttribute(
        ODBasisMill.modifierBuilder().PROTECTED().build(), "test", -1l);

    String result = ODBasisMill.prettyPrint(longAttribute, false);
    // replace '#' due to generated prettyprinter
    // remove once it is possible to select between print and alt print for constants
    Assert.assertEquals("protected long test=-1l;", result.replaceFirst(Pattern.quote("#"), " "));

    longAttribute = SimpleAttributeFactory.createSimpleLongAttribute(
        ODBasisMill.modifierBuilder().PROTECTED().build(), "test", 1l);
    result = ODBasisMill.prettyPrint(longAttribute, false);
    // replace '#' due to generated prettyprinter
    // remove once it is possible to select between print and alt print for constants
    Assert.assertEquals("protected long test=1l;", result.replaceFirst(Pattern.quote("#"), " "));
  }

  @Test
  public void testCreateFloat() {
    ASTODAttribute floatAttribute = SimpleAttributeFactory.createSimpleFloatAttribute(
        ODBasisMill.modifierBuilder().PROTECTED().build(), "test", -1.0f);

    String result = ODBasisMill.prettyPrint(floatAttribute, false);
    // replace '#' due to generated prettyprinter
    // remove once it is possible to select between print and alt print for constants
    Assert.assertEquals("protected float test=-1.0f;",
        result.replaceFirst(Pattern.quote("#"), " "));

    floatAttribute = SimpleAttributeFactory.createSimpleFloatAttribute(
        ODBasisMill.modifierBuilder().PROTECTED().build(), "test", 1.0f);
    result = ODBasisMill.prettyPrint(floatAttribute, false);
    // replace '#' due to generated prettyprinter
    // remove once it is possible to select between print and alt print for constants
    Assert.assertEquals("protected float test=1.0f;", result.replaceFirst(Pattern.quote("#"), " "));
  }

  @Test
  public void testCreateDouble() {
    ASTODAttribute doubleAttribute = SimpleAttributeFactory.createSimpleDoubleAttribute(
        ODBasisMill.modifierBuilder().PROTECTED().build(), "test", -1.0);

    String result = ODBasisMill.prettyPrint(doubleAttribute, false);
    // replace '#' due to generated prettyprinter
    // remove once it is possible to select between print and alt print for constants
    Assert.assertEquals("protected double test=-1.0;",
        result.replaceFirst(Pattern.quote("#"), " "));

    doubleAttribute = SimpleAttributeFactory.createSimpleDoubleAttribute(
        ODBasisMill.modifierBuilder().PROTECTED().build(), "test", 1.0);
    result = ODBasisMill.prettyPrint(doubleAttribute, false);
    // replace '#' due to generated prettyprinter
    // remove once it is possible to select between print and alt print for constants
    Assert.assertEquals("protected double test=1.0;", result.replaceFirst(Pattern.quote("#"), " "));
  }

  @Test
  public void testCreateBoolean() {
    ASTODAttribute booleanAttribute = SimpleAttributeFactory.createSimpleBooleanAttribute(
        ODBasisMill.modifierBuilder().PROTECTED().build(), "test", true);

    String result = ODBasisMill.prettyPrint(booleanAttribute, false);
    // replace '#' due to generated prettyprinter
    // remove once it is possible to select between print and alt print for constants
    Assert.assertEquals("protected boolean test=true;",
        result.replaceFirst(Pattern.quote("#"), " "));

    booleanAttribute = SimpleAttributeFactory.createSimpleBooleanAttribute(
        ODBasisMill.modifierBuilder().PROTECTED().build(), "test", false);
    result = ODBasisMill.prettyPrint(booleanAttribute, false);
    // replace '#' due to generated prettyprinter
    // remove once it is possible to select between print and alt print for constants
    Assert.assertEquals("protected boolean test=false;",
        result.replaceFirst(Pattern.quote("#"), " "));
  }

  @Test
  public void testCreateString() {
    ASTODAttribute stringAttribute = SimpleAttributeFactory.createSimpleStringAttribute(
        ODBasisMill.modifierBuilder().PROTECTED().build(), "test", "test");

    String result = ODBasisMill.prettyPrint(stringAttribute, false);
    // replace '#' due to generated prettyprinter
    // remove once it is possible to select between print and alt print for constants
    Assert.assertEquals("protected String test=\"test\";",
        result.replaceFirst(Pattern.quote("#"), " "));
  }

}
