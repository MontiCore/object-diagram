// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore

package de.monticore.odbasics._ast;

import java.util.ArrayList;

public class ASTODLink extends ASTODLinkTOP {

  /**
   * This method returns the left reference names of an {@link ASTODLink} as Strings.
   *
   * @return List of Strings containing the left reference names
   */
  public ArrayList<String> getLeftReferenceNames() {
    ArrayList<String> leftNames = new ArrayList<>();
    getODLinkLeftSide().getReferenceNameList().forEach(astodName -> leftNames.add(astodName.getName()));
    return leftNames;
  }

  /**
   * This method returns the right reference names of an {@link ASTODLink} as String.
   *
   * @return List of Strings containing the right reference names
   */
  public ArrayList<String> getRightReferenceNames() {
    ArrayList<String> rightNames = new ArrayList<>();
    getODLinkRightSide().getReferenceNameList().forEach(astodName -> rightNames.add(astodName.getName()));
    return rightNames;
  }

}

