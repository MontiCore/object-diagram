/* (c) https://github.com/MontiCore/monticore */
package de.monticore;

import com.google.common.base.Joiner;
import de.se_rwth.commons.logging.Finding;
import de.se_rwth.commons.logging.Log;
import de.se_rwth.commons.logging.LogStub;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public abstract class ODTestBasis {
  
  public static final Path PATH = Paths.get("src", "test", "resources");
  
  /** have a temporary folder for the tests */
  
  @TempDir
  public File folder;
  
  @BeforeEach
  public void initLog() {
    LogStub.init();
    Log.enableFailQuick(false);
  }
  
  @AfterEach
  public void checkLog() {
    checkLogError();
  }
  
  public String getTmpAbsolutePath() {
    return folder.getAbsolutePath();
  }
  
  public String getTmpFilePath(String fileName) {
    return getTmpAbsolutePath() + File.separator + fileName;
  }
  
  protected boolean modelFileExists(String fileName) {
    Path filePath = Paths.get(fileName);
    return Files.exists(filePath);
  }
  
  public static String getFilePath(String path) {
    return Paths.get(PATH + path).toString();
  }
  
  public static String getJoinedErrors() {
    return Joiner.on("\n").join(Log.getFindings());
  }
  
  public static void checkLogError() {
    if (Log.getErrorCount() > 0) {
      final String joinedErrors = getJoinedErrors();
      Log.getFindings().clear();
      fail("Following errors occurred: \n" + joinedErrors);
    }
  }
  
  public static void expectErrorCount(int i, List<String> listOfErrors) {
    if (Log.getErrorCount() == 0) {
      if (i == 0) {
        return;
      }
      else {
        fail("expected " + i + " errors, but none were present");
      }
    }
    
    assertEquals(Log.getErrorCount(), i,
        "expected to get exact " + i + " errors, the errors where:\n" + getJoinedErrors());
    final List<Finding> findings = Log.getFindings();
    IntStream.range(0, i)
        .forEach(c -> assertEquals(listOfErrors.get(c), findings.get(c).toString()));
    Log.getFindings().clear();
  }
  
  protected void assertContains(String haystack, String needle) {
    this.assertContains("%1$s did not contain `%2$s`", haystack, needle);
  }
  
  protected void assertNotContains(String haystack, String needle) {
    this.assertNotContains("%1$s did contain `%2$s`", haystack, needle);
  }
  
  protected void assertContains(String message, String haystack, String needle) {
    if (!haystack.contains(needle)) {
      fail(String.format(message, haystack, needle));
    }
  }
  
  protected void assertNotContains(String message, String haystack, String needle) {
    if (haystack.contains(needle)) {
      fail(String.format(message, haystack, needle));
    }
  }
}
