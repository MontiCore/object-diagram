/*
 * ******************************************************************************
 * MontiCore Language Workbench, www.monticore.de
 * Copyright (c) 2017, MontiCore, All rights reserved.
 *
 * This project is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this project. If not, see <http://www.gnu.org/licenses/>.
 * ******************************************************************************
 */

package de.monticore.lang.odbasics;

import de.monticore.lang.odbasics._ast.ASTODArtifact;
import de.monticore.lang.odbasics._cocos.ODBasicsCoCoChecker;
import de.monticore.lang.odbasics._parser.ODBasicsParser;
import de.monticore.lang.odbasics._symboltable.ODBasicsLanguage;
import de.monticore.lang.odbasics.cocos.ODBasicsCoCos;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by TGr on 29.04.2016.
 */
public class ODBasicsCLI {

  private static final String JAR_NAME = "od-<version>-odBasics-cli.jar";

  private static final String PARSING_SUCCESSFUL = "Parsing Successful!";

  public static void main(String[] args) throws IOException {
    ODBasicsCLI cli = new ODBasicsCLI();
    cli.run(args);
  }

  private void run(String[] args) throws IOException {
    handleArgs(args);
  }

  private void handleArgs(String[] args) throws IOException {
    if (args.length != 1) {
      printWrongUsage();
    }
    else {
      if ("-h".equals(args[0])) {
        printUsage();
      }
      else {
        doParse(args[0]);
      }
    }
  }

  private void doParse(String file) throws IOException {
    ODBasicsLanguage odLanguage = new ODBasicsLanguage();

    ODBasicsCoCoChecker odCoCoChecker = new ODBasicsCoCos().getCheckerForAllCoCos();

    ODBasicsParser parser = odLanguage.getParser();

    Optional<ASTODArtifact> odDef = parser.parseODArtifact(file);

    odDef.ifPresent(odCoCoChecker::checkAll);

    System.out.println(PARSING_SUCCESSFUL);
  }

  private void printUsage() {
    System.out.println("Usage: " + JAR_NAME + " [OPTION] [ODFILE]");
    System.out.println(
        "Parses the ODFILE. Displays parsing error or \"" + PARSING_SUCCESSFUL + "\" message\n");
    System.out.println("  -h   display this help and exit");
  }

  private void printWrongUsage() {
    System.out.println("ODCLI: wrong usage");
    System.out.println("For more information type: ODCLI -h");
  }

}
