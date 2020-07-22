// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore

package de.monticore.odlink._visitor;

import de.monticore.odlink._ast.ASTODLink;

import java.util.ArrayList;
import java.util.List;

public class LinkVisitor implements ODLinkVisitor {

  private List<ASTODLink> links = new ArrayList<>();

  @Override
  public void visit(ASTODLink link) {
    links.add(link);
  }

  public List<ASTODLink> getODLinkList() {
    return links;
  }

}
