/*
 * Copyright (c) 2017, MontiCore. All rights reserved.
 *
 * http://www.se-rwth.de/
 */
package de.monticore.lang.od;

import de.monticore.io.paths.ModelPath;
import de.monticore.lang.od._ast.*;
import de.monticore.lang.od._symboltable.ODLanguage;
import de.monticore.lang.od._symboltable.ODObjectSymbol;
import de.monticore.lang.od._symboltable.ODSymbolTableCreator;
import de.monticore.lang.od._symboltable.ObjectDiagramSymbol;
import de.monticore.symboltable.GlobalScope;
import de.monticore.symboltable.ResolvingConfiguration;
import de.monticore.symboltable.Scope;
import de.se_rwth.commons.logging.Slf4jLog;

import org.junit.BeforeClass;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.Collection;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

public class ODSymbolTableCreatorTest {

  private static ODLanguage odLanguage;

  private static ResolvingConfiguration resolverConfiguration;

  private static ModelPath modelPath;

  private Scope globalScope;

  @BeforeClass
  public static void setup() {
    Slf4jLog.init();
    Slf4jLog.enableFailQuick(false);
    
    odLanguage = new ODLanguage();

    resolverConfiguration = new ResolvingConfiguration();
    resolverConfiguration.addDefaultFilters(odLanguage.getResolvingFilters());

    modelPath =
        new ModelPath(Paths.get("src/test/resources/symboltable"));
  }

  @Test
  public void testResolveODObjectFromFile() {
    final ObjectDiagramSymbol objectDiagramSymbol =
        createODDefinitionFromFile("AuctionParticipants");

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
        createObjectDiagramFromAST("AuctionParticipants", "kupfer912");

    Collection<ODObjectSymbol> odObjects = objectDiagramSymbol
        .getODObjects();

    assertTrue(!odObjects.isEmpty());
    for (ODObjectSymbol obj : odObjects) {
      assertTrue(obj.getAstNode().isPresent());
    }
  }

  @Test
  public void testAvoidanceOfUnnamedObjects() {
    final ObjectDiagramSymbol objectDiagramSymbol =
        createODDefinitionFromFile("STInnerLinkVariants");

    Collection<ODObjectSymbol> odObjects = objectDiagramSymbol.getODObjects();

    assertTrue(!odObjects.isEmpty() && odObjects.size() == 5);
    for (ODObjectSymbol obj : odObjects) {
      assertTrue(obj.getAstNode().isPresent());
    }

    Optional<ODObjectSymbol> fooBarObject = objectDiagramSymbol.getSpannedScope()
        .resolve("@fooBar2", ODObjectSymbol.KIND);
    assertTrue(fooBarObject.isPresent());
  }

  private ObjectDiagramSymbol createODDefinitionFromFile(String odName) {

    globalScope = new GlobalScope(modelPath, odLanguage, resolverConfiguration);

    return globalScope.<ObjectDiagramSymbol>resolve(odName, ObjectDiagramSymbol.KIND)
        .orElse(null);
  }

  private ObjectDiagramSymbol createObjectDiagramFromAST(String odName, String objectName) {
    ASTODArtifact artifact = ODNodeFactory.createASTODArtifact();
    ASTObjectDiagram objectDiagram = ODNodeFactory.createASTObjectDiagram();
    artifact.setObjectDiagram(objectDiagram);
    objectDiagram.setName(odName);
    ASTODObject odObject = ODNodeFactory.createASTODObject();
    ASTODName refName = ODNodeFactory.createASTODName();
    refName.setSimpleName(objectName);
    odObject.setODName(refName);
    objectDiagram.getODObjectList().add(odObject);

    GlobalScope globalScope = new GlobalScope(new ModelPath(), odLanguage, resolverConfiguration);

    Optional<ODSymbolTableCreator> symbolTable = odLanguage.getSymbolTableCreator(
        resolverConfiguration, globalScope);

    if (symbolTable.isPresent()) {
      symbolTable.get().createFromAST(artifact);
    }

    return globalScope.<ObjectDiagramSymbol>resolve(odName, ObjectDiagramSymbol.KIND)
        .orElse(null);
  }

}
