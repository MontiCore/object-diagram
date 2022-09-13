<#-- (c) https://github.com/MontiCore/monticore -->
${tc.signature("type", "attributes", "values")}
${cd4c.method("public ${type} instantiate()")}

return FooMill.${type?uncapFirst}Builder()
<#list attributes as attribute>
  .set${attribute?capFirst}(${values[attribute?index]})
</#list>
.build();