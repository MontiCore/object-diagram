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
  
  /** Base directory that contains test resources. */
  public static final Path PATH = Paths.get("src", "test", "resources");
  
  /**
   * Temporary directory managed by JUnit for each test run.
   */
  @TempDir
  protected File folder;
  
  /**
   * Returns the absolute path of the temporary test directory.
   *
   * @return absolute path to the JUnit temporary directory
   */
  public String getTmpAbsolutePath() {
    return folder.getAbsolutePath();
  }
  
  /**
   * Resolves a file name against the temporary test directory.
   *
   * @param fileName file name relative to the temporary directory
   * @return absolute path to the resolved file location
   */
  public Path getTmpFilePath(String fileName) {
    return getTmpDirPath().resolve(fileName).toAbsolutePath();
  }
  
  /**
   * Resolves a relative path against the temporary test directory.
   *
   * @param path relative path inside the temporary directory
   * @return absolute path to the resolved file location
   */
  public Path getTmpFilePath(Path path) {
    return getTmpDirPath().resolve(path).toAbsolutePath();
  }
  
  /**
   * Resolves a path built from multiple segments against the temporary test directory.
   *
   * @param first first path segment
   * @param more additional path segments
   * @return absolute path to the resolved file location
   */
  public Path getTmpFilePath(String first, String... more) {
    return getTmpDirPath().resolve(Paths.get(first, more)).toAbsolutePath();
  }
  
  /**
   * Returns the temporary directory as a {@link Path}.
   *
   * @return temporary directory path
   */
  public Path getTmpDirPath() {
    return folder.toPath();
  }
  
  /**
   * Checks whether a model file exists at the given path.
   *
   * @param fileName path of the model file to check
   * @return {@code true} if the file exists, otherwise {@code false}
   */
  protected boolean modelFileExists(String fileName) {
    Path filePath = Paths.get(fileName);
    return Files.exists(filePath);
  }
  
  /**
   * Builds a path inside {@code src/test/resources}.
   *
   * @param path relative path below the test resources directory
   * @return normalized path inside the test resources directory
   */
  public static String getFilePath(String path) {
    // Strip leading separators so resolve() always appends to PATH.
    String normalizedPath = path.replaceFirst("^[\\\\/]+", "");
    return PATH.resolve(normalizedPath).toString();
  }
  
  /**
   * Joins all current log findings into a single newline-separated string.
   *
   * @return joined error and warning findings from the global log
   */
  public static String getJoinedErrors() {
    return Joiner.on("\n").join(Log.getFindings());
  }
  
  /**
   * Fails the test if the global log contains errors and clears the findings afterwards.
   */
  public static void checkLogError() {
    if (Log.getErrorCount() > 0) {
      final String joinedErrors = getJoinedErrors();
      Log.getFindings().clear();
      fail("Following errors occurred: \n" + joinedErrors);
    }
  }
  
  /**
   * Asserts that {@code haystack} contains {@code needle} using a default message.
   *
   * @param haystack input string that should contain the expected text
   * @param needle expected substring
   */
  protected void assertContains(String haystack, String needle) {
    this.assertContains("%1$s did not contain `%2$s`", haystack, needle);
  }
  
  /**
   * Asserts that {@code haystack} does not contain {@code needle} using a default message.
   *
   * @param haystack input string that should not contain the text
   * @param needle forbidden substring
   */
  protected void assertNotContains(String haystack, String needle) {
    this.assertNotContains("%1$s did contain `%2$s`", haystack, needle);
  }
  
  /**
   * Fails if {@code haystack} does not contain {@code needle}.
   * Null values are treated as assertion failures.
   *
   * @param message assertion message format with two placeholders (%1$s and %2$s)
   * @param haystack input string that should contain the expected text
   * @param needle expected substring
   */
  protected void assertContains(String message, String haystack, String needle) {
    if (haystack == null || needle == null || !haystack.contains(needle)) {
      fail(String.format(message, haystack, needle));
    }
  }
  
  /**
   * Fails if {@code haystack} contains {@code needle}.
   * Null values are treated as assertion failures.
   *
   * @param message assertion message format with two placeholders (%1$s and %2$s)
   * @param haystack input string that should not contain the text
   * @param needle forbidden substring
   */
  protected void assertNotContains(String message, String haystack, String needle) {
    if (haystack == null || needle == null || haystack.contains(needle)) {
      fail(String.format(message, haystack, needle));
    }
  }
}
