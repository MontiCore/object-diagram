/* (c) https://github.com/MontiCore/monticore */
package de.monticore;

import com.google.common.base.Joiner;
import de.se_rwth.commons.logging.Log;

import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.fail;

public abstract class ODTestBasis {
  
  public static final Path PATH = Paths.get("src", "test", "resources");
  
  /** have a temporary folder for the tests */
  
  @TempDir
  public File folder;
  
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
