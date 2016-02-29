/*
 * Copyright (c) 2015 RWTH Aachen. All rights reserved.
 *
 * http://www.se-rwth.de/
 */
package de.monticore.lang.od;

import de.monticore.io.paths.ModelPath;
import de.monticore.lang.od._ast.ASTODDefinition;
import de.monticore.lang.od._ast.ASTODObject;
import de.monticore.lang.od._ast.ODNodeFactory;
import de.monticore.lang.od._symboltable.ODDefinitionSymbol;
import de.monticore.lang.od._symboltable.ODLanguage;
import de.monticore.lang.od._symboltable.ODObjectSymbol;
import de.monticore.lang.od._symboltable.ODSymbolTableCreator;
import de.monticore.symboltable.GlobalScope;
import de.monticore.symboltable.ResolverConfiguration;
import de.monticore.symboltable.Scope;
import org.junit.BeforeClass;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

public class ODSymbolTableCreatorTest {

  private static final String OD_NAME = "AuctionParticipants";

  private static final String OBJECT_NAME = "kupfer912";

  static ODLanguage odLanguage;

  static ResolverConfiguration resolverConfiguration;

  static ModelPath modelPath;

  private Scope globalScope;

  @BeforeClass
  public static void setup() {
    odLanguage = new ODLanguage();

    resolverConfiguration = new ResolverConfiguration();
    resolverConfiguration.addTopScopeResolvers(odLanguage.getResolvers());

    modelPath =
        new ModelPath(Paths.get("src/test/resources/symboltable"));
  }

  @Test
  public void testResolveODObjectFromFile() {
    final ODDefinitionSymbol odDefinitionSymbol =
        createODDefinitionFromFile();

    Optional<ODObjectSymbol> odObject = odDefinitionSymbol.getODObject(OBJECT_NAME);

    assertTrue(odObject.isPresent());
    assertTrue(odObject.get().getAstNode().isPresent());
  }

  @Test
  public void testResolveODObjectFromAST() {
    final ODDefinitionSymbol odDefinitionSymbol =
        createODDefinitionFromAST();

    Optional<ODObjectSymbol> odObject = odDefinitionSymbol.getODObject(OBJECT_NAME);

    assertTrue(odObject.isPresent());
    assertTrue(odObject.get().getAstNode().isPresent());
  }

  private ODDefinitionSymbol createODDefinitionFromFile() {
    globalScope = new GlobalScope(modelPath, odLanguage, resolverConfiguration);
    return globalScope.<ODDefinitionSymbol>resolve(OD_NAME, ODDefinitionSymbol.KIND)
        .orElse(null);
  }

  private ODDefinitionSymbol createODDefinitionFromAST() {
    ASTODDefinition odDefinition = ODNodeFactory.createASTODDefinition();
    odDefinition.setName(OD_NAME);
    ASTODObject odObject = ODNodeFactory.createASTODObject();
    odObject.setName(OBJECT_NAME);
    odDefinition.getODObjects().add(odObject);

    GlobalScope globalScope = new GlobalScope(new ModelPath(), odLanguage, resolverConfiguration);

    Optional<ODSymbolTableCreator> symbolTable = odLanguage.getSymbolTableCreator(
        resolverConfiguration, globalScope);
    symbolTable.get().createFromAST(odDefinition);

    return globalScope.<ODDefinitionSymbol>resolve(OD_NAME, ODDefinitionSymbol.KIND)
        .orElse(null);
  }

}
