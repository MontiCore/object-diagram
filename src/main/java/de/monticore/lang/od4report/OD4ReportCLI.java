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

package de.monticore.lang.od4report;

import de.monticore.lang.od4report._cocos.OD4ReportCoCoChecker;
import de.monticore.lang.od4report._parser.OD4ReportParser;
import de.monticore.lang.od4report._symboltable.OD4ReportLanguage;
import de.monticore.lang.od4report._cocos.OD4ReportCoCos;
import de.monticore.lang.odbasics._ast.ASTODArtifact;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by TGr on 29.04.2016.
 */
public class OD4ReportCLI {

  private static final String JAR_NAME = "od-<version>-od4Report-cli.jar";

  private static final String PARSING_SUCCESSFUL = "Parsing Successful!";

  public static void main(String[] args) throws IOException {
    OD4ReportCLI cli = new OD4ReportCLI();
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
    OD4ReportLanguage odLanguage = new OD4ReportLanguage();

    OD4ReportCoCoChecker odCoCoChecker = new OD4ReportCoCos().getCheckerForAllCoCos();

    OD4ReportParser parser = odLanguage.getParser();

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
