/* (c) https://github.com/MontiCore/monticore */
package de.monticore.od2plantuml;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.nio.charset.StandardCharsets.UTF_8;

import static org.junit.jupiter.api.Assertions.fail;

public class ODPlantUMLTestUtil {
  
  /**
   * Runs the OD tool in a separate JVM process and returns the merged console output.
   * This isolates the JUnit JVM from potential System.exit(...) calls inside the tool.
   */
  public static List<String> runToolInSeparateProcess(String... args) {
    List<String> command = new ArrayList<>();
    command.add(Paths.get(System.getProperty("java.home"), "bin", "java").toString());
    command.add("-cp");
    command.add(System.getProperty("java.class.path"));
    command.add(ODPlantUMLTool.class.getName());
    command.addAll(List.of(args));

    try {
      Process process = new ProcessBuilder(command).redirectErrorStream(true).start();
      List<String> outputLines;
      try (
          BufferedReader reader = new BufferedReader(
              new InputStreamReader(process.getInputStream(), UTF_8))) {
        outputLines = reader.lines().toList();
      }

      boolean finished = process.waitFor(30, TimeUnit.SECONDS);
      if (!finished) {
        process.destroyForcibly();
        fail("Tool process did not finish within timeout.");
      }

      return outputLines;
    }
    catch (Exception e) {
      fail("Running tool in separate process failed: " + e.getMessage());
      return List.of();
    }
  }
}
