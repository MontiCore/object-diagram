/*
 *  (c) https://github.com/MontiCore/monticore
 */

package de.monticore.od4development.trafo;

import de.monticore.ODTestBasis;
import de.monticore.expressions.expressionsbasis._ast.ASTLiteralExpression;
import de.monticore.io.paths.MCPath;
import de.monticore.od4development.OD4DevelopmentTestUtil;
import de.monticore.od4development.OD4DevelopmentMill;
import de.monticore.odattribute._ast.ASTODList;
import de.monticore.odattribute._ast.ASTODMap;
import de.monticore.odbasis._ast.*;
import de.monticore.odlink._ast.ASTODLink;
import de.monticore.runtime.junit.TestWithMCLanguage;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@TestWithMCLanguage(OD4DevelopmentMill.class)
public class OD4DevelopmentAttributeValueCompositionTrafoTest extends ODTestBasis {
  
  private final Path TRAFO_EXAMPLES = PATH.resolve("trafos");
  
  @Test
  void testAttributeCompositionTrafo() {
    String testOD = TRAFO_EXAMPLES.resolve("AttributeObjectComposition.od").toString();
    ASTODArtifact artifact = OD4DevelopmentTestUtil.loadModelAndST(testOD, new MCPath(PATH));
    
    ASTObjectDiagram diagram = artifact.getObjectDiagram();
    
    // Assertions pre trafo
    assertEquals(2, diagram.getODElementList().size());
    ASTODNamedObject foo = assertAndGetAsODNamedObject(diagram.getODElement(0));
    assertEquals("foo", foo.getName());
    ASTODNamedObject bar = assertAndGetAsODNamedObject(diagram.getODElement(1));
    assertEquals("bar", bar.getName());
    assertEquals(3, bar.getODAttributeList().size());
    ASTODNamedObject barAttr = assertAndGetAsODNamedObject(bar.getODAttribute(1).getODValue());
    assertEquals("blaa", barAttr.getName());
    
    new OD4DevelopmentAttributeValueCompositionTrafo().transform(artifact);
    
    // Assertions post trafo
    assertEquals(5, diagram.getODElementList().size());
    ASTODNamedObject foo2 = assertAndGetAsODNamedObject(diagram.getODElement(0));
    assertEquals("foo", foo2.getName());
    ASTODNamedObject bar2 = assertAndGetAsODNamedObject(diagram.getODElement(1));
    assertEquals("bar", bar2.getName());
    assertEquals(1, bar2.getODAttributeList().size());
    ASTODNamedObject blaa = assertAndGetAsODNamedObject(diagram.getODElement(2));
    assertEquals("blaa", blaa.getName());
    ASTODLink fooLink = assertAndGetAsODLink(diagram.getODElement(3));
    assertLinkConfig(fooLink, "bar", "foo", "foobar", false);
    ASTODLink blaaLink = assertAndGetAsODLink(diagram.getODElement(4));
    assertLinkCompositionConfig(blaaLink, "bar", "blaa", "foobar2");
  }
  
  @Test
  void testAttributeCompositionTrafoInList() {
    String testOD = TRAFO_EXAMPLES.resolve("AttributeObjectComposition2.od").toString();
    ASTODArtifact artifact = OD4DevelopmentTestUtil.loadModelAndST(testOD, new MCPath(PATH));
    
    ASTObjectDiagram diagram = artifact.getObjectDiagram();
    
    // Assertions pre trafo
    assertEquals(2, diagram.getODElementList().size());
    ASTODNamedObject foo = assertAndGetAsODNamedObject(diagram.getODElement(0));
    assertEquals("foo", foo.getName());
    ASTODNamedObject bar = assertAndGetAsODNamedObject(diagram.getODElement(1));
    assertEquals("bar", bar.getName());
    assertEquals(1, foo.getODAttributeList().size());
    ASTODList fooList = assertAndGetAsODList(foo.getODAttribute(0).getODValue());
    assertEquals(2, fooList.sizeODValues());
    ASTODNamedObject fooListVal1 = assertAndGetAsODNamedObject(fooList.getODValue(0));
    assertEquals("foo1", fooListVal1.getName());
    
    new OD4DevelopmentAttributeValueCompositionTrafo().transform(artifact);
    
    // Assertions post trafo
    assertEquals(4, diagram.getODElementList().size());
    ASTODNamedObject foo2 = assertAndGetAsODNamedObject(diagram.getODElement(0));
    assertEquals("foo", foo2.getName());
    ASTODNamedObject bar2 = assertAndGetAsODNamedObject(diagram.getODElement(1));
    assertEquals("bar", bar2.getName());
    assertEquals(1, foo2.getODAttributeList().size());
    ASTODList fooList2 = assertAndGetAsODList(foo2.getODAttribute(0).getODValue());
    assertEquals(1, fooList2.sizeODValues());
    ASTODNamedObject foo1 = assertAndGetAsODNamedObject(diagram.getODElement(2));
    assertEquals("foo1", foo1.getName());
    ASTODLink foo1Link = assertAndGetAsODLink(diagram.getODElement(3));
    assertLinkCompositionConfig(foo1Link, "foo", "foo1", "foobar");
  }
  
  @Test
  void testAttributeCompositionTrafoInMap() {
    String testOD = TRAFO_EXAMPLES.resolve("AttributeObjectComposition3.od").toString();
    ASTODArtifact artifact = OD4DevelopmentTestUtil.loadModelAndST(testOD, new MCPath(PATH));
    
    ASTObjectDiagram diagram = artifact.getObjectDiagram();
    
    // Assertions pre trafo
    assertEquals(2, diagram.getODElementList().size());
    ASTODNamedObject foo = assertAndGetAsODNamedObject(diagram.getODElement(0));
    assertEquals("foo", foo.getName());
    ASTODNamedObject bar = assertAndGetAsODNamedObject(diagram.getODElement(1));
    assertEquals("bar", bar.getName());
    assertEquals(1, foo.getODAttributeList().size());
    ASTODMap fooMap = assertAndGetAsODMap(foo.getODAttribute(0).getODValue());
    assertEquals(2, fooMap.sizeODMapElements());
    ASTODNamedObject fooMapVal1 = assertAndGetAsODNamedObject(fooMap.getODMapElement(0).getVal());
    assertEquals("foo1", fooMapVal1.getName());
    
    new OD4DevelopmentAttributeValueCompositionTrafo().transform(artifact);
    
    // Assertions post trafo
    assertEquals(4, diagram.getODElementList().size());
    ASTODNamedObject foo2 = assertAndGetAsODNamedObject(diagram.getODElement(0));
    assertEquals("foo", foo2.getName());
    ASTODNamedObject bar2 = assertAndGetAsODNamedObject(diagram.getODElement(1));
    assertEquals("bar", bar2.getName());
    assertEquals(1, foo2.getODAttributeList().size());
    ASTODMap fooMap2 = assertAndGetAsODMap(foo2.getODAttribute(0).getODValue());
    assertEquals(1, fooMap2.sizeODMapElements());
    ASTODNamedObject foo1 = assertAndGetAsODNamedObject(diagram.getODElement(2));
    assertEquals("foo1", foo1.getName());
    ASTODLink foo1Link = assertAndGetAsODLink(diagram.getODElement(3));
    assertLinkCompositionConfig(foo1Link, "foo", "foo1", "foobar");
    assertLinkQualifier(foo1Link, 1);
  }
  
  protected ASTODList assertAndGetAsODList(ASTODValue value) {
    assertInstanceOf(ASTODList.class, value);
    return OD4DevelopmentMill.typeDispatcher().asODAttributeASTODList(value);
  }
  
  protected ASTODMap assertAndGetAsODMap(ASTODValue value) {
    assertInstanceOf(ASTODMap.class, value);
    return OD4DevelopmentMill.typeDispatcher().asODAttributeASTODMap(value);
  }
  
  protected ASTODNamedObject assertAndGetAsODNamedObject(ASTODElement element) {
    assertInstanceOf(ASTODNamedObject.class, element);
    return OD4DevelopmentMill.typeDispatcher().asODBasisASTODNamedObject(element);
  }
  
  protected ASTODNamedObject assertAndGetAsODNamedObject(ASTODValue value) {
    assertInstanceOf(ASTODNamedObject.class, value);
    return OD4DevelopmentMill.typeDispatcher().asODBasisASTODNamedObject(value);
  }
  
  protected ASTODLink assertAndGetAsODLink(ASTODElement element) {
    assertInstanceOf(ASTODLink.class, element);
    return OD4DevelopmentMill.typeDispatcher().asODLinkASTODLink(element);
  }
  
  protected void assertLinkCompositionConfig(ASTODLink link, String source, String target,
      String role) {
    assertLinkConfig(link, source, target, role, true);
  }
  
  protected void assertLinkConfig(ASTODLink link, String source, String target, String role,
      boolean isComposition) {
    assertEquals(1, link.getLeftReferenceNames().size());
    assertEquals(source, link.getLeftReferenceNames().get(0));
    assertEquals(1, link.getRightReferenceNames().size());
    assertEquals(target, link.getRightReferenceNames().get(0));
    assertEquals(isComposition, link.isComposition());
    assertFalse(link.isLink());
    assertFalse(link.isAggregation());
    assertTrue(OD4DevelopmentMill.typeDispatcher().isODLinkASTODLeftToRightDir(link.getODLinkDirection()));
    assertEquals(role, link.getODLinkRightSide().getRole());
  }
  
  protected void assertLinkQualifier(ASTODLink link, int qualifier) {
    ASTODValue qualifierValue = link.getODLinkLeftSide().getODLinkQualifier().getODValue();
    if (OD4DevelopmentMill.typeDispatcher().isODBasisASTODSimpleAttributeValue(qualifierValue)) {
      ASTODSimpleAttributeValue value =
          OD4DevelopmentMill.typeDispatcher().asODBasisASTODSimpleAttributeValue(qualifierValue);
      ASTLiteralExpression expectedExpression = OD4DevelopmentMill.literalExpressionBuilder()
          .setLiteral(OD4DevelopmentMill.natLiteralBuilder().setDigits(String.valueOf(qualifier)).build())
          .build();
      assertTrue(value.getExpression().deepEquals(expectedExpression));
    }
    else if (OD4DevelopmentMill.typeDispatcher().isODBasisASTODName(qualifierValue)) {
      ASTODName name = OD4DevelopmentMill.typeDispatcher().asODBasisASTODName(qualifierValue);
      assertEquals(String.valueOf(qualifier), name.getName());
    }
  }
}
