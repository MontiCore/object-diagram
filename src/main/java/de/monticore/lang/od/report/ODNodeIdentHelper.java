/*
 * Copyright (c) 2014 RWTH Aachen. All rights reserved.
 *
 * http://www.se-rwth.de/
 */
package de.monticore.lang.od.report;

import de.monticore.ast.ASTNode;
import de.monticore.generating.templateengine.reporting.commons.Layouter;
import de.monticore.lang.od._ast.ASTODArtefact;
import de.monticore.lang.od._ast.ASTODAttribute;
import de.monticore.lang.od._ast.ASTODLink;
import de.monticore.lang.od._ast.ASTODName;
import de.monticore.lang.od._ast.ASTODObject;
import de.monticore.lang.od._ast.ASTObjectDiagram;
import de.monticore.literals.literals._ast.ASTIntLiteral;
import de.monticore.symboltable.Scope;
import de.monticore.symboltable.Symbol;
import de.monticore.types.TypesNodeIdentHelper;
import de.monticore.types.types._ast.ASTPrimitiveType;
import de.monticore.types.types._ast.ASTQualifiedName;
import de.monticore.types.types._ast.ASTSimpleReferenceType;

/**
 * TODO: Write me!
 *
 * @author (last commit) $Author$
 * @version $Revision$, $Date$
 * @since TODO: add version number
 */
public class ODNodeIdentHelper extends TypesNodeIdentHelper {
  
  private String getIdent(ASTObjectDiagram a) {
    String type = Layouter.nodeName(a);
    String name = a.getName();
    return format(name, type);
  }
  
  private String getIdent(ASTODArtefact a) {
    String type = Layouter.nodeName(a);
    String name = a.getObjectDiagram().getName();
    return format(name, type);
  }
  
  private String getIdent(ASTODObject a) {
    String type = Layouter.nodeName(a);
    String name = "";
    if (a.getODName().isPresent()) {
      ASTODName ref = a.getODName().get();
      if (ref.getName().isPresent()) {
        name = ref.getName().get();
      }
      if (ref.getODSpecialName().isPresent()) {
        name = ref.getODSpecialName().get();
      }
    }
        
    return format(maskSpecialChars(name), type);
  }
  
  private String maskSpecialChars(String name) {
    // Replace all special characters by _
    name = name.replaceAll("[^a-zA-Z0-9_$\\-+]", "_");
    if (name.matches("[0-9].*")) {
      // if the name starts with a digit ...
      name = "_".concat(name);
    }
    return name;
  }
  
  private String getIdent(ASTODAttribute a) {
    return format(a.getName(), Layouter.nodeName(a));
  }
  
  private String getIdent(ASTODLink a) {
    String name = "";
    if (a.getName().isPresent()) {
      name = a.getName().get();
    }
    else if (!a.getLeftReferenceNames().isEmpty() && !a.getRightReferenceNames().isEmpty()) {
      // TODO MB
    }
    return format(name, Layouter.nodeName(a));
  }
  
  @Override
  public String getIdent(ASTNode a) {
    if (a instanceof ASTODArtefact) {
      return getIdent((ASTODArtefact) a);
    }
    else if (a instanceof ASTObjectDiagram) {
      return getIdent((ASTObjectDiagram) a);
    }
    else if (a instanceof ASTODAttribute) {
      return getIdent((ASTODAttribute) a);
    }
    else if (a instanceof ASTODLink) {
      return getIdent((ASTODLink) a);
    }
    else if (a instanceof ASTODObject) {
      return getIdent((ASTODObject) a);
    }
    else if (a instanceof ASTPrimitiveType) {
      return getIdent((ASTPrimitiveType) a);
    }
    else if (a instanceof ASTQualifiedName) {
      return getIdent((ASTQualifiedName) a);
    }
    else if (a instanceof ASTSimpleReferenceType) {
      return getIdent((ASTSimpleReferenceType) a);
    }
    else if (a instanceof ASTIntLiteral) {
      return getIdent((ASTIntLiteral) a);
    }
    else {
      String type = Layouter.className(a);
      return format(type);
    }
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
