/* (c) https://github.com/MontiCore/monticore */
package de.monticore;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.opentest4j.AssertionFailedError;

import java.nio.file.Path;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link ToolProcessRunner}.
 */
class ToolProcessRunnerTest {

  @TempDir
  Path tmp;

  /** Prints its arguments, one per line, to standard output and the working directory to error. */
  public static class Echo {
    public static void main(String[] args) {
      for (String arg : args) {
        System.out.println(arg);
      }
      System.out.flush();
      System.err.println(System.getProperty("user.dir"));
    }
  }

  /** Prints a line and then hangs. */
  public static class Hang {
    public static void main(String[] args) throws InterruptedException {
      System.out.println("started");
      System.out.flush();
      Thread.sleep(Duration.ofMinutes(5));
    }
  }

  @Test
  void shouldReturnMergedOutputAndUseWorkingDirectory() {
    List<String> out = ToolProcessRunner.run(Echo.class, tmp, "a", "b");

    assertEquals(List.of("a", "b", tmp.toAbsolutePath().toString()), out);
  }

  @Test
  void shouldFailWhenTimeoutIsExceeded() {
    long start = System.nanoTime();

    AssertionFailedError error = assertThrows(AssertionFailedError.class,
        () -> ToolProcessRunner.run(Hang.class, null, Duration.ofSeconds(2)));

    Duration elapsed = Duration.ofNanos(System.nanoTime() - start);
    assertTrue(elapsed.compareTo(Duration.ofSeconds(30)) < 0, "took " + elapsed);
    assertTrue(error.getMessage().contains("did not finish within 2s"), error.getMessage());
    assertTrue(error.getMessage().contains("started"), error.getMessage());
  }
}
