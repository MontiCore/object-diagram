// (c) https://github.com/MontiCore/monticore

package de.monticore.lang.dateliterals.cocos;

import de.monticore.lang.dateliterals._cocos.DateLiteralsCoCoChecker;
import de.monticore.lang.dateliterals.cocos.date.DateConcistencyCoCo;

public class DateLiteralsCoCos {

  public DateLiteralsCoCoChecker getCheckerForAllCoCos() {
    final DateLiteralsCoCoChecker dateLiteralsCoCoChecker = new DateLiteralsCoCoChecker();

    dateLiteralsCoCoChecker.addCoCo(new DateConcistencyCoCo());

    return dateLiteralsCoCoChecker;
  }

}
