/*
 * Copyright (c) 2015 RWTH Aachen. All rights reserved.
 *
 * http://www.se-rwth.de/
 */
package de.monticore.lang.od;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.antlr.v4.runtime.RecognitionException;
import org.junit.Test;

import de.monticore.lang.od._ast.ASTODCompilationUnit;
import de.monticore.lang.od._parser.ODParser;
import de.monticore.lang.od.prettyprint.HierachicalODPrettyPrinterConcreteVisitor;
import de.monticore.lang.od.prettyprint.ODPrettyPrinterConcreteVisitor;
import de.monticore.prettyprint.IndentPrinter;
import de.se_rwth.commons.logging.Log;

/**
 * This test compares the ASTs of the files in the examples folder with the
 * pretty-printed versions of these files.
 * 
 * @author Robert Heim, Timo Greifenberg
 */
public class ExamplesTest {
  
  @Test
  public void testQualifiedLinks() throws RecognitionException, IOException {
    Log.enableFailQuick(false);
    test("src/test/resources/examples/od/QualifiedLinks.od", false);
  }
  
  @Test
  public void testAuctionParticipats() throws RecognitionException, IOException {
    test("src/test/resources/examples/od/AuctionParticipants.od", false);
  }
  
  @Test
  public void testVariants() throws RecognitionException, IOException {
    test("src/test/resources/examples/od/Variants.od", false);
  }
  
  @Test
  public void testStereoWithKeyword() throws RecognitionException, IOException {
    test("src/test/resources/examples/od/StereoWithKeyword.od", false);
  }
  
  @Test
  public void testHierarchicalOd() throws RecognitionException, IOException {
    test("src/test/resources/examples/hierarchical/StandardInnerLink.od", true);
  }
  
  private void test(String modelName, boolean hierarchical)
      throws RecognitionException, IOException {
    Path model = Paths.get(modelName);
    ODParser parser = new ODParser();
    Optional<ASTODCompilationUnit> odDef = parser.parseODCompilationUnit(model.toString());
    assertFalse(parser.hasErrors());
    assertTrue(odDef.isPresent());
    
    // pretty print the AST
    ODPrettyPrinterConcreteVisitor pp;
    if (hierarchical) {
      pp = new HierachicalODPrettyPrinterConcreteVisitor(new IndentPrinter());
    }
    else {
      pp = new ODPrettyPrinterConcreteVisitor(new IndentPrinter());
    }
    
    pp.handle(odDef.get());
    String ppResult = pp.getPrinter().getContent();
    System.out.println(ppResult);
    
    // parse the printers content
    Optional<ASTODCompilationUnit> ppOd = parser.parseString_ODCompilationUnit(ppResult);
    
    assertFalse(parser.hasErrors());
    assertTrue(ppOd.isPresent());
    
    // must be equal to original parsed AST.
    assertTrue("pretty printed OD: " + ppResult, odDef.get().deepEquals(ppOd.get()));
    
  }
}
