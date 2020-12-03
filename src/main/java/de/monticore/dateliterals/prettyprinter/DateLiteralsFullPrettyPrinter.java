package de.monticore.dateliterals.prettyprinter;

import de.monticore.dateliterals.DateLiteralsMill;
import de.monticore.dateliterals._ast.ASTDateLiteralsNode;
import de.monticore.dateliterals._visitor.DateLiteralsTraverser;
import de.monticore.literals.prettyprint.MCCommonLiteralsPrettyPrinter;
import de.monticore.prettyprint.IndentPrinter;

public class DateLiteralsFullPrettyPrinter {
  
  protected DateLiteralsTraverser traverser = DateLiteralsMill.traverser();
  
  protected IndentPrinter printer;
  
  public DateLiteralsFullPrettyPrinter() {
    this.printer = new IndentPrinter();
    init();
  }
  
  public DateLiteralsFullPrettyPrinter(IndentPrinter printer) {
    this.printer = printer;
    init();
  }
  
  public IndentPrinter getPrinter() {
    return this.printer;
  }
  
  public String prettyprint(ASTDateLiteralsNode ast) {
    ast.accept(traverser);
    return getPrinter().getContent();
  }
  
  private void init() {
    
    // date literals
    traverser.setDateLiteralsHandler(new DateLiteralsPrettyPrinter(printer));
    
    // mc common literals
    MCCommonLiteralsPrettyPrinter commonLiterals = new MCCommonLiteralsPrettyPrinter(printer);
    traverser.add4MCCommonLiterals(commonLiterals);
    traverser.setMCCommonLiteralsHandler(commonLiterals);
  }
}
