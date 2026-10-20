<#-- (c) https://github.com/MontiCore/monticore -->
<#-- @ftlvariable name="tc" type="de.monticore.generating.templateengine.TemplateController" -->
<#-- @ftlvariable name="glex" type="de.monticore.generating.templateengine.GlobalExtensionManagement" -->
<#-- @ftlvariable name="converter" type="de.monticore.od2cd.OD2CDConverter" -->
<#-- @ftlvariable name="generator" type="de.monticore.cd.codegen.CDGenerator" -->
<#-- @ftlvariable name="ast" type="de.monticore.odbasis._ast.ASTODArtifact" -->
<#-- @ftlvariable name="cdata" type="de.monticore.od2cd.OD2CDData" -->
${tc.signature("glex", "converter", "hwPath", "generator")}

<!-- ====================================================================
     build classdiagram
-->
<#assign cdata=converter.doConvert(ast, glex)>

<!-- ====================================================================
     call TopDecorator
-->
<#--<#assign topDecorator = tc.instantiate("de.monticore.cd.codegen.TopDecorator", [hwPath])>
${topDecorator.decorate(cdata.getCompilationUnit())}-->

<!-- ====================================================================
     Generate Java-classes
-->
${generator.generate(cdata.getCompilationUnit())}