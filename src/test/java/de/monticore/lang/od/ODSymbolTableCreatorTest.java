/*
 * Copyright (c) 2015 RWTH Aachen. All rights reserved.
 *
 * http://www.se-rwth.de/
 */
package de.monticore.lang.od;

import de.monticore.io.paths.ModelPath;
import de.monticore.lang.od._ast.ASTODDefinition;
import de.monticore.lang.od._ast.ASTODName;
import de.monticore.lang.od._ast.ASTODObject;
import de.monticore.lang.od._ast.ODNodeFactory;
import de.monticore.lang.od._symboltable.ODDefinitionSymbol;
import de.monticore.lang.od._symboltable.ODLanguage;
import de.monticore.lang.od._symboltable.ODSymbolTableCreator;
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

  static ODLanguage odLanguage;

  static ResolvingConfiguration resolverConfiguration;

  static ModelPath modelPath;

  private Scope globalScope;

  @BeforeClass
  public static void setup() {
    odLanguage = new ODLanguage();

    resolverConfiguration = new ResolvingConfiguration();
    resolverConfiguration.addTopScopeResolvers(odLanguage.getResolvers());

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

  private ODDefinitionSymbol createODDefinitionFromFile() {
    globalScope = new GlobalScope(modelPath, odLanguage, resolverConfiguration);
    return globalScope.<ODDefinitionSymbol>resolve(OD_NAME, ODDefinitionSymbol.KIND)
        .orElse(null);
  }

  private ODDefinitionSymbol createODDefinitionFromAST() {
    ASTODDefinition odDefinition = ODNodeFactory.createASTODDefinition();
    odDefinition.setName(OD_NAME);
    ASTODObject odObject = ODNodeFactory.createASTODObject();
    ASTODName refName = ODNodeFactory.createASTODName();
    refName.setName(OBJECT_NAME);
    odObject.setODName(refName);
    odDefinition.getODObjects().add(odObject);

    GlobalScope globalScope = new GlobalScope(new ModelPath(), odLanguage, resolverConfiguration);

    Optional<ODSymbolTableCreator> symbolTable = odLanguage.getSymbolTableCreator(
        resolverConfiguration, globalScope);
    symbolTable.get().createFromAST(odDefinition);

    return globalScope.<ODDefinitionSymbol>resolve(OD_NAME, ODDefinitionSymbol.KIND)
        .orElse(null);
  }

}
