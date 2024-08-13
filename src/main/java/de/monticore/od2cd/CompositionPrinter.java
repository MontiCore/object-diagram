/* (c) https://github.com/MontiCore/monticore */
package de.monticore.od2cd;

import de.monticore.od4development.OD4DevelopmentMill;
import de.monticore.od4development._symboltable.IOD4DevelopmentArtifactScope;
import de.monticore.od4development._symboltable.IOD4DevelopmentGlobalScope;
import de.monticore.od4development._symboltable.IOD4DevelopmentScope;
import de.monticore.types.mcbasictypes._ast.ASTMCType;

import java.util.List;

public class CompositionPrinter {
  
  public String create(ASTMCType type) {
    
    String res = "example.Mill." + type.printType().toLowerCase() + "Builder()";
    
    IOD4DevelopmentGlobalScope gs = OD4DevelopmentMill.globalScope();
    
    if (gs.getSubScopes().size() == 1) {
      IOD4DevelopmentArtifactScope as = (IOD4DevelopmentArtifactScope) gs.getSubScopes().get(0);
      
      if (as.getTypeSymbols().containsKey(type.printType()) ||
          as.getOOTypeSymbols().containsKey(type.printType()) ||
          as.getUnknownSymbols().containsKey(type.printType())) {
        res = as.getFullName().toLowerCase() + ".";
        res += as.getFullName() + "Manager.";
        res += type.printType().toLowerCase() + "Builder()";
      }
      
    }
    
    return res;
  }
  
  public String read() {
    return "// NA";
  }
  
  public String write(ASTMCType type) {
    IOD4DevelopmentGlobalScope gs = OD4DevelopmentMill.globalScope();
    
    if (gs.getSubScopes().size() == 1) {
      return ".build().get()";
    }
    return ".build()";
  }
  
  public String update(String attribute, String value) {
    IOD4DevelopmentGlobalScope gs = OD4DevelopmentMill.globalScope();
    
    if (gs.getSubScopes().size() == 1) {
      String asName = gs.getSubScopes().get(0).getName();
      
      String res = "";
      String line = "";
      if (attribute.equals("password")) {
        value = "org.springframework.security.crypto.factory.PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(" + value + ")";
        line = "\n  .registrationDate(java.time.LocalDateTime.now().truncatedTo(java.time.temporal.ChronoUnit.SECONDS))";
      } else if (value.startsWith(asName)) {
        value = asName.toLowerCase() + "." + value;
      }
      
      return "." + attribute + "(" + value + ")" + line;
    }
    
    return ".set" + attribute.substring(0,1).toUpperCase() + attribute.substring(1) + "(" + value + ")";
  }
  
}
