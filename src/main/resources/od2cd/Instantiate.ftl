<#-- (c) https://github.com/MontiCore/monticore -->
<#-- @ftlvariable name="tc" type="de.monticore.generating.templateengine.TemplateController" -->
<#-- @ftlvariable name="cd4c" type="de.monticore.cd.methodtemplates.CD4C" -->
<#-- @ftlvariable name="cp" type="de.monticore.od2cd.CompositionPrinter" -->
<#-- @ftlvariable name="type" type="de.monticore.types.mcbasictypes._ast.ASTMCType" -->
<#-- @ftlvariable name="printInfo" type="java.lang.String" -->
<#-- @ftlvariable name="attributes" type="java.util.List<java.lang.String>" -->
<#-- @ftlvariable name="values" type="java.util.List<java.lang.String>" -->
<#-- @ftlvariable name="objectname" type="java.lang.String" -->
${tc.signature("type", "printInfo", "attributes", "values", "objectname")}
${cd4c.method("public ${printInfo} instantiate${objectname?capFirst}()")}

return ${cp.create(type)}
<#list attributes as attribute>
  ${cp.update(attribute, values[attribute?index])}
</#list>
;
