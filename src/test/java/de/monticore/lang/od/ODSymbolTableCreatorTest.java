/*
 * Copyright (c) 2015 RWTH Aachen. All rights reserved.
 *
 * http://www.se-rwth.de/
 */
package de.monticore.lang.od;

import de.monticore.io.paths.ModelPath;
import de.monticore.lang.od._ast.ASTODName;
import de.monticore.lang.od._ast.ASTODObject;
import de.monticore.lang.od._ast.ASTObjectDiagram;
import de.monticore.lang.od._ast.ODNodeFactory;
import de.monticore.lang.od._symboltable.ODLanguage;
import de.monticore.lang.od._symboltable.ODObjectSymbol;
import de.monticore.lang.od._symboltable.ODSymbolTableCreator;
import de.monticore.lang.od._symboltable.ObjectDiagramSymbol;
import de.monticore.symboltable.GlobalScope;
import de.monticore.symboltable.ResolvingConfiguration;
import de.monticore.symboltable.Scope;
import org.junit.BeforeClass;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.Collection;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

public class ODSymbolTableCreatorTest {

  private static final String OD_NAME = "AuctionParticipants";

  private static final String OBJECT_NAME = "kupfer912";

  private static ODLanguage odLanguage;

  private static ResolvingConfiguration resolverConfiguration;

  private static ModelPath modelPath;

  private Scope globalScope;

  @BeforeClass
  public static void setup() {
    odLanguage = new ODLanguage();

    resolverConfiguration = new ResolvingConfiguration();
    resolverConfiguration.addDefaultFilters(odLanguage.getResolvers());

    modelPath =
        new ModelPath(Paths.get("src/test/resources/symboltable"));
  }

  @Test
  public void testResolveODObjectFromFile() {
    final ObjectDiagramSymbol objectDiagramSymbol =
        createODDefinitionFromFile();

    Collection<ODObjectSymbol> odObjects = objectDiagramSymbol.getODObjects();

    assertTrue(!odObjects.isEmpty() && odObjects.size() == 6);
    for (ODObjectSymbol obj : odObjects) {
      assertTrue(obj.getAstNode().isPresent());
    }

    Optional<ODObjectSymbol> kupferObject = objectDiagramSymbol.getSpannedScope()
        .resolve("kupfer912", ODObjectSymbol.KIND);
    assertTrue(kupferObject.isPresent());

  }

  @Test
  public void testResolveODObjectFromAST() {
    final ObjectDiagramSymbol objectDiagramSymbol =
        createObjectDiagramFromAST();

    Collection<ODObjectSymbol> odObjects = objectDiagramSymbol
        .getODObjects();//.getODObject(OBJECT_NAME);

    assertTrue(!odObjects.isEmpty());
    for (ODObjectSymbol obj : odObjects) {
      assertTrue(obj.getAstNode().isPresent());
    }
  }

  private ObjectDiagramSymbol createODDefinitionFromFile() {
    globalScope = new GlobalScope(modelPath, odLanguage, resolverConfiguration);
    return globalScope.<ObjectDiagramSymbol>resolve(OD_NAME, ObjectDiagramSymbol.KIND)
        .orElse(null);
  }

  private ObjectDiagramSymbol createObjectDiagramFromAST() {
    ASTObjectDiagram objectDiagram = ODNodeFactory.createASTObjectDiagram();
    objectDiagram.setName(OD_NAME);
    ASTODObject odObject = ODNodeFactory.createASTODObject();
    ASTODName refName = ODNodeFactory.createASTODName();
    refName.setName(OBJECT_NAME);
    odObject.setODName(refName);
    objectDiagram.getODObjects().add(odObject);

    GlobalScope globalScope = new GlobalScope(new ModelPath(), odLanguage, resolverConfiguration);

    Optional<ODSymbolTableCreator> symbolTable = odLanguage.getSymbolTableCreator(
        resolverConfiguration, globalScope);
    if (symbolTable.isPresent()) {
      symbolTable.get().createFromAST(objectDiagram);
    }

    return globalScope.<ObjectDiagramSymbol>resolve(OD_NAME, ObjectDiagramSymbol.KIND)
        .orElse(null);
  }

}
