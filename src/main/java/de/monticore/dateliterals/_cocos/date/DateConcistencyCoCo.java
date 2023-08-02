// (c) https://github.com/MontiCore/monticore

// (c) https://github.com/MontiCore/monticore

package de.monticore.dateliterals._cocos.date;

import de.monticore.dateliterals._ast.ASTDate;
import de.monticore.dateliterals._cocos.DateLiteralsASTDateCoCo;
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
          .of(node.getDatePart().getYear().getValue(), node.getDatePart().getMonth().getValue(),
              node.getDatePart().getDay().getValue(), node.getTimePart().getHour().getValue(),
              node.getTimePart().getMinute().getValue(), node.getTimePart().getSecond().getValue());
    }
    catch (DateTimeException e) {
      Log.error("Violation of CoCo 'DateConcistencyCoCo'", node.get_SourcePositionStart());
    }
  }

}
