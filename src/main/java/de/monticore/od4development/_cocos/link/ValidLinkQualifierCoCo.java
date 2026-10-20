/* (c) https://github.com/MontiCore/monticore */

package de.monticore.od4development._cocos.link;

import de.monticore.odattribute._ast.ASTODList;
import de.monticore.odattribute._ast.ASTODMap;
import de.monticore.odbasis._ast.ASTODObject;
import de.monticore.odbasis._ast.ASTODValue;
import de.monticore.odlink._ast.ASTODLinkQualifier;
import de.monticore.odlink._cocos.ODLinkASTODLinkQualifierCoCo;
import de.se_rwth.commons.logging.Log;

/**
 * Checks that the value of an {@link ASTODLinkQualifier} is neither a list, a map, nor an object,
 * e.g., that {@code link a [ [1, 2] ] -> b;}, {@code link a [ [1 -> 2] ] -> b;} and
 * {@code link a [ :A {} ] -> b;} are rejected. References to objects by name, e.g.,
 * {@code link a [c] -> b;}, are allowed.
 * <p>
 * The CoCo is part of OD4Development, as lists and maps are introduced by ODAttribute, which
 * ODLink does not know.
 */
public class ValidLinkQualifierCoCo implements ODLinkASTODLinkQualifierCoCo {

  public static final String ERROR_OBJECT_QUALIFIER =
      "0x0D007: The qualifier of a link must not be an object.";

  public static final String ERROR_LIST_QUALIFIER =
      "0x0D00E: The qualifier of a link must not be a list.";

  public static final String ERROR_MAP_QUALIFIER =
      "0x0D00F: The qualifier of a link must not be a map.";

  @Override
  public void check(ASTODLinkQualifier node) {
    if (!node.isPresentODValue()) {
      return;
    }
    ASTODValue value = node.getODValue();
    if (value instanceof ASTODObject) {
      Log.error(ERROR_OBJECT_QUALIFIER, node.get_SourcePositionStart(),
          node.get_SourcePositionEnd());
    }
    else if (value instanceof ASTODList) {
      Log.error(ERROR_LIST_QUALIFIER, node.get_SourcePositionStart(),
          node.get_SourcePositionEnd());
    }
    else if (value instanceof ASTODMap) {
      Log.error(ERROR_MAP_QUALIFIER, node.get_SourcePositionStart(),
          node.get_SourcePositionEnd());
    }
  }

}
