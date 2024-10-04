/* (c) https://github.com/MontiCore/monticore */
package example;

import java.util.Optional;

public class DBuilder extends D {

  public DBuilder i(int i) {
    this.i = i;
    return this;
  }
  
  public Optional<D> build() {
    return Optional.of(this);
  }
}
