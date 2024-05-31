/* (c) https://github.com/MontiCore/monticore */

import de.se_rwth.commons.logging.Log;
import de.se_rwth.commons.logging.LogStub;
import example.A;
import example.B;
import examples.ExamplesInstantiator;
import java.util.List;
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
    List<Object> list = new ExamplesInstantiator().instantiate();
    assertEquals(2, list.size());
  }
}