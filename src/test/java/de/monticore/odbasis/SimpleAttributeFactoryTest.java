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
    String result = ODBasisMill.prettyPrint(integerAttribute, false).trim();
    Assert.assertEquals("protected int test=-10;", result);

    integerAttribute = SimpleAttributeFactory.createSimpleIntegerAttribute(
        ODBasisMill.modifierBuilder().PROTECTED().build(), "test", 10);
    result = ODBasisMill.prettyPrint(integerAttribute, false).trim();
    Assert.assertEquals("protected int test=10;", result);
  }

  @Test
  public void testCreateLong() {
    ASTODAttribute longAttribute = SimpleAttributeFactory.createSimpleLongAttribute(
        ODBasisMill.modifierBuilder().PROTECTED().build(), "test", -1l);

    String result = ODBasisMill.prettyPrint(longAttribute, false).trim();
    Assert.assertEquals("protected long test=-1l;", result);

    longAttribute = SimpleAttributeFactory.createSimpleLongAttribute(
        ODBasisMill.modifierBuilder().PROTECTED().build(), "test", 1l);
    result = ODBasisMill.prettyPrint(longAttribute, false).trim();
    Assert.assertEquals("protected long test=1l;", result);
  }

  @Test
  public void testCreateFloat() {
    ASTODAttribute floatAttribute = SimpleAttributeFactory.createSimpleFloatAttribute(
        ODBasisMill.modifierBuilder().PROTECTED().build(), "test", -1.0f);

    String result = ODBasisMill.prettyPrint(floatAttribute, false).trim();
    Assert.assertEquals("protected float test=-1.0f;", result);

    floatAttribute = SimpleAttributeFactory.createSimpleFloatAttribute(
        ODBasisMill.modifierBuilder().PROTECTED().build(), "test", 1.0f);
    result = ODBasisMill.prettyPrint(floatAttribute, false).trim();
    Assert.assertEquals("protected float test=1.0f;", result);
  }

  @Test
  public void testCreateDouble() {
    ASTODAttribute doubleAttribute = SimpleAttributeFactory.createSimpleDoubleAttribute(
        ODBasisMill.modifierBuilder().PROTECTED().build(), "test", -1.0);

    String result = ODBasisMill.prettyPrint(doubleAttribute, false).trim();
    Assert.assertEquals("protected double test=-1.0;", result);

    doubleAttribute = SimpleAttributeFactory.createSimpleDoubleAttribute(
        ODBasisMill.modifierBuilder().PROTECTED().build(), "test", 1.0);
    result = ODBasisMill.prettyPrint(doubleAttribute, false).trim();
    Assert.assertEquals("protected double test=1.0;", result);
  }

  @Test
  public void testCreateBoolean() {
    ASTODAttribute booleanAttribute = SimpleAttributeFactory.createSimpleBooleanAttribute(
        ODBasisMill.modifierBuilder().PROTECTED().build(), "test", true);

    String result = ODBasisMill.prettyPrint(booleanAttribute, false).trim();
    Assert.assertEquals("protected boolean test=true;", result);

    booleanAttribute = SimpleAttributeFactory.createSimpleBooleanAttribute(
        ODBasisMill.modifierBuilder().PROTECTED().build(), "test", false);
    result = ODBasisMill.prettyPrint(booleanAttribute, false).trim();
    Assert.assertEquals("protected boolean test=false;", result);
  }

  @Test
  public void testCreateString() {
    ASTODAttribute stringAttribute = SimpleAttributeFactory.createSimpleStringAttribute(
        ODBasisMill.modifierBuilder().PROTECTED().build(), "test", "test");

    String result = ODBasisMill.prettyPrint(stringAttribute, false).trim();
    Assert.assertEquals("protected String test=\"test\";", result);
  }

}
