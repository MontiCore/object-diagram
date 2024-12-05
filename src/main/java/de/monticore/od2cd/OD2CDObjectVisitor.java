/* (c) https://github.com/MontiCore/monticore */
package de.monticore.od2cd;

import de.monticore.cd.methodtemplates.CD4C;
import de.monticore.cdbasis.CDBasisMill;
import de.monticore.cdbasis._ast.ASTCDClass;
import de.monticore.cdbasis._ast.ASTCDCompilationUnit;
import de.monticore.cdbasis._ast.ASTCDDefinition;
import de.monticore.cdbasis._ast.ASTCDPackage;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.od4data._prettyprint.OD4DataFullPrettyPrinter;
import de.monticore.od4development.OD4DevelopmentMill;
import de.monticore.od4development._symboltable.CDRoleAdapter;
import de.monticore.od4development._symboltable.CDRoleAdapter.LinkCardinality;
import de.monticore.odbasis._ast.*;
import de.monticore.odbasis._visitor.ODBasisVisitor2;
import de.monticore.odlink._ast.*;
import de.monticore.odlink._symboltable.IODLinkScope;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.symbols.basicsymbols._symboltable.TypeSymbol;
import de.monticore.symbols.oosymbols._symboltable.FieldSymbol;
import de.monticore.types.mcbasictypes._ast.ASTMCImportStatement;

import java.util.*;
import java.util.stream.Collectors;

public class OD2CDObjectVisitor implements ODBasisVisitor2 {

  protected ASTCDCompilationUnit cdCompilationUnit;

  protected ASTCDPackage cdPackage;

  protected ASTCDClass instantiatorClass;

  protected ASTCDClass checkerClass;

  protected final Map<String, ASTCDClass> objectToClassMap = new HashMap<>();

  protected final List<String> linkAttributeList = new ArrayList<>();

  protected final List<String> objectNameList = new ArrayList<>();

  protected final CD4C cd4C;

  protected final GlobalExtensionManagement glex;
  
  protected final CompositionPrinter cp;
  
  protected ASTMCImportStatement imp = null;
  
  protected ASTObjectDiagram diagram;

  @Override
  public void visit(ASTODArtifact odArtifact) {
    diagram = odArtifact.getObjectDiagram();
    
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
    
    if (odArtifact.getMCImportStatementList().size() > 0) {
      ASTMCImportStatement i = odArtifact.getMCImportStatementList().get(0);
      imp = CDBasisMill.mCImportStatementBuilder()
          .setMCQualifiedName(CDBasisMill.mCQualifiedNameBuilder().addParts(i.getQName().toLowerCase()).build())
          .setStar(true).build();
    }

    this.instantiatorClass = CDBasisMill.cDClassBuilder()
            .setName(odArtifact.getObjectDiagram().getName() + "Instantiator")
            .setModifier(CDBasisMill.modifierBuilder().PUBLIC().build())
            .build();
    
    this.checkerClass = CDBasisMill.cDClassBuilder()
            .setName(odArtifact.getObjectDiagram().getName() + "Checker")
            .setModifier(CDBasisMill.modifierBuilder().PUBLIC().build())
            .build();
    
    cd4C.addImport(instantiatorClass, imp.getQName() + ".*");
    cd4C.addImport(instantiatorClass, "java.time.*");
    cd4C.addImport(checkerClass, imp.getQName() + ".*");
    
    
    this.cdPackage.addCDElement(instantiatorClass);
//    this.cdPackage.addCDElement(checkerClass);
  }

  @Override
  public void endVisit(ASTODArtifact odArtifact) {
    this.cd4C.addMethod(this.instantiatorClass,
            "od2cd.InstList",
            odArtifact.getObjectDiagram().getODElementList()
                    .stream().
                    filter(e -> e instanceof ASTODNamedObject)
                    .map(e -> cp.genType(((ASTODNamedObject) e)
                            .getMCObjectType()))
                    .collect(Collectors.toList()),
            odArtifact.getObjectDiagram().getODElementList()
                    .stream()
                    .filter(e -> e instanceof ASTODNamedObject)
                    .map(e -> ((ASTODNamedObject) e).getName())
                    .collect(Collectors.toList()),
            this.linkAttributeList,
            this.objectNameList);
  }

  public void createInstantiator(ASTODNamedObject odElement) {
    this.cd4C.addMethod(instantiatorClass, "od2cd.Instantiate",
        odElement.getMCObjectType(),
            cp.genType(odElement.getMCObjectType()),
            odElement.getODAttributeList()
                    .stream()
                    .map(ASTODAttribute::getName)
                    .collect(Collectors.toList()),
            odElement.getODAttributeList()
                    .stream()
                    .map(a -> new OD4DataFullPrettyPrinter(new IndentPrinter()).prettyprint(a.getODValue()))
                    .collect(Collectors.toList()),
            odElement.getName());

    this.objectNameList.add(odElement.getName());
  }

  public void createChecker(ASTODNamedObject odElement) {
    this.cd4C.addMethod(checkerClass, "od2cd.Check",
            odElement.getName(),
            odElement.getODAttributeList()
                    .stream()
                    .map(ASTODAttribute::getName)
                    .collect(Collectors.toList()),
            odElement.getODAttributeList()
                    .stream()
                    .map(a -> new OD4DataFullPrettyPrinter(new IndentPrinter()).prettyprint(a.getODValue()))
                    .collect(Collectors.toList()),
            odElement.getMCObjectType().printType());
  }

  public void handleLinks(ASTODLink odLink) {
    List<String> leftSides = odLink.getODLinkLeftSide().getReferenceNamesList().stream().map(ASTODName::getName).collect(Collectors.toList());

    List<String> rightSides = odLink.getODLinkRightSide().getReferenceNamesList().stream().map(ASTODName::getName).collect(Collectors.toList());
    
    String rightRoleName = odLink.getODLinkRightSide().isPresentRole() ? odLink.getODLinkRightSide().getRole() : "";
    String leftRoleName = odLink.getODLinkLeftSide().isPresentRole() ? odLink.getODLinkLeftSide().getRole() : "";
    
    if(odLink.getODLinkDirection() instanceof ASTODLeftToRightDir) {
      for(String left: leftSides) {
        for(String right: rightSides) {
          Optional<CDRoleAdapter> cdRole = findRole(left, right, rightRoleName);
          this.linkAttributeList.add(constructLink(left, right, rightRoleName, cdRole));
//          this.linkAttributeList.add(left + ".add" + ((rightRoleName.isEmpty())
//                  ? capFirst(right)
//                  : capFirst(rightRoleName)) + "(" + right + ")");
        }
      }
    }
    if(odLink.getODLinkDirection() instanceof ASTODRightToLeftDir) {
      for(String right: rightSides) {
        for(String left: leftSides) {
          Optional<CDRoleAdapter> cdRole = findRole(right, left, leftRoleName);
          this.linkAttributeList.add(constructLink(right, left, leftRoleName, cdRole));
//          this.linkAttributeList.add(right + ".add" + ((leftRoleName.isEmpty())
//                  ? capFirst(left)
//                  : capFirst(leftRoleName)) + "(" + left + ")");
        }
      }
    }
    if(odLink.getODLinkDirection() instanceof ASTODBiDir || odLink.getODLinkDirection() instanceof ASTODUnspecifiedDir) {
      for(String left: leftSides) {
        for(String right: rightSides) {
          Optional<CDRoleAdapter> cdRoleOfLeft = findRole(left, right, rightRoleName);
          Optional<CDRoleAdapter> cdRoleOfRight = findRole(right, left, leftRoleName);
          
          
          if (cdRoleOfLeft.isPresent() && cdRoleOfRight.isPresent()) {
            // only select one assoc side as the other is added automatically
            // hereby make sure to select the [1] or [+] cardinality side
            LinkCardinality card_l2r = cdRoleOfLeft.get().getCardinality();
            LinkCardinality card_r2l = cdRoleOfRight.get().getCardinality();
            if (card_l2r.equals(LinkCardinality.ONE) || card_l2r.equals(LinkCardinality.PLUS)) {
              this.linkAttributeList.add(constructLink(left, right, rightRoleName, cdRoleOfLeft));
            } else if (card_r2l.equals(LinkCardinality.ONE) || card_r2l.equals(LinkCardinality.PLUS)) {
              this.linkAttributeList.add(constructLink(right, left, leftRoleName, cdRoleOfRight));
            } else {
              // otherwise, we can choose arbitrarily, but only one side
              this.linkAttributeList.add(constructLink(left, right, rightRoleName, cdRoleOfLeft));
            }
          }
          // in case of direction disruption, we choose how it is possible to navigate
          else if (cdRoleOfLeft.isPresent() && !cdRoleOfRight.isPresent()) {
            this.linkAttributeList.add(constructLink(left, right, rightRoleName, cdRoleOfLeft));
          } else if (!cdRoleOfLeft.isPresent() && cdRoleOfRight.isPresent()) {
            this.linkAttributeList.add(constructLink(right, left, leftRoleName, cdRoleOfRight));
          }
          
//          this.linkAttributeList.add(left + ".add" + ((rightRoleName.isEmpty())
//                  ? capFirst(right)
//                  : capFirst(rightRoleName)) + "(" + right + ")");
        }
      }
//      for(String right: rightSides) {
//        for(String left: leftSides) {
//          this.linkAttributeList.add(right + ".add" + ((leftRoleName.isEmpty())
//                  ? capFirst(left)
//                  : capFirst(leftRoleName)) + "(" + left + ")");
//        }
//      }
    }
  }

  @Override
  public void visit(ASTODElement odElement) {
    if(odElement instanceof ASTODNamedObject) {
      createInstantiator((ASTODNamedObject) odElement);
      createChecker((ASTODNamedObject) odElement);
    }
    if(odElement instanceof ASTODLink) {
      handleLinks((ASTODLink) odElement);
    }
  }

  protected String capFirst(String s) {
    return s.substring(0, 1).toUpperCase() + s.substring(1);
  }
  
  protected String uncapFirst(String s) {
    return s.substring(0, 1).toLowerCase() + s.substring(1);
  }
  
  protected Optional<CDRoleAdapter> findRole(String src, String tgt, String roleName) {
    Optional<CDRoleAdapter> res = Optional.empty();
    
    // find object in OD
    Optional<ASTODNamedObject> objSym = diagram.getODElementList().stream()
        .filter(ASTODNamedObject.class::isInstance)
        .map(ASTODNamedObject.class::cast)
        .filter(o -> o.getName().equals(src))
        .findFirst();
  
    if (objSym.isPresent()) {
      // find type of object
      String typeName = objSym.get().getMCObjectType().printType();
      TypeSymbol type = OD4DevelopmentMill.globalScope().resolveType(typeName).get();
      
      // find hidden cdRole in resolved type
      String roleName2resolve = roleName.isEmpty() ? tgt : roleName;
      List<FieldSymbol> types = ((IODLinkScope) type.getSpannedScope()).resolveFieldDownMany(roleName2resolve);
      if (types.size() > 0 && types.get(0) instanceof CDRoleAdapter) {
        res = Optional.of((CDRoleAdapter) types.get(0));
      }
    }
  
    return res;
  }
  
  protected String findObjectAccessorName4Role(String tgt) {
    // find object in OD
    Optional<ASTODNamedObject> obj = diagram.getODElementList().stream()
        .filter(ASTODNamedObject.class::isInstance)
        .map(ASTODNamedObject.class::cast)
        .filter(o -> o.getName().equals(tgt))
        .findFirst();
    
    if (obj.isPresent()) {
      return uncapFirst(obj.get().getMCObjectType().printType());
    }
    return tgt;
  }
  
  protected String constructLink(String src, String tgt, String roleName, Optional<CDRoleAdapter> cdRole) {
    
    String cardModifier = "";
    if (cdRole.isPresent()) {
      cardModifier = cardModifier(cdRole.get());
    }
    
    if (OD4DevelopmentMill.globalScope().getSubScopes().size() >= 1) {
      return src + "." + ((roleName.isEmpty())
          ? findObjectAccessorName4Role(tgt) + cardModifier
          : roleName + cardModifier) + "(" + tgt + ")";
    } else {
      cardModifier = "set";
      return src + "." + cardModifier + ((roleName.isEmpty())
          ? capFirst(tgt)
          : capFirst(roleName)) + "(" + tgt + ")";
    }
  }
  
  protected String cardModifier(CDRoleAdapter cdRole) {
    String cardModifier = "";
    if (OD4DevelopmentMill.globalScope().getSubScopes().size() >= 1) {
      switch (cdRole.getCardinality()) {
        case ONE:
          cardModifier = "";
          break;
        case OPTIONAL:
          cardModifier = "";
          break;
        case STAR:
          cardModifier = "Add";
          break;
        case PLUS:
          cardModifier = "Add";
          break;
      }
    } else {
      switch (cdRole.getCardinality()) {
        case ONE:
          cardModifier = "set";
          break;
        case OPTIONAL:
          cardModifier = "set";
          break;
        case STAR:
          cardModifier = "add";
          break;
        case PLUS:
          cardModifier = "add";
          break;
      }
    }
    return cardModifier;
  }
  
  public OD2CDObjectVisitor(GlobalExtensionManagement glex) {
    this.cd4C = CD4C.getInstance();
    this.glex = glex;
    this.cp = (CompositionPrinter) glex.getGlobalVar("cp");
  }

  public ASTCDCompilationUnit getCDCompilationUnit() {
    return this.cdCompilationUnit;
  }

  public ASTCDClass getInstantiatorClass() {
    return this.instantiatorClass;
  }

  public ASTCDClass getCheckerClass() {
    return this.checkerClass;
  }

  public Map<String, ASTCDClass> getObjectToClassMap() {
    return this.objectToClassMap;
  }
  
}
