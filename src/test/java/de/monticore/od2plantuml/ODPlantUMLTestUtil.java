/* (c) https://github.com/MontiCore/monticore */
package de.monticore.od2plantuml;

import de.monticore.ToolProcessRunner;

import java.nio.file.Path;
import java.util.List;

public class ODPlantUMLTestUtil {

  /**
   * Runs the OD tool in a separate JVM process and returns the merged console output.
   *
   * @see ToolProcessRunner
   */
  public static List<String> runToolInSeparateProcess(String... args) {
    return runToolInSeparateProcess(null, args);
  }

  /**
   * Runs the OD tool in a separate JVM process with the given working directory and returns the
   * merged console output.
   *
   * @param workingDir working directory of the process, {@code null} for the current one
   * @see ToolProcessRunner
   */
  public static List<String> runToolInSeparateProcess(Path workingDir, String... args) {
    return ToolProcessRunner.run(ODPlantUMLTool.class, workingDir, args);
  }
}
