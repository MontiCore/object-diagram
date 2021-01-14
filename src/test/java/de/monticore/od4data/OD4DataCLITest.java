// (c) https://github.com/MontiCore/monticore

package de.monticore.od4data;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;

public class OD4DataCLITest {

  private final Path INPUTOD = Paths.get("src", "test", "resources", "examples", "od",
      "SimpleOD2.od");

  private final Path INPUTDIR = Paths.get("src", "resources", "examples", "od");

  private final Path TARGET = Paths.get("target", "cli", "od4data");

  private PrintStream originalOut;

  private PrintStream originalErr;

  private ByteArrayOutputStream out;

  private ByteArrayOutputStream err;

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
  public void testOD4DataCLIHelp() {
    String[] help = { "-h" };
    OD4DataCLI.main(help);
  }

  @Test
  public void testOD4DataCLIPath() {
    String[] input = { "-i", INPUTOD.toString(), "-path",
        Paths.get(INPUTDIR.toString(), "examples").toString(),
        Paths.get(INPUTDIR.toString(), "cocos").toString() };
    OD4DataCLI.main(input);
  }

  @Test
  public void testOD4CLIPrettyPrint() {
    String[] input = { "-i", INPUTOD.toString(), "-pp",
        Paths.get(TARGET.toString(), "pp.od").toString() };
    OD4DataCLI.main(input);
    assertTrue(Paths.get(TARGET.toString(), "pp.od").toFile().exists());
  }

  @Test
  public void testOD4DataStoreST() {
    OD4DataMill.init();
    String[] input = { "-i", INPUTOD.toString(), "-s",
        Paths.get(TARGET.toString(), "MyFamily.odsym").toString() };
    OD4DataCLI.main(input);
    assertTrue(Paths.get(TARGET.toString(), "MyFamily.odsym").toFile().exists());
  }

}
