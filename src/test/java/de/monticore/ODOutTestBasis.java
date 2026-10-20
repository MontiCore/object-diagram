/* (c) https://github.com/MontiCore/monticore */
package de.monticore;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Captures {@code System.out} and {@code System.err} during each test. The original streams are
 * restored after each test.
 */
public abstract class ODOutTestBasis extends ODTestBasis {

  /** Captures everything written to {@code System.out} during the current test. */
  protected final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

  /** Captures everything written to {@code System.err} during the current test. */
  protected final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

  private PrintStream originalOut;

  private PrintStream originalErr;

  /**
   * Redirects standard output and error streams to in-memory buffers for the current test.
   */
  @BeforeEach
  public void redirectStreams() {
    originalOut = System.out;
    originalErr = System.err;
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));
  }

  /**
   * Restores the original standard output and error streams.
   */
  @AfterEach
  public void restoreStreams() {
    System.setOut(originalOut);
    System.setErr(originalErr);
  }

  /**
   * Returns the standard output captured during the current test.
   *
   * @return captured {@code System.out} content as a string
   */
  protected String getOut() {
    return outContent.toString();
  }

  /**
   * Returns the error output captured during the current test.
   *
   * @return captured {@code System.err} content as a string
   */
  protected String getErr() {
    return errContent.toString();
  }
}
