/* (c) https://github.com/MontiCore/monticore */
package example;

import java.util.Optional;

public class CBuilder extends C {

  public CBuilder d(D d) {
    this.d = d;
    return this;
  }
  
  public Optional<C> build() {
    return Optional.of(this);
  }

}
