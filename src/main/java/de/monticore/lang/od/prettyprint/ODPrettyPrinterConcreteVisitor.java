package de.monticore.lang.od.prettyprint;

import de.monticore.common.prettyprint.CommonPrettyPrinterConcreteVisitor;
import de.monticore.lang.od._ast.*;
import de.monticore.lang.od._visitor.ODVisitor;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.types._ast.ASTImportStatement;
import de.monticore.types.types._ast.ASTQualifiedName;
import de.se_rwth.commons.Joiners;
import de.se_rwth.commons.Names;

import java.util.Iterator;

/**
 * This class is responsible for pretty-printing object diagrams. It is implemented using the
 * Visitor pattern. The Visitor pattern traverses a tree in depth first, the visit and
 * ownVisit-methods are called when a node is traversed, the endVisit methods are called when the
 * whole subtree of a node has been traversed. The ownVisit-Methods stop the automatic traversal
 * order and allow to explictly visit subtrees by calling getVisitor().startVisit(ASTNode)
 * 
 * @author Martin Schindler, Robert Heim
 */
public class ODPrettyPrinterConcreteVisitor extends CommonPrettyPrinterConcreteVisitor implements
    ODVisitor {
  
  /**
   * Constructor.
   *
   * @param printer the printer to write to.
   */
  public ODPrettyPrinterConcreteVisitor(IndentPrinter printer) {
    super(printer);
  }
  
  /**
   * Prints the compilation unit of an object diagram (start of the pretty print)
   * 
   * @param unit a OD compilation unit
   */
  @Override
  public void handle(ASTODCompilationUnit unit) {
    if (unit.getPackage() != null && !unit.getPackage().isEmpty()) {
      getPrinter()
          .println("package " + Names.getQualifiedName(unit.getPackage()) + ";\n");
    }
    if (unit.getVersion().isPresent()) {
      unit.getVersion().get().accept(getRealThis());
    }
    if (unit.getImportStatements() != null && !unit.getImportStatements().isEmpty()) {
      for (ASTImportStatement s : unit.getImportStatements()) {
        getPrinter().print("import " + Names.getQualifiedName(s.getImportList()));
        if (s.isStar()) {
          getPrinter().println(".*;");
        }
        else {
          getPrinter().println(";");
        }
      }
      getPrinter().println();
    }
    unit.getODDefinition().accept(getRealThis());
  }
  
  @Override
  public void handle(ASTVersion node) {
    getPrinter().print("version ");
    node.getStringLiteral().accept(getRealThis());
    getPrinter().println(";");
    getPrinter().println();
  }
  
  /**
   * Prints the object diagram definition
   * 
   * @param a object diagram definition
   */
  @Override
  public void handle(ASTODDefinition a) {
    // print completeness
    if (a.getCompleteness().isPresent()) {
      a.getCompleteness().get().accept(getRealThis());
    }
    // print stereotype
    if (a.getStereotype().isPresent()) {
      a.getStereotype().get().accept(getRealThis());
      getPrinter().println();
    }
    // print object diagram name and parameters
    getPrinter().print("objectdiagram " + a.getName());
    // print body
    getPrinter().println(" {\n");
    getPrinter().indent();
    // print Objects
    
    for (Iterator<ASTODObject> it = a.getODObjects().iterator(); it.hasNext();) {
      it.next().accept(getRealThis());
      if (it.hasNext()) {
        getPrinter().println();
      }
    }
    for (ASTODLink l : a.getODLinks()) {
      l.accept(getRealThis());
    }
    getPrinter().unindent();
    getPrinter().print("}\n");
  }
  
  /**
   * Prints an object in an object diagram
   * 
   * @param a object
   */
  @Override
  public void handle(ASTODObject a) {
    // print completeness
    if (a.getCompleteness().isPresent()) {
      a.getCompleteness().get().accept(getRealThis());
    }
    // print object modifier
    if (a.getModifier().isPresent()) {
      a.getModifier().get().accept(getRealThis());
    }
    // print object name and type
    if (a.getName().isPresent()) {
      getPrinter().print(a.getName().get());
    }
    if (a.getType().isPresent()) {
      getPrinter().print(":");
      a.getType().get().accept(getRealThis());
    }
    // print object body
    if (!a.getODAttributes().isEmpty() || !a.getInnerLinks().isEmpty()) {
      getPrinter().println(" {");
      getPrinter().indent();
      for (ASTODAttribute ast : a.getODAttributes()) {
        ast.accept(getRealThis());
      }
      
      for (ASTODInnerLink ast : a.getInnerLinks()) {
        ast.accept(getRealThis());
      }
      getPrinter().unindent();
      getPrinter().println("}");
    }
    else {
      getPrinter().println(";");
    }
  }
  
  /**
   * Prints an object in an object diagram
   * 
   * @param a object
   */
  @Override
  public void handle(ASTODInnerLink a) {
    if (a.getLinkName().isPresent()) {
      getPrinter().print(a.getLinkName().get());
      if (a.isAggregable()) {
        getPrinter().print(" -> ");
      }
      else {
        getPrinter().print(" = ");
      }
    }
    if (!a.getODInnerObjects().isEmpty()) {
      String delim = "";
      for (ASTODInnerObject odObject : a.getODInnerObjects()) {
        getPrinter().print(delim);
        odObject.accept(getRealThis());
        delim = ", ";
      }
    }
    if (!a.getODQualifiedInnerObjects().isEmpty()) {
      getPrinter().print("[");
      String delim = "";
      for (ASTODQualifiedInnerObject odObject : a.getODQualifiedInnerObjects()) {
        getPrinter().print(delim);
        odObject.accept(getRealThis());
        delim = ", ";
      }
      getPrinter().print("]");
    }
    getPrinter().print(";");
  }
  
  /**
   * @see de.monticore.lang.od._visitor.ODVisitor#handle(de.monticore.lang.od._ast.ASTConformsStatement)
   */
  @Override
  public void handle(ASTConformsStatement a) {
    if (!a.getConformedModels().isEmpty()) {
      getPrinter().print("conformsTo");
      String delim = "";
      for (ASTQualifiedName qualName : a.getConformedModels()) {
        getPrinter().print(delim);
        qualName.accept(getRealThis());
        delim = ", ";
      }
      getPrinter().print(";");
    }
  }
  
  /**
   * @see de.monticore.lang.od._visitor.ODVisitor#handle(de.monticore.lang.od._ast.ASTODQualifiedInnerObject)
   */
  @Override
  public void handle(ASTODQualifiedInnerObject a) {
    if (a.getName().isPresent()) {
      getPrinter().print(a.getName().get() + " -> ");
    }
    if (a.getODValue().isPresent()) {
      a.getODValue().get().accept(getRealThis());
      getPrinter().print(" -> ");
    }
    a.getODInnerObject().accept(getRealThis());
  }
  
  /**
   * Prints an inner object in an object diagram
   * 
   * @param a object
   */
  @Override
  public void handle(ASTODInnerObject a) {
    // print completeness
    if (a.getCompleteness().isPresent()) {
      a.getCompleteness().get().accept(getRealThis());
    }
    // print object modifier
    if (a.getModifier().isPresent()) {
      a.getModifier().get().accept(getRealThis());
    }
    // print object name and type
    if (a.getName().isPresent()) {
      getPrinter().print(a.getName().get());
    }
    if (a.getType().isPresent()) {
      getPrinter().print(":");
      a.getType().get().accept(getRealThis());
    }
    // print object body
    if (!a.getODAttributes().isEmpty() || !a.getInnerLinks().isEmpty()) {
      getPrinter().println(" {");
      getPrinter().indent();
      for (ASTODAttribute ast : a.getODAttributes()) {
        ast.accept(getRealThis());
      }
      
      for (ASTODInnerLink ast : a.getInnerLinks()) {
        ast.accept(getRealThis());
      }
      getPrinter().unindent();
      getPrinter().println("}");
    }
  }
  
  /**
   * Prints an attribute of an object in an object diagram
   * 
   * @param a attribute
   */
  @Override
  public void handle(ASTODAttribute a) {
    // print modifier
    if (a.getModifier().isPresent())
      a.getModifier().get().accept(getRealThis());
    // print type
    if (a.getType().isPresent()) {
      a.getType().get().accept(getRealThis());
      getPrinter().print(" ");
    }
    // print name
    getPrinter().print(a.getName());
    // print value
    if (a.getODValue().isPresent()) {
      getPrinter().print(" = ");
      a.getODValue().get().accept(getRealThis());
    }
    // print value collection
    else if (a.getODValueCollection().isPresent()) {
      getPrinter().print(" = ");
      a.getODValueCollection().get().accept(getRealThis());
    }
    // print value map
    else if (a.getODValueMap().isPresent()) {
      getPrinter().print(" = ");
      a.getODValueMap().get().accept(getRealThis());
    }
    getPrinter().println(";");
  }
  
  /**
   * Prints a qualifier of a link in an object diagram
   * 
   * @param a qualifier of a link
   */
  @Override
  public void handle(ASTODLinkQualifier a) {
    if (a.getName().isPresent()) {
      getPrinter().print("[[");
      getPrinter().print(a.getName().get());
      getPrinter().print("]]");
    }
    else {
      getPrinter().print("[");
      a.getODValue().get().accept(getRealThis());
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
    if (a.getStereotype().isPresent()) {
      a.getStereotype().get().accept(getRealThis());
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
    if (a.getName().isPresent()) {
      getPrinter().print(a.getName().get() + " ");
    }
    // print left modifier
    if (a.getLeftModifier().isPresent()) {
      a.getLeftModifier().get().accept(getRealThis());
    }
    // print objects referenced on the left side of the link
    getPrinter().print(Joiners.COMMA.join(a.getLeftReferenceNames()));
    getPrinter().print(" ");
    // print left qualifier
    if (a.getLeftQualifier().isPresent()) {
      a.getLeftQualifier().get().accept(getRealThis());
    }
    // print left role
    if (a.getLeftRole().isPresent()) {
      getPrinter().print("(");
      getPrinter().print(a.getLeftRole().get());
      getPrinter().print(") ");
    }
    // print arrow
    if (a.isLeftToRight()) {
      getPrinter().print("->");
    }
    if (a.isRightToLeft()) {
      getPrinter().print("<-");
    }
    if (a.isBidirectional()) {
      getPrinter().print("<->");
    }
    if (a.isUnspecified()) {
      getPrinter().print("--");
    }
    // print right role
    if (a.getRightRole().isPresent()) {
      getPrinter().print(" (");
      getPrinter().print(a.getRightRole().get());
      getPrinter().print(")");
    }
    // print right qualifier
    if (a.getRightQualifier().isPresent()) {
      a.getRightQualifier().get().accept(getRealThis());
    }
    // print objects referenced on the right side of the link
    getPrinter().print(" ");
    getPrinter().print(Joiners.COMMA.join(a.getRightReferenceNames()));
    getPrinter().print(" ");
    // print right modifier
    if (a.getRightModifier().isPresent()) {
      a.getRightModifier().get().accept(getRealThis());
    }
    getPrinter().println(";");
  }
  
  /**
   * Prints an ODValueCollection in an object diagram
   *
   * @param a ASTODValueCollection
   */
  @Override
  public void handle(ASTODValueCollection a) {
    getPrinter().print("[");
    for (Iterator<ASTODValue> it = a.getValues().iterator(); it.hasNext();) {
      it.next().accept(getRealThis());
      if (it.hasNext()) {
        getPrinter().print(",");
      }
    }
    getPrinter().print("]");
  }
  
  /**
   * Prints an ODValueMap in an object diagram
   *
   * @param a ASTODValueMap
   */
  @Override
  public void handle(ASTODValueMap a) {
    getPrinter().print("[");
    for (Iterator<ASTODEntry> it = a.getEntries().iterator(); it.hasNext();) {
      it.next().accept(getRealThis());
      if (it.hasNext()) {
        getPrinter().print(",");
      }
    }
    getPrinter().print("]");
  }

  /**
   * Prints an ODEntry in an object diagram
   *
   * @param a ASTODEntry
   */
  @Override
  public void handle(ASTODEntry a) {
    a.getKey().accept(getRealThis());
    getPrinter().print(" -> ");
    a.getValue().accept(getRealThis());
  }

 /**
   * @see de.monticore.lang.od._visitor.ODVisitor#handle(de.monticore.lang.od._ast.ASTODValue)
   */
  @Override
  public void handle(ASTODValue a) {
   if (a.getSignedLiteral().isPresent()) {
    a.getSignedLiteral().get().accept(getRealThis());
   } else {
     getPrinter().print(" Optional_empty");
   }
  }
  
  private ODVisitor realThis = this;
  
  /**
   * @see de.monticore.lang.od._visitor.ODVisitor#setRealThis(de.monticore.lang.od._visitor.ODVisitor)
   */
  @Override
  public void setRealThis(ODVisitor realThis) {
    this.realThis = realThis;
  }
  
  /**
   * @see de.monticore.common.prettyprint.CommonPrettyPrinterConcreteVisitor#getRealThis()
   */
  @Override
  public ODVisitor getRealThis() {
    return realThis;
  }
}
