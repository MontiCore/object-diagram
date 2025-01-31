/* (c) https://github.com/MontiCore/monticore */
package de.monticore;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ODOutTestBasis extends ODTestBasis {
  
  protected static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  protected static final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
  
  @BeforeAll
  public static void redirectStreams() {
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));
    outContent.reset();
    errContent.reset();
  }
  
  @AfterEach
  public void resetStreams() {
    outContent.reset();
    errContent.reset();
  }
  
  protected String getOut() {
    return outContent.toString();
  }
  
  protected String getErr() {
    return errContent.toString();
  }
}
