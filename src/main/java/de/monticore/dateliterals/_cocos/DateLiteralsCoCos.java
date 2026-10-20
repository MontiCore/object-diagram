// (c) https://github.com/MontiCore/monticore

package de.monticore.dateliterals._cocos;

import de.monticore.dateliterals._cocos.date.DateConsistencyCoCo;

public class DateLiteralsCoCos {

  public DateLiteralsCoCoChecker getCheckerForAllCoCos() {
    final DateLiteralsCoCoChecker dateLiteralsCoCoChecker = new DateLiteralsCoCoChecker();

    dateLiteralsCoCoChecker.addCoCo(new DateConsistencyCoCo());

    return dateLiteralsCoCoChecker;
  }

}
