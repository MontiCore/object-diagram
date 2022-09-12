/* (c) https://github.com/MontiCore/monticore */
package de.monticore.od2cd;

import de.monticore.cd.methodtemplates.CD4C;
import de.monticore.cdbasis.CDBasisMill;
import de.monticore.cdbasis._ast.ASTCDClass;
import de.monticore.cdbasis._ast.ASTCDCompilationUnit;
import de.monticore.cdbasis._ast.ASTCDDefinition;
import de.monticore.cdbasis._ast.ASTCDPackage;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.odbasis._ast.ASTODAttribute;
import de.monticore.odbasis._ast.ASTODElement;
import de.monticore.odbasis._ast.ASTODNamedObject;
import de.monticore.odbasis._visitor.ODBasisVisitor2;
import de.monticore.odlink._ast.ASTODLink;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.tf.odrules._ast.ASTODObject;
import de.monticore.types.prettyprint.MCArrayTypesFullPrettyPrinter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public class OD2CDObjectVisitor implements ODBasisVisitor2 {

  private static final String ERROR_CODE = "0xCD013";
  protected ASTODArtifact odArtifact;

  protected ASTCDCompilationUnit cdCompilationUnit;

  protected ASTCDPackage cdPackage;

  protected ASTCDClass instantiatorClass;

  protected final Map<String, ASTCDClass> objectToClassMap = new HashMap<>();

  protected final CD4C cd4C;

  protected final GlobalExtensionManagement glex;

  @Override
  public void visit(ASTODArtifact odArtifact) {
    this.odArtifact = odArtifact;

    ASTCDDefinition cdDefinition = CDBasisMill.cDDefinitionBuilder()
            .setName(odArtifact.getObjectDiagram().getName())
            .setModifier(CDBasisMill.modifierBuilder().PUBLIC().build())
            .build();

    this.cdPackage = CDBasisMill.cDPackageBuilder()
            .setMCQualifiedName(CDBasisMill.mCQualifiedNameBuilder()
                    .setPartsList(List.of(odArtifact.getObjectDiagram().getName().toLowerCase()))
                    .build())
            .build();

    cdDefinition.addCDElement(cdPackage);

    this.cdCompilationUnit = CDBasisMill.cDCompilationUnitBuilder()
            .setCDDefinition(cdDefinition)
            .build();

    this.instantiatorClass = CDBasisMill.cDClassBuilder()
            .setName(odArtifact.getObjectDiagram().getName()+"Instantiator")
            .setModifier(CDBasisMill.modifierBuilder().PUBLIC().build())
            .build();
    this.cdPackage.addCDElement(instantiatorClass);
  }

  @Override
  public void endVisit(ASTODArtifact odArtifact) {
    this.cd4C.addMethod(this.instantiatorClass,
            "od2cd.InstList",
            this.objectToClassMap.keySet());

    for (String instantiator: this.objectToClassMap.keySet()) {
      this.cd4C.addAttribute(this.instantiatorClass,
              "protected "
                      +instantiator
                      +" "
                      +instantiator.substring(0,1).toLowerCase()
                      +instantiator.substring(1));
    }
  }

  @Override
  public void visit(ASTODElement odElement) {
    ASTODNamedObject odObject = null;
    if (odElement instanceof ASTODNamedObject) {
      odObject = (ASTODNamedObject) odElement;
    }
    assert nonNull(odObject);

    ASTCDClass linkClass = CDBasisMill.cDClassBuilder()
            .setName(odObject.getName().substring(0,1).toUpperCase()
                    +odObject.getName().substring(1)
                    +"Instantiator")
            .setModifier(CDBasisMill.modifierBuilder().PUBLIC().build())
            .build();

    this.cd4C.addMethod(linkClass, "od2cd.Instantiate",
            odObject.getMCObjectType()
                    .printType(new MCArrayTypesFullPrettyPrinter(new IndentPrinter())),
            odObject.getODAttributeList()
                    .stream()
                    .map(ASTODAttribute::getName)
                    .collect(Collectors.toList()),
            odObject.getODAttributeList()
                    .stream()
                    .map(ASTODAttribute::getODValue)
                    .collect(Collectors.toList()));

    this.cdPackage.addCDElement(linkClass);
    this.objectToClassMap.put(linkClass.getName(), linkClass);

  }

  public OD2CDObjectVisitor (GlobalExtensionManagement glex) {
    this.cd4C = CD4C.getInstance();
    this.glex = glex;
  }

  public ASTCDCompilationUnit getCDCompilationUnit() {
    return this.cdCompilationUnit;
  }

  public ASTCDClass getInstantiatorClass() {
    return this.instantiatorClass;
  }

  public Map<String, ASTCDClass> getObjectToClassMap() {
    return this.objectToClassMap;
  }
}
