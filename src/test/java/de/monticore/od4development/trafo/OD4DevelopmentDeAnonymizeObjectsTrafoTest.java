/*
 *  (c) https://github.com/MontiCore/monticore
 */

package de.monticore.od4development.trafo;

import de.monticore.ODTestBasis;
import de.monticore.io.paths.MCPath;
import de.monticore.od4development.OD4DevelopmentTestUtil;
import de.monticore.od4development.OD4DevelopmentMill;
import de.monticore.od4development._visitor.OD4DevelopmentTraverser;
import de.monticore.odattribute._ast.ASTODList;
import de.monticore.odattribute._ast.ASTODMap;
import de.monticore.odbasis._ast.*;
import de.monticore.odbasis.utils.ODBasisObjectCollector;
import de.monticore.runtime.junit.TestWithMCLanguage;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link OD4DevelopmentDeAnonymizeObjectsTrafo}. Anonymous objects are named when the
 * trafo leaves their containing node, so nested objects are numbered first.
 */
@TestWithMCLanguage(OD4DevelopmentMill.class)
public class OD4DevelopmentDeAnonymizeObjectsTrafoTest extends ODTestBasis {

  private final Path TRAFO_EXAMPLES = PATH.resolve("trafos");

  @Test
  void testDeAnonymizationOuterObject() {
    ASTObjectDiagram diagram = loadAndTransform("AnonymousObjects.od", artifact -> {
      assertAndGetAsODAnonymousObject(artifact.getObjectDiagram().getODElement(0));
    });

    assertEquals("__objecttype1_anonymous_1",
        assertAndGetAsODNamedObject(diagram.getODElement(0)).getName());
    ASTODNamedObject bar = assertAndGetAsODNamedObject(diagram.getODElement(1));
    assertEquals("bar", bar.getName());
    assertEquals("blaa", assertAndGetAsODNamedObject(bar.getODAttribute(1).getODValue()).getName());
  }

  @Test
  void testDeAnonymizationInnerObject() {
    ASTObjectDiagram diagram = loadAndTransform("AnonymousObjects2.od", artifact -> {
      ASTODNamedObject bar = assertAndGetAsODNamedObject(artifact.getObjectDiagram().getODElement(1));
      assertAndGetAsODAnonymousObject(bar.getODAttribute(1).getODValue());
    });

    assertEquals("foo", assertAndGetAsODNamedObject(diagram.getODElement(0)).getName());
    ASTODNamedObject bar = assertAndGetAsODNamedObject(diagram.getODElement(1));
    assertEquals("bar", bar.getName());
    assertEquals("__objecttype2_anonymous_1",
        assertAndGetAsODNamedObject(bar.getODAttribute(1).getODValue()).getName());
  }

  @Test
  void testDeAnonymizationInnerList() {
    ASTObjectDiagram diagram = loadAndTransform("AnonymousObjects3.od", artifact -> {
      ASTODNamedObject foo = assertAndGetAsODNamedObject(artifact.getObjectDiagram().getODElement(0));
      assertAndGetAsODAnonymousObject(assertAndGetAsODList(foo.getODAttribute(0).getODValue()).getODValue(1));
    });

    ASTODNamedObject foo = assertAndGetAsODNamedObject(diagram.getODElement(0));
    assertEquals("foo", foo.getName());
    assertEquals("bar", assertAndGetAsODNamedObject(diagram.getODElement(1)).getName());
    ASTODList fooList = assertAndGetAsODList(foo.getODAttribute(0).getODValue());
    assertEquals("foo1", assertAndGetAsODNamedObject(fooList.getODValue(0)).getName());
    assertEquals("__objecttype_anonymous_1",
        assertAndGetAsODNamedObject(fooList.getODValue(1)).getName());
  }

  @Test
  void testDeAnonymizationInnerMap() {
    ASTObjectDiagram diagram = loadAndTransform("AnonymousObjects4.od", artifact -> {
      ASTODNamedObject foo = assertAndGetAsODNamedObject(artifact.getObjectDiagram().getODElement(0));
      assertAndGetAsODAnonymousObject(
          assertAndGetAsODMap(foo.getODAttribute(0).getODValue()).getODMapElement(1).getVal());
    });

    ASTODNamedObject foo = assertAndGetAsODNamedObject(diagram.getODElement(0));
    assertEquals("foo", foo.getName());
    assertEquals("bar", assertAndGetAsODNamedObject(diagram.getODElement(1)).getName());
    ASTODMap fooMap = assertAndGetAsODMap(foo.getODAttribute(0).getODValue());
    assertEquals("foo1", assertAndGetAsODNamedObject(fooMap.getODMapElement(0).getVal()).getName());
    assertEquals("__objecttype_anonymous_1",
        assertAndGetAsODNamedObject(fooMap.getODMapElement(1).getVal()).getName());
  }

  @Test
  void testDeAnonymizationNested() {
    ASTObjectDiagram diagram = loadAndTransform("AnonymousObjects5.od", artifact -> {});

    ASTODNamedObject foo = assertAndGetAsODNamedObject(diagram.getODElement(0));
    assertEquals("foo", foo.getName());
    assertEquals("bar", assertAndGetAsODNamedObject(diagram.getODElement(1)).getName());
    ASTODMap fooMap = assertAndGetAsODMap(foo.getODAttribute(0).getODValue());
    ASTODNamedObject foo1 = assertAndGetAsODNamedObject(fooMap.getODMapElement(0).getVal());
    assertEquals("foo1", foo1.getName());
    ASTODList foo1List = assertAndGetAsODList(foo1.getODAttribute(0).getODValue());
    assertEquals("innerA", assertAndGetAsODNamedObject(foo1List.getODValue(0)).getName());
    ASTODNamedObject innerC = assertAndGetAsODNamedObject(foo1List.getODValue(2));
    assertEquals("innerC", innerC.getName());
    assertEquals("innerE",
        assertAndGetAsODNamedObject(innerC.getODAttribute(1).getODValue()).getName());

    // innermost first: attribute of innerC, then the list element, then the map value
    assertEquals("__objecttype_anonymous_1",
        assertAndGetAsODNamedObject(innerC.getODAttribute(0).getODValue()).getName());
    assertEquals("__objecttype_anonymous_2",
        assertAndGetAsODNamedObject(foo1List.getODValue(1)).getName());
    assertEquals("__objecttype_anonymous_3",
        assertAndGetAsODNamedObject(fooMap.getODMapElement(1).getVal()).getName());
  }

  @Test
  void testDeAnonymizationGeneratesUniqueNamesPerType() throws IOException {
    ASTODArtifact artifact = OD4DevelopmentMill.parser().parse_StringODArtifact(
        "objectdiagram Unique { :A {}; :A {}; :B { a = :A {}; }; }").orElseThrow();
    ASTObjectDiagram diagram = artifact.getObjectDiagram();

    new OD4DevelopmentDeAnonymizeObjectsTrafo().transform(artifact);

    ASTODNamedObject a1 = assertAndGetAsODNamedObject(diagram.getODElement(0));
    ASTODNamedObject a2 = assertAndGetAsODNamedObject(diagram.getODElement(1));
    ASTODNamedObject b = assertAndGetAsODNamedObject(diagram.getODElement(2));
    ASTODNamedObject nestedA = assertAndGetAsODNamedObject(b.getODAttribute(0).getODValue());

    // the nested object is renamed first, as attributes are visited before the diagram ends
    assertEquals("__a_anonymous_1", nestedA.getName());
    assertEquals("__a_anonymous_2", a1.getName());
    assertEquals("__a_anonymous_3", a2.getName());
    assertEquals("__b_anonymous_1", b.getName());
  }

  /**
   * The named copy keeps the type, the modifier, the attributes, and the comments of the
   * anonymous object.
   */
  @Test
  void testDeAnonymizationKeepsObjectContent() throws IOException {
    ASTODArtifact artifact = OD4DevelopmentMill.parser().parse_StringODArtifact(
        "objectdiagram Copy { a:A { b = /* pre */ public :B { x = 1; y = \"z\"; }; }; }")
        .orElseThrow();
    ASTODNamedObject a = assertAndGetAsODNamedObject(artifact.getObjectDiagram().getODElement(0));
    ASTODAnonymousObject original = assertAndGetAsODAnonymousObject(a.getODAttribute(0).getODValue());
    List<ASTODAttribute> originalAttributes = List.copyOf(original.getODAttributeList());
    assertEquals(1, original.get_PreCommentList().size(), "comment not attached to the object");

    new OD4DevelopmentDeAnonymizeObjectsTrafo().transform(artifact);

    ASTODNamedObject copy = assertAndGetAsODNamedObject(a.getODAttribute(0).getODValue());
    assertEquals("__b_anonymous_1", copy.getName());
    assertEquals("B", copy.getMCObjectType().printType());
    assertTrue(copy.getModifier().isPublic());
    assertEquals(originalAttributes, copy.getODAttributeList());
    assertEquals(original.get_SourcePositionStart(), copy.get_SourcePositionStart());
    assertEquals(original.get_PreCommentList(), copy.get_PreCommentList());
  }

  /**
   * Loads the model, runs the given assertions on the model before the trafo, applies the trafo,
   * and checks that no anonymous object is left.
   */
  protected ASTObjectDiagram loadAndTransform(String model,
      Consumer<ASTODArtifact> assertionsBeforeTrafo) {
    ASTODArtifact artifact =
        OD4DevelopmentTestUtil.loadModel(TRAFO_EXAMPLES.resolve(model).toString(), new MCPath(PATH));
    assertionsBeforeTrafo.accept(artifact);

    new OD4DevelopmentDeAnonymizeObjectsTrafo().transform(artifact);

    ODBasisObjectCollector collector = new ODBasisObjectCollector();
    OD4DevelopmentTraverser traverser = OD4DevelopmentMill.traverser();
    traverser.add4ODBasis(collector);
    artifact.accept(traverser);
    assertEquals(List.of(), collector.getAnonymousObjects(), "anonymous objects left");

    return artifact.getObjectDiagram();
  }

  protected ASTODList assertAndGetAsODList(ASTODValue value) {
    return assertInstanceOf(ASTODList.class, value);
  }

  protected ASTODMap assertAndGetAsODMap(ASTODValue value) {
    return assertInstanceOf(ASTODMap.class, value);
  }

  protected ASTODNamedObject assertAndGetAsODNamedObject(ASTODElement element) {
    return assertInstanceOf(ASTODNamedObject.class, element);
  }

  protected ASTODAnonymousObject assertAndGetAsODAnonymousObject(ASTODElement element) {
    return assertInstanceOf(ASTODAnonymousObject.class, element);
  }

  protected ASTODNamedObject assertAndGetAsODNamedObject(ASTODValue value) {
    return assertInstanceOf(ASTODNamedObject.class, value);
  }

  protected ASTODAnonymousObject assertAndGetAsODAnonymousObject(ASTODValue value) {
    return assertInstanceOf(ASTODAnonymousObject.class, value);
  }
}
