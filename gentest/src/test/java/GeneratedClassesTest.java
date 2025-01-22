/* (c) https://github.com/MontiCore/monticore */

import de.se_rwth.commons.logging.Log;
import de.se_rwth.commons.logging.LogStub;
import example.A;
import example.B;
import examples.ExamplesInstantiator;
import java.util.List;

import examples.ExamplesODInstances;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GeneratedClassesTest {

  @Before
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