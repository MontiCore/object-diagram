// (c) https://github.com/MontiCore/monticore

package de.monticore.od4data;

import de.monticore.od4data._symboltable.IOD4DataGlobalScope;
import de.monticore.symbols.basicsymbols._symboltable.TypeSymbol;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;

public class OD4DataToolTest {

  private final Path INPUTOD = Paths.get("src", "test", "resources", "examples", "od",
      "SimpleOD2.od");

  private final Path INPUTDIR = Paths.get("src", "resources", "examples", "od");

  private final Path TARGET = Paths.get("target", "cli", "od4data");

  private PrintStream originalOut;

  private PrintStream originalErr;

  private ByteArrayOutputStream out;

  private ByteArrayOutputStream err;

  @Before
  public void setup() {
    OD4DataMill.reset();
    OD4DataMill.init();
    OD4DataMill.globalScope().clear();
    IOD4DataGlobalScope gs = OD4DataMill.globalScope();

    TypeSymbol objectType = OD4DataMill.typeSymbolBuilder().setName("ObjectType").setEnclosingScope(gs).setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol objectType2 = OD4DataMill.typeSymbolBuilder().setName("ObjectType2").setEnclosingScope(gs).setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol t1 = OD4DataMill.typeSymbolBuilder().setName("T1").setEnclosingScope(gs).setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol t2 = OD4DataMill.typeSymbolBuilder().setName("T2").setEnclosingScope(gs).setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol t3 = OD4DataMill.typeSymbolBuilder().setName("T3").setEnclosingScope(gs).setSpannedScope(OD4DataMill.scope()).build();
    gs.add(objectType);
    gs.add(objectType2);
    gs.add(t1);
    gs.add(t2);
    gs.add(t3);
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
  public void testOD4DataToolHelp() {
    String[] help = { "-h" };
    OD4DataTool.main(help);
  }

  @Test
  public void testOD4DataToolPath() {
    String[] input = { "-i", INPUTOD.toString(), "-path",
        Paths.get(INPUTDIR.toString(), "examples").toString(),
        Paths.get(INPUTDIR.toString(), "cocos").toString() };
    OD4DataTool.main(input);
  }

  @Test
  public void testOD4ToolPrettyPrint() {
    String[] input = { "-i", INPUTOD.toString(), "-pp",
        Paths.get(TARGET.toString(), "pp.od").toString() };
    OD4DataTool.main(input);
    assertTrue(Paths.get(TARGET.toString(), "pp.od").toFile().exists());
  }

  @Test
  public void testOD4DataStoreST() {
    OD4DataMill.init();
    String[] input = { "-i", INPUTOD.toString(), "-s",
        Paths.get(TARGET.toString(), "MyFamily.odsym").toString() };
    OD4DataTool.main(input);
    assertTrue(Paths.get(TARGET.toString(), "MyFamily.odsym").toFile().exists());
  }

}
