// (c) https://github.com/MontiCore/monticore

package de.monticore.dateliterals._ast;

import de.monticore.dateliterals.DateLiteralsMill;
import de.monticore.literals.mccommonliterals.MCCommonLiteralsMill;

import java.time.LocalDateTime;

public class ASTDate extends ASTDateTOP {

  /**
   * Sets {@link ASTDatePart} and {@link ASTTimePart} of an {@link ASTDate} to the corresponding
   * values of a {@link LocalDateTime} object.
   *
   * @return An {@link ASTDate} object with consisting of an {@link ASTDatePartHyphen} and {@link
   * ASTTimePartColon} object.
   */
  public LocalDateTime toLocalDateTime() {
    return LocalDateTime.of(getDatePart().getYear().getValue(), getDatePart().getMonth().getValue(),
        getDatePart().getDay().getValue(), getTimePart().getHour().getValue(),
        getTimePart().getMinute().getValue(), getTimePart().getSecond().getValue());
  }

  /**
   * Sets the date provided by a {@link LocalDateTime} object using {@link ASTDatePartHyphen} and
   * {@link ASTTimePartColon}.
   *
   * @param localDateTime Date providing the time information.
   */
  public void setDate(LocalDateTime localDateTime) {
    ASTDatePartHyphen datePartHyphen = DateLiteralsMill.datePartHyphenBuilder()
        .setYear(MCCommonLiteralsMill.natLiteralBuilder()
            .setDigits(String.valueOf(localDateTime.getYear()))
            .build())
        .setMonth(MCCommonLiteralsMill.natLiteralBuilder()
            .setDigits(String.valueOf(localDateTime.getMonthValue()))
            .build())
        .setDay(MCCommonLiteralsMill.natLiteralBuilder()
            .setDigits(String.valueOf(localDateTime.getDayOfMonth()))
            .build())
        .build();

    ASTTimePartColon timePartColon = DateLiteralsMill.timePartColonBuilder()
        .setHour(MCCommonLiteralsMill.natLiteralBuilder()
            .setDigits(String.valueOf(localDateTime.getHour()))
            .build())
        .setMinute(MCCommonLiteralsMill.natLiteralBuilder()
            .setDigits(String.valueOf(localDateTime.getMinute()))
            .build())
        .setSecond(MCCommonLiteralsMill.natLiteralBuilder()
            .setDigits(String.valueOf(localDateTime.getSecond()))
            .build())
        .build();

    setDatePart(datePartHyphen);
    setTimePart(timePartColon);
  }

  /**
   * Returns the year as an integer.
   *
   * @return year as int value
   */
  public int getYear() {
    return Integer.valueOf(getDatePart().getYear().getDigits()).intValue();
  }

  /**
   * Returns the month as an integer.
   *
   * @return month as int value
   */
  public int getMonth() {
    return Integer.valueOf(getDatePart().getMonth().getDigits()).intValue();
  }

  /**
   * Returns the day as an integer.
   *
   * @return day as int value
   */
  public int getDay() {
    return Integer.valueOf(getDatePart().getDay().getDigits()).intValue();
  }

  /**
   * Returns the hours as an integer.
   *
   * @return hours as int value
   */
  public int getHour() {
    return Integer.valueOf(getTimePart().getHour().getDigits()).intValue();
  }

  /**
   * Returns the minutes as an integer.
   *
   * @return minutes as int value
   */
  public int getMinute() {
    return Integer.valueOf(getTimePart().getMinute().getDigits()).intValue();
  }

  /**
   * Returns the seconds as an integer.
   *
   * @return seconds as int value
   */
  public int getSecond() {
    return Integer.valueOf(getTimePart().getSecond().getDigits()).intValue();
  }

}
