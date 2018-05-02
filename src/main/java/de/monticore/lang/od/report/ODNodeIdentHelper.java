/*
 * Copyright (c) 2017, MontiCore. All rights reserved.
 *
 * http://www.se-rwth.de/
 */
package de.monticore.lang.od.report;

import de.monticore.ast.ASTNode;
import de.monticore.generating.templateengine.reporting.commons.Layouter;
import de.monticore.lang.od._ast.*;
import de.monticore.symboltable.Scope;
import de.monticore.symboltable.Symbol;
import de.monticore.types.TypesNodeIdentHelper;

/**
 * TODO: Write me!
 *
 * @author (last commit) $Author$
 * @since TODO: add version number
 */
public class ODNodeIdentHelper extends TypesNodeIdentHelper {

  public String getIdent(ASTObjectDiagram a) {
    String type = Layouter.nodeName(a);
    String name = a.getName();
    return format(name, type);
  }

  public String getIdent(ASTODArtifact a) {
    String type = Layouter.nodeName(a);
    String name = a.getObjectDiagram().getName();
    return format(name, type);
  }

  public String getIdent(ASTODObject a) {
    String type = Layouter.nodeName(a);
    String name = "";
    if (a.isPresentODName()) {
      ASTODName ref = a.getODName();
      if (ref.isPresentName()) {
        name = ref.getName();
      }
      if (ref.isPresentODSpecialName()) {
        name = ref.getODSpecialName();
      }
    }

    return format(maskSpecialChars(name), type);
  }

  public String maskSpecialChars(String name) {
    // Replace all special characters by _
    name = name.replaceAll("[^a-zA-Z0-9_$\\-+]", "_");
    if (name.matches("[0-9].*")) {
      // if the name starts with a digit ...
      name = "_".concat(name);
    }
    return name;
  }

  public String getIdent(ASTODAttribute a) {
    return format(a.getName(), Layouter.nodeName(a));
  }

  public String getIdent(ASTODLink a) {
    String name = "";
    if (a.isPresentName()) {
      name = a.getName();
    }
    else if (!a.getLeftReferenceNameList().isEmpty() && !a.getRightReferenceNameList().isEmpty()) {
      // TODO MB
    }
    return format(name, Layouter.nodeName(a));
  }

  @Override
  public String getIdent(ASTNode a) {
    String type = Layouter.className(a);
    return format(type);
  }

  /**
   * @see de.monticore.generating.templateengine.reporting.commons.IASTNodeIdentHelper#getIdent(de.monticore.symboltable.Symbol)
   */
  @Override
  public String getIdent(Symbol symbol) {
    return format(maskSpecialChars(symbol.getName()), symbol.getClass().getSimpleName());
  }

  /**
   * @see de.monticore.generating.templateengine.reporting.commons.IASTNodeIdentHelper#getIdent(de.monticore.symboltable.Scope)
   */
  @Override
  public String getIdent(Scope scope) {
    return format(scope.getName().orElse(""), scope.getClass().getSimpleName());
  }

}
