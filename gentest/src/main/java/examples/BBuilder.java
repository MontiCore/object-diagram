/* (c) https://github.com/MontiCore/monticore */
package examples;

import de.se_rwth.commons.logging.Log;

import java.util.Optional;

public class BBuilder {
  
  private Optional<Boolean> b;
  
  public BBuilder setB(boolean b) {
    this.b = Optional.of(b);
    return this;
  }
  
  public B build() {
    if (!isValid()) {
      Log.error("0x80954 attribute of type B must not be null");
    }
    return new B(b.get());
  }
  
  private boolean isValid() {
    return b.isPresent();
  }
  
}
