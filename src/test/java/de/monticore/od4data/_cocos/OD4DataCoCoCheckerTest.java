// (c) https://github.com/MontiCore/monticore

package de.monticore.od4data._cocos;

import de.monticore.io.paths.MCPath;
import de.monticore.od4data.OD4DataMill;
import de.monticore.od4data._parser.OD4DataParser;
import de.monticore.od4data._symboltable.IOD4DataGlobalScope;
import de.monticore.od4data._symboltable.OD4DataScopesGenitorDelegator;
import de.monticore.odbasis._ast.ASTODArtifact;
import de.monticore.odbasis._cocos.attributes.PartialAndCompleteAttributesCoCo;
import de.monticore.odbasis._cocos.names.UniqueObjectNamesCoCo;
import de.monticore.odbasis._cocos.object.ValidObjectReferenceCoCo;
import de.monticore.odlink._cocos.link.LinkEndConsistencyCoCo;
import de.monticore.odlink._cocos.link.ValidLinkReferenceCoCo;
import de.monticore.symbols.basicsymbols._symboltable.TypeSymbol;
import de.se_rwth.commons.logging.Log;
import de.se_rwth.commons.logging.LogStub;
import de.se_rwth.commons.logging.ODLogReset;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.Assert.*;

public class OD4DataCoCoCheckerTest {

  private MCPath symbolPath = new MCPath(Paths.get("src", "test", "resources", "examples"));

  private Path cocoExamples;

  private OD4DataCoCoChecker odCoCoChecker;

  private PrintStream originalOut;

  private PrintStream originalErr;

  private ByteArrayOutputStream out;

  private ByteArrayOutputStream err;

  @Before
  public void setup() {
    LogStub.init();
    Log.enableFailQuick(false);
    ODLogReset.resetFindings();
    odCoCoChecker = new OD4DataCoCoChecker();
    cocoExamples = Paths.get("src/test/resources/cocos");

    OD4DataMill.reset();
    OD4DataMill.init();
    OD4DataMill.globalScope().clear();
    IOD4DataGlobalScope gs = OD4DataMill.globalScope();
    OD4DataMill.globalScope().setSymbolPath(symbolPath);

    TypeSymbol objectType = OD4DataMill.typeSymbolBuilder().setName("ObjectType").setEnclosingScope(gs).setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol objectType1 = OD4DataMill.typeSymbolBuilder().setName("ObjectType1").setEnclosingScope(gs).setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol objectType2 = OD4DataMill.typeSymbolBuilder().setName("ObjectType2").setEnclosingScope(gs).setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol objectType3 = OD4DataMill.typeSymbolBuilder().setName("ObjectType3").setEnclosingScope(gs).setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol objectType4 = OD4DataMill.typeSymbolBuilder().setName("ObjectType4").setEnclosingScope(gs).setSpannedScope(OD4DataMill.scope()).build();
    TypeSymbol objectType5 = OD4DataMill.typeSymbolBuilder().setName("ObjectType5").setEnclosingScope(gs).setSpannedScope(OD4DataMill.scope()).build();
    gs.add(objectType);
    gs.add(objectType1);
    gs.add(objectType2);
    gs.add(objectType3);
    gs.add(objectType4);
    gs.add(objectType5);
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

  private Optional<ASTODArtifact> createASTandSTFromFile(String path) {
    Optional<ASTODArtifact> artifact = Optional.empty();

    try {
      OD4DataParser parser = new OD4DataParser();
      artifact = parser.parseODArtifact(path);
      assertFalse(parser.hasErrors());
      assertTrue(artifact.isPresent());
    }
    catch (IOException e) {
      Log.error("Cannot parse model: " + path);
    }

    OD4DataScopesGenitorDelegator od4DataScopesGenitorDelegator =
        OD4DataMill.scopesGenitorDelegator();

    artifact.ifPresent(od4DataScopesGenitorDelegator::createFromAST);

    return artifact;
  }

  @Test
  public void checkUniqueObjectNamesCoCo() {
    Optional<ASTODArtifact> odASTODArtifact = createASTandSTFromFile(
        Paths.get(cocoExamples.toString(), "NoUniqueNames.od").toString());

    if (odASTODArtifact.isPresent()) {
      odCoCoChecker.addCoCo(new UniqueObjectNamesCoCo());
      odCoCoChecker.checkAll(odASTODArtifact.get());
      assertEquals(2, Log.getErrorCount());
    }
  }

  @Test
  public void checkAnonymusObjectsValid() {
    Optional<ASTODArtifact> odASTODArtifact = createASTandSTFromFile(
        Paths.get(cocoExamples.toString(), "AnonymusObject.od").toString());

    if (odASTODArtifact.isPresent()) {
      odCoCoChecker.addCoCo(new UniqueObjectNamesCoCo());
      odCoCoChecker.checkAll(odASTODArtifact.get());
      assertEquals(0, Log.getErrorCount());
    }
  }

  @Test
  public void checkValidReferenceCoCo() {
    Optional<ASTODArtifact> odASTODArtifact = createASTandSTFromFile(
        Paths.get(cocoExamples.toString(), "InvalidLinkReference.od").toString());

    if (odASTODArtifact.isPresent()) {
      odCoCoChecker.addCoCo(new ValidLinkReferenceCoCo());
      odCoCoChecker.checkAll(odASTODArtifact.get());
      assertEquals(1, Log.getErrorCount());
    }
  }

  @Test
  public void checkObjectReferenceCoCo() {
    Optional<ASTODArtifact> odASTODArtifact = createASTandSTFromFile(
        Paths.get(cocoExamples.toString(), "InvalidObjectReference.od").toString());

    if (odASTODArtifact.isPresent()) {
      odCoCoChecker.addCoCo(new ValidObjectReferenceCoCo());
      odCoCoChecker.checkAll(odASTODArtifact.get());
      assertEquals(1, Log.getErrorCount());
    }
  }

  @Test
  public void checkPartialAndCompleteAttributesCoCo() {
    Optional<ASTODArtifact> odASTODArtifact = createASTandSTFromFile(
        Paths.get(cocoExamples.toString(), "PartialAndCompleteAttributes.od").toString());

    if (odASTODArtifact.isPresent()) {
      odCoCoChecker.addCoCo(new PartialAndCompleteAttributesCoCo());
      odCoCoChecker.checkAll(odASTODArtifact.get());
      assertEquals(3, Log.getErrorCount());
    }
  }

  @Test
  public void checkLinkEndConsistencyCoCo() {
    Optional<ASTODArtifact> odASTODArtifact = createASTandSTFromFile(
        Paths.get(cocoExamples.toString(), "InvalidLinkEndConsistency.od").toString());

    if (odASTODArtifact.isPresent()) {
      odCoCoChecker.addCoCo(new LinkEndConsistencyCoCo());
      odCoCoChecker.checkAll(odASTODArtifact.get());
      assertEquals(2, Log.getErrorCount());
    }
  }

}
