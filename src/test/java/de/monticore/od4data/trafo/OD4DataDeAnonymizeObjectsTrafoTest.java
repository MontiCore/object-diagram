/*
 *  (c) https://github.com/MontiCore/monticore
 */

package de.monticore.od4data.trafo;

import de.monticore.ODTestBasis;
import de.monticore.io.paths.MCPath;
import de.monticore.od4data.OD4DataMill;
import de.monticore.od4data.OD4DataTestUtil;
import de.monticore.odattribute._ast.ASTODList;
import de.monticore.odattribute._ast.ASTODMap;
import de.monticore.odbasis._ast.*;
import de.monticore.runtime.junit.TestWithMCLanguage;
import de.se_rwth.commons.logging.Log;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@TestWithMCLanguage(OD4DataMill.class)
public class OD4DataDeAnonymizeObjectsTrafoTest extends ODTestBasis {
  
  private final Path TRAFO_EXAMPLES = PATH.resolve("trafos");
  
  @Test
  void testDeAnonymizationOuterObject() {
    String testOD = TRAFO_EXAMPLES.resolve("AnonymousObjects.od").toString();
    ASTODArtifact artifact = OD4DataTestUtil.loadModel(testOD, new MCPath(PATH));
    
    ASTObjectDiagram diagram = artifact.getObjectDiagram();
    
    // Assertions pre trafo
    ASTODAnonymousObject foo = assertAndGetAsODAnonymousObject(diagram.getODElement(0));
    ASTODNamedObject bar = assertAndGetAsODNamedObject(diagram.getODElement(1));
    assertEquals("bar", bar.getName());
    ASTODNamedObject barAttr = assertAndGetAsODNamedObject(bar.getODAttribute(1).getODValue());
    
    new OD4DataDeAnonymizeObjectsTrafo().transform(artifact);
    
    // Assertions post trafo
    ASTODNamedObject foo2 = assertAndGetAsODNamedObject(diagram.getODElement(0));
    assertContains(foo2.getName(), "_anonymous_");
    ASTODNamedObject bar2 = assertAndGetAsODNamedObject(diagram.getODElement(1));
    assertEquals("bar", bar2.getName());
    ASTODNamedObject barAttr2 = assertAndGetAsODNamedObject(bar2.getODAttribute(1).getODValue());
    
    assertEquals(0, Log.getFindingsCount());
  }
  
  @Test
  void testDeAnonymizationInnerObject() {
    String testOD = TRAFO_EXAMPLES.resolve("AnonymousObjects2.od").toString();
    ASTODArtifact artifact = OD4DataTestUtil.loadModel(testOD, new MCPath(PATH));
    
    ASTObjectDiagram diagram = artifact.getObjectDiagram();
    
    // Assertions pre trafo
    ASTODNamedObject foo = assertAndGetAsODNamedObject(diagram.getODElement(0));
    assertEquals("foo", foo.getName());
    ASTODNamedObject bar = assertAndGetAsODNamedObject(diagram.getODElement(1));
    assertEquals("bar", bar.getName());
    ASTODAnonymousObject barAttr =
        assertAndGetAsODAnonymousObject(bar.getODAttribute(1).getODValue());
    
    new OD4DataDeAnonymizeObjectsTrafo().transform(artifact);
    
    // Assertions post trafo
    ASTODNamedObject foo2 = assertAndGetAsODNamedObject(diagram.getODElement(0));
    assertEquals("foo", foo2.getName());
    ASTODNamedObject bar2 = assertAndGetAsODNamedObject(diagram.getODElement(1));
    assertEquals("bar", bar2.getName());
    ASTODNamedObject barAttr2 = assertAndGetAsODNamedObject(bar2.getODAttribute(1).getODValue());
    assertContains(barAttr2.getName(), "_anonymous_");
    
    assertEquals(0, Log.getFindingsCount());
  }
  
  @Test
  void testDeAnonymizationInnerList() {
    String testOD = TRAFO_EXAMPLES.resolve("AnonymousObjects3.od").toString();
    ASTODArtifact artifact = OD4DataTestUtil.loadModel(testOD, new MCPath(PATH));
    
    ASTObjectDiagram diagram = artifact.getObjectDiagram();
    
    // Assertions pre trafo
    ASTODNamedObject foo = assertAndGetAsODNamedObject(diagram.getODElement(0));
    assertEquals("foo", foo.getName());
    ASTODNamedObject bar = assertAndGetAsODNamedObject(diagram.getODElement(1));
    assertEquals("bar", bar.getName());
    ASTODList fooList = assertAndGetAsODList(foo.getODAttribute(0).getODValue());
    ASTODNamedObject fooListVal1 = assertAndGetAsODNamedObject(fooList.getODValue(0));
    assertEquals("foo1", fooListVal1.getName());
    ASTODAnonymousObject fooListVal2 = assertAndGetAsODAnonymousObject(fooList.getODValue(1));
    
    new OD4DataDeAnonymizeObjectsTrafo().transform(artifact);
    
    // Assertions post trafo
    ASTODNamedObject foo2 = assertAndGetAsODNamedObject(diagram.getODElement(0));
    assertEquals("foo", foo2.getName());
    ASTODNamedObject bar2 = assertAndGetAsODNamedObject(diagram.getODElement(1));
    assertEquals("bar", bar2.getName());
    ASTODList fooList2 = assertAndGetAsODList(foo2.getODAttribute(0).getODValue());
    ASTODNamedObject fooList2Val1 = assertAndGetAsODNamedObject(fooList2.getODValue(0));
    assertEquals("foo1", fooList2Val1.getName());
    ASTODNamedObject fooList2Val2 = assertAndGetAsODNamedObject(fooList2.getODValue(1));
    assertContains(fooList2Val2.getName(), "_anonymous_");
    
    assertEquals(0, Log.getFindingsCount());
  }
  
  @Test
  void testDeAnonymizationInnerMap() {
    String testOD = TRAFO_EXAMPLES.resolve("AnonymousObjects4.od").toString();
    ASTODArtifact artifact = OD4DataTestUtil.loadModel(testOD, new MCPath(PATH));
    
    ASTObjectDiagram diagram = artifact.getObjectDiagram();
    
    // Assertions pre trafo
    ASTODNamedObject foo = assertAndGetAsODNamedObject(diagram.getODElement(0));
    assertEquals("foo", foo.getName());
    ASTODNamedObject bar = assertAndGetAsODNamedObject(diagram.getODElement(1));
    assertEquals("bar", bar.getName());
    ASTODMap fooMap = assertAndGetAsODMap(foo.getODAttribute(0).getODValue());
    ASTODNamedObject fooMapVal1 = assertAndGetAsODNamedObject(fooMap.getODMapElement(0).getVal());
    assertEquals("foo1", fooMapVal1.getName());
    ASTODAnonymousObject fooMapVal2 =
        assertAndGetAsODAnonymousObject(fooMap.getODMapElement(1).getVal());
    
    new OD4DataDeAnonymizeObjectsTrafo().transform(artifact);
    
    // Assertions post trafo
    ASTODNamedObject foo2 = assertAndGetAsODNamedObject(diagram.getODElement(0));
    assertEquals("foo", foo2.getName());
    ASTODNamedObject bar2 = assertAndGetAsODNamedObject(diagram.getODElement(1));
    assertEquals("bar", bar2.getName());
    ASTODMap fooMap2 = assertAndGetAsODMap(foo2.getODAttribute(0).getODValue());
    ASTODNamedObject fooMap2Val1 = assertAndGetAsODNamedObject(fooMap2.getODMapElement(0).getVal());
    assertEquals("foo1", fooMap2Val1.getName());
    ASTODNamedObject fooMap2Val2 = assertAndGetAsODNamedObject(fooMap2.getODMapElement(1).getVal());
    assertContains(fooMap2Val2.getName(), "_anonymous_");
    
    assertEquals(0, Log.getFindingsCount());
  }
  
  @Test
  void testDeAnonymizationNested() {
    String testOD = TRAFO_EXAMPLES.resolve("AnonymousObjects5.od").toString();
    ASTODArtifact artifact = OD4DataTestUtil.loadModel(testOD, new MCPath(PATH));
    
    ASTObjectDiagram diagram = artifact.getObjectDiagram();
    
    // Assertions pre trafo
    ASTODNamedObject foo = assertAndGetAsODNamedObject(diagram.getODElement(0));
    assertEquals("foo", foo.getName());
    ASTODNamedObject bar = assertAndGetAsODNamedObject(diagram.getODElement(1));
    assertEquals("bar", bar.getName());
    ASTODMap fooMap = assertAndGetAsODMap(foo.getODAttribute(0).getODValue());
    ASTODNamedObject fooMapVal1 = assertAndGetAsODNamedObject(fooMap.getODMapElement(0).getVal());
    assertEquals("foo1", fooMapVal1.getName());
    ASTODAnonymousObject fooMapVal2 =
        assertAndGetAsODAnonymousObject(fooMap.getODMapElement(1).getVal());
    ASTODList fooMapVal1List = assertAndGetAsODList(fooMapVal1.getODAttribute(0).getODValue());
    ASTODNamedObject fooMapVal1ListVal1 = assertAndGetAsODNamedObject(fooMapVal1List.getODValue(0));
    assertEquals("innerA", fooMapVal1ListVal1.getName());
    ASTODAnonymousObject fooMapVal1ListVal2 =
        assertAndGetAsODAnonymousObject(fooMapVal1List.getODValue(1));
    ASTODNamedObject fooMapVal1ListVal3 = assertAndGetAsODNamedObject(fooMapVal1List.getODValue(2));
    assertEquals("innerC", fooMapVal1ListVal3.getName());
    ASTODAnonymousObject fooMapVal1ListVal3FB1 =
        assertAndGetAsODAnonymousObject(fooMapVal1ListVal3.getODAttribute(0).getODValue());
    ASTODNamedObject fooMapVal1ListVal3FB2 =
        assertAndGetAsODNamedObject(fooMapVal1ListVal3.getODAttribute(1).getODValue());
    assertEquals("innerE", fooMapVal1ListVal3FB2.getName());
    
    new OD4DataDeAnonymizeObjectsTrafo().transform(artifact);
    
    ASTODNamedObject foo2 = assertAndGetAsODNamedObject(diagram.getODElement(0));
    assertEquals("foo", foo2.getName());
    ASTODNamedObject bar2 = assertAndGetAsODNamedObject(diagram.getODElement(1));
    assertEquals("bar", bar2.getName());
    ASTODMap fooMap2 = assertAndGetAsODMap(foo2.getODAttribute(0).getODValue());
    ASTODNamedObject fooMap2Val1 = assertAndGetAsODNamedObject(fooMap2.getODMapElement(0).getVal());
    assertEquals("foo1", fooMap2Val1.getName());
    ASTODNamedObject fooMap2Val2 = assertAndGetAsODNamedObject(fooMap2.getODMapElement(1).getVal());
    assertContains(fooMap2Val2.getName(), "_anonymous_");
    ASTODList fooMap2Val1List = assertAndGetAsODList(fooMap2Val1.getODAttribute(0).getODValue());
    ASTODNamedObject fooMap2Val1ListVal1 =
        assertAndGetAsODNamedObject(fooMap2Val1List.getODValue(0));
    assertEquals("innerA", fooMap2Val1ListVal1.getName());
    ASTODNamedObject fooMap2Val1ListVal2 =
        assertAndGetAsODNamedObject(fooMap2Val1List.getODValue(1));
    assertContains(fooMap2Val1ListVal2.getName(), "_anonymous_");
    ASTODNamedObject fooMap2Val1ListVal3 =
        assertAndGetAsODNamedObject(fooMap2Val1List.getODValue(2));
    assertEquals("innerC", fooMap2Val1ListVal3.getName());
    ASTODNamedObject fooMap2Val1ListVal3FB1 =
        assertAndGetAsODNamedObject(fooMap2Val1ListVal3.getODAttribute(0).getODValue());
    assertContains(fooMap2Val1ListVal3FB1.getName(), "_anonymous_");
    ASTODNamedObject fooMap2Val1ListVal3FB2 =
        assertAndGetAsODNamedObject(fooMap2Val1ListVal3.getODAttribute(1).getODValue());
    assertEquals("innerE", fooMap2Val1ListVal3FB2.getName());
  }
  
  protected ASTODList assertAndGetAsODList(ASTODValue value) {
    assertInstanceOf(ASTODList.class, value);
    return OD4DataMill.typeDispatcher().asODAttributeASTODList(value);
  }
  
  protected ASTODMap assertAndGetAsODMap(ASTODValue value) {
    assertInstanceOf(ASTODMap.class, value);
    return OD4DataMill.typeDispatcher().asODAttributeASTODMap(value);
  }
  
  protected ASTODNamedObject assertAndGetAsODNamedObject(ASTODElement element) {
    assertInstanceOf(ASTODNamedObject.class, element);
    return OD4DataMill.typeDispatcher().asODBasisASTODNamedObject(element);
  }
  
  protected ASTODAnonymousObject assertAndGetAsODAnonymousObject(ASTODElement element) {
    assertInstanceOf(ASTODAnonymousObject.class, element);
    return OD4DataMill.typeDispatcher().asODBasisASTODAnonymousObject(element);
  }
  
  protected ASTODNamedObject assertAndGetAsODNamedObject(ASTODValue value) {
    assertInstanceOf(ASTODNamedObject.class, value);
    return OD4DataMill.typeDispatcher().asODBasisASTODNamedObject(value);
  }
  
  protected ASTODAnonymousObject assertAndGetAsODAnonymousObject(ASTODValue value) {
    assertInstanceOf(ASTODAnonymousObject.class, value);
    return OD4DataMill.typeDispatcher().asODBasisASTODAnonymousObject(value);
  }
}
