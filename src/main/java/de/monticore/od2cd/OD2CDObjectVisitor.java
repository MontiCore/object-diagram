/* (c) https://github.com/MontiCore/monticore */
package de.monticore.od2cd;

import de.monticore.cd.methodtemplates.CD4C;
import de.monticore.cdbasis.CDBasisMill;
import de.monticore.cdbasis._ast.ASTCDClass;
import de.monticore.cdbasis._ast.ASTCDCompilationUnit;
import de.monticore.cdbasis._ast.ASTCDDefinition;
import de.monticore.cdbasis._ast.ASTCDPackage;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.od.prettyprinter.ODFullPrettyPrinter;
import de.monticore.od4data.prettyprinter.OD4DataFullPrettyPrinter;
import de.monticore.odbasis._ast.*;
import de.monticore.odbasis._visitor.ODBasisVisitor2;
import de.monticore.odlink._ast.*;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.prettyprint.MCBasicsPrettyPrinter;
import de.monticore.types.prettyprint.MCArrayTypesFullPrettyPrinter;
import de.monticore.types.prettyprint.MCBasicTypesFullPrettyPrinter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OD2CDObjectVisitor implements ODBasisVisitor2 {

  protected ASTCDCompilationUnit cdCompilationUnit;

  protected ASTCDPackage cdPackage;

  protected ASTCDClass instantiatorClass;

  protected final Map<String, ASTCDClass> objectToClassMap = new HashMap<>();

  protected final List<String> linkAttributeList = new ArrayList<>();

  protected final CD4C cd4C;

  protected final GlobalExtensionManagement glex;

  @Override
  public void visit(ASTODArtifact odArtifact) {
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
            this.objectToClassMap.keySet(),
            odArtifact.getObjectDiagram().getODElementList().stream().map(e -> {
              if (e instanceof ASTODNamedObject) {
                ASTODNamedObject object = (ASTODNamedObject) e;
                return object
                        .getMCObjectType()
                        .printType(new MCArrayTypesFullPrettyPrinter(new IndentPrinter()));
              } else {
                return null;
              }
            }).collect(Collectors.toList()),
            odArtifact.getObjectDiagram().getODElementList().stream().map(e -> {
              if (e instanceof ASTODNamedObject) {
                ASTODNamedObject object = (ASTODNamedObject) e;
                return object.getName();
              } else {
                return null;
              }
            }).collect(Collectors.toList()),
            this.linkAttributeList);

    for (String instantiator: this.objectToClassMap.keySet()) {
      this.cd4C.addAttribute(this.instantiatorClass,
              "protected "
                      +instantiator
                      +" "
                      +instantiator.substring(0,1).toLowerCase()
                      +instantiator.substring(1));
    }
  }

  public void handleNamedObjects(ASTODNamedObject odElement) {
    ASTCDClass instClass = CDBasisMill.cDClassBuilder()
            .setName(odElement.getName().substring(0,1).toUpperCase()
                    +odElement.getName().substring(1)
                    +"Instantiator")
            .setModifier(CDBasisMill.modifierBuilder().PUBLIC().build())
            .build();

    this.cd4C.addMethod(instClass, "od2cd.Instantiate",
            odElement.getMCObjectType()
                    .printType(new MCArrayTypesFullPrettyPrinter(new IndentPrinter())),
            odElement.getODAttributeList()
                    .stream()
                    .map(ASTODAttribute::getName)
                    .collect(Collectors.toList()),
            odElement.getODAttributeList()
                    .stream()
                    .map(a -> new OD4DataFullPrettyPrinter().prettyprint(a.getODValue()))
                    .collect(Collectors.toList()));

    this.cdPackage.addCDElement(instClass);
    this.objectToClassMap.put(instClass.getName(), instClass);
  }

  public void handleLinks(ASTODLink odLink) {
    List<String> leftSides = odLink.getODLinkLeftSide().getReferenceNamesList().stream().map(ASTODName::getName).collect(Collectors.toList());

    List<String> rightSides = odLink.getODLinkRightSide().getReferenceNamesList().stream().map(ASTODName::getName).collect(Collectors.toList());

    String roleName = odLink.getODLinkRightSide().isPresentRole() ? odLink.getODLinkRightSide().getRole() : "";

    if (odLink.getODLinkDirection() instanceof ASTODLeftToRightDir) {
      for (String left: leftSides) {
        for (String right: rightSides) {
          this.linkAttributeList.add(left + ".add" + ((roleName.isEmpty()) ? capFirst(right) : capFirst(roleName)) + "(" + right + ")");
        }
      }
    }
    if (odLink.getODLinkDirection() instanceof ASTODRightToLeftDir) {
      for (String right: rightSides) {
        for (String left: leftSides) {
          this.linkAttributeList.add(right + ".add" + ((roleName.isEmpty()) ? capFirst(left) : capFirst(roleName)) + "(" + left + ")");
        }
      }
    }
    if (odLink.getODLinkDirection() instanceof ASTODBiDir || odLink.getODLinkDirection() instanceof ASTODUnspecifiedDir) {
      for (String left: leftSides) {
        for (String right: rightSides) {
          this.linkAttributeList.add(left + ".add" + ((roleName.isEmpty()) ? capFirst(right) : capFirst(roleName)) + "(" + right + ")");
        }
      }
      for (String right: rightSides) {
        for (String left: leftSides) {
          this.linkAttributeList.add(right + ".add" + ((roleName.isEmpty()) ? capFirst(left) : capFirst(roleName)) + "(" + left + ")");
        }
      }
    }
  }

  @Override
  public void visit(ASTODElement odElement) {
    if(odElement instanceof ASTODNamedObject) {
      handleNamedObjects((ASTODNamedObject) odElement);
    }
    if (odElement instanceof ASTODLink) {
      handleLinks((ASTODLink) odElement);
    }
  }

  protected String capFirst(String s) {
    return s.substring(0,1).toUpperCase() + s.substring(1);
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
