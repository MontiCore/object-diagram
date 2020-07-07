// (c) https://github.com/MontiCore/monticore

package de.monticore.dateliterals;

import de.monticore.dateliterals._ast.ASTDatePartDot;
import de.monticore.dateliterals._ast.ASTDatePartHyphen;
import de.monticore.dateliterals._ast.ASTDatePartSlash;
import de.monticore.dateliterals._ast.ASTTimePartColon;
import de.monticore.dateliterals.prettyprinter.DateLiteralsPrettyPrinter;
import de.monticore.od4report._ast.ASTODDate;
import de.monticore.od4report._parser.OD4ReportParser;
import de.monticore.odbasics._ast.ASTODArtifact;
import de.monticore.odbasics._ast.ASTODObject;
import de.monticore.prettyprint.IndentPrinter;
import de.se_rwth.commons.logging.Slf4jLog;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.Assert.*;

public class DateLiteralsTest {

  private final Path SIMPLEDATE = Paths
      .get("src", "test", "resources", "examples", "date", "SimpleDate.od");

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

    assertEquals(2, astodArtifact.get().getObjectDiagram().getODObjectList().size());
    Optional<ASTODObject> object = astodArtifact.get().getObjectDiagram().getODObjectList().stream()
        .filter(o -> "myObject2".equals(o.getName())).findFirst();
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
    DateLiteralsPrettyPrinter dateLiteralsPrettyPrinter;
    Optional<ASTODDate> astodDate;

    OD4ReportParser od4ReportParser = new OD4ReportParser();

    // hyphen
    String dateHyphen = "2017-12-20 15:18:12";
    astodDate = od4ReportParser.parse_StringODDate(dateHyphen);
    assertTrue(astodDate.isPresent());
    assertTrue(astodDate.get().getDate().getDatePart() instanceof ASTDatePartHyphen);
    assertTrue(astodDate.get().getDate().getTimePart() instanceof ASTTimePartColon);

    dateLiteralsPrettyPrinter = new DateLiteralsPrettyPrinter(new IndentPrinter());
    astodDate.get().getDate().accept(dateLiteralsPrettyPrinter);
    assertEquals(dateHyphen, dateLiteralsPrettyPrinter.getPrinter().getContent());

    // slash
    String dateSlash = "2017/12/20 15:18:12";
    astodDate = od4ReportParser.parse_StringODDate(dateSlash);
    assertTrue(astodDate.isPresent());
    assertTrue(astodDate.get().getDate().getDatePart() instanceof ASTDatePartSlash);
    assertTrue(astodDate.get().getDate().getTimePart() instanceof ASTTimePartColon);

    dateLiteralsPrettyPrinter = new DateLiteralsPrettyPrinter(new IndentPrinter());
    astodDate.get().getDate().accept(dateLiteralsPrettyPrinter);
    assertEquals(dateSlash, dateLiteralsPrettyPrinter.getPrinter().getContent());

    // dot
    String dateDot = "2017.12.20 15:18:12";
    astodDate = od4ReportParser.parse_StringODDate(dateDot);
    assertTrue(astodDate.isPresent());
    assertTrue(astodDate.get().getDate().getDatePart() instanceof ASTDatePartDot);
    assertTrue(astodDate.get().getDate().getTimePart() instanceof ASTTimePartColon);

    dateLiteralsPrettyPrinter = new DateLiteralsPrettyPrinter(new IndentPrinter());
    astodDate.get().getDate().accept(dateLiteralsPrettyPrinter);
    assertEquals(dateDot, dateLiteralsPrettyPrinter.getPrinter().getContent());
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

}
