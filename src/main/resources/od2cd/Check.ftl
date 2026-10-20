<#-- (c) https://github.com/MontiCore/monticore -->
<#-- @ftlvariable name="tc" type="de.monticore.generating.templateengine.TemplateController" -->
<#-- @ftlvariable name="cd4c" type="de.monticore.cd.methodtemplates.CD4C" -->
<#-- @ftlvariable name="name" type="java.lang.String" -->
<#-- @ftlvariable name="attributes" type="java.util.List<java.lang.String>" -->
<#-- @ftlvariable name="values" type="java.util.List<java.lang.String>" -->
<#-- @ftlvariable name="type" type="java.lang.String" -->
${tc.signature("name", "attributes", "values", "type")}
${cd4c.method("public void check${name?capFirst}(${type?capFirst} ${name})")}

<#list attributes as attribute>
  org.junit.jupiter.api.Assertions.assertEquals(${name}.get${attribute?capFirst}(), ${values[attribute?index]});
</#list>