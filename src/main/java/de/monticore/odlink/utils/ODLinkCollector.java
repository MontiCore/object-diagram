// (c) https://github.com/MontiCore/monticore

package de.monticore.odlink.utils;

import de.monticore.odlink._ast.ASTODLink;
import de.monticore.odlink._visitor.ODLinkVisitor;

import java.util.ArrayList;
import java.util.List;

public class ODLinkCollector implements ODLinkVisitor {

  private List<ASTODLink> links = new ArrayList<>();

  @Override
  public void visit(ASTODLink link) {
    links.add(link);
  }

  public List<ASTODLink> getLinks() {
    return links;
  }

  private ODLinkVisitor realThis = this;

  @Override
  public void setRealThis(ODLinkVisitor realThis) {
    this.realThis = realThis;
  }

  @Override
  public ODLinkVisitor getRealThis() {
    return realThis;
  }

}
