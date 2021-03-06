// (c) https://github.com/MontiCore/monticore

package de.monticore.od4report;

import de.monticore.od4data.OD4DataMill;
import de.monticore.od4report._symboltable.IOD4ReportGlobalScope;
import de.monticore.symbols.basicsymbols._symboltable.TypeSymbol;
import de.se_rwth.commons.logging.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;

public class OD4ReportCLITest {

  private final Path INPUTOD = Paths.get("src", "test", "resources", "examples", "od",
      "Examples" + ".od");

  private final Path INPUTDIR = Paths.get("src", "resources", "examples", "od");

  private final Path TARGET = Paths.get("target", "cli", "od4Report");

  private PrintStream originalOut;

  private PrintStream originalErr;

  private ByteArrayOutputStream out;

  private ByteArrayOutputStream err;

  @BeforeClass
  public static void disableFailQuick() {
    Log.init();
    Log.enableFailQuick(false);
  }

  @Before
  public void setUp() {
    OD4ReportMill.reset();
    OD4ReportMill.init();
    OD4ReportMill.globalScope().clear();
    IOD4ReportGlobalScope gs = OD4ReportMill.globalScope();

    TypeSymbol rule = OD4ReportMill.typeSymbolBuilder().setName("Rule").setEnclosingScope(gs).setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol actionA = OD4ReportMill.typeSymbolBuilder().setName("ActionA").setEnclosingScope(gs).setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol actionB = OD4ReportMill.typeSymbolBuilder().setName("ActionB").setEnclosingScope(gs).setSpannedScope(OD4DataMill.scope()).build();
    gs.add(rule);
    gs.add(actionA);
    gs.add(actionB);
  }

  @Before
  public void setStreams() {
    // redirect System.out
    originalOut = System.out;
    out = new ByteArrayOutputStream();
    System.setOut(new PrintStream(out));

    //redirect System.err
    originalErr = System.err;
    err = new ByteArrayOutputStream();
    System.setErr(new PrintStream(err));
  }

  @After
  public void restoreSysOut() {
    System.setOut(originalOut);
    System.setErr(originalErr);
  }

  @Test
  public void testOD4ReportCLIHelp() {
    String[] help = { "-h" };
    OD4ReportCLI.main(help);
  }

  @Test
  public void testOD4ReportCLIPath() {
    String[] input = { "-i", INPUTOD.toString(), "-path",
        Paths.get(INPUTDIR.toString(), "examples").toString(),
        Paths.get(INPUTDIR.toString(), "cocos").toString() };
    OD4ReportCLI.main(input);
  }

  @Test
  public void testOD4ReportCLICocosIntra() {
    String[] input = { "-i", INPUTOD.toString(), "-c", "intra" };
    OD4ReportCLI.main(input);
  }

  @Test
  public void testOD4ReportCLIPrettyPrint() {
    String[] input = { "-i", INPUTOD.toString(), "-pp",
        Paths.get(TARGET.toString(), "pp.od").toString() };
    OD4ReportCLI.main(input);
    assertTrue(Paths.get(TARGET.toString(), "pp.od").toFile().exists());
  }

  @Test
  public void testOD4ReportStoreST() {
    OD4ReportMill.init();
    String[] input = { "-i", INPUTOD.toString(), "-s",
        Paths.get(TARGET.toString(), "Examples.odsym").toString() };
    OD4ReportCLI.main(input);
    assertTrue(Paths.get(TARGET.toString(), "Examples.odsym").toFile().exists());
  }

}
