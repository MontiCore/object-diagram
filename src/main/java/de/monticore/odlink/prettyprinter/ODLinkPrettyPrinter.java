// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore

package de.monticore.odlink.prettyprinter;

import de.monticore.odbasis._ast.ASTODName;
import de.monticore.odlink._ast.*;
import de.monticore.odlink._visitor.ODLinkVisitor;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.prettyprint.MCBasicsPrettyPrinter;

import java.util.Iterator;

public class ODLinkPrettyPrinter extends MCBasicsPrettyPrinter implements ODLinkVisitor {

  /**
   * Constructor.
   *
   * @param printer the printer to write to.
   */
  public ODLinkPrettyPrinter(IndentPrinter printer) {
    super(printer);
  }

  /**
   * Prints a qualifier of a link in an object diagram
   *
   * @param a qualifier of a link
   */
  @Override
  public void handle(ASTODLinkQualifier a) {
    if (a.isPresentName()) {
      getPrinter().print("[[");
      getPrinter().print(a.getName());
      getPrinter().print("]]");
    }
    else if (a.isPresentODValue()) {
      getPrinter().print("[");
      a.getODValue().accept(getRealThis());
      getPrinter().print("]");
    }
    getPrinter().print(" ");
  }

  /**
   * Prints an association, aggregation or composition in an object diagram
   *
   * @param a association, aggregation or composition
   */
  @Override
  public void handle(ASTODLink a) {
    getPrinter().println();
    // print stereotype
    if (a.isPresentStereotype()) {
      a.getStereotype().accept(getRealThis());
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
    if (a.getODLinkLeftSide().isPresentModifier()) {
      a.getODLinkLeftSide().getModifier().accept(getRealThis());
    }

    // print objects referenced on the left side of the link
    Iterator<ASTODName> refNames = a.getODLinkLeftSide().getReferenceNamesList().iterator();
    while (refNames.hasNext()) {
      refNames.next().accept(getRealThis());
      if (refNames.hasNext()) {
        getPrinter().print(", ");
      }
    }

    getPrinter().print(" ");

    // print left qualifier
    if (a.getODLinkLeftSide().isPresentODLinkQualifier()) {
      a.getODLinkLeftSide().getODLinkQualifier().accept(getRealThis());
    }
    // print left role
    if (a.getODLinkLeftSide().isPresentRole()) {
      getPrinter().print("(");
      getPrinter().print(a.getODLinkLeftSide().getRole());
      getPrinter().print(") ");
    }
    // print arrow
    a.getODLinkDirection().accept(getRealThis());

    // print right role
    if (a.getODLinkRightSide().isPresentRole()) {
      getPrinter().print(" (");
      getPrinter().print(a.getODLinkRightSide().getRole());
      getPrinter().print(")");
    }
    // print right qualifier
    if (a.getODLinkRightSide().isPresentODLinkQualifier()) {
      a.getODLinkRightSide().getODLinkQualifier().accept(getRealThis());
    }
    // print objects referenced on the right side of the link
    getPrinter().print(" ");
    refNames = a.getODLinkRightSide().getReferenceNamesList().iterator();
    while (refNames.hasNext()) {
      refNames.next().accept(getRealThis());
      if (refNames.hasNext()) {
        getPrinter().print(", ");
      }
    }
    // print right modifier
    if (a.getODLinkRightSide().isPresentModifier()) {
      a.getODLinkRightSide().getModifier().accept(getRealThis());
    }
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

  private ODLinkVisitor realThis = this;

  /**
   * @see de.monticore.odlink._visitor.ODLinkVisitor#setRealThis(de.monticore.odlink._visitor.ODLinkVisitor)
   */
  @Override
  public void setRealThis(ODLinkVisitor realThis) {
    this.realThis = realThis;
  }

  /**
   * @see de.monticore.odlink._visitor.ODLinkVisitor#getRealThis()
   */
  @Override
  public ODLinkVisitor getRealThis() {
    return realThis;
  }

}
