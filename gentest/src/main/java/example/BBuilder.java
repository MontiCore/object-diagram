/* (c) https://github.com/MontiCore/monticore */
package example;

import de.se_rwth.commons.logging.Log;

import java.util.Optional;

public class BBuilder extends B {


  public BBuilder b(boolean b) {
    this.b = b;
    return this;
  }

  public Optional<B> build() {
    return Optional.of(this);
  }


}
