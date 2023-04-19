/* (c) https://github.com/MontiCore/monticore */

import de.se_rwth.commons.logging.Log;
import de.se_rwth.commons.logging.LogStub;
import examples.A;
import examples.B;
import examples.ExamplesChecker;
import examples.ExamplesInstantiator;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class GeneratedClassesTest {

  @Before
  public void setup() {
    LogStub.init();
    Log.enableFailQuick(false);
  }

  @Test
  public void test() {
    List<Object> list = new ExamplesInstantiator().instantiate();
    new ExamplesChecker().checkFoo((A)list.get(0));
    new ExamplesChecker().checkBar((B)list.get(1));
  }
}