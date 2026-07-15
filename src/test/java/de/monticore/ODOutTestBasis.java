/* (c) https://github.com/MontiCore/monticore */
package de.monticore;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public abstract class ODOutTestBasis extends ODTestBasis {
  
  /** Captures everything written to {@code System.out} during tests. */
  protected static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

  /** Captures everything written to {@code System.err} during tests. */
  protected static final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
  
  /**
   * Redirects standard output and error streams to in-memory buffers once per test class.
   */
  @BeforeAll
  public static void redirectStreams() {
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));
    outContent.reset();
    errContent.reset();
  }
  
  /**
   * Clears captured output after each test to avoid cross-test interference.
   */
  @AfterEach
  public void resetStreams() {
    outContent.reset();
    errContent.reset();
  }
  
  /**
   * Returns the currently captured standard output.
   *
   * @return captured {@code System.out} content as a string
   */
  protected String getOut() {
    return outContent.toString();
  }
  
  /**
   * Returns the currently captured error output.
   *
   * @return captured {@code System.err} content as a string
   */
  protected String getErr() {
    return errContent.toString();
  }
}
