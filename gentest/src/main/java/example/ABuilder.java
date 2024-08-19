/* (c) https://github.com/MontiCore/monticore */
package example;

import de.se_rwth.commons.logging.Log;

import java.util.Optional;

public class ABuilder extends A {


  public ABuilder x(int x) {
    this.x = x;
    return this;
  }

  public ABuilder s(String s) {
    this.s = s;
    return this;
  }

  public Optional<A> build() {
    return Optional.of(this);
  }

}
