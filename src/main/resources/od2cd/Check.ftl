<#-- (c) https://github.com/MontiCore/monticore -->
${tc.signature("name", "attributes", "values")}
${cd4c.method("public void check${name?capFirst}()")}

<#list attributes as attribute>
  org.junit.Assert.assertEquals(${name}.get${attribute?capFirst}(), ${values[attribute?index]});
</#list>