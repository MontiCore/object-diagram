// (c) https://github.com/MontiCore/monticore

package de.monticore.odlink.prettyprinter;

import de.monticore.odbasis._ast.ASTODName;
import de.monticore.odlink._ast.*;
import de.monticore.odlink._visitor.ODLinkHandler;
import de.monticore.odlink._visitor.ODLinkTraverser;
import de.monticore.prettyprint.IndentPrinter;

import java.util.Iterator;

public class ODLinkPrettyPrinter implements ODLinkHandler {

  protected IndentPrinter printer;

  protected ODLinkTraverser traverser;

  public ODLinkPrettyPrinter(IndentPrinter printer) {
    this.printer = printer;
  }

  @Override
  public void handle(ASTODLinkQualifier a) {
    if (a.isPresentName()) {
      getPrinter().print("[[");
      getPrinter().print(a.getName());
      getPrinter().print("]]");
    }
    else if (a.isPresentODValue()) {
      getPrinter().print("[");
      a.getODValue().accept(getTraverser());
      getPrinter().print("]");
    }
    getPrinter().print(" ");
  }

  @Override
  public void handle(ASTODLink a) {
    getPrinter().println();
    // print stereotype
    if (a.isPresentStereotype()) {
      a.getStereotype().accept(getTraverser());
      getPrinter().print(" ");
    }
    // print type of the link
    if (a.isLink()) {
      getPrinter().print("link ");
    }
    else if (a.isAggregation()) {
      getPrinter().print("aggregation ");
    }
    else if (a.isComposition()) {
      getPrinter().print("composition ");
    }
    // print name
    if (a.isDerived()) {
      getPrinter().print("/");
    }
    if (a.isPresentName()) {
      getPrinter().print(a.getName() + " ");
    }
    // print left modifier
    a.getODLinkLeftSide().getModifier().accept(getTraverser());

    // print objects referenced on the left side of the link
    Iterator<ASTODName> refNames = a.getODLinkLeftSide().getReferenceNamesList().iterator();
    while (refNames.hasNext()) {
      refNames.next().accept(getTraverser());
      if (refNames.hasNext()) {
        getPrinter().print(", ");
      }
    }

    getPrinter().print(" ");

    // print left qualifier
    if (a.getODLinkLeftSide().isPresentODLinkQualifier()) {
      a.getODLinkLeftSide().getODLinkQualifier().accept(getTraverser());
    }
    // print left role
    if (a.getODLinkLeftSide().isPresentRole()) {
      getPrinter().print("(");
      getPrinter().print(a.getODLinkLeftSide().getRole());
      getPrinter().print(") ");
    }
    // print arrow
    a.getODLinkDirection().accept(getTraverser());

    // print right role
    if (a.getODLinkRightSide().isPresentRole()) {
      getPrinter().print(" (");
      getPrinter().print(a.getODLinkRightSide().getRole());
      getPrinter().print(")");
    }
    // print right qualifier
    if (a.getODLinkRightSide().isPresentODLinkQualifier()) {
      a.getODLinkRightSide().getODLinkQualifier().accept(getTraverser());
    }
    // print objects referenced on the right side of the link
    getPrinter().print(" ");
    refNames = a.getODLinkRightSide().getReferenceNamesList().iterator();
    while (refNames.hasNext()) {
      refNames.next().accept(getTraverser());
      if (refNames.hasNext()) {
        getPrinter().print(", ");
      }
    }
    // print right modifier
    a.getODLinkRightSide().getModifier().accept(getTraverser());
  }

  @Override
  public void handle(ASTODLeftToRightDir leftToRightDir) {
    getPrinter().print("->");
  }

  @Override
  public void handle(ASTODRightToLeftDir rightToLeftDir) {
    getPrinter().print("<-");
  }

  @Override
  public void handle(ASTODBiDir biDir) {
    getPrinter().print("<->");
  }

  @Override
  public void handle(ASTODUnspecifiedDir unspecifiedDir) {
    getPrinter().print("--");
  }

  @Override
  public ODLinkTraverser getTraverser() {
    return traverser;
  }

  @Override
  public void setTraverser(ODLinkTraverser traverser) {
    this.traverser = traverser;
  }

  public IndentPrinter getPrinter() {
    return printer;
  }

}
