/* (c) https://github.com/MontiCore/monticore */

import examples.A;
import examples.B;
import examples.ExamplesChecker;
import examples.ExamplesInstantiator;
import org.junit.Test;

import java.util.List;

public class GeneratedClassesTest {

  @Test
  public void test() {
    List<Object> list = new ExamplesInstantiator().instantiate();
    new ExamplesChecker().checkFoo((A)list.get(0));
    new ExamplesChecker().checkBar((B)list.get(1));
  }
}