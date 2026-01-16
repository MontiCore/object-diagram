/* (c) https://github.com/MontiCore/monticore */

import de.se_rwth.commons.logging.Log;
import de.se_rwth.commons.logging.LogStub;
import example.A;
import example.B;
import examples.ExamplesInstantiator;
import java.util.List;

import examples.ExamplesODInstances;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GeneratedClassesTest {

  @BeforeEach
  public void setup() {
    LogStub.init();
    Log.enableFailQuick(false);
  }

  @Test
  public void test() {
    ExamplesODInstances objs = new ExamplesInstantiator().instantiate();
    assertEquals(5, objs.getFoo().getX());
    assertEquals("hello", objs.getFoo().getS());
  }
}