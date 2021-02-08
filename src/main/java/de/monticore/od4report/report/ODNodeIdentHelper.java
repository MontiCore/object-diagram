/* (c) https://github.com/MontiCore/monticore */

package de.monticore.od4report.report;

import de.monticore.ast.ASTNode;
import de.monticore.generating.templateengine.reporting.commons.Layouter;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.odbasis._ast.ASTODAttribute;
import de.monticore.odbasis._ast.ASTODObject;
import de.monticore.odbasis._ast.ASTObjectDiagram;
import de.monticore.odlink._ast.ASTODLink;
import de.monticore.symboltable.IScope;
import de.monticore.symboltable.ISymbol;
import de.monticore.types.MCBasicTypesNodeIdentHelper;

/**
 * Helper for ODs.
 */
public class ODNodeIdentHelper extends MCBasicTypesNodeIdentHelper {

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
    String name;
    name = a.getName();

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
    else if (!a.getODLinkLeftSide().getReferenceNamesList().isEmpty() && !a.getODLinkRightSide()
        .getReferenceNamesList()
        .isEmpty()) {
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
   * @see de.monticore.generating.templateengine.reporting.commons.IASTNodeIdentHelper#getIdent(de.monticore.symboltable.ISymbol)
   */
  @Override
  public String getIdent(ISymbol symbol) {
    return format(maskSpecialChars(symbol.getName()), symbol.getClass().getSimpleName());
  }

  /**
   * @see de.monticore.generating.templateengine.reporting.commons.IASTNodeIdentHelper#getIdent(de.monticore.symboltable.IScope)
   */
  @Override
  public String getIdent(IScope scope) {
    return format(scope.getName(), scope.getClass().getSimpleName());
  }

}
