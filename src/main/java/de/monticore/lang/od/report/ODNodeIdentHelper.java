/*
 * Copyright (c) 2014 RWTH Aachen. All rights reserved.
 *
 * http://www.se-rwth.de/
 */
package de.monticore.lang.od.report;

import de.monticore.ast.ASTNode;
import de.monticore.generating.templateengine.reporting.commons.Layouter;
import de.monticore.lang.od._ast.ASTODAttribute;
import de.monticore.lang.od._ast.ASTODCompilationUnit;
import de.monticore.lang.od._ast.ASTODDefinition;
import de.monticore.lang.od._ast.ASTODInnerLink;
import de.monticore.lang.od._ast.ASTODLink;
import de.monticore.lang.od._ast.ASTODObject;
import de.monticore.lang.od._ast.ASTODReferenceName;
import de.monticore.literals.literals._ast.ASTIntLiteral;
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
  
  public String getIdent(ASTODDefinition a) {
    String type = Layouter.nodeName(a);
    String name = a.getName();
    return format(name, type);
  }
  
  public String getIdent(ASTODCompilationUnit a) {
    String type = Layouter.nodeName(a);
    String name = a.getODDefinition().getName();
    return format(name, type);
  }
  
  public String getIdent(ASTODObject a) {
    String type = Layouter.nodeName(a);
    String name = "";
    if (a.getName().isPresent()) {
      ASTODReferenceName ref = a.getName().get();
      if (ref.getName().isPresent()) {
        name = ref.getName().get();
      } 
    }
    else if (a.getType().isPresent()) {
      name = a.getType().toString();
    }
    return format(name, type);
  }
  
  public String getIdent(ASTODAttribute a) {
    return format(a.getName(), Layouter.nodeName(a));
  }
  
  public String getIdent(ASTODLink a) {
    String name = "";
    if (a.getName().isPresent()) {
      name = a.getName().get();
    }
    else if (!a.getLeftReferenceNames().isEmpty() && !a.getRightReferenceNames().isEmpty()) {
      // TODO MB
    }
    return format(name, Layouter.nodeName(a));
  }
  
  public String getIdent(ASTODInnerLink a) {
    String name = a.getLinkName();
    return format(name, Layouter.nodeName(a));
  }
  
  @Override
  public String getIdent(ASTNode a) {
    if (a instanceof ASTODCompilationUnit) {
      return getIdent((ASTODCompilationUnit) a);
    }
    else if (a instanceof ASTODDefinition) {
      return getIdent((ASTODDefinition) a);
    }
    else if (a instanceof ASTODAttribute) {
      return getIdent((ASTODAttribute) a);
    }
    else if (a instanceof ASTODInnerLink) {
      return getIdent((ASTODInnerLink) a);
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
  
}
