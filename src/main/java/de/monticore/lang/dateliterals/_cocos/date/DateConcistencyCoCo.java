// (c) https://github.com/MontiCore/monticore

package de.monticore.lang.dateliterals._cocos.date;

import de.monticore.lang.dateliterals._ast.ASTDate;
import de.monticore.lang.dateliterals._cocos.DateLiteralsASTDateCoCo;
import de.se_rwth.commons.logging.Log;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Check if the ODDate is a consistent Date. Consistency is checked using {@link LocalDate}.
 */
public class DateConcistencyCoCo implements DateLiteralsASTDateCoCo {

  @Override
  public void check(ASTDate node) {
    try {
      LocalDateTime
          .of(node.getYear().getValue(), node.getMonth().getValue(), node.getDay().getValue(),
              node.getHour().getValue(), node.getMinute().getValue(), node.getSecond().getValue());
    }
    catch (DateTimeException e) {
      Log.error("Violation of CoCo 'DateConcistencyCoCo'", node.get_SourcePositionStart());
    }
  }

}
