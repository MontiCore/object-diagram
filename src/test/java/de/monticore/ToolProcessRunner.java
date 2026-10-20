/* (c) https://github.com/MontiCore/monticore */
package de.monticore;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Runs a tool in a separate JVM process. This isolates the test JVM from {@code System.exit(...)}
 * calls inside the tool.
 * <p>
 * The output is redirected to a temporary file instead of being read while the process runs, so
 * that a hanging tool cannot block the test and the timeout takes effect.
 */
public final class ToolProcessRunner {

  public static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30);

  private ToolProcessRunner() {
  }

  /**
   * Runs the main class with the default timeout.
   *
   * @param workingDir working directory of the process, {@code null} for the current one
   * @return the merged standard and error output, line by line
   */
  public static List<String> run(Class<?> mainClass, Path workingDir, String... args) {
    return run(mainClass, workingDir, DEFAULT_TIMEOUT, args);
  }

  /**
   * Runs the main class and fails the test if the process does not finish within the timeout.
   *
   * @param workingDir working directory of the process, {@code null} for the current one
   * @return the merged standard and error output, line by line
   */
  public static List<String> run(Class<?> mainClass, Path workingDir, Duration timeout,
      String... args) {
    List<String> command = new ArrayList<>();
    command.add(Paths.get(System.getProperty("java.home"), "bin", "java").toString());
    command.add("-cp");
    command.add(System.getProperty("java.class.path"));
    command.add(mainClass.getName());
    command.addAll(List.of(args));

    Path output = null;
    try {
      output = Files.createTempFile("tool-output", ".txt");
      ProcessBuilder processBuilder = new ProcessBuilder(command)
          .redirectErrorStream(true)
          .redirectOutput(output.toFile());
      if (workingDir != null) {
        processBuilder.directory(workingDir.toFile());
      }

      Process process = processBuilder.start();
      if (!process.waitFor(timeout.toMillis(), TimeUnit.MILLISECONDS)) {
        process.destroyForcibly();
        // wait for the process to end, so that it releases the output file
        process.waitFor(5, TimeUnit.SECONDS);
        fail("Tool process did not finish within " + timeout.toSeconds() + "s. Output so far:\n"
            + Files.readString(output, UTF_8));
      }
      return Files.readAllLines(output, UTF_8);
    }
    catch (IOException e) {
      return fail("Running tool in separate process failed: " + e.getMessage(), e);
    }
    catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      return fail("Interrupted while waiting for the tool process", e);
    }
    finally {
      deleteQuietly(output);
    }
  }

  private static void deleteQuietly(Path file) {
    if (file == null) {
      return;
    }
    try {
      Files.deleteIfExists(file);
    }
    catch (IOException e) {
      // a leftover temporary file does not affect the test result
    }
  }
}
