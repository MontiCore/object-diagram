<#-- (c) https://github.com/MontiCore/monticore -->
${tc.signature("name", "attributes", "values", "type")}
${cd4c.method("public void check${name?capFirst}(${type?capFirst} ${name})")}

<#list attributes as attribute>
    org.junit.Assert.assertEquals(${name}.get${attribute?capFirst}(), ${values[attribute?index]});
</#list>