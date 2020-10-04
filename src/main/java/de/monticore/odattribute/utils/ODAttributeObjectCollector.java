// (c) https://github.com/MontiCore/monticore

package de.monticore.odattribute.utils;

import de.monticore.odattribute._visitor.ODAttributeVisitor;
import de.monticore.odbasis._ast.ASTODAnonymousObject;
import de.monticore.odbasis._ast.ASTODNamedObject;
import de.monticore.odbasis._ast.ASTODObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ODAttributeObjectCollector implements ODAttributeVisitor {
  private List<ASTODNamedObject> namedObjects = new ArrayList<>();

  private List<ASTODAnonymousObject> anonymousObjects = new ArrayList<>();

  @Override
  public void visit(ASTODNamedObject namedObject) {
    namedObjects.add(namedObject);
  }

  @Override
  public void visit(ASTODAnonymousObject anonymousObject) {
    anonymousObjects.add(anonymousObject);
  }

  public List<ASTODNamedObject> getNamedObjects() {
    return namedObjects;
  }

  public List<ASTODAnonymousObject> getAnonymousObjects() {
    return anonymousObjects;
  }

  public List<ASTODObject> getODObjects() {
    return Stream.concat(namedObjects.stream(), anonymousObjects.stream())
        .collect(Collectors.toList());
  }

  private ODAttributeVisitor realThis = this;

  @Override
  public void setRealThis(ODAttributeVisitor realThis) {
    this.realThis = realThis;
  }

  @Override
  public ODAttributeVisitor getRealThis() {
    return realThis;
  }

}
