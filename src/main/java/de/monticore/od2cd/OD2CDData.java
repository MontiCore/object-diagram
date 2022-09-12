/* (c) https://github.com/MontiCore/monticore */
package de.monticore.od2cd;

import de.monticore.cdbasis._ast.ASTCDClass;
import de.monticore.cdbasis._ast.ASTCDCompilationUnit;

import java.util.Collection;

public class OD2CDData {

  protected final ASTCDCompilationUnit compilationUnit;

  protected final ASTCDClass instantiatorClass;

  protected final Collection<ASTCDClass> objectClasses;

  public OD2CDData (ASTCDCompilationUnit compilationUnit,
                    ASTCDClass instantiatorClass,
                    Collection<ASTCDClass> objectClasses) {
    this.compilationUnit = compilationUnit;
    this.instantiatorClass = instantiatorClass;
    this.objectClasses = objectClasses;
  }
}