/* (c) https://github.com/MontiCore/monticore */
package examples;

import de.se_rwth.commons.logging.Log;

import java.util.Optional;

public class ABuilder {
  
  private Optional<Integer> x;
  
  private Optional<String> s;
  
  public ABuilder setX(int x) {
    this.x = Optional.of(x);
    return this;
  }
  
  public ABuilder setS(String s) {
    this.s = Optional.of(s);
    return this;
  }
  
  public A build() {
    if (!isValid()) {
      Log.error("0x80953 attributes of type A must not be null");
    }
    return new A(x.get(), s.get());
  }
  
  private boolean isValid() {
    return x.isPresent() && s.isPresent();
  }
}
