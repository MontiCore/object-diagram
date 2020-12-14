// (c) https://github.com/MontiCore/monticore

package de.monticore.dateliterals;

import de.monticore.dateliterals._ast.*;
import de.monticore.dateliterals.prettyprinter.DateLiteralsFullPrettyPrinter;
import de.monticore.od4report._ast.ASTODDate;
import de.monticore.od4report._parser.OD4ReportParser;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.odbasis._ast.ASTODObject;
import de.monticore.prettyprint.IndentPrinter;
import de.se_rwth.commons.logging.Slf4jLog;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.Assert.*;

public class DateLiteralsTest {

  private final Path SIMPLEDATE = Paths.get("src", "test", "resources", "examples", "date",
      "SimpleDate.od");

  @Before
  public void setup() {
    Slf4jLog.init();
    Slf4jLog.enableFailQuick(false);
  }

  @Test
  public void testSimpleDate() throws IOException {
    OD4ReportParser od4ReportParser = new OD4ReportParser();

    Optional<ASTODArtifact> astodArtifact = od4ReportParser.parseODArtifact(SIMPLEDATE.toString());
    assertTrue(astodArtifact.isPresent());

    assertEquals(2, astodArtifact.get().getObjectDiagram().getODElementList().size());
    Optional<ASTODObject> object = astodArtifact.get()
        .getObjectDiagram()
        .getODElementList()
        .stream()
        .filter(elem -> elem instanceof ASTODObject)
        .map(elem -> (ASTODObject) elem)
        .filter(o -> "myObject2".equals(o.getName()))
        .findFirst();
    assertTrue(object.isPresent());
    assertEquals(3, object.get().getODAttributeList().size());

    object.get().getODAttributeList().forEach(attr -> {
      assertTrue(attr.isPresentODValue());
      assertTrue(attr.getODValue() instanceof ASTODDate);
      assertNotNull(attr.getODValue());
    });
  }

  @Test
  public void testPrettyPrint() throws IOException {
    DateLiteralsFullPrettyPrinter dateLiteralsPrettyPrinter;
    Optional<ASTODDate> astodDate;

    OD4ReportParser od4ReportParser = new OD4ReportParser();

    // hyphen
    String dateHyphen = "2017-12-20 15:18:12";
    astodDate = od4ReportParser.parse_StringODDate(dateHyphen);
    assertTrue(astodDate.isPresent());
    assertTrue(astodDate.get().getDate().getDatePart() instanceof ASTDatePartHyphen);
    assertTrue(astodDate.get().getDate().getTimePart() instanceof ASTTimePartColon);

    dateLiteralsPrettyPrinter = new DateLiteralsFullPrettyPrinter(new IndentPrinter());
    String prettyprint = dateLiteralsPrettyPrinter.prettyprint(astodDate.get().getDate());
    assertEquals(dateHyphen, prettyprint);

    // slash
    String dateSlash = "2017/12/20 15:18:12";
    astodDate = od4ReportParser.parse_StringODDate(dateSlash);
    assertTrue(astodDate.isPresent());
    assertTrue(astodDate.get().getDate().getDatePart() instanceof ASTDatePartSlash);
    assertTrue(astodDate.get().getDate().getTimePart() instanceof ASTTimePartColon);

    dateLiteralsPrettyPrinter = new DateLiteralsFullPrettyPrinter(new IndentPrinter());
    String prettyprint1 = dateLiteralsPrettyPrinter.prettyprint(astodDate.get().getDate());
    assertEquals(dateSlash, prettyprint1);

    // dot
    String dateDot = "2017.12.20 15:18:12";
    astodDate = od4ReportParser.parse_StringODDate(dateDot);
    assertTrue(astodDate.isPresent());
    assertTrue(astodDate.get().getDate().getDatePart() instanceof ASTDatePartDot);
    assertTrue(astodDate.get().getDate().getTimePart() instanceof ASTTimePartColon);

    dateLiteralsPrettyPrinter = new DateLiteralsFullPrettyPrinter(new IndentPrinter());
    String prettyprint2 = dateLiteralsPrettyPrinter.prettyprint(astodDate.get().getDate());
    assertEquals(dateDot, prettyprint2);
  }

  @Test
  public void testDatePart() throws IOException {
    OD4ReportParser od4ReportParser = new OD4ReportParser();

    String dateString = "2017.12.20 15:18:12";
    Optional<ASTODDate> astodDate = od4ReportParser.parse_StringODDate(dateString);
    assertTrue(astodDate.isPresent());

    assertEquals(2017, astodDate.get().getDate().getDatePart().getYear().getValue());
    assertEquals(12, astodDate.get().getDate().getDatePart().getMonth().getValue());
    assertEquals(20, astodDate.get().getDate().getDatePart().getDay().getValue());
  }

  @Test
  public void testTimePart() throws IOException {
    OD4ReportParser od4ReportParser = new OD4ReportParser();

    String dateString = "2017.12.20 15:18:12";
    Optional<ASTODDate> astodDate = od4ReportParser.parse_StringODDate(dateString);
    assertTrue(astodDate.isPresent());

    assertEquals(15, astodDate.get().getDate().getTimePart().getHour().getValue());
    assertEquals(18, astodDate.get().getDate().getTimePart().getMinute().getValue());
    assertEquals(12, astodDate.get().getDate().getTimePart().getSecond().getValue());
  }

  @Test
  public void testLocalDateTime() throws IOException {
    OD4ReportParser od4ReportParser = new OD4ReportParser();

    String dateString = "2017.12.20 15:18:12";
    Optional<ASTODDate> astodDate = od4ReportParser.parse_StringODDate(dateString);
    assertTrue(astodDate.isPresent());

    LocalDateTime localDateTime = astodDate.get().getDate().toLocalDateTime();
    assertEquals(2017, localDateTime.getYear());
    assertEquals(12, localDateTime.getMonthValue());
    assertEquals(20, localDateTime.getDayOfMonth());
    assertEquals(15, localDateTime.getHour());
    assertEquals(18, localDateTime.getMinute());
    assertEquals(12, localDateTime.getSecond());

  }

  @Test
  public void testSetLocalDateTime() {
    LocalDateTime localDateTime = LocalDateTime.now();
    ASTDate astDate = new ASTDate();

    astDate.setDate(localDateTime);
    assertEquals(localDateTime.getYear(), astDate.getDatePart().getYear().getValue());
    assertEquals(localDateTime.getMonthValue(), astDate.getDatePart().getMonth().getValue());
    assertEquals(localDateTime.getDayOfMonth(), astDate.getDatePart().getDay().getValue());
    assertEquals(localDateTime.getHour(), astDate.getTimePart().getHour().getValue());
    assertEquals(localDateTime.getMinute(), astDate.getTimePart().getMinute().getValue());
    assertEquals(localDateTime.getSecond(), astDate.getTimePart().getSecond().getValue());

    assertTrue(astDate.getDatePart() instanceof ASTDatePartHyphen
        && astDate.getTimePart() instanceof ASTTimePartColon);
  }

  @Test
  public void testLeadingZeroes() throws IOException {
    ASTDate date = DateLiteralsMill.dateBuilder()
        .setDatePart(DateLiteralsMill.datePartSlashBuilder()
            .setYear(DateLiteralsMill.natLiteralBuilder().setDigits("2020").build())
            .setMonth(DateLiteralsMill.natLiteralBuilder().setDigits("12").build())
            .setDay(DateLiteralsMill.natLiteralBuilder().setDigits("14").build())
            .build())
        .setTimePart(DateLiteralsMill.timePartColonBuilder()
            .setHour(DateLiteralsMill.natLiteralBuilder().setDigits("15").build())
            .setMinute(DateLiteralsMill.natLiteralBuilder().setDigits("00").build())
            .setSecond(DateLiteralsMill.natLiteralBuilder().setDigits("00").build())
            .build()).build();

    String prettyPrint = new DateLiteralsFullPrettyPrinter(new IndentPrinter()).prettyprint(date);
    Optional<ASTDate> ppDate = new OD4ReportParser().parse_StringDate(prettyPrint);
    assertTrue(ppDate.isPresent());
    assertEquals("00", ppDate.get().getTimePart().getMinute().getDigits());
    assertEquals("00", ppDate.get().getTimePart().getSecond().getDigits());
  }

}
