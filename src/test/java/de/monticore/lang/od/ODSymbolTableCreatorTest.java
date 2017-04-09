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
import de.monticore.lang.od._symboltable.ODSymbolTableCreator;
import de.monticore.lang.od._symboltable.ObjectDiagramSymbol;
import de.monticore.symboltable.GlobalScope;
import de.monticore.symboltable.ResolvingConfiguration;
import de.monticore.symboltable.Scope;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.Optional;

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

  @Ignore
  @Test
  public void testResolveODObjectFromFile() {
    /* TODO Create Symbol table
    final ODDefinitionSymbol odDefinitionSymbol =
        createODDefinitionFromFile();

    Optional<ODObjectSymbol> odObject = odDefinitionSymbol.getODObject(OBJECT_NAME);

    assertTrue(odObject.isPresent());
    assertTrue(odObject.get().getAstNode().isPresent());
    */
  }

  @Ignore
  @Test
  public void testResolveODObjectFromAST() {
    /* TODO Create Symbol table
    final ODDefinitionSymbol odDefinitionSymbol =
        createODDefinitionFromAST();

    Optional<ODObjectSymbol> odObject = odDefinitionSymbol.getODObject(OBJECT_NAME);

    assertTrue(odObject.isPresent());
    assertTrue(odObject.get().getAstNode().isPresent());
    */
  }

  private ObjectDiagramSymbol createODDefinitionFromFile() {
    globalScope = new GlobalScope(modelPath, odLanguage, resolverConfiguration);
    return globalScope.<ObjectDiagramSymbol>resolve(OD_NAME, ObjectDiagramSymbol.KIND)
        .orElse(null);
  }

  private ObjectDiagramSymbol createODDefinitionFromAST() {
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
